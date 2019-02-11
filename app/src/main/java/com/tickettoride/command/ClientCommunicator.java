package com.tickettoride.command;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

import android.os.AsyncTask;
import android.util.Log;

import command.Command;
import command.Response;

public class ClientCommunicator {

    private WebSocketClient mWebSocketClient;
    protected static Gson gson = new Gson();
    private String websockethost = "10.0.2.2";
    private String port = "80";

    public static ClientCommunicator SINGLETON = new ClientCommunicator();

    private ClientCommunicator() {
    }

    public void connect() {
        try {
            if (mWebSocketClient == null) {
                mWebSocketClient = new WebSocketClient(new URI("ws://" + websockethost + ":" + port), new Draft_6455()) {

                    @Override
                    public void onMessage(String message) {
                        Log.i("ClientCommunicator", "Received Message From Server: " + message);
                        try {
                            Response response = gson.fromJson(message, Response.class);
                            if (response.hasCommand()) {
                                Command command = response.getCommand();
                                command.execute();
                            } else {
                                System.out.println(response.getMessage());
                            }
                        } catch (Throwable throwable) {
                            Log.e("ClientCommunicator", throwable.getMessage(), throwable);
                        }
                    }

                    @Override
                    public void onOpen(ServerHandshake handshake) {
                        Log.i("WebSocket", "Handshake Successful");
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.i("WebSocket", "Close");
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e("WebSocket", "Error");
                        Log.e("WebSocket", ex.getMessage());
                        Log.e("WebSocket", ex.getStackTrace().toString());
                    }
                };
            }
            if (mWebSocketClient.getConnection().getReadyState() != WebSocket.READYSTATE.OPEN) {
                mWebSocketClient.connect();
            }
        } catch (URISyntaxException e) {
            Log.e("ClientCommunicator", e.getMessage());
        }
    }
    
    public void send(Command command){
//        connect();
        try {
            String message=gson.toJson(command);
            mWebSocketClient.send(message);
            Log.i("ClientCommunicator", "Sent Command to Server");
        } catch (Exception t) {
            Log.e("ClientCommunicator", t.getMessage());
            Log.e("ClientCommunicator", t.getStackTrace().toString());
            throw t;
        }

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
