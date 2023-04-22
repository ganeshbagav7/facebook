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
@Table(name = "friend_list")
public class FriendList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userMobileNumber;
    private String friendMobileNumber;
    public FriendList( String userMobileNumber, String friendMobileNumber) {
        this.userMobileNumber = userMobileNumber;
        this.friendMobileNumber = friendMobileNumber;
    }
    public FriendList() {
    }
    

}
