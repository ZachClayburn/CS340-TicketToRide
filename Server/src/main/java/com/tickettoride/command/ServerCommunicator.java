package com.tickettoride.command;

import com.google.gson.Gson;
import com.tickettoride.database.DatabaseProvider;
import com.tickettoride.database.interfaces.IDatabase;
import com.tickettoride.models.idtypes.GameID;
import command.Command;
import command.Response;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;


//to start the server just call run()
public class ServerCommunicator extends WebSocketServer {

    private static Logger logger = LogManager.getLogger(ServerCommunicator.class.getName());

    static {
        var defaultPortNum = 8090;
        var envPortNum = System.getenv("PORT");
        SERVER_PORT_NUMBER = (envPortNum == null) ? defaultPortNum : Integer.parseInt(envPortNum);
    }


    private static final int SERVER_PORT_NUMBER;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    private static Gson gson = new Gson();
    
    private static ServerCommunicator INSTANCE=null;
    private static ConnectionManager connectionManager=null;

    private ServerCommunicator(int port) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
        connectionManager=new ConnectionManager();
    }

    public static ServerCommunicator getINSTANCE() {
        if(INSTANCE==null){
            try { INSTANCE = new ServerCommunicator(SERVER_PORT_NUMBER); }
            catch(UnknownHostException e){ logger.error("Could not start ServerCommunicator! ", e); }
        }
        return INSTANCE;
    }

    /** Called after an opening handshake has been performed and the given websocket is ready to be written on. */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake ){
        logger.info("Handshake Opened");
        connectionManager.addConnection(conn);
        sendToOne(conn,new Response("connection added"));
    }
    
    /**
     * Called after the websocket connection has been closed.
     *
     * @param code
     *            The codes can be looked up here: {@link CloseFrame}
     * @param reason
     *            Additional information string
     * @param remote
     *            Returns whether or not the closing of the connection was initiated by the remote host.
     **/
    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ){
        logger.info("Connection Closed");
        connectionManager.removeConnection(conn);
        //anything else we need to do to ensure proper removal of authtokens, etc
    }
    /**
     * Callback for string messages received from the remote host
     *
     * @see #onMessage(WebSocket, ByteBuffer)
     **/
    @Override
    public void onMessage( WebSocket conn, String message ){
        logger.info("Message Received");
        UUID connid = connectionManager.getConnectionId(conn);
        Command command = new Command(message,connid);
        try {
            command.execute();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
        }
    }
    /**
     * Called when errors occurs. If an error causes the websocket connection to fail {@link #onClose(WebSocket, int, String, boolean)} will be called additionally.<br>
     * This method will be called primarily because of IO or protocol errors.<br>
     * If the given exception is an RuntimeException that probably means that you encountered a bug.<br>
     *
     * @param conn
     *            Can be null if there error does not belong to one specific websocket. For example if the servers port could not be bound.
     **/
    @Override
    public void onError( WebSocket conn, Exception ex ){
        logger.info("Error Occurred");
        System.out.println(ex.getMessage());
    }
    /**
     * Callback for binary messages received from the remote host
     *
     * @see #onMessage(WebSocket, String)
     **/

    //these seven are the ones that will be called by the rest of the server
    
    //send response to a single connection (login, register, reconnect, etc)
    public void sendToOne(UUID connid, Response response){
        logger.info("Sending to One Connection ID: " + connid.toString());
        WebSocket conn=connectionManager.getConnection(connid);
        if (conn == null) { return; }
        sendToOne(conn,response);
    }
    
    //send response to the room that the specific connection is in
    public void sendToConnectionRoom(UUID connid, Response response) {
        logger.info("Sending to Room for Connection ID: " + connid.toString());
        List<WebSocket> conns = connectionManager.getRoomConnections(connid);
        sendToMany(conns,response);
    }
    
    //send response to the room with the roomid (
    public void sendToRoom(UUID roomid, Response response){
        logger.info("Sending to Room Id: " + roomid.toString());
        List<WebSocket> conns = connectionManager.getConnectionsOfRoom(roomid);
        sendToMany(conns,response);
    }
    
    //send updates to main lobby (new games created, game lobbies updated, etc)
    public void sendToMainLobby(Response response){
        logger.info("Sending to Room Main Lobby");
        List<WebSocket> conns = connectionManager.getConnectionsOfMainLobby();
        sendToMany(conns,response);
    }
    
    public void moveToRoom(UUID connid, GameID roomid){
        connectionManager.moveConnectionToRoom(connid,roomid);
    }
    
    public void moveToLogin(UUID connid){
        connectionManager.moveConnectionToLogin(connid);
    }
    
    public void moveToMainLobby(UUID connid){
        connectionManager.moveConnecionToMainLobby(connid);
    }
    
    //private helper methods
    
    private void sendToMany(List<WebSocket> connections, Response response){
        for(WebSocket conn:connections){
            sendToOne(conn,response);
        }
    }
    
    private void sendToOne(WebSocket conn, Response response){
        conn.send(gson.toJson(response));
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public void addRoom(GameID roomID) { connectionManager.addRoom(roomID); }
    

    private class ConnectionManager{
        
        private Map<UUID,WebSocket> connections;
        private RoomManager roomManagerInstance;
        
        public ConnectionManager(){
            connections=new HashMap<>();
            roomManagerInstance=new RoomManager();
        }

        public void addRoom(GameID roomID) { roomManagerInstance.addRoom(roomID); }

        public void addConnection(WebSocket conn){
            UUID connid = UUID.randomUUID();
            conn.setAttachment(connid);
            connections.put(connid,conn);
            roomManagerInstance.addConnectionToEntrance(connid);
        }
        
        public void removeConnection(WebSocket conn){
            UUID connid = conn.<UUID>getAttachment();
            connections.remove(connid);
            roomManagerInstance.removeConnection(connid);
        }
        
        public WebSocket getConnection(UUID connid){
            return connections.get(connid);
        }
        
        public UUID getConnectionId(WebSocket conn){
            return conn.<UUID>getAttachment();
        }
        
        public List<WebSocket> getConnectionsOfRoom(UUID roomid){
            List<UUID> connids = roomManagerInstance.getRoomConnectionIds(roomid);
            if(connids==null){
                return null;
            }
            List<WebSocket> conns=new LinkedList<>();
            for(UUID connid:connids){
                conns.add(connections.get(connid));
            }
            return conns;
        }
        
        public List<WebSocket> getRoomConnections(UUID connid){
            List<UUID> connids = roomManagerInstance.getConnectionRoomConnectionIds(connid);
            if(connids==null){
                return null;
            }
            List<WebSocket> conns=new LinkedList<>();
            for(UUID connId:connids){
                conns.add(connections.get(connId));
            }
            return conns;
        }


        public List<WebSocket> getConnectionsOfMainLobby(){
            List<UUID> connids = roomManagerInstance.getMainLobbyConnectionIds();
            if(connids==null){
                return null;
            }
            List<WebSocket> conns=new LinkedList<>();
            for(UUID connid:connids){
                conns.add(connections.get(connid));
            }
            return conns;
        }
        
        public void moveConnectionToLogin(UUID connid){
            roomManagerInstance.moveConnectionToEntrance(connid);
        }
        
        public void moveConnecionToMainLobby(UUID connid){
            roomManagerInstance.moveConnectionToMainLobby(connid);
        }
        
        public void moveConnectionToRoom(UUID connid, GameID roomid){
            roomManagerInstance.moveConnectionToRoom(connid,roomid);
        }
        
        
        //class specifically for managing which connection is in which room
        private class RoomManager{
            private Map<GameID,Room> rooms;
            private Map<UUID,GameID> connectionRoomMap;//stores the roomid the connection is in
            private GameID entrance;
            private GameID mainLobby;
            
            public RoomManager(){
                rooms=new HashMap<>();
                connectionRoomMap=new HashMap<>();
                entrance = GameID.randomUUID();
                mainLobby = GameID.randomUUID();
                addRoom(entrance);
                addRoom(mainLobby);
            }
            
            public void addRoom(GameID roomid){
                rooms.put(roomid,new Room());
            }
            
            public void removeRoom(UUID roomid){
                rooms.get(roomid).clearRoom();
                rooms.remove(roomid);
            }
            
            public void addConnectionToEntrance(UUID connid){
                addToRoom(connid,entrance);
            }
            
            public boolean moveConnectionToRoom(UUID connid,GameID roomid){
                if(connectionRoomMap.get(connid)==null){
                    return false; 
                }
                return moveToRoom(connid,roomid);
            }
            
            public boolean moveConnectionToEntrance(UUID connid){
                if(connectionRoomMap.get(connid)==null){
                    return false;
                }
                return moveToRoom(connid,entrance);
            }
            
            public boolean moveConnectionToMainLobby(UUID connid){
                if(connectionRoomMap.get(connid)==null){
                    return false;
                }
                return moveToRoom(connid,mainLobby);
            }
            
            public void removeConnection(UUID connid){
                GameID roomid=findConnectionRoomId(connid);
                if(roomid==null){
                    return;
                }
                removeFromRoom(connid,roomid);
            }
            
            public List<UUID> getConnectionRoomConnectionIds(UUID connid){
                GameID roomid=findConnectionRoomId(connid);
                if(roomid==null){
                    return null;
                }
                Room room = rooms.get(roomid);
                if(room==null){
                    return null;
                }
                return room.getConnections();
            }
            
            public List<UUID> getRoomConnectionIds(UUID roomid){
                Room room = rooms.get(roomid);
                if(room==null){
                    return null;
                }
                return room.getConnections();
            }

            public List<UUID> getMainLobbyConnectionIds(){
                Room room = rooms.get(mainLobby);
                if(room==null){
                    return null;
                }
                return room.getConnections();
            }
            
            private GameID findConnectionRoomId(UUID connid){
                return connectionRoomMap.get(connid);
            }
            
            private void addToRoom(UUID connid,GameID roomid){
                Room room=rooms.get(roomid);
                if(room==null){
                    return;
                }
                room.addConnectionId(connid);
                connectionRoomMap.put(connid,roomid);
            }
            
            private void removeFromRoom(UUID connid,GameID roomid){
                Room room=rooms.get(roomid);
                if(room==null){
                    return;
                }
                room.removeConnectionId(connid);
                connectionRoomMap.remove(connid);
            }
            
            private boolean moveToRoom(UUID connid,GameID newroomid){
                GameID oldroomid=findConnectionRoomId(connid);
                if(oldroomid==null || rooms.get(newroomid)==null){
                    return false;
                }
                Room oldroom = rooms.get(oldroomid);
                oldroom.removeConnectionId(connid);
                addToRoom(connid,newroomid);
                return true;
            }
            

            private class Room{
                private List<UUID> connections;

                public Room(){
                    connections=new ArrayList<>();
                }
                
                public void addConnectionId(UUID connid){
                    connections.add(connid);
                }
                
                public boolean removeConnectionId(UUID connid){
                    return connections.remove(connid);
                }

                public void clearRoom(){
                    connections.clear();
                }
                
                public List<UUID> getConnections() {
                    return connections;
                }


            }
        }
        
        
        
    }

    public static void main(String[] args) {
        try {
            processCLArgs(args);
            try (IDatabase IDatabase = DatabaseProvider.getDatabase()) {
                IDatabase.resetDatabase();
            }
            logger.info("Starting Server on port: " + ServerCommunicator.SERVER_PORT_NUMBER);
            ServerCommunicator.getINSTANCE().run();
        } catch (Throwable t) {
            logger.fatal("",t);
            System.exit(1);
        }
    }

    private static void processCLArgs(String[] args){

        try {
            if (args.length != 2)
                throw new IllegalArgumentException("Expected 2 command line arguments, got " + args.length + " instead!");
            String databasePluginJarFile = args[0];
            DatabaseProvider.intiDatabasePlugin(databasePluginJarFile);

            int delta = Integer.parseInt(args[1]);
            DatabaseProvider.getDatabase().setSyncInterval(delta);

        } catch (DatabaseException e) {
            logger.fatal("Fatal error occurred during database startup!", e);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("Usage: java SeverCommunicator <database plugin jar> <database sync interval>");
            System.exit(1);
        }

    }
}
