package com.stock.entity.naverProduct;

import java.util.List;

import com.stock.entity.OptionsMaterialEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="tbl_option")  
public class OptionEntity {

    //////////////OptionCombination 안에 있는 값.//////////
    @Id
    @Column(name = "option_id")
    private Long id; 

    private String optionName1; //조합형 옵션값 1
    private String optionName2;
    private String optionName3;
    private String optionName4;
    private long stockQuantity;
    private long price; //추가되는 옵션가격
    private String sellerManagerCode;
    private boolean usable; //옵션 사용 여부

    //////////////OptionCombination 안에 있는 값 End//////////
    

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity; // ProductEntity와의 연관관계

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionsMaterialEntity> optionsMaterialEntity; //옵션 제품 - 재료 관계 리스트
    
}
