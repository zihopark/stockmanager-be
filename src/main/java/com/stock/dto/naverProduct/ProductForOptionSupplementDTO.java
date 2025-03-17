package com.stock.dto.naverProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductForOptionSupplementDTO { //옵션 및 추가 상품 조회를 위해 필요한 필드만 포함하는 DTO
    private Long originProductNo;
    private String name;
    private String representativeImage;

}