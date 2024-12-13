package kr.co.infob.config.security;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import kr.co.infob.config.security.authorization.CustomAuthorizationManager;
import kr.co.infob.config.security.filter.CustomAuthenticationFilter;
import kr.co.infob.config.security.filter.JwtAuthorizationFilter;
import kr.co.infob.config.security.handler.CustomAccessDeniedHandler;
import kr.co.infob.config.security.handler.CustomAuthFailureHandler;
import kr.co.infob.config.security.handler.CustomAuthSuccessHandler;
import kr.co.infob.config.security.handler.CustomAuthenticationEntryPoint;
import kr.co.infob.config.security.intercept.CustomSecurityMetadataSource;
import kr.co.infob.config.security.provider.CustomAuthenticationProvider;
import kr.co.infob.config.security.service.SecurityService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final SecurityService securityService;
	private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthSuccessHandler customAUthSuccessHandler;
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationFilter customAuthenticationFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)

            //Login, Logout 등 기본 방식 미사용 처리
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            //JWT를 사용할 예정으로 Session 사용하지 않음
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            //요청 URL 검증 추리
            .authorizeHttpRequests(requests ->
                requests
                	.requestMatchers("/error").permitAll()
                    .anyRequest().access(customAuthorizationManager())
            )

            .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // JWT 인증 (커스텀 필터)
            .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)

            .anonymous(anonymous -> anonymous
            			.authorities(List.of(new SimpleGrantedAuthority("ANONYMOUS")))
                )
            // Exception 처리
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            );

        return http.build();
    }

    /**
     * 권한정보에 자동으로 붙는 기본 접두사(ROLE_) 제거
     * @return
     */
    @Bean
	GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}

    /**
     * 권한 및 엑세스 제어 시 기본 접두사(ROLE_) 제거
     * @return
     */
	@Bean
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultWebSecurityExpressionHandler;
	}

	/**
	 * 로그인한 사용자 정보를 확인을 위한 Provider 설정
	 * @param http
	 * @return
	 * @throws Exception
	 */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

    /**
     * UsernamePasswordAuthenticationFilter를 확장한 CustomeAuthenticationFilter
     * Form 기반의 UsernamePasswordAuthenticationFilter를 JSON 형태로 변경하여 사용자 정보를 처리함.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http) throws Exception {

    	AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class).build();

        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManager);
        filter.setFilterProcessesUrl("/api/v1/auth/signin");
        filter.setAuthenticationSuccessHandler(customAUthSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthFailureHandler);
        return filter;
    }

    /**
     * 인증정보를 통한 승인 여부를 관리하는 Manager
     * Database에 저장된 정보와 사용자 로그인정보를 비교하여 접근 가능여부를 확인한다.
     * @return
     */
    @Bean
    AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager() {
        return new CustomAuthorizationManager(customSecurityMetadataSource());
    }

    /**
     * Database에서 관리하는 URL 정보를 조회하여 현재 접속한 URL의 권한 정보를 반환한다.
     *  FilterInvocationSecurityMetadataSource 구현한 class
     * @return
     */
    @Bean
    CustomSecurityMetadataSource customSecurityMetadataSource() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> destMap = securityService.selectUrlRoleMapping();
        return new CustomSecurityMetadataSource(destMap);
    }

}