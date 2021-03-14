package com.example.flashcardz;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("Set")

public class Set extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_SET_NAME = "setName";

    // empty constructor needed by the parceler library
//    public Set(){}

    public void setSetName(String setName){
        put(KEY_SET_NAME, setName);
    }

    public String getSetName(){
        return getString(KEY_SET_NAME);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
}
