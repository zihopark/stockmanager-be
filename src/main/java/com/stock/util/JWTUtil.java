package com.stock.util;

import java.security.Key;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.stock.entity.MemberEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JWTUtil {
    
    private String secretKey;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        this.secretKey = generateSecretKey();
        log.info("Generated JWT Secret Key: {}", secretKey);
    }

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);
        return Base64.getEncoder().encodeToString(sharedSecret);
    }

    private Key createKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }
    
    public String generateToken(MemberEntity member, int days) {
    	Map<String, Object> payloads = new HashMap<>();
        payloads.put("email", member.getEmail());
        payloads.put("role", "ROLE_" + member.getRole());

    	
    	Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        JwtBuilder builder = Jwts.builder()
                                .setHeader(headers)
                                .setClaims(payloads)
                                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                                .setExpiration(Date.from(ZonedDateTime.now().plusDays(days).toInstant()))
                                .signWith(createKey(), signatureAlgorithm);

        return builder.compact();
    }
    
    public String validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(createKey()).build().parseClaimsJws(token);
            return "VALID_JWT";
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return "INVALID_JWT";
        } catch(ExpiredJwtException e) {
            return "EXPIRED_JWT";
        } catch(UnsupportedJwtException e) {
            return "UNSUPPORTED_JWT";
        } catch(IllegalArgumentException e) {
            return "EMPTY_JWT";
        }
    }   
    
    public String getTokenFromAuthorization(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && !bearerToken.isEmpty() && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        } else return "INVALID_HEADER";
    }
    
    public Map<String,Object> getDataFromToken(String token) throws Exception {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(createKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        Map<String, Object> data = new HashMap<>();
        data.put("email", claims.get("email").toString());
        data.put("role", claims.get("role").toString()); // 역할 추가
        return data;
    }   
}
