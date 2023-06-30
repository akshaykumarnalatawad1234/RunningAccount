package dev.akshay.RunningAccount.controller;

import dev.akshay.RunningAccount.domain.UserObject;
import dev.akshay.RunningAccount.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/api/v1/user", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody UserObject userObject){
        return userService.create(userObject);
    }

}
