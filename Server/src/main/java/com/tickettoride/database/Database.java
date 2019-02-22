package com.tickettoride.database;

import com.google.gson.Gson;
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

public class Database implements AutoCloseable {

    static {
        try {
            final String driver = "org.postgresql.Driver";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected DatabaseParameters parameters;

    private static Logger logger = LogManager.getLogger(Database.class.getName());

    protected Connection connection;
    private boolean doCommit = false;

    List<DataAccessObject> DAOs = new ArrayList<>();

    protected SessionDAO sessionDAO;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected PlayerDAO playerDAO;
    protected DestinationCardDAO destinationCardDAO;

    /**
     * Creates a new SQLite database file at {@code location} and initialize the tables
     *
     * @throws DatabaseException If there is an error in creating the database and tables, or if the
     *                           database already exists
     */
    public void resetDatabase() throws DatabaseException {

        System.out.println("Creating database " + parameters.databaseName);

        try (var statement = connection.createStatement()) {

            //Language=PostgreSQL
            String sql = "DO $$ DECLARE" +
                    "    r RECORD;" +
                    "BEGIN" +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP" +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';" +
                    "    END LOOP;" +
                    "END $$;" +
                    "DROP TYPE IF EXISTS cardState;";
            sql += DAOs.stream().map(DataAccessObject::getTableCreateString).collect(Collectors.joining());

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

        Gson gson = new Gson();
        var cl = Database.class.getClassLoader();
        URL fileurl = Database.class.getClassLoader().getResource("databaseParams.json");
        InputStream in = null;
        try {
            in = fileurl.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Reader reader = new InputStreamReader(in);

        parameters = gson.fromJson(reader, DatabaseParameters.class);

        final String url = "jdbc:postgresql://" + parameters.getServerAddress();

        try {
            connection = DriverManager.getConnection(url,
                    parameters.getServerUserName(), parameters.getServerPassword());
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
        destinationCardDAO = new DestinationCardDAO(connection);
        DAOs.add(destinationCardDAO);
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

    public SessionDAO getSessionDAO() {
        return sessionDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }

    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public DestinationCardDAO getDestinationCardDAO() {
        return destinationCardDAO;
    }

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

    static class DatabaseParameters {

        private String URL;
        private Integer port;
        private String databaseName;
        private String username;
        private String password;

        public String getServerAddress() {
            return URL + ":" + port + "/" + databaseName;
        }

        public String getServerUserName() {
            return username;
        }

        public String getServerPassword() {
            return password;
        }

        @Override
        public String toString() {
            return "DatabaseParameters{" +
                    "URL='" + URL + '\'' +
                    ", port=" + port +
                    ", databaseName='" + databaseName + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws DatabaseException {
        try (var db = new Database()) {
            db.resetDatabase();
        }
    }

}
