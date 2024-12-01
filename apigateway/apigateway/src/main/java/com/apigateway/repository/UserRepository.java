package com.apigateway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apigateway.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
	
	@Query(value="SELECT * FROM users WHERE email=?1 AND is_validated=true", nativeQuery=true)
	Optional<User> findByEmail(String email);
	
	@Query(value="SELECT * FROM users WHERE email=?1", nativeQuery=true)
	Optional<User> findByEmailForNonValidatedUsers(String email);
	
	List<User> findAll();

}
