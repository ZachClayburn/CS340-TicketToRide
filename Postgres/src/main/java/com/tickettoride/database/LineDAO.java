package com.tickettoride.database;

import com.tickettoride.database.interfaces.ILineDAO;
import com.tickettoride.models.Line;
import com.tickettoride.models.idtypes.LineID;
import com.tickettoride.models.idtypes.RouteID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import exceptions.DatabaseException;

public class LineDAO extends Database.DataAccessObject implements ILineDAO {
    private final String tableCreateString =
            // language=PostgreSQL
            "CREATE TABLE Lines" +
                    "(" +
                    "lineID TEXT PRIMARY KEY NOT NULL CHECK ( length(lineID) > 0 )," +
                    "routeID TEXT NOT NULL," +
                    "startX NUMERIC NOT NULL," +
                    "endX NUMERIC NOT NULL," +
                    "startY NUMERIC NOT NULL," +
                    "endY NUMERIC NOT NULL," +
                    "FOREIGN KEY (routeID) REFERENCES routes(routeID)" +
                    ");";

    public LineDAO(Connection connection) {
        super(connection);
    }

    private Logger logger = LogManager.getLogger(LineDAO.class.getName());

    @Override
    String getTableCreateString() {
        return tableCreateString;
    }

    @Override
    public void createLine(Line line) throws DatabaseException {
        final String sql = "INSERT INTO Lines (lineID, routeID, startX, endX, startY, endY) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, line.getLineID().toString());
            statement.setString(2, line.getRouteID().toString());
            statement.setInt(3, line.getStartX());
            statement.setInt(4, line.getEndX());
            statement.setInt(5, line.getStartY());
            statement.setInt(6, line.getEndY());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not add new Line to Database!", e);
        }
    }

    @Override
    public List<Line> getLines(RouteID routeID) throws DatabaseException {
        List<Line> lines = new ArrayList<>();
        String sql = "Select * from Lines WHERE routeID = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, routeID.toString());
            var results = statement.executeQuery();
            while (results.next()) { lines.add(buildLineFromQueryResult(results)); }
        } catch (SQLException e) { throw new DatabaseException(("Could Not Retrieve Lines"), e); }
        return lines;
    }

    public Line buildLineFromQueryResult(ResultSet result) throws SQLException {
        RouteID routeID = RouteID.fromString(result.getString("routeID"));
        LineID lineID = LineID.fromString(result.getString("lineID"));
        int startX = result.getInt("startX");
        int startY = result.getInt("startY");
        int endX = result.getInt("endX");
        int endY = result.getInt("endY");
        return new Line(lineID, routeID, startX, startY, endX, endY);
    }
}
