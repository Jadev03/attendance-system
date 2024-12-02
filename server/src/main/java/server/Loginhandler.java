package server;

import com.sun.net.httpserver.*;

import java.io.*;
import server.token.Tokengeneration;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;


public class Loginhandler implements HttpHandler {

    private final Tokengeneration tokenGenerator = new Tokengeneration();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
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
            if (Login.validate(username, password)) {
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
        return tokenGenerator.generateJWT(username);

    }
}
