package server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;

public class Httpserver{
    public static void main(String[] args) throws IOException {
        int port=8080; // define backend server running port number 8080
        HttpServer server= HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Server is running on http://localhost:8080 ..........");
        server.createContext("/login", new Loginhandler());
        server.setExecutor(null); // Use the default executor
        server.start();      
    }


}