package test.connect.geoexploreapp.api;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.User;

public interface UserApi {

    @POST("/user/create")
    Call<User> UserCreate(@Body User newUser);

    @GET("/user/{id}")
    Call<Optional<User>> getUser(@Path("id") Long id);

    @PUT("/user/{id}/update")
    Call<User> updateUser(@Path("id") Long id, @Body User updated);
    @DELETE("/user/{id}/delete")
    Call<String> deleteUser(@Path("id") Long Id);

    @DELETE("/user/delete/all")
    Call<String> deleteAll();
    @GET("/userinfo")
    Call<List<User>> getAllUsers();
}

//C of Crudl
//    @PostMapping(path = "/user/create")
//    public @ResponseBody User UserCreate(@RequestBody User newUser){
//        //User newUser = new User(name, username, password);
//        userRepository.save(newUser);
//        return newUser;
//    }
//
//
//    //R of Crudl
//    @GetMapping(path = "/user/{id}")
//    Optional<User> getUser(@PathVariable Long id){
//        return userRepository.findById(id);
//    }
//
//    //U of Crudl
//    @PutMapping(path = "/user/{id}/update")
//    public @ResponseBody User updateUser(@PathVariable Long id, @RequestBody User updated){
//        userRepository.deleteById(id);
//        User updater = new User(id, updated.getName(), updated.getEmailId(), updated.getPassword());
//        userRepository.save(updater);
//        return updater;
//    }
//
//
//    @DeleteMapping(path = "/user/{id}/delete")
//    public @ResponseBody String deleteUser(@PathVariable Long id){
//        User deleted = userRepository.findById(id).get();
//        userRepository.deleteById(id);
//        return "Successfully deleted: \n" + deleted.toString();
//    }
//
//    @DeleteMapping(path = "user/delete/all")
//    public @ResponseBody String deleteAll(){
//        userRepository.deleteAll();
//        return "Successfully deleted all users";
//    }
//
//
//
//    //L of Crudl
//    @GetMapping(path = "/userinfo")
//    @ResponseBody List<User>  getAllUsers() {
//        return userRepository.findAll();
//    }
//
