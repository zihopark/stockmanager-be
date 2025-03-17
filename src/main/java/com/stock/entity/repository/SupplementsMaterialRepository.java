package com.stock.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.SupplementsMaterialEntity;

public interface SupplementsMaterialRepository extends JpaRepository<SupplementsMaterialEntity, Long> {

    //SupplementEntity의 Id를 기준으로 SupplementsMaterialEntity를 조회 (productOrderEntity의 optionCode = SupplementEntity의 supplementId)
    public List<SupplementsMaterialEntity> findBySupplementId(Long SupplementId);

    @Modifying
    @Query("DELETE FROM SupplementsMaterialEntity s WHERE s.supplement.id = :supplementId")
    public void deleteAllBySupplementId(@Param("supplementId") Long supplementId);

    
	public List<SupplementsMaterialEntity> findByMaterialId(Long materialId);
    
}