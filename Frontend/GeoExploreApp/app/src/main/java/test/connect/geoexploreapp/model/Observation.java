package test.connect.geoexploreapp.model;

import java.util.Date;

public class Observation implements FeedItem{
    public Long id;
    public double latitude;
    public double longitude;

    public String title;
    public String description;
    private Date date;

    public Observation(){
        this.date = new Date();
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

    public void setDate(Date date) {
        this.date= date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
