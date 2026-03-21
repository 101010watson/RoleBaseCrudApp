package com.example.crudapp.service;

import com.example.crudapp.Entity.Employee;
import com.example.crudapp.Entity.User;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(Integer id);
    Employee save(Employee theEmployee);
    void delete(Integer id);
}
