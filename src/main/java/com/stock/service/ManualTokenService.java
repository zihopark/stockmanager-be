package com.stock.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Base64;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ManualTokenService {

    private static final String TOKEN_URL = "https://api.commerce.naver.com/external/v1/oauth2/token";

    public String getToken(String clientId, String clientSecret, String type) {
        try {
        	// 1. 타임스탬프 생성
            long timestamp = (Instant.now().toEpochMilli() - 3000);

            // 2. 클라이언트 비밀번호 생성 및 해싱
            String pwd = clientId + "_" + timestamp;
            String hashedPwd = BCrypt.hashpw(pwd, clientSecret);

            // 3. Base64 인코딩 (java.util.Base64 사용)
            String clientSecretSign = Base64.getEncoder().encodeToString(hashedPwd.getBytes());

            // 4. 요청 데이터 생성
            Map<String, String> requestData = new HashMap<>();
            requestData.put("client_id", clientId);
            requestData.put("timestamp", String.valueOf(timestamp));
            requestData.put("client_secret_sign", clientSecretSign);
            requestData.put("grant_type", "client_credentials");
            requestData.put("type", type);

            // 5. URL 파라미터 구성
            StringBuilder queryBuilder = new StringBuilder(TOKEN_URL);
            queryBuilder.append("?");
            requestData.forEach((key, value) -> queryBuilder.append(key).append("=").append(value).append("&"));
            String url = queryBuilder.toString().replaceAll("&$", "");
            System.out.println("URL : " + url);

            // 6. API 호출
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            // 7. 응답 처리
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            } else {
                throw new RuntimeException("토큰 발급 실패: " + responseBody);
            }
        } catch (Exception e) {
            throw new RuntimeException("토큰 요청 중 오류 발생", e);
        }
    }
}

