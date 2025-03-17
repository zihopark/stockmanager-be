package com.stock.entity.repository;

import java.util.List;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.ProductsMaterialEntity;

public interface ProductsMaterialRepository extends JpaRepository<ProductsMaterialEntity, Long> {

    //ProductEntity의 Id를 기준으로 ProductsMaterialEntity를 조회 (productOrderEntity의 optionCode = ProductEntity의 Id값)
    public List<ProductsMaterialEntity> findByProduct_OriginProductNo(Long ProductId);

    @Modifying
    @Query("DELETE FROM ProductsMaterialEntity p WHERE p.product.channelProductNo = :productId")
    public void deleteAllByProductId(@Param("productId") Long productId);

	public List<ProductsMaterialEntity> findByMaterialId(Long materialId);

	@Query("SELECT p FROM ProductsMaterialEntity p WHERE p.product.id = :productId")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<ProductsMaterialEntity> findAllByProductIdWithLock(@Param("productId") Long productId);
}