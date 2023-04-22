package com.incubators.facebook.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incubators.facebook.data.model.BookmarkList;
import com.incubators.facebook.data.model.CommentList;
import com.incubators.facebook.data.model.FriendList;
import com.incubators.facebook.data.model.FriendRequestList;
import com.incubators.facebook.data.model.LikesList;
import com.incubators.facebook.data.model.Post;
import com.incubators.facebook.data.model.User;
import com.incubators.facebook.data.repository.BookmarkListRepository;
import com.incubators.facebook.data.repository.CommentListRepository;
import com.incubators.facebook.data.repository.FriendListRepository;
import com.incubators.facebook.data.repository.FriendRequestListRepository;
import com.incubators.facebook.data.repository.LikesListRepository;
import com.incubators.facebook.data.repository.PostRepository;
import com.incubators.facebook.data.repository.UserRepository;

@Service
public class InteractionService {

    @Autowired
    private LikesListRepository likesListRepository;

    @Autowired
    private CommentListRepository commentListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BookmarkListRepository bookmarkListRepository;

    @Autowired
    private FriendRequestListRepository friendRequestListRepository;

    @Autowired
    private FriendListRepository friendListRepository;

    @Autowired
    private FeedPostHelper feedPostHelper;

    public ResponseEntity<Object> likePost(Map<String, String> entity) {

        /*
         * likedPostId
         * userLikedPostMobileNumber
         * 
         */

        Map<String, String> response = new HashMap<String, String>();

        String userLikedPostMobileNumber = entity.get("userLikedPostMobileNumber");
        Integer likedPostId = Integer.parseInt(entity.get("likedPostId"));

        LikesList likesList = new LikesList(likedPostId, userLikedPostMobileNumber);
        likesListRepository.save(likesList);

        Post post = postRepository.findById(likedPostId).get();
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> commentPost(Map<String, String> entity) {

        /*
         * commentByUserMobileNumber
         * commentData
         * commentPostId
         * date
         * time
         * 
         */

        // Map<String, String> response = new HashMap<String, String>();

        Integer commentPostId = Integer.parseInt(entity.get("commentPostId"));
        String commentData = entity.get("commentPostId");
        String commentByUserMobileNumber = entity.get("commentByUserMobileNumber");
        Time time = Time.valueOf(entity.get("time"));
        Date date = Date.valueOf(entity.get("date"));

        Post post = postRepository.findById(commentPostId).get();
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        CommentList commentList = new CommentList(commentPostId, commentData, commentByUserMobileNumber, date, time);
        commentListRepository.save(commentList);

        // response.put("message", "success");
        return ResponseEntity.ok().body(feedPostHelper.showComment(entity.get("commentPostId")));

    }

    public ResponseEntity<Object> bookmarkPost(Map<String, String> entity) {
        /*
         * bookmarkedByUserMobileNumber
         * bookmarkedPostId
         * 
         */
        Map<String, String> response = new HashMap<String, String>();

        Integer bookmarkedPostId = Integer.parseInt(entity.get("bookmarkedPostId"));
        String bookmarkedByUserMobileNumber = entity.get("bookmarkedByUserMobileNumber");

        BookmarkList bookmarkList = new BookmarkList(bookmarkedPostId, bookmarkedByUserMobileNumber);
        bookmarkListRepository.save(bookmarkList);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> removeBookmarkPost(Map<String, String> entity) {

        Map<String, String> response = new HashMap<String, String>();

        Integer removeBookmarkPostId = Integer.parseInt(entity.get("removeBookmarkPostId"));
        String bookmarkedByUserMobileNumber = entity.get("bookmarkedByUserMobileNumber");

        bookmarkListRepository.deleteBookmark(removeBookmarkPostId, bookmarkedByUserMobileNumber);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> undoLike(Map<String, String> entity) {
        // Delete query

        /*
         * unlikedPostId
         * userUnlikedPostMobileNumber
         * 
         */

        Map<String, String> response = new HashMap<String, String>();

        String userUnlikedPostMobileNumber = entity.get("userUnlikedPostMobileNumber");
        Integer unlikedPostId = Integer.parseInt(entity.get("unlikedPostId"));

        likesListRepository.deleteLike(unlikedPostId, userUnlikedPostMobileNumber);

        Post post = postRepository.findById(unlikedPostId).get();
        post.setLikesCount(post.getLikesCount() - 1);
        postRepository.save(post);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> deleteComment(Map<String, String> entity) {

        // * commentByUserMobileNumber
        // * commentPostId

        Map<String, String> response = new HashMap<String, String>();

        String commentByUserMobileNumber = entity.get("commentByUserMobileNumber");
        Integer commentPostId = Integer.parseInt(entity.get("commentPostId"));

        commentListRepository.deleteComment(commentPostId, commentByUserMobileNumber);

        Post post = postRepository.findById(commentPostId).get();
        post.setCommentsCount(post.getCommentsCount() - 1);
        postRepository.save(post);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> addFriends(Map<String, String> entity) {

        Map<String, String> response = new HashMap<String, String>();

        String sendToMobileNumber = entity.get("sendToMobileNumber");
        String sendFromMobileNumber = entity.get("sendFromMobileNumber");

        FriendRequestList friendRequestList = new FriendRequestList(sendToMobileNumber, sendFromMobileNumber);

        friendRequestListRepository.save(friendRequestList);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> showBookmarkPost(Map<String, String> entity) {

        // mobileNumber

        // bookmarked_by_user_mobile_number
        // bookmarked_post_id

        List<BookmarkList> bl = bookmarkListRepository.findBookmarkPost(entity.get("mobileNumber"));
        ListIterator<BookmarkList> listItr = bl.listIterator();

        List<Map<String, String>> userPostList = new ArrayList<>();

        while (listItr.hasNext()) {

            BookmarkList bookmark = listItr.next();
            Post post = postRepository.findById(bookmark.getBookmarkedPostId()).get();
            Map<String, String> postData = new HashMap<>();
            postData.put("path", "/post/" + post.getPostName());
            postData.put("mobileNumber", post.getPostUserMobileNumber());
            postData.put("postName", post.getPostName());
            postData.put("likeCount", post.getLikesCount().toString());
            postData.put("commentCount", post.getCommentsCount().toString());
            postData.put("caption", post.getCaption());
            postData.put("time", post.getTime().toString());
            postData.put("date", post.getDate().toString());
            postData.put("userFirstName", userRepository.findById(post.getPostUserMobileNumber()).get().getFirstName());
            postData.put("userLastName", userRepository.findById(post.getPostUserMobileNumber()).get().getLastName());
            userPostList.add(postData);
        }

        return ResponseEntity.ok().body(userPostList);

    }

    public ResponseEntity<Object> showFriends(Map<String, String> entity) {
        List<FriendList> friendLists = friendListRepository.findByUserMobileNumber(entity.get("userMobileNumber"));

        ListIterator<FriendList> itr = friendLists.listIterator();

        List<Map<String, String>> response = new ArrayList<>();

        while (itr.hasNext()) {
            FriendList friend = itr.next();
            String friendMobileNumber = friend.getFriendMobileNumber();
            User user = userRepository.findById(friendMobileNumber).get();
            Map<String, String> friendData = new HashMap<>();
            friendData.put("name", user.getFirstName() + " " + user.getLastName());
            friendData.put("pfp-path", "" + user.getMobileNumber() + ".jpg");
            friendData.put("userMobileNumber", friendMobileNumber);
            response.add(friendData);
        }

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Object> removeFriend(Map<String, String> entity) {
        Map<String, String> response = new HashMap<String, String>();

        String selfMobileNumber = entity.get("selfMobileNumber");
        String friendMobileNumber = entity.get("friendMobileNumber");

        friendListRepository.deleteFriend(selfMobileNumber, friendMobileNumber);
        friendListRepository.deleteFriend(friendMobileNumber, selfMobileNumber);

        // Incrementing Friend Count of user 1
        User user = userRepository.findById(selfMobileNumber).get();
        user.setFriendsCount(user.getFriendsCount() - 1);
        userRepository.save(user);

        // Incrementing Friend Count of user 2
        user = userRepository.findById(friendMobileNumber).get();
        user.setFriendsCount(user.getFriendsCount() - 1);
        userRepository.save(user);

        response.put("message", "success");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Object> searchUser(Map<String, String> entity) {
        // same as showFriendRequest
        List<User> users = userRepository.searchUsers(entity.get("name"));
        ListIterator<User> usersList = users.listIterator();
        List<Map<String, String>> searchUsers = new ArrayList<>();

        while (usersList.hasNext()) {
            User user = usersList.next();
            Map<String, String> userData = new HashMap<String, String>();
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("mobileNumber", user.getMobileNumber());
            userData.put("pfp", user.getMobileNumber() + ".jpg");
            userData.put("friendCount", user.getFriendsCount().toString());
            searchUsers.add(userData);
        }

        return ResponseEntity.ok().body(searchUsers);
    }

}
