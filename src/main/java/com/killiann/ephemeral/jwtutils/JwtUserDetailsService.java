package com.killiann.ephemeral.jwtutils;

import com.killiann.ephemeral.repositories.UserRepository;
import com.killiann.ephemeral.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.get().getUsername(), "", new ArrayList<>());
    }

    public UserDetails loadNewUser(UserModel newUser) {
        return new User(newUser.getUsername(), "", new ArrayList<>());
    }
}