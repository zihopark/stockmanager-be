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
		
		// 환경 변수를 읽어 애플리케이션에 설정할 때의 내용 (EC2의 docker-compose.yml에서 설정된 환경변수)
        System.setProperty("NAVER_CLIENT_ID", System.getenv("NAVER_CLIENT_ID"));
        System.setProperty("NAVER_CLIENT_SECRET", System.getenv("NAVER_CLIENT_SECRET"));
        System.setProperty("MYSQL_URL", System.getenv("MYSQL_URL"));
        System.setProperty("MYSQL_USERNAME", System.getenv("MYSQL_USERNAME"));
        System.setProperty("MYSQL_PASSWORD", System.getenv("MYSQL_PASSWORD"));

        SpringApplication.run(StockManagementApplication.class, args);
	}

}
