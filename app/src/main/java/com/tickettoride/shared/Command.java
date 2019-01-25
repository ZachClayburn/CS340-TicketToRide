package com.tickettoride.shared;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Command {
    private static Gson gson = new Gson();

    private String controllerName;
    private String methodName;
    private ArrayList<String> parametersAsJSONStrings;
    private ArrayList<String> parameterTypeNames;

    public Command(String controllerName, String methodName, List<Object> parameters) {
        ArrayList<Object> compactedParameters = ListHelper.compactArray(parameters);
        ArrayList<String> compactedJSONStringParameters = toJSONStringArrayList(compactedParameters);
        this.methodName = methodName;
        this.controllerName = controllerName;
        this.parametersAsJSONStrings = compactedJSONStringParameters;
        this.parameterTypeNames = parameterTypeNames(compactedParameters);
    }

    public Object execute() throws Throwable {
        Class targetClass = Class.forName(controllerName);
        Class[] parameterTypes = parameterTypes();
        Object[] parameters = parameters(parameterTypes);
        Method method = targetClass.getMethod(methodName, parameterTypes);
        Object result = method.invoke(targetClass, parameters);
        return result;
    }

    private ArrayList<String> parameterTypeNames(ArrayList<Object> parameters) {
        ArrayList<String> parameterTypeNames = new ArrayList<>();
        for(int i = 0; i < parameters.size(); i++) { parameterTypeNames.add(parameters.get(i).getClass().getName()); }
        return parameterTypeNames;
    }

    private Object[] parameters(Class[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = gson.fromJson(parametersAsJSONStrings.get(i), parameterTypes[i]);
        }
        return parameters;
    }

    private static ArrayList<String> toJSONStringArrayList(List<Object> parameters) {
        ArrayList<String> JSONStringParameters = new ArrayList<>();
        for(int i = 0; i < parameters.size(); i++) { JSONStringParameters.add(gson.toJson(parameters.get(i).toString())); }
        return JSONStringParameters;
    }

    private Class[] parameterTypes() throws ClassNotFoundException {
        Class[] parameterTypes = new Class[parameterTypeNames.size()];
        for (int i = 0; i < parameterTypeNames.size(); i++) { parameterTypes[i] = getClassFor(parameterTypeNames.get(i)); }
        return parameterTypes;
    }

    private static final Class<?> getClassFor(String className) throws ClassNotFoundException {
        Class<?> result;
        switch (className) {
            case "boolean" : result = boolean.class; break;
            case "byte"    : result = byte.class;    break;
            case "char"    : result = char.class;    break;
            case "double"  : result = double.class;  break;
            case "float"   : result = float.class;   break;
            case "int"     : result = int.class;     break;
            case "long"    : result = long.class;    break;
            case "short"   : result = short.class;   break;
            default: result = Class.forName(className);
        }
        return result;
    }
}
