package command;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Command {
    private static Gson gson = new Gson();

    private String facadeName;
    private String methodName;
    private List<String> parametersAsJSONStrings;
    private List<String> parameterTypeNames;

    public Command(String facadeName, String methodName, List<Object> parameters) {
        List<Object> compactedParameters = ListHelper.compactArray(parameters);
        List<String> compactedJSONStringParameters = toJSONStringList(compactedParameters);
        this.methodName = methodName;
        this.facadeName = facadeName;
        this.parametersAsJSONStrings = compactedJSONStringParameters;
        this.parameterTypeNames = parameterTypeNames(compactedParameters);
    }
    
    //main use is putting the connid in on server side. 
    // DO NOT USE ON CLIENT
    public Command(String jsonMessage, UUID connid){
        Command temp=gson.fromJson(jsonMessage,Command.class);
        this.methodName=temp.methodName;
        this.facadeName=temp.facadeName;
        this.parametersAsJSONStrings=temp.parametersAsJSONStrings;
        this.parameterTypeNames=temp.parameterTypeNames;
        this.parametersAsJSONStrings.add(0,gson.toJson(connid.toString()));
        this.parameterTypeNames.add(0,connid.getClass().getName());
    }

    public Object execute() throws Throwable {
        Object result = null;
        
        try {
            Class targetClass = Class.forName(facadeName);
            Class[] parameterTypes = parameterTypes();
            Object[] parameters = parameters(parameterTypes);
            Method method = targetClass.getMethod(methodName, parameterTypes);
            result = method.invoke(targetClass, parameters);
            //System.err.println("got to system call");
        } catch (NoSuchMethodException | SecurityException e) {
            System.out.println("ERROR: Could not find the method " + methodName + ", or, there was a security error");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("Illegal accesss while trying to execute the method " + methodName);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Illegal argument while trying to find the method " + methodName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.err.println("Illegal accesss while trying to execute the method " + methodName);
            //e.printStackTrace();
            throw e.getCause();
        }

        return result;
    }

    private List<String> parameterTypeNames(List<Object> parameters) {
        List<String> parameterTypeNames = new ArrayList<>();
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

    private static List<String> toJSONStringList(List<Object> parameters) {
        List<String> JSONStringParameters = new ArrayList<>();
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
