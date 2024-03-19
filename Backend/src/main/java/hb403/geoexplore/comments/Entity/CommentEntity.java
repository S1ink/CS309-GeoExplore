package hb403.geoexplore.comments.Entity;

import hb403.geoexplore.UserStorage.entity.User;

public class CommentEntity {

    private Long id;
    private String comment;
    private int likes;
    public CommentEntity(Long Userid, String comment, int likes){
    this.id = Userid;
    this.comment = comment;
    this.likes = likes;
    }

}
