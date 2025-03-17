package com.stock.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stock.dto.ProductsMaterialDTO;
import com.stock.dto.SupplementsMaterialDTO;
import com.stock.entity.MaterialEntity;
import com.stock.entity.ProductsMaterialEntity;
import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.ProductsMaterialRepository;
import com.stock.entity.repository.naverProduct.ProductRepository;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductsMaterialServiceImpl implements ProductsMaterialService {

	private final ProductsMaterialRepository productsMaterialRepository;
	private final ProductRepository productRepository;
	private final MaterialRepository materialRepository;
	
	//Product에 재료 매칭 
	@Transactional
	@Override
	public ProductsMaterialEntity addProductsMaterial(ProductsMaterialDTO productsMaterialDTO) {
	    ProductEntity product = productRepository.findByChannelProductNo(productsMaterialDTO.getProductId())
	            .stream()
	            .findFirst()
	            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

	    MaterialEntity material = materialRepository.findById(productsMaterialDTO.getMaterialId())
	            .orElseThrow(() -> new IllegalArgumentException("Material not found"));

	    ProductsMaterialEntity productsMaterial = new ProductsMaterialEntity();
	    productsMaterial.setProduct(product);
	    productsMaterial.setMaterial(material);
	    productsMaterial.setQuantity(productsMaterialDTO.getQuantity());

	    return productsMaterialRepository.save(productsMaterial);
	}


	
	//제품을 구성하는 재료 보여주는 메소드 
	@Override
    public List<ProductsMaterialDTO> getMaterialsByProduct(Long productId) {
        List<ProductsMaterialEntity> entities = productsMaterialRepository.findByProduct_OriginProductNo(productId);
    
        // Entity -> DTO 변환
	    return entities.stream()
    		.map(entity -> ProductsMaterialDTO.builder()
    			.id(entity.getId())
	            .productId(entity.getProduct().getOriginProductNo()) 
	            .materialId(entity.getMaterial().getId())    
	            .quantity(entity.getQuantity())               
	            .productName(entity.getProduct().getName()) 
	            .materialName(entity.getMaterial().getName())     
	            .materialImageUrl(entity.getMaterial().getImageurl()) 
	            .build()
	        )
        .collect(Collectors.toList());
	
	}
	
	//특정 product 에 대한 재료를 모두 삭제하는 메소드
	@Transactional
	@Override
    public void deleteAllByProductId(Long productId) {
			productsMaterialRepository.deleteAllByProductId(productId);
		    //System.out.println("기존 정보 삭제 완료");
		
	}
	
}
