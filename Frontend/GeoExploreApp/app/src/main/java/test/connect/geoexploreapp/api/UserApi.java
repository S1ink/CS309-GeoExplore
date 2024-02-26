package test.connect.geoexploreapp.api;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.User;

public interface UserApi {
    @GET("/userinfo")
    Call<List<User>> getAllUsers();

    @GET("/user/{id}")
    Call<Optional<User>> getUser(@Path("id") Integer id);
    @POST("/user/create")
    Call<String> UserCreate(@Body User newUser);

}


//
//    @GetMapping(path = "/userinfo")
//    List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @GetMapping(path = "/user/{id}")
//    Optional<User> getUser(@PathVariable Integer id){
//        return userRepository.findById(id);
//    }
//
//
//
//    @PostMapping(path = "/user/create")
//    public String UserCreate(@RequestBody User newUser){
//        //User newUser = new User(name, username, password);
//        ArrayList<String> adminList = new ArrayList<>();
//        adminList.add("emessmer@iastate.edu");
//        adminList.add("aditin@iastate.edu");
//        adminList.add("yharb@iastate.edu");
//        adminList.add("samr888@iastate.edu");
//
//        if (!adminList.contains(newUser.getEmailId())){
//            newUser.setIfAdmin(false);
//        }
//        userRepository.save(newUser);
//        return newUser.toString();
//    }
//
//}

