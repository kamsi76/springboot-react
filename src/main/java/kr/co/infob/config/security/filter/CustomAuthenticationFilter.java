package kr.co.infob.config.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.database.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	/**
	 * 지정된 URL로 form 전송을 하였을 경우 파라미터 정보를 가져온다.
	 *
	 * @param request  from which to extract parameters and perform the authentication
	 * @param response the response, which may be needed if the implementation has to do a redirect as part of a multi-stage authentication process (such as OpenID).
	 * @return Authentication {}
	 * @throws AuthenticationException {}
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken authRequest;
		try {

			authRequest = getAuthRequest(request);
			setDetails(request, authRequest);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	/**
	 * Request로 받은 ID와 패스워드 기반으로 토큰을 발급한다.
	 *
	 * @param request HttpServletRequest
	 * @return UsernamePasswordAuthenticationToken
	 * @throws Exception e
	 */
	private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {

		try {

			JsonFactory factory = new JsonFactory();
			factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

			ObjectMapper mapper = new ObjectMapper(factory);

			UserInfo user = mapper.readValue(request.getInputStream(), UserInfo.class);
			log.debug("CustomAuthenticationFilter :: userId:" + user.getUserId() + " userPw:" + user.getPasswd());

			// ID와 암호화된 패스워드를 기반으로 토큰 발급
			return new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPasswd());

		} catch (UsernameNotFoundException ae) {
			throw new UsernameNotFoundException(ae.getMessage());
		}
	}

}
