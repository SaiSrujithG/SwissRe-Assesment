package com.bigcompany;

import com.bigcompany.Repository.LoadFromCsv;
import com.bigcompany.model.Employee;
import com.bigcompany.service.EmployeeAnalyzer;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: java -jar big-company-analyzer.jar <bigCompanyData.csv>");
            System.exit(1);
            System.out.println("error is done");
        }

        LoadFromCsv reader = new LoadFromCsv();
        List<Employee> employees = reader.readLines(args[0]);

        EmployeeAnalyzer analyzer = new EmployeeAnalyzer(employees);

        System.out.println("=== Salary Issues ===");
        if (analyzer.reportSalaryIssues().isEmpty()) {
            System.out.println("No managers earning more or less than they should.");
        }
        analyzer.reportSalaryIssues().forEach(System.out::println);

        System.out.println("\n=== Long Reporting Lines ===");
        if (analyzer.reportLongChains().isEmpty()) {
            System.out.println("No employees with reporting lines that are too long.");
        }
        analyzer.reportLongChains().forEach(System.out::println);
    }
}