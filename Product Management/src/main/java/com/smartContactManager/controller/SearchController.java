package com.smartContactManager.controller;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import com.smartContactManager.repo.ContactRepo;
import com.smartContactManager.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private UserRepo userRepo;

    // search handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query,
                                    Principal principal){
        String name = principal.getName();
        User user = this.userRepo.getUserByUsername(name);
        List<Contact> contacts = this.contactRepo.findByNameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }

}
