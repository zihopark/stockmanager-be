package com.stock.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.dto.object.ApiOrderResponse;
import com.stock.dto.object.ApiOrderResponse.Data.Content;
import com.stock.entity.MaterialEntity;
import com.stock.entity.OptionsMaterialEntity;
import com.stock.entity.ProductsMaterialEntity;
import com.stock.entity.StockLogEntity;
import com.stock.entity.SupplementsMaterialEntity;
import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.OptionsMaterialRepository;
import com.stock.entity.repository.ProductsMaterialRepository;
import com.stock.entity.repository.StockLogRepository;
import com.stock.entity.repository.SupplementsMaterialRepository;
import com.stock.entity.repository.naverOrder.OrderRepository;
import com.stock.entity.repository.naverOrder.ProductOrderRepository;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiOrderService {

    private final OrderRepository orderRepository;
    private final ProductOrderRepository productOrderRepository;
    private final MaterialRepository materialRepository;
    private final OptionsMaterialRepository optionsMaterialRepository;
    private final SupplementsMaterialRepository supplementsMaterialRepository;
    private final ProductsMaterialRepository productsMaterialRepository;
    private final AutoTokenService autoTokenService;
    private final OrderService orderService;
    private final StockLogRepository stockLogRepository;
    private final ProductOrderService productOrderService;
    private final DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
    private final SettingsService settingsService;


    //주문건 직접 조회 
    @Transactional
    public String fetchOrders(String from, String rangeType, String productOrderStatuses) throws Exception {
        
    	String url = String.format(
                "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders?from=%s&rangeType=%s&productOrderStatuses=%s",
                from, rangeType, productOrderStatuses);
        System.out.println("API URL: " + url);

        String token = autoTokenService.getAccessToken(); //토큰 자동 발급 

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        // HttpEntity 객체 생성 (헤더 포함) 
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 받은 JSON 데이터를 ApiResponse 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ApiOrderResponse apiResponse = objectMapper.readValue(response.getBody(), ApiOrderResponse.class);
        
        saveOrderData(apiResponse);
        
        return response.getBody();
    }
    
    //////////////////////////
    
    //30분마다 실행되며 사용자가 입력한 시간이 현재 시각과 일치하면 API 호출.
    @Scheduled(cron = "0 0/30 * * * *")		//@Scheduled(cron = "0 0 0/1 * * *") (한시간마다 실행되는 코드)
    public void executeScheduledFetch() {
    	try {
    		String scheduledTime = settingsService.getDeliveredTime(); //사용자가 설정했던 배송 이후 시간 
    		LocalDateTime now = LocalDateTime.now();
            ZonedDateTime utcNow = now.atZone(ZoneOffset.UTC);  // UTC 시간대로 변환
            String currentTime = utcNow.format(DateTimeFormatter.ofPattern("HH:mm")); //현재 한국 시간 

            // 33시간 전 시간 계산 (한국 시간 기준 24시간 빼고, UTC 기준으로는 33시간 빼기)
            ZonedDateTime thirtyThreeHoursAgo = utcNow.minusHours(33);  // 33시간 전
            String isoTime = thirtyThreeHoursAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));  // API에 보낼 시간 

            // 로그 출력 (UTC 시간)
            //System.out.println("API에 보낼 시간 : " + isoTime);
            //System.out.println("현재 한국 시간 : " + currentTime);
            //System.out.println("api 자동 실행될 한국 시간 : " + scheduledTime);

            // 설정된 시간과 비교
            if (currentTime.equals(scheduledTime)) {
                System.out.println("Scheduled execution at: " + isoTime);
                fetchAutoOrders(isoTime, "PAYED_DATETIME", "PAYED,DELIVERING,DELIVERED");
            }
        } catch (Exception e) {
            System.err.println("Error during scheduled fetch: " + e.getMessage());
        }
    }
    
    //주문건 조회 (위에서 30분마다 자동으로 실행되는 코드로 인해 실행되는 아래 코드)
    @Transactional
    public String fetchAutoOrders(String from, String rangeType, String productOrderStatuses) throws Exception {

        String url = String.format(
                "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders?from=%s&rangeType=%s&productOrderStatuses=%s",
                from, rangeType, productOrderStatuses);
        System.out.println("API URL: " + url);

        String token = autoTokenService.getAccessToken(); //토큰 자동 발급 

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        
        // HttpEntity 객체 생성 (헤더 포함) 
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 받은 JSON 데이터를 ApiResponse 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ApiOrderResponse apiResponse = objectMapper.readValue(response.getBody(), ApiOrderResponse.class);
        
        saveOrderData(apiResponse);
        
        return response.getBody();
    }
    
    /////////////////////////////////////////
    
    
    /*
    클라이언트에서 JSON 데이터를 POST로 전송하여 주문 정보를 저장
    @param apiResponse JSON 데이터를 매핑한 ApiResponse 객체
    */
    @Transactional
    public void saveOrderData(ApiOrderResponse apiorderResponse) {
        Map<String, OrderEntity> orderEntityMap = new HashMap<>();

        for (Content content : apiorderResponse.getData().getContents()) {
            String orderId = content.getContent().getOrder().getOrderId();

            // OrderEntity 조회 또는 새로 생성
            OrderEntity orderEntity = orderEntityMap.computeIfAbsent(orderId, id -> {
                OrderEntity newOrderEntity = new OrderEntity();
                newOrderEntity.setOrderId(id);
                newOrderEntity.setPaymentDate(content.getContent().getOrder().getPaymentDate());
                newOrderEntity.setOrdererName(content.getContent().getOrder().getOrdererName());
                return newOrderEntity;
            });

            // ProductOrderEntity 매핑
            ProductOrderEntity productOrderEntity = new ProductOrderEntity();
            productOrderEntity.setProductOrderId(content.getContent().getProductOrder().getProductOrderId());
            productOrderEntity.setGroupProductId(String.valueOf(content.getContent().getProductOrder().getGroupProductId()));
            productOrderEntity.setProductId(content.getContent().getProductOrder().getProductId());
            productOrderEntity.setOriginalProductId(content.getContent().getProductOrder().getOriginalProductId());
            productOrderEntity.setClaimStatus(content.getContent().getProductOrder().getClaimStatus());
            productOrderEntity.setClaimType(content.getContent().getProductOrder().getClaimType());
            productOrderEntity.setOptionCode(content.getContent().getProductOrder().getOptionCode());
            productOrderEntity.setOptionPrice(content.getContent().getProductOrder().getOptionPrice());
            productOrderEntity.setProductClass(content.getContent().getProductOrder().getProductClass());
            productOrderEntity.setProductName(content.getContent().getProductOrder().getProductName());
            productOrderEntity.setProductOption(content.getContent().getProductOrder().getProductOption());
            productOrderEntity.setProductOrderStatus(content.getContent().getProductOrder().getProductOrderStatus());
            productOrderEntity.setQuantity(content.getContent().getProductOrder().getQuantity());
            productOrderEntity.setInitialQuantity(content.getContent().getProductOrder().getInitialQuantity());
            productOrderEntity.setRemainQuantity(content.getContent().getProductOrder().getRemainQuantity());
            productOrderEntity.setSellerProductCode(content.getContent().getProductOrder().getSellerProductCode());
            productOrderEntity.setTotalPaymentAmount(content.getContent().getProductOrder().getTotalPaymentAmount());
            productOrderEntity.setRemainPaymentAmount(content.getContent().getProductOrder().getRemainPaymentAmount());
            productOrderEntity.setPaymentCommission(content.getContent().getProductOrder().getPaymentCommission());
            productOrderEntity.setExpectedSettlementAmount(content.getContent().getProductOrder().getExpectedSettlementAmount());
            productOrderEntity.setOrderEntity(orderEntity);

            // 기존 ProductOrders에 추가
            orderEntity.getProductOrders().add(productOrderEntity);
        }

        // 모든 OrderEntity를 저장
        orderEntityMap.values().forEach(orderRepository::save);
        
        System.out.println("주문 데이터 저장 완료 ");
        
    }

    
    
    //재료 차감 및 해당 orderId 와 ProductOrderID 삭제
    @Transactional
    public void processingDeliveredOrders(List<ProductOrderEntity> productOrderEntities) {
        Set<String> processedOrders = new HashSet<>(); // 중복 방지용 Set

        for (ProductOrderEntity entity : productOrderEntities) {
            try {
                String productClass = entity.getProductClass();
                int quantity = entity.getQuantity();

                switch (productClass) {
                    case "조합형옵션상품":
                        processOption(entity, quantity);
                        break;
                    case "추가구성상품":
                        processSupplement(entity, quantity);
                        break;
                    case "단일상품":
                        processSingleProduct(entity, quantity);
                        break;
                    default:
                        System.out.println("새로운 유형인 '" + productClass + "'의 제품이 주문되었습니다. 코드를 추가해주세요!");
                }

                // ProductOrder 삭제
                productOrderService.deleteProductOrder(entity.getProductOrderId());
                processedOrders.add(entity.getOrderEntity().getOrderId()); // 처리한 orderId 저장

            } catch (Exception e) {
                System.err.println("주문 처리 중 오류 발생 : " + e.getMessage());
                e.printStackTrace();
            }
        }

        // 모든 ProductOrder 삭제 후, 한 번만 실행
        for (String orderId : processedOrders) {
            deleteOrderIfNoProducts(orderId);
        }
    }

    
    //ProductOrder가 아예 없으면 그 부모인 Order를 없애는 코드.
    @Transactional
    public void deleteOrderIfNoProducts(String orderId) {
        OrderEntity order = orderRepository.findByIdWithProductOrders(orderId).orElse(null);
        if (order != null && order.getProductOrders().isEmpty()) {
            orderRepository.delete(order);
            System.out.println("ProductOrder가 없는 주문건이 삭제 완료되었습니다.");
        }
    }

    
    private void processOption(ProductOrderEntity entity, int quantity) {
    	Long optionCode = Long.valueOf(entity.getOptionCode());
	    List<OptionsMaterialEntity> optionsMaterialEntities = optionsMaterialRepository.findByOptionId(optionCode);	    
	    for (OptionsMaterialEntity optionsMaterialEntity : optionsMaterialEntities) {
	        handleMaterialReduction(optionsMaterialEntity.getMaterial(), optionsMaterialEntity.getQuantity() * quantity, entity.getProductOrderId());
	    }
    }
    private void processSupplement(ProductOrderEntity entity, int quantity) {
    	Long optionCode = Long.valueOf(entity.getOptionCode());
    	List<SupplementsMaterialEntity> supplementsMaterialEntities = supplementsMaterialRepository.findBySupplementId(optionCode);  
	    for (SupplementsMaterialEntity supplementsMaterialEntity : supplementsMaterialEntities) {
	        handleMaterialReduction(supplementsMaterialEntity.getMaterial(), supplementsMaterialEntity.getQuantity() * quantity, entity.getProductOrderId());
	    }
    }
    private void processSingleProduct(ProductOrderEntity entity, int quantity) {
    	Long originalProductId = Long.valueOf(entity.getOriginalProductId());
    	List<ProductsMaterialEntity> productsMaterialEntities = productsMaterialRepository.findByProduct_OriginProductNo(originalProductId);
        for (ProductsMaterialEntity productsMaterialEntity : productsMaterialEntities) {
            handleMaterialReduction(productsMaterialEntity.getMaterial(), productsMaterialEntity.getQuantity() * quantity, entity.getProductOrderId());
        }
    }

    
    // 재료 차감 로직
    public void handleMaterialReduction(MaterialEntity material, int quantity, String productOrderId) {
        
    	// 재고 차감
        material.setStockQuantity(material.getStockQuantity() - quantity);
        materialRepository.save(material);
        
        
        //로그 추가
        StockLogEntity stockLogEntity = new StockLogEntity();
        stockLogEntity.setQuantity(quantity);
        stockLogEntity.setIsUserInput("N");
        String content = "주문건으로 " + material.getName() + " " + quantity + "EA 출고 | 상품 주문번호 : " + productOrderId;
        stockLogEntity.setContent(content);
        stockLogEntity.setType("outbound");
        stockLogEntity.setLogDate(LocalDateTime.now());
        stockLogEntity.setMaterialEntity(material);
        stockLogRepository.save(stockLogEntity);
        
        
        // 재고가 마이너스라면
        if (material.getStockQuantity() < 0) {
            System.out.println(material.getName()+ "의 현재 재고가 마이너스입니다. 재고를 업데이트해주세요!");
        }
    }
    

}

