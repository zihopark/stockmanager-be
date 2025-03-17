package com.stock.entity.repository.naverOrder;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stock.entity.naverOrder.ProductOrderEntity;

public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, String>{
	
	
    List<ProductOrderEntity> findByClaimStatus(String claimStatus);

    ProductOrderEntity findByProductOrderId(String productOrderId);
    
    // productOrderStatus가 배송중, 배송완료, 구매확정인 ProductOrder 주문만 조회
    List<ProductOrderEntity> findByProductOrderStatusIn(List<String> statuses);
}
