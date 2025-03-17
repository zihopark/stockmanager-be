package com.stock.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockLogDTO {
	
	private Long id;
	private LocalDateTime logDate;
	
	private Integer quantity;
	private String type;
	private String content;
	
	private Long materialId;
	private String materialName;
	private String isUserInput;
	
}
