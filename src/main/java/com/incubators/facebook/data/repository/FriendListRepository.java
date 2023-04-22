package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.incubators.facebook.data.model.FriendList;

import jakarta.transaction.Transactional;

public interface FriendListRepository extends JpaRepository<FriendList,Integer>{
    
    @Query(value = "SELECT * FROM friend_list fl WHERE fl.user_mobile_number = :userMobileNumber AND  fl.friend_mobile_number = :friendMobileNumber", nativeQuery = true )
    public FriendList findByUserMobileNumberAndfriendMobileNumber(String userMobileNumber , String friendMobileNumber);



    public List<FriendList> findByUserMobileNumber(String userMobileNumber );
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM friend_list fl WHERE  fl.user_mobile_number = :userMobileNumber AND  fl.friend_mobile_number = :friendMobileNumber ", nativeQuery = true )
    void deleteFriend(String userMobileNumber , String friendMobileNumber );
    
}