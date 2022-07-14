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

    @PostMapping("/users")
    UserModel newUser(@RequestBody UserModel newUserModel) {
        return userRepository.save(newUserModel);
    }

    @GetMapping("/users/{id}")
    UserModel one(@PathVariable String id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/users/{id}")
    UserModel replaceUser(@RequestBody UserModel newUserModel, @PathVariable String id) {

        return userRepository.findById(id)
                .map(userModel -> {
                    userModel.setUsername(newUserModel.getUsername());
                    userModel.setEmail(newUserModel.getEmail());
                    userModel.setPassword(newUserModel.getPassword());
                    userModel.setRole(newUserModel.getRole());
                    return userRepository.save(userModel);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
    }

}
