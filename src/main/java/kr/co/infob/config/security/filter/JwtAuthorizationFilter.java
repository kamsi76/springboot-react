package kr.co.infob.config.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.exception.JwtTokenException;
import kr.co.infob.common.exception.JwtTokenException.ErrorCase;
import kr.co.infob.config.security.provider.JwtTokenProvider;
import kr.co.infob.config.security.service.RedisService;
import kr.co.infob.config.security.service.SecurityService;
import kr.co.infob.config.security.vo.TokenVo;
import kr.co.infob.config.security.vo.UserDetailsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtAuthenticationFilter 역할
 * JWT Token을 이용하여 인증 및 권한 부여를 처리하는 필터
 *
 * 1. 요청 해더에서 JWT 토튼 추출
 * 2. JWT 검증처리(Token 유효성, 사용자 정보, 만료여부)
 * 3. 토큰은 있는데 SecurityContext에 없는 경우 사용자 정보를 조회해서 SecurityContext에 저장
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final SecurityService securityService;
	private final RedisService redisService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {

			HttpServletRequest httpServletRequest = (HttpServletRequest) request;

			if( "/api/v1/auth/signin".equals(request.getRequestURI()) ) {
				filterChain.doFilter(request, response);
				return;
			}

			String headerAccessToken = jwtTokenProvider.getHeaderToken(httpServletRequest.getHeader("Authorization"));
			String headerRefreshToken = jwtTokenProvider.getHeaderToken(httpServletRequest.getHeader("x-refresh-token"));


			boolean validAccessToken = jwtTokenProvider.validateToken(headerAccessToken);
			boolean validRefeshToken = jwtTokenProvider.validateToken(headerRefreshToken);

			// Refresh Token 유효성이 false 이면 Token 검증을 하지 않는다.
			if( !validRefeshToken ) {
				filterChain.doFilter(request, response);
				return;
			}

			/*
			 * AccessToken 또는 refreshToken이 살아 있는 경우
			 *
			 * 1. refreshToken이 Blacklist에 포함되어 있는지 여부 확인 포함되어 있으면 다시 로그인 처리 필요(403)
			 *
			 * 2. AccessToken 존재 및 적합성, 만료여부 확인
			 * 	정상 :
			 * 		1-1. refreshToken과 accessToken 아이디가 일치여부 확인 일치하지 않은 경우 다시 로그인 필요(403)
			 * 		1-2. AccessToken이 있고 AccessToken이 유효한 Token이면 다음 필터로 이동
			 *	비정상 : 1. accessToken 재 생성
			 */
			if (StringUtils.hasText(headerAccessToken) || StringUtils.hasText(headerRefreshToken)) {

				if (redisService.isBlacklisted(headerRefreshToken)) {
					throw new JwtTokenException(ErrorCase.BLACKLIST);
				}

				String username = jwtTokenProvider.getUsernameFromToken(headerAccessToken);;

				// 정상인 경우
				if (StringUtils.hasText(username) && validAccessToken) {

					username = jwtTokenProvider.getUsernameFromToken(headerAccessToken);

					// accessToken과 headerRefreshToken을 사용하여 비교 처리해서 문제가 있는 경우 다시 로그인 처리
					if (!validateRefresh(username, headerRefreshToken)) {
						throw new JwtTokenException(ErrorCase.BAD_REFRESH);
					}

					String refreshUsername = jwtTokenProvider.getUsernameFromToken(headerRefreshToken);
					UserDetailsVo userDetails = securityService.loadUserByUsername(refreshUsername);

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);

					filterChain.doFilter(request, response);

				} else {

					if (!validateRefresh(null, headerRefreshToken)) {
						throw new JwtTokenException(ErrorCase.BAD_REFRESH);
					}

					/*
					 * refreshToken 정보가 정상이면 accessToken을 재발급한다.
					 */
					String refreshUsername = jwtTokenProvider.getUsernameFromToken(headerRefreshToken);
					UserDetailsVo userDetails = securityService.loadUserByUsername(refreshUsername);

					if (!ObjectUtils.isEmpty(userDetails)) {

						String accessToken = jwtTokenProvider.generateAccessToken(userDetails.getUsername());

						// 사용자에게 전달
						TokenVo token = TokenVo.builder().accessToken(accessToken).build();

						throw new JwtTokenException(ErrorCase.REISSUE_REFRESH, token);
					} else {
						throw new JwtTokenException(ErrorCase.NOT_FOUND_USER);
					}
				}
			}

			filterChain.doFilter(request, response);

		} catch (JwtTokenException e) {
			log.error(e.getMessage(), e);
			e.sendResponseError(response);
		}

	}

	/**
	 * RefreshToken 유효성 체크 refreshToken의 유효성을 체크한다.
	 * accessToken이 있는 경우 accessToken과 비교를 하고 없는 경우 refreshToken 자체로 비교한다.
	 *
	 * @param accessUsername AccessToken username
	 * @param headerRefreshToken
	 * @return
	 */
	private boolean validateRefresh(String accessUsername, String headerRefreshToken) {

		if (!StringUtils.hasText(headerRefreshToken))
			return false;

		/*
		 * 1. Header RefreshToken Blacklist 등록 여부와 유효성 체크 및 만료 여부 체크
		 * 	1-1 정상이면
		 * 		1-1-2. Redis의 refreshToken과 header refreshToken의 username이 동일한지 확인
		 * 2. AccessToken의 username 있는 경우 AccessToken username과 RefreshToken의 username이 같은지 확인
		 */
		String refreshUsername = jwtTokenProvider.getUsernameFromToken(headerRefreshToken);

		if (StringUtils.hasText(refreshUsername) ) {
			String refreshToken = redisService.getRefreshToken(refreshUsername);
			if (!headerRefreshToken.equals(refreshToken)) {
				return false;
			}
		}

		if (StringUtils.hasText(accessUsername)) {
			if (!accessUsername.equals(refreshUsername)) {
				return false;
			}
		}

		return true;
	}
}
