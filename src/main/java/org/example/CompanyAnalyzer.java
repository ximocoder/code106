package org.example;

import java.util.*;
import java.math.BigDecimal;

public class CompanyAnalyzer {
    private final Map<Integer, Employee> employees;

    public CompanyAnalyzer(Map<Integer, Employee> employees) {
        this.employees = employees;
    }

    public List<String> findSalaryViolations() {
        List<String> violations = new ArrayList<>();

        for (Employee manager : employees.values()) {
            List<Employee> subordinates = getSubordinates(manager.getId());
            if (subordinates.isEmpty()) continue;

            // Calculate average salary of subordinates
            BigDecimal avgSalary = subordinates.stream()
                    .map(Employee::getSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add) // Sum all salaries
                    .divide(BigDecimal.valueOf(subordinates.size()), 2, BigDecimal.ROUND_HALF_UP); // Average

            BigDecimal minSalary = avgSalary.multiply(new BigDecimal("1.2"));
            BigDecimal maxSalary = avgSalary.multiply(new BigDecimal("1.5"));

            if (manager.getSalary().compareTo(minSalary) < 0) {
                violations.add(manager.getFirstName() + " earns LESS than required by " + minSalary.subtract(manager.getSalary()));
            } else if (manager.getSalary().compareTo(maxSalary) > 0) {
                violations.add(manager.getFirstName() + " earns MORE than allowed by " + manager.getSalary().subtract(maxSalary));
            }
        }
        return violations;
    }

    public List<String> findLongReportingLines() {
        List<String> reportingIssues = new ArrayList<>();

        for (Employee employee : employees.values()) {
            int depth = getReportingDepth(employee.getId());
            if (depth > 4) {
                reportingIssues.add(employee + " has " + depth + " levels to the CEO (Too long)");
            }
        }
        return reportingIssues;
    }

    private List<Employee> getSubordinates(int managerId) {
        List<Employee> subordinates = new ArrayList<>();
        for (Employee e : employees.values()) {
            if (e.getManagerId() != null && e.getManagerId() == managerId) {
                subordinates.add(e);
            }
        }
        return subordinates;
    }

    private int getReportingDepth(int employeeId) {
        int depth = 0;
        Integer managerId = employees.get(employeeId).getManagerId();

        while (managerId != null) {
            depth++;
            managerId = employees.containsKey(managerId) ? employees.get(managerId).getManagerId() : null;
        }
        return depth;
    }
}