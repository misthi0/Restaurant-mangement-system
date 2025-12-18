
import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.nio.file.*;

public class Main {
    private static Connection connection;
    
    public static void main(String[] args) throws Exception {
        System.out.println("🚀 Starting Restaurant Management System...");
        
        // Initialize H2 Database
        initDatabase();
        
        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 5000), 0);
        
        // API endpoints
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/menu", new MenuHandler());
        server.createContext("/api/orders", new OrderHandler());
        server.createContext("/api/reservations", new ReservationHandler());
        server.createContext("/api/inventory", new InventoryHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("✅ Server started on http://0.0.0.0:5000");
        System.out.println("📋 Preview will open automatically - check the top of your screen!");
    }
    
    private static void initDatabase() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:restaurant", "sa", "");
        
        Statement stmt = connection.createStatement();
        
        // Create tables
        stmt.execute("CREATE TABLE menu_items (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), description TEXT, price DECIMAL(10,2), category VARCHAR(50), available BOOLEAN)");
        stmt.execute("CREATE TABLE orders (id INT PRIMARY KEY AUTO_INCREMENT, customer_name VARCHAR(255), table_number INT, total DECIMAL(10,2), status VARCHAR(50), created_at TIMESTAMP)");
        stmt.execute("CREATE TABLE reservations (id INT PRIMARY KEY AUTO_INCREMENT, customer_name VARCHAR(255), phone VARCHAR(20), date VARCHAR(50), time VARCHAR(20), guests INT, status VARCHAR(50))");
        stmt.execute("CREATE TABLE inventory (id INT PRIMARY KEY AUTO_INCREMENT, item_name VARCHAR(255), quantity INT, unit VARCHAR(50), min_threshold INT)");
        
        // Insert sample data
        stmt.execute("INSERT INTO menu_items VALUES (1, 'Margherita Pizza', 'Classic Italian pizza', 12.99, 'MAIN_COURSE', true)");
        stmt.execute("INSERT INTO menu_items VALUES (2, 'Caesar Salad', 'Fresh romaine lettuce', 8.99, 'APPETIZER', true)");
        stmt.execute("INSERT INTO menu_items VALUES (3, 'Tiramisu', 'Italian coffee dessert', 6.99, 'DESSERT', true)");
        stmt.execute("INSERT INTO menu_items VALUES (4, 'Pasta Carbonara', 'Creamy pasta with bacon', 14.99, 'MAIN_COURSE', true)");
        
        System.out.println("✅ Database initialized with sample data");
    }
    
    static class StaticFileHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";
            
            File file = new File("src/main/resources/static" + path);
            if (!file.exists()) {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
            
            String contentType = "text/html";
            if (path.endsWith(".js")) contentType = "application/javascript";
            if (path.endsWith(".css")) contentType = "text/css";
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, file.length());
            
            OutputStream os = exchange.getResponseBody();
            Files.copy(file.toPath(), os);
            os.close();
        }
    }
    
    static class MenuHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            if ("GET".equals(exchange.getRequestMethod())) {
                try {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items");
                    
                    StringBuilder json = new StringBuilder("[");
                    boolean first = true;
                    while (rs.next()) {
                        if (!first) json.append(",");
                        json.append("{")
                            .append("\"id\":").append(rs.getInt("id")).append(",")
                            .append("\"name\":\"").append(rs.getString("name")).append("\",")
                            .append("\"description\":\"").append(rs.getString("description")).append("\",")
                            .append("\"price\":").append(rs.getDouble("price")).append(",")
                            .append("\"category\":\"").append(rs.getString("category")).append("\",")
                            .append("\"available\":").append(rs.getBoolean("available"))
                            .append("}");
                        first = false;
                    }
                    json.append("]");
                    
                    byte[] response = json.toString().getBytes();
                    exchange.sendResponseHeaders(200, response.length);
                    exchange.getResponseBody().write(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    byte[] response = "[]".getBytes();
                    exchange.sendResponseHeaders(500, response.length);
                    exchange.getResponseBody().write(response);
                }
            }
            exchange.getResponseBody().close();
        }
    }
    
    static class OrderHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
                BufferedReader br = new BufferedReader(isr);
                String body = br.readLine();
                
                byte[] response = "{\"status\":\"success\",\"message\":\"Order created\"}".getBytes();
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
            } else {
                byte[] response = "[]".getBytes();
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
            }
            exchange.getResponseBody().close();
        }
    }
    
    static class ReservationHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            byte[] response = "[]".getBytes();
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.getResponseBody().close();
        }
    }
    
    static class InventoryHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            byte[] response = "[]".getBytes();
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
            exchange.getResponseBody().close();
        }
    }
}
