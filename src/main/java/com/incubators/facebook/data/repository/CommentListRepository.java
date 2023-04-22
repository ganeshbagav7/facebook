package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.incubators.facebook.data.model.CommentList;

import jakarta.transaction.Transactional;

public interface CommentListRepository extends JpaRepository<CommentList,Integer> {
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CommentList cl WHERE  cl.commentPostId = :id AND cl.commentByUserMobileNumber = :mobno ")
    void deleteComment(Integer id , String mobno );

    public void deleteAllByCommentPostId(Integer id);

    @Query(value = "SELECT * FROM comment_list cl WHERE  cl.commentPostId = :id ORDER BY date , time ", nativeQuery = true)
    public List<CommentList> findAllByCommentPostId(Integer id);

}
