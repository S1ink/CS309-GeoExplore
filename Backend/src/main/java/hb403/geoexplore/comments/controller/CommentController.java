package hb403.geoexplore.comments.controller;


import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.entity.UserGroup;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    //C of Crudl
    @PostMapping(path = "/comment/store")
    public @ResponseBody CommentEntity commentStore (@RequestBody CommentEntity newComment){
        commentRepository.save(newComment);
        return newComment;
    }
    //R of Crudl
    /*@GetMapping(path = "/user/{id}")
    public @ResponseBody User getUser(@PathVariable Long id){

        return null;
    }

    //U of Crudl
    @PutMapping(path = "/user/{id}/update")
    public @ResponseBody User updateUser(@PathVariable Long id, @RequestBody User updated){
        //commentRepository.deleteById(id);
        User updater = new User(id, updated.getName(), updated.getEmailId(), updated.getPassword());
        //commentRepository.save();
        return updater;
    }


    @DeleteMapping(path = "/user/{id}/delete")
    public @ResponseBody String deleteUser(@PathVariable Long id){
        User deleted = commentRepository.findById(id).get();
        commentRepository.deleteById(id);
        return "Successfully deleted: \n" + deleted.toString();
    }

    @DeleteMapping(path = "user/delete/all")
    public @ResponseBody String deleteAll(){
        commentRepository.deleteAll();
        return "Successfully deleted all users";
    }



    //L of Crudl
    @GetMapping(path = "/userinfo")
    @ResponseBody
    List<User> getAllUsers() {
        return commentRepository.findAll();
    }

    @GetMapping(path="/user/{id}/groups")
    @ResponseBody
    Set<UserGroup> getUserGroups(@PathVariable Long id) {
        try {
            return this.getUser(id).getGroups();
        } catch(Exception e) {
            // ...
        }
        return null;
    }
*/
}
