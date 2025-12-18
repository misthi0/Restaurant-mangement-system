package com.restaurant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole employeeRole;
    
    @Column(unique = true)
    private String email;
    
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;
    
    private Double salary;
    
    @Column(name = "hire_date")
    private LocalDateTime hireDate;
    
    @PrePersist
    protected void onCreate() {
        hireDate = LocalDateTime.now();
    }
    
    public enum EmployeeRole {
        CHEF, WAITER, CASHIER, MANAGER, DELIVERY
    }
    
    public enum EmployeeStatus {
        ACTIVE, INACTIVE, ON_LEAVE
    }
}
