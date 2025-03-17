package com.stock.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stock.entity.naverProduct.ProductEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tbl_products_material")
public class ProductsMaterialEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productsmaterial_seq")
    @SequenceGenerator(name="productsmaterial_seq", sequenceName="productsmaterial_seq", initialValue = 1, allocationSize = 1)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties("productsMaterialEntity")
    private ProductEntity product; //제품

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnoreProperties("productsMaterials")
    private MaterialEntity material; //재료

    private int quantity; //이 재료가 특정 제품을 만드는 데에 필요한 수량

	
}
