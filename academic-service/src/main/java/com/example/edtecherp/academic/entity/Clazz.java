package com.example.edtecherp.academic.entity;

import com.example.edtecherp.academic.enums.ClassStatus;
import com.example.edtecherp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class (section) of a course.
 * Named 'Clazz' to avoid conflict with java.lang.Class.
 * Maps to database table 'classes'.
 *
 * Schedule is stored inline (MVP): schedule_days + start_time + end_time
 * for fixed weekly schedule (e.g., MON,WED,FRI 18:00-20:00).
 */
@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clazz extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "max_students")
    @Builder.Default
    private Integer maxStudents = 30;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    /** Weekly schedule days, comma-separated: "MON,WED,FRI" */
    @Column(name = "schedule_days", length = 50)
    private String scheduleDays;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "room", length = 50)
    private String room;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ClassStatus status = ClassStatus.UPCOMING;

    @OneToMany(mappedBy = "clazz")
    @Builder.Default
    private List<ClassEnrollment> enrollments = new ArrayList<>();
}
