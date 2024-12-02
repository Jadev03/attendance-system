package server.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class Tokenvalidation{

    private static final String SECRET = "Visayam-Top-Secret";

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();

            // Verify the token
            DecodedJWT decodedJWT = verifier.verify(token);

            return checkTokenList(decodedJWT);
        } catch (JWTVerificationException e) {
            System.err.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    private boolean checkTokenList(DecodedJWT decodedJWT) {
        String username = decodedJWT.getSubject();
        Path path = Paths.get("src", "main", "resources", "tokenInfo.json");

        try (FileReader reader = new FileReader(path.toString())) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            boolean isTokenValid = false;

            // Create a new JSONArray for valid tokens
            JSONArray updatedTokenList = new JSONArray();

            for (Object obj : jsonArray) {
                JSONObject userObject = (JSONObject) obj;
                String storedUsername = (String) userObject.get("username");

                if (storedUsername.equals(username)) {
                    Instant tokenExpiry = decodedJWT.getExpiresAt().toInstant();
                    if (tokenExpiry.isBefore(Instant.now())) {
                        System.out.println("Token is expired for user: " + username);
                        continue; // Skip adding this expired token to the updated list
                    }
                    isTokenValid = true; // Found a valid token
                }

                // Add all valid tokens to the updated list
                updatedTokenList.add(userObject);
            }

            // Write the updated list back to the file
            try (FileWriter writer = new FileWriter(path.toString())) {
                writer.write(updatedTokenList.toJSONString());
            }

            return isTokenValid;

        } catch (IOException | ParseException e) {
            System.err.println("Error reading or parsing token list: " + e.getMessage());
        }
        return false; // Token not found or an error occurred
    }


//    public static void main(String[] args) {
//        TokenValidation validator = new TokenValidation();
//        String token = "your-jwt-token-here"; // Replace with your actual token
//        boolean isValid = validator.validateToken(token);
//        if (isValid) {
//            System.out.println("Token is valid.");
//        } else {
//            System.out.println("Token is invalid.");
//        }
//    }
}
