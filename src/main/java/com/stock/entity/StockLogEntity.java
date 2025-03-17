package com.stock.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="tbl_stocklog")
public class StockLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stocklog_seq")
    @SequenceGenerator(name="stocklog_seq", sequenceName="stocklog_seq", initialValue = 1, allocationSize = 1)
	private Long id;

    @Column(nullable = false)
    private LocalDateTime logDate;

    @Column
    private String type; //입고(inbound), 불량(bad), 출고(outbound), 보류(onhold) 

    @Column(nullable = true)
    private Integer quantity;
    
    @Column(length = 2)
    private String isUserInput; //Y or N
    
    @Column(nullable = true)
    private String content; //log 내용
    
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "material_id")
    @JsonBackReference
    private MaterialEntity materialEntity;
    
}
