package com.hr.hrapplication.service;

import com.hr.hrapplication.dao.EmployeeEntity;
import com.hr.hrapplication.dao.EmployeeRepository;
import com.hr.hrapplication.domain.Employee;
import com.hr.hrapplication.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.findAll()
                    .stream()
                    .map(employeeEntity -> modelMapper.map(employeeEntity, Employee.class))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to get all employees: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            EmployeeEntity employeeEntity = employeeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));
            log.info("Employee found with id {}: {}", id, employeeEntity);
            return modelMapper.map(employeeEntity, Employee.class);
        } catch (Exception e) {
            log.error("Failed to get employee by id {}: {}", id, e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Page<Employee> getEmployeeBySearchText(String searchText, Pageable pageable) {
        try {
            if (searchText == null || searchText.isBlank()) {
                return employeeRepository.findAll(pageable)
                        .map(employeeEntity -> modelMapper.map(employeeEntity, Employee.class));
            }
            return employeeRepository
                    .findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(searchText, searchText, pageable)
                    .map(employeeEntity -> modelMapper.map(employeeEntity, Employee.class));
        } catch (Exception e) {
            log.error("Failed to search employee by text: {}", searchText, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String deleteEmployeeById(String id) {
        try {
            employeeRepository.deleteById(id);
            log.info("Employee with id {} deleted successfully", id);
            return "Employee with id " + id + " deleted successfully";
        } catch (Exception e) {
            log.error("Failed to delete employee with id {}: {}", id, e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Employee saveOrEditEmployee(Employee employee) {
        try {
            EmployeeEntity employeeEntity = employeeRepository.findById(employee.getId()).orElse(null);
            if (employeeEntity != null) {
                employeeEntity.setFirstname(employee.getFirstname());
                employeeEntity.setLastname(employee.getLastname());
                employeeEntity.setDivision(employee.getDivision());
                employeeEntity.setBuilding(employee.getBuilding());
                employeeEntity.setTitle(employee.getTitle());
                employeeEntity.setRoom(employee.getRoom());
                employeeEntity.setUpdatedat(LocalDateTime.now());
                EmployeeEntity updatedEntity = employeeRepository.save(employeeEntity);
                log.info("Employee with id {} updated successfully", employee.getId());
                return modelMapper.map(updatedEntity, Employee.class);
            } else {
                EmployeeEntity newEmployeeEntity = modelMapper.map(employee, EmployeeEntity.class);
                EmployeeEntity savedEntity = employeeRepository.save(newEmployeeEntity);
                log.info("New employee with id {} created successfully", savedEntity.getId());
                return modelMapper.map(savedEntity, Employee.class);
            }
        } catch (Exception e) {
            log.error("Failed to save/edit employee: {}", employee, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
