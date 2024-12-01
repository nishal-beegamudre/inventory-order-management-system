package com.apigateway.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name="users")
public class User implements Serializable,UserDetails {
	
	@Id
	@GenericGenerator(name = "user-key-generator", strategy = "com.apigateway.keygenerator.UserKeyGenerator")
	@GeneratedValue(generator = "user-key-generator")
	@Column(nullable = false)
	@JsonProperty("id")
	private String id;
	
	@Column(name="name")
	@JsonProperty("name")
	private String name;
	
    @Column(name="email", unique = true, length = 100, nullable = false)
    @JsonProperty("email")
    private String email;
    
    @Column(name="username")
    @JsonProperty("username")
    private String username;

    @Column(name="password",nullable = false)
    @JsonProperty("password")
    private String password;
    
    @Column(name="enabled")
    @JsonProperty("enabled")
    private Boolean enabled;
    
    @Column(name="authorities")
    @JsonProperty("authorities")
    private List<GrantedAuthority> authorities;
    
    @Column(name="account_non_expired")
    @JsonProperty("accountNonExpired")
    private Boolean accountNonExpired;
    
    @Column(name="account_non_locked")
    @JsonProperty("accountNonLocked")
    private Boolean accountNonLocked;
    
    @Column(name="credentials_non_expired")
    @JsonProperty("credentialsNonExpired")
    private Boolean credentialsNonExpired;
    
    @Column(name="is_validated")
    @JsonProperty("isValidated")
    private Boolean isValidated;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of();
	}


	public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

