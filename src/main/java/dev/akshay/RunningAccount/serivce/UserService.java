package dev.akshay.RunningAccount.serivce;

import dev.akshay.RunningAccount.domain.User;
import dev.akshay.RunningAccount.domain.UserObject;
import dev.akshay.RunningAccount.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void splitName(String fullName, User user){
        String parts[] = fullName.split(" ");
        user.setFirstName(parts[0]);
        if(parts.length == 2){
            user.setLastName(parts[1]);
        }
        if(parts.length == 3){
            user.setMiddleName(parts[1]);
            user.setLastName(parts[2]);
        }
    }

    public ResponseEntity createUser(UserObject userObject){
        User user = new User();
        String fullName = userObject.getFullName();
        splitName(fullName, user);
        user.setDob(userObject.getDob());
        user.setAddress(userObject.getAddress());
        userRepository.save(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
}