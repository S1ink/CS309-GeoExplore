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



    @GetMapping(path = "/userinfo")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/{id}")
    Optional<User> getUser(@PathVariable Integer id){
        return userRepository.findById(id);
    }



    @PostMapping(path = "/user/create")
    public String UserCreate(@RequestBody User newUser){
        //User newUser = new User(name, username, password);
        ArrayList<String> adminList = new ArrayList<>();
        adminList.add("emessmer@iastate.edu");
        adminList.add("aditin@iastate.edu");
        adminList.add("yharb@iastate.edu");
        adminList.add("samr888@iastate.edu");

        if (!adminList.contains(newUser.getEmailId())){
            newUser.setIfAdmin(false);
        }
        userRepository.save(newUser);
        return newUser.toString();
    }

}
