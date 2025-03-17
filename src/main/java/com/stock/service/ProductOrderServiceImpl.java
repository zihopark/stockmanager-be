package com.stock.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.stock.entity.repository.naverOrder.ProductOrderRepository;

import lombok.AllArgsConstructor;

import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;
import com.stock.entity.repository.naverOrder.OrderRepository;

@Service
@AllArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService{
	
	private final ProductOrderRepository productOrderRepository;
	
	//productOrderStatus가 DELIVERING, DELIVERED, PURCHASED_DECIDED인 ProductOrderEntity들만 리스트로 반환
	@Override
	public List<ProductOrderEntity> alreadyDeliveredProductOrders(){
		List<String> statuses = Arrays.asList("DELIVERING", "DELIVERED", "PURCHASED_DECIDED");
		List<ProductOrderEntity> orders = productOrderRepository.findByProductOrderStatusIn(statuses);
		return orders;
	}
	
	@Override
    public void deleteProductOrder(String productOrderId) {
    	Optional<ProductOrderEntity> productOrderEntityOptional = productOrderRepository.findById(productOrderId);

        if (productOrderEntityOptional.isPresent()) {
            productOrderRepository.delete(productOrderEntityOptional.get());
            System.out.println(productOrderId + " 상품 주문번호가 삭제되었습니다.");
        } else {
            System.out.println("ProductOrderEntity not found for id: " + productOrderId + ". 삭제할 주문이 없습니다.");
        }
    }
    
}
