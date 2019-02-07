package com.tickettoride.database;

import java.sql.Connection;

public class SessionDAO extends Database.DataAccessObject {

    private final String tableCreateString =
            // language=PostgreSQL
            "Create TABLE Sessions(" +
            "sessionID TEXT PRIMARY KEY NOT NULL ," +
            "userID TEXT NOT NULL," +
            "FOREIGN KEY (userID) REFERENCES Users(userID)" +
            ");";


    public SessionDAO(Connection connection) {
        super(connection);
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }
}
