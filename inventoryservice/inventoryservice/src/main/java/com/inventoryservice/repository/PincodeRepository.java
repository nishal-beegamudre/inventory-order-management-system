package com.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventoryservice.entity.Pincode;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, String> {

	@Query(value="SELECT * FROM pincodes WHERE pincode_number=?1", nativeQuery=true)
	public Optional<Pincode> findByPincode(String pincode);

	@Query(value="SELECT * FROM pincodes WHERE pincode_number IN ?1", nativeQuery=true)
	public List<Pincode> findPincodesByPincodeNumbers(List<String> pinCodeNumberList);
}
