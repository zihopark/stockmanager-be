package com.stock.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.stock.dto.StockLogDTO;
import com.stock.entity.MaterialEntity;
import com.stock.entity.StockLogEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.StockLogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StockLogServiceImpl implements StockLogService{

	private final MaterialRepository materialRepository;
	private final StockLogRepository stockLogRepository;
	
	
	@Override
	public StockLogEntity addLog(StockLogDTO stockLogDTO) {
		
		MaterialEntity material = materialRepository.findById(stockLogDTO.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Material not found"));

		String defaultContent = "";
		String materialName = material.getName();
		
		if (stockLogDTO.getType().equals("inbound")) {
			defaultContent = materialName + " " + stockLogDTO.getQuantity()+ "EA 입고";
		} else if (stockLogDTO.getType().equals("onhold")) {                                                                                                                                                                                                                                                                                              
			defaultContent = materialName + " " + stockLogDTO.getQuantity()+ "EA 보류";
		} else if (stockLogDTO.getType().equals("bad")) {
			defaultContent = materialName + " " + stockLogDTO.getQuantity()+ "EA 불량";
		} else { //outbound
			defaultContent = materialName + " " + stockLogDTO.getQuantity()+ "EA 재고감소";
		}
		
		if (!(stockLogDTO.getContent().equals(""))) {
			defaultContent = defaultContent + " | " + stockLogDTO.getContent();
		}
		
		StockLogEntity stockLogEntity = new StockLogEntity();
		
		stockLogEntity.setQuantity(stockLogDTO.getQuantity());
		stockLogEntity.setIsUserInput(stockLogDTO.getIsUserInput());
		stockLogEntity.setContent(defaultContent);
		stockLogEntity.setType(stockLogDTO.getType());
		stockLogEntity.setLogDate(LocalDateTime.now());
		stockLogEntity.setMaterialEntity(material);
		
		return stockLogRepository.save(stockLogEntity);
		
	}
	
	@Override
	public List<StockLogEntity> getAllLogs(){
		return stockLogRepository.findAllSortedByLogDate();
	}
	
	@Override
	public List<StockLogDTO> getAllLogsAsDTO() {
        List<StockLogEntity> logList = stockLogRepository.findAllSortedByLogDate();
        return logList.stream()
                      .map(this::convertToDTO)
                      .collect(Collectors.toList());
	}

    private StockLogDTO convertToDTO(StockLogEntity entity) {
        StockLogDTO dto = new StockLogDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setQuantity(entity.getQuantity());
        dto.setContent(entity.getContent());
        dto.setLogDate(entity.getLogDate());
        dto.setIsUserInput(entity.getIsUserInput());
        dto.setMaterialName(entity.getMaterialEntity().getName());
        return dto;
    }
    
    @Override
    public void deleteLog (Long id) {
    	Optional<StockLogEntity> stockOptional = stockLogRepository.findById(id);
    	
    	if (stockOptional.isPresent()) {
    		stockLogRepository.delete(stockOptional.get());
    		//System.out.println(id + "로그가 삭제됐습니다.");
    	} else {
    		System.out.println("StockLogEntity not found for id : " + id);
    	}
    }
    
    @Override
    public void deleteAll() {
    	stockLogRepository.deleteAll();
        System.out.println("전체 로그 삭제되었습니다.");
    }
    
    
    @Override
    public List<StockLogDTO> getSpecificLogsAsDTO(Long materialId){
    	 List<StockLogEntity> logList = stockLogRepository.findByMaterialId(materialId);
         return logList.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }
    
}
