package com.example.edtecherp.assessment.entity;

import com.example.edtecherp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Stores a student's score for a specific assessment.
 * student_id is a cross-service reference to Enrollment Service.
 * score = null means not yet graded.
 */
@Entity
@Table(name = "grades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"assessment_id", "student_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    /** Cross-service reference to students.id in Enrollment Service */
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
}
