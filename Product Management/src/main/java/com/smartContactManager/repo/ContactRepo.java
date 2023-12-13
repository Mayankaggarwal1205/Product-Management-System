package com.smartContactManager.repo;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

    @Query("from Contact as d where d.user.id = :userId")
    // Pageable contains two information:
    // 1. current page --> that we have to pass
    // 2. no of contants per page --> that we have to pass
    public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pepagable);


    List<Contact> findByNameContainingAndUser(String name, User user);

}
