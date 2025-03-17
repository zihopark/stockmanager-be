package com.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplementsMaterialDTO {

	 private Long id;
	 private Long supplementId;
	 private Long materialId;
	 private int quantity;
	 
	 private String supplementName;
	 private String materialName;
	 private String materialImageUrl;
	 
}
