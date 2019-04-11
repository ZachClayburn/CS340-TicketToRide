package com.tickettoride.database;

import com.google.gson.Gson;
import com.tickettoride.database.interfaces.IPlayerStateDAO;
import exceptions.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Database implements com.tickettoride.database.interfaces.IDatabase {

    static {
        try {
            final String driver = "org.postgresql.Driver";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    DatabaseParameters parameters;
    private static DatabaseParameters staticParameters = null;

    private static Logger logger = LogManager.getLogger(Database.class.getName());

    protected Connection connection;
    private boolean doCommit = false;

    List<DataAccessObject> DAOs = new ArrayList<>();

    protected SessionDAO sessionDAO;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected PlayerDAO playerDAO;
    protected ChatDAO chatDAO;
    protected DestinationCardDAO destinationCardDAO;
    protected RouteDAO routeDAO;
    protected LineDAO lineDAO;
    protected HistoryDAO historyDAO;
    protected TrainCardDAO trainCardDAO;
    protected PlayerStateDAO playerStateDAO;

    /**
     * Creates a new SQLite database file at {@code location} and initialize the tables
     *
     * @throws DatabaseException If there is an error in creating the database and tables, or if the
     *                           database already exists
     */
    @Override
    public void resetDatabase() throws DatabaseException {

        try (var statement = connection.createStatement()) {

            //Language=PostgreSQL
            //String typeSql = "DROP TYPE IF EXISTS cardstate CASCADE";

            String sql = "DO $$ DECLARE" +
                    "    r RECORD;" +
                    "BEGIN" +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP" +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';" +
                    "    END LOOP;" +
                    "END $$;";
            sql += DAOs.stream().map(DataAccessObject::getTableCreateString).collect(Collectors.joining());

            //statement.executeUpdate(typeSql);
            statement.executeUpdate(sql);

            commit();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not create the database!", e);
        }

    }

    /**
     * Constructs a new Database object and connects to the database file.
     */
    public Database() throws DatabaseException {

        parameters = getParameters();

        try {
            connection = parameters.connectWithParameters();
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException(e);
        }

        userDAO = new UserDAO(connection);
        DAOs.add(userDAO);
        sessionDAO = new SessionDAO(connection);
        DAOs.add(sessionDAO);
        gameDAO = new GameDAO(connection);
        DAOs.add(gameDAO);
        playerDAO = new PlayerDAO(connection);
        DAOs.add(playerDAO);
        chatDAO=new ChatDAO(connection);
        DAOs.add(chatDAO);
        destinationCardDAO = new DestinationCardDAO(connection);
        DAOs.add(destinationCardDAO);
        routeDAO = new RouteDAO(connection);
        DAOs.add(routeDAO);
        lineDAO = new LineDAO(connection);
        DAOs.add(lineDAO);
        historyDAO = new HistoryDAO(connection);
        DAOs.add(historyDAO);
        trainCardDAO = new TrainCardDAO(connection);
        DAOs.add(trainCardDAO);
        playerStateDAO = new PlayerStateDAO(connection);
        DAOs.add(playerStateDAO);
    }

    private DatabaseParameters getParameters(){
        if (staticParameters != null) return staticParameters;
        var envParams = getParametersFromEnv();
        if (envParams!= null) return envParams;
        return extractParametersFromConfigFile();
    }

    private DatabaseParameters extractParametersFromConfigFile() {
        Gson gson = new Gson();
        URL fileurl = Database.class.getClassLoader().getResource("databaseParams.json");
        InputStream in = null;
        try {
            in = fileurl.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Reader reader = new InputStreamReader(in);

        return gson.fromJson(reader, DatabaseJSONParameters.class);
    }

    private DatabaseParameters getParametersFromEnv(){

        var dbURL = System.getenv("JDBC_DATABASE_URL");
        if (dbURL == null) {
            return null;
        }

        return new DatabaseEnvironmentParameters(dbURL);
    }

    /**
     * Closes the connection to the database, closing the connection
     *
     * @throws DatabaseException If an error occurs in the adding process
     */
    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null) {
                if (doCommit) {
                    connection.commit();
                } else {
                    logger.warn("Aborting all transactions!");
                    connection.rollback();
                }
                connection.close();
                connection = null;
                DAOs.forEach(dataAccessObject -> dataAccessObject.connection = null);
            }
        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException(e);
        }
    }

    @Override
    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public GameDAO getGameDAO() {
        return gameDAO;
    }

    @Override
    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    @Override
    public ChatDAO getChatDAO() {
        return chatDAO;
    }

    @Override
    public DestinationCardDAO getDestinationCardDAO() {
        return destinationCardDAO;
    }

    @Override
    public RouteDAO getRouteDAO() { return routeDAO; }

    @Override
    public LineDAO getLineDAO() { return  lineDAO; }

    @Override
    public HistoryDAO getHistoryDAO() {
        return historyDAO;
    }

    @Override
    public TrainCardDAO getTrainCardDAO() {return trainCardDAO;}

    @Override
    public PlayerStateDAO getPlayerStateDAO() { return  playerStateDAO; }

    @Override
    public void commit() {
        doCommit = true;
    }

    

    static abstract class DataAccessObject {

        protected Connection connection;

        DataAccessObject(Connection connection) {
            this.connection = connection;
        }

        abstract String getTableCreateString();
    }


    interface  DatabaseParameters {
        Connection connectWithParameters() throws SQLException;
    }

    static class DatabaseEnvironmentParameters implements DatabaseParameters {

        private String serverURL;

        public DatabaseEnvironmentParameters(String serverURL) {

            this.serverURL = serverURL;
        }

        @Override
        public Connection connectWithParameters() throws SQLException {
            return DriverManager.getConnection(serverURL);
        }

    }

    static class DatabaseJSONParameters implements DatabaseParameters{

        private String URL;
        private Integer port;
        private String databaseName;
        private String username;
        private String password;

        public String getServerAddress() {
            return"jdbc:postgresql://" + URL + ":" + port + "/" + databaseName;
        }

        public String getServerUserName() {
            return username;
        }

        public String getServerPassword() {
            return password;
        }

        @Override
        public String toString() {
            return "DatabaseJSONParameters{" +
                    "URL='" + URL + '\'' +
                    ", port=" + port +
                    ", databaseName='" + databaseName + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

        @Override
        public Connection connectWithParameters() throws SQLException {
            return DriverManager.getConnection(getServerAddress(), getServerUserName(), getServerPassword());
        }
    }

    public static void main(String[] args) throws DatabaseException {
        try (var db = new Database()) {
            db.resetDatabase();
        }
    }

}
