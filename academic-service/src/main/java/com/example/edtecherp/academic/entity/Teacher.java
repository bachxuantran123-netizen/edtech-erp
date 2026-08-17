package com.example.edtecherp.academic.entity;

import com.example.edtecherp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a teacher with a linked Keycloak account.
 * Stores business-specific info not managed by Keycloak.
 */
@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Column(name = "keycloak_user_id", nullable = false, unique = true)
    private UUID keycloakUserId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "specialization")
    private String specialization;
}
