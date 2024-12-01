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
public class SignupResponseDTO {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("name")
	private String name;
	
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
    
    @JsonProperty("phone")
    private String phone;
	
	@JsonProperty("message")
	private String message;

}

