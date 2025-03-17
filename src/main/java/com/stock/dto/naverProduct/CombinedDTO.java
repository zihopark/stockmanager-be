package com.stock.dto.naverProduct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CombinedDTO {
    private Long id;
    private String type;
    private String name;
    private long addedPrice;
    private long price;
    private long stockQuantity;
    private ProductForOptionSupplementDTO productEntity;
    
}