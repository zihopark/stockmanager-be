package com.stock.dto;

import lombok.Data;

@Data
public class MaterialsFinalProductDTO {

	//materialId 를 통해 검색된 해당 재료가 어떤 final product를 만드는지 보여주기 위한 DTO
	
	private String type; //option? Product? Supplement?
	
	private Long id; //optionId? ProductId? SupplementId?
	
	private String imageurl; //각 제품에 대한 사진 
	
	private String name; //optionName? ProductName? SupplementName?
	
	private int quantity;
	
}
