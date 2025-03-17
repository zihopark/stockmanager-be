package com.stock.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.dto.object.ApiDetailProductResponse;
import com.stock.dto.object.ApiOrderResponse;
import com.stock.dto.object.ApiProductResponse;
import com.stock.entity.MaterialEntity;
import com.stock.entity.OptionsMaterialEntity;
import com.stock.entity.ProductsMaterialEntity;
import com.stock.entity.SupplementsMaterialEntity;
import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.OptionsMaterialRepository;
import com.stock.entity.repository.ProductsMaterialRepository;
import com.stock.entity.repository.SupplementsMaterialRepository;
import com.stock.entity.repository.naverOrder.OrderRepository;
import com.stock.entity.repository.naverOrder.ProductOrderRepository;
import com.stock.entity.repository.naverProduct.ProductRepository;
import com.stock.service.ApiDetailProductService;
import com.stock.service.ApiOrderService;
import com.stock.service.ApiProductService;
import com.stock.service.AutoTokenService;
import com.stock.service.ManualTokenService;
import com.stock.service.OrderService;
import com.stock.service.ProductOrderService;
import com.stock.service.SettingsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommerceApiController {

    private final ManualTokenService manualTokenService; 
    private final ApiOrderService apiOrderService;
    private final ApiProductService apiProductService;
    private final AutoTokenService autoTokenService;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ApiDetailProductService apiDetailProductService;
    private final ProductOrderService productOrderService;
    private final SettingsService settingsService;

    // 이미 처리된 상품 ID들을 저장하는 리스트
    private List<Long> processedProductIds = new ArrayList<>();

    
    //주문건 조회 
    @GetMapping("/fetch-orders")
    public ResponseEntity<String> fetchOrders(@RequestParam(name = "from") String from, 
                                               @RequestParam(name = "rangeType") String rangeType,
                                               @RequestParam(name = "productOrderStatuses") String productOrderStatuses) {
    	try {
            // 외부 API 호출
            String result = apiOrderService.fetchOrders(from, rangeType, productOrderStatuses);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("외부 API 호출 중 에러 발생: " + e.getMessage());
        }
    }
    
    //DB에 저장되어 있는 설정값 클라이언트로 보내기 
    @GetMapping("/get-settings")
    public ResponseEntity<Map<String, String>> getSettings() {
        Map<String, String> settings = new HashMap<>();
        settings.put("fromDate", settingsService.getSetting("fromDate"));
        settings.put("deliveredTime", settingsService.getSetting("deliveredTime"));
        return ResponseEntity.ok(settings);
    }

    //DB에 설정값 저장하기 
    @PostMapping("/save-settings")
    public ResponseEntity<?> saveSettings(@RequestBody Map<String, String> settings) {
        settingsService.saveSetting("fromDate", settings.get("fromDate"));
        settingsService.saveSetting("deliveredTime", settings.get("deliveredTime"));
        System.out.println("fromDate is : " + settings.get("fromDate") + " & deliveredTime is : " + settings.get("deliveredTime"));
        return ResponseEntity.ok().build();
    }
    
    
    //자동으로 매 0분, 30분의 10초마다 배송된 주문건이 있는지 확인하여, 재료 차감 이후 해당 orderId 와 ProductOrderID 삭제
    @Transactional
    @Scheduled(cron = "10 0/30 * * * *")  //(cron = "10 0 * * * *")  
    public void processDeliveredOrders() {
        try {
            List<ProductOrderEntity> productOrderEntities = productOrderService.alreadyDeliveredProductOrders();
            apiOrderService.processingDeliveredOrders(productOrderEntities);
            System.out.println("주문 자동 업데이트 - 이미 발송된 주문건이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("발송된 주문건 데이터 처리 실패: " + e.getMessage());
        }
    }
    
    
    
    //주문 정보 저장 
    @PostMapping("/saveOrders")
    public ResponseEntity<String> postSaveOrders(@RequestBody ApiOrderResponse apiResponse) {
        try {
            apiOrderService.saveOrderData(apiResponse);
            return ResponseEntity.ok("주문 데이터가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("주문 데이터 저장 실패: " + e.getMessage());
        }
    }

    //재료 차감 이후 해당 orderId 와 ProductOrderID 삭제
    @PostMapping("/processingDeliveredOrders")
    public ResponseEntity<String> postProcessingDeliveredOrders() {
    	try {
    		List<ProductOrderEntity> productOrderEntities = productOrderService.alreadyDeliveredProductOrders();
    		apiOrderService.processingDeliveredOrders(productOrderEntities);
            return ResponseEntity.ok("이미 발송된 주문건이 성공적으로 처리되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("발송된 주문건 데이터 처리 실패: " + e.getMessage());
        }
    }

    //상품 목록 조회 및 저장
    @PostMapping("/saveProducts")
    public ResponseEntity<String> saveProducts(@RequestParam("fromDate") String fromDate) {     // 사용자로부터 시작일을 받기) 
        String apiUrl = "https://api.commerce.naver.com/external/v1/products/search";
        
        // 오늘 날짜를 "yyyy-MM-dd" 형식으로 가져오기
        String toDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // 요청 본문 구성
        String requestBody = "{\n" +
                "    \"productStatusTypes\": [\"SALE\"],\n" + //상품 판매 상태 코드 (WAIT(판매 대기), SALE(판매 중), OUTOFSTOCK(품절), UNADMISSION(승인 대기), REJECTION(승인 거부), SUSPENSION(판매 중지), CLOSE(판매 종료), PROHIBITION(판매 금지))
                "    \"page\": 1,\n" + //페이지 번호
                "    \"size\": 500,\n" + //페이지 크기 (최대 500건 까지 가능)
                "    \"orderType\": \"REG_DATE\",\n" + //상품 등록일
                "    \"periodType\": \"PROD_REG_DAY\",\n" + //검색 기간 타입은 상품 등록일
                "    \"fromDate\": \"" + fromDate + "\",\n" + // 사용자로부터 받은 시작일
                "    \"toDate\": \"" + toDate + "\"\n" + // 오늘 날짜로 설정된 종료일
                "}";

        // 토큰 자동 발급
        String token = autoTokenService.getAccessToken();  // 자동으로 발급받은 토큰

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token); 

        // HttpEntity로 요청 설정
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();

            // API 호출
            ResponseEntity<ApiProductResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    ApiProductResponse.class
            );

            // 응답을 통해 받은 데이터를 DB에 저장
            ApiProductResponse apiProductResponse = response.getBody();
            
            try {
                apiProductService.saveProductData(apiProductResponse);
                System.out.println("상품 데이터 저장 성공");
            } catch (Exception e) {
                System.out.println("상품 데이터 저장 실패: " + e.getMessage());
            }

            return ResponseEntity.ok("성공적으로 업데이트되었습니다.");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Error: " + e.getMessage());
        }
    }

    //각 상품에 대한 옵션 목록 저장
    @GetMapping("/saveOptionSupplements")
    public ResponseEntity<String> getSaveOptionSupplements() {    
        String DETAIL_API_URL = "https://api.commerce.naver.com/external/v2/products/origin-products/";

        // DB에서 모든 product_id 가져오기
        List<Long> productIds = productRepository.findAllProductIds();

        // 각 product_id에 대해 상세 정보 API 호출 및 데이터 저장
        for (Long productId : productIds) {
            try {
                // 이미 처리된 상품인 경우 건너뛰기
                if (isProductProcessed(productId)) {
                    continue;
                }

                // (1) API 호출
                String apiUrl = DETAIL_API_URL + productId;
                String token = autoTokenService.getAccessToken();  // 자동으로 발급받은 토큰
                
                // - RestTemplate을 사용하여 외부 API 요청 시 헤더에 토큰 추가
                RestTemplate restTemplate = new RestTemplate();
                // - HTTP 헤더에 Authorization: Bearer <token> 추가
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);
                
                // - HttpEntity 객체 생성 (헤더 포함)
                HttpEntity<String> entity = new HttpEntity<>(headers);
                // - API 요청 보내기
                ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

                // API 요청량 상태 확인
                HttpHeaders responseHeaders = response.getHeaders();
                System.out.println("ResponseHeaders : " + responseHeaders);

                // 필요한 헤더 값들 추출
                String remainingStr = responseHeaders.getFirst("gncp-gw-ratelimit-remaining");
                String burstCapacityStr = responseHeaders.getFirst("gncp-gw-ratelimit-burst-capacity");
                String replenishRateStr = responseHeaders.getFirst("gncp-gw-ratelimit-replenish-rate");

                if (remainingStr != null && burstCapacityStr != null && replenishRateStr != null) {
                    int remaining = Integer.parseInt(remainingStr);
                    int burstCapacity = Integer.parseInt(burstCapacityStr);
                    int replenishRate = Integer.parseInt(replenishRateStr);

                    // 남은 요청 수가 0일 경우, 대기 시간 계산
                    if (remaining <= 0) {
                        System.out.println("Rate limit 초과, 다음 요청을 위해 대기 중...");
                        // replenishRate에 맞춰 대기 시간을 조정
                        int waitTimeInSeconds = 1; // 최소 1초 대기
                        if (replenishRate > 0) { 
                            waitTimeInSeconds = 2 / replenishRate; // 대기 시간을 초 단위로 계산 (2초 / replenishRate) 그냥 넉넉하게. 1로 하니 안됨.
                        }
                        System.out.println("대기 시간: " + waitTimeInSeconds + "초");
                        Thread.sleep(waitTimeInSeconds * 1000L);
                    }

                    System.out.println("remaining: " + remaining);
                    System.out.println("burstCapacity: " + burstCapacity);
                    System.out.println("replenishRate: " + replenishRate);

                } else {
                    System.out.println("헤더에 필요한 'gncp-gw-ratelimit-remaining', 'gncp-gw-ratelimit-burst-capacity', 'gncp-gw-ratelimit-replenish-rate' 값이 없습니다.");
                }
                    
                // (2) JSON 응답을 DTO로 변환
                ApiDetailProductResponse detailResponse = objectMapper.readValue(response.getBody(), ApiDetailProductResponse.class);

                // (3) OptionCombination 처리
                if (detailResponse.getOriginProduct() != null &&
                        detailResponse.getOriginProduct().getDetailAttribute() != null &&
                        detailResponse.getOriginProduct().getDetailAttribute().getOptionInfo() != null &&
                        detailResponse.getOriginProduct().getDetailAttribute().getOptionInfo().getOptionCombinations() != null) {
                    
                    apiDetailProductService.saveOptionData(detailResponse.getOriginProduct().getDetailAttribute().getOptionInfo().getOptionCombinations(),productId);
                }

                // (4)SupplementProduct 처리
                if (detailResponse.getOriginProduct() != null &&
                        detailResponse.getOriginProduct().getDetailAttribute() != null &&
                        detailResponse.getOriginProduct().getDetailAttribute().getSupplementProductInfo() != null &&
                        detailResponse.getOriginProduct().getDetailAttribute().getSupplementProductInfo().getSupplementProducts() != null) {
                    
                    apiDetailProductService.saveSupplementData(detailResponse.getOriginProduct().getDetailAttribute().getSupplementProductInfo().getSupplementProducts(), productId);
                }

                // 상품이 처리되었음을 마킹
                markProductAsProcessed(productId);
                

            } catch (Exception e) {
                System.err.println("상품 상세 정보 처리 실패 (ID: " + productId + "): " + e.getMessage());
            }
        }
        return ResponseEntity.ok("상품 옵션 및 추가 상품이 성공적으로 저장되었습니다.");
    }
    // 이미 처리된 상품인지 확인하는 메서드
    private boolean isProductProcessed(Long productId) {
        // DB 또는 캐시에서 productId가 이미 처리되었는지 확인하는 로직을 추가
        return processedProductIds.contains(productId);  // 예시: 이미 처리된 상품 ID 목록
    }
    // 상품이 처리되었음을 마킹하는 메서드
    private void markProductAsProcessed(Long productId) {
        // DB에 기록하거나, 해당 상품 ID를 리스트에 추가하여 추후 중복 처리 방지
        processedProductIds.add(productId);  // 예시: 처리된 상품 ID 목록에 추가
    }


    //매뉴얼로 토큰 발급하는 method
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody Map<String, String> request) {
        try {
        	
            String clientId = request.get("clientId");
            String clientSecret = request.get("clientSecret");

            if (clientId == null || clientSecret == null) {
                return ResponseEntity.badRequest().body("clientId와 clientSecret을 모두 제공해야 합니다.");
            }

            // 토큰 발급
            String accessToken = manualTokenService.getToken(clientId, clientSecret, "SELF");
            System.out.println("토큰발급 성공! : " +accessToken );

            return ResponseEntity.ok(Map.of("accessToken", accessToken));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    
}
