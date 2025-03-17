package com.stock.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.dto.MaterialDTO;
import com.stock.dto.MaterialsFinalProductDTO;
import com.stock.dto.OptionsMaterialDTO;
import com.stock.dto.ProductsMaterialDTO;
import com.stock.dto.StockLogDTO;
import com.stock.dto.SupplementsMaterialDTO;
import com.stock.entity.MaterialEntity;
import com.stock.entity.OptionsMaterialEntity;
import com.stock.entity.ProductsMaterialEntity;
import com.stock.entity.StockLogEntity;
import com.stock.entity.SupplementsMaterialEntity;
import com.stock.entity.repository.MaterialRepository;
import com.stock.entity.repository.StockLogRepository;
import com.stock.service.MaterialService;
import com.stock.service.OptionsMaterialService;
import com.stock.service.OrderService;
import com.stock.service.ProductsMaterialService;
import com.stock.service.StockLogService;
import com.stock.service.SupplementsMaterialService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/material")
public class MaterialController {

	private final MaterialService materialService;
	private final MaterialRepository materialRepository;
	private final ProductsMaterialService productsMaterialService;
	private final OptionsMaterialService optionsMaterialService;
	private final SupplementsMaterialService supplementsMaterialService;
	private final StockLogService stockLogService;
	private final StockLogRepository stockLogRepository;
	private final TransactionTemplate transactionTemplate;
	
	
	//재료 신규 등록 
	@PostMapping("/add")
	  public ResponseEntity<?> saveMaterial(HttpServletRequest request) throws Exception {
        try {
        	 // 원시 요청 본문 로깅
            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            //System.out.println("Raw request body: " + requestBody);

            //요청 본문은 올바르게 서버에 도달하고 있지만, @RequestBody로 자동 매핑되는 과정에서 문제가 발생. 
            // 그래서 ObjectMapper를 사용하여 JSON을 MaterialDTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            MaterialDTO materialData = objectMapper.readValue(requestBody, MaterialDTO.class);

            //System.out.println("Parsed material data: " + materialData);

            
            // materialData에서 데이터를 추출하여 MaterialEntity로 변환
            String name = (String) materialData.getName();
            Integer stockQuantity = (Integer) materialData.getStockQuantity();
            Integer onHoldStock = materialData.getOnHoldStock() != null ? (Integer) materialData.getOnHoldStock() : 0;
            Integer badStock = materialData.getBadStock() != null ? (Integer) materialData.getBadStock() : 0;
            Integer unitCost = materialData.getUnitCost() != null ? (Integer) materialData.getUnitCost() : null;
            String imageurl = (String) materialData.getImageurl();
            String type = (String) materialData.getType();

            // MaterialEntity 생성 및 값 설정
            MaterialEntity materialEntity = new MaterialEntity();
            materialEntity.setName(name);
            materialEntity.setStockQuantity(stockQuantity);
            materialEntity.setOnHoldStock(onHoldStock);
            materialEntity.setBadStock(badStock);
            materialEntity.setUnitCost(unitCost);
            materialEntity.setImageurl(imageurl);
            materialEntity.setType(type);

            MaterialEntity savedMaterial = materialRepository.save(materialEntity);

            return ResponseEntity.ok(Map.of("Saved Material", savedMaterial));
            
        } catch (Exception e) {
            System.out.println("Error during save: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
	
	//재료 목록 조회
	@GetMapping("/list")
	public ResponseEntity<List<MaterialEntity>> getList(){
		List<MaterialEntity> materialList = materialService.getAllMaterials(); //재고 적은 순으로 오름차순 나열 
		return ResponseEntity.ok(materialList);
	}
	
	//재료 목록 조회
	@GetMapping("/listOrderByName")
	public ResponseEntity<List<MaterialEntity>> getListOrderByName(){
		List<MaterialEntity> materialList = materialService.getAllMaterialOrderByName(); //이름 순으로 오름차순 나열 
		return ResponseEntity.ok(materialList);
	}
	
	//재료 삭제
	@DeleteMapping("/delete/{materialId}")
	public ResponseEntity<Void> deleteMaterial(@PathVariable("materialId") Long materialId){
		try {
			materialService.deleteMaterial(materialId);
	        return ResponseEntity.ok().build();
		} catch (Exception e) {
			System.out.println("재료 삭제 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	//재료 수정 
	@PostMapping("/edit/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
        	String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            //System.out.println("Raw request body: " + requestBody);
        	
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            MaterialDTO materialData = objectMapper.readValue(requestBody, MaterialDTO.class);

            //System.out.println("Parsed material data: " + materialData);

            // materialData에서 데이터를 추출하여 MaterialEntity로 변환
            String name = (String) materialData.getName();
            Integer stockQuantity = (Integer) materialData.getStockQuantity();
            Integer onHoldStock = materialData.getOnHoldStock() != null ? (Integer) materialData.getOnHoldStock() : 0;
            Integer badStock = materialData.getBadStock() != null ? (Integer) materialData.getBadStock() : 0;
            Integer unitCost = materialData.getUnitCost() != null ? (Integer) materialData.getUnitCost() : null;
            String imageurl = (String) materialData.getImageurl();
            String type = (String) materialData.getType();
            
            MaterialEntity materialEntity = new MaterialEntity();
            materialEntity.setName(name);
            materialEntity.setStockQuantity(stockQuantity);
            materialEntity.setOnHoldStock(onHoldStock);
            materialEntity.setBadStock(badStock);
            materialEntity.setUnitCost(unitCost);
            materialEntity.setImageurl(imageurl);
            materialEntity.setType(type);

            materialService.updateMaterial(id, materialEntity);
            return ResponseEntity.ok("재료가 성공적으로 수정되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("JSON 처리 실패: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("수정 실패: " + e.getMessage());
        }
    }
	
	
	//Product 에 재료 매칭
	@PostMapping("/match/product")
	public ResponseEntity<?> addProductsMaterial(HttpServletRequest request) throws IOException {
	    try {
	        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        //System.out.println("Raw request body: " + requestBody);

	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	        List<ProductsMaterialDTO> productsMaterialDTOList = objectMapper.readValue(requestBody, new TypeReference<List<ProductsMaterialDTO>>() {});

	        if (productsMaterialDTOList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty request body");
	        }

	        return transactionTemplate.execute(status -> {
	            try {
	                // 모든 productId가 동일하다고 가정하고 첫 번째 요소의 productId로 삭제 진행
	                Long productId = productsMaterialDTOList.get(0).getProductId();
	                productsMaterialService.deleteAllByProductId(productId);

	                List<ProductsMaterialEntity> createdEntities = new ArrayList<>();

	                for (ProductsMaterialDTO dto : productsMaterialDTOList) {
	                    ProductsMaterialEntity created = productsMaterialService.addProductsMaterial(dto);
	                    createdEntities.add(created);
	                }
	               
	                Map<String, Object> response = new HashMap<>();
	                response.put("success", true);
	                response.put("message", "Materials added successfully");
	                response.put("createdIds", createdEntities.stream().map(ProductsMaterialEntity::getId).collect(Collectors.toList()));

	                return ResponseEntity.status(HttpStatus.CREATED).body(response);
	            } catch (Exception e) {
	                status.setRollbackOnly();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
	            }
	        });

	    } catch (JsonProcessingException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format");
	    }
	}


	
	//Option 에 재료 매칭
	@PostMapping("/match/option")
	@Transactional
	public ResponseEntity<?> addOptionsMaterial(HttpServletRequest request) throws IOException {
	    try {
	        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        //System.out.println("Raw request body: " + requestBody);

	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	        List<OptionsMaterialDTO> optionsMaterialDTOList = objectMapper.readValue(requestBody, new TypeReference<List<OptionsMaterialDTO>>() {});

	        if (optionsMaterialDTOList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty request body");
	        }

	        return transactionTemplate.execute(status -> {
	            try {
	                // 모든 optionId가 동일하다고 가정하고 첫 번째 요소의 optionId로 삭제 진행
	                Long optionId = optionsMaterialDTOList.get(0).getOptionId();
	                optionsMaterialService.deleteAllByOptionId(optionId);

	                List<OptionsMaterialEntity> createdEntities = new ArrayList<>();

	                for (OptionsMaterialDTO dto : optionsMaterialDTOList) {
	                    OptionsMaterialEntity created = optionsMaterialService.addOptionsMaterial(dto);
	                    createdEntities.add(created);
	                }

	                Map<String, Object> response = new HashMap<>();
	                response.put("success", true);
	                response.put("message", "Materials added successfully");
	                response.put("createdIds", createdEntities.stream().map(OptionsMaterialEntity::getId).collect(Collectors.toList()));

	                return ResponseEntity.status(HttpStatus.CREATED).body(response);
	            } catch (Exception e) {
	                status.setRollbackOnly();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
	            }
	        });

	    } catch (JsonProcessingException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format");
	    }
	}
	
	//Supplement 에 재료 매칭
	@PostMapping("/match/supplement")
	@Transactional
	public ResponseEntity<?> addSupplementsMaterial(HttpServletRequest request) throws IOException {
	    try {
	        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        //System.out.println("Raw request body: " + requestBody);

	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	        List<SupplementsMaterialDTO> supplementsMaterialDTOList = objectMapper.readValue(requestBody, new TypeReference<List<SupplementsMaterialDTO>>() {});

	        if (supplementsMaterialDTOList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty request body");
	        }

	        Long supplementId = supplementsMaterialDTOList.get(0).getSupplementId();
	        supplementsMaterialService.deleteAllBySupplementId(supplementId);

	        return transactionTemplate.execute(status -> {
	            try {
	                List<SupplementsMaterialEntity> createdEntities = new ArrayList<>();

	                for (SupplementsMaterialDTO dto : supplementsMaterialDTOList) {
	                    SupplementsMaterialEntity created = supplementsMaterialService.addSupplementsMaterial(dto);
	                    createdEntities.add(created);
	                }

	                Map<String, Object> response = new HashMap<>();
	                response.put("success", true);
	                response.put("message", "Materials added successfully");
	                response.put("createdIds", createdEntities.stream().map(SupplementsMaterialEntity::getId).collect(Collectors.toList()));

	                return ResponseEntity.status(HttpStatus.CREATED).body(response);
	            } catch (Exception e) {
	                status.setRollbackOnly();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
	            }
	        });

	    } catch (JsonProcessingException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format");
	    }
	}



	//Product 의 재료 보기 
    @GetMapping("/ofProduct/{productId}")
    public ResponseEntity<List<ProductsMaterialDTO>> getMaterialsByProduct(@PathVariable("productId") Long productId) {
    	
    	// Service 메서드를 호출하여 DTO 리스트를 반환
        List<ProductsMaterialDTO> dtoList = productsMaterialService.getMaterialsByProduct(productId);
        return ResponseEntity.ok(dtoList);
    }
    
    //Option 의 재료 보기 
    @GetMapping("/ofOption/{optionId}")
    public ResponseEntity<List<OptionsMaterialDTO>> getMaterialsByOption(@PathVariable("optionId") Long optionId) {
    	
    	// Service 메서드를 호출하여 DTO 리스트를 반환
        List<OptionsMaterialDTO> dtoList = optionsMaterialService.getMaterialsByOption(optionId);
        return ResponseEntity.ok(dtoList);
    }
    
    //supplement 의 재료 보기 
    @GetMapping("/ofSupplement/{supplementId}")
    public ResponseEntity<List<SupplementsMaterialDTO>> getMaterialsBySupplement(@PathVariable("supplementId") Long supplementId) {
        
        // Service 메서드를 호출하여 DTO 리스트를 반환
        List<SupplementsMaterialDTO> dtoList = supplementsMaterialService.getMaterialsBySupplement(supplementId);
        return ResponseEntity.ok(dtoList);
    }
    
    
    //해당 재료를 바탕으로 만들어지는 제품들 확인하기 
    @GetMapping("/showFinal/{materialId}")
    public ResponseEntity<List<MaterialsFinalProductDTO>> getShowFinal(@PathVariable("materialId") Long materialID){
    	
    	List<MaterialsFinalProductDTO> dtoList = materialService.getMaterialsFinalProduct(materialID);
    	return ResponseEntity.ok(dtoList);
    }
    
    
    //사용자가 재고 로그 직접 기록하면 로그 생성, 재료 재고 변경
    @PostMapping("/addLog")
    public ResponseEntity<?> postLog(HttpServletRequest request) throws IOException{
    	try {
	        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        //System.out.println("Raw request body: " + requestBody);
	    
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	        StockLogDTO stockLogDTO = objectMapper.readValue(requestBody, StockLogDTO.class);
	        //System.out.println("stockLogDTO: " + stockLogDTO);
	        
	        //로그 생성하기 
	        StockLogEntity created = stockLogService.addLog(stockLogDTO);
	        
	        //재료 재고 변경
	        String type = stockLogDTO.getType();
	        Long materialId = stockLogDTO.getMaterialId();
	        int quantity = stockLogDTO.getQuantity();
	        materialService.stockFromLog(materialId, quantity, type);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "Material added successfully");
	        response.put("id", created.getId());
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	        
	    } catch (JsonProcessingException e) {
	        // JSON 파싱 오류 처리
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    }
    }
    
    //재고 로그 리스트 
    @GetMapping("/log/list")
    public ResponseEntity<List<StockLogDTO>> getLogList() {
        List<StockLogDTO> logList = stockLogService.getAllLogsAsDTO();
		return ResponseEntity.ok(logList);
	}
    
    //재고 로그 리스트 
    @GetMapping("/log/specificlist/{materialId}")
    public ResponseEntity<List<StockLogDTO>> getSpecificLogList(@PathVariable("materialId") Long materialId) {
        List<StockLogDTO> logList = stockLogService.getSpecificLogsAsDTO(materialId);
		return ResponseEntity.ok(logList);
	}
    
    //재고 로그 하나씩 지우기
    @DeleteMapping("/log/delete/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable("id") Long id){
    	try {
    		stockLogService.deleteLog(id);
    		return ResponseEntity.ok().build();
    	} catch (Exception e) {
    		System.out.println("로그 삭제 중 오류 발생: " + e.getMessage());
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }
	
    //재고 로그 전체 지우기
    @DeleteMapping("/log/delete-all")
    public ResponseEntity<String> deleteAll() {
        try {
            stockLogService.deleteAll();
            return ResponseEntity.ok("모든 로그가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그 삭제 중 오류가 발생했습니다.");
        }
    }
   
}
