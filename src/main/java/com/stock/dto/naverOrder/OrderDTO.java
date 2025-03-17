package com.stock.dto.naverOrder;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

	@JsonProperty("orderId")
    private String orderId;

	@JsonProperty("paymentDate")
    private String paymentDate;

	@JsonProperty("ordererName")
    private String ordererName;

	private List<ProductOrderDTO> productOrders; // 연관된 상품 주문 리스트


	// OrderDTO -> OrderEntity 변환
	public OrderEntity dtoToOrderEntity(OrderDTO orderDTO) {
		OrderEntity orderEntity = OrderEntity.builder()
											.orderId(orderDTO.getOrderId())
											.paymentDate(orderDTO.getPaymentDate())
											.ordererName(orderDTO.getOrdererName())
											.build();

        // ProductOrderDTO 리스트를 ProductOrderEntity로 변환
        List<ProductOrderEntity> productOrderEntities = this.productOrders.stream()
                .map(dto -> dto.dtoToProductOrderEntity(orderEntity))
                .toList();

        orderEntity.setProductOrders(productOrderEntities); // 양방향 관계 설정
        return orderEntity;
	}

}
