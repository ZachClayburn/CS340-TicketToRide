package database;

import java.io.Closeable;
import java.io.File;
import java.security.PrivilegedActionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database implements AutoCloseable{

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String databaseFile = "src/main/resources/ticketToRide.DB";

    protected Connection connection;

    protected static void setDatabaseFile(String databaseFile) {
        Database.databaseFile = databaseFile;
    }

    /**
     * Creates a new SQLite database file at {@code location} and initialize the tables
     * @param location The path where you want the database file to be created
     * @throws DatabaseException If there is an error in creating the database and tables, or if the
     * database already exists
     */
    public static void createDatabase(String location) throws DatabaseException {
        final String url = "jdbc:sqlite:" + location;

        if (new File(location).exists()) {
            throw new DatabaseException("Database at " + location + " already exists!");
        }

        try (Connection con = DriverManager.getConnection(url)){
            //Database created!
        } catch (SQLException e) {
            throw new DatabaseException("Could not create the database!", e);
        }

    }

    /**
     * Constructs a new Database object and connects to the database file.
     */
    public Database() throws DatabaseException{

        final String url = "jdbc:sqlite:" + databaseFile;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        //FIXME Create DAOs here
    }

    /**
     * Closes the connection to the database, closing the conne
     * @throws DatabaseException
     */
    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                //FIXME Set all DAOs' ref to connection to null as well
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
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
