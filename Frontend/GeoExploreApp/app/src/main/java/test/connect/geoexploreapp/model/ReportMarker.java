package test.connect.geoexploreapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportMarker implements FeedItem{

    private long id;

    private double longitude;

    private double latitude;

    private String title;
    private Date date;
    private String address;

    private List<Comment> comments;

    public ReportMarker() {
        this.date = new Date();
        this.comments = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getType() {
        return "Report";
    }

    @Override
    public String getDepartment() {
        return null;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date= date;
    }

    public void setTitle(String title) {
        this.title = title;
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
        this.comments=comments;
    }

    public void setLocation(String address)  {
        this.address =address;
    }



}
