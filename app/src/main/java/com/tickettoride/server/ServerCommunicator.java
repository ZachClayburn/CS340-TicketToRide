package com.tickettoride.server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tickettoride.shared.Command;
import com.tickettoride.shared.Response;

public class ServerCommunicator {

    private static final int SERVER_PORT_NUMBER = 8080;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    private static Gson gson = new Gson();

    private HttpServer server;

    private ServerCommunicator() {}

    private void run() {
        try { server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS); }
        catch (IOException e) {
            System.out.println("Could not create HTTP server: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        server.setExecutor(null); // use the default executor
        server.createContext(COMMAND_DESIGNATOR, commandHandler);
        server.start();
    }

    private HttpHandler commandHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Command command = buildCommandFromExchange(exchange);
            Response response;
            try { response = new Response(command.execute()); }
            catch (Throwable t) {
                Throwable cause = t.getCause();
                response = new Response(cause);
            }
            sendResponse(exchange, response);
        }
    };

    private Command buildCommandFromExchange(HttpExchange exchange) throws  IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody());
        Command command = gson.fromJson(inputStreamReader, Command.class);
        inputStreamReader.close();
        return command;
    }

    private void sendResponse(HttpExchange exchange, Response response) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(response, outputStreamWriter);
        outputStreamWriter.close();
    };

    public static int getServerPortNumber() {
        return SERVER_PORT_NUMBER;
    }

    public static void main(String[] args) {
        new ServerCommunicator().run();
    }

    public static final String COMMAND_DESIGNATOR = "/command";
}
