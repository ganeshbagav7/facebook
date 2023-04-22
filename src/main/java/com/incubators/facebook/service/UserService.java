package com.incubators.facebook.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.incubators.facebook.data.model.FriendList;
import com.incubators.facebook.data.model.FriendRequestList;
import com.incubators.facebook.data.model.LikesList;
import com.incubators.facebook.data.model.Post;
import com.incubators.facebook.data.model.User;
import com.incubators.facebook.data.repository.FriendRequestListRepository;
import com.incubators.facebook.data.repository.LikesListRepository;
import com.incubators.facebook.data.repository.PostRepository;
import com.incubators.facebook.data.repository.UserRepository;
import com.incubators.facebook.util.UserData;

import jakarta.servlet.http.HttpServletResponse;

import com.incubators.facebook.data.repository.CommentListRepository;
import com.incubators.facebook.data.repository.FriendListRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikesListRepository likesListRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserPostHelper userPostHelper;

    @Autowired
    private FriendRequestListRepository friendRequestListRepository;

    @Autowired
    private CommentListRepository commentListRepository;

    @Autowired
    private ShowFeed showFeed;

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

    public ResponseEntity<Object> signUp(Map<String, String> entity) {

        String mobileNumber = entity.get("mobileNumber");
        Map<String, String> response = new HashMap<String, String>();

        if (userRepository.existsById(mobileNumber)) {
            response.put("message", "already in use");
            return ResponseEntity.ok().body(response);
        }
        String firstName = entity.get("firstName");
        String lastName = entity.get("lastName");
        String password = entity.get("password");
        String securityQuestion = entity.get("securityQuestion");
        String securityAnswer = entity.get("securityAnswer");
        Date dob = Date.valueOf(entity.get("date"));
        String gender = entity.get("gender");

        User user = new User(mobileNumber, firstName, lastName, password, securityQuestion, securityAnswer, dob, gender,
                0, 0);
        userRepository.save(user);

        return ResponseEntity.ok().body(showFeed.getFeed(entity));

    }

    public ResponseEntity<Object> logIn(Map<String, String> entity) {
        String mobileNumber = entity.get("mobileNumber");
        Map<String, String> response = new HashMap<String, String>();

        if (userRepository.existsById(mobileNumber)) {
            String password = userRepository.findById(mobileNumber).get().getPassword();
            if (password.equals(entity.get("password"))) {

                // send feed / home page data
                org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
                return ResponseEntity.ok().body(showFeed.getFeed(entity));
            }
            response.put("message", "wrong password");
            return ResponseEntity.ok().body(response);
        }
        response.put("message", "user not found");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> addPost(MultipartFile multipartFile,
            String caption, String postUserMobileNumber, Date date, Time time) {

        Map<String, String> response = new HashMap<String, String>();

        if (multipartFile.getContentType().equals("image/jpeg")) {
            System.out.println("jpeg");
        } else {
            response.put("message", "only jpeg data is valid");
            return ResponseEntity.ok().body(response);
        }
        User user = userRepository.findById(postUserMobileNumber).get();
        String postName = postUserMobileNumber + "-" + (user.getPostCount() + 1) + ".jpg";
        // Upload Image in target/static folder
        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath + "/post/" + postName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            response.put("message", "something went wrong file not uploaded");
            return ResponseEntity.ok().body(response);
        }
        Post post = new Post(postUserMobileNumber, user.getPostCount() + 1, caption, 0, 0, date, time, postName,
                user.getFirstName(), user.getLastName());
        postRepository.save(post);

        user.setPostCount(user.getPostCount() + 1);
        userRepository.save(user);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> forgotPassword(Map<String, String> ent) {
        String mobileNumber = ent.get("mobileNumber");
        Map<String, String> response = new HashMap<String, String>();

        if (userRepository.existsById(mobileNumber)) {
            if (ent.get("securityQuestion").equals(userRepository.findById(mobileNumber).get().getSecurityQuestion())) {
                if (ent.get("securityAnswer").equals(userRepository.findById(mobileNumber).get().getSecurityAnswer())) {
                    response.put("message", "success");
                    return ResponseEntity.ok().body(response);
                }
                response.put("message", "Invalid Answer");
                return ResponseEntity.ok().body(response);
            }
            response.put("message", "Invalid Question Selection");
            return ResponseEntity.ok().body(response);
        }
        response.put("message", "User does not exist");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> resetPassword(Map<String, String> ent) {
        String mobileNumber = ent.get("mobileNumber");
        Map<String, String> response = new HashMap<String, String>();

        User user = userRepository.findById(mobileNumber).get();
        user.setPassword(ent.get("password"));
        userRepository.save(user);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> addPfp(MultipartFile multipartFile, String postUserMobileNumber) {

        Map<String, String> response = new HashMap<String, String>();

        if (multipartFile.getContentType().equals("image/jpeg")) {
            System.out.println("jpeg");
        } else {
            response.put("message", "only jpeg data is valid");
            return ResponseEntity.ok().body(response);
        }

        String postName = postUserMobileNumber + ".jpg";

        // Upload Image in target/static folder
        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath + "/pfp/" + postName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            response.put("message", "something went wrong file not uploaded");
            return ResponseEntity.ok().body(response);
        }

        response.put("message", "success");

        return ResponseEntity.ok().body(response);

    }

    public <T> ResponseEntity<Object> userProfile(Map<String, String> entity) {
        Map<String, String> response = new HashMap<String, String>();

        if (userRepository.existsById(entity.get("mobileNumber"))) {

            Map<String, Map<String, Map<String, String>>> userProfileData = new HashMap<>();
            User user = userRepository.findById(entity.get("mobileNumber")).get();
            Map<String, String> userData = new HashMap<>();
            userData.put("mobileNumber", user.getMobileNumber());
            userData.put("pfpName", user.getMobileNumber() + ".jpg");
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("dob", user.getDob().toString());
            userData.put("gender", user.getGender());
            userData.put("postCount", user.getPostCount().toString());
            userData.put("friendsCount", user.getFriendsCount().toString());

            if (entity.get("mobileNumber").equals(entity.get("selfNumber"))) {
                userData.put("isSelf", "1");
            } else {
                userData.put("isSelf", "0");
                userData.put("isFriend", userPostHelper.isFriend(entity).toString());
                userData.put("isFriendRequestReceived", userPostHelper.isFriendRequestReceived(entity).toString());
                userData.put("isFriendRequestSent", userPostHelper.isFriendRequestSent(entity).toString());

            }
            Map<String, Map<String, String>> allUserData = new HashMap<>();

            allUserData.put(entity.get("mobileNumber"), userData);

            userProfileData.put("userData", allUserData);
            userProfileData.put("userPost", userPostHelper.getUserPosts(entity.get("mobileNumber")));

            return ResponseEntity.ok().body(userProfileData);

        }
        response.put("message", "user not found");
        return ResponseEntity.ok().body(response);
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

    public ResponseEntity<Object> deletePost(Map<String, String> ent) {
        // String mobileNumber = ent.get("mobileNumber");
        int postId = Integer.parseInt(ent.get("postId"));
        Map<String, String> response = new HashMap<String, String>();

        Post post = postRepository.findById(postId).get();

        likesListRepository.deleteAllByLikedPostId(postId);
        commentListRepository.deleteAllByCommentPostId(postId);

        postRepository.delete(post);

        response.put("message", "deleted");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> friendRequestResult(Map<String, String> entity) {

        Map<String, String> response = new HashMap<String, String>();

        // Extracting data from requestBody
        String sendFromMob = entity.get("sendFromMobileNumber");
        String sendToMob = entity.get("sendToMobileNumber");

        // Deleting pending request
        friendRequestListRepository.deleteRequest(sendToMob, sendFromMob);

        if (Integer.parseInt(entity.get("result")) == 0) {
            response.put("message", "Request rejected");
            return ResponseEntity.ok().body(response);
        }
        // Creating Friends relation
        FriendList friendList1 = new FriendList(sendFromMob, sendToMob);
        FriendList friendList2 = new FriendList(sendToMob, sendFromMob);
        friendListRepository.save(friendList1);
        friendListRepository.save(friendList2);

        User user = userRepository.findById(sendToMob).get();
        user.setFriendsCount(user.getFriendsCount() + 1);
        userRepository.save(user);

        // Incrementing Friend Count of user 2
        user = userRepository.findById(sendFromMob).get();
        user.setFriendsCount(user.getFriendsCount() + 1);
        userRepository.save(user);
        // Incrementing Friend Count of user 1

        response.put("message", "Request Accepted");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> editPost(MultipartFile multipartFile,
            String caption, String postUserMobileNumber, Date date, Time time, String postId) {

        Map<String, String> response = new HashMap<String, String>();

        if (multipartFile.getContentType().equals("image/jpeg")) {
            System.out.println("jpeg");
        } else {
            response.put("message", "only jpeg data is valid");
            return ResponseEntity.ok().body(response);
        }
        Post post = postRepository.findById(Integer.parseInt(postId)).get();

        try {
            Files.copy(multipartFile.getInputStream(), Paths.get(filePath + "/post/" + post.getPostName()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            response.put("message", "something went wrong file not uploaded");
            return ResponseEntity.ok().body(response);
        }

        post.setCaption(caption);
        post.setDate(date);
        post.setTime(time);
        postRepository.save(post);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<InputStreamResource> getPfp(String pfp) {
        File file = new File(filePath + "/pfp/" + pfp);
        InputStreamResource inputStreamResource = null;
        try {

            inputStreamResource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception e) {
            // TODO: handle exception
            // return default pfp image if given pfp is not present 
            File file2 = new File(filePath + "/pfp/user.jpg");
            try {

                inputStreamResource = new InputStreamResource(new FileInputStream(file2));
            } catch (Exception ex) {
                // TODO: handle exception
                
            }
            org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
            responseHeaders.set("Content-Type", "image/jpeg");
            return ResponseEntity.ok().headers(responseHeaders).contentType(MediaType.IMAGE_JPEG)
                    .body(inputStreamResource);

            // System.out.println("error : "+e.getMessage());
        }
        org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.set("Content-Type", "image/jpeg");
        return ResponseEntity.ok().headers(responseHeaders).contentType(MediaType.IMAGE_JPEG).body(inputStreamResource);
    }

    public ResponseEntity<Resource> getPost(String post) {
        File file = new File(filePath + "/post/" + post);
        InputStreamResource inputStreamResource = null;
        try {

            inputStreamResource = new InputStreamResource(new FileInputStream(file));
        } catch (Exception e) {
            // TODO: handle exception
            // return default pfp image if given pfp is not present 
            File file2 = new File(filePath + "/post/user.jpg");
            try {

                inputStreamResource = new InputStreamResource(new FileInputStream(file2));
            } catch (Exception ex) {
                // TODO: handle exception
                
            }
            org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
            responseHeaders.set("Content-Type", "image/jpeg");
            return ResponseEntity.ok().headers(responseHeaders).contentType(MediaType.IMAGE_JPEG)
                    .body(inputStreamResource);

            // System.out.println("error : "+e.getMessage());
        }
        org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.set("Content-Type", "image/jpeg");
        return ResponseEntity.ok().headers(responseHeaders).contentType(MediaType.IMAGE_JPEG).body(inputStreamResource);
    }
}