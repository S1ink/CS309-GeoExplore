package hb403.geoexplore.comments.Entity;

import hb403.geoexplore.UserStorage.entity.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Comment")
public class CommentEntity {

    @OneToMany(mappedBy = "id", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<User> user;
    /*@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<ObservationEntity> observationEntities;*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentid;
    private String userEmailid;
    private String comment;
    private Long postid;

    //Constuctor that acts as more of a commenter object
    public CommentEntity(String Userid, Long postid){
        this.userEmailid = Userid;
        this.postid = postid;
    }


    private int likes;
    public CommentEntity(CommentEntity commentor, String comment){//likes can be added later

    this.postid = commentor.getPostid();
    this.userEmailid = commentor.getUserid();
    this.comment = comment;

    //this.likes = likes; //not used yet
    }

    //not going to be used
    public CommentEntity() {

    }

    public List<User> getUser() {
        return user;
    }
    public String getUserbyid(Long id){
        int found = 0;
        for (int i = 0; i < user.size() - 1; i++){
            if (user.get(i).getId().equals(id)) {
                found = i;
                break;
            }
        }
        return user.get(found).getEmailId();
    }


    public void setId(Long id) {
        this.commentid = id;
    }

    public Long getId() {
        return commentid;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public Long getPostid() {
        return postid;
    }

    public String getUserid() {
        return userEmailid;
    }

    public void setUserid(String userid) {
        this.userEmailid = userid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

}
