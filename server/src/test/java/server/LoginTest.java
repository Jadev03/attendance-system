package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void testLoginValidation() {
        // Create an instance of the Login class
        Login login = new Login();

        // Test valid username and password
        String validUsername = "user";
        String validPassword = "Password&123";
        assertTrue(login.validate(validUsername, validPassword), "Login should succeed for valid credentials");

        // Test invalid username
        String invalidUsername = "invalidUser";
        assertFalse(login.validate(invalidUsername, validPassword), "Login should fail for an invalid username");

        // Test invalid password
        String invalidPassword = "wrongPassword";
        assertFalse(login.validate(validUsername, invalidPassword), "Login should fail for an invalid password");

        // Test both invalid username and password
        assertFalse(login.validate(invalidUsername, invalidPassword), "Login should fail for both invalid username and password");
    }

    @Test
    void testPasswordLength() {
        Login login = new Login();

        // Test password shorter than 8 characters
        String shortPassword = "Pass12!";
        assertFalse(login.validate("user", shortPassword), "Login  fail if password is shorter than 8 characters");

        // Test password exactly 8 characters
        String validPassword = "Passw@12";
        assertFalse(login.validate("user", validPassword), "Login  fail as it doesn't match the hardcoded password");
    }

    @Test
    void testPasswordUppercaseRequirement() {
        Login login = new Login();

        // Test password with no uppercase letters
        String noUppercasePassword = "password&123";
        assertFalse(login.validate("user", noUppercasePassword), "Login  fail if password does not contain an uppercase letter");
    }

    @Test
    void testPasswordNumberRequirement() {
        Login login = new Login();

        // Test password with no numbers
        String noNumberPassword = "Password&!";
        assertFalse(login.validate("user", noNumberPassword), "Login  fail if password does not contain a number");
    }

    @Test
    void testPasswordSpecialCharacterRequirement() {
        Login login = new Login();

        // Test password with no special characters
        String noSpecialCharacterPassword = "Password123";
        assertFalse(login.validate("user", noSpecialCharacterPassword), "Login  fail if password does not contain a special character");
    }
}
