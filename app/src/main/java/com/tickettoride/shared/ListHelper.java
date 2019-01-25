package com.tickettoride.shared;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {
    public static ArrayList<Object> compactArray(List<Object> parameters) {
        ArrayList<Object> compactedParameters = new ArrayList<Object>();
        for(int i = 0; i < parameters.size(); i++) {
            Object object = parameters.get(i);
            if (!object.equals(null)) { compactedParameters.add(parameters.get(i)); }
        }
        return compactedParameters;
    }
}