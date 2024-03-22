package test.connect.geoexploreapp.model;

public class ReportMarker implements FeedItem{

    private long id;

    private double longitude;

    private double latitude;

    private String title;
    public ReportMarker() {
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

    public void setTitle(String title) {
        this.title = title;
    }



}
