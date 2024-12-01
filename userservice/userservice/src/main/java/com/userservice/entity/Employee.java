package com.userservice.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="employees")
public class Employee implements Serializable {

    @Id
    @GenericGenerator(name = "employee-key-generator", strategy = "com.userservice.keygenerator.EmployeeKeyGenerator")
    @GeneratedValue(generator = "employee-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="employee_number")
    @JsonProperty("employeeNumber")
    private String employeeNumber;
    
    @Column(name="name")
    @JsonProperty("name")
    private String name;

    @Column(name="role_name")
    @JsonProperty("roleName")
    private String roleName;

    @Column(name="department_name")
    @JsonProperty("departmentName")
    private String departmentName;

    @Column(name="phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name="secondary_phone")
    @JsonProperty("secondaryPhone")
    private String secondaryPhone;

    @Column(name="personal_email")
    @JsonProperty("personalEmail")
    private String personalEmail;

    @Column(name="official_email")
    @JsonProperty("officialEmail")
    private String officialEmail;

    @Column(name="official_phone_number")
    @JsonProperty("officialPhoneNumber")
    private String officialPhoneNumber;

    @Column(name="is_active")
    @JsonProperty("isActive")
    private Boolean isActive;

    @Column(name="address")
    @JsonProperty("address")
    private String address;

    @Column(name="pincode")
    @JsonProperty("pincode")
    private String pincode;

    @Column(name="warehouse_number")
    @JsonProperty("warehouseNumber")
    private String warehouseNumber;

    @Column(name="joining_date")
    @JsonProperty("joiningDate")
    private Date joiningDate;

    @Column(name="salary")
    @JsonProperty("salary")
    private Double salary;

    @Column(name="reporter_name")
    @JsonProperty("reporterName")
    private String reporterName;

    @Column(name="reporter_employee_number")
    @JsonProperty("reporterEmployeeNumber")
    private String reporterEmployeeNumber;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="is_presently_working_in_this_company")
    @JsonProperty("isPresentlyWorkingInThisCompany")
    private Boolean isPresentlyWorkingInThisCompany;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
