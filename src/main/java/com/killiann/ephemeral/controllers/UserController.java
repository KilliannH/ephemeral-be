package com.killiann.ephemeral.controllers;

import com.killiann.ephemeral.exceptions.UserNotFoundException;
import com.killiann.ephemeral.repositories.UserRepository;
import com.killiann.ephemeral.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/limit")
    Page<UserModel> limit(@RequestParam Integer start, @RequestParam Integer end) {
        return userRepository.findAll(
                PageRequest.of(start, end));
    }

    @GetMapping("/users")
    List<UserModel> all() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    UserModel one(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/users/facebookId/{facebookid}")
    UserModel one(@PathVariable String facebookid) {
        return userRepository.findByFacebookId(facebookid)
                .orElseThrow(() -> new UserNotFoundException(facebookid));
    }

    @PutMapping("/users/{id}")
    UserModel replaceUser(@RequestBody UserModel newUserModel, @PathVariable Long id) {

        return userRepository.findById(id)
                .map(userModel -> {
                    userModel.setUsername(newUserModel.getUsername());
                    userModel.setFacebookId(newUserModel.getFacebookId());
                    userModel.setEmail(newUserModel.getEmail());
                    userModel.setRole(newUserModel.getRole());
                    return userRepository.save(userModel);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

}
