package com.stock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.dto.naverOrder.OrderDTO;
import com.stock.dto.naverOrder.ProductOrderDTO;
import com.stock.entity.naverOrder.OrderEntity;
import com.stock.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class TotalOrderController {
    
    private final OrderService orderService;

    @GetMapping("/list")
    public List<OrderDTO> getOrderList() {
        List<OrderEntity> orders = orderService.getAllOrders();

        // OrderEntity -> OrderDTO 변환
        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            List<ProductOrderDTO> productOrderDTOs = order.getProductOrders().stream().map(productOrder -> 
                ProductOrderDTO.builder()
                    .productOrderId(productOrder.getProductOrderId())
                    .productName(productOrder.getProductName())
                    .productOption(productOrder.getProductOption())
                    .quantity(productOrder.getQuantity())
                    .totalPaymentAmount(productOrder.getTotalPaymentAmount())
                    .build()
            ).toList();

            return OrderDTO.builder()
                .orderId(order.getOrderId())
                .paymentDate(order.getPaymentDate())
                .ordererName(order.getOrdererName())
                .productOrders(productOrderDTOs)
                .build();
        }).toList();

        return orderDTOs;
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") String orderId) {
    	try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("주문 삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAll() {
        try {
            orderService.deleteAll();
            return ResponseEntity.ok("모든 주문이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 삭제 중 오류가 발생했습니다.");
        }
    }

}
