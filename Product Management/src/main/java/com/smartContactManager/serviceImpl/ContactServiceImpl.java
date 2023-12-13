package com.smartContactManager.serviceImpl;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.repo.ContactRepo;
import com.smartContactManager.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;


    @Override
    public Iterable<Contact> findAll() {
        return contactRepo.findAll();
    }
}
