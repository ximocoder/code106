package org.example;

import java.math.BigDecimal;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private Integer managerId; // Nullable for CEO

    public Employee(int id, String firstName, String lastName, BigDecimal salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public BigDecimal getSalary() { return salary; }
    public Integer getManagerId() { return managerId; }

    public void setSalary(BigDecimal salary) { this.salary = salary; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID: " + id + ")";
    }
}