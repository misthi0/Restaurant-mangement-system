package com.restaurant.dto;

import com.restaurant.model.Order.OrderType;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long customerId;
    private Integer tableNumber;
    private OrderType orderType;
    private List<OrderItemRequest> items;
    private String specialInstructions;
    
    @Data
    public static class OrderItemRequest {
        private Long menuItemId;
        private Integer quantity;
    }
}
