package com.productservice.entity;

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
@Table(name="categories")
public class Category implements Serializable {

    @Id
    @GenericGenerator(name = "category-key-generator", strategy = "com.productservice.keygenerator.CategoryKeyGenerator")
    @GeneratedValue(generator = "category-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="name")
    @JsonProperty("name")
    private String name;

    @Column(name="description")
    @JsonProperty("description")
    private String description;

    @Column(name="is_active")
    @JsonProperty("isActive")
    private Boolean isActive;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
