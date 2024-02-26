package test.connect.geoexploreapp.api;


import retrofit2.Call;
import retrofit2.http.GET;
import test.connect.geoexploreapp.model.Post;

public interface PostApi{
        @GET("posts/1")
        Call<Post> getFirstPost();

}
