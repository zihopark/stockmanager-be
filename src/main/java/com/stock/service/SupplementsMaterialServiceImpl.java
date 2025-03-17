package com.stock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stock.dto.SupplementsMaterialDTO;
import com.stock.entity.MaterialEntity;
import com.stock.entity.SupplementsMaterialEntity;
import com.stock.entity.naverProduct.SupplementEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.SupplementsMaterialRepository;
import com.stock.entity.repository.naverProduct.SupplementRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SupplementsMaterialServiceImpl implements SupplementsMaterialService{
	
	private final SupplementsMaterialRepository supplementsMaterialRepository;
	private final SupplementRepository supplementRepository;
	private final MaterialRepository materialRepository;
	
	//추가상품에 재료 매칭
	@Transactional
	@Override
	public SupplementsMaterialEntity addSupplementsMaterial(SupplementsMaterialDTO request) {
		SupplementEntity supplement = supplementRepository.findById(request.getSupplementId())
        		.stream()
        	    .findFirst()
        		.orElseThrow(() -> new IllegalArgumentException("Supplement not found"));
        
		MaterialEntity material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Material not found"));

        SupplementsMaterialEntity supplementsMaterial = new SupplementsMaterialEntity();
        supplementsMaterial.setSupplement(supplement);
        supplementsMaterial.setMaterial(material);
        supplementsMaterial.setQuantity(request.getQuantity());

        return supplementsMaterialRepository.save(supplementsMaterial);
	}

	@Override
	public List<SupplementsMaterialDTO> getMaterialsBySupplement(Long supplementId) {
		 List<SupplementsMaterialEntity> entities = supplementsMaterialRepository.findBySupplementId(supplementId);
		 
	    // Entity -> DTO 변환
	    return entities.stream()
    		.map(entity -> SupplementsMaterialDTO.builder()
    			.id(entity.getId())
	            .supplementId(entity.getSupplement().getId()) // 1. Supplement ID
	            .materialId(entity.getMaterial().getId())     // 2. Material ID
	            .quantity(entity.getQuantity())               // 3. Quantity
	            .supplementName(entity.getSupplement().getName()) // 4. Supplement Name
	            .materialName(entity.getMaterial().getName())     // 5. Material Name
	            .materialImageUrl(entity.getMaterial().getImageurl()) // 6. Material Image URL
	            .build()
	        )
        .collect(Collectors.toList());
	}
	
	//특정 product 에 대한 재료를 모두 삭제하는 메소드
	@Transactional
	@Override
    public void deleteAllBySupplementId(Long supplementId) {
		supplementsMaterialRepository.deleteAllBySupplementId(supplementId);
		//System.out.println("추가상품 기존 정보 삭제 완료");
	}

}
