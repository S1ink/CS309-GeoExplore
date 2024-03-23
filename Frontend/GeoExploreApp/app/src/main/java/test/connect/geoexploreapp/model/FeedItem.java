package test.connect.geoexploreapp.model;

import java.io.IOException;
import java.util.Date;

public interface FeedItem {
    double getLatitude();
    double getLongitude();
    String getTitle();
    String getDescription();
    String getType();
    String getDepartment();
    Date getDate();
    String getLocation();


}
