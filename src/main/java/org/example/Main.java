package org.example;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/employeesv2.csv";
        Map<Integer, Employee> employees = CSVReader.readEmployees(filePath);
    }
}