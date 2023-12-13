package com.smartContactManager.repo;

import com.smartContactManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email = :email")
    public User getUserByUsername(@Param("email") String email);
//    @Param is used to pass dynamic value of email in ":email" whatever value we will pass in email it will
//    replace directly it in query
}
