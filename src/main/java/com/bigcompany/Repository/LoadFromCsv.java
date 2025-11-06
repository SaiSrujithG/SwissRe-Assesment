package com.bigcompany.Repository;

import com.bigcompany.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoadFromCsv implements FileReader {

    @Override
    public List<Employee> readLines(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 1; i < lines.size(); i++) { // skip header
            String[] parts = lines.get(i).split(",");
            int id = Integer.parseInt(parts[0]);
            String first = parts[1];
            String last = parts[2];
            double salary = Double.parseDouble(parts[3]);
            Integer managerId = parts.length > 4 && !parts[4].isBlank() ? Integer.parseInt(parts[4]) : null;
            employees.add(new Employee(id, first, last, salary, managerId));
        }
        return employees;
    }
}