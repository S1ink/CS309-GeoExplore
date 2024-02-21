package hb403.geoexplore.UserStorage.entity;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String emailId;

    private String password;
    private String encryptedPassword;
    private boolean ifActive;

    public User(String name, String emailId, String password) {
//        this.id = id;
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.ifActive = true;
        encryptPassword();
    }

    public User() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
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


    public boolean getIsActive(){
        return ifActive;
    }

    public void setIfActive(boolean ifActive){
        this.ifActive = ifActive;
    }

    @Override
    public String toString(){
        return "Name: " + name +
                "\nusername: " + emailId +
                "\nPassword: " + encryptedPassword +
                "\nSuccessfully created";
    }
}
