package com.apigateway.dto;

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
public class LoginResponseDTO {
	
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("expiresIn")
    private long expiresIn;
    
	@JsonProperty("message")
    private String message;

    public String getToken() {
        return token;
    }

}

