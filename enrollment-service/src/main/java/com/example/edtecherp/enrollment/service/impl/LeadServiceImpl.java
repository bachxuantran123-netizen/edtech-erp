package com.example.edtecherp.enrollment.service.impl;

import com.example.edtecherp.enrollment.dto.request.CreateLeadRequest;
import com.example.edtecherp.enrollment.dto.request.UpdateLeadRequest;
import com.example.edtecherp.enrollment.dto.response.LeadResponse;
import com.example.edtecherp.enrollment.entity.Lead;
import com.example.edtecherp.enrollment.enums.LeadStatus;
import com.example.edtecherp.enrollment.exception.BusinessRuleException;
import com.example.edtecherp.enrollment.exception.DuplicateResourceException;
import com.example.edtecherp.enrollment.exception.ResourceNotFoundException;
import com.example.edtecherp.enrollment.repository.LeadRepository;
import com.example.edtecherp.enrollment.service.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;

    @Override
    @Transactional
    public LeadResponse createLead(CreateLeadRequest request) {
        if (leadRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Lead with email '" + request.getEmail() + "' already exists");
        }

        Lead lead = Lead.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .source(request.getSource())
                .notes(request.getNotes())
                .build();

        Lead saved = leadRepository.save(lead);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadResponse getLeadById(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", id));
        return toResponse(lead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadResponse> getAllLeads(LeadStatus status, Pageable pageable) {
        Page<Lead> leads;
        if (status != null) {
            leads = leadRepository.findByStatus(status, pageable);
        } else {
            leads = leadRepository.findAll(pageable);
        }
        return leads.map(this::toResponse);
    }

    @Override
    @Transactional
    public LeadResponse updateLead(Long id, UpdateLeadRequest request) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", id));

        // Check email uniqueness if email is being changed
        if (request.getEmail() != null && !request.getEmail().equals(lead.getEmail())) {
            if (leadRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Lead with email '" + request.getEmail() + "' already exists");
            }
        }

        // Partial update — only overwrite non-null fields
        if (request.getFullName() != null) {
            lead.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            lead.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            lead.setPhone(request.getPhone());
        }
        if (request.getSource() != null) {
            lead.setSource(request.getSource());
        }
        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            lead.setNotes(request.getNotes());
        }

        Lead updated = leadRepository.save(lead);
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteLead(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", id));

        if (lead.getStatus() == LeadStatus.CONVERTED) {
            throw new BusinessRuleException("Cannot delete a lead that has been converted to a student");
        }

        leadRepository.delete(lead);
    }

    // ---- Mapping helpers ----

    private LeadResponse toResponse(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .fullName(lead.getFullName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .source(lead.getSource())
                .status(lead.getStatus())
                .notes(lead.getNotes())
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}
