package com.restaurant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;
    
    private Integer tableNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    private Double totalPrice;
    private Double taxAmount;
    private Double serviceCharge;
    private Double grandTotal;
    
    private String specialInstructions;
    
    @Column(name = "order_time")
    private LocalDateTime orderTime;
    
    @Column(name = "completed_time")
    private LocalDateTime completedTime;
    
    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
        orderNumber = "ORD-" + System.currentTimeMillis();
    }
    
    public enum OrderType {
        DINE_IN, TAKEAWAY, DELIVERY
    }
    
    public enum OrderStatus {
        PENDING, CONFIRMED, PREPARING, READY, SERVED, COMPLETED, CANCELLED
    }
}
