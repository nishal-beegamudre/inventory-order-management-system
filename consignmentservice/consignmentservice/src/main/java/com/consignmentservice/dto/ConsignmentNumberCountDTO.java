package com.consignmentservice.dto;

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
public class ConsignmentNumberCountDTO {

    @JsonProperty("consignmentNumber")
    private String consignmentNumber;
    
    @JsonProperty("count")
    private Long count;

}
