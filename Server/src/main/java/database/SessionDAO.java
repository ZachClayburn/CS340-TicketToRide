package database;

import java.sql.Connection;

public class SessionDAO extends Database.DataAccessObject {
    public SessionDAO(Connection connection) {
        super(connection);
    }
}
