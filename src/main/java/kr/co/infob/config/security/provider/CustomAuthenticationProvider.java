package kr.co.infob.config.security.provider;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import kr.co.infob.common.utils.RequestUtil;
import kr.co.infob.config.security.service.SecurityService;
import kr.co.infob.config.security.vo.UserDetailsVo;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private SecurityService securityService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
        String password = (String)authentication.getCredentials();

		UserDetailsVo user = securityService.loadUserByUsername(username);

		if( user == null || !user.getUsername().equalsIgnoreCase(username)) {
            throw new BadCredentialsException("401");
        }

        if( !RequestUtil.equalPassword(password, user.getPasswd()) ) {
            throw new BadCredentialsException("401");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, password, authorities);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
