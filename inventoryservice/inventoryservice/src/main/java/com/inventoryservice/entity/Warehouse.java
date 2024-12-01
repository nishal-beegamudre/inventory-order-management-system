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
@Table(name="warehouses")
public class Warehouse implements Serializable {

    @Id
    @GenericGenerator(name = "warehouse-key-generator", strategy = "com.inventoryservice.keygenerator.WarehouseKeyGenerator")
    @GeneratedValue(generator = "warehouse-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="name")
    @JsonProperty("name")
    private String name;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Column(name="is_active")
    @JsonProperty("isActive")
    private Boolean isActive;

    @Column(name="pin_code")
    @JsonProperty("pinCode")
    private String pinCode;

    @Column(name="address")
    @JsonProperty("address")
    private String address;

    @Column(name="latitude")
    @JsonProperty("latitude")
    private String latitude;

    @Column(name="longitude")
    @JsonProperty("longitude")
    private String longitude;

    @Column(name="district")
    @JsonProperty("district")
    private String district;

    @Column(name="state_id")
    @JsonProperty("stateId")
    private String stateId;

    @Column(name="official_phone_number")
    @JsonProperty("officialPhoneNumber")
    private String officialPhoneNumber;

    @Column(name="official_email_id")
    @JsonProperty("officialEmailId")
    private String officialEmailId;

    @Column(name="point_of_contact_name")
    @JsonProperty("pointOfContactName")
    private String pointOfContactName;

    @Column(name="point_of_contact_phone_number")
    @JsonProperty("pointOfContactPhoneNumber")
    private String pointOfContactPhoneNumber;

    @Column(name="second_poc")
    @JsonProperty("secondPOC")
    private String secondPOC;

    @Column(name="second_poc_number")
    @JsonProperty("secondPOCNumber")
    private String secondPOCNumber;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
