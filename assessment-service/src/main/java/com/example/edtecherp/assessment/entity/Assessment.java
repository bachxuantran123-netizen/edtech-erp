package com.example.edtecherp.assessment.entity;

import com.example.edtecherp.assessment.enums.AssessmentType;
import com.example.edtecherp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines an assessment (exam, assignment, quiz) for a class.
 * Weights across all assessments in a class should sum to 1.00 (validated in service layer).
 * class_id is a cross-service reference to Academic Service.
 */
@Entity
@Table(name = "assessments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"class_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment extends BaseEntity {

    /** Cross-service reference to classes.id in Academic Service */
    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private AssessmentType type;

    @Column(name = "weight", nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal maxScore = new BigDecimal("10.00");

    @Column(name = "due_date")
    private LocalDate dueDate;

    @OneToMany(mappedBy = "assessment")
    @Builder.Default
    private List<Grade> grades = new ArrayList<>();
}
