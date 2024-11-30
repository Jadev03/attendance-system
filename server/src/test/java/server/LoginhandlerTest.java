package server;

import com.sun.net.httpserver.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class LoginhandlerTest {

    private Loginhandler loginHandler;
    private HttpExchange exchange;
    private ByteArrayOutputStream outputStream;

//    void setUp() throws IOException {
//        loginHandler = new Loginhandler();
//        exchange = mock(HttpExchange.class);
//        outputStream = new ByteArrayOutputStream();
//
//        // Mock the exchange behavior
//        when(exchange.getResponseBody()).thenReturn(outputStream);
//        doAnswer(invocation -> {
//            int statusCode = (int) invocation.getArguments()[0];
//            // Simulate writing status code as a response field for testing purposes
//            outputStream.write(("{\"status\":" + statusCode + "}").getBytes());
//            return null;
//        }).when(exchange).sendResponseHeaders(anyInt(), anyLong());
//    }
    @BeforeEach
    void setUp() throws IOException {
        loginHandler = new Loginhandler();
        exchange = mock(HttpExchange.class);
        outputStream = new ByteArrayOutputStream();

        // Mock the response body
        when(exchange.getResponseBody()).thenReturn(outputStream);

        // Mock the sendResponseHeaders to simulate status setting (optional for real HTTP response)
        doNothing().when(exchange).sendResponseHeaders(anyInt(), anyLong());
    }

    @Test
    void testHandleLogin_Success() {
        // Mock the request method and body for a successful login
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(
                "{\"username\":\"user\", \"password\":\"Password&123\"}".getBytes()
        ));

        try {
            loginHandler.handle(exchange);
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }

        // Verify the response body
        String response = outputStream.toString();
        JSONObject responseJson = new JSONObject(response);

        assertEquals("Login successful", responseJson.getString("message"), "Message should be 'Login successful'");
        assertTrue(responseJson.has("token"), "Response should include a token for valid login");
    }


    @Test
    void testHandleLogin_Failure_InvalidCredentials() {
        // Mock the request method and body for invalid login credentials
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(
                "{\"username\":\"wrongUser\", \"password\":\"wrongPassword\"}".getBytes()
        ));

        try {
            loginHandler.handle(exchange);
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }

        // Verify the response body
        String response = outputStream.toString();
        JSONObject responseJson = new JSONObject(response);

        assertEquals("Invalid credentials", responseJson.getString("message"), "Message should be 'Invalid credentials'");
    }

    @Test
    void testHandleLogin_EmptyRequest() {
        // Mock an empty request body
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream("".getBytes()));

        try {
            loginHandler.handle(exchange);
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }

        // Verify the response body
        String response = outputStream.toString();
        JSONObject responseJson = new JSONObject(response);

        assertEquals("Request body is empty", responseJson.getString("message"), "Message should be 'Request body is empty'");
    }

    @Test
    void testHandleUnsupportedMethod() {
        // Mock a method other than POST
        when(exchange.getRequestMethod()).thenReturn("GET");

        try {
            loginHandler.handle(exchange);
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }

        // Verify the response body
        String response = outputStream.toString();
        JSONObject responseJson = new JSONObject(response);

        assertEquals(405, responseJson.getInt("status"), "Status should be 405 for unsupported methods");
        assertEquals("Method not allowed", responseJson.getString("message"), "Message should be 'Method not allowed'");
    }

}


