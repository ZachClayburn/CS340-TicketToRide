package command;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {
    public static List<Object> compactArray(List<Object> parameters) {
        List<Object> compactedParameters = new ArrayList<>();
        for(int i = 0; i < parameters.size(); i++) {
            Object object = parameters.get(i);
            if (!object.equals(null)) { compactedParameters.add(parameters.get(i)); }
        }
        return compactedParameters;
    }
}