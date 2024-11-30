package server;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

class HttpserverTest {

    @Test
    void testServerStartup() {
        int port = 8080;
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.start();
            assertNotNull(server, "Server should not be null");
            assertEquals(port, server.getAddress().getPort(), "Server port should match");
        } catch (IOException e) {
            fail("Server failed to start: " + e.getMessage());
        } finally {
            if (server != null) {
                server.stop(0);
            }
        }
    }
}
