package command;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Response {
    private Gson gson;
    
    private String gsonCommand = null;
    private Throwable error = null;
    private String type = null;
    private String message = null;

    /** Default constructor*/
    public Response() {}

    public Response (String message) {
        this.message = message;
    }

    /** constructor that takes in the message to be stored */
    public Response(Command command) { this.gsonCommand = gson.toJson(command); }

    public Response (Throwable throwable) {
        error = throwable;
        this.type = throwable.getClass().getName();
        this.message = throwable.getMessage();
    }

    public Command getCommand() { return gson.fromJson(gsonCommand, Command.class); }

    public Throwable getException() throws ClassNotFoundException,NoSuchMethodException,InstantiationException,IllegalAccessException,InvocationTargetException{
        Class<? extends Throwable> eClass = getExceptionClass(type);
        Constructor<? extends Throwable> constructor=eClass.getConstructor(String.class);
        Throwable instance = constructor.newInstance((String)message);
        return instance;
    }

    public boolean isThrowable() { return error != null; }

    public boolean isCommand() { return gsonCommand != null; }

    //much thanks to araknoid on stack overflow for this method, I got close but couldn't quite get it
    private Class<? extends Throwable> getExceptionClass(String className) throws ClassNotFoundException {
        return (Class<? extends Throwable>) Class.forName(className);
    }
    
//    //Domain
//    private Object returnValue;
//    private Throwable throwable;
//    private String throwableType;
//
//    //Constructors
//    public Response(Object returnValue) {
//        this.returnValue = returnValue;
//        throwable = null;
//
//    }
//
//    public Response(Throwable throwable) {
//        returnValue = null;
//        this.throwable = throwable;
//        this.throwableType = throwable.getClass().getName();
//    }
//
//    //Queries
//    public boolean isThrowable() {return throwable != null;}
//    public Object getReturnValue() {return returnValue;}
//    public Throwable getThrowable() { return throwable; }
//    public Object getSpecificThrowable() {
//        try {
//            Class throwClass = Class.forName(throwableType);
//            Constructor constructor = throwClass.getConstructor();
//            Object exception = constructor.newInstance(throwable.getMessage());
//            return exception;
//        }
//        catch (IllegalAccessException e) { return  throwable; }
//        catch (ClassNotFoundException e) { return throwable; }
//        catch (NoSuchMethodException e) { return throwable; }
//        catch (InstantiationException e) { return  throwable; }
//        catch (InvocationTargetException e) { return  throwable; }
//    }
}
