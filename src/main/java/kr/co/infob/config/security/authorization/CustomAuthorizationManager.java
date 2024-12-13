package kr.co.infob.config.security.authorization;

import java.util.Collection;
import java.util.function.Supplier;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.CollectionUtils;

import kr.co.infob.config.security.intercept.CustomSecurityMetadataSource;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	private final CustomSecurityMetadataSource customSecurityMetadataSource;


	public CustomAuthorizationManager(CustomSecurityMetadataSource customSecurityMetadataSource) {
		this.customSecurityMetadataSource = customSecurityMetadataSource;
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		try {

			Collection<ConfigAttribute> attributes = customSecurityMetadataSource.getAttributes(object);
			if( CollectionUtils.isEmpty(attributes) ) {
				return new AuthorizationDecision(false);
			}

			Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();

			boolean hasAuthority =  attributes.stream()
			        .anyMatch(attribute -> authorities.stream()
			        	.anyMatch(authority ->
			        				attribute.getAttribute().equals(authority.getAuthority())
			        			)
			        );

			return new AuthorizationDecision(hasAuthority);

		} catch (AccessDeniedException e) {

			return new AuthorizationDecision(false);
		}
	}

}
