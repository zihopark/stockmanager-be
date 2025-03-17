package com.stock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
class StockManagementApplicationTests {

	@Test
	void contextLoads() {
	}

	@BeforeAll
    static void loadEnv() {
		/*
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("NAVER_CLIENT_ID", dotenv.get("NAVER_CLIENT_ID"));
        System.setProperty("NAVER_CLIENT_SECRET", dotenv.get("NAVER_CLIENT_SECRET"));
        System.setProperty("NAVER_API_CALLER_IP", dotenv.get("NAVER_API_CALLER_IP"));
        */
    }

}
