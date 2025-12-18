package com.restaurant.repository;

import com.restaurant.model.Employee;
import com.restaurant.model.Employee.EmployeeRole;
import com.restaurant.model.Employee.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmployeeRole(EmployeeRole role);
    List<Employee> findByStatus(EmployeeStatus status);
}
