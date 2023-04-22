package com.incubators.facebook.data.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user_info")
public class User {

    @Id
    private String mobileNumber;
    private String firstName;
    private String lastName;
    private String password;
    private String securityQuestion;
    private String securityAnswer;
    private Date dob;
    private String gender;
    private Integer postCount=0;
    private Integer friendsCount=0;
    
    public User(String mobileNumber, String firstName, String lastName, String password, String securityQuestion,
            String securityAnswer, Date dob, String gender, Integer postCount, Integer friendsCount) {
        this.mobileNumber = mobileNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.dob = dob;
        this.gender = gender;
        this.postCount = postCount;
        this.friendsCount = friendsCount;
    }

    public User() {
    }


}
