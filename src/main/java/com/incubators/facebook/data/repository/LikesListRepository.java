package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.incubators.facebook.data.model.LikesList;

import jakarta.transaction.Transactional;

public interface LikesListRepository extends JpaRepository<LikesList,Integer>{
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM LikesList ll WHERE  ll.likedPostId = :id AND ll.userLikedPostMobileNumber = :mobno ")
    void deleteLike(Integer id , String mobno );

    public void  deleteAllByLikedPostId(Integer id);

    @Query(value = "SELECT * FROM likes_list ll WHERE  ll.liked_post_id = :id AND ll.user_liked_post_mobile_number = :mobno ",nativeQuery = true )
    List<LikesList> findLikeObject(Integer id , String mobno );


}
