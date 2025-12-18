import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 5000
        HttpServer server = HttpServer.create(new InetSocketAddress(5000), 0);

        // Set the default handler
        server.createContext("/", new MainHandler());
        server.setExecutor(null); // creates a default executor
        server.start();

        System.out.println("🚀 Starting Restaurant Management System...");
        System.out.println("✅ Server started on http://0.0.0.0:5000");
    }

    static class MainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Welcome to the Restaurant Management System</h1>" +
                              "<p><a href='index.html'>Menu</a></p>";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}