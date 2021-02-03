package com.zakharov.restful.service;

import com.zakharov.restful.model.MyUser;
import com.zakharov.restful.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        MyUser myUser = myUserRepository.findByLogin(userName);

        if (myUser == null) {
            throw new UsernameNotFoundException("Unknown user: " + userName);
        }
        UserDetails user = User.builder().username(myUser.getLogin()).password(myUser.getPassword()).roles(myUser.getRole()).build();
        return user;
    }
}