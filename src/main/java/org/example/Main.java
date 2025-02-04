package org.example;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/employeesv2.csv";
        Map<Integer, Employee> employees = CSVReader.readEmployees(filePath);

        CompanyAnalyzer analyzer = new CompanyAnalyzer(employees);

        System.out.println("\n=== Salary Violations ===");
        List<String> salaryViolations = analyzer.findSalaryViolations();
        salaryViolations.forEach(System.out::println);

        System.out.println("\n=== Reporting Line Issues ===");
        List<String> reportingIssues = analyzer.findLongReportingLines();
        reportingIssues.forEach(System.out::println);
    }
}