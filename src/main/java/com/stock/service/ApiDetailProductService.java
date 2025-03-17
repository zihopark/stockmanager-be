package com.stock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stock.dto.object.ApiDetailProductResponse;
import com.stock.entity.naverProduct.OptionEntity;
import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.naverProduct.SupplementEntity;
import com.stock.entity.repository.naverProduct.OptionRepository;
import com.stock.entity.repository.naverProduct.ProductRepository;
import com.stock.entity.repository.naverProduct.SupplementRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiDetailProductService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final SupplementRepository supplementRepository;
    
    
    // OptionCombination 데이터를 OptionEntity로 저장
    @Transactional
    public void saveOptionData(List<ApiDetailProductResponse.OriginProduct.DetailAttribute.OptionInfo.OptionCombination> optionCombinations, Long productId) {
        ProductEntity productEntity = productRepository.findById(productId).get();

         List<OptionEntity> optionEntities = optionCombinations.stream()
                .map(option -> {
                    OptionEntity optionEntity = optionRepository.findById(option.getId())
                                                                .orElse(OptionEntity.builder()
                                                                        .id(option.getId())
                                                                        .build());
                                optionEntity.setOptionName1(option.getOptionName1());
                                optionEntity.setOptionName2(option.getOptionName2());
                                optionEntity.setOptionName3(option.getOptionName3());
                                optionEntity.setOptionName4(option.getOptionName4());
                                optionEntity.setStockQuantity(option.getStockQuantity());
                                optionEntity.setPrice(option.getPrice());
                                optionEntity.setSellerManagerCode(option.getSellerManagerCode());
                                optionEntity.setUsable(option.isUsable());
                                optionEntity.setProductEntity(productEntity); // 연관 관계 설정
                                return optionEntity;
                })
                .collect(Collectors.toList());

        optionRepository.saveAll(optionEntities);
    }



     // SupplementProduct 데이터를 SupplementEntity로 저장
     @Transactional
     public void saveSupplementData(List<ApiDetailProductResponse.OriginProduct.DetailAttribute.SupplementProductInfo.SupplementProduct> supplementProducts, Long productId) {
        ProductEntity productEntity = productRepository.findById(productId).get();

        List<SupplementEntity> supplementEntities = supplementProducts.stream()
                 .map(supplement -> {
                        SupplementEntity supplementEntity = supplementRepository.findById(supplement.getId())
                                                                                .orElse(SupplementEntity.builder()
                                                                                        .id(supplement.getId())
                                                                                        .build());
                                        supplementEntity.setGroupName(supplement.getGroupName());
                                        supplementEntity.setName(supplement.getName());
                                        supplementEntity.setPrice(supplement.getPrice());
                                        supplementEntity.setStockQuantity(supplement.getStockQuantity());
                                        supplementEntity.setSellerManagementCode(supplement.getSellerManagementCode());
                                        supplementEntity.setUsable(supplement.isUsable());
                                        supplementEntity.setProductEntity(productEntity); // 연관 관계 설정
                                        return supplementEntity;
                })
                .collect(Collectors.toList());


         supplementRepository.saveAll(supplementEntities);
     }

}

