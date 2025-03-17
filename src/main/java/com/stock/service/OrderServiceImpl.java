package com.stock.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.dto.object.ApiOrderResponse;
import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.OptionsMaterialRepository;
import com.stock.entity.repository.ProductsMaterialRepository;
import com.stock.entity.repository.SupplementsMaterialRepository;
import com.stock.entity.repository.naverOrder.OrderRepository;
import com.stock.entity.repository.naverOrder.ProductOrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductOrderRepository productOrderRepository;
    

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll(); //이 때 연관된 ProductOrderEntity도 같이 자동으로 조회됨 
    }

    @Override
    public List<ProductOrderEntity> getAllProductOrders() {
        return productOrderRepository.findAll();
    }

    @Override
    public void deleteOrder(String orderId) {
    	Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);

        if (orderEntityOptional.isPresent()) {
            orderRepository.delete(orderEntityOptional.get());
            System.out.println(orderId + " 주문건이 삭제되었습니다.");
        } else {
            System.out.println("OrderEntity not found for id: " + orderId + ". 삭제할 주문이 없습니다.");
        }
    }
    

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
        System.out.println("전체 주문이 삭제되었습니다.");
    }
    
}
