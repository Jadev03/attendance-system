package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.token.Tokenvalidation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Logouthandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            JSONObject requestJson;
            try {
                requestJson = (JSONObject) new JSONParser().parse(requestBody.toString());
                String token = (String) requestJson.get("token");

                if (token == null || token.isEmpty()) {
                    sendResponse(exchange, 400, "Token is required");
                    return;
                }

                Tokenvalidation tokenValidation = new Tokenvalidation();
                if (tokenValidation.validateToken(token)) {
                    if (removeToken(token)) {
                        sendResponse(exchange, 200, "Logout successful");
                    } else {
                        sendResponse(exchange, 500, "Failed to logout");
                    }
                } else {
                    sendResponse(exchange, 401, "Invalid or expired token");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 400, "Invalid request format");
            }
        } else {
            sendResponse(exchange, 405, "Method not allowed");
        }
    }

    private boolean removeToken(String token) {
        Path path = Paths.get("src", "main", "resources", "tokenInfo.json");
        if (!Files.exists(path)) {
            System.err.println("Token file not found: " + path.toAbsolutePath());
            return false;
        }

        try {
            JSONParser parser = new JSONParser();
            JSONArray tokenArray;
            try (FileReader reader = new FileReader(path.toString())) {
                tokenArray = (JSONArray) parser.parse(reader);
            }

            boolean tokenRemoved = false;
            JSONArray updatedArray = new JSONArray();

            for (Object obj : tokenArray) {
                JSONObject tokenObj = (JSONObject) obj;
                if (!token.equals(tokenObj.get("token"))) {
                    updatedArray.add(tokenObj);
                } else {
                    tokenRemoved = true;
                }
            }

            if (tokenRemoved) {
                try (FileWriter writer = new FileWriter(path.toString())) {
                    writer.write(updatedArray.toJSONString());
                }
            }

            return tokenRemoved;
        } catch (Exception e) {
            System.err.println("Error updating token list: " + e.getMessage());
        }
        return false;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", message);
        String response = responseJson.toJSONString();

        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
