package test.connect.geoexploreapp.model;

import java.util.List;

public class Comment {
    private List<User> user;
    /*@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<ObservationEntity> observationEntities;*/
    private Long commentid;
    private String userEmailid;
    private String comment;
    private Long postid;

    public Comment() {

    }

    public Comment(String comment, String userEmailid, Long postid) {
        this.comment = comment;
        this.userEmailid=userEmailid;
        this.postid=postid;
    }


    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Long getCommentid() {
        return commentid;
    }

    public void setCommentid(Long commentid) {
        this.commentid = commentid;
    }

    public String getUserEmailid() {
        return userEmailid;
    }

    public void setUserEmailid(String userEmailid) {
        this.userEmailid = userEmailid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }
}
