package com.restaurant.service;

import com.restaurant.dto.OrderRequest;
import com.restaurant.model.MenuItem;
import com.restaurant.model.Order;
import com.restaurant.model.OrderItem;
import com.restaurant.model.User;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    
    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = new Order();
        
        if (request.getCustomerId() != null) {
            User customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            order.setCustomer(customer);
        }
        
        order.setTableNumber(request.getTableNumber());
        order.setOrderType(request.getOrderType());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSpecialInstructions(request.getSpecialInstructions());
        
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        
        for (OrderRequest.OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setUnitPrice(menuItem.getPrice());
            orderItem.setSubtotal(menuItem.getPrice() * itemReq.getQuantity());
            
            orderItems.add(orderItem);
            totalPrice += orderItem.getSubtotal();
        }
        
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        order.setTaxAmount(totalPrice * 0.08);
        order.setServiceCharge(totalPrice * 0.10);
        order.setGrandTotal(totalPrice + order.getTaxAmount() + order.getServiceCharge());
        
        return orderRepository.save(order);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        
        if (status == Order.OrderStatus.COMPLETED) {
            order.setCompletedTime(LocalDateTime.now());
        }
        
        return orderRepository.save(order);
    }
}
