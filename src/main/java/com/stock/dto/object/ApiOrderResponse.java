package com.stock.dto.object;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ApiOrderResponse {
    private String timestamp;
    private String traceId;
    private Data data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<Content> contents;
        private Pagination pagination;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Pagination {
            private int page;
            private int size;
            private boolean hasNext;
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Content {
            private String productOrderId;
            private ContentDetail content;
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentDetail {
        private Order order;
        private ProductOrder productOrder;
        private BeforeClaim beforeClaim;
        private CurrentClaim currentClaim;
        private List<CompletedClaim> completedClaims;
        private Delivery delivery;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        private int chargeAmountPaymentAmount;
        private int checkoutAccumulationPaymentAmount;
        private int generalPaymentAmount;
        private int naverMileagePaymentAmount;
        private String orderDate;
        private int orderDiscountAmount;
        private String orderId;
        private String ordererId;
        private String ordererName;
        private String ordererTel;
        private String paymentDate;
        private String paymentDueDate;
        private String paymentMeans;
        private String isDeliveryMemoParticularInput;
        private String payLocationType;
        private String ordererNo;
        private int payLaterPaymentAmount;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductOrder {
        private String claimStatus;
        private String claimType;
        private String decisionDate;
        private String delayedDispatchDetailedReason;
        private String delayedDispatchReason;
        private int deliveryDiscountAmount;
        private int deliveryFeeAmount;
        private String deliveryPolicyType;
        private String expectedDeliveryMethod;
        private String freeGift;
        private String mallId;
        private String optionCode;
        private int optionPrice;
        private String packageNumber;
        private String placeOrderDate;         
        private String placeOrderStatus;
        private String productClass;
        private int productDiscountAmount;
        private int initialProductDiscountAmount;
        private int remainProductDiscountAmount;
        private int groupProductId;
        private String productId;
        private String originalProductId;
        private String merchantChannelId;
        private String productName;
        private String productOption;
        private String productOrderId;
        private String productOrderStatus;
        private int quantity;
        private int initialQuantity;
        private int remainQuantity;
        private int sectionDeliveryFee;
        private String sellerProductCode;
        private ShippingAddress shippingAddress;
        private String shippingStartDate;
        private String shippingDueDate;
        private String shippingFeeType;
        private String shippingMemo;
        private TakingAddress takingAddress;
        private int totalPaymentAmount;
        private int initialPaymentAmount;
        private int remainPaymentAmount;
        private int totalProductAmount;
        private int initialProductAmount;
        private int remainProductAmount;
        private int unitPrice;
        private int sellerBurdenDiscountAmount;
        private String commissionRatingType;
        private String commissionPrePayStatus;
        private int paymentCommission;
        private int saleCommission;
        private int expectedSettlementAmount;
        private String inflowPath;
        private String inflowPathAdd;
        private String itemNo;
        private String optionManageCode;
        private String sellerCustomCode1;
        private String sellerCustomCode2;
        private String claimId;
        private int channelCommission;
        private String individualCustomUniqueCode;
        private int productImediateDiscountAmount;
        private int productProductDiscountAmount;
        private int productMultiplePurchaseDiscountAmount;
        private int sellerBurdenImediateDiscountAmount;
        private int sellerBurdenProductDiscountAmount;
        private int sellerBurdenMultiplePurchaseDiscountAmount;
        private int knowledgeShoppingSellingInterlockCommission;
        private String giftReceivingStatus;
        private int sellerBurdenStoreDiscountAmount;
        private String sellerBurdenMultiplePurchaseDiscountType;
        private String logisticsCompanyId;
        private String logisticsCenterId;
        private HopeDelivery hopeDelivery;
        private String arrivalGuaranteeDate;
        private String deliveryAttributeType;
        private String expectedDeliveryCompany;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShippingAddress {
        private String addressType;
        private String baseAddress;
        private String city;
        private String country;
        private String detailedAddress;
        private String name;
        private String state;
        private String tel1;
        private String tel2;
        private String zipCode;
        private boolean isRoadNameAddress;
        private String pickupLocationType;
        private String pickupLocationContent;
        private String entryMethod;
        private String entryMethodContent;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TakingAddress {
        private String addressType;
        private String baseAddress;
        private String city;
        private String country;
        private String detailedAddress;
        private String name;
        private String state;
        private String tel1;
        private String tel2;
        private String zipCode;
        private boolean isRoadNameAddress;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HopeDelivery {
        private String region;
        private int additionalFee;
        private String hopeDeliveryYmd;
        private String hopeDeliveryHm;
        private String changeReason;
        private String changer;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BeforeClaim {
        private Exchange exchange;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Exchange {
        private String claimId;
        private int claimDeliveryFeeDemandAmount;
        private String claimDeliveryFeePayMeans;
        private String claimDeliveryFeePayMethod;
        private String claimRequestDate;
        private String claimStatus;
        private ShippingAddress collectAddress;
        private String collectCompletedDate;
        private String collectDeliveryCompany;
        private String collectDeliveryMethod;
        private String collectStatus;
        private String collectTrackingNumber;
        private int etcFeeDemandAmount;
        private String etcFeePayMeans;
        private String etcFeePayMethod;
        private String exchangeDetailedReason;
        private String exchangeReason;
        private String holdbackDetailedReason;
        private String holdbackReason;
        private String holdbackStatus;
        private String reDeliveryMethod;
        private String reDeliveryStatus;
        private String reDeliveryCompany;
        private String reDeliveryTrackingNumber;
        private ShippingAddress reDeliveryAddress;
        private String requestChannel;
        private int requestQuantity;
        private ShippingAddress returnReceiveAddress;
        private String holdbackConfigDate;
        private String holdbackConfigurer;
        private String holdbackReleaseDate;
        private String holdbackReleaser;
        private String claimDeliveryFeeProductOrderIds;
        private String reDeliveryOperationDate;
        private int claimDeliveryFeeDiscountAmount;
        private int remoteAreaCostChargeAmount;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentClaim {
        private Cancel cancel;
        private Return cancelReturn;
        private Exchange exchange;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cancel {
        private String claimId;
        private String cancelApprovalDate;
        private String cancelCompletedDate;
        private String cancelDetailedReason;
        private String cancelReason;
        private String claimRequestDate;
        private String claimStatus;
        private String refundExpectedDate;
        private String refundStandbyReason;
        private String refundStandbyStatus;
        private String requestChannel;
        private int requestQuantity;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Return {
        private String claimId;
        private int claimDeliveryFeeDemandAmount;
        private String claimDeliveryFeePayMeans;
        private String claimDeliveryFeePayMethod;
        private String claimRequestDate;
        private String claimStatus;
        private ShippingAddress collectAddress;
        private String collectCompletedDate;
        private String collectDeliveryCompany;
        private String collectDeliveryMethod;
        private String collectStatus;
        private String collectTrackingNumber;
        private int etcFeeDemandAmount;
        private String etcFeePayMeans;
        private String etcFeePayMethod;
        private String returnDetailedReason;
        private String returnReason;
        private ShippingAddress returnReceiveAddress;
        private String returnCompletedDate;
        private String holdbackConfigDate;
        private String holdbackConfigurer;
        private String holdbackReleaseDate;
        private String holdbackReleaser;
        private String claimDeliveryFeeProductOrderIds;
        private int claimDeliveryFeeDiscountAmount;
        private int remoteAreaCostChargeAmount;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletedClaim {
        private String claimType;
        private String claimId;
        private String claimStatus;
        private String claimRequestDate;
        private String requestChannel;
        private String claimRequestDetailContent;
        private String claimRequestReason;
        private String refundExpectedDate;
        private String refundStandbyReason;
        private String refundStandbyStatus;
        private int requestQuantity;
        private int claimDeliveryFeeDemandAmount;
        private String claimDeliveryFeePayMeans;
        private String claimDeliveryFeePayMethod;
        private ShippingAddress returnReceiveAddress;
        private ShippingAddress collectAddress;
        private String collectCompletedDate;
        private String collectDeliveryCompany;
        private String collectDeliveryMethod;
        private String collectStatus;
        private String collectTrackingNumber;
        private int etcFeeDemandAmount;
        private String etcFeePayMeans;
        private String etcFeePayMethod;
        private String holdbackDetailedReason;
        private String holdbackReason;
        private String holdbackStatus;
        private String holdbackConfigDate;
        private String holdbackConfigurer;
        private String holdbackReleaseDate;
        private String holdbackReleaser;
        private String claimDeliveryFeeProductOrderIds;
        private int claimDeliveryFeeDiscountAmount;
        private int remoteAreaCostChargeAmount;
        private String claimCompleteOperationDate;
        private String claimCompleteConfigurer;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Delivery {
        private String deliveryDate;
        private String deliveryMethod;
        private String deliveryCompany;
        private String deliveryTrackingNumber;
        private String expectedDeliveryDate;
        private String shippingMemo;
    }
}
