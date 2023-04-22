package com.incubators.facebook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

import com.incubators.facebook.data.repository.UserRepository;
import com.incubators.facebook.service.ShowFeed;
import com.incubators.facebook.service.UserService;

@RestController
@RequestMapping("/facebook")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShowFeed showFeed;

    @Autowired 
    private UserRepository userRepository;

    private static String filePath = "";

    static {
        try {

            filePath = new ClassPathResource("static/images").getFile().getAbsolutePath();

        } catch (IOException io) {

            System.out.println(io.getMessage());

        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody Map<String, String> entity) {
        System.out.println("signup");
        if (entity.get("mobileNumber").equals(null)) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("message", "null mobile number");
            return ResponseEntity.ok().body(response);
        }
        return userService.signUp(entity);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody Map<String, String> ent) {

        return userService.forgotPassword(ent);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody Map<String, String> ent) {

        return userService.resetPassword(ent);
    }

    @PostMapping("/log-in")
    public ResponseEntity<Object> logIn(@RequestBody Map<String, String> entity) {
        System.out.println("log-in");
        if (entity.get("mobileNumber").equals(null)) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("message", "null mobile number");
            return ResponseEntity.ok().body(response);
        }
        return userService.logIn(entity);
    }

    @PostMapping("/add-post")
    public ResponseEntity<Object> addPost(@RequestParam(name = "post") MultipartFile multipartFile,
            @RequestParam String caption, @RequestParam String postUserMobileNumber, @RequestParam Date date,
            @RequestParam Time time) {
                Map<String, String> entity = new HashMap<String, String>();
                if(!userRepository.existsById(postUserMobileNumber)){
                    entity.put("message", "user not found");    
                    return ResponseEntity.ok().body(entity);
                }
        
        userService.addPost(multipartFile, caption, postUserMobileNumber, date, time);
        entity.put("mobileNumber", postUserMobileNumber);
        return ResponseEntity.ok().body(showFeed.getFeed(entity));

    }

    @PostMapping("/add-pfp")
    public ResponseEntity<Object> addPfp(@RequestParam(name = "pfp") MultipartFile multipartFile,
            @RequestParam String pfpUserMobileNumber) {

        Map<String, String> entity = new HashMap<String, String>();
        if(!userRepository.existsById(pfpUserMobileNumber)){
            entity.put("message", "user not found");    
            return ResponseEntity.ok().body(entity);
        }

        userService.addPfp(multipartFile, pfpUserMobileNumber);
        entity.put("mobileNumber", pfpUserMobileNumber);
        return userService.userProfile(entity);
    }

    @GetMapping("/get-user-profile")
    public ResponseEntity<Object> getUserProfile(@RequestBody Map<String, String> entity) {

        return userService.userProfile(entity);
    }


    @DeleteMapping("/delete-post")
    public ResponseEntity<Object> deletePost(@RequestBody Map<String, String> ent) {

        return userService.deletePost(ent);
    }

    @GetMapping("/show-friend-requests")
    public ResponseEntity<Object> showFriendRequests(@RequestBody Map<String, String> entity) {
        return ResponseEntity.ok().body(userService.showFriendRequest(entity.get("mobileNumber")));
    }

    @PutMapping("/edit-post")
    public ResponseEntity<Object> editPost(@RequestParam(name = "post") MultipartFile multipartFile,
            @RequestParam String caption, @RequestParam String postUserMobileNumber, @RequestParam Date date,
            @RequestParam Time time, @RequestParam String postId) {

        return userService.editPost(multipartFile, caption, postUserMobileNumber, date, time, postId);
    }

    @PostMapping("/friend-request-result")
    public ResponseEntity<Object> friendRequestResult(@RequestBody Map<String, String> entity) {
        return userService.friendRequestResult(entity);
    }

    @PostMapping("/get-pfp")
    public ResponseEntity<InputStreamResource> getPfp(@RequestBody Map<String,String> entity ) {
        // System.out.println("image name "+entity.get("image"));
        return userService.getPfp(entity.get("image"));
    }

    @PostMapping("/get-post")
    public ResponseEntity<Resource> getPost(@RequestParam String postName) {

        return userService.getPost(postName);
    }

}
