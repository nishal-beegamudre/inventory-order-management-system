package com.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notificationservice.entity.EmailLog;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, String> {
}
