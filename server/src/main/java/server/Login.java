package server;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

public class Login {

    public static boolean validate(String inputUsername, String inputPassword) {
        boolean isValid = false;

        try {
            JSONParser parser = new JSONParser();
            // Load the userInfo.json file
            Path path = Paths.get("src", "main", "resources", "userInfo.json");
            FileReader reader = new FileReader(path.toString());

            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Iterate through the array to find the matching username
            for (Object obj : jsonArray) {
                JSONObject userObject = (JSONObject) obj;
                String storedUsername = (String) userObject.get("username");
                String storedHashedPassword = (String) userObject.get("password");

                // Match the username (case-insensitive if needed)
                if (storedUsername != null && storedUsername.equalsIgnoreCase(inputUsername)) {
                    // Check the password using BCrypt
                    if (storedHashedPassword != null && BCrypt.checkpw(inputPassword, storedHashedPassword)) {
                        isValid = true;
                        break;
                    }
                }
            }

            reader.close();
        } catch (IOException | ParseException e) {
            System.out.println("Error reading credentials: " + e.getMessage());
        }

        return isValid;
    }

//    public static void main(String[] args) {
//        // Simulate user input
//        String inputUsername = "Adminadmin"; // Replace with the username
//        String inputPassword = "Password@123"; // Replace with the plaintext password
//
//        // Validate credentials
//        if (validate(inputUsername, inputPassword)) {
//            System.out.println("Login successful!");
//        } else {
//            System.out.println("Invalid credentials.");
//        }
//    }
}
