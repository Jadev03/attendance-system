package server.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Tokengeneration {

    // Load environment variables

    // Retrieve the secret key from the .env file
    private static final String SECRET = "Visayam-Top-Secret";
    //private static final String TOKEN_FILE = "tokenInfo.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String generateJWT(String username) {
        // Set token expiration time (30 minutes)
        long expirationTimeInMillis = 1800000; // 30 minutes
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMillis);



        // Generate JWT
        String token = JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(SECRET));

        // Store the username and token in the JSON file
        appendTokenToFile(username, token);

        return token;
    }

    private void appendTokenToFile(String username, String token) {
        JsonArray tokenArray;
        Path path = Paths.get("src", "main", "resources", "tokenInfo.json");
      //  FileReader reader = new FileReader(path.toString());
      //  String resourcePath = "server/src/main/resources/tokenInfo.json";
        File file = new File(path.toString());
        // Read existing data
        try (FileReader reader = new FileReader(file)) {
            tokenArray = gson.fromJson(reader, JsonArray.class);
            if (tokenArray == null) {
                tokenArray = new JsonArray();
            }
        } catch (IOException e) {
            // If file doesn't exist or is empty, initialize a new array
            tokenArray = new JsonArray();
        }

        // Create a new token entry
        JsonObject tokenEntry = new JsonObject();
        tokenEntry.addProperty("username", username);
        tokenEntry.addProperty("token", token);

        // Add the entry to the array
        tokenArray.add(tokenEntry);

        // Write the updated data back to the file
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(tokenArray, writer);
        } catch (IOException e) {
            System.err.println("Error writing to token file: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        Tokengeneration tokenGen = new Tokengeneration();
//        String token = tokenGen.generateJWT("exampleUser");
//        System.out.println("Generated JWT: " + token);
//    }
}
