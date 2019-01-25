package com.tickettoride.shared;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Response {
    //Domain
    private Object returnValue;
    private Throwable throwable;
    private String throwableType;

    //Constructors
    public Response(Object returnValue) {
        this.returnValue = returnValue;
        throwable = null;

    }

    public Response(Throwable throwable) {
        returnValue = null;
        this.throwable = throwable;
        this.throwableType = throwable.getClass().getName();
    }

    //Queries
    public boolean isThrowable() {return throwable != null;}
    public Object getReturnValue() {return returnValue;}
    public Throwable getThrowable() { return throwable; }
    public Object getSpecificThrowable() {
        try {
            Class throwClass = Class.forName(throwableType);
            Constructor constructor = throwClass.getConstructor();
            Object exception = constructor.newInstance(throwable.getMessage());
            return exception;
        }
        catch (IllegalAccessException e) { return  throwable; }
        catch (ClassNotFoundException e) { return throwable; }
        catch (NoSuchMethodException e) { return throwable; }
        catch (InstantiationException e) { return  throwable; }
        catch (InvocationTargetException e) { return  throwable; }
    }
}
