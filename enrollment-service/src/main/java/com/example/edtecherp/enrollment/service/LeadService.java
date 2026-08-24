package com.example.edtecherp.enrollment.service;

import com.example.edtecherp.enrollment.dto.request.CreateLeadRequest;
import com.example.edtecherp.enrollment.dto.request.UpdateLeadRequest;
import com.example.edtecherp.enrollment.dto.response.LeadResponse;
import com.example.edtecherp.enrollment.enums.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadService {

    LeadResponse createLead(CreateLeadRequest request);

    LeadResponse getLeadById(Long id);

    Page<LeadResponse> getAllLeads(LeadStatus status, Pageable pageable);

    LeadResponse updateLead(Long id, UpdateLeadRequest request);

    void deleteLead(Long id);
}
