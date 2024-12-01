package com.apigateway.dto;

import java.util.Date;

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
public class RegisterUserDTO {
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("employeeNumber")
    private String employeeNumber;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("departmentName")
    private String departmentName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("secondaryPhone")
    private String secondaryPhone;

    @JsonProperty("personalEmail")
    private String personalEmail;

    @JsonProperty("officialEmail")
    private String officialEmail;

    @JsonProperty("officialPhoneNumber")
    private String officialPhoneNumber;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("address")
    private String address;

    @JsonProperty("pincode")
    private String pincode;

    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @JsonProperty("joiningDate")
    private Date joiningDate;

    @JsonProperty("salary")
    private Double salary;

    @JsonProperty("reporterName")
    private String reporterName;

    @JsonProperty("reporterEmployeeNumber")
    private String reporterEmployeeNumber;

    @JsonProperty("isPresentlyWorkingInThisCompany")
    private Boolean isPresentlyWorkingInThisCompany;
    

}
