package test.connect.geoexploreapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportMarker implements FeedItem{

    private long id;
    private String title;
    private double latitude;
    private double longitude;
    private User creator;
    private Date time_created;
    private Date time_updated;
    private String meta;
    private List<MarkerTag> tags;
    private List<User> confirmed_by;
    private String address;
    private List<Comment> comments;

    public ReportMarker() {
        this.time_created = new Date();
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
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
    public Date getTime_created() {
        return time_created;
    }

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }
    @Override
    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getLocation()  {
        return address;
    }

    @Override
    public Long getPostID() {
        return id;
    }

    public void setLocation(String address)  {
        this.address =address;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getConfirmed_by() {
        return confirmed_by;
    }

    public void setConfirmed_by(List<User> confirmed_by) {
        this.confirmed_by = confirmed_by;
    }
}

