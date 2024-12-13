package kr.co.infob.config.security.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

	private final StringRedisTemplate redisTemplate;

	/**
	 * Refresh Token 저장
	 * @param username
	 * @param refreshToken
	 * @param duration
	 */
    public void saveRefreshToken(String username, String refreshToken, long duration) {
        redisTemplate.opsForValue().set(username, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    /**
     * Refresh Token 조회
     * @param username
     * @return
     */
    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get(username);
    }

    /**
     * Access Token 블랙리스트 추가
     * @param token
     * @param duration
     */
    public void addToBlacklist(String token, long duration) {
        redisTemplate.opsForValue().set(token, "BLACKLIST", duration, TimeUnit.MILLISECONDS);
    }

	/**
	 * 블랙리스트 확인
	 * @param accessToken
	 * @return
	 */
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }

}