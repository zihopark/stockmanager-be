package com.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.entity.naverProduct.SupplementEntity;

@Service
public interface SupplementService {
    
    public List<SupplementEntity> getAllSupplements(); 
}
