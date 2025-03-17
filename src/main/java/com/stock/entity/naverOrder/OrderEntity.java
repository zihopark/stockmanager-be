package com.stock.entity.naverOrder;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="tbl_order")
public class OrderEntity {

	@Id
    @Column(name = "order_id", length = 20)
    private String orderId; // 주문 번호. 20바이트 내외
	
	@Column(name = "payment_date", length = 45)
	private String paymentDate; //결제 일시, 45바이트 내외
	
	@Column(name = "orderer_name", length = 300)
	private String ordererName; //주문자이름, 300바이트 내외



	// ProductOrderEntity와의 연관관계 (1:N) 이건 칼럼은 아니고 그냥 엔티티 간의 관계를 표현하기 위한 매핑 정보일 뿐. OneToMany는 칼럼이 아님.
    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<ProductOrderEntity> productOrders = new ArrayList<>(); // 초기화 추가
	
    
    
    
}
