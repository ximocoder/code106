package org.example;

import java.sql.SQLException;
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


        System.out.println("\n=== Running the Sql version ===");
        CompanyAnalyzerSql analyzerSql = new CompanyAnalyzerSql(filePath);

        try {
            analyzerSql.findSalaryViolations();
            analyzerSql.findReportingLineIssues();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}