package com.stock.entity.naverProduct;

import java.util.List;

import com.stock.entity.SupplementsMaterialEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="tbl_supplement")  
public class SupplementEntity {
    
    ///////////SupplementProduct 안에 있는 값.//////////
  
    @Id
    @Column(name = "supplement_id")
    private Long id;

    private String groupName; //추가 상품 그룹명 (추가 상품명)
    private String name; //추가 상품명 (추가 상품값)
    private long price; //추가 상품 금액
    private long stockQuantity; //추가 상품 재고
    private String sellerManagementCode; //추가 상품 판매자 관리 코드
    private boolean usable; //추가 상품 사용 여부

    //////////////SupplementProduct 안에 있는 값 End//////////

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity; // ProductEntity와의 연관관계


    @OneToMany(mappedBy = "supplement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplementsMaterialEntity> supplementsMaterialEntity; //추가 상품 - 재료 관계 리스트

}
