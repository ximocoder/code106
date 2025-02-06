# Company Analyzer

This is a Java-based application designed to analyze the organizational structure of a company. It reads employee data from a CSV file and identifies:
1. **Salary Violations**: Managers who earn less than 20% or more than 50% of the average salary of their direct subordinates.
2. **Long Reporting Lines**: Employees who have more than 4 managers between them and the CEO.

The project is built using **Java SE**, **Maven** for project structure and build, and **JUnit** for testing.

---

## Table of Contents
1. [Requirements](#requirements)
2. [Project Structure](#project-structure)
3. [How to Run](#how-to-run)
4. [Input File Format](#input-file-format)
5. [Output](#output)
6. [Testing](#testing)
7. [Assumptions](#assumptions)
8. [Future Improvements](#future-improvements)

---

## Requirements
- Java SE (version 8 or higher)
- Maven (for building the project)
- JUnit (for running tests)

---

## Project Structure
The project is organized as follows:
```
company-analyzer/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── CompanyAnalyzer.java       # Analyzes salary violations and reporting lines
│   │   │   ├── CSVReader.java             # Reads employee data from a CSV file
│   │   │   ├── Employee.java              # Represents an employee
│   │   │   └── Main.java                  # Entry point for the application
│   │   └── resources/
│   │       └── employeesv2.csv            # Sample CSV file with employee data
│   └── test/
│       └── java/org/example/
│           └── CompanyAnalyzerTest.java   # Unit tests for CompanyAnalyzer
├── pom.xml                                # Maven build configuration
└── README.md                              # This file
```

---

## How to Run

### 1. Clone the Repository
```bash
git clone <repository-url>
cd company-analyzer
```

### 2. Build the Project
Use Maven to build the project:
```bash
mvn clean install
```

### 3. Run the Application
After building the project, run the `Main` class:
```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### 4. Run Tests
To run the unit tests:
```bash
mvn test
```

---

## Input File Format
The application reads employee data from a CSV file (`employeesv2.csv`). The file should have the following format:
```
id,firstName,lastName,salary,managerId
1,Alice,CEO,200000,
2,Bob,Manager1,120000,1
3,Charlie,Manager2,100000,2
4,David,Manager3,90000,3
5,Eve,Manager4,85000,4
6,Frank,Manager5,80000,5
7,Grace,Employee,75000,6
```

- **id**: Unique identifier for the employee.
- **firstName**: First name of the employee.
- **lastName**: Last name of the employee.
- **salary**: Employee's salary (as a number).
- **managerId**: ID of the employee's manager. Leave empty for the CEO.

---

## Output
The application prints the following to the console:

### Salary Violations
- Managers who earn less than 20% of the average salary of their subordinates.
- Managers who earn more than 50% of the average salary of their subordinates.

Example:
```
=== Salary Violations ===
Alice earns LESS than required by 40000.00
Bob earns MORE than allowed by 10000.00
```

### Reporting Line Issues
- Employees who have more than 4 managers between them and the CEO.

Example:
```
=== Reporting Line Issues ===
Frank has 5 levels to the CEO (Too long)
Grace has 6 levels to the CEO (Too long)
```

---

## Testing
Unit tests are provided in `CompanyAnalyzerTest.java`. They cover:
- Detection of salary violations.
- Detection of long reporting lines.

To run the tests:
```bash
mvn test
```

---

## Assumptions
1. The CEO has no manager (`managerId` is `null`).
2. The CSV file is well-formed and contains valid data.
3. Salaries are represented as positive numbers.
4. No cycles or multiple managers for an employee.

---

## Future Improvements
1. **Error Handling**: Add better error handling for invalid CSV files or missing data.
2. **Use an inmemory sqlite Db**: Instead of java code for the queries use a proper domain language
3. **Performance Optimization**: Cache reporting line depths and precompute subordinate lists for better performance.
4. **Logging**: Use a logging framework (e.g., Log4j) instead of printing to the console.
5. **GUI**: Add a simple GUI for easier interaction with the application.
6. **More Tests**: Add more test cases for edge cases (e.g., empty files, employees with no subordinates).

---

## License
This project is open-source and available under the [MIT License](LICENSE).
