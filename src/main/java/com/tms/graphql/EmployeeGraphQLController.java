package com.tms.graphql;

import com.tms.model.Employee;
import com.tms.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import java.util.Collections;

@Controller
public class EmployeeGraphQLController {

    private final EmployeeService service;

    public EmployeeGraphQLController(EmployeeService service) {
        this.service = service;
    }

    @QueryMapping
    public EmployeePage employees(
            @Argument int page,
            @Argument int size,
            @Argument String name,
            @Argument String className,
            @Argument String sortField,
            @Argument String sortDir
    ) {
        Page<Employee> result = service.getEmployees(
                page,
                size,
                name,
                className,
                sortField,
                sortDir
        );

        return new EmployeePage(
                result.getContent() != null ? result.getContent() : Collections.emptyList(),
                result.getTotalElements()
        );
    }

    @QueryMapping
    public Employee employee(@Argument Long id) {
        return service.getEmployee(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Employee addEmployee(
            @Argument String name,
            @Argument Integer age,
            @Argument String className,
            @Argument String role,
            @Argument java.util.List<String> subjects,
            @Argument Integer attendance
    ) {
        Employee e = new Employee();
        e.setName(name);
        e.setAge(age);
        e.setClassName(className);
        e.setRole(role);
        e.setSubjects(subjects);
        e.setAttendance(attendance);
        return service.save(e);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Employee updateEmployee(
            @Argument Long id,
            @Argument String name,
            @Argument Integer age,
            @Argument String className,
            @Argument String role,
            @Argument java.util.List<String> subjects,
            @Argument Integer attendance
    ) {
        Employee e = service.getEmployee(id);
        if (e == null) return null;
        if (name != null) e.setName(name);
        if (age != null) e.setAge(age);
        if (className != null) e.setClassName(className);
        if (role != null) e.setRole(role);
        if (subjects != null) e.setSubjects(subjects);
        if (attendance != null) e.setAttendance(attendance);
        return service.save(e);
    }
}
