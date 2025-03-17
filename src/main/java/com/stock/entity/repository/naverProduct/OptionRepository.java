package com.stock.entity.repository.naverProduct;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.naverProduct.OptionEntity;

public interface OptionRepository extends JpaRepository<OptionEntity, Long>{
    
    @Query("SELECT o FROM OptionEntity o JOIN FETCH o.productEntity")
    public List<OptionEntity> findAllWithProduct();
}
