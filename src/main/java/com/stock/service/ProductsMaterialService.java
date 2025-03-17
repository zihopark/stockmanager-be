package com.stock.service;

import java.util.List;

import com.stock.dto.ProductsMaterialDTO;
import com.stock.entity.ProductsMaterialEntity;

public interface ProductsMaterialService {

	//Product에 재료 매칭 
	public ProductsMaterialEntity addProductsMaterial(ProductsMaterialDTO request);
	
	//제품을 구성하는 재료 보여주는 메소드 
    public List<ProductsMaterialDTO> getMaterialsByProduct(Long productId);
    
    //특정 product 에 대한 재료를 모두 삭제하는 메소드
    public void deleteAllByProductId(Long productId);
}
