package hb403.geoexplore.comments.controller;


import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    @Autowired
    CommentRepository commentRepository;


}
