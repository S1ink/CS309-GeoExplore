package test.connect.geoexploreapp.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.Image;

public interface ImageApi {
    @Multipart
    @POST("observation/image/{postId}")
    Call<ResponseBody> observationFileUpload(@Part MultipartBody.Part image, @Path("postId") Long postId);
//    @Operation(summary = "gets image from repository using observation id")
    @GET("image/{id}")
    Call<ResponseBody> getImageById(@Path("id") Long id);

    @GET("observation/image/{post_id}")
    Call<ResponseBody> getImageByPostId(@Path("post_id") Long post_id);

    @PUT("observation/image/{id}")
    Call<String> imageUpdate( @Part MultipartBody.Part image,@Path("id") Long id);


    @DELETE("image/{id}")
    Call<ResponseBody> deleteImage(@Path("id") Long id);

    @GET("images/list")
    Call<List<Image>> listImageEntities();


}
