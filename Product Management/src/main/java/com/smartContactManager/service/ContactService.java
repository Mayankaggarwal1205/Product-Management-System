package com.smartContactManager.service;

import com.smartContactManager.entity.Contact;

public interface ContactService {

    Iterable<Contact> findAll();
}
