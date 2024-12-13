package kr.co.infob.config.security.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.utils.RequestUtil;
import kr.co.infob.common.vo.Response;
import kr.co.infob.config.security.provider.JwtTokenProvider;
import kr.co.infob.config.security.service.RedisService;
import kr.co.infob.config.security.vo.TokenVo;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인이 성공한 경우 처리하는 Handler
 * 사용자 정보 token에서 인증정보를 통하여
 * 	AccessToken과 RefreshToken 을 생성하고
 * 	RefreshToken을 Redis에 저장하고 사용자에게 Token 정보를 전달한다.
 */
@Component
@Slf4j
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private RedisService redisService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken)authentication;

		String accessToken = jwtTokenProvider.generateAccessToken(authentication);
		String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

		//이전에 refresh token이 있는 경우 이전 refresh token은 Blacklist에 등록한다.
		String refreshUsername = jwtTokenProvider.getUsernameFromToken(refreshToken);
		String redisToken = redisService.getRefreshToken(refreshUsername);
		if( StringUtils.hasText(redisToken) ) {
			redisService.addToBlacklist(redisToken, jwtTokenProvider.getExpirationTimeFromToken(refreshToken));
		}

		redisService.saveRefreshToken(user.getName(), refreshToken, jwtTokenProvider.getExpirationTimeFromToken(refreshToken));

		Response<?> res = Response.success(TokenVo.of(accessToken, refreshToken));
		RequestUtil.printJsonResponse(response, res);
	}

}
