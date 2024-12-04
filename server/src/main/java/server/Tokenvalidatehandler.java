package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import server.token.Tokenvalidation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Tokenvalidatehandler implements HttpHandler {

    private final Tokenvalidation tokenValidator = new Tokenvalidation();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStream inputStream = exchange.getRequestBody();
            String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            JSONObject responseJson = new JSONObject();
            if (requestBody.isEmpty()) {
                responseJson.put("message", "Request body is empty");
                sendResponse(exchange, 400, responseJson);
                return;
            }

            JSONObject requestJson = new JSONObject(requestBody);
            String token = requestJson.optString("token");

            if (token.isEmpty()) {
                responseJson.put("message", "Token is missing in the request");
                sendResponse(exchange, 400, responseJson);
                return;
            }

            // Validate the token
            boolean isValid = tokenValidator.validateToken(token);

            if (isValid) {
                responseJson.put("message", "Token is valid");
                sendResponse(exchange, 200, responseJson);
            } else {
                responseJson.put("message", "Token is invalid or expired");
                sendResponse(exchange, 401, responseJson);
            }
        } else {
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Method not allowed");
            sendResponse(exchange, 405, responseJson);
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, JSONObject responseJson) throws IOException {
        byte[] responseBytes = responseJson.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.close();
    }
}
