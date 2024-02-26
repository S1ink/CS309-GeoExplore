package hb403.geoexplore.UserStorage.controller;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {



    @Autowired
    UserRepository userRepository;

    //C of Crudl
    @PostMapping(path = "/user/create")
    public @ResponseBody String UserCreate(@RequestBody User newUser){
        //User newUser = new User(name, username, password);
        userRepository.save(newUser);
        return newUser.toString();
    }


    //R of Crudl
    @GetMapping(path = "/user/{id}")
    Optional<User> getUser(@PathVariable Integer id){
        return userRepository.findById(id);
    }

    //U of Crudl
    @PutMapping(path = "/user/{id}/update")
    public @ResponseBody User updateUser(@PathVariable Integer id){
        User updater =  userRepository.findById(id).get();

        return new User();
    }


    @DeleteMapping(path = "/user/{id}/")

    @GetMapping(path = "/userinfo")
    @ResponseBody List<User>  getAllUsers() {
        return userRepository.findAll();
    }


}
