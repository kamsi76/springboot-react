package kr.co.infob.common.utils;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.common.vo.Response;

public class RequestUtil {


	private static BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();


	public static boolean equalPassword(String password, String encodedPassword) {
    	return bpe.matches(password, encodedPassword);
    }

	/**
	 * Response 값을 사용자에게 JSON 형태로 출력해 준다.
	 * @param <T>
	 * @param httpServletResponse
	 * @param response
	 * @throws IOException
	 */
	public static <T> void printJsonResponse(HttpServletResponse httpServletResponse, Response<T> response) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		httpServletResponse.setStatus(HttpStatus.OK.value());
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpServletResponse.getWriter().print(mapper.writeValueAsString(response));
		httpServletResponse.getWriter().flush();
		httpServletResponse.getWriter().close();
	}
}
