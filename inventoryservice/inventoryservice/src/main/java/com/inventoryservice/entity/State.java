package com.inventoryservice.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

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
@Table(name="states")
public class State implements Serializable {

    @Id
    @GenericGenerator(name = "state-key-generator", strategy = "com.inventoryservice.keygenerator.StateKeyGenerator")
    @GeneratedValue(generator = "state-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="name")
    @JsonProperty("name")
    private String name;

    @Column(name="first_warehouse_number")
    @JsonProperty("firstWarehouseNumber")
    private String firstWarehouseNumber;

    @Column(name="second_warehouse_number")
    @JsonProperty("secondWarehouseNumber")
    private String secondWarehouseNumber;

    @Column(name="third_warehouse_number")
    @JsonProperty("thirdWarehouseNumber")
    private String thirdWarehouseNumber;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
