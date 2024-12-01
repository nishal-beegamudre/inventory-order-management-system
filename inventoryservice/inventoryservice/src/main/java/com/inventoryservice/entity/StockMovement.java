package com.inventoryservice.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name="stock_movements")
public class StockMovement implements Serializable {

    @Id
    @GenericGenerator(name = "stock-movement-key-generator", strategy = "com.inventoryservice.keygenerator.StockMovementKeyGenerator")
    @GeneratedValue(generator = "stock-movement-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Lob
    @Column(name="sku_and_stock", columnDefinition = "LONGTEXT")
    @JsonProperty("skuAndStock")
    private String skuAndStock; 

    @Column(name="transaction_date")
    @JsonProperty("transactionDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime transactionDate;

    @Column(name="completion_date")
    @JsonProperty("completionDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime completionDate;

    @Column(name="received_by_employee_number")
    @JsonProperty("receivedByEmployeeNumber")
    private String receivedByEmployeeNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="movement_type")
    @JsonProperty("movementType")
    private StockMovementTypeEnum movementType;

    @Enumerated(EnumType.STRING)
    @Column(name="reason")
    @JsonProperty("reason")
    private StockMovementReasonEnum reason;

    @Column(name="consignment_number")
    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @Column(name="logistics_code_name")
    @JsonProperty("logisticsCodeName")
    private String logisticsCodeName;

    @Column(name="logistics_deliverer_name")
    @JsonProperty("logisticsDelivererName")
    private String logisticsDelivererName;

    @Column(name="logistics_deliverer_number")
    @JsonProperty("logisticsDelivererNumber")
    private String logisticsDelivererNumber;

    @Column(name="is_consumed")
    @JsonProperty("isConsumed")
    private Boolean isConsumed;
    
    @Column(name="status")
    @JsonProperty("status")
    private StockMovementStatus status;
}
