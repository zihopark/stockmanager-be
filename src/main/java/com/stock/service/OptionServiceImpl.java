package com.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stock.entity.naverProduct.OptionEntity;
import com.stock.entity.repository.naverProduct.OptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    
    private final OptionRepository optionRepository;

    @Override
    public List<OptionEntity> getAllOptions() {
        return optionRepository.findAll();
    }
}
