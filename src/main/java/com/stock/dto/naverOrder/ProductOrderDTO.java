package com.stock.dto.naverOrder;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.entity.naverOrder.OrderEntity;
import com.stock.entity.naverOrder.ProductOrderEntity;
import com.stock.entity.repository.naverOrder.OrderRepository;

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
public class ProductOrderDTO {

	@Autowired
	private OrderRepository orderRepository;

	@JsonProperty("productOrderId")
	private String productOrderId; // 상품 주문 번호

	@JsonProperty("claimStatus")
	private String claimStatus; //클레임 상태 (취소 요청, 취소 처리중, 취소 처리 완료, 반품 요청, 교환 요청, 수거 처리 중, 수거 완료, 반품 완료, 교환 완료, 교환 재배송 중 등)
	
	@JsonProperty("claimType")
	private String claimType; //클레임 구분 250바이트 내외 (CANCEL, RETURN, EXCHANGE, PURCHASE_DECISION_HOLDBACK, ADMIN_CANCEL)

	@JsonProperty("optionCode")
	private String optionCode; //옵션 코드, 1000바이트 내외
	
	@JsonProperty("optionPrice")
	private Integer optionPrice; //옵션 금액
	
	@JsonProperty("productClass")
	private String productClass; //상품 종류(일반/추가 상품구분, 250바이트 내외)
	
	@JsonProperty("groupProductId")
	private String groupProductId; //그룹 상품 번호
	
	@JsonProperty("productId")
	private String productId; //상품 번호

	@JsonProperty("originalProductId")
	private String originalProductId; //원상품번호, 150바이트 내외
	
	@JsonProperty("productName")
	private String productName; //상품명, 4000바이트 내외
	
	@JsonProperty("productOption")
	private String productOption; //상품 옵션(옵션명), 4000바이트 내외
	
	@JsonProperty("productOrderStatus")
	private String productOrderStatus; //상품 주문 상태. 250바이트 내외
	
	@JsonProperty("quantity")
	private Integer quantity; //최초 수량
	
	@JsonProperty("initialQuantity")
	private Integer initialQuantity; //최초 수량 (위에랑 차이가 뭐지?)
	
	@JsonProperty("remainQuantity")
	private Integer remainQuantity; //잔여 수량

	@JsonProperty("sellerProductCode")
	private String sellerProductCode; //판매자 상품 코드
	
	@JsonProperty("totalPaymentAmount")
	private Integer totalPaymentAmount; //최초 결제 금액(할인 적용 후 금액)
	
	@JsonProperty("remainPaymentAmount")
	private Integer remainPaymentAmount; //잔여 결제 금액(할인 적용 후 금액)
	
	@JsonProperty("paymentCommission")
	private Integer paymentCommission; //결제 수수료
	
	@JsonProperty("expectedSettlementAmount")
	private Integer expectedSettlementAmount; //정산 예정 금액
	
	

	// ProductOrderDTO -> ProductOrderEntity 변환
	public ProductOrderEntity dtoToProductOrderEntity(OrderEntity orderEntity) {
		return ProductOrderEntity.builder()
								.productOrderId(this.productOrderId)
								.claimStatus(this.claimStatus)
								.claimType(this.claimType)
								.optionCode(this.optionCode)
								.optionPrice(this.optionPrice)
								.productClass(this.productClass)
								.groupProductId(this.groupProductId)
								.productId(this.productId)
								.originalProductId(this.originalProductId)
								.productName(this.productName)
								.productOption(this.productOption)
								.productOrderStatus(this.productOrderStatus)
								.quantity(this.quantity)
								.remainQuantity(this.remainQuantity)
								.sellerProductCode(this.sellerProductCode)
								.totalPaymentAmount(this.totalPaymentAmount)
								.remainPaymentAmount(this.remainPaymentAmount)
								.paymentCommission(this.paymentCommission)
								.expectedSettlementAmount(this.expectedSettlementAmount)
								.orderEntity(orderEntity) // 외부에서 전달받은 OrderEntity를 설정
								.build();
	}


}
