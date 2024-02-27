package hb403.geoexplore.UserStorage.entity;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "user", schema = "testDB")
public class User {
    //todo as of saturday, Work on this to be finished and start the observation request using this as a
    // framework but start outside of git so it won't be too annoying
    /*
    Input for creating user {

        "name": "Ethan",
        "emailId": "emessmer@iastate.edu",
        "password": "password"

    }
     */




    @Basic

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String emailId;

    //String[] adminList = {"emessmer@iastate.edu","aditin@iastate.edu" ,"samr888@iastate.edu","yharb@iastate.edu"};


    private String password;
    private String encryptedPassword;
    private boolean isAdmin;
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
