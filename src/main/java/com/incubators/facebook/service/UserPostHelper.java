package com.incubators.facebook.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.incubators.facebook.data.model.FriendList;
import com.incubators.facebook.data.model.FriendRequestList;
import com.incubators.facebook.data.model.Post;
import com.incubators.facebook.data.model.User;
import com.incubators.facebook.data.repository.FriendListRepository;
import com.incubators.facebook.data.repository.FriendRequestListRepository;
import com.incubators.facebook.data.repository.PostRepository;
import com.incubators.facebook.data.repository.UserRepository;

@Service
public class UserPostHelper {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestListRepository friendRequestListRepository;

    @Autowired
    private FriendListRepository friendListRepository;

    private static String filePath = "";

    static {
        try {

            filePath = new ClassPathResource("static/images").getFile().getAbsolutePath();

        } catch (IOException io) {

            System.out.println(io.getMessage());

        }
    }

    public Map<String,Map<String,String>>  getUserPosts(String mobileNumber) {

        
        Map<String,Map<String,String>> userPostList = new HashMap<>(); 

        List<Post> postList = postRepository.findAllUserPosts(mobileNumber);

        ListIterator<Post> listItr = postList.listIterator();

        while (listItr.hasNext()) {
    
            Post post = listItr.next();
            Map<String,String> postData = new HashMap<>(); 
            postData.put("postId", post.getId().toString());
            // postData.put("path", Paths.get(filePath + "/post/" + post.getPostName()).toString());
            postData.put("mobileNumber",mobileNumber );
            postData.put("postName",post.getPostName() );
            postData.put("likeCount",post.getLikesCount().toString() );
            postData.put("commentCount", post.getCommentsCount().toString() );
            postData.put("caption",post.getCaption());
            postData.put("time", post.getTime().toString() );
            postData.put("date",post.getDate().toString());
            postData.put("userFirstName",userRepository.findById(mobileNumber).get().getFirstName());
            postData.put("userLastName",userRepository.findById(mobileNumber).get().getLastName());


            userPostList.put(post.getPostNumber().toString(),postData);
        }

        return userPostList;

    }

    public List<Map<String, String>> showFriendRequest(String mobileNumber) {

        List<FriendRequestList> frl = friendRequestListRepository.findFriendRequests(mobileNumber);

        ListIterator<FriendRequestList> listItr = frl.listIterator();

        List<Map<String, String>> requestList = new ArrayList<>();

        while (listItr.hasNext()) {

            FriendRequestList friendRequestList = listItr.next();
            String sendFrom = friendRequestList.getSendFromMobileNumber();
            User user = userRepository.findById(sendFrom).get();
            Map<String, String> request = new HashMap<>();
            request.put("name", user.getFirstName() + " " + user.getLastName());
            request.put("pfp-path", "" + user.getMobileNumber() + ".jpg");
            request.put("sendMobileNumber", sendFrom);

            requestList.add(request);
        }

        return requestList;

    }

    public Integer isFriend( Map<String , String > entity) {
        String selfMob =  entity.get("selfMobileNumber");
        String userMob = entity.get("userMobileNumber"); 

        FriendList frdLst = friendListRepository.findByUserMobileNumberAndfriendMobileNumber(selfMob, userMob);
        if(frdLst == null ){
            return 0;
        }
        return 1;
    }

    public Integer isFriendRequestSent( Map<String , String > entity ){
        String selfMob =  entity.get("selfMobileNumber");
        String userMob = entity.get("userMobileNumber");

        FriendRequestList frl = friendRequestListRepository.checkRequestList(selfMob, userMob);

                if(frl == null ){
            return 0;
        }
        return 1;
    }
    public Integer 
    isFriendRequestReceived( Map<String , String > entity ){
        String selfMob =  entity.get("selfMobileNumber");
        String userMob = entity.get("userMobileNumber");
        FriendRequestList frl = friendRequestListRepository.checkRequestList(userMob , selfMob);
        if(frl == null ){
            return 0;
        }
        return 1;
    }

}
