package com.stock.entity.naverProduct;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.stock.entity.ProductsMaterialEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name="tbl_product")
public class ProductEntity {

    @Id
    @Column(name = "product_id")
    private Long originProductNo; // 원 상품 번호

    private Long channelProductNo; //채널 상품 번호
    private Long groupProductNo; //그룹 상품 번호
    private String name; //상품명
    private String sellerManagementCode; //판매자 관리 코드
    private String statusType; //상품 판매 상태 코드 (WAIT(판매 대기), SALE(판매 중), OUTOFSTOCK(품절), UNADMISSION(승인 대기), REJECTION(승인 거부), SUSPENSION(판매 중지), CLOSE(판매 종료), PROHIBITION(판매 금지))
    private Long salePrice; //판매가
    private Long discountedPrice; //할인가
    private Long stockQuantity; //재고 수량
    private String representativeImage; //대표 이미지
    private String modelName; //모델명
    private String brandName; //브랜드명
    private String regDate; //상품 등록일
    private String modifiedDate; //상품 수정일


    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("productEntity")
    @BatchSize(size = 50)
    private List<OptionEntity> options = new ArrayList<>(); // 여러 OptionEntity와의 연관관계


    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("productEntity")
    @BatchSize(size = 50)
    private List<OptionEntity> supplements = new ArrayList<>(); // 여러 SupplementEntity와의 연관관계


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductsMaterialEntity> productsMaterialEntity; //제품 - 재료 관계 리스트
}