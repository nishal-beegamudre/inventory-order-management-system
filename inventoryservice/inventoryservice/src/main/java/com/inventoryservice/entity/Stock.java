package com.inventoryservice.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="stocks")
public class Stock implements Serializable {

    @Id
    @GenericGenerator(name = "stock-key-generator", strategy = "com.inventoryservice.keygenerator.StockKeyGenerator")
    @GeneratedValue(generator = "stock-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="name")
    @JsonProperty("name")
    private String name;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Column(name="sku")
    @JsonProperty("sku")
    private String sku;

    @Column(name="stock_count")
    @JsonProperty("stockCount")
    private Double stockCount;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
