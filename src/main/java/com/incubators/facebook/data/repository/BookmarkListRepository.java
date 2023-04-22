package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.incubators.facebook.data.model.BookmarkList;

import jakarta.transaction.Transactional;

public interface BookmarkListRepository extends JpaRepository<BookmarkList,Integer> {
        
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM BookmarkList bm WHERE  bm.bookmarkedPostId = :id AND bm.bookmarkedByUserMobileNumber = :mobno ")
    void deleteBookmark(Integer id , String mobno );

    @Query(value = "SELECT * FROM BookmarkList bm WHERE bm.bookmarked_by_user_mobile_number = :mobileNumber",nativeQuery = true)
    public List<BookmarkList> findBookmarkPost(String mobileNumber );

}
