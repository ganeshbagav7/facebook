package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.incubators.facebook.data.model.Post;

public interface PostRepository extends CrudRepository<Post,Integer>{
    
    //import com.incubators.facebook.data.model.Post;

    @Query(value = "SELECT * FROM post_info pi WHERE pi.post_user_mobile_number= :mobileNumber" , nativeQuery = true )
    public List<Post> findAllUserPosts(String mobileNumber );

    @Query(value = "SELECT * FROM post_info ORDER BY date DESC , time DESC", nativeQuery = true)
    public List<Post> findAllOrderByIdDesc();
    
    public Post getById(Integer postId);
}
