package hb403.geoexplore.comments.controller;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.entity.UserGroup;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;

import hb403.geoexplore.comments.ObservationCommentWebsocket;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.repository.ObservationRepository;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.*;


@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    public static ObservationRepository observationRepository;
    @Autowired
    public void autoObservationRepository(ObservationRepository repo) {
        CommentController.observationRepository = repo;
    }
    @Autowired
    UserRepository userRepository;
    
    //C of Crudl
    @Operation(summary = "Add a new comment to the database")
    @PostMapping(path = "/comment/store")
    public @ResponseBody CommentEntity commentStore (@RequestBody CommentEntity newComment){

        //CommentEntity newComment = new CommentEntity("emessmer", (long)53, "How do you do this?");
        commentRepository.save(newComment);
        System.out.print(newComment);
        //System.out.println(newComment);
        return newComment;
    }
    //R of Crudl
    @Operation(summary = "Get a comment from the database by its id")
    @GetMapping(path = "/comment/{id}")
    public @ResponseBody CommentEntity getComment(@PathVariable Long id){
        CommentEntity found = commentRepository.getById(id);
        //System.out.println(found.getPostIds());
        return found;
    }

    //U of Crudl
    @Operation(summary = "Update a comment already in the database by its id")
    @PutMapping(path = "/comment/{id}/update")
    public @ResponseBody CommentEntity updateComment(@PathVariable Long id, @RequestBody CommentEntity updated){
        CommentEntity updater = commentRepository.getById(id);
        CommentEntity update = new CommentEntity(id, updated.getPostId(), updated.getUserId(), updated.getPostType(), updated.getComment());
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
    //List of all comments under a specific Observation
    @Operation(summary = "Gets a list of all comments for a specific observation")
    @GetMapping(path = "/observation/comments/{postId}")
    @ResponseBody
    public List<CommentEntity> getCommentsForObs(@PathVariable Long postId){
        List<CommentEntity> getAllComments = commentRepository.findAll();
        ArrayList<CommentEntity> commentEntitiesOnPost = new ArrayList<CommentEntity>();
        for (int i = 0; i < getAllComments().size();i++) {
            if (getAllComments.get(i).getPostType().equals("Observation")){
                if(getAllComments.get(i).getPostId().equals(postId));
                commentEntitiesOnPost.add(getAllComments.get(i));
            }
        }
        return commentEntitiesOnPost;
    }
    @Operation(summary = "Gets a list of all comments for a specific event")
    @GetMapping(path = "/event/comments/{postId}")
    @ResponseBody
    public List<CommentEntity> getCommentsForEvents(@PathVariable Long postId){
        List<CommentEntity> getAllComments = commentRepository.findAll();
        ArrayList<CommentEntity> commentEntitiesOnPost = new ArrayList<CommentEntity>();
        for (int i = 0; i < getAllComments().size();i++) {
            if (getAllComments.get(i).getPostType().equals("Event")){
                if(getAllComments.get(i).getPostId().equals(postId));
                commentEntitiesOnPost.add(getAllComments.get(i));
            }
        }
        return commentEntitiesOnPost;
    }


    @Operation(summary = "Gets a list of all comments for a specific event")
    @GetMapping(path = "/report/comments/{postId}")
    @ResponseBody
    public List<CommentEntity> getCommentsForReports(@PathVariable Long postId){
        List<CommentEntity> getAllComments = commentRepository.findAll();
        ArrayList<CommentEntity> commentEntitiesOnPost = new ArrayList<CommentEntity>();
        for (int i = 0; i < getAllComments().size();i++) {
            if (getAllComments.get(i).getPostType().equals("Report")){
                if(getAllComments.get(i).getPostId().equals(postId));
                commentEntitiesOnPost.add(getAllComments.get(i));
            }
        }
        return commentEntitiesOnPost;
    }

}
