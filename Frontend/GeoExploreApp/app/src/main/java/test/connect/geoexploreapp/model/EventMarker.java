package test.connect.geoexploreapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventMarker implements FeedItem{
    private long id;
    private double latitude;
    private double longitude;
    private User creator;
    private String title;
    private String city_department;
    private Date time_created;
    private Date time_updated;
    private String meta;
    private List<MarkerTag> tags;
    //    private String address;
    private List<Comment> comments;

    public EventMarker() {
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.time_created = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getIo_latitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getIo_longitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
        return "Event";
    }

    @Override
    public String getDepartment() {
        return city_department;
    }

    @Override
    public Date getTime_created() {
        return time_created;
    }

    @Override
    public Long getPostID() {
        return id;
    }


//    public void setLocation(String address)  {
//        this.address = address;
//    }

    @Override
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity_department() {
        return city_department;
    }

    public void setCity_department(String city_department) {
        this.city_department = city_department;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(Date time_updated) {
        this.time_updated = time_updated;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public List<MarkerTag> getTags() {
        return tags;
    }

    public void setTags(List<MarkerTag> tags) {
        this.tags = tags;
    }
}
