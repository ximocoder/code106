package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyAnalyzerTest {

    private CompanyAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        Map<Integer, Employee> employees = new HashMap<>();

        // Creating a hierarchy with more than 4 levels
        employees.put(1, new Employee(1, "Alice", "CEO", new BigDecimal(200000) , null));
        employees.put(2, new Employee(2, "Bob", "Manager1", new BigDecimal(120000), 1));
        employees.put(3, new Employee(3, "Charlie", "Manager2", new BigDecimal(100000), 2));
        employees.put(4, new Employee(4, "David", "Manager3", new BigDecimal(90000), 3));
        employees.put(5, new Employee(5, "Eve", "Manager4", new BigDecimal(85000), 4));
        employees.put(6, new Employee(6, "Frank", "Manager5", new BigDecimal(80000), 5));  // Depth = 5 (too deep)
        employees.put(7, new Employee(7, "Grace", "Employee", new BigDecimal(75000), 6));  // Depth = 6 (too deep)

        analyzer = new CompanyAnalyzer(employees);
    }

    @Test
    void testSalaryViolations() {
        List<String> violations = analyzer.findSalaryViolations();
        assertFalse(violations.isEmpty(), "Salary violations should be detected");

        // Checking expected violations
        assertTrue(violations.stream().anyMatch(v -> v.contains("Alice")), "Alice should have a salary check issue");
    }

    @Test
    void testLongReportingLines() {
        List<String> reportingIssues = analyzer.findLongReportingLines();
        assertFalse(reportingIssues.isEmpty(), "There should be reporting line violations");

        // Checking specific employees
        assertTrue(reportingIssues.stream().anyMatch(v -> v.contains("Frank")), "Frank should be flagged for too deep reporting");
        assertTrue(reportingIssues.stream().anyMatch(v -> v.contains("Grace")), "Grace should be flagged for too deep reporting");
    }

}
