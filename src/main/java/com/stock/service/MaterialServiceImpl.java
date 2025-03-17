package com.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.dto.MaterialDTO;
import com.stock.dto.MaterialsFinalProductDTO;
import com.stock.entity.MaterialEntity;
import com.stock.entity.OptionsMaterialEntity;
import com.stock.entity.ProductsMaterialEntity;
import com.stock.entity.SupplementsMaterialEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.OptionsMaterialRepository;
import com.stock.entity.repository.ProductsMaterialRepository;
import com.stock.entity.repository.SupplementsMaterialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MaterialServiceImpl implements MaterialService{

	private final MaterialRepository materialRepository;
	private final SupplementsMaterialRepository supplementsMaterialRepository;
	private final ProductsMaterialRepository productsMaterialRepository;
	private final OptionsMaterialRepository optionsMaterialRepository;

    @Override
    public List<MaterialEntity> getAllMaterials() {
        return materialRepository.findAllMaterialsSortedByStockQuantity(); //재고 적은 순으로 정렬 
    }
    
    @Override
    public List<MaterialEntity> getAllMaterialOrderByName() {
        return materialRepository.findAllMaterialsSortedByName(); //이름 순으로 정렬 
    }
    
    @Override
    public MaterialEntity saveMaterial(MaterialDTO materialDTO) {
        MaterialEntity materialEntity = new MaterialEntity();
        
        materialEntity.setName(materialDTO.getName());
        materialEntity.setStockQuantity(materialDTO.getStockQuantity());
        materialEntity.setOnHoldStock(materialDTO.getOnHoldStock());
        materialEntity.setBadStock(materialDTO.getBadStock());
        materialEntity.setUnitCost(materialDTO.getUnitCost());
        materialEntity.setImageurl(materialDTO.getImageurl());
        materialEntity.setType(materialDTO.getType());

        return materialRepository.save(materialEntity);
    }
    
    
    @Transactional
    public void updateMaterial(Long id, MaterialEntity materialEntity) {
        
    	MaterialEntity material = materialRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 재료를 찾을 수 없습니다: " + id));

        material.setName(materialEntity.getName());
        material.setStockQuantity(materialEntity.getStockQuantity());
        material.setOnHoldStock(materialEntity.getOnHoldStock());
        material.setBadStock(materialEntity.getBadStock());
        material.setUnitCost(materialEntity.getUnitCost());
        material.setImageurl(materialEntity.getImageurl());
        material.setType(materialEntity.getType());

        materialRepository.save(material);
    }
    
    @Override
    @Transactional
    public void deleteMaterial(Long id) {
    	materialRepository.deleteById(id);
    }
    
    @Override
    public List<MaterialsFinalProductDTO> getMaterialsFinalProduct(Long materialId){
    	
    	List<MaterialsFinalProductDTO> result = new ArrayList<>();
        
    	List<ProductsMaterialEntity> productsMaterialEntities = productsMaterialRepository.findByMaterialId(materialId);
    	List<OptionsMaterialEntity> optionsMaterialEntities = optionsMaterialRepository.findByMaterialId(materialId);
    	List<SupplementsMaterialEntity> supplementsMaterialEntities = supplementsMaterialRepository.findByMaterialId(materialId);
		 
    	 // Process Products
        for (ProductsMaterialEntity pme : productsMaterialEntities) {
            MaterialsFinalProductDTO dto = new MaterialsFinalProductDTO();
            dto.setType("단일상품");
            dto.setId(pme.getProduct().getOriginProductNo());
            dto.setImageurl(pme.getProduct().getRepresentativeImage());
            dto.setName(pme.getProduct().getName());
            dto.setQuantity(pme.getQuantity());
            result.add(dto);
        }
        
        // Process Options
        for (OptionsMaterialEntity ome : optionsMaterialEntities) {
            MaterialsFinalProductDTO dto = new MaterialsFinalProductDTO();
            dto.setType("옵션");
            dto.setId(ome.getOption().getId());
         
            StringBuilder optionNameBuilder = new StringBuilder();
            if (ome.getOption().getOptionName1() != null && !ome.getOption().getOptionName1().isEmpty()) {
                optionNameBuilder.append(ome.getOption().getOptionName1());
            }
            if (ome.getOption().getOptionName2() != null && !ome.getOption().getOptionName2().isEmpty()) {
                if (optionNameBuilder.length() > 0) optionNameBuilder.append(" ");
                optionNameBuilder.append(ome.getOption().getOptionName2());
            }
            if (ome.getOption().getOptionName3() != null && !ome.getOption().getOptionName3().isEmpty()) {
                if (optionNameBuilder.length() > 0) optionNameBuilder.append(" ");
                optionNameBuilder.append(ome.getOption().getOptionName3());
            }
            
            dto.setImageurl(ome.getOption().getProductEntity().getRepresentativeImage());
            dto.setName(optionNameBuilder.toString());
            dto.setQuantity(ome.getQuantity());
            result.add(dto);
        }
        
        // Process Supplements
        for (SupplementsMaterialEntity sme : supplementsMaterialEntities) {
            MaterialsFinalProductDTO dto = new MaterialsFinalProductDTO();
            dto.setType("추가상품");
            dto.setId(sme.getSupplement().getId());
            dto.setImageurl(sme.getSupplement().getProductEntity().getRepresentativeImage());
            dto.setName(sme.getSupplement().getName());
            dto.setQuantity(sme.getQuantity());
            result.add(dto);
        }
        
        return result;
      	
    }
    
    @Override
    @Transactional
    public void stockFromLog(Long id, int quantity, String type) {
    	
    	if(type.equals("outbound")) {
    		materialRepository.quantityDecrease(id, quantity);
    	} else if (type.equals("inbound")) {
    		materialRepository.quantityIncrease(id, quantity);
    	} else if (type.equals("bad")) {
    		materialRepository.badIncrease(id, quantity);
    		materialRepository.quantityDecrease(id, quantity);
    	} else { //'보류'일 때 
    		materialRepository.onHoldIncrease(id, quantity);
    	}
    }
    
    
    
    
}
