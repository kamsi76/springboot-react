package kr.co.infob.config.security.provider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration.access}")
	private long accessTokenExpiration;

	@Value("${jwt.expiration.refresh}")
	private long refreshTokenExpiration;

	private SecretKey key;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	/**
	 * token 사용자 모든 속성 정보 조회
	 *
	 * @param token JWT
	 * @return All Claims
	 */
	public Claims getAllClaimsFromToken(String token) {

		return Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload();
	}

	/**
	 * 토큰 유효성 검토
	 *
	 * @param token
	 * @return
	 */
	public Boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

			return true;

		} catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
				| IllegalArgumentException e) {
			log.warn("JWT Exception: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * token 사용자 속성 정보 조회
	 *
	 * @param token          JWT
	 * @param claimsResolver Get Function With Target Claim
	 * @param <T>            Target Claim
	 * @return 사용자 속성 정보
	 */
	private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
		// token 유효성 검증
		if (Boolean.FALSE.equals(validateToken(token)))
			return null;

		final Claims claims = getAllClaimsFromToken(token);

		return claimsResolver.apply(claims);
	}

	/**
	 * token Username 조회
	 *
	 * @param token JWT
	 * @return token Username
	 */
	public String getUsernameFromToken(final String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * 토큰 만료 일자 조회
	 *
	 * @param token JWT
	 * @return 만료 일자
	 */
	public Date getExpirationDateFromToken(final String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * 토큰 만료 일자 조회
	 *
	 * @param token JWT
	 * @return 만료 일자
	 */
	public long getExpirationTimeFromToken(final String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.getTime();
	}

	/**
	 * 토큰 만료여부 조회
	 *
	 * @param token
	 * @return
	 */
	public Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * 토큰 만료 잔여 시간
	 *
	 * @param token
	 * @return
	 */
	public long getRemainMilliSeconds(String token) {
		Date expiration = getExpirationDateFromToken(token);
		Date now = new Date();
		return expiration.getTime() - now.getTime();
	}

	/**
	 * JWT token 생성
	 *
	 * @param id     token 생성 id
	 * @param claims token 생성 claims
	 * @return token
	 */
	private String doGenerateToken(final String id, final Map<String, Object> claims, final long expireTime) {

		Date currentDate = new Date();
		Date accessTokenExpiresIn = new Date(currentDate.getTime() + expireTime);

		return Jwts.builder()
					.subject(id)
					.claims(claims)
					.issuedAt(new Date())
					.expiration(accessTokenExpiresIn)
					.signWith(key).compact();
	}

	/**
	 * Spring Security 인증정보를 통한 생성
	 *
	 * @param authentication
	 * @return
	 */
	public String generateAccessToken(Authentication authentication) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String id = token.getName();
		return generateAccessToken(id);
	}

	/**
	 * access token 생성
	 *
	 * @param id token 생성 id
	 * @return access token
	 */
	public String generateAccessToken(final String id) {
		return generateAccessToken(id, new HashMap<>());
	}

	/**
	 * access token 생성
	 *
	 * @param id     token 생성 id
	 * @param claims token 생성 claims
	 * @return access token
	 */
	public String generateAccessToken(final String id, final Map<String, Object> claims) {
		return doGenerateToken(id, claims, accessTokenExpiration);
	}

	/**
	 * Spring Security 인증정보를 통한 Refresh Token 생성
	 *
	 * @param authentication
	 * @return
	 */
	public String generateRefreshToken(Authentication authentication) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String id = token.getName();
		return generateRefreshToken(id);
	}

	/**
	 * refresh token 생성
	 *
	 * @param id token 생성 id
	 * @return refresh token
	 */
	public String generateRefreshToken(final String id) {
		return generateRefreshToken(id, new HashMap<>());
	}

	/**
	 * refresh token 생성
	 *
	 * @param id     token 생성 id
	 * @param claims token 생성 claims
	 * @return refresh token
	 */
	public String generateRefreshToken(final String id, final Map<String, Object> claims) {
		return doGenerateToken(id, new HashMap<>(), refreshTokenExpiration);
	}

	/**
	 * Header에 저장된 Token 정보 조회
	 *
	 * @param header
	 * @return
	 */
	public String getHeaderToken(String header) {
		String token = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
		}

		return token;
	}

}
