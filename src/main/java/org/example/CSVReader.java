package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class CSVReader {
    public static Map<Integer, Employee> readEmployees(String filePath) {
        Map<Integer, Employee> employees = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String firstName = values[1];
                String lastName = values[2];
                BigDecimal salary = new BigDecimal(values[3].trim());
                Integer managerId = values.length > 4 && !values[4].isEmpty() ? Integer.parseInt(values[4]) : null;
                employees.put(id, new Employee(id, firstName, lastName, salary, managerId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }
}