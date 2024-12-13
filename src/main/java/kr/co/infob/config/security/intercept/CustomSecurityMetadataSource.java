package kr.co.infob.config.security.intercept;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.infob.config.security.intercept.http.SimpleHttpServletRequest;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private final Map<RequestMatcher, List<ConfigAttribute>> requestMap;

	public CustomSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> destMap) {
    	this.requestMap = destMap;
    }

//	public Collection<ConfigAttribute> getAttributes(String path) throws IllegalArgumentException {
//
//		Collection<ConfigAttribute> result = null;
//
//		SimpleHttpServletRequest mockRequest = null;
//		for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()){
//
//			mockRequest = new SimpleHttpServletRequest(path);
//
//			boolean match = entry.getKey().matches(mockRequest);
//
//			if(match) {
//				result = entry.getValue();
//				break;
//			}
//		}
//		return result;
//	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		HttpServletRequest request = null;
		if( object instanceof String ) {

			ObjectMapper mapper = new ObjectMapper();
			try {

				JsonNode pathNode = mapper.readTree((String)object);
				if( pathNode.has("pathname") ) {
					request = new SimpleHttpServletRequest(pathNode.get("pathname").asText());
				} else {
					return null;
				}
			} catch (JsonProcessingException e) {
				return null;
			}

		} else {

			RequestAuthorizationContext requestAuthorizationContext = (RequestAuthorizationContext)object;
			request = requestAuthorizationContext.getRequest();
		}

		Collection<ConfigAttribute> result = null;
		for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()){

			boolean match = entry.getKey().matches(request);

			if(match) {
				result = entry.getValue();
				break;
			}
		}
		return result;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<>();
		for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()){
			allAttributes.addAll(entry.getValue());
		}
		return allAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}