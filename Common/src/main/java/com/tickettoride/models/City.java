package com.tickettoride.models;

public enum City { //TODO Move the model classes to common, kill the unused interfaces
    VANCOUVER,
    CALGARY,
    WINNIPEG,
    SAUL_ST_MARIE,
    MONTREAL,
    SEATTLE,
    HELENA,
    DULUTH,
    TORONTO,
    BOSTON,
    PORTLAND,
    SALT_LAKE_CITY,
    SAN_FRANCISCO,
    LOS_ANGELES,
    PHOENIX,
    LAS_VEGAS,
    SANTA_FE,
    EL_PASO,
    DALLAS,
    HOUSTON,
    NEW_ORLEANS,
    MIAMI,
    CHARLESTON,
    ATLANTA,
    LITTLE_ROCK,
    OKLAHOMA_CITY,
    DENVER,
    OMAHA,
    CHICAGO,
    PITTSBURGH,
    SAINT_LOUIS,
    KANSAS_CITY,
    NASHVILLE,
    RALEIGH,
    WASHINGTON,
    NEW_YORK;
    
    @Override
    public String toString(){
        String name=super.toString();
        return capitalizeSimple(name);
    }
    
    private String capitalizeSimple(String in){//much thanks to True Soft https://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string
        String out = in.toLowerCase();
        out = in.replace('_',' ');
        char[] chars = out.toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) {
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
