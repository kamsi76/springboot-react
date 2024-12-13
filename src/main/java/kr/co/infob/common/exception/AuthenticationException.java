package kr.co.infob.common.exception;

/**
 * 로그인하려 하는 사용자의 정보가 일치하지 않을 경우 발생하는 오류
 */
@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String msg) {
		super(msg);
	}
}
