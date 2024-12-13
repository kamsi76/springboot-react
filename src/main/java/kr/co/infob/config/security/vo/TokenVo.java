package kr.co.infob.config.security.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenVo {

	private String accessToken;

	private String refreshToken;

	public static TokenVo of(String accessToken, String refreshToken) {
        return TokenVo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}