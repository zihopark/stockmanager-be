package com.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionsMaterialDTO {

	 private Long id;
	 private Long optionId;
	 private Long materialId;
	 private int quantity;
	 
	 private String optionName;
	 private String materialName;
	 private String materialImageUrl;
	 
}
