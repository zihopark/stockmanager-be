package com.stock.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.OptionsMaterialEntity;

public interface OptionsMaterialRepository extends JpaRepository<OptionsMaterialEntity, Long> {

    //OptionEntity의 Id를 기준으로 OptionsMaterialEntity를 조회 (productOrderEntity의 optionCode = OptionEntity의 optionId)
    public List<OptionsMaterialEntity> findByOptionId(Long OptionId);
    
    @Modifying
    @Query("DELETE FROM OptionsMaterialEntity o WHERE o.option.id = :optionId")
    public void deleteAllByOptionId(@Param("optionId") Long optionId);

	public List<OptionsMaterialEntity> findByMaterialId(Long materialId);

}