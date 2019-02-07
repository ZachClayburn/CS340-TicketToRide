package command;

import com.google.gson.Gson;

public class Response {
    private Gson gson;

    private String jsonCommand = null;
    private String message = null;

    public Response(String message) {
        this.message = message;
    }

    public Response(Command command) {
        this.jsonCommand = gson.toJson(command);
    }

    public Command getCommand() {
        return gson.fromJson(jsonCommand, Command.class);
    }

    public Boolean hasCommand() { return jsonCommand != null; }

//    //much thanks to araknoid on stack overflow for this method, I got close but couldn't quite get it
//    private Class<? extends Throwable> getExceptionClass(String className) throws ClassNotFoundException {
//        return (Class<? extends Throwable>) Class.forName(className);
//    }
}
