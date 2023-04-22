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
@Table(name = "friend_request_list")

public class FriendRequestList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String sendToMobileNumber;
    private String sendFromMobileNumber;
    
    public FriendRequestList(String sendToMobileNumber, String sendFromMobileNumber) {
        this.sendToMobileNumber = sendToMobileNumber;
        this.sendFromMobileNumber = sendFromMobileNumber;
    }
    public FriendRequestList() {
    }

    
    
}
