package com.tickettoride.database;

import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MongoCommand {
    private MongoCollection collection;
    private String methodName;
    private Class[] parameterClasses;
    private List<Object> parameters;

    public MongoCommand(MongoCollection collection, String methodName, List<Object> parameters) {
        this.collection = collection;
        this.methodName = methodName;
        this.parameters = parameters;
        parameterClasses = new Class[parameters.size()];
        for (int x = 0; x < parameters.size(); x++) {
            Object parameter = parameters.get(x);
            parameterClasses[x] = parameter.getClass();
        }
    }

    public void execute() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (methodName == Database.INSERT_METHOD_NAME) { collection.insertOne(parameters.get(0)); return; }
        if (methodName == Database.DELETE_METHOD_NAME) { collection.deleteOne((Bson) parameters.get(0)); return; }
        if (methodName == Database.UPDATE_METHOD_NAME) { collection.updateOne((Bson) parameters.get(0), (Bson) parameters.get(1)); return; }
        if (methodName == Database.DELETE_MANY_METHOD_NAME) { collection.deleteMany((Bson) parameters.get(0)); }
        Method method = collection.getClass().getMethod(methodName, parameterClasses);
        method.invoke(collection, parameters.toArray());
    }
}
