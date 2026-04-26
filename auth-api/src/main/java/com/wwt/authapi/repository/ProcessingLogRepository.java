package com.wwt.authapi.repository;

import com.wwt.authapi.entity.ProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, Long> {
}
