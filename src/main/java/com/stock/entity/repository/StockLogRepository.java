package com.stock.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.StockLogEntity;

public interface StockLogRepository extends JpaRepository<StockLogEntity, Long> {

	@Query("SELECT s FROM StockLogEntity s ORDER BY s.logDate DESC")
	public List<StockLogEntity> findAllSortedByLogDate();

	@Query("SELECT s FROM StockLogEntity s WHERE s.materialEntity.id = :materialId ORDER BY s.logDate DESC")
	public List<StockLogEntity> findByMaterialId(@Param("materialId") Long materialId);
}
