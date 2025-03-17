package com.stock.service;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;  
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service  // Spring의 서비스 빈으로 등록
public class AutoTokenService {

    private static final String API_URL = "https://api.commerce.naver.com/external/v1/oauth2/token";

    
    @Value("${naver.commerce.client-id}")
    private String CLIENT_ID; // @Value를 사용하여 application.properties에서 값을 가져옵니다.
    
    
    @Value("${naver.commerce.client-secret}")
    private String CLIENT_SECRET; // @Value를 사용하여 application.properties에서 값을 가져옵니다.
    
    private String cachedToken;
    private Instant tokenExpirationTime;

    public String getAccessToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpirationTime)) {
            // 토큰이 없거나 만료된 경우, 새로 발급받기
            fetchNewAccessToken();
        }
        return cachedToken;
    }

    private void fetchNewAccessToken() {
        
        RestTemplate restTemplate = new RestTemplate();


        // 1. 타임스탬프 생성
        long timestamp = (Instant.now().toEpochMilli() - 3000);

        // 2. 클라이언트 비밀번호 생성 및 해싱
        String pwd = CLIENT_ID + "_" + timestamp;
        String hashedPwd = BCrypt.hashpw(pwd, CLIENT_SECRET);

        // 3. Base64 인코딩 (java.util.Base64 사용)
        String clientSecretSign = Base64.getEncoder().encodeToString(hashedPwd.getBytes());

        // 4. 요청 데이터 생성
        Map<String, String> requestData = new HashMap<>();
        requestData.put("client_id", CLIENT_ID);
        requestData.put("timestamp", String.valueOf(timestamp));
        requestData.put("client_secret_sign", clientSecretSign);
        requestData.put("grant_type", "client_credentials");
        requestData.put("type", "SELF");

        // 5. URL 파라미터 구성
        StringBuilder queryBuilder = new StringBuilder(API_URL);
        queryBuilder.append("?");
        requestData.forEach((key, value) -> queryBuilder.append(key).append("=").append(value).append("&"));
        String url = queryBuilder.toString().replaceAll("&$", "");

        // 6. API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        // 7. 응답 처리
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody.containsKey("access_token")) {
                // 토큰과 만료 시간을 캐시
                String token = (String) responseBody.get("access_token");
                cachedToken = token;
                tokenExpirationTime = Instant.now().plusSeconds(3600); // 토큰 만료 시간 1시간 후로 설정
            } else {
                throw new RuntimeException("토큰 발급 실패: " + responseBody);
            }
        } else {
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }
    }
}
