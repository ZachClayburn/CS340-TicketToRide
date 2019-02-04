package com.tickettoride.command;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import command.Command;
import command.Response;

public class ClientCommunicator {

    private WebSocketClient mWebSocketClient;
    protected static Gson gson = new Gson();
    //TODO: change the websockethost to the actual host IP (probably 10.0.0.2 if I remember right)
    private String websockethost="websockethost";
    private String port= "8080";

    private static ClientCommunicator SINGLETON = new ClientCommunicator();

    private ClientCommunicator() {
        try {

            mWebSocketClient = new WebSocketClient(new URI("ws://"+websockethost+":"+port), new Draft_6455()) {

                @Override
                public void onMessage(String message) {
                    Response response=gson.fromJson(message, Response.class);
                    //TODO: send it off to whatever callback method will handle the response
                    //NOTE: might have to switch threads to do so, not sure.
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    //whatever we want to do. log opening, etc
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    //whatever we want to do. log closing, try to reconnect, etc
                }

                @Override
                public void onError(Exception ex) {
                    //however we want to handle errors on the websocket (usually IO)
                }
            };
        }catch(URISyntaxException e){

        }
    }

    public static ClientCommunicator getSingleton() {
        return SINGLETON;
    }


    public void send(Command command){
        String message=gson.toJson(command);
        mWebSocketClient.send(message);
    }

    //    public Response send(Command command) throws Throwable {
//        HttpURLConnection connection = openConnection(ServerCommunicator.COMMAND_DESIGNATOR);
//        sendToServerCommunicator(connection, command);
//        Response response = getResponse(connection);
//        return response;
//    }
//
//    protected static Gson gson = new Gson();
//
//    protected HttpURLConnection openConnection(String contextIdentifier) {
//        HttpURLConnection result = null;
//        try {
//            URL url = new URL(URL_PREFIX + contextIdentifier);
//            result = (HttpURLConnection) url.openConnection();
//            result.setRequestMethod(HTTP_POST);
//            result.setDoOutput(true);
//            result.connect();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    protected void sendToServerCommunicator(HttpURLConnection connection, Object objectToSend) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
//            gson.toJson(objectToSend, outputStreamWriter);
//            outputStreamWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected Response getResponse(HttpURLConnection connection) throws Throwable {
//        Response result = null;
//        try {
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                if (connection.getContentLength() == -1) {
//                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
//                    result = gson.fromJson(inputStreamReader, Response.class);
//                    inputStreamReader.close();
//                }
//            } else {
//                throw new Exception(String.format("http code %d", connection.getResponseCode()));
//            }
//        } catch (JsonSyntaxException | JsonIOException | IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (result.isThrowable()) {
//            try { throw result.getThrowable(); }
//            catch (ClassCastException e) { throw result.getThrowable(); }
//        }
//        return result;
//    }
//
//    //Auxiliary Constants, Attributes, and Methods
//    private static final String SERVER_HOST = "localhost";
//    private static final String URL_PREFIX = "http://" + SERVER_HOST + ":" + ServerCommunicator.getServerPortNumber();
//    private static final String HTTP_POST = "POST";
}
