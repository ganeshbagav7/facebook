package com.incubators.facebook.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incubators.facebook.data.model.User;


public interface UserRepository extends JpaRepository<User,String> {

    // @Query(value = "SELECT * FROM user_info ui WHERE ui.first_name = :firstName",nativeQuery = true)
    // public List<User> findUsers(String firstName );

    // @Query(value = "SELECT * FROM user_info ui WHERE ui.first_name = :firstName AND ui.last_name = :lastName",nativeQuery = true)
    // public List<User> findUsers(String firstName , String lastName );

    @Query(value = "SELECT * FROM user_info ui WHERE LOWER(ui.first_name) LIKE %:name% OR LOWER(ui.last_name) LIKE %:name%",nativeQuery = true)
    public List<User> searchUsers(String name );

}
