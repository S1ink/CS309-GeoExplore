package hb403.geoexplore.UserStorage.controller;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.entity.UserGroup;
import hb403.geoexplore.UserStorage.repository.UserRepository;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    //C of Crudl
    @Operation(summary = "Add a new user to the database")
    @PostMapping(path = "/user/create")
    public @ResponseBody User UserCreate(@RequestBody User newUser){
        if(newUser != null) {
            User newestUser = new User(newUser.getName(), newUser.getEmailId(), newUser.getPassword());
            userRepository.save(newestUser);
            return newestUser;
        }
        return null;
    }


    //R of Crudl
    @Operation(summary = "Get a user from the database from its id")
    @GetMapping(path = "/user/{id}")
    public @ResponseBody User getUser(@PathVariable Long id){
        if(id != null) {
            try {
                return userRepository.findById(id).get();
            } catch(Exception e) {

            }
        }
        return null;
    }

    //U of Crudl
    @Operation(summary = "Update a user already in the database by its id")
    @PutMapping(path = "/user/{id}/update")
    public @ResponseBody User updateUser(@PathVariable Long id, @RequestBody User updated){
        if(id != null && updated != null) {
            userRepository.deleteById(id);
            User updater = new User(id, updated.getName(), updated.getEmailId(), updated.getPassword());
            userRepository.save(updater);
            return updater;
        }
        return null;
    }

    // D of Crudl
    @Operation(summary = "Delete a user from the database by its id")
    @DeleteMapping(path = "/user/{id}/delete")
    public @ResponseBody String deleteUser(@PathVariable Long id){
        if(id != null) {
            try {
                User deleted = userRepository.findById(id).get();
                userRepository.deleteById(id);
                return "Successfully deleted: \n" + deleted.toString();
            } catch(Exception e) {

            }
        }
        return null;
    }



    //L of Crudl
    @Operation(summary = "Get a list of all the users in the database")
    @GetMapping(path = "/userinfo")
    @ResponseBody List<User>  getAllUsers() {
        return userRepository.findAll();
    }

    // Get a list of groups that a user is in
    @Operation(summary = "Get the list of usergroups that a user is a member in")
    @GetMapping(path="/user/{id}/groups")
    @ResponseBody Set<UserGroup> getUserGroups(@PathVariable Long id) {
        try {
            return this.getUser(id).getGroups();
        } catch(Exception e) {
            // ...
        }
        return null;
    }


}
