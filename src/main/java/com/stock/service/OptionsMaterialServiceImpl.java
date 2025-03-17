package com.stock.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.stock.dto.OptionsMaterialDTO;
import com.stock.entity.MaterialEntity;
import com.stock.entity.OptionsMaterialEntity;
import com.stock.entity.naverProduct.OptionEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.OptionsMaterialRepository;
import com.stock.entity.repository.naverProduct.OptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OptionsMaterialServiceImpl implements OptionsMaterialService{

	private final OptionsMaterialRepository optionsMaterialRepository;
	private final OptionRepository optionRepository;
	private final MaterialRepository materialRepository;
	
	//Option에 재료 매칭 
	@Transactional
	@Override
	public OptionsMaterialEntity addOptionsMaterial(OptionsMaterialDTO optionsMaterialDTO) {
        OptionEntity option = optionRepository.findById(optionsMaterialDTO.getOptionId())
        		.stream()
        	    .findFirst()
        		.orElseThrow(() -> new IllegalArgumentException("Option not found"));
        
        MaterialEntity material = materialRepository.findById(optionsMaterialDTO.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Material not found"));

        OptionsMaterialEntity optionsMaterial = new OptionsMaterialEntity();
        optionsMaterial.setOption(option);
        optionsMaterial.setMaterial(material);
        optionsMaterial.setQuantity(optionsMaterialDTO.getQuantity());

        return optionsMaterialRepository.save(optionsMaterial);
    }

	
	//제품을 구성하는 재료 보여주는 메소드 
	@Override
    public List<OptionsMaterialDTO> getMaterialsByOption(Long optionId) {
        List<OptionsMaterialEntity> entities = optionsMaterialRepository.findByOptionId(optionId);
        
        // Entity -> DTO 변환
        return entities.stream()
		    .map((OptionsMaterialEntity entity) -> {
		        String combinedOptionName = Stream.of(
		            entity.getOption().getOptionName1(),
		            entity.getOption().getOptionName2(),
		            entity.getOption().getOptionName3(),
		            entity.getOption().getOptionName4()
		        )
		        .filter(Objects::nonNull)
		        .filter(s -> !s.isEmpty())
		        .collect(Collectors.joining(" "));
	
		        return OptionsMaterialDTO.builder()
		        	.id(entity.getId())
		            .optionId(entity.getOption().getId())
		            .materialId(entity.getMaterial().getId())
		            .quantity(entity.getQuantity())
		            .optionName(combinedOptionName)
		            .materialName(entity.getMaterial().getName())
		            .materialImageUrl(entity.getMaterial().getImageurl())
		            .build();
		    })
		    .collect(Collectors.toList());

    }
	
	//특정 option 에 대한 재료를 모두 삭제하는 메소드
	@Transactional
	@Override
    public void deleteAllByOptionId(Long optionId) {
		optionsMaterialRepository.deleteAllByOptionId(optionId);
	}
		
}
