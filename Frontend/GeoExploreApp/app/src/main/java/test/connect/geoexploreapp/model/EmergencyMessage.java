package test.connect.geoexploreapp.model;

public class EmergencyMessage {
    private String title;
    private String message;
    private double latitude;
    private double longitude;

    public EmergencyMessage(String title, String message, double latitude, double longitude){
        this.title = title;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
