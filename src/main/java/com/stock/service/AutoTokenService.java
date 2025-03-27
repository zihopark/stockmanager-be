package com.stock.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AutoTokenService {

    private static final String API_URL = "https://api.commerce.naver.com/external/v1/oauth2/token";

    @Value("${naver.commerce.client-id}")
    private String CLIENT_ID;

    @Value("${naver.commerce.client-secret}")
    private String CLIENT_SECRET;

    private String cachedToken;
    private Instant tokenExpirationTime;

    public String getAccessToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpirationTime)) {
            fetchNewAccessToken();
        }
        return cachedToken;
    }

    private void fetchNewAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        long timestamp = Instant.now().toEpochMilli();

        /*
        // bcrypt + base64-url encoding
        String password = CLIENT_ID + "_" + timestamp;
        String hashed = BCrypt.hashpw(password, CLIENT_SECRET);
        System.out.println("참고로 CLIENT_SECRET = " + CLIENT_SECRET);

        String clientSecretSign = Base64.getUrlEncoder().encodeToString(hashed.getBytes(StandardCharsets.UTF_8));
		*/
        
        String password = CLIENT_ID + "_" + timestamp;
        String hashedPw = CLIENT_SECRET; // 이미 bcrypt된 해시
        System.out.println("참고로 CLIENT_SECRET = " + CLIENT_SECRET);
        String clientSecretSign = Base64.getUrlEncoder()
            .encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
        
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", CLIENT_ID);
        formData.add("timestamp", String.valueOf(timestamp));
        formData.add("client_secret_sign", clientSecretSign);
        formData.add("grant_type", "client_credentials");
        formData.add("type", "SELF");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody.containsKey("access_token")) {
                cachedToken = (String) responseBody.get("access_token");
                tokenExpirationTime = Instant.now().plusSeconds(3600);
            } else {
                throw new RuntimeException("토큰 발급 실패: " + responseBody);
            }
        } else {
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }
    }
}

