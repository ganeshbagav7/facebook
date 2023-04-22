package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.incubators.facebook.data.model.FriendRequestList;

import jakarta.transaction.Transactional;

public interface FriendRequestListRepository extends JpaRepository<FriendRequestList , Integer>{
    
    @Query(value = "SELECT * FROM friend_request_list frl WHERE frl.send_to_mobile_number = :sendToMobileNumber",nativeQuery = true)
    public List<FriendRequestList> findFriendRequests(String sendToMobileNumber );


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM friend_request_list frl WHERE frl.send_to_mobile_number = :sendToMob AND frl.send_from_mobile_number = :sendFromMob ",nativeQuery = true)
    public void deleteRequest(String sendToMob , String sendFromMob );

    @Query(value = "SELECT * FROM friend_request_list frl WHERE frl.send_to_mobile_number = :sendToMob AND frl.send_from_mobile_number = :sendFromMob ",nativeQuery = true)
    public FriendRequestList checkRequestList(String sendToMob , String sendFromMob );

}
