package com.smartContactManager.config;

import com.smartContactManager.entity.User;
import com.smartContactManager.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        fetching user from database
        User userByUsername = userRepo.getUserByUsername(username);
        if(userByUsername == null){
           throw new UsernameNotFoundException("Could not found user..");
        }
        CustomUserDetail customUserDetail = new CustomUserDetail(userByUsername);
        return customUserDetail;
    }
}
