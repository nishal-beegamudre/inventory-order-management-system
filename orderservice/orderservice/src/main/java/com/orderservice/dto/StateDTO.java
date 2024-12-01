package com.orderservice.dto;

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
public class StateDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("firstWarehouseNumber")
    private String firstWarehouseNumber;

    @JsonProperty("secondWarehouseNumber")
    private String secondWarehouseNumber;

    @JsonProperty("thirdWarehouseNumber")
    private String thirdWarehouseNumber;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}

