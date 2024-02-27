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
    public @ResponseBody User UserCreate(@RequestBody User newUser){
        User newestUser = new User(newUser.getName(), newUser.getEmailId(), newUser.getPassword());
        userRepository.save(newestUser);
        return newestUser;
    }


    //R of Crudl
    @GetMapping(path = "/user/{id}")
    public @ResponseBody User getUser(@PathVariable Long id){

        return userRepository.findById(id).get();
    }

    //U of Crudl
    @PutMapping(path = "/user/{id}/update")
    public @ResponseBody User updateUser(@PathVariable Long id, @RequestBody User updated){
        userRepository.deleteById(id);
        User updater = new User(id, updated.getName(), updated.getEmailId(), updated.getPassword());
        userRepository.save(updater);
        return updater;
    }


    @DeleteMapping(path = "/user/{id}/delete")
    public @ResponseBody String deleteUser(@PathVariable Long id){
        User deleted = userRepository.findById(id).get();
        userRepository.deleteById(id);
        return "Successfully deleted: \n" + deleted.toString();
    }

    @DeleteMapping(path = "user/delete/all")
    public @ResponseBody String deleteAll(){
        userRepository.deleteAll();
        return "Successfully deleted all users";
    }



    //L of Crudl
    @GetMapping(path = "/userinfo")
    @ResponseBody List<User>  getAllUsers() {
        return userRepository.findAll();
    }


}
