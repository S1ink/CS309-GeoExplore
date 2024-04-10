package hb403.geoexplore.UserStorage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "reported_users")
@Getter
@Setter
public class ReportedUser {
    //A reported user should basically just have a relationship that can delete a user from this table
    // , and it will have the same list of comments that show up under any user it will also have a counter
    // for how many times they have been reported and the reason they were reported.
    //onetoone userid -> reported user id
    //onetoone usercomments -> reportedusercomments JsonIgnore
    //number of reports (controlled by controller) JsonIgnore
    //Reason for report -> (controlled by controller) ((could be a list of strings or booleans for multiple reasons))
    /*
    Example Json format for post
    {
    "reportedUserId" : 1
    "Reason" : "{harrassment, language, etc}"
    }
     */


    @Id
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id")
    private User reportedUser;



    //private Set<String> Reasons = new Set<String>() {


    @Getter
    @Setter
    private int numReports = 0;


    //for post, put will just edit the original and resave it


    public ReportedUser(){} //no arg constructor



}
