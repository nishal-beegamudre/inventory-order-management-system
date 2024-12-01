package com.consignmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consignmentservice.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
