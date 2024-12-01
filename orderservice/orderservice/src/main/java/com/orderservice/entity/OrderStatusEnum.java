package com.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderStatusEnum {
	DRAFT(1),
	CREATED(2),
	PACKED(3),
	SHIPPED(4),
	OUT_FOR_DELIVERY(5),
	DELIVERED(6),
	RETURN_REQUEST_CREATED(7),
	REPLACEMENT_REQUEST_CREATED(8),
	RETURN_REQUEST_CONFIRMED(9),
	REPLACEMENT_REQUEST_CONFIRMED(10),
	OUT_FOR_PICKUP(11),
	PICKED_UP(12),
	RETURNED(13),
	REPLACED(14),
	CANCELLED(15);
	
	private Integer value;
	
	public static OrderStatusEnum fromInt(int i) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getValue() == i) {
                return status;
            }
        }
        return null;
    }
}
