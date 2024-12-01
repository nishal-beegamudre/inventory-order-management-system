package com.notificationservice.dto;

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
public class OtpDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("otp")
    private String otp;

    @JsonProperty("email")
    private String email;

    @JsonProperty("isUsed")
    private Boolean isUsed;

    @JsonProperty("creationDate")
    private String creationDate;

    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;

    @JsonProperty("expiryDate")
    private String expiryDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
