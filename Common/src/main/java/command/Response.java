package command;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Response {
    private Gson gson;
    
    private String message="";
    private String type="";
    private Throwable error=null;
    /** Default constructor*/
    public Response(){}
    /** constructor that takes in the message to be stored */
    public Response(Object message){
        this.message=gson.toJson(message);
        this.type=message.getClass().getName();
        error=null;
    }

    public Response(Throwable throwable){
        error=throwable;
        this.type=throwable.getClass().getName();
        this.message = throwable.getMessage();
    }

    public Object getMessage() throws ClassNotFoundException{
        try {
            return gson.fromJson(message, Class.forName(type));
        }catch(Exception e){
            System.out.print(e.getMessage());
            throw e;
        }
    }

    public Throwable getException() throws ClassNotFoundException,NoSuchMethodException,InstantiationException,IllegalAccessException,InvocationTargetException{
        Class<? extends Throwable> eClass = getExceptionClass(type);
        Constructor<? extends Throwable> constructor=eClass.getConstructor(String.class);
        Throwable instance = constructor.newInstance((String)message);
        return instance;
    }

    public boolean isThrowable(){
        return error != null;
    }
    
    public String getType(){
        return type;
    }

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
