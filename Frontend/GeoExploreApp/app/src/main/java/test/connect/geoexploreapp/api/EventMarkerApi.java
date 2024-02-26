package test.connect.geoexploreapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.EventMarker;

public interface EventMarkerApi {

    @GET("geomap/events")
    Call<List<EventMarker>> GetAllEventMarker();

    @GET("geomap/events/{id}")
    Call<EventMarker> getEventById(@Path("id") Long id);

    @POST("geomap/events/add")
    Call<EventMarker> addEvent(@Body EventMarker eventMarker);

}
