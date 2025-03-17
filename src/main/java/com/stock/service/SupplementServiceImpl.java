package com.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.entity.naverProduct.SupplementEntity;
import com.stock.entity.repository.naverProduct.SupplementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplementServiceImpl implements SupplementService {
    
    private final SupplementRepository supplementRepository;

    @Override
    public List<SupplementEntity> getAllSupplements() {
        return supplementRepository.findAll();
    }
}
