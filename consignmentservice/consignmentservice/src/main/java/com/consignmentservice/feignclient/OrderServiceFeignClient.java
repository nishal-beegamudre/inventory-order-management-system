package com.consignmentservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service", url = "http://localhost:8085")
public interface OrderServiceFeignClient {

}
