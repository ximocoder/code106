package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyAnalyzerSql {
    private static final String DB_URL = "jdbc:sqlite::memory:";
    private Connection conn;

    public CompanyAnalyzerSql(String filePath) {
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTables();
            insertDataFromCSV(filePath);
            analyze();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String createTableSQL = """
                CREATE TABLE employees (
                    id INTEGER PRIMARY KEY,
                    firstName TEXT,
                    lastName TEXT,
                    salary REAL,
                    managerId INTEGER,
                    FOREIGN KEY (managerId) REFERENCES employees(id)
                );
            """;
            stmt.execute(createTableSQL);
        }
    }

    private void insertDataFromCSV(String filePath) throws SQLException, IOException {
        String insertSQL = "INSERT INTO employees (id, firstName, lastName, salary, managerId) VALUES (?, ?, ?, ?, ?);";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) { // Skip the header row
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String firstName = values[1];
                String lastName = values[2];
                double salary = Double.parseDouble(values[3]);
                Integer managerId = values.length > 4 && !values[4].isEmpty() ? Integer.parseInt(values[4]) : null;

                pstmt.setInt(1, id);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setDouble(4, salary);
                if (managerId != null) {
                    pstmt.setInt(5, managerId);
                } else {
                    pstmt.setNull(5, Types.INTEGER);
                }
                pstmt.executeUpdate();
            }
        }
    }

    private void analyze() throws SQLException {
        System.out.println("\n=== Salary Violations ===");
        List<String> salaryViolations = findSalaryViolations();
        salaryViolations.forEach(System.out::println);

        System.out.println("\n=== Reporting Line Issues ===");
        List<String> reportingIssues = findReportingLineIssues();
        reportingIssues.forEach(System.out::println);
    }

    public List<String> findSalaryViolations() throws SQLException {
        List<String> violations = new ArrayList<>();

        String query = """
            WITH SubordinateSalaries AS (
                SELECT e.managerId, AVG(e.salary) AS avgSalary
                FROM employees e
                WHERE e.managerId IS NOT NULL
                GROUP BY e.managerId
            )
            SELECT m.firstName, m.salary, s.avgSalary, 
                   (s.avgSalary * 1.2) AS minSalary, (s.avgSalary * 1.5) AS maxSalary,
                   CASE
                       WHEN m.salary < (s.avgSalary * 1.2) THEN 'LESS'
                       WHEN m.salary > (s.avgSalary * 1.5) THEN 'MORE'
                   END AS violationType,
                   ABS(m.salary - (CASE 
                                    WHEN m.salary < (s.avgSalary * 1.2) THEN (s.avgSalary * 1.2)
                                    ELSE (s.avgSalary * 1.5)
                                   END)) AS violationAmount
            FROM employees m
            JOIN SubordinateSalaries s ON m.id = s.managerId
            WHERE m.salary < (s.avgSalary * 1.2) OR m.salary > (s.avgSalary * 1.5);
        """;

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String name = rs.getString("firstName");
                double violationAmount = rs.getDouble("violationAmount");
                String violationType = rs.getString("violationType");

                violations.add(String.format("%s earns %s than allowed by %.2f", name, violationType, violationAmount));
            }
        }
        return violations;
    }

    public List<String> findReportingLineIssues() throws SQLException {
        List<String> issues = new ArrayList<>();

        String query = """
            WITH RECURSIVE ReportingHierarchy AS (
                SELECT id, firstName, managerId, 0 AS depth
                FROM employees
                WHERE managerId IS NULL
                UNION ALL
                SELECT e.id, e.firstName, e.managerId, rh.depth + 1
                FROM employees e
                JOIN ReportingHierarchy rh ON e.managerId = rh.id
            )
            SELECT id, firstName, depth
            FROM ReportingHierarchy
            WHERE depth > 4;
        """;

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("firstName");
                int depth = rs.getInt("depth");

                issues.add(String.format("%s (ID: %d) has %d levels to the CEO (Too long)", name, id, depth));
            }
        }
        return issues;
    }
}
