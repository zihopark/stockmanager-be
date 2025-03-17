package com.stock.entity.naverOrder;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tbl_productorder")
public class ProductOrderEntity {

	@Id
    @Column(name = "product_order_id", length = 20)
    private String productOrderId; // 상품 주문 번호

	//OrderEntity와의 연관관계 (N:1)
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // 외래 키 설정
    private OrderEntity orderEntity;

	private String claimStatus; //클레임 상태 (취소 요청, 취소 처리중, 취소 처리 완료, 반품 요청, 교환 요청, 수거 처리 중, 수거 완료, 반품 완료, 교환 완료, 교환 재배송 중 등)
	
	private String claimType; //클레임 구분 250바이트 내외 (CANCEL, RETURN, EXCHANGE, PURCHASE_DECISION_HOLDBACK, ADMIN_CANCEL)

	private String optionCode; //옵션 코드, 1000바이트 내외 > 옵션 자체가 설정되지 않은 제품은 ProductEntity의 channelProductNo로 자동 설정.
	
	private Integer optionPrice; //옵션 금액
	
	private String productClass; //상품 종류(일반/추가 상품구분, 250바이트 내외)
	
	private String groupProductId; //그룹 상품 번호

	private String productId; //상품 번호 > ProductEntity의 channelProductNo
	
	private String originalProductId; //원상품번호, 150바이트 내외 > ProductEntity의 originProductNo
	
	private String productName; //상품명, 4000바이트 내외
	
	private String productOption; //상품 옵션(옵션명), 4000바이트 내외
	
	private String productOrderStatus; //상품 주문 상태, 250바이트 내외 (PAYMENT_WAITING, PAYED, DELIVERING, DELIVERED, PURCHASE_DECIDED, EXCHANGED, CANCELED, RETURNED, CANCELED_BY_NOPAYMENT)
	
	private Integer quantity; //최초 수량
	
	private Integer initialQuantity; //최초 수량 (위에랑 차이가 뭐지?)
	
	private Integer remainQuantity; //잔여 수량

	private String sellerProductCode; //판매자 상품 코드
	
	private Integer totalPaymentAmount; //최초 결제 금액(할인 적용 후 금액)
	
	private Integer remainPaymentAmount; //잔여 결제 금액(할인 적용 후 금액)
	
	private Integer paymentCommission; //결제 수수료
	
	private Integer expectedSettlementAmount; //정산 예정 금액
	
}
