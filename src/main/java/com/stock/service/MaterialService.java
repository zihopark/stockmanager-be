package com.stock.service;

import java.util.List;

import com.stock.dto.MaterialDTO;
import com.stock.dto.MaterialsFinalProductDTO;
import com.stock.entity.MaterialEntity;

public interface MaterialService {

	public List<MaterialEntity> getAllMaterials();
	
	public List<MaterialEntity> getAllMaterialOrderByName() ;
	
	public MaterialEntity saveMaterial(MaterialDTO materialDTO);
	 
	public void updateMaterial(Long id, MaterialEntity materialEntity);
	
	public void deleteMaterial(Long id);
	
	public List<MaterialsFinalProductDTO> getMaterialsFinalProduct(Long materialId);
	
	public void stockFromLog(Long id, int quantity, String type);
}
