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

    @GET("geomap/observations/{id}")
    Call<Observation> getObs(@Path("id") Long id);

    @PUT("geomap/observations/{id}")
    Call<Observation> updateObs(@Path("id") Long id, @Body Observation observation);

    @DELETE("geomap/observations/{id}")
    Call<Observation> deleteObs(@Path("id") Long id);

    @GET("geomap/observations")
    Call<List<Observation>> getAllObs();

    //C of Crudl, adds observation to repo
//    @PostMapping(path = "geomap/observations/add")
//    public @ResponseBody ObservationEntity.JsonFormat saveObs(@RequestBody ObservationEntity.JsonFormat obs_json){
//        if (obs_json != null){
//            final ObservationEntity saved = this.obsRepo.save(ObservationEntity.fromJson(obs_json));
//            return ObservationEntity.formatJson(saved);
//
//        }
//        else {
//            return null;
//        }
//    }
//
//
//    //R of Crudl gets observation from repo
//    @GetMapping(path = "geomap/observations/{id}")
//    public @ResponseBody ObservationEntity.JsonFormat getObs(@PathVariable Long id) {
//        if (id != null) {
//            return ObservationEntity.formatJson(this.obsRepo.findById(id).get());
//        } else {
//            return null;
//        }
//    }
//
//
//    @PutMapping(path = "geomap/Observations/{id}/update")
//    public @ResponseBody ObservationEntity.JsonFormat updateObs(@PathVariable Long id,@RequestBody ObservationEntity.JsonFormat obs_json){
//        if (id != null){
//            final ObservationEntity.JsonFormat ref = this.getObs(id);
//            this.obsRepo.deleteById(id);
//            final ObservationEntity saved = this.obsRepo.save(ObservationEntity.fromJson(obs_json));
//            return ObservationEntity.formatJson(saved);
//        }
//        else {
//            return null;
//        }
//    }
//
//    @DeleteMapping(path = "geomap/observations/{id}/delete")
//    public @ResponseBody ObservationEntity.JsonFormat deleteObs(@PathVariable Long id){
//        if (id != null){
//            final ObservationEntity.JsonFormat ref = this.getObs(id);
//            this.obsRepo.deleteById(id);
//            return ref;
//        }
//        else {
//            return null;
//        }
//    }
//
//    @GetMapping(path = "geomap/observations")
//    public List<ObservationEntity.JsonFormat> getAllObs(){
//        final List<ObservationEntity> obs = this.obsRepo.findAll();
//        final ArrayList<ObservationEntity.JsonFormat> formatted = new ArrayList<>();
//        for (ObservationEntity o :obs){
//            formatted.add(ObservationEntity.formatJson(o));
//        }
//        return formatted;
//    }

}
