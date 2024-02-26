package test.connect.geoexploreapp.model;

public class User {
    private int id;
    private String name;
    private String emailId;
    private String password;
    private String encryptedPassword;
    private boolean ifAdmin;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isIfAdmin() {
        return ifAdmin;
    }

    public void setIfAdmin(boolean ifAdmin) {
        this.ifAdmin = ifAdmin;
    }

}
