package com.notificationservice.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="email_logs")
public class EmailLog implements Serializable {

    @Id
    @GenericGenerator(name = "email-log-key-generator", strategy = "com.notificationservice.keygenerator.EmailLogKeyGenerator")
    @GeneratedValue(generator = "email-log-key-generator")
    @JsonProperty("id")
    private String id;

    @Column(name="email")
    @JsonProperty("email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="email_type")
    @JsonProperty("emailType")
    private EmailTypeEnum emailType;

    @Column(name="customer_id")
    @JsonProperty("customerId")
    private String customerId;

    @Column(name="customer_name")
    @JsonProperty("customerName")
    private String customerName;

    @Column(name="consignment_number")
    @JsonProperty("consignmentNumber")
    private String consignmentNumber;

    @Column(name="order_number")
    @JsonProperty("orderNumber")
    private String orderNumber;

    @Column(name="transaction_id")
    @JsonProperty("transactionId")
    private String transactionId;

    @Column(name="subject")
    @JsonProperty("subject")
    private String subject;

    @Column(name="body")
    @JsonProperty("body")
    private String body;

    @Column(name="email_recipient_date_time")
    @JsonProperty("emailRecipientDateTime")
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    private ZonedDateTime emailRecipientDateTime;
}
