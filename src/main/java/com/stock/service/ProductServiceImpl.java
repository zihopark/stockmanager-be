package com.stock.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.repository.naverProduct.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public List<ProductEntity> getAllProducts() {
    	List<ProductEntity> products = productRepository.findAll();
        for (ProductEntity product : products) {
            Hibernate.initialize(product.getOptions());
            Hibernate.initialize(product.getSupplements());
        }
        return products;
    }
}
