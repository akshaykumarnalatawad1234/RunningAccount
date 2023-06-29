package dev.akshay.RunningAccount.controller;

import dev.akshay.RunningAccount.repository.UserRepository;
import dev.akshay.RunningAccount.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @RequestMapping(value = "/api/v1/user/create", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody User user){
        userRepository.save(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
}
