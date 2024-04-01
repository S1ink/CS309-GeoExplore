package test.connect.geoexploreapp.model;

import java.io.IOException;
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
    private User confirmed_by;
    private String address;

    public Observation(){
        this.time_created = new Date();
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
    public Long getPostID() {
        return id;
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


    public User getConfirmed_by() {
        return confirmed_by;
    }

    public void setConfirmed_by(User confirmed_by) {
        this.confirmed_by = confirmed_by;
    }

}
