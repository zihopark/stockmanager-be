package com.stock.entity.repository.naverOrder;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.naverOrder.OrderEntity;


public interface OrderRepository extends JpaRepository<OrderEntity, String>{

	
	 // orderId로 주문을 찾을 수 있도록 하는 메소드
    public OrderEntity findByOrderId(String orderId);

    // 모든 주문을 찾을 수 있도록 하는 메소드
    public List<OrderEntity> findAll();

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.productOrders WHERE o.orderId = :orderId")
    Optional<OrderEntity> findByIdWithProductOrders(@Param("orderId") String orderId);
     
}
