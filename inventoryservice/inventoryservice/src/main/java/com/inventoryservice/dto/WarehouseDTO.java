package com.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("pinCode")
    private String pinCode;

    @JsonProperty("address")
    private String address;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("district")
    private String district;

    @JsonProperty("stateId")
    private String stateId;

    @JsonProperty("officialPhoneNumber")
    private String officialPhoneNumber;

    @JsonProperty("officialEmailId")
    private String officialEmailId;

    @JsonProperty("pointOfContactName")
    private String pointOfContactName;

    @JsonProperty("pointOfContactPhoneNumber")
    private String pointOfContactPhoneNumber;

    @JsonProperty("secondPOC")
    private String secondPOC;

    @JsonProperty("secondPOCNumber")
    private String secondPOCNumber;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
