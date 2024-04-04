package hb403.geoexplore.comments.controller;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;


import hb403.geoexplore.datatype.marker.EventMarker;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.ReportMarker;
import hb403.geoexplore.datatype.marker.repository.EventRepository;
import hb403.geoexplore.datatype.marker.repository.ObservationRepository;
import hb403.geoexplore.datatype.marker.repository.ReportRepository;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;
    public static ObservationRepository observationRepository;
    public static ReportRepository reportRepository;
    public static EventRepository eventRepository;
    @Autowired
    public void autoObservationRepository(ObservationRepository repo) {
        CommentController.observationRepository = repo;
    }
    @Autowired
    public void autoReportRepository(ReportRepository repo) {
        CommentController.reportRepository = repo;
    }
    @Autowired
    public void autoEventRepository(EventRepository repo) {
        CommentController.eventRepository = repo;
    }


    //C of Crudl
    @Operation(summary = "Add a new comment to the database")
    @PostMapping(path = "/comment/store/{postType}") //Observation, Event, or Report with capital
    public @ResponseBody CommentEntity commentStore (@RequestBody CommentEntity newComment, @PathVariable String postType){
        CommentEntity toSave = new CommentEntity(newComment.getUserId(),newComment.getPostId(), postType , newComment.getComment());
        // List<User> templist =  userRepository.findAll();
        // Long userId;
        // templist.forEach(user -> {
        //     if (toSave.getUserId().equals(user.getEmailId())) {
        //         toSave.setUserTableId(user.getId());
        //     }
        // });
        commentRepository.save(toSave);

            try {
                    if (postType.equals("Observation")) {
                        final ObservationMarker tempObservation = this.observationRepository.findById(newComment.getPostId()).get();
                        tempObservation.getComments().add(toSave);
                        toSave.setPertainsObservationMarker(tempObservation);
                        observationRepository.save(tempObservation);
                    }
                    else if (postType.equals("Report")){
                        final ReportMarker tempReport = this.reportRepository.findById(newComment.getPostId()).get();
                        tempReport.getComments().add(toSave);
                        toSave.setPertainsReportMarker(tempReport);
                        reportRepository.save(tempReport);
                    }
                    else if(postType.equals("Event")){
                        final EventMarker tempEvent = this.eventRepository.findById(newComment.getPostId()).get();
                        tempEvent.getComments().add(toSave);
                        toSave.setPertainsEventMarker(tempEvent);
                        eventRepository.save(tempEvent);
                    }
                    else{
                        System.out.println("Post type not included");
                    }
                commentRepository.save(toSave);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        return newComment;
    }
    //R of Crudl
    @Operation(summary = "Get a comment from the database by its id")
    @GetMapping(path = "/comment/{id}")
    public @ResponseBody CommentEntity getComment(@PathVariable Long id){
        return commentRepository.getById(id);
    }

    //U of Crudl
    @Operation(summary = "Update a comment already in the database by its id")
    @PutMapping(path = "/comment/{id}/update")
    public @ResponseBody CommentEntity updateComment(@PathVariable Long id, @RequestBody CommentEntity updated){
        try {CommentEntity updater = commentRepository.getById(id); //making sure the comment exists in the repository
            CommentEntity update = new CommentEntity(id, updated.getPostId(), updater.getUserId(), updater.getPostType(), updated.getComment() + " \n(comment edited)");
            commentRepository.save(update);
            if (update.getPostType().equals("Observation")) {
                final ObservationMarker tempObservation = observationRepository.findById(update.getPostId()).get();
                /*List<CommentEntity> commentEntityList = tempObservation.getComments();
                commentEntityList.forEach(commentEntity -> {
                    if (updater.equals()){
                        commentEntityList.remove(commentEntity);
                    }
                });*/
                update.setPertainsObservationMarker(tempObservation);
                observationRepository.save(tempObservation);
            }
            else if (update.getPostType().equals("Report")){
                final ReportMarker tempReport = reportRepository.findById(update.getPostId()).get();
                tempReport.getComments().add(update);

                reportRepository.save(tempReport);
            }
            else if(update.getPostType().equals("Event")){
                final EventMarker tempEvent = eventRepository.findById(update.getPostId()).get();
                tempEvent.getComments().add(update);
                update.setPertainsEventMarker(tempEvent);
                eventRepository.save(tempEvent);
            }
            }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
        return updated;
    }



    @Operation(summary = "Delete a comment from the database by its id")
    @DeleteMapping(path = "/comment/{id}/delete")
    public @ResponseBody String deleteComment(@PathVariable Long id){
        CommentEntity deleted = commentRepository.findById(id).get();
        commentRepository.deleteById(id);

        if (deleted.getPostType().equals("Observation")) {
            ObservationMarker deleteFromList = observationRepository.findById(deleted.getPostId()).get();
            List<CommentEntity> listToDelete = deleteFromList.getComments();
            AtomicInteger i = new AtomicInteger();
            listToDelete.forEach(comment -> {
                if (comment.equals(deleted)){
                    i.getAndIncrement();
                    listToDelete.remove(i.get());
                }

            });
        }
        else if (deleted.getPostType().equals("Event")) {
            EventMarker deleteFromList = eventRepository.findById(deleted.getPostId()).get();
            List<CommentEntity> listToDelete = deleteFromList.getComments();
            AtomicInteger i = new AtomicInteger();
            listToDelete.forEach(comment -> {
                if (comment.equals(deleted)){
                    i.getAndIncrement();
                    listToDelete.remove(i.get());
                }

            });
        }
        else if (deleted.getPostType().equals("Report")){
            ReportMarker deleteFromList = reportRepository.findById(deleted.getPostId()).get();
            List<CommentEntity> listToDelete = deleteFromList.getComments();
            AtomicInteger i = new AtomicInteger();
            listToDelete.forEach(comment -> {
                if (comment.equals(deleted)){
                    i.getAndIncrement();
                    listToDelete.remove(i.get());
                }

            });
        }
        else {
            return "deleted from comment repo but not post";
        }

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
        try{
            List<CommentEntity> CommentsOnObs = observationRepository.getById(postId).getComments();
            return CommentsOnObs;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    @Operation(summary = "Gets a list of all comments for a specific event")
    @GetMapping(path = "/event/comments/{postId}")
    @ResponseBody
    public List<CommentEntity> getCommentsForEvents(@PathVariable Long postId){
        /*List<CommentEntity> getAllComments = commentRepository.findAll();
        ArrayList<CommentEntity> commentEntitiesOnPost = new ArrayList<CommentEntity>();
        for (int i = 0; i < getAllComments().size();i++) {
             if (getAllComments.get(i).getPostType().equals("Event")){
                if(getAllComments.get(i).getPostId().equals(postId)) {
                    commentEntitiesOnPost.add(getAllComments.get(i));
                }
            }

        }*/
        try{
            List<CommentEntity> CommentsOnEvent = eventRepository.getById(postId).getComments();
            return CommentsOnEvent;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    @Operation(summary = "Gets a list of all comments for a specific user using their id in the table (LONG)")
    @GetMapping(path = "/user/comments/{user_table_Id}")
    @ResponseBody
    public List<CommentEntity> getCommentsForUser(@PathVariable Long user_table_Id){
        return userRepository.getById(user_table_Id).getComments();
    }

    @Operation(summary = "Gets a list of all comments for a specific event")
    @GetMapping(path = "/report/comments/{postId}")
    @ResponseBody
    public List<CommentEntity> getCommentsForReports(@PathVariable Long postId){
        List<CommentEntity> getAllComments = commentRepository.findAll();
        //List<CommentEntity> =
        /*for (int i = 0; i < getAllComments().size();i++) {
             if (getAllComments.get(i).getPostType().equals("Report")){
                if(getAllComments.get(i).getPostId().equals(postId)) {
                    commentEntitiesOnPost.add(getAllComments.get(i));
                }
            }
        }*/
        try{
            List<CommentEntity> CommentsOnReport = reportRepository.getById(postId).getComments();
        }catch(Exception e){

        }
        return reportRepository.getById(postId).getComments();
    }

}
