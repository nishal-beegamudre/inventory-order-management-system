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
@Table(name="pincodes")
public class Pincode implements Serializable {

    @Id
    @GenericGenerator(name = "pincode-key-generator", strategy = "com.inventoryservice.keygenerator.PincodeKeyGenerator")
    @GeneratedValue(generator = "pincode-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="pincode_number")
    @JsonProperty("pincodeNumber")
    private String pincodeNumber;

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

    @Column(name="is_served")
    @JsonProperty("isServed")
    private Boolean isServed;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
