package com.incubators.facebook.util;

import java.sql.Date;
import java.sql.Time;

public class UserPost {
    
    String path ;
    String mobileNumber ;
    String postName ;
    Integer likeCount;
    Integer commentCount;
    String caption;
    Time time;
    Date date;
    String userFirstName;
    String userLastName;


    public UserPost(String path, String postName, Integer likeCount, Integer commentCount, String caption, Time time,
            Date date ,String userFirstName,  String userLastName) {
        this.path = path;
        this.postName = postName;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.caption = caption;
        this.time = time;
        this.date = date;
        this.userFirstName= userFirstName;
        this.userLastName= userLastName;

    }
    
}
