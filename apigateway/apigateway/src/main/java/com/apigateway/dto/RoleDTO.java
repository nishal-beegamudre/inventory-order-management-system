package com.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
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
public class RoleDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isSpecialAccessEnabled")
    private Boolean isSpecialAccessEnabled;

    @JsonProperty("isAdmin")
    private Boolean isAdmin;

    @JsonProperty("creationDate")
    private ZonedDateTime creationDate;

    @JsonProperty("lastModifiedDate")
    private ZonedDateTime lastModifiedDate;

    @JsonProperty("responseStatus")
    private String responseStatus;

    @JsonProperty("responseMessage")
    private String responseMessage;
}
