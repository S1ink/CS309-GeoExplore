package hb403.geoexplore.comments.Entity;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Comment")
public class CommentEntity {

    @OneToMany(mappedBy = "id", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<User> user;
    /*@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<ObservationEntity> observationEntities;*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long commentid;

    private Long postid;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = { CascadeType.ALL }
    )
    @JoinTable(
            name = "comments",		// the name of the intermediate table that links users and groups (NEW)
            joinColumns = {
                    @JoinColumn(
                            name = "commentBody",		// the name of the column in the intermediate table that links to the primary key (NEW)
                            referencedColumnName="comment"	// the name of the column in the owning entity table that this column links to (REFERENCED)
                    )

            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "postid_linked",		// the name of the column in the intermediate table that links to the non-owning key (NEW)
                            referencedColumnName="marker_id"	// the name of the column in the non-owning entity table for which this column links to (REFERENCED)
                    )
            }
    )
    private HashSet<ObservationMarker> posts = new HashSet<>();
    /*@JoinTable(
            name = "comments",		// the name of the intermediate table that links users and groups (NEW)
            joinColumns = {
                    @JoinColumn(
                            name = "commentBody",		// the name of the column in the intermediate table that links to the primary key (NEW)
                            referencedColumnName="comment"	// the name of the column in the owning entity table that this column links to (REFERENCED)
                    )

            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "userid_linked",		// the name of the column in the intermediate table that links to the non-owning key (NEW)
                            referencedColumnName="emailId"	// the name of the column in the non-owning entity table for which this column links to (REFERENCED)
                    )
            }

    )
    private HashSet<User> users = new HashSet<>();*/
    private String comment;
    private String userEmailid;

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
    //used for updating
    public CommentEntity(Long CommentId,Long PostId, String UserId, String comment){//likes can be added later
        this.commentid = CommentId;
        this.postid = PostId;
        this.userEmailid = UserId;
        this.comment = comment;

        //this.likes = likes; //not used yet
    }

    //not going to be used
    public CommentEntity() {

    }

   /* public List<User> getUser() {
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
    }*/


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
