package com.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.userservice.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	public Optional<Customer> findByEmail(String email);

	@Query(value="SELECT * FROM customers WHERE phone=?1 AND email=?2", nativeQuery=true)
	public Optional<Customer> findByEmailAndPhone(String phone, String email);
}
