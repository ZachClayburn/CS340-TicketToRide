package com.tickettoride.command;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WebsiteServer implements Runnable {

    private Logger logger = LogManager.getLogger(WebsiteServer.class.getName());

    private HttpServer server;
    private static final int MAX_WAITING_CONNECTIONS = 12;
    public static final int DEFAULT_PORT_NUMBER = 80;

    public WebsiteServer(int portNumber) throws IOException {
        server = HttpServer.create(new InetSocketAddress(portNumber), MAX_WAITING_CONNECTIONS);
    }

    @Override
    public void run() {
        logger.info("Starting the website server");

        server.createContext("/", exchange -> {
            logger.info("Received website request from " + exchange.getRemoteAddress());

            if (!exchange.getRequestMethod().toLowerCase().equals("get")) {
                exchange.sendResponseHeaders(HttpsURLConnection.HTTP_BAD_METHOD, -1);
                return;
            }

            try {

                String requestURL = exchange.getRequestURI().toString();
                if (requestURL.isEmpty() || requestURL.equals("/"))
                    requestURL = "/index.html";

                var classLoader = getClass().getClassLoader();
                var inputStream = classLoader.getResourceAsStream("website" + requestURL);

                if (inputStream == null) {

                    inputStream = classLoader.getResourceAsStream("website/404.html");
                    assert inputStream != null;
                    var bytes = inputStream.readAllBytes();

                    exchange.sendResponseHeaders(HttpsURLConnection.HTTP_NOT_FOUND, bytes.length);
                    try (var responseBody = exchange.getResponseBody()) {
                        responseBody.write(bytes);
                    }
                    return;
                }

                var bytes = inputStream.readAllBytes();
                exchange.sendResponseHeaders(HttpsURLConnection.HTTP_OK, bytes.length);

                try (var responseBody = exchange.getResponseBody()) {
                    responseBody.write(bytes);
                }

            } catch (IOException e) {
                logger.error("Error sending reply to " + exchange.getRemoteAddress(), e);
            }
        });

        server.start();
    }
}
