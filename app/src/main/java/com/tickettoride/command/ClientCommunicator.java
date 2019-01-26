package com.tickettoride.command;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import command.Command;
import command.Response;

public class ClientCommunicator {

    public static ClientCommunicator SINGLETON = new ClientCommunicator();

    public ClientCommunicator() { }

    public Response send(Command command) throws Throwable {
        HttpURLConnection connection = openConnection(ServerCommunicator.COMMAND_DESIGNATOR);
        sendToServerCommunicator(connection, command);
        Response response = getResponse(connection);
        return response;
    }

    protected static Gson gson = new Gson();

    protected HttpURLConnection openConnection(String contextIdentifier) {
        HttpURLConnection result = null;
        try {
            URL url = new URL(URL_PREFIX + contextIdentifier);
            result = (HttpURLConnection) url.openConnection();
            result.setRequestMethod(HTTP_POST);
            result.setDoOutput(true);
            result.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected void sendToServerCommunicator(HttpURLConnection connection, Object objectToSend) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            gson.toJson(objectToSend, outputStreamWriter);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Response getResponse(HttpURLConnection connection) throws Throwable {
        Response result = null;
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (connection.getContentLength() == -1) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    result = gson.fromJson(inputStreamReader, Response.class);
                    inputStreamReader.close();
                }
            } else {
                throw new Exception(String.format("http code %d", connection.getResponseCode()));
            }
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.isThrowable()) {
            try { throw result.getThrowable(); }
            catch (ClassCastException e) { throw result.getThrowable(); }
        }
        return result;
    }

    //Auxiliary Constants, Attributes, and Methods
    private static final String SERVER_HOST = "localhost";
    private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + ServerCommunicator.getServerPortNumber();
    private static final String HTTP_POST = "POST";
}
