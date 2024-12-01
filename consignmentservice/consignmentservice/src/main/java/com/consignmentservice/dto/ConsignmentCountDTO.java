package com.consignmentservice.dto;

import java.time.ZonedDateTime;

import com.consignmentservice.entity.ConsignmentFulfillmentTypeEnum;
import com.consignmentservice.entity.ConsignmentStatusEnum;
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
public class ConsignmentCountDTO {

    @JsonProperty("orderNumber")
    private String orderNumber;
    
    @JsonProperty("count")
    private Long count;

}
