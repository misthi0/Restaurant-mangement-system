package com.restaurant.service;

import com.restaurant.model.Inventory;
import com.restaurant.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found"));
    }
    
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }
    
    public Inventory createInventoryItem(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    
    public Inventory updateInventory(Long id, Inventory inventory) {
        Inventory existing = getInventoryById(id);
        existing.setItemName(inventory.getItemName());
        existing.setQuantity(inventory.getQuantity());
        existing.setUnit(inventory.getUnit());
        existing.setMinThreshold(inventory.getMinThreshold());
        existing.setSupplier(inventory.getSupplier());
        return inventoryRepository.save(existing);
    }
    
    public void deleteInventoryItem(Long id) {
        inventoryRepository.deleteById(id);
    }
}
