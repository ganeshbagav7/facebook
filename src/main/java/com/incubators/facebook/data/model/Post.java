package com.incubators.facebook.data.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "post_info")
public class Post { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String postUserMobileNumber;
    private String firstName;
    private String lastName;
    private Integer postNumber;
    private String caption;
    private Integer likesCount=0;
    private Integer commentsCount=0;
    private Date date;
    private Time time;
    private String postName;
    
    
    public Post(String postUserMobileNumber, Integer postNumber, String caption, Integer likesCount,
            Integer commentsCount, Date date, Time time , String postName ,  String firstName , String lastName ) {
        
        this.postUserMobileNumber = postUserMobileNumber;
        this.postNumber = postNumber;
        this.caption = caption;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.time = time;
        this.date = date;
        this.postName=postName;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public Post() {
    }
    
}
