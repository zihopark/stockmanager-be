package com.stock.entity;

import com.stock.entity.naverProduct.OptionEntity;

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
@Table(name="tbl_options_material")
public class OptionsMaterialEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "optionsmaterial_seq")
    @SequenceGenerator(name="optionsmaterial_seq", sequenceName="optionsmaterial_seq", initialValue = 1, allocationSize = 1)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private OptionEntity option; //옵션

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private MaterialEntity material; //재료


    private int quantity; //이 재료가 특정 제품을 만드는 데에 필요한 수량

}
