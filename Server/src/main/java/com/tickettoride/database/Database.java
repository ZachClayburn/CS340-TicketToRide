package com.tickettoride.database;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.security.PrivilegedActionException;
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

    List<DataAccessObject> DAOs = new ArrayList<>();

    protected SessionDAO sessionDAO;
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected PlayerDAO playerDAO;

    /**
     * Creates a new SQLite database file at {@code location} and initialize the tables
     * @throws DatabaseException If there is an error in creating the database and tables, or if the
     * database already exists
     */
    public void createDatabase() throws DatabaseException {

        try (var statement = connection.createStatement()){

            //Language=PostgreSQL
            String sql = "DO $$ DECLARE" +
                    "    r RECORD;" +
                    "BEGIN" +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP" +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';" +
                    "    END LOOP;" +
                    "END $$;";
            sql += DAOs.stream().map(DataAccessObject::getTableCreateString).collect(Collectors.joining());

            statement.executeUpdate(sql);

            connection.commit();

        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Could not create the database!", e);
        }

    }

    /**
     * Constructs a new Database object and connects to the database file.
     */
    public Database() throws DatabaseException{

        Gson gson = new Gson();
        ClassLoader cl = this.getClass().getClassLoader();
        URL fileurl = Database.class.getClassLoader().getResource("databaseParams.json");
        InputStream in = null;
        try {
            in = fileurl.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader reader = null;
        if (in != null) {
            reader = new InputStreamReader(in);
        } else {
            System.err.println("No databaseParams.json present!");
            System.exit(1);
        }

        parameters =  gson.fromJson(reader, DatabaseParameters.class);


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
    }

    /**
     * Closes the connection to the database, closing the connection
     * @throws DatabaseException If an error occurs in the adding process
     */
    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null) {
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

    public GameDAO getGameDAO() {return gameDAO;}

    public PlayerDAO getPlayerDAO() {return playerDAO;}

    public void commit() throws DatabaseException {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.catching(e);
            throw new DatabaseException("Can not commit transaction!", e);
        }
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

        public String getServerAddress(){
            return URL + ":" + port + "/" + databaseName;
        }

        public String getServerUserName() {
            return username;
        }

        public String getServerPassword() {
            return password;
        }

    }

    public static void main(String[] args) throws DatabaseException {
        try (var db = new Database()) {
            db.createDatabase();
        }
    }

    /**
     * A custom exception for handling all errors relating to the database
     */
    public static class DatabaseException extends Exception {
        /**
         * Constructs a new exception with {@code null} as its detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         */
        public DatabaseException() {
            super();
        }

        /**
         * Constructs a new exception with the specified detail message.  The
         * cause is not initialized, and may subsequently be initialized by
         * a call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public DatabaseException(String message) {
            super(message);
        }

        /**
         * Constructs a new exception with the specified detail message and
         * cause.  <p>Note that the detail message associated with
         * {@code cause} is <i>not</i> automatically incorporated in
         * this exception's detail message.
         *
         * @param message the detail message (which is saved for later retrieval
         *                by the {@link #getMessage()} method).
         * @param cause   the cause (which is saved for later retrieval by the
         *                {@link #getCause()} method).  (A {@code null} value is
         *                permitted, and indicates that the cause is nonexistent or
         *                unknown.)
         * @since 1.4
         */
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new exception with the specified cause and a detail
         * message of {@code (cause==null ? null : cause.toString())} (which
         * typically contains the class and detail message of {@code cause}).
         * This constructor is useful for exceptions that are little more than
         * wrappers for other throwables (for example, {@link
         * PrivilegedActionException}).
         *
         * @param cause the cause (which is saved for later retrieval by the
         *              {@link #getCause()} method).  (A {@code null} value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         * @since 1.4
         */
        public DatabaseException(Throwable cause) {
            super(cause);
        }
    }

}
