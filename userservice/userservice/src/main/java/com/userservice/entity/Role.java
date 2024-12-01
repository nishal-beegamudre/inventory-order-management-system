package com.userservice.entity;

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
@Table(name="roles")
public class Role implements Serializable {

    @Id
    @GenericGenerator(name = "role-key-generator", strategy = "com.userservice.keygenerator.RoleKeyGenerator")
    @GeneratedValue(generator = "role-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="name")
    @JsonProperty("name")
    private String name;

    @Column(name="is_active")
    @JsonProperty("isActive")
    private Boolean isActive;

    @Column(name="is_special_access_enabled")
    @JsonProperty("isSpecialAccessEnabled")
    private Boolean isSpecialAccessEnabled;

    @Column(name="is_admin")
    @JsonProperty("isAdmin")
    private Boolean isAdmin;

    @Column(name="creation_date")
    @JsonProperty("creationDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime creationDate;

    @Column(name="last_modified_date")
    @JsonProperty("lastModifiedDate")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime lastModifiedDate;
}
