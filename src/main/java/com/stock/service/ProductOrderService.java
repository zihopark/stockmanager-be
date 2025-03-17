package com.stock.service;

import java.util.List;

import com.stock.entity.naverOrder.ProductOrderEntity;

public interface ProductOrderService {

	//이미 발송된 ProductOrder List 
	public List<ProductOrderEntity> alreadyDeliveredProductOrders();
	
	public void deleteProductOrder(String productOrderId) ;
}
