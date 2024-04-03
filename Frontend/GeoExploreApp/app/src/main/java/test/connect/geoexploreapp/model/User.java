package test.connect.geoexploreapp.model;

import java.io.Serializable;

public class User  implements Serializable {
    private Long id;
    private String name;
    private String emailId;
    private String password;
    private String encryptedPassword;
    private boolean isAdmin;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {

        this.isAdmin = isAdmin;
    }

    @Override
    public String toString(){
        return "{ name: " + this.name +
                "\nemailId: " + this.emailId +
                "\nPassword: " + this.password +
                "\nisAdmin: " + this.getIsAdmin();
    }


}
