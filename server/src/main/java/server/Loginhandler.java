package server;

import com.sun.net.httpserver.*;

import java.io.*;

import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import java.util.Base64;

public class Loginhandler implements HttpHandler {
    private final Login loginValidator = new Login();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Parse the request body
            InputStream inputStream = exchange.getRequestBody();
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (requestBody.isEmpty()) {
                JSONObject responseJson = new JSONObject();
                responseJson.put("message", "Request body is empty");
                exchange.sendResponseHeaders(400, responseJson.toString().getBytes(StandardCharsets.UTF_8).length);

                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(responseJson.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.close();
                return;
            }

            JSONObject requestJson = new JSONObject(requestBody);
            String username = requestJson.optString("username");
            String password = requestJson.optString("password");

            JSONObject responseJson = new JSONObject();

            // Validating
            if (loginValidator.validate(username, password)) {
                String token = generateJWT(username);
                responseJson.put("message", "Login successful");
                responseJson.put("token", token);
                exchange.sendResponseHeaders(200, responseJson.toString().getBytes(StandardCharsets.UTF_8).length);
            } else {
                responseJson.put("message", "Invalid credentials");
                exchange.sendResponseHeaders(401, responseJson.toString().getBytes(StandardCharsets.UTF_8).length);
            }

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseJson.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } else {
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", 405);
            responseJson.put("message", "Method not allowed");
            byte[] responseBytes = responseJson.toString().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(405, responseBytes.length);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseBytes);
            outputStream.close();
        }
    }



    private String generateJWT(String username) {
        // Mock JWT generation (use proper libraries like JJWT or JOSE in production)
        String header = Base64.getEncoder().encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = Base64.getEncoder().encodeToString(("{\"username\":\"" + username + "\"}").getBytes());
        String signature = Base64.getEncoder().encodeToString("secretKey".getBytes());
        return header + "." + payload + "." + signature;
    }
}
