package com.stock;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stock.service.UserDetailsServiceImpl;
import com.stock.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final JWTUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//공개 경로에 대해선 필터 건너 뛰기.
		String uri = request.getRequestURI();
		System.out.println("JwtAuthFilter 통과! 요청 URI: " + request.getRequestURI());
		
		if (uri.startsWith("/member") || uri.equals("/member/signup") || uri.equals("/member/idCheck") || uri.equals("/member/loginCheck")) {
		    filterChain.doFilter(request, response);
		    return;
		}
		
	    //나머지는 그대로 수행.
		//REST API 실행 시 HTTP Request 헤더에 포함된 Authorization 값 읽음
		String authorizationHeader = request.getHeader("Authorization");
		
		String email = "";
		
		//Authorization 내에 Bearer 필드가 존재하면 토큰값을 읽음
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {			
			String token = authorizationHeader.substring(7);			
			if(jwtUtil.validateToken(token).equals("VALID_JWT")) {
				
				try {
					//토큰이 유효하면 토큰내의 payload에서 email을 읽음
					email = (String) jwtUtil.getDataFromToken(token).get("email");
					} catch(Exception e) {
						e.printStackTrace();
				}
		
				//토큰에서 읽어 온 email로 사용자 정보를 읽어 와서 userDetails에 저장
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
				
				//
				if(userDetails != null) {					
					Map<String, Object> claims = jwtUtil.getDataFromToken(token);
					String roleFromToken = claims.get("role").toString();

					// role 값이 이미 "ROLE_" 접두사를 포함하고 있지 않다면 추가
					if (!roleFromToken.startsWith("ROLE_")) {
					    roleFromToken = "ROLE_" + roleFromToken;
					}

					List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleFromToken));

				    UsernamePasswordAuthenticationToken authentication =
				        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

				    SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);		
	}
}
