package hb403.geoexplore.UserStorage.entity;

import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    /*
    Input for creating user {

        "name": "Ethan",
        "emailId": "emessmer@iastate.edu",
        "password": "password"

    }
     */
    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // probably use UUID after we are done testing
    @Column(name = "user_id")
    private Long id;
    private String name;
    @Column(name = "email_id")
    private String emailId;

    //String[] adminList = {"emessmer@iastate.edu","aditin@iastate.edu" ,"samr888@iastate.edu","yharb@iastate.edu"};


    private String password;
    private String encryptedPassword;
    private boolean isAdmin;

    /*@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "posts")
    @JsonIgnore
    private Set<CommentEntity> comments = new HashSet<>();*/

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "members")
    @JsonIgnore
    private Set<UserGroup> groups = new HashSet<>();


    @Getter
    @Setter
    @OneToMany(mappedBy = "pertainsUser", fetch = FetchType.EAGER)
    private List<CommentEntity> Comments;

    public User(Long id, String name, String emailId, String password) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;
        this.password = password;

        checkIfAdmin();
        encryptPassword();
    }

    public User(String name, String emailId, String password) {
//        this.id = id;
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        checkIfAdmin();
        encryptPassword();
    }

    public User() {

    }


    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void encryptPassword(){
        ArrayList<Character> encryption = new ArrayList<Character>();
        for (int i = 0; i < password.length();i++){
            encryption.add('*');
        }
        encryptedPassword = encryption.toString();
    }


    public boolean getIsAdmin(){
        return isAdmin;
    }

    public void checkIfAdmin(){
        ArrayList<String> adminList = new ArrayList<>();
        adminList.add("emessmer@iastate.edu");
        adminList.add("samr888@iastate.edu");
        adminList.add("aditin@iastate.edu");
        adminList.add("yharb@iastate.edu");
        //for (String s : adminList) {
            if (adminList.contains(emailId)) {
                this.setIsAdmin(true);
            }

    }
    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }


    @Override
    public String toString(){
        return "Name: " + this.name +
                "\nusername: " + this.emailId +
                "\nPassword: " + this.encryptedPassword +
                "\nSuccessfully created";
    }
}
