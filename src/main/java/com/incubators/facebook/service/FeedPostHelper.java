package com.incubators.facebook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incubators.facebook.data.model.CommentList;
import com.incubators.facebook.data.model.Post;
import com.incubators.facebook.data.model.User;
import com.incubators.facebook.data.repository.CommentListRepository;
import com.incubators.facebook.data.repository.LikesListRepository;
import com.incubators.facebook.data.repository.UserRepository;

@Service
public class FeedPostHelper {
    
    @Autowired
    private LikesListRepository likesListRepository;

    @Autowired
    private CommentListRepository commentListRepository;

    @Autowired
    private UserRepository userRepository;

    public  List< Map < String , String >> createPostMap( List<Post> postlList , String mobileNumber ){

        List< Map < String , String >> postListMap = new ArrayList<>();
        ListIterator<Post> listItr = postlList.listIterator();

        while (listItr.hasNext()) {
            
            Post post = listItr.next();
            Map<String,String> postData = new HashMap<>(); 
            postData.put("postId", post.getId().toString());
            postData.put("path", post.getPostName());
            postData.put("mobileNumber",post.getPostUserMobileNumber() );
            postData.put("postName",post.getPostName() );
            postData.put("likeCount",post.getLikesCount().toString() );
            postData.put("commentCount", post.getCommentsCount().toString() );
            postData.put("caption",post.getCaption());
            postData.put("time", post.getTime().toString() );
            postData.put("date",post.getDate().toString());
            postData.put("userFirstName",post.getFirstName());
            postData.put("userLastName",post.getLastName());


            if(likesListRepository.findLikeObject(post.getId(), mobileNumber).isEmpty()){
                postData.put("isLiked","0");
            }else{
                postData.put("isLiked","1");
            }


            postListMap.add(postData);
        }

        return postListMap;
    }

    public List<Map<String , String >> showComment (String commentPostId) {

        List<CommentList> comments = commentListRepository.findAllByCommentPostId(Integer.parseInt(commentPostId));
        ListIterator<CommentList> commentIterator = comments.listIterator();

        List<Map<String , String >> commentMap = new ArrayList<>();

        while (commentIterator.hasNext()) {
            CommentList commentObject = commentIterator.next();
            
            Map<String , String > comment = new HashMap<>();
            comment.put("commentId",commentObject.getId().toString());
            comment.put("commentData",commentObject.getCommentData());
            comment.put("userCommentMobile",commentObject.getCommentByUserMobileNumber());
            comment.put("time",commentObject.getTime().toString());
            comment.put("date",commentObject.getDate().toString());
            comment.put("pfpName",commentObject.getCommentByUserMobileNumber()+".jpg");
            User user = userRepository.findById(commentObject.getCommentByUserMobileNumber()).get();
            comment.put("firstName",user.getFirstName());
            comment.put("lastName",user.getLastName());

            commentMap.add(comment);
             
        }

        return commentMap;
        
    }
}
