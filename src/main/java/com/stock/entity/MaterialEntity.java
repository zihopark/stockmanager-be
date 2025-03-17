package com.stock.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="tbl_material")
public class MaterialEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_seq")
    @SequenceGenerator(name="material_seq", sequenceName="material_seq", initialValue = 1, allocationSize = 1)
	private Long id;

    @Column(length=50, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = true)
    private Integer onHoldStock; //보류된 재고
    
    @Column(nullable = true)
    private Integer badStock; //불량품 재고 

    @Column(nullable = true)
    private Integer unitCost;
    
    @Column(nullable = true)
    private String imageurl;
    
    @Column(nullable = true)
    private String type; //포장용품인지 실재료 인지?
    
    
    @OneToMany(mappedBy = "materialEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<StockLogEntity> stockLogs = new ArrayList<>();

}
