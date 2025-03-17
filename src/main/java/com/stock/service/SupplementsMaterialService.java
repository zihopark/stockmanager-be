package com.stock.service;

import java.util.List;

import com.stock.dto.SupplementsMaterialDTO;
import com.stock.entity.SupplementsMaterialEntity;

import jakarta.transaction.Transactional;

public interface SupplementsMaterialService {

	//Supplement에 재료 매칭 
	public SupplementsMaterialEntity addSupplementsMaterial(SupplementsMaterialDTO request);
	
	//제품을 구성하는 재료 보여주는 메소드 
	public List<SupplementsMaterialDTO> getMaterialsBySupplement(Long supplementId);

	//특정 product 에 대한 재료를 모두 삭제하는 메소드
	public void deleteAllBySupplementId(Long supplementId);
	
}
