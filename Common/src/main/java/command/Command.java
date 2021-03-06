package command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tickettoride.models.Message;
import com.tickettoride.models.idtypes.PlayerID;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Command {
    
    protected static GsonBuilder gsonBuilder = new GsonBuilder();
    private static JsonSerializer<Message> serializer = new JsonSerializer<Message>() {
        @Override
        public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            
            jsonObject.addProperty("message",src.getMessage());
            jsonObject.addProperty("playerId", src.getPlayerID().toString());
            jsonObject.addProperty("timeString", src.getTime().toString());

            return jsonObject;
        }
    };

    private static JsonDeserializer<Message> desserializer = new JsonDeserializer<Message>() {
        @Override
        public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String time = jsonObject.get("timeString").getAsString();
            PlayerID playerID= PlayerID.fromString(jsonObject.get("playerId").getAsString());
            String message = jsonObject.get("message").getAsString();
            return new Message(message, playerID, time);
        }
    };
        
    protected static Gson gson = new Gson();

    protected String facadeName;
    protected String methodName;
    protected List<String> parametersAsJSONStrings;
    protected List<String> parameterTypeNames;
    protected final String GET_SINGLETON_METHOD_NAME = "getSingleton";

    public Command(String facadeName, String methodName, Object... parameters) {
        gsonBuilder.registerTypeAdapter(Instant.class,serializer);
        gsonBuilder.registerTypeAdapter(Instant.class,desserializer);
        List<Object> commandParameters = Arrays.asList(parameters);
        List<String> compactedJSONStringParameters = toJSONStringList(commandParameters);
        this.methodName = methodName;
        this.facadeName = facadeName;
        this.parametersAsJSONStrings = compactedJSONStringParameters;
        this.parameterTypeNames = parameterTypeNames(commandParameters);
    }
    
    //main use is putting the connid in on server side. 
    // DO NOT USE ON CLIENT
    public Command(String jsonMessage, UUID connid){
        gsonBuilder.registerTypeAdapter(Instant.class,serializer);
        gsonBuilder.registerTypeAdapter(Instant.class,desserializer);
        Command temp=gson.fromJson(jsonMessage,Command.class);
        this.methodName=temp.methodName;
        this.facadeName=temp.facadeName;
        this.parametersAsJSONStrings=temp.parametersAsJSONStrings;
        this.parameterTypeNames=temp.parameterTypeNames;
        this.parametersAsJSONStrings.add(0,gson.toJson(connid));
        this.parameterTypeNames.add(0,connid.getClass().getName());
    }

    public final void execute() throws Throwable {//for security purposes this should be final
        try {
            Class targetClass = null;
            try {
                targetClass = Class.forName("com.tickettoride.facades." + facadeName);
            }
            catch (ClassNotFoundException e) {}

            if (targetClass == null)//fixme: this should be in a try catch too in case someone tries any funny business 
                targetClass = Class.forName("com.tickettoride.controllers." + facadeName);

            Method getSingletonMethod = targetClass.getMethod(GET_SINGLETON_METHOD_NAME);
            Object singleton = getSingletonMethod.invoke(targetClass);
            Class[] parameterTypes = parameterTypes();
            Method method = targetClass.getMethod(methodName, parameterTypes);
            Object[] parameters = parameters(parameterTypes);
            method.invoke(singleton, parameters);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    protected List<String> parameterTypeNames(List<Object> parameters) {
        List<String> parameterTypeNames = new ArrayList<>();
        for(int i = 0; i < parameters.size(); i++) { parameterTypeNames.add(parameters.get(i).getClass().getName()); }
        return parameterTypeNames;
    }

    protected Object[] parameters(Class[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = gson.fromJson(parametersAsJSONStrings.get(i), parameterTypes[i]);
        }
        return parameters;
    }

    protected static List<String> toJSONStringList(List<Object> parameters) {
        List<String> JSONStringParameters = new ArrayList<>();
        for(int i = 0; i < parameters.size(); i++) { JSONStringParameters.add(gson.toJson(parameters.get(i))); }
        return JSONStringParameters;
    }

    protected Class[] parameterTypes() throws ClassNotFoundException {
        Class[] parameterTypes = new Class[parameterTypeNames.size()];
        for (int i = 0; i < parameterTypeNames.size(); i++) { parameterTypes[i] = getClassFor(parameterTypeNames.get(i)); }
        return parameterTypes;
    }

    protected static final Class<?> getClassFor(String className) throws ClassNotFoundException {
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
