package com.tms.service;

import com.tms.model.Employee;
import com.tms.repository.EmployeeRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import com.tms.specification.EmployeeSpecification;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "employees", key = "#page + '-' + #size + '-' + #name + '-' + #className + '-' + #sortField + '-' + #sortDir")
    public Page<Employee> getEmployees(
            int page,
            int size,
            String name,
            String className,
            String sortField,
            String sortDir
    ) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortField == null ? "id" : sortField
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = Specification.where(EmployeeSpecification.hasName(name))
                .and(EmployeeSpecification.hasClassName(className));

        return repository.findAll(spec, pageable);
    }

    @Cacheable(value = "employee", key = "#id")
    public Employee getEmployee(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Employee save(Employee e) {
        return repository.save(e);
    }
}
