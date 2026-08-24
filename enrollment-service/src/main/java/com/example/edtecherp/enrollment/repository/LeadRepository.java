package com.example.edtecherp.enrollment.repository;

import com.example.edtecherp.enrollment.entity.Lead;
import com.example.edtecherp.enrollment.enums.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    Page<Lead> findByStatus(LeadStatus status, Pageable pageable);

    Optional<Lead> findByEmail(String email);

    boolean existsByEmail(String email);
}
