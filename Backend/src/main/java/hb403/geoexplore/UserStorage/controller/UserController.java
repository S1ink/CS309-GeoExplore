package hb403.geoexplore.UserStorage.controller;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/userinfo")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/user/create")
    public String UserCreate(@RequestBody User newUser){
        //User newUser = new User(name, username, password);
        userRepository.save(newUser);
        return newUser.toString();
    }

}
