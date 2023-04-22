package com.incubators.facebook.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incubators.facebook.service.FeedPostHelper;
import com.incubators.facebook.service.InteractionService;

@RestController
@RequestMapping("/facebook")
public class InteractionController {
    

   @Autowired
   private InteractionService interactionService;

   @Autowired
   private FeedPostHelper feedPostHelper;

    @PostMapping("/like-post")
    public ResponseEntity<Object> likePost( @RequestBody  Map<String,String> entity ){
        return interactionService.likePost(entity);
    }

    @GetMapping("/show-bookmark-post")
    public ResponseEntity<Object> showBookmarkPost(@RequestBody Map<String,String> entity){
        return interactionService.showBookmarkPost(entity);
    }

    @PostMapping("/add-friend")
    public ResponseEntity<Object> addFriend(@RequestBody Map<String,String> entity){
        return  interactionService.addFriends(entity);
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<Object> deleteComment(@RequestBody Map<String,String> entity){
        return interactionService.deleteComment(entity);
    }

    @DeleteMapping("/remove-bookmark")
    public ResponseEntity<Object> removeBookmark(@RequestBody Map<String,String> entity){
        return interactionService.removeBookmarkPost(entity);
    }

    @DeleteMapping("/undo-like")
    public ResponseEntity<Object> undoLike(@RequestBody Map<String,String> entity){
        return interactionService.undoLike(entity);
    }   

    @PostMapping("/comment-post")
    public ResponseEntity<Object> commentPost(@RequestBody Map<String,String> entity){
        return interactionService.commentPost(entity);
    }

    @PostMapping("/bookmark-post")
    public ResponseEntity<Object> bookmarkPost(@RequestBody Map<String,String> entity){
        return interactionService.bookmarkPost(entity);
    }
    
    
    @GetMapping("/show-friends")
    public ResponseEntity<Object> showFriends(@RequestBody Map<String,String> entity){
        return interactionService.showFriends(entity);      
    }

    @GetMapping("/show-comments")
    public ResponseEntity<Object> showComments(@RequestBody Map<String,String> entity ){
        return ResponseEntity.ok().body(feedPostHelper.showComment(entity.get("postId")));
    }

    @DeleteMapping("/remove-friend")
    public ResponseEntity<Object> removeFriend(@RequestBody Map<String,String> entity ){
        return interactionService.removeFriend(entity);
    }

    @PostMapping("/search-user")
    public ResponseEntity<Object> searchUesr(@RequestBody Map<String,String> entity ){
        return ResponseEntity.ok().body(interactionService.searchUser(entity));
    }



    

}
