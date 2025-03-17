package com.stock.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.dto.naverProduct.CombinedDTO;
import com.stock.dto.naverProduct.OptionDTO;
import com.stock.dto.naverProduct.ProductDTO;
import com.stock.dto.naverProduct.ProductForOptionSupplementDTO;
import com.stock.dto.naverProduct.SupplementDTO;
import com.stock.entity.naverProduct.OptionEntity;
import com.stock.entity.naverProduct.ProductEntity;
import com.stock.entity.naverProduct.SupplementEntity;
import com.stock.service.OptionService;
import com.stock.service.ProductService;
import com.stock.service.SupplementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class TotalProductController {
    
    private final ProductService productService;
    private final OptionService optionService;
    private final SupplementService supplementService;

    //판매 중인 상품 전체 목록 조회 (재고 적은 순으로 정렬)
    @GetMapping("/total/list")
    public ResponseEntity<List<ProductDTO>> getProductList() {
        List<ProductEntity> productList = productService.getAllProducts();


        // ProductEntity를 stockQuantity에 따라 오름차순으로 정렬
        List<ProductEntity> sortedProductList = productList.stream()
            .sorted(Comparator.comparing(ProductEntity::getStockQuantity))
            .collect(Collectors.toList());

        // 정렬된 ProductEntity를 ProductDTO로 변환
        List<ProductDTO> productDTOList = sortedProductList.stream()
            .map(productEntity -> {
                ProductDTO productDTO = new ProductDTO();
                productDTO.productEntityToDTO(productEntity);
                return productDTO;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(productDTOList);
    }

    //옵션 목록 조회 (ProductForOptionSupplementDTO 사용)
    @GetMapping("/option/list")
    public ResponseEntity<List<OptionDTO>> getOptionList() {
        List<OptionEntity> optionList = optionService.getAllOptions();
        System.out.println("옵션 개수: " + optionList.size());

        // OptionEntity -> OptionDTO 변환
        List<OptionDTO> optionDTOList = optionList.stream()
            .map(option -> {
                OptionDTO optionDTO = new OptionDTO();
                optionDTO.setId(option.getId());
                optionDTO.setOptionName1(option.getOptionName1());
                optionDTO.setOptionName2(option.getOptionName2());
                optionDTO.setOptionName3(option.getOptionName3());
                optionDTO.setOptionName4(option.getOptionName4());
                optionDTO.setStockQuantity(option.getStockQuantity());
                optionDTO.setPrice(option.getPrice());
                optionDTO.setSellerManagerCode(option.getSellerManagerCode());
                optionDTO.setUsable(option.isUsable());
                
                // ProductEntity를 ProductForOptionSupplementDTO로 변환하여 설정
                ProductForOptionSupplementDTO productDTO = toProductForOptionSupplementDTO(option.getProductEntity());  // 수정된 부분
                optionDTO.setProductEntity(productDTO);
                
                return optionDTO;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(optionDTOList);
    }

    //추가 상품 목록 조회
    @GetMapping("/supplement/list")
    public ResponseEntity<List<SupplementDTO>> getSupplementList() {
        List<SupplementEntity> supplementList = supplementService.getAllSupplements();
        System.out.println("추가 상품 개수: " + supplementList.size());
        
        // SupplementEntity -> SupplementDTO 변환
        List<SupplementDTO> supplementDTOList = supplementList.stream()
            .map(supplement -> {
                SupplementDTO supplementDTO = new SupplementDTO();
                supplementDTO.setId(supplement.getId());
                supplementDTO.setName(supplement.getName());
                supplementDTO.setPrice(supplement.getPrice());
                supplementDTO.setStockQuantity(supplement.getStockQuantity());
                supplementDTO.setSellerManagementCode(supplement.getSellerManagementCode());
                supplementDTO.setUsable(supplement.isUsable());
                
                // ProductEntity를 ProductForOptionSupplementDTO로 변환하여 설정
                ProductForOptionSupplementDTO productDTO = toProductForOptionSupplementDTO(supplement.getProductEntity());
                supplementDTO.setProductEntity(productDTO);
                
                return supplementDTO;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(supplementDTOList);
    }

    public ProductForOptionSupplementDTO toProductForOptionSupplementDTO(ProductEntity productEntity) {
        return new ProductForOptionSupplementDTO(
                productEntity.getOriginProductNo(),
                productEntity.getName(),
                productEntity.getRepresentativeImage()
        );
    }

    @GetMapping("/combined-list")
    public ResponseEntity<List<CombinedDTO>> getCombinedList() {
        List<OptionEntity> optionList = optionService.getAllOptions();
        List<SupplementEntity> supplementList = supplementService.getAllSupplements();
        
        // ProductEntity 목록 가져오기 (옵션/추가상품 없이 존재하는 ProductEntity 포함)
        List<ProductEntity> productList = productService.getAllProducts();  // 제품 목록 가져오기

        
        List<CombinedDTO> combinedList = new ArrayList<>();
        
        // 옵션을 CombinedDTO로 변환
        for (OptionEntity option : optionList) {
            CombinedDTO dto = new CombinedDTO();
            dto.setId(option.getId());
            dto.setType("옵션");
            dto.setName(Stream.of(option.getOptionName1(), option.getOptionName2(), option.getOptionName3(), option.getOptionName4())
                            .filter(Objects::nonNull)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining(" ")));
            dto.setAddedPrice(option.getPrice());
            dto.setPrice(option.getProductEntity().getDiscountedPrice());
            dto.setStockQuantity(option.getStockQuantity());
            dto.setProductEntity(toProductForOptionSupplementDTO(option.getProductEntity()));
            combinedList.add(dto);
        }
        
        // 추가 상품을 CombinedDTO로 변환
        for (SupplementEntity supplement : supplementList) {
            CombinedDTO dto = new CombinedDTO();
            dto.setId(supplement.getId());
            dto.setType("추가상품");
            dto.setName(Stream.of(supplement.getGroupName(), supplement.getName())
                            .filter(Objects::nonNull)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.joining(" ")));
            dto.setAddedPrice(supplement.getPrice());
            dto.setPrice(supplement.getProductEntity().getDiscountedPrice());
            dto.setStockQuantity(supplement.getStockQuantity());
            dto.setProductEntity(toProductForOptionSupplementDTO(supplement.getProductEntity()));
            combinedList.add(dto);
        }
        
        // 옵션/추가상품이 없는 ProductEntity를 CombinedDTO에 추가
        for (ProductEntity product : productList) {
            // 해당 ProductEntity가 옵션이나 추가상품을 가지고 있지 않으면
            if (product.getOptions().isEmpty() && product.getSupplements().isEmpty()) {
                CombinedDTO dto = new CombinedDTO();
                dto.setId(product.getChannelProductNo());
                dto.setType("단일상품");
                dto.setName(product.getName()); // ProductEntity 이름을 설정
                dto.setPrice(product.getDiscountedPrice()); // ProductEntity 가격을 설정
                dto.setStockQuantity(product.getStockQuantity()); // ProductEntity 재고 설정
                dto.setProductEntity(toProductForOptionSupplementDTO(product)); // ProductEntity 객체 설정
                combinedList.add(dto); // 리스트에 추가
            }
        }
        
        // productEntity.id 기준으로 정렬
        combinedList.sort(Comparator.comparing(dto -> dto.getProductEntity().getOriginProductNo()));
        
        return ResponseEntity.ok(combinedList);
    }






}
