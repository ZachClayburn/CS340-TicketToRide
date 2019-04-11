package com.tickettoride.database.interfaces;

import com.tickettoride.models.Line;
import com.tickettoride.models.idtypes.RouteID;
import exceptions.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ILineDAO {
    void createLine(Line line) throws DatabaseException;

    List<Line> getLines(RouteID routeID) throws DatabaseException;

    Line buildLineFromQueryResult(ResultSet result) throws SQLException;
}
