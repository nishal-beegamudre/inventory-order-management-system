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
public class LogisticsDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("codeName")
    private String codeName;

    @JsonProperty("pointOfContactName")
    private String pointOfContactName;

    @JsonProperty("pointOfContactPhoneNumber")
    private String pointOfContactPhoneNumber;

    @JsonProperty("pointOfContactEmail")
    private String pointOfContactEmail;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
