package com.tickettoride.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.ILineDAO;
import com.tickettoride.models.Line;
import com.tickettoride.models.idtypes.RouteID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import exceptions.DatabaseException;

public class LineDAO extends Database.DataAccessObject implements ILineDAO {

    public LineDAO(MongoDatabase database) {
        super(database);
        collectionName = "lines";
    }

    @Override
    public MongoCollection getCollection() {
        return null;
    }

    @Override
    public void createCollection() {

    }

    @Override
    public void initializeData() {

    }

    @Override
    public void createLine(Line line) throws DatabaseException {

    }

    @Override
    public List<Line> getLines(RouteID routeID) throws DatabaseException {
        return null;
    }

    @Override
    public Line buildLineFromQueryResult(ResultSet result) throws SQLException {
        return null;
    }
}
