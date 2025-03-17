package com.stock.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.MaterialEntity;
import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.repository.naverProduct.ProductRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {

	 @Query("SELECT m FROM MaterialEntity m LEFT JOIN FETCH m.stockLogs ORDER BY m.stockQuantity ASC")
	 public List<MaterialEntity> findAllMaterialsSortedByStockQuantity();
	 
	 @Query("SELECT m FROM MaterialEntity m LEFT JOIN FETCH m.stockLogs ORDER BY m.name ASC")
	 public List<MaterialEntity> findAllMaterialsSortedByName();
	 
	 @Modifying
	 @Query("UPDATE MaterialEntity m SET m.stockQuantity = m.stockQuantity - :quantity WHERE m.id = :id")
	 public void quantityDecrease(@Param("id") Long id, @Param("quantity") int quantity);

	 @Modifying
	 @Query("UPDATE MaterialEntity m SET m.stockQuantity = m.stockQuantity + :quantity WHERE m.id = :id")
	 public void quantityIncrease(@Param("id") Long id, @Param("quantity") int quantity);

	 @Modifying
	 @Query("UPDATE MaterialEntity m SET m.onHoldStock = m.onHoldStock + :quantity WHERE m.id = :id")
	 public void onHoldIncrease(@Param("id") Long id, @Param("quantity") int quantity);

	 @Modifying
	 @Query("UPDATE MaterialEntity m SET m.badStock = m.badStock + :quantity WHERE m.id = :id")
	 public void badIncrease(@Param("id") Long id, @Param("quantity") int quantity);
	 
}