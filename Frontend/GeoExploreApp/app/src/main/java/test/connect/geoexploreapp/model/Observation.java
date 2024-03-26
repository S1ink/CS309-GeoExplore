package test.connect.geoexploreapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Observation implements FeedItem{
    public Long id;
    public double latitude;
    public double longitude;

    public String title;
    public String description;
    private Date date;
    private String address;
    private List<Comment> comments;

    public Observation(){
        this.date = new Date();
        this.comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return "Observation";
    }

    @Override
    public String getDepartment() {
        return null;
    }
    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getLocation()  {
        return address;
    }

    @Override
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments= comments;
    }

    public void setLocation(String address)  {
        this.address =address;
    }

    public void setDate(Date date) {
        this.date= date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
