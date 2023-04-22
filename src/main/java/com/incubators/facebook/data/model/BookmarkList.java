package com.incubators.facebook.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookmark_list")
public class BookmarkList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer bookmarkedPostId;
    private String bookmarkedByUserMobileNumber;
    public BookmarkList(Integer bookmarkedPostId, String bookmarkedByUserMobileNumber) {
        this.bookmarkedPostId = bookmarkedPostId;
        this.bookmarkedByUserMobileNumber = bookmarkedByUserMobileNumber;
    }
    public BookmarkList() {
    }
    
}
