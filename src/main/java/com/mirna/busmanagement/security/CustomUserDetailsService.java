package com.mirna.busmanagement.security;

import com.mirna.busmanagement.models.User;
import com.mirna.busmanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userService.findUserByUsernameOpt(username);

        if (optionalUser.isPresent()) {
            User user = userService.getUserByUsername(username);
            return new CustomUserDetails(user);
        } else {
            throw new UsernameNotFoundException("Username doesn't exist!");
        }
    }

}
