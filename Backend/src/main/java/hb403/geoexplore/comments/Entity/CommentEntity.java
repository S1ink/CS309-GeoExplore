package hb403.geoexplore.comments.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.entity.UserGroup;
import hb403.geoexplore.datatype.marker.EventMarker;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.ReportMarker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "CommentEntity")
@Getter
@Setter
public class CommentEntity {


        @Setter
        @Getter
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)     // probably use UUID after we are done testing
        @Column(name = "comment_id")
        private Long id;
        /*@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "PostIds")
        @JsonIgnore
        private Set<ObservationMarker> observations = new HashSet<>();*/
        /*
       @JsonIgnore
        @Column(name = "post_Id_linker")
        @OneToMany(mappedBy = "ObservationMarker")
        protected ArrayList<ObservationMarker> postLinker;*/
    @Getter
       @ManyToOne
       @JoinTable(
               name = "Observation_pertains",
               joinColumns = @JoinColumn(name = "comment_id"),
               inverseJoinColumns = @JoinColumn(name = "marker_id"))
       ObservationMarker pertainsObservationMarker;
    @Getter
    @ManyToOne
    @JoinTable(
            name = "Event_pertains",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "marker_id"))
    EventMarker pertainsEventMarker;
    @Getter
    @ManyToOne
    @JoinTable(
            name = "Report_pertains",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "marker_id"))
    ReportMarker pertainsReportMarker;


        @Getter
        @Setter
        //@Column(name = "post_id_linked")
        private Long postId;

        @Setter
        @Getter
        @Column
        private String userId;

        @Column
        private String comment;
        @Column
        private String postType;
        public CommentEntity(Long Commentid,Long PostID,  String userId,String type, String comment) {
        this.id = Commentid;
        this.postId = PostID;
        if (type.equals("Report") || type.equals("Observation") || type.equals("Event")) {
            this.postType = type;
        }
        this.userId = userId;
        this.comment= comment;
        }

        public CommentEntity(String adduserId, Long addpostid, String type, String Addcomment) {
//        this.id = id;
            this.userId = adduserId;
            this.postId = addpostid;
            this.postType = type;
            this.comment = Addcomment;

        }

        public CommentEntity( String Userid,Long postId) {
        this.userId = Userid;
        this.postId = postId;
        }

        public CommentEntity() {

        }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    @Override
        public String toString(){
            return "CommentId: " + this.id +
                    "\nUserId " + this.userId +
                    "\nPostId: " + this.postId +
                    "\nComment" + this.comment +
                    "\nSuccessfully created";
        }
    }


