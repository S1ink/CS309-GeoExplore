package test.connect.geoexploreapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientFactory {

    static Retrofit apiClientSeed = null;

    static Retrofit GetApiClientSeed(){
        if(apiClientSeed == null){

            apiClientSeed = new Retrofit.Builder()
                    .baseUrl("https://bdabeac2-f13b-4e71-b97d-9c7c1bab23c5.mock.pstmn.io/") // Server url here with / at the end http://coms-309-005.class.las.iastate.edu:8080
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
    public static CommentApi GetCommentApi() {
        return GetApiClientSeed().create(CommentApi.class);
    }

}
