package kr.co.infob.api.auth.service;

import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.infob.api.auth.mapper.AuthMapper;
import kr.co.infob.api.auth.vo.UserVo;
import kr.co.infob.common.database.entity.UserInfo;
import kr.co.infob.common.database.mapper.UserInfoMapper;
import kr.co.infob.config.security.intercept.CustomSecurityMetadataSource;
import kr.co.infob.config.security.provider.JwtTokenProvider;
import kr.co.infob.config.security.service.SecurityService;
import kr.co.infob.config.security.vo.UserDetailsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

	private final AuthMapper authMapper;
	private final UserInfoMapper userInfoMapper;

	private final SecurityService securityService;
	private final JwtTokenProvider tokenProvider;
	private final CustomSecurityMetadataSource securityMetadataSource;


	public void insertUserInfo(UserInfo user) {
		userInfoMapper.insert(user);
	}

	public UserVo login(UserInfo userInfo) {
		UserVo user = authMapper.getUser(userInfo.getUserId());
		return user;
	}


	public String reissueAccessToken(String refreshToken) {

		return null;
	}

	/**
	 * Path에 해당하는 경로에 대한 현재 사용자의 권한이 존제하는지 여부 판단
	 */
	public boolean authroize(HttpServletRequest request, String path) {

		log.debug("Target Path : " + path);

		//사용자 정보를 조회 하여 사용자가 가지고 있는 role 정보를 조회 한다.
		//메뉴에 대한 Role 정보를 조회한다.
		//메뉴 Role 정보와 사용자 Role 정보가 일치하지는 겂이 없으면 false을 반환한다.
		String headerAccessToken = tokenProvider.getHeaderToken(request.getHeader("Authorization"));
		boolean validAccessToken = tokenProvider.validateToken(headerAccessToken);

		UserDetailsVo userDetails = null;

		//토큰이 있으면
		if(StringUtils.hasText(headerAccessToken) && validAccessToken ) {
			String username = tokenProvider.getUsernameFromToken(headerAccessToken);
			userDetails = securityService.loadUserByUsername(username);
		}

		if( userDetails == null ) {
			userDetails = new UserDetailsVo();
			userDetails.addRole("ANONYMOUS");
		}

		Collection<? extends GrantedAuthority> userGrants = userDetails.getAuthorities();
		Collection<ConfigAttribute> urlGrants = securityMetadataSource.getAttributes(path);

		if( CollectionUtils.isEmpty(userGrants) || CollectionUtils.isEmpty(urlGrants) ) {
			return false;
		}

		for (GrantedAuthority userGrant : userGrants) {
			for( ConfigAttribute urlGrant : urlGrants ) {
				if( userGrant.getAuthority().equals(urlGrant.toString()) )
					return true;
			}
		}

		return false;
	}

}
