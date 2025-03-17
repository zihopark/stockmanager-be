package com.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.entity.naverProduct.OptionEntity;


@Service
public interface OptionService {
    
    public List<OptionEntity> getAllOptions();
}
