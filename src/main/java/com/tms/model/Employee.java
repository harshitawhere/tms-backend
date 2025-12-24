package com.tms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "employee", indexes = {
        @Index(name = "idx_employee_name", columnList = "name"),
        @Index(name = "idx_employee_class", columnList = "class_name")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    @Column(name = "class_name")
    private String className;

    private String role;

    @ElementCollection
    @CollectionTable(name = "employee_subjects", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "subject")
    private List<String> subjects;

    private Integer attendance;
}
