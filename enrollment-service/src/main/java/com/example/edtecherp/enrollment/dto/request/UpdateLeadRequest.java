package com.example.edtecherp.enrollment.dto.request;

import com.example.edtecherp.enrollment.enums.LeadSource;
import com.example.edtecherp.enrollment.enums.LeadStatus;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing lead.
 * All fields are nullable — only non-null fields will be updated (partial update).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLeadRequest {

    private String fullName;

    @Email(message = "Email must be valid")
    private String email;

    private String phone;

    private LeadSource source;

    private LeadStatus status;

    private String notes;
}
