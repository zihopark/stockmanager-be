package com.stock.service;

import java.util.List;

import com.stock.dto.OptionsMaterialDTO;
import com.stock.entity.OptionsMaterialEntity;

import jakarta.transaction.Transactional;

public interface OptionsMaterialService {

	//Option에 재료 매칭 
	public OptionsMaterialEntity addOptionsMaterial(OptionsMaterialDTO optionsMaterialDTO);
	
	//제품을 구성하는 재료 보여주는 메소드 
	public List<OptionsMaterialDTO> getMaterialsByOption(Long optionId) ;
	
	//특정 option 에 대한 재료를 모두 삭제하는 메소드
	public void deleteAllByOptionId(Long optionId);
}
