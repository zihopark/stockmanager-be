package com.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsMaterialDTO {

	 private Long id;
	 private Long productId;
	 private Long materialId;
	 private int quantity;
	 
	 private String productName;
	 private String materialName;
	 private String materialImageUrl;
	 
}
