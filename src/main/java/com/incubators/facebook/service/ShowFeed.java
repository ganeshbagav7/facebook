package com.incubators.facebook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.incubators.facebook.data.model.Post;
import com.incubators.facebook.data.model.User;
import com.incubators.facebook.data.repository.LikesListRepository;
import com.incubators.facebook.data.repository.PostRepository;
import com.incubators.facebook.data.repository.UserRepository;



@Service
public class ShowFeed {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserPostHelper userPostHelper;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  LikesListRepository likesListRepository;

    @Autowired
    private FeedPostHelper feedPostHelper;
    
    public ResponseEntity<Object> getFeed( Map<String , String > entity ){


        Map< String , List< Map < String , String >>>  feedResponse = new HashMap<>();


       List<Post> postlList = postRepository.findAllOrderByIdDesc();
    //    ShowFeed showFeed = new ShowFeed();

       feedResponse.put("posts", feedPostHelper.createPostMap(postlList , entity.get("mobileNumber")));
       feedResponse.put("friendRequests", userPostHelper.showFriendRequest(entity.get("mobileNumber")));
        feedResponse.put("userData",ShowFeed.userDataMap(userRepository.findById(entity.get("mobileNumber")).get()));
        
        return ResponseEntity.ok().body(feedResponse);

    }

    public static List< Map < String , String >> userDataMap( User user){
        List< Map < String , String >> userDataMap = new ArrayList<>();
        
        Map<String,String> userData = new HashMap<>();
        userData.put("message","user added");
        userData.put("firstName",user.getFirstName());
        userData.put("lastName",user.getLastName());
        userData.put("mobileNumber",user.getMobileNumber());

        userDataMap.add(userData);
        return userDataMap;
        
    }


}
