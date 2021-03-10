package com.example.flashcardz;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Flashcard")
public class Flashcard extends ParseObject {
    public static final String KEY_FRONT_TEXT = "frontText";
    public static final String KEY_BACK_TEXT = "backText";
    public static final String KEY_SET = "setObjectId";
//    public static final String KEY_USER = "user";

    public String getFrontText(){
        return getString(KEY_FRONT_TEXT);
    }

    public void setFrontText(String frontText){
        put(KEY_FRONT_TEXT, frontText);
    }

    public String getBackText(){
        return getString(KEY_BACK_TEXT);
    }

    public void setBackText(String backText){
        put(KEY_BACK_TEXT, backText);
    }

    public String getSetObjectId(){
        return getString(KEY_SET);
    }

    public void setSetObjectId(String id){
        put(KEY_SET, id);
    }
}
