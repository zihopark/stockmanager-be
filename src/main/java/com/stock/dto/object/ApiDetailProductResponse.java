package com.stock.dto.object;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDetailProductResponse {
   
    private GroupProduct groupProduct;
    private OriginProduct originProduct;
    private boolean cultureCostIncomeDeductionYn;
    private boolean customProductYn;
    private boolean itselfProductionProductYn;
    private boolean brandCertificationYn;

    //옵션은 조합형 옵션 사용 예정.

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GroupProduct {
        private long groupProductNo;
        private String leafCategoryId;
        private String groupProductName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OriginProduct {
        private String statusType; //상품 판매 상태 코드
        private String saleType; 
        private String leafCategoryId; 
        private String name; //상품명
        private String detailContent; //상품 상세페이지 내용
        private Images images; //상품 이미지
        private ZonedDateTime saleStartDate; //상품 판매 시작일
        private ZonedDateTime saleEndDate; //상품 판매 종료일
        private long salePrice; //상품 판매 가격
        private long stockQuantity; //상품 재고 수량
        private DeliveryInfo deliveryInfo;
        private List<ProductLogistic> productLogistics;
        private DetailAttribute detailAttribute;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Images {
            private RepresentativeImage representativeImage;
            private List<OptionalImage> optionalImages;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RepresentativeImage {
                private String url; //이미지 URL
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OptionalImage {
                private String url; //이미지 URL
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DeliveryInfo {
            private String deliveryType; //배송 타입
            private String deliveryAttributeType;
            private String deliveryCompany;
            private String outboundLocationId;
            private boolean deliveryBundleGroupUsable;
            private long deliveryBundleGroupId;
            private List<String> quickServiceAreas;
            private long visitAddressId;
            private DeliveryFee deliveryFee;
            private ClaimDeliveryInfo claimDeliveryInfo;
            private boolean installation;
            private boolean installationFee;
            private String expectedDeliveryPeriodType;
            private String expectedDeliveryPeriodDirectInput;
            private int todayStockQuantity;
            private boolean customProductAfterOrderYn;
            private long hopeDeliveryGroupId;
            private boolean businessCustomsClearanceSaleYn;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class DeliveryFee {
                private String deliveryFeeType;
                private long baseFee;
                private long freeConditionalAmount;
                private int repeatQuantity;
                private int secondBaseQuantity;
                private int secondExtraFee;
                private int thirdBaseQuantity;
                private int thirdExtraFee;
                private String deliveryFeePayType;
                private DeliveryFeeByArea deliveryFeeByArea;
                private String differentialFeeByArea;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class DeliveryFeeByArea {
                    private String deliveryAreaType;
                    private long area2extraFee;
                    private long area3extraFee;
                }
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ClaimDeliveryInfo {
                private String returnDeliveryCompanyPriorityType;
                private long returnDeliveryFee;
                private long exchangeDeliveryFee;
                private long shippingAddressId;
                private long returnAddressId;
                private boolean freeReturnInsuranceYn;
            }
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ProductLogistic {
            private String logisticsCompanyId;
            private String logisticsCenterId;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DetailAttribute {
            private NaverShoppingSearchInfo naverShoppingSearchInfo;
            private String manufactureDefineNo;
            private AfterServiceInfo afterServiceInfo;
            private PurchaseQuantityInfo purchaseQuantityInfo;
            private OriginAreaInfo originAreaInfo;
            private SellerCodeInfo sellerCodeInfo;
            private OptionInfo optionInfo; //옵션 정보
            private SupplementProductInfo supplementProductInfo; //추가 상품 정보
            private PurchaseReviewInfo purchaseReviewInfo;
            private IsbnInfo isbnInfo;
            private BookInfo bookInfo;
            private String eventPhraseCont;
            private LocalDate manufactureDate;
            private LocalDate releaseDate;
            private LocalDate validDate;
            private String taxType;
            private List<ProductCertificationInfo> productCertificationInfos;
            private CertificationTargetExcludeContent certificationTargetExcludeContent;
            private String sellerCommentContent;
            private boolean sellerCommentUsable;
            private boolean minorPurchasable;
            private Ecoupon ecoupon;
            private ProductInfoProvidedNotice productInfoProvidedNotice;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class NaverShoppingSearchInfo {
                private long modelId;
                private String modelName;
                private String manufacturerName;
                private long brandId;
                private String brandName;
                private boolean catalogMatchingYn;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class AfterServiceInfo {
                private String afterServiceTelephoneNumber;
                private String afterServiceGuideContent;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PurchaseQuantityInfo {
                private int minPurchaseQuantity;
                private long maxPurchaseQuantityPerId;
                private int maxPurchaseQuantityPerOrder;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OriginAreaInfo {
                private String originAreaCode;
                private String importer;
                private String content;
                private boolean plural;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class SellerCodeInfo {
                private String sellerManagementCode;
                private String sellerBarcode;
                private String sellerCustomCode1;
                private String sellerCustomCode2;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OptionInfo {
                private String simpleOptionSortType; //단독형 옵션 정렬 순서
                private List<OptionSimple> optionSimple;
                private List<OptionCustom> optionCustom;
                private String optionCombinationSortType; //조합형 옵션 정렬 순서
                private OptionCombinationGroupNames optionCombinationGroupNames; //조합형 옵션 목록
                private List<OptionCombination> optionCombinations; //조합형 옵션
                private List<StandardOptionGroup> standardOptionGroups;
                private List<OptionStandard> optionStandards; //표준형 옵션
                private boolean useStockManagement; //옵션 재고 수량 관리 사용 여부 ('옵션 재고 수량 관리 사용 여부'를 입력하지 않거나 false로 지정하면 수량이 9,999로 설정됨)
                private List<String> optionDeliveryAttributes;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class OptionSimple {
                    private long id; //옵션 ID
                    private String groupName; //옵션명
                    private String name; //옵션값 ("단독형 옵션"인 경우 입력합니다. "직접 입력형 옵션"인 경우 무시됩니다)
                    private boolean usable; 
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class OptionCustom { //직접 입력형 옵션
                    private long id; //옵션 ID
                    private String groupName; //옵션명
                    private String name; 
                    private boolean usable; 
                }

                ////////////////////////////////////////////////////////////////
                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class OptionCombinationGroupNames {
                    private String optionGroupName1; //조합형 옵션명1
                    private String optionGroupName2; //조합형 옵션명2
                    private String optionGroupName3; //조합형 옵션명3
                    private String optionGroupName4; //조합형 옵션명4
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class OptionCombination {
                    private long id;
                    private String optionName1; //조합형 옵션값 1
                    private String optionName2;
                    private String optionName3;
                    private String optionName4;
                    private long stockQuantity;
                    private long price;
                    private String sellerManagerCode;
                    private boolean usable;
                }
                ////////////////////////////////////////////////////////////////



                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class StandardOptionGroup { //표준형 옵션 그룹 (색상, 사이즈 등록))
                    private String groupName; //표준형 옵션 그룹 타입
                    private List<StandardOptionAttribute> standardOptionAttributes; //표준형 옵션 속성

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class StandardOptionAttribute {
                        private long attributeId;
                        private long attributeValueId;
                        private String attributeValueName;
                        private List<String> imageUrls;
                    }
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class OptionStandard {
                    private Long id;
                    private String optionName1;
                    private String optionName2;
                    private long stockQuantity;
                    private String sellerManagerCode;
                    private boolean usable;
                }
            }


            ////////////////////////////////////////////////////////////////
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class SupplementProductInfo {
                private String sortType;
                private List<SupplementProduct> supplementProducts;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class SupplementProduct {
                    private Long id;
                    private String groupName; //추가 상품 그룹명 (추가 상품명)
                    private String name; //추가 상품명 (추가 상품값)
                    private long price;
                    private long stockQuantity;
                    private String sellerManagementCode;
                    private boolean usable;
                }
            }
            ////////////////////////////////////////////////////////////////

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class PurchaseReviewInfo {
                private boolean purchaseReviewExposure;
                private String reviewUnExposeReason;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class IsbnInfo {
                private String isbn13;
                private String issn;
                private boolean independentPublicationYn;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class BookInfo {
                private String publishDay;
                private Publisher publisher;
                private List<Author> authors;
                private List<Illustrator> illustrators;
                private List<Translator> translators;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Publisher {
                    private String code;
                    private String text;
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Author {
                    private String code;
                    private String text;
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Illustrator {
                    private String code;
                    private String text;
                }

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Translator {
                    private String code;
                    private String text;
                }
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ProductCertificationInfo {
                private long certificationInfoId;
                private String certificationKindType;
                private String name;
                private String certificationNumber;
                private boolean certificationMark;
                private String companyName;
                private LocalDate certificationDate;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class CertificationTargetExcludeContent {
                private boolean childCertifiedProductExclusionYn;
                private String kcExemptionType;
                private String kcCertifiedProductExclusionYn;
                private boolean greenCertifiedProductExclusionYn;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Ecoupon {
                private String periodType;
                private LocalDate validStartDate;
                private LocalDate validEndDate;
                private int periodDays;
                private String publicInformationContents;
                private String contactInformationContents;
                private String usePlaceType;
                private String usePlaceContents;
                private boolean restrictCart;
                private String siteName;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ProductInfoProvidedNotice {
                private String productInfoProvidedNoticeType;
                private Wear wear;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Wear {
                    private String returnCostReason;
                    private String noRefundReason;
                    private String qualityAssuranceStandard;
                    private String compensationProcedure;
                    private String troubleShootingContents;
                    private String material;
                    private String color;
                    private String size;
                    private String manufacturer;
                    private String caution;
                    private String packDate;
                    private String packDateText;
                    private String warrantyPolicy;
                    private String afterServiceDirector;
                }
            }
        }
    }

}