package com.stock.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;

public interface OrderService {

    public List<OrderEntity> getAllOrders();

    public List<ProductOrderEntity> getAllProductOrders() ;

    public void deleteOrder(String orderId);

    public void deleteAll();

}
