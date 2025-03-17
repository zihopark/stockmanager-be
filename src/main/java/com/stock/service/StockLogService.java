package com.stock.service;

import java.util.List;

import com.stock.dto.StockLogDTO;
import com.stock.entity.StockLogEntity;

public interface StockLogService {

	public StockLogEntity addLog(StockLogDTO stockLogDTO);
	
	public List<StockLogEntity> getAllLogs();
	
	public List<StockLogDTO> getAllLogsAsDTO();
	
	public void deleteLog(Long id);
	
	public void deleteAll();
	
	public List<StockLogDTO> getSpecificLogsAsDTO(Long materialId);
	
}
