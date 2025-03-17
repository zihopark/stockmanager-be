package com.stock.entity;

import com.stock.entity.naverProduct.SupplementEntity;

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
@Table(name="tbl_supplements_material")
public class SupplementsMaterialEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplementsmaterial_seq")
    @SequenceGenerator(name="supplementsmaterial_seq", sequenceName="supplementsmaterial_seq", initialValue = 1, allocationSize = 1)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "supplement_id", nullable = false)
    private SupplementEntity supplement; //옵션

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private MaterialEntity material; //재료

    private int quantity; //이 재료가 특정 제품을 만드는 데에 필요한 수량

}
