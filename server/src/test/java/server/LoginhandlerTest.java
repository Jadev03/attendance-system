package server;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.token.Tokengeneration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginhandlerTest {
    private Loginhandler loginHandler;
    private Login loginValidatorMock;
    private Tokengeneration tokenGeneratorMock;
    private HttpExchange httpExchangeMock;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        loginValidatorMock = mock(Login.class);
        tokenGeneratorMock = mock(Tokengeneration.class);

        // Instantiate the handler with mocked dependencies
        loginHandler = new Loginhandler() {
            @Override
            protected Login getLoginValidator() {
                return loginValidatorMock;
            }

            @Override
            protected Tokengeneration getTokenGenerator() {
                return tokenGeneratorMock;
            }
        };

        httpExchangeMock = mock(HttpExchange.class);
    }

    @Test
    void testHandleValidCredentials() throws Exception {
        // Arrange
        String validUsername = "testuser";
        String validPassword = "password";
        String token = "mockedToken";

        // Simulate HTTP request body
        JSONObject requestJson = new JSONObject();
        requestJson.put("username", validUsername);
        requestJson.put("password", validPassword);
        ByteArrayInputStream requestStream = new ByteArrayInputStream(requestJson.toString().getBytes(StandardCharsets.UTF_8));

        when(httpExchangeMock.getRequestMethod()).thenReturn("POST");
        when(httpExchangeMock.getRequestBody()).thenReturn(requestStream);

        // Mock validator and token generation
        when(loginValidatorMock.validate(validUsername, validPassword)).thenReturn(true);
        when(tokenGeneratorMock.generateJWT(validUsername)).thenReturn(token);

        // Capture output stream
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(httpExchangeMock.getResponseBody()).thenReturn(responseStream);

        // Act
        loginHandler.handle(httpExchangeMock);

        // Assert
        String response = new String(responseStream.toByteArray(), StandardCharsets.UTF_8);
        JSONObject responseJson = new JSONObject(response);

        assertEquals(200, responseJson.getInt("status"));
        assertEquals("Login successful", responseJson.getString("message"));
        assertEquals(token, responseJson.getString("token"));

        verify(httpExchangeMock).sendResponseHeaders(eq(200), anyInt());
    }

    @Test
    void testHandleInvalidCredentials() throws Exception {
        // Arrange
        String invalidUsername = "invaliduser";
        String invalidPassword = "wrongpassword";

        // Simulate HTTP request body
        JSONObject requestJson = new JSONObject();
        requestJson.put("username", invalidUsername);
        requestJson.put("password", invalidPassword);
        ByteArrayInputStream requestStream = new ByteArrayInputStream(requestJson.toString().getBytes(StandardCharsets.UTF_8));

        when(httpExchangeMock.getRequestMethod()).thenReturn("POST");
        when(httpExchangeMock.getRequestBody()).thenReturn(requestStream);

        // Mock validator
        when(loginValidatorMock.validate(invalidUsername, invalidPassword)).thenReturn(false);

        // Capture output stream
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(httpExchangeMock.getResponseBody()).thenReturn(responseStream);

        // Act
        loginHandler.handle(httpExchangeMock);

        // Assert
        String response = new String(responseStream.toByteArray(), StandardCharsets.UTF_8);
        JSONObject responseJson = new JSONObject(response);

        assertEquals(401, responseJson.getInt("status"));
        assertEquals("Invalid credentials", responseJson.getString("message"));

        verify(httpExchangeMock).sendResponseHeaders(eq(401), anyInt());
    }

    @Test
    void testHandleEmptyRequestBody() throws Exception {
        // Arrange
        ByteArrayInputStream requestStream = new ByteArrayInputStream(new byte[0]);
        when(httpExchangeMock.getRequestMethod()).thenReturn("POST");
        when(httpExchangeMock.getRequestBody()).thenReturn(requestStream);

        // Capture output stream
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(httpExchangeMock.getResponseBody()).thenReturn(responseStream);

        // Act
        loginHandler.handle(httpExchangeMock);

        // Assert
        String response = new String(responseStream.toByteArray(), StandardCharsets.UTF_8);

        JSONObject responseJson = new JSONObject(response);

        assertEquals(400, responseJson.getInt("status"));
        assertEquals("Request body is empty", responseJson.getString("message"));

        verify(httpExchangeMock).sendResponseHeaders(eq(400), anyInt());
    }
}
