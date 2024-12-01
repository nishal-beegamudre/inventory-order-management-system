package com.notificationservice.entity;

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
@Table(name="otps")
public class OTP implements Serializable {
	
	@Id
	@GenericGenerator(name = "otp-key-generator", strategy = "com.notificationservice.keygenerator.OtpKeyGenerator")
	@GeneratedValue(generator = "otp-key-generator")
	@JsonProperty("id")
	private String id;
	
	@Column(name="otp")
	@JsonProperty("otp")
	private String otp;
	
	@Column(name="email")
	@JsonProperty("email")
	private String email;
	
	@Column(name="is_used")
	@JsonProperty("isUsed")
	private Boolean isUsed;
	
	@Column(name="creation_date")
	@JsonProperty("creationDate")
	@TimeZoneStorage(TimeZoneStorageType.NATIVE)
	private ZonedDateTime creationDate;
	
	@Column(name="last_modified_date")
	@JsonProperty("lastModifiedDate")
	@TimeZoneStorage(TimeZoneStorageType.NATIVE)
	private ZonedDateTime lastModifiedDate;
	
	@Column(name="expiry_date")
	@JsonProperty("expiryDate")
	@TimeZoneStorage(TimeZoneStorageType.NATIVE)
	private ZonedDateTime expiryDate;

}
