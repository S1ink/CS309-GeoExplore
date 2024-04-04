package test.connect.geoexploreapp.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.Comment;

public interface CommentApi {

    //create
    @POST("comment/store/{postType}")
    Call<Comment> commentStore(@Body Comment newComment, @Path("postType") String postType);

    @GET("comment/{id}")
    Call<Comment> getComment(@Path("id") Long id);

    @PUT("comment/{id}/update")
    Call<Comment> updateComment(@Path("id") Long id, @Body Comment updatedComment);

    @DELETE("comment/{id}/delete")
    Call<ResponseBody> deleteComment(@Path("id") Long id);

    @GET("comment/list")
    Call<List<Comment>> getAllComments();

    @GET("observation/comments/{postId}")
    Call<List<Comment>> getCommentsForObs(@Path("postId") Long postId);

    @GET("event/comments/{postId}")
    Call<List<Comment>> getCommentsForEvents(@Path("postId") Long postId);

    @GET("user/comments/{user_table_Id}")
    Call<List<Comment>> getCommentsForUser(@Path("user_table_Id") Long user_table_Id);

    @GET("report/comments/{postId}")
    Call<List<Comment>> getCommentsForReports(@Path("postId") Long postId);

}


//public class CommentController {
//    @Autowired
//    CommentRepository commentRepository;
//    //C of Crudl
//    @Operation(summary = "Add a new comment to the database")
//    @PostMapping(path = "/comment/store")
//    public @ResponseBody CommentEntity commentStore (@RequestBody CommentEntity newComment){
//        commentRepository.save(newComment);
//        return newComment;
//    }
//    //R of Crudl
//    @Operation(summary = "Get a comment from the database by its id")
//    @GetMapping(path = "/comment/{id}")
//    public @ResponseBody CommentEntity getComment(@PathVariable Long id){
//        CommentEntity deleted = commentRepository.getById(id);
//        commentRepository.deleteById(id);
//        return deleted;
//    }
//
//    //U of Crudl
//    @Operation(summary = "Update a comment already in the database by its id")
//    @PutMapping(path = "/comment/{id}/update")
//    public @ResponseBody CommentEntity updateComment(@PathVariable Long id, @RequestBody CommentEntity updated){
//        //CommentEntity updater = commentRepository.getById(id);
//        CommentEntity updater = new CommentEntity(id, updated.getPostid(), updated.getUserid(), updated.getComment());
//        commentRepository.save(updater);
//        return updated;
//    }
//
//
//    @Operation(summary = "Delete a comment from the database by its id")
//    @DeleteMapping(path = "/comment/{id}/delete")
//    public @ResponseBody String deleteComment(@PathVariable Long id){
//        CommentEntity deleted = commentRepository.findById(id).get();
//        commentRepository.deleteById(id);
//        return "Successfully deleted: \n" + deleted.toString();
//    }
//
//
//
//    //L of Crudl (won't be used probably)
//    @Operation(summary = "Get a list of all the comments in the database")
//    @GetMapping(path = "/comment/list")
//    @ResponseBody
//    List<CommentEntity> getAllComments() {
//        return commentRepository.findAll();
//    }
//}
