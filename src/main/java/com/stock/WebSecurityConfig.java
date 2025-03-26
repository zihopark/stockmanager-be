package com.stock;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.stock.service.UserDetailsServiceImpl;
import com.stock.util.JWTUtil;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.unit.DataSize;
import jakarta.servlet.MultipartConfigElement;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final JWTUtil jwtUtil;

	@Value("${cors.allowed-origins}")
	private String allowedOrigins;
	
	//스프링시큐리티 암호화 스프링빈 등록 
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//로그인 처리
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        
        //CSRF, CORS 공격 보안 비활성화
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());
        
        //FormLogin, BasicHttp 비활성화
        http.formLogin((formLogin) -> formLogin.disable());
        http.httpBasic((auth)-> auth.disable());
        
        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 구현전에 실행
        /* UsernamePasswordAuthenticationFilter는 request body에 포함된 
           username과 password를 파싱하여 Authentication Manager에게 전달.
           현재 SecurityConfig에서 formLogin 방식을 disable 했기 때문에 
           기본적으로 활성화 되어 있는 해당 필터는 동작하지 않으며,
           따라서, 로그인을 진행하기 위해서 필터를 커스텀하여 등록해야 함.
           실행순서 : 리액트와 REST API를 이용한 로그인을 통해 토큰 및 쿠키 생성 
                  -> REST API 실행할때마다 Authorization Bearer 필드에 포함된 토큰을 JWTFILTER를 통해 읽어 들이고 유효성 검증 
                  -> UsernamePasswordAuthenticationFilter를 통해 아이디, 패스워드 검증
                  -> 필처 체인에서 접근권한 설정 가능해 짐    
        */
        
        //JWT Filter 설정
	    http.addFilterBefore(new JwtAuthFilter(userDetailsServiceImpl, jwtUtil), 
    			UsernamePasswordAuthenticationFilter.class);

        //세션 관리 상태 비활성화. -> Spring Security가 세션 생성 및 관리 안함.
        http.sessionManagement((session) -> session
        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//스프링 시큐리티의 접근권한 설정(Authentication)
		http.authorizeHttpRequests((authz)-> authz
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.requestMatchers("/member/**").permitAll() //Login, Signup, 비밀번호 찾기 등 
			.requestMatchers(HttpMethod.POST, "/member/signup").permitAll()
			.requestMatchers(HttpMethod.POST, "/member/idCheck").permitAll()
			.requestMatchers("/api/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_MASTER")
			.requestMatchers("/material/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_MASTER")
			.requestMatchers("/order/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_MASTER")
			.requestMatchers("/product/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_MASTER")
			.requestMatchers("/master/**").hasAnyAuthority("ROLE_MASTER")
			.anyRequest().authenticated());
		
		log.info("=============== 스프링 시큐리티 필터 체인 설정 완료 ===============");		
		return http.build();
    }
    
    
    //react와 연동을 위한 cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 허용할 헤더
        configuration.setAllowCredentials(true); // 자격 증명 전송 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 엔드포인트에 대해 CORS 허용

        return source;
    }
    
	@Bean
    public AuthenticationManager authenticationManager
    	(AuthenticationConfiguration configuration) throws Exception{ 
		return configuration.getAuthenticationManager(); 
	}    

	@Bean
	public MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize(DataSize.ofMegabytes(50));
	    factory.setMaxRequestSize(DataSize.ofMegabytes(50));
	    return factory.createMultipartConfig();
	}
	
}