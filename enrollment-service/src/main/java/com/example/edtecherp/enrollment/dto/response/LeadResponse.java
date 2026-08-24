package com.example.edtecherp.enrollment.dto.response;

import com.example.edtecherp.enrollment.enums.LeadSource;
import com.example.edtecherp.enrollment.enums.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LeadSource source;
    private LeadStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
