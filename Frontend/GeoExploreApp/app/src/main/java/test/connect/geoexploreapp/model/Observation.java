package test.connect.geoexploreapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Observation implements FeedItem{
    private Long id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private User creator;
    private Date time_created;
    private Date time_updated;
    private String meta;
    private List<MarkerTag> tags;
    private User confirmed_by;
    private String address;
    private List<Comment> comments;

    public Observation(){
        this.time_created = new Date();
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
    public Date getTime_created() {
        return time_created;
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

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
