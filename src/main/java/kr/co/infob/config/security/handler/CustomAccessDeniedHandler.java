package kr.co.infob.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.utils.RequestUtil;
import kr.co.infob.common.vo.Response;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		log.info("[CustomAccessDeniedHandler] :: {}", accessDeniedException.getMessage());
		log.info("[CustomAccessDeniedHandler] :: {}", request.getRequestURL());
		log.info("[CustomAccessDeniedHandler] :: 리소스에 접근권한이 없습니다.");

		Response<?> result = Response.error("You do not have permission to access that resource.", HttpStatus.FORBIDDEN);
		RequestUtil.printJsonResponse(response, result);

	}
}
