package com.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, String> {

	@Query(value="SELECT * FROM states WHERE name IN ?1", nativeQuery=true)
	List<State> findByNames(List<String> stateNames);
}
