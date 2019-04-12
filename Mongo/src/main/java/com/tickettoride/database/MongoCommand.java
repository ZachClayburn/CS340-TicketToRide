package com.tickettoride.database;

import com.mongodb.client.MongoCollection;

import org.bson.Document;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MongoCommand {
    private MongoCollection collection;
    private String methodName;
    private List<Class> parameterClasses;
    private List<Object> parameters;

    public MongoCommand(MongoCollection collection, String methodName, List<Object> parameters) {
        this.collection = collection;
        this.methodName = methodName;
        this.parameters = parameters;
        parameterClasses = new ArrayList<>();
        for (Object parameter: parameters) {
            parameterClasses.add(parameter.getClass());
        }
    }

    public void execute() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = collection.getClass().getMethod(methodName, (Class[]) parameterClasses.toArray());
        method.invoke(collection, parameters.toArray());
    }
}
