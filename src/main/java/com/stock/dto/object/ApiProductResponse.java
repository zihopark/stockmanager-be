package com.stock.dto.object;

import java.util.List;

import lombok.Data;

@Data
public class ApiProductResponse {
    private List<Content> contents;
    private Long page;
    private Long size;
    private Long totalElements;
    private Long totalPages;
    private Sort sort;
    private boolean first;
    private boolean last;

    @Data
    public static class Content {
        private Long groupProductNo;
        private Long originProductNo;
        private List<ChannelProduct> channelProducts;
    }


    @Data
    public static class ChannelProduct {
        private Long groupProductNo;
        private Long originProductNo;
        private Long channelProductNo;
        private String channelServiceType;
        private Long injectProductNo;
        private String categoryId;
        private String name;
        private String sellerManagementCode;
        private String statusType;
        private String channelProductDisplayStatusType;
        private Long salePrice;
        private Long discountedPrice;
        private Long mobileDiscountedPrice;
        private Long stockQuantity;
        private boolean knowledgeShoppingProductRegistration;
        private String deliveryAttributeType;
        private Long deliveryFee;
        private Long returnFee;
        private Long exchangeFee;
        private Long multiPurchaseDiscount;
        private String multiPurchaseDiscountUnitType;
        private Long sellerPurchasePoint;
        private String sellerPurchasePointUnitType;
        private Long managerPurchasePoint;
        private Long textReviewPoint;
        private Long photoVideoReviewPoint;
        private Long regularCustomerPoint;
        private Long freeInterest;
        private String gift;
        private String saleStartDate;
        private String saleEndDate;
        private String wholeCategoryName;
        private String wholeCategoryId;
        private RepresentativeImage representativeImage;
        private Long modelId;
        private String modelName;
        private String brandName;
        private String manufacturerName;
        private List<SellerTag> sellerTags;
        private String regDate;
        private String modifiedDate;
    }

    @Data
    public static class RepresentativeImage {
        private String url;
    }

    @Data
    public static class SellerTag {
        private Long code;
        private String text;
    }

    @Data
    public static class Sort {
        private boolean sorted;
        private List<Field> fields;
    }

    @Data
    public static class Field {
        private String name;
        private String direction;
    }

}
