package test.connect.geoexploreapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.AlertMarker;

public interface AlertMarkerApi {

    // c[R]udl - Get an alert from the database by its id
    @GET("geomap/alerts/{id}")
    Call<AlertMarker> getAlertById(@Path("id") Long id);

    // crud[L] - Get a list of all the alerts in the database
    @GET("geomap/alerts")
    Call<List<AlertMarker>> getAllAlertMarker();

    // cru[D]l - Delete an alert in the database by it's id
    @DELETE("geomap/alerts/{id}")
    Call<Void> deleteAlertById(@Path("id") Long id);
}
