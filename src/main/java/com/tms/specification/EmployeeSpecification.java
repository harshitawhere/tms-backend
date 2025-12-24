package com.tms.specification;

import com.tms.model.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Employee> hasClassName(String className) {
        return (root, query, cb) ->
                className == null ? null : cb.equal(root.get("className"), className);
    }

}
