package com.bigcompany.Repository;

import com.bigcompany.model.Employee;

import java.io.IOException;
import java.util.List;

public interface FileReader {
    List<Employee> readLines(String filePath) throws IOException;
}