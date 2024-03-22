package test.connect.geoexploreapp.model;

import java.util.Date;

public class EventMarker implements FeedItem{
    private long id;
    private double latitude;
    private double longitude;
    private String title;
    private String city_department;
    private Date date;

    public EventMarker() {
        this.date = new Date();
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
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
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date= date;
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


}
