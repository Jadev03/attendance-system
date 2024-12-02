package server;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

public class Registration {

    // Hash the password using BCrypt
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Register the user and append credentials to userInfo.json
    public void registerUser() {
        Scanner scanner = new Scanner(System.in);

        // Get username and password from user
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Hash the password
        String hashedPassword = hashPassword(password);

        // Create a JSON object for the new user
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", hashedPassword);

        // Determine the correct file path
        String resourcePath = "server/src/main/resources/userInfo.json";
        File file = new File(resourcePath);

        // Ensure the parent directories exist
        file.getParentFile().mkdirs();

        // Initialize JSON array to hold users
        JSONArray users = new JSONArray();

        // If the file already exists, read existing users
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(reader);
                users = (JSONArray) obj; // Load existing users into the array
            } catch (IOException | ParseException e) {
                System.out.println("Error reading existing user data: " + e.getMessage());
            }
        }

        // Add the new user to the array
        users.add(newUser);

        // Write the updated array back to the file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(users.toJSONString());
            fileWriter.flush();
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("Error saving user information: " + e.getMessage());
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Registration registration = new Registration();
        registration.registerUser();
    }
}
