package com.stock.dto.naverProduct;

import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.naverProduct.SupplementEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplementDTO {
    
    private Long id;
    private String groupName; //추가 상품 그룹명 (추가 상품명)
    private String name; //추가 상품명 (추가 상품값)
    private long price; //추가 상품 금액
    private long stockQuantity; //추가 상품 재고
    private String sellerManagementCode; //추가 상품 판매자 관리 코드
    private boolean usable; //추가 상품 사용 여부
    private ProductForOptionSupplementDTO productEntity;


    // SupplementDTO -> SupplementEntity 변환 
    public SupplementEntity dtoToSupplementEntity(ProductEntity productEntity) {
        return SupplementEntity.builder()
                                .id(this.id)
                                .groupName(this.groupName)
                                .name(this.name)
                                .price(this.price)
                                .stockQuantity(this.stockQuantity)
                                .sellerManagementCode(this.sellerManagementCode)
                                .usable(this.usable)
                                .productEntity(productEntity)
                                .build();
    }
}
