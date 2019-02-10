package command;

import com.google.gson.Gson;

public class Response {
    private String jsonCommand = null;
    private String message = null;

    public Response(String message) {
        this.message = message;
    }

    public Response(Command command) {
        Gson gson = new Gson();
        this.jsonCommand = gson.toJson(command);
    }

    public Command getCommand() {
        Gson gson = new Gson();
        return gson.fromJson(jsonCommand, Command.class);
    }

    public Boolean hasCommand() { return jsonCommand != null; }

    public String getMessage() { return message; }

//    //much thanks to araknoid on stack overflow for this method, I got close but couldn't quite get it
//    private Class<? extends Throwable> getExceptionClass(String className) throws ClassNotFoundException {
//        return (Class<? extends Throwable>) Class.forName(className);
//    }
}
