package test.connect.geoexploreapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientFactory {

    static Retrofit apiClientSeed = null;

    static Retrofit GetApiClientSeed(){
        if(apiClientSeed == null){

            apiClientSeed = new Retrofit.Builder()
                    .baseUrl("https://67d7f572-57ac-492a-8502-49174d854116.mock.pstmn.io/") // Server url here with / at the end http://coms-309-005.class.las.iastate.edu:8080
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return apiClientSeed;
    }


    public static ReportMarkerApi getReportMarkerApi(){ return GetApiClientSeed().create(ReportMarkerApi.class);}
    public static EventMarkerApi getEventMarkerApi(){ return GetApiClientSeed().create(EventMarkerApi.class);}

    public static UserApi GetUserApi(){

        return GetApiClientSeed().create(UserApi.class);
    }
    public static ObservationApi GetObservationApi() { return GetApiClientSeed().create(ObservationApi.class);}

}
