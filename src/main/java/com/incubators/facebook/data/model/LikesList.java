package com.incubators.facebook.data.model;

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
@Table(name = "likes_list")
public class LikesList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;
    private Integer likedPostId;
    private String userLikedPostMobileNumber;

    public LikesList(Integer likedPostId, String userLikedPostMobileNumber) {
        this.likedPostId = likedPostId;
        this.userLikedPostMobileNumber = userLikedPostMobileNumber;
    }
    public LikesList() {
    }

    
}
