package com.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Date;
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
public class EmployeeDTO {

    @JsonProperty("id")
    private String id;

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

    @JsonProperty("creationDate")
    private ZonedDateTime creationDate;

    @JsonProperty("isPresentlyWorkingInThisCompany")
    private Boolean isPresentlyWorkingInThisCompany;

    @JsonProperty("lastModifiedDate")
    private ZonedDateTime lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}

