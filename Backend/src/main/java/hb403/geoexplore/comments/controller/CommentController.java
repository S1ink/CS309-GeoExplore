package hb403.geoexplore.comments.controller;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.entity.UserGroup;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    //C of Crudl
    @Operation(summary = "Add a new comment to the database")
    @PostMapping(path = "/comment/store")
    public @ResponseBody CommentEntity commentStore (@RequestBody CommentEntity newComment){
        commentRepository.save(newComment);
        return newComment;
    }
    //R of Crudl
    @Operation(summary = "Get a comment from the database by its id")
    @GetMapping(path = "/comment/{id}")
    public @ResponseBody CommentEntity getComment(@PathVariable Long id){
        CommentEntity deleted = commentRepository.getById(id);
        commentRepository.deleteById(id);
        return deleted;
    }

    //U of Crudl
    @Operation(summary = "Update a comment already in the database by its id")
    @PutMapping(path = "/comment/{id}/update")
    public @ResponseBody CommentEntity updateComment(@PathVariable Long id, @RequestBody CommentEntity updated){
        //CommentEntity updater = commentRepository.getById(id);
        CommentEntity updater = new CommentEntity(id, updated.getPostid(), updated.getUserid(), updated.getComment());
        commentRepository.save(updater);
        return updated;
    }


    @Operation(summary = "Delete a comment from the database by its id")
    @DeleteMapping(path = "/comment/{id}/delete")
    public @ResponseBody String deleteComment(@PathVariable Long id){
        CommentEntity deleted = commentRepository.findById(id).get();
        commentRepository.deleteById(id);
        return "Successfully deleted: \n" + deleted.toString();
    }



    //L of Crudl (won't be used probably)
    @Operation(summary = "Get a list of all the comments in the database")
    @GetMapping(path = "/comment/list")
    @ResponseBody
    List<CommentEntity> getAllComments() {
        return commentRepository.findAll();
    }
}
