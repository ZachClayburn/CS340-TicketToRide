package com.tickettoride.database;

import java.sql.Connection;

public class SessionDAO extends Database.DataAccessObject {

    private final String tableCreateString =
            // language=SQLite
            "DROP TABLE IF EXISTS Sessions;" +
            "Create TABLE Sessions(" +
            "sessionID TEXT PRIMARY KEY NOT NULL ," +
            "userID TEXT NOT NULL," +
            "FOREIGN KEY (userID) REFERENCES Users(userID)" +
            ");" +
            "create table Users" +
            "(" +
            "userID TEXT PRIMARY KEY NOT NULL," +
            "userName TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL" +
            ");";


    public SessionDAO(Connection connection) {
        super(connection);
    }

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }
}
