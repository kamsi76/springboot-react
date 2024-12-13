package kr.co.infob.common.vo;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Response<T> {

	private int status;

	private String error;

	private String message;

	private T data;

	@Builder
    public Response(int status, String error, String message, T data) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * 단순 에러 메시지 전달
     * @param <T>
     * @param message
     * @return
     */
    public static <T> Response<T> error(String message) {
        return error(null, message, null, null);
    }

    /**
     * 에러 정보와 에러 메시지 전달
     * HttpStatus 코드는 무조건 500으로 리턴한다.
     * @param <T>
     * @param error
     * @param message
     * @return
     */
    public static <T> Response<T> error(String error, String message) {
    	return error(error, message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * 에러 메시지와 HttpStatus 상태값
     * @param <T>
     * @param message
     * @param status
     * @return
     */
    public static <T> Response<T> error(String message, HttpStatus status) {
    	return error(null, message, status, null);
    }

    /**
     * 에러 정보와 메시지 그리고 HttpStatus 값을 모두 전달
     * @param <T>
     * @param error
     * @param message
     * @param status
     * @return
     */
    public static <T> Response<T> error(String error, String message, HttpStatus status) {
    	return error(error, message, status, null);
    }

    /**
     * 에러 메시지와 HttpStatus 상태값과 전송하고자 하는 Data를 전달한다.
     * @param <T>
     * @param message
     * @param status
     * @param data
     * @return
     */
    public static <T> Response<T> error(String message, HttpStatus status, T data) {
    	return error(null, message, status, data);
    }

    /**
     * 에러 정보와 메시지, HttpStatus 상태 및 Data 모두 전달
     * @param <T>
     * @param error
     * @param message
     * @param status
     * @param data
     * @return
     */
    public static <T> Response<T> error(String error, String message, HttpStatus status, T data) {

    	int httpStatus = 500;
    	if( status != null ) httpStatus = status.value();

        return Response.<T>builder()
                .status(httpStatus)
                .error(error)
                .message(message)
                .data(data)
                .build();
    }

    public int getStatus() {
    	return this.status;
    }

}
