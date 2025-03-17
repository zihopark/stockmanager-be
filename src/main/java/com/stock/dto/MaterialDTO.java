package com.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.dto.naverProduct.OptionDTO;
import com.stock.dto.naverProduct.ProductForOptionSupplementDTO;
import com.stock.entity.MaterialEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialDTO {
	

    @JsonProperty("name")  // JSON의 "name" 필드와 매핑
    private String name;

    @JsonProperty("stockQuantity")  // JSON의 "stockQuantity" 필드와 매핑
    private Integer stockQuantity;

    @JsonProperty("onHoldStock")  // JSON의 "onHoldStock" 필드와 매핑
    private Integer onHoldStock;

    @JsonProperty("badStock")  // JSON의 "faultyStock" 필드와 매핑
    private Integer badStock;
    
    @JsonProperty("unitCost")  // JSON의 "unitCost" 필드와 매핑
    private Integer unitCost;

    @JsonProperty("imageurl")  // JSON의 "imageurl" 필드와 매핑
    private String imageurl;

    @JsonProperty("type")  // JSON의 "type" 필드와 매핑
    private String type;
    
    private String customType;
}
