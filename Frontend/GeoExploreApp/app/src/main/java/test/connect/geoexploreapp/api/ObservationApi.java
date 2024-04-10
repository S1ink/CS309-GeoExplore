package test.connect.geoexploreapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.Observation;

public interface ObservationApi {

    @POST("geomap/observations")
    Call<Observation> saveObs(@Body Observation observation);

    @POST("geomap/observations/{id}/tags")
    Call<Void> addExistingTagToObservation(@Path("id") Long reportId, @Body Long tagId);

    @GET("geomap/observations/{id}")
    Call<Observation> getObs(@Path("id") Long id);

    @PUT("geomap/observations/{id}")
    Call<Observation> updateObs(@Path("id") Long id, @Body Observation observation);

    @DELETE("geomap/observations/{id}")
    Call<Observation> deleteObs(@Path("id") Long id);

    @GET("geomap/observations")
    Call<List<Observation>> getAllObs();

}