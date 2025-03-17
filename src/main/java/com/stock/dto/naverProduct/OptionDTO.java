package com.stock.dto.naverProduct;

import com.stock.entity.naverProduct.OptionEntity;
import com.stock.entity.naverProduct.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDTO {

    private Long id;
    private String optionName1; //조합형 옵션값 1 
    private String optionName2;
    private String optionName3;
    private String optionName4;
    private long stockQuantity;
    private long price; //추가되는 옵션가격
    private String sellerManagerCode;
    private boolean usable; //옵션 사용 여부
    private ProductForOptionSupplementDTO productEntity;

     // OptionDTO -> OptionEntity 변환
    public OptionEntity dtoToOptionEntity(ProductEntity productEntity) {
        return OptionEntity.builder()
                           .id(this.id)
                           .optionName1(this.optionName1)
                           .optionName2(this.optionName2)
                           .optionName3(this.optionName3)
                           .optionName4(this.optionName4)
                           .stockQuantity(this.stockQuantity)
                           .price(this.price)
                           .sellerManagerCode(this.sellerManagerCode)
                           .usable(this.usable)
                           .productEntity(productEntity) // 외부에서 전달받은 ProductEntity를 설정
                           .build();
    }
}


