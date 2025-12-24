package com.tms.graphql;

import com.tms.model.Employee;
import java.util.List;
import java.util.Collections;

public record EmployeePage(List<Employee> data, long totalCount) {
    public EmployeePage {
        if (data == null) data = Collections.emptyList();
    }
}
