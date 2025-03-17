package com.stock.entity.repository.naverProduct;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.naverProduct.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    
    @Query("SELECT p.originProductNo FROM ProductEntity p")
    public List<Long> findAllProductIds();
    
    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.options LEFT JOIN FETCH p.supplements")
    List<ProductEntity> getAllProducts();
    
    public List<ProductEntity> findByChannelProductNo(Long channelProductNo);

}
