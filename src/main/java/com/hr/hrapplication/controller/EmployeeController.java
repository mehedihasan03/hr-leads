package com.hr.hrapplication.controller;

import com.hr.hrapplication.domain.Employee;
import com.hr.hrapplication.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Employee", description = "Employee lookup & search")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //    get all employees
    @GetMapping("/list")
    public ResponseEntity<?> getEmployees(Pageable pageable) {
        List<Employee> employeeList = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok().body(Map.of(
                "data", employeeList,
                "total", employeeList.size()
        ));
    }

//    search by employee id
    @GetMapping("/search/id/{id}")
    public ResponseEntity<?> searchEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok().body(Map.of(
                "data", employee,
                "total", employee != null ? 1 : 0
        ));
    }

//    search by employee name
    @GetMapping("/search/name/{searchText}")
    public ResponseEntity<?> searchEmployeeByText(@PathVariable String searchText, Pageable pageable) {
        Page<Employee> employees = employeeService.getEmployeeBySearchText(searchText, pageable);
        return ResponseEntity.ok().body(Map.of(
                "data", employees.getContent(),
                "total", employees.getTotalElements()
        ));
    }

//    delete by employee id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable String id) {
        String deleted = employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok().body(deleted);
    }

//    Edit employee details
    @PostMapping("/save")
    public ResponseEntity<?> editEmployee(@RequestBody Employee employee) {
        // Implementation for editing employee details goes here
        Employee emp = employeeService.saveOrEditEmployee(employee);
        return ResponseEntity.ok().body(Map.of(
                "data", emp,
                "total", emp != null ? 1 : 0
        ));
    }
}
