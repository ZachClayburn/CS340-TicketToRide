package com.tickettoride.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tickettoride.database.interfaces.ILineDAO;
import com.tickettoride.models.Line;
import com.tickettoride.models.idtypes.LineID;
import com.tickettoride.models.idtypes.RouteID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import exceptions.DatabaseException;

public class LineDAO extends Database.DataAccessObject implements ILineDAO {

    private List<Line> lineList;

    public LineDAO(MongoDatabase database) {
        super(database);
        collectionName = "lines";
        lineList = DataManager.getLineList();
    }


    @Override
    public void initializeData() {
        List<Line> lineList = DataManager.getLineList();
        lineList.addAll(allLines());
    }

    @Override
    public void createLine(Line line) throws DatabaseException {
        Document document = new Document();
        document.append("lineID", line.getLineID().toString());
        document.append("routeID", line.getRouteID().toString());
        document.append("startX", line.getStartX());
        document.append("endX", line.getEndX());
        document.append("startY", line.getStartY());
        document.append("endY", line.getEndY());
        MongoCollection collection = getCollection();
        List<Object> parameters = new ArrayList<>();
        parameters.add(document);
        MongoCommand mongoCommand = new MongoCommand(collection, Database.INSERT_METHOD_NAME, parameters);
        Database.addCommand(mongoCommand);
        lineList.add(line);
    }

    @Override
    public List<Line> getLines(RouteID routeID) throws DatabaseException {
        List<Line> lines = new ArrayList<>();
        for (Line line: lineList) {
            if (line.getRouteID().equals(routeID)) {
                lines.add(line);
            }
        }
        return lines;
    }

    public List<Line> allLines() {
        FindIterable<Document> iterLines = getCollection().find();
        Iterator iter = iterLines.iterator();
        List<Line> lines = new ArrayList<>();
        while(iter.hasNext()) {
            Document doc = (Document) iter.next();
            lines.add(buildLineFromDocument(doc));
        }
        return lines;
    }

    private Line buildLineFromDocument(Document doc) {
        LineID lineID = LineID.fromString(doc.getString("lineID"));
        RouteID routeID = RouteID.fromString(doc.getString("routeID"));
        int startX = doc.getInteger("startX");
        int startY = doc.getInteger("startY");
        int endX = doc.getInteger("endX");
        int endY = doc.getInteger("endY");
        return new Line(lineID, routeID, startX, startY, endX, endY);
    }

}
