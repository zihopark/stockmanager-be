package com.stock.dto.naverProduct;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.entity.naverOrder.ProductOrderEntity;
import com.stock.entity.naverProduct.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @JsonProperty("originProductNo")
    private Long originProductNo; //원 상품 번호

    @JsonProperty("groupProductNo")
    private Long groupProductNo; //그룹 상품 번호

    @JsonProperty("channelProductNo")
    private Long channelProductNo; //채널 상품 번호

    @JsonProperty("name")
    private String name; //상품명

    @JsonProperty("sellerManagementCode")
    private String sellerManagementCode; //판매자 관리 코드

    @JsonProperty("statusType")
    private String statusType; //상품 판매 상태 코드 (WAIT(판매 대기), SALE(판매 중), OUTOFSTOCK(품절), UNADMISSION(승인 대기), REJECTION(승인 거부), SUSPENSION(판매 중지), CLOSE(판매 종료), PROHIBITION(판매 금지))
    
    @JsonProperty("salePrice")
    private Long salePrice; //판매가

    @JsonProperty("discountedPrice")
    private Long discountedPrice; //할인가

    @JsonProperty("stockQuantity")
    private Long stockQuantity; //재고 수량

    @JsonProperty("representativeImage")
    private String representativeImage; //대표 이미지

    @JsonProperty("modelName")
    private String modelName; //모델명

    @JsonProperty("brandName")
    private String brandName; //브랜드명

    @JsonProperty("regDate")
    private String regDate; //상품 등록일

    @JsonProperty("modifiedDate")
    private String modifiedDate; //상품 수정일


    // ProductDTO -> ProductEntity 변환
    public ProductEntity dtoToProductEntity(ProductOrderEntity productOrderEntity) {
        return ProductEntity.builder()
                            .originProductNo(this.originProductNo)
                            .groupProductNo(this.groupProductNo)
                            .channelProductNo(this.channelProductNo)
                            .name(this.name)
                            .sellerManagementCode(this.sellerManagementCode)
                            .statusType(this.statusType)
                            .salePrice(this.salePrice)
                            .discountedPrice(this.discountedPrice)
                            .stockQuantity(this.stockQuantity)
                            .representativeImage(this.representativeImage)
                            .modelName(this.modelName)
                            .brandName(this.brandName)
                            .regDate(this.regDate)
                            .modifiedDate(this.modifiedDate)
                            .build();
    }
    
    //ProductEntity => ProductDTO 변환 
    public ProductDTO productEntityToDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        
    	this.originProductNo = productEntity.getOriginProductNo();
    	this.groupProductNo = productEntity.getGroupProductNo();
    	this.channelProductNo = productEntity.getChannelProductNo();
    	this.name = productEntity.getName();
    	this.sellerManagementCode = productEntity.getSellerManagementCode();
    	this.salePrice = productEntity.getSalePrice();
    	this.discountedPrice = productEntity.getDiscountedPrice();
    	this.stockQuantity = productEntity.getStockQuantity();
    	this.representativeImage = productEntity.getRepresentativeImage();
    	this.modelName = productEntity.getModelName();
    	this.brandName = productEntity.getBrandName();
    	this.regDate = productEntity.getRegDate();
    	this.modifiedDate = productEntity.getModifiedDate();
    	
    	return productDTO;
    }

}