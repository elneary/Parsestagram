package com.example.emma_instagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;




@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_USER = "user";



    public ParseUser getUser() {
        return super.getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public String getTimestamp(){
        Date date = getCreatedAt();
        DateFormat df = new SimpleDateFormat("h:ma |  MM-dd-yyyy");
        String reportDate = df.format(date);
        return reportDate;
    }

    public void setImage(ParseFile imageFile){
        put(KEY_IMAGE, imageFile);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }


}
