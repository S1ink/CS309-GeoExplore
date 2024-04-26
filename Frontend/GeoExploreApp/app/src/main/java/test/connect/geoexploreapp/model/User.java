package test.connect.geoexploreapp.model;

import java.io.Serializable;

public class User  implements Serializable {
    private Long id;
    private String name;
    private String emailId;
    private String password;
    private String encryptedPassword;
    private boolean isAdmin;
    private double io_latitude;
    private double io_longitude;
    private String last_location_update;
    private LocationPrivacy location_privacy;

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

    public double getIo_latitude() {
        return io_latitude;
    }

    public void setIo_latitude(double io_latitude) {
        this.io_latitude = io_latitude;
    }

    public double getIo_longitude() {
        return io_longitude;
    }

    public void setIo_longitude(double io_longitude) {
        this.io_longitude = io_longitude;
    }

    public String getLast_location_update() {
        return last_location_update;
    }

    public void setLast_location_update(String last_location_update) {
        this.last_location_update = last_location_update;
    }

    public LocationPrivacy getLocation_privacy() {
        return location_privacy;  // Getter for location privacy
    }

    public void setLocation_privacy(LocationPrivacy location_privacy) {
        this.location_privacy = location_privacy;  // Setter for location privacy
    }

    @Override
    public String toString() {
        return "{ name: " + this.name +
                "\nemailId: " + this.emailId +
                "\nPassword: " + this.password +
                "\nisAdmin: " + this.getIsAdmin() +
                "\nLocation Privacy: " + this.location_privacy + " }";
    }

}
