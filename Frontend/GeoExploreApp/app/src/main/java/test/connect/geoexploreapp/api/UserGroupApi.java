package test.connect.geoexploreapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.UserGroup;

public interface UserGroupApi {

    //add a new usergroup to the db
    @POST("user/groups")
    Call<UserGroup> addGroup(@Body UserGroup group);


    //add a new usergroup by including a name
    @POST("user/groups/create")
    Call<UserGroup> createGroup(@Body String name);

    @GET("user/groups/{id}")
    Call<UserGroup> getGroupById(@Path("id") Long id);

    @PUT("user/groups/{id}")
    Call<UserGroup> updateGroupById(@Path("id") Long id, @Body UserGroup group);

    @DELETE("user/groups/{id}")
    Call<UserGroup> deleteGroupById(@Path("id") Long id);

    @GET("user/groups")
    Call<List<UserGroup>> getAllGroups();

    @POST("user/groups/{group_id}/members")
    Call<UserGroup> addMemberToGroupById(@Path("id") Long id, @Body UserGroup group);


}