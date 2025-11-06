package com.bigcompany.service;

import com.bigcompany.model.Employee;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EmployeeAnalyzer {

    private static final double MIN_SALARY_MULTIPLIER = 1.20; // 20% more
    private static final double MAX_SALARY_MULTIPLIER = 1.50; // 50% more
    private static final int MAX_REPORTING_LEVELS = 4;

    private Map<Integer, Employee> employeeMap;
    private Employee ceo;

    public EmployeeAnalyzer(List<Employee> employees) {
        this.employeeMap = new HashMap<>();
        buildOrganizationStructure(employees);
    }

    private void buildOrganizationStructure(List<Employee> employees) {
        for (Employee emp : employees) {
            employeeMap.put(emp.getId(), emp);
        }
        // link subordinates
        for (Employee emp : employeeMap.values()) {
            if (emp.getManagerId() == null) ceo = emp;
            else {
                Employee manager = employeeMap.get(emp.getManagerId());
                if (manager != null) manager.addSubordinate(emp);
            }
        }
    }

    public List<String> reportSalaryIssues() {
        List<String> result = new ArrayList<>();
        for (Employee manager : employeeMap.values()) {
            if (manager.getSubordinates().isEmpty()) continue;

            double avgSubordinateSalary = manager.getSubordinates()
                    .stream().mapToDouble(Employee::getSalary).average().orElse(0);
            double minExpectedSalary = avgSubordinateSalary * MIN_SALARY_MULTIPLIER;
            double maxExpectedSalary = avgSubordinateSalary * MAX_SALARY_MULTIPLIER;

            if (manager.getSalary() < minExpectedSalary) {
                result.add(manager.getFullName() + " earns too little by " +
                        String.format("%.2f", (minExpectedSalary - manager.getSalary())));
            } else if (manager.getSalary() > maxExpectedSalary) {
                result.add(manager.getFullName() + " earns too much by " +
                        String.format("%.2f", (manager.getSalary() - maxExpectedSalary)));
            }
        }
        return result;
    }

    public List<String> reportLongChains() {
        List<String> result = new ArrayList<>();
        for (Employee e : employeeMap.values()) {
            int levels = countManagers(e);
            if (levels > MAX_REPORTING_LEVELS) {
                result.add(e.getFullName() + " has too long reporting line by " + (levels - MAX_REPORTING_LEVELS));
            }
        }
        return result;
    }

    private int countManagers(Employee employee) {
        int count = 0;
        Integer mid = employee.getManagerId();
        while (mid != null) {
            count++;
            Employee m = employeeMap.get(mid);
            mid = (m != null) ? m.getManagerId() : null;
        }
        return count;
    }
}