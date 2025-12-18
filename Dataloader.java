package com.restaurant.config;

import com.restaurant.model.*;
import com.restaurant.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            MenuItemRepository menuItemRepository,
            InventoryRepository inventoryRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            try {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@restaurant.com");
            admin.setFullName("Admin User");
            admin.setPhone("1234567890");
            admin.setRole(User.UserRole.ADMIN);
            userRepository.save(admin);

            User customer = new User();
            customer.setUsername("customer");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setEmail("customer@example.com");
            customer.setFullName("John Doe");
            customer.setPhone("0987654321");
            customer.setRole(User.UserRole.CUSTOMER);
            userRepository.save(customer);
            
            User kushal = new User();
            kushal.setUsername("kushal");
            kushal.setPassword(passwordEncoder.encode("kushal123"));
            kushal.setEmail("kushal@srmist.edu.in");
            kushal.setFullName("Kushal - SRM-IST Delhi NCR Campus");
            kushal.setPhone("9876543210");
            kushal.setRole(User.UserRole.STAFF);
            userRepository.save(kushal);
            
            User param = new User();
            param.setUsername("param");
            param.setPassword(passwordEncoder.encode("param123"));
            param.setEmail("param@srmist.edu.in");
            param.setFullName("Param - SRM-IST Delhi NCR Campus");
            param.setPhone("9876543211");
            param.setRole(User.UserRole.STAFF);
            userRepository.save(param);
            
            User priyanshi = new User();
            priyanshi.setUsername("priyanshi");
            priyanshi.setPassword(passwordEncoder.encode("priyanshi123"));
            priyanshi.setEmail("priyanshi@srmist.edu.in");
            priyanshi.setFullName("Priyanshi - SRM-IST Delhi NCR Campus");
            priyanshi.setPhone("9876543212");
            priyanshi.setRole(User.UserRole.MANAGER);
            userRepository.save(priyanshi);

            MenuItem pizza = new MenuItem();
            pizza.setName("Margherita Pizza");
            pizza.setDescription("Classic Italian pizza with fresh mozzarella and basil");
            pizza.setPrice(899.99);
            pizza.setCategory(MenuItem.Category.MAIN_COURSE);
            pizza.setAvailable(true);
            pizza.setImageUrl("https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400");
            menuItemRepository.save(pizza);

            MenuItem pasta = new MenuItem();
            pasta.setName("Spaghetti Carbonara");
            pasta.setDescription("Creamy pasta with bacon and parmesan");
            pasta.setPrice(999.99);
            pasta.setCategory(MenuItem.Category.MAIN_COURSE);
            pasta.setAvailable(true);
            pasta.setImageUrl("/assets/pasta.jpg");
            menuItemRepository.save(pasta);

            MenuItem salad = new MenuItem();
            salad.setName("Caesar Salad");
            salad.setDescription("Fresh romaine lettuce with parmesan and croutons");
            salad.setPrice(599.99);
            salad.setCategory(MenuItem.Category.STARTERS);
            salad.setAvailable(true);
            salad.setImageUrl("https://images.unsplash.com/photo-1546793665-c74683f339c1?w=400");
            menuItemRepository.save(salad);

            MenuItem tiramisu = new MenuItem();
            tiramisu.setName("Tiramisu");
            tiramisu.setDescription("Traditional Italian dessert with coffee and mascarpone");
            tiramisu.setPrice(499.99);
            tiramisu.setCategory(MenuItem.Category.DESSERTS);
            tiramisu.setAvailable(true);
            tiramisu.setImageUrl("https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400");
            menuItemRepository.save(tiramisu);

            MenuItem coffee = new MenuItem();
            coffee.setName("Espresso");
            coffee.setDescription("Strong Italian coffee");
            coffee.setPrice(249.99);
            coffee.setCategory(MenuItem.Category.DRINKS);
            coffee.setAvailable(true);
            coffee.setImageUrl("/assets/coffee.jpg");
            menuItemRepository.save(coffee);

            Inventory flour = new Inventory();
            flour.setItemName("Flour");
            flour.setQuantity(50);
            flour.setUnit("kg");
            flour.setMinThreshold(10);
            flour.setSupplier("Food Supplies Inc");
            inventoryRepository.save(flour);

            Inventory cheese = new Inventory();
            cheese.setItemName("Mozzarella Cheese");
            cheese.setQuantity(5);
            cheese.setUnit("kg");
            cheese.setMinThreshold(8);
            cheese.setSupplier("Dairy Co");
            inventoryRepository.save(cheese);

            System.out.println("✅ Sample data loaded successfully for SRM-IST Delhi NCR Campus Restaurant Management System!");
            System.out.println("Team Members: Kushal, Param, Priyanshi");
            } catch (Exception e) {
                System.err.println("❌ Error loading sample data: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}