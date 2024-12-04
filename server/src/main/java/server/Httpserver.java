package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;

public class Httpserver {
    public static void main(String[] args) throws IOException {
        int port = 8080; // Backend server running on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Server is running on http://localhost:8080 ..........");

        // Add context handlers
        server.createContext("/login", exchange -> handleWithCors(exchange, new Loginhandler()));
        server.createContext("/logout", exchange -> handleWithCors(exchange, new Logouthandler()));

        server.setExecutor(null); // Default executor
        server.start();
    }

    // Utility method to handle CORS
    private static void handleWithCors(HttpExchange exchange, HttpHandler handler) throws IOException {
        // Add CORS headers for the frontend at localhost:3000
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Handle preflight requests
            exchange.sendResponseHeaders(204, -1);
        } else {
            handler.handle(exchange);
        }
    }
}