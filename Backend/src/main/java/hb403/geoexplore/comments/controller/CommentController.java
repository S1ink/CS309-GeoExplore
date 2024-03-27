package hb403.geoexplore.comments.controller;


import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


}
