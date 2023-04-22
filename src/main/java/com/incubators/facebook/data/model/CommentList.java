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
@Table(name = "comment_list")
public class CommentList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer commentPostId;
    private String commentData;
    private String commentByUserMobileNumber;
    private Date date;
    private Time time;
    
    public CommentList(Integer commentPostId, String commentData, String commentByUserMobileNumber,
            Date date, Time time) {
        this.commentPostId = commentPostId;
        this.commentData = commentData;
        this.commentByUserMobileNumber = commentByUserMobileNumber;
        this.time = time;
        this.date = date;
    }
    public CommentList() {
    }

}
