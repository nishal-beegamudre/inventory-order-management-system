package com.consignmentservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListOfConsignmentDTO {

    @JsonProperty("listOfConsignments")
    private List<ConsignmentDTO> listOfConsignments;
    
    @JsonProperty("responseStatus")
    private String responseStatus;
    
    @JsonProperty("responseMessage")
    private String responseMessage;
}
