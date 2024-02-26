package test.connect.geoexploreapp.model;

public class EventMarker {
    private long id;
    private double latitude;
    private double longitude;
    private String title;

    public EventMarker() {
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

    public String getEventTitle() {
        return title;
    }

    public void setEventTitle(String eventTitle) {
        this.title = eventTitle;
    }


}
