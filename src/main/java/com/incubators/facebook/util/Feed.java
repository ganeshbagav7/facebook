package com.incubators.facebook.util;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import com.incubators.facebook.data.model.FriendRequestList;

public class Feed {
    Pageable pageable;
    List<Request> requestList;
    public Feed(Pageable pageable, List<Request> requestList) {
        this.pageable = pageable;
        this.requestList = requestList;
    }

}
