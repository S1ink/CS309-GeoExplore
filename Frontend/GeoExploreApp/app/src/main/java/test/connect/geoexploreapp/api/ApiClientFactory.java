package test.connect.geoexploreapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClientFactory {

    static Retrofit apiClientSeed=null;

    static Retrofit GetApiClientSeed() {

        if(apiClientSeed==null) {
            apiClientSeed = new Retrofit.Builder()
                    .baseUrl("https://bdabeac2-f13b-4e71-b97d-9c7c1bab23c5.mock.pstmn.io")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return apiClientSeed;
    }


    public static UserApi GetUserApi(){
        return GetApiClientSeed().create(UserApi.class);
    }


}
