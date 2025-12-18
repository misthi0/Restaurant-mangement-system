package com.restaurant.repository;

import com.restaurant.model.MenuItem;
import com.restaurant.model.MenuItem.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(Category category);
    List<MenuItem> findByAvailable(Boolean available);
    List<MenuItem> findByNameContainingIgnoreCase(String name);
}
