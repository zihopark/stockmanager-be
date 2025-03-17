package com.stock.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stock.entity.naverProduct.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name="tbl_settings")
public class SettingsEntity {

	@Id
    @Column(name = "settings_key", length = 50)
    private String key;

    @Column(name = "settings_value", length = 255)
    private String value;
    
    
    @Override
    public String toString() {
        return "Setting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
