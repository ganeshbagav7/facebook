package com.incubators.facebook.util;

import java.sql.Date;

public class UserData {
    String mobileNumber;
    String firstName;
    String lastName;
    Date dob;
    String gender;
    Integer postCount=0;
    Integer friendsCount=0;
    
    public UserData(String mobileNumber, String firstName, String lastName, Date dob, String gender, Integer postCount,
            Integer friendsCount) {
        this.mobileNumber = mobileNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.postCount = postCount;
        this.friendsCount = friendsCount;
    }
    
}
