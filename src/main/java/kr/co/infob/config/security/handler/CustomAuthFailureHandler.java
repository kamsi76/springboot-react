package kr.co.infob.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.utils.RequestUtil;
import kr.co.infob.common.vo.Response;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		log.error(exception.getMessage(), exception);

		Response<?> res = Response.error("invalid_credentials", exception.getMessage(), HttpStatus.UNAUTHORIZED);
		RequestUtil.printJsonResponse(response, res);

	}

}
