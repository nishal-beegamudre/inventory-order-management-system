package com.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userservice.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	public Optional<Employee> findByOfficialEmail(String officialEmail);

	public Optional<Employee> findByEmployeeNumber(String employeeNumber);
}
