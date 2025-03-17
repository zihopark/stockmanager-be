package com.stock.service;

import org.springframework.stereotype.Service;

import com.stock.dto.object.ApiProductResponse;
import com.stock.dto.object.ApiProductResponse.ChannelProduct;
import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.repository.naverProduct.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void saveProductData(ApiProductResponse apiProductResponse) {
         if (apiProductResponse.getContents() != null) {
            for (ApiProductResponse.Content content : apiProductResponse.getContents()) {
                if (content.getChannelProducts() != null) {
                        for (ChannelProduct channelProduct : content.getChannelProducts()) {
                                
                            ProductEntity productEntity = new ProductEntity();
                            productEntity.setOriginProductNo(channelProduct.getOriginProductNo());
                            productEntity.setGroupProductNo(channelProduct.getGroupProductNo());
                            productEntity.setChannelProductNo(channelProduct.getChannelProductNo());
                            productEntity.setName(channelProduct.getName());
                            productEntity.setSellerManagementCode(channelProduct.getSellerManagementCode());
                            productEntity.setStatusType(channelProduct.getStatusType());
                            productEntity.setSalePrice(channelProduct.getSalePrice());
                            productEntity.setDiscountedPrice(channelProduct.getDiscountedPrice());
                            productEntity.setStockQuantity(channelProduct.getStockQuantity());
                            productEntity.setRepresentativeImage(channelProduct.getRepresentativeImage().getUrl());
                            productEntity.setModelName(channelProduct.getModelName());
                            productEntity.setBrandName(channelProduct.getBrandName());
                            productEntity.setRegDate(channelProduct.getRegDate());
                            productEntity.setModifiedDate(channelProduct.getModifiedDate());
                        
                            productRepository.save(productEntity);
                    }
                }
            }
         }
    }
}

