package com.stock.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.SettingsEntity;

public interface SettingsRepository extends JpaRepository<SettingsEntity, String> {

	@Query("SELECT s.value FROM SettingsEntity s WHERE s.key = 'deliveredTime'")
	public String getDeliveredTime();
}
