package com.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderservice.entity.OrderStatusEnum;

import jakarta.persistence.Column;
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
public class OrderItemStatusDTO {
	
	@Column(name="orderItemNumber")
	@JsonProperty("orderItemNumber")
    private String orderItemNumber;
	
	@Column(name="status")
	@JsonProperty("status")
    private OrderStatusEnum status;

}
