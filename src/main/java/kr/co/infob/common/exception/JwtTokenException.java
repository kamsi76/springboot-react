package kr.co.infob.common.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.utils.RequestUtil;
import kr.co.infob.common.vo.Response;
import kr.co.infob.config.security.vo.TokenVo;

@SuppressWarnings("serial")
public class JwtTokenException extends Exception {

	private ErrorCase errorCase;

	private TokenVo token;

    public enum ErrorCase {
        NO_ACCESS("Not found access token."),
        BAD_ACCESS("Invalid access token."),
        NO_REFRESH("Not found refresh token."),
        BAD_REFRESH("Invalid refresh token."),
        BLACKLIST("Blacklist refresh token.");

    	private final String message;

    	ErrorCase(String message) {
    		this.message = message;
    	}

    	public String getMessage() {
    		return message;
    	}
    }

    public JwtTokenException(ErrorCase errorCase) {
        super(errorCase.name());
        this.errorCase = errorCase;
    }

    public JwtTokenException(ErrorCase errorCase, TokenVo token) {
    	super(errorCase.name());
        this.errorCase = errorCase;

        this.token = token;
    }

    public void sendResponseError(HttpServletResponse response) {

    	/*
         * Access Token이 없는 경우
         * 	다시 Fefresh Token고 비교 할 수 있도록 401로 반환하고
         * 	나머지는 올바르지 않은 요청으로 로그인 화면으로 이동하도록 처리한다.
         */
    	HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if( "NO_ACCESS".equals(errorCase.name()) ) {
        	httpStatus = HttpStatus.UNAUTHORIZED;
        }

        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Response<?> result = Response.error(errorCase.name(), errorCase.getMessage(), httpStatus, token);
		try {
			RequestUtil.printJsonResponse(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}
