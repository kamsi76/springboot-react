package kr.co.infob.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.utils.RequestUtil;
import kr.co.infob.common.vo.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		log.error(authException.getMessage(), authException);

		log.debug("[CustomAuthenticationEntryPoint] :: {}", authException.getMessage());
        log.debug("[CustomAuthenticationEntryPoint] :: {}", request.getRequestURL());
        log.debug("[CustomAuthenticationEntryPoint] :: " + authException.getMessage());

		Response<?> result = Response.error(authException.getMessage(), HttpStatus.FORBIDDEN);
		RequestUtil.printJsonResponse(response, result);
	}
}
