package kr.co.infob.common.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.exception.AuthenticationException;
import kr.co.infob.common.vo.Response;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GLobalExceptionHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<Response<?>> handleNoHandlerFound(NoHandlerFoundException e) {

		log.error(e.getMessage(), e);

		log.info("[JwtAccessDeniedHandler] :: {}", e.getMessage());
        log.info("[JwtAccessDeniedHandler] :: {}", e.getRequestURL());
        log.info("[JwtAccessDeniedHandler] :: Not found page.");

        // JSON 형식의 오류 응답
		Response<?> result = Response.error(e.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(result);  // 404 상태 코드와 JSON 반환
    }

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Response<?>> handlerNotAllowedFileExtException(HttpServletResponse response, AuthenticationException e) throws IOException {
		//log.error(e.getMessage(), e);
		Response<?> result = Response.error(e.getMessage(), HttpStatus.BAD_REQUEST); //400
		return ResponseEntity.status(HttpStatus.OK).body(result);  // 404 상태 코드와 JSON 반환
	}
}
