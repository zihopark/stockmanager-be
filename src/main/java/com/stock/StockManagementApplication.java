package com.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class StockManagementApplication {

	public static void main(String[] args) {
		
		/* .env 파일을 로드하여 환경변수로 설정할 때의 내용 
        Dotenv dotenv = Dotenv.configure().load();
        
        // 이후 로드된 환경변수를 사용하여 애플리케이션 설정
        System.setProperty("NAVER_CLIENT_ID", dotenv.get("NAVER_CLIENT_ID"));
        System.setProperty("NAVER_CLIENT_SECRET", dotenv.get("NAVER_CLIENT_SECRET"));
        System.setProperty("NAVER_API_CALLER_IP", dotenv.get("NAVER_API_CALLER_IP"));
        System.setProperty("MYSQL_URL", dotenv.get("MYSQL_URL"));
        System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
        System.setProperty("MYSQL_USERNAME", dotenv.get("MYSQL_USERNAME"));
        
		SpringApplication.run(StockManagementApplication.class, args);
		*/
		

        // NAVER_CLIENT_ID
        String naverClientId = System.getenv("NAVER_CLIENT_ID");
        if (naverClientId != null && !naverClientId.isBlank()) {
            System.setProperty("NAVER_CLIENT_ID", naverClientId);
        }

        // NAVER_CLIENT_SECRET
        String naverClientSecret = System.getenv("NAVER_CLIENT_SECRET");
        if (naverClientSecret != null && !naverClientSecret.isBlank()) {
            System.setProperty("NAVER_CLIENT_SECRET", naverClientSecret);
        }

        // MYSQL_URL
        String mysqlUrl = System.getenv("MYSQL_URL");
        if (mysqlUrl != null && !mysqlUrl.isBlank()) {
            System.setProperty("MYSQL_URL", mysqlUrl);
        }

        // MYSQL_USERNAME
        String mysqlUsername = System.getenv("MYSQL_USERNAME");
        if (mysqlUsername != null && !mysqlUsername.isBlank()) {
            System.setProperty("MYSQL_USERNAME", mysqlUsername);
        }

        // MYSQL_PASSWORD
        String mysqlPassword = System.getenv("MYSQL_PASSWORD");
        if (mysqlPassword != null && !mysqlPassword.isBlank()) {
            System.setProperty("MYSQL_PASSWORD", mysqlPassword);
        }

        // CORS_ALLOWED_ORIGINS
        String corsAllowed = System.getenv("CORS_ALLOWED_ORIGINS");
        if (corsAllowed != null && !corsAllowed.isBlank()) {
            System.setProperty("CORS_ALLOWED_ORIGINS", corsAllowed);
        }

        SpringApplication.run(StockManagementApplication.class, args);
	}

}
