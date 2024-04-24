package test.connect.geoexploreapp.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.Image;

public interface ImageApi {

    @POST("observation/image/{postId}")
    Call<ResponseBody> observationFileUpload(@Part MultipartBody.Part image,@Path("postId") Long postId);
//    @Operation(summary = "gets image from repository using observation id")
    @GET("images/{id}")
    Call<ResponseBody> getImageById(@Path("id") Long id);

    @GET("observation/image/{post_id}")
    Call<ResponseBody> getImageByPostId(@Path("post_id") Long post_id);

    @PUT("observation/image/{id}")
    Call<String> imageUpdate( @Part MultipartBody.Part image,@Path("id") Long id);


    @DELETE("image/{id}")
    Call<String> deleteImage(@Path("id") Long id);

    @GET("image/LIST")
    Call<List<Image>> listImageEntities();

//    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
//    byte[] getImageById(@PathVariable long id) throws IOException {
//        Image image = imageRepository.findById(id).get();
//        File imageFile = new File(image.getFilePath());
//        return Files.readAllBytes(imageFile.toPath());
//    }
//
//    @PostMapping("observation/image/{postId}")
//    public String handleFileUpload(@RequestParam("image") MultipartFile imageFile, @PathVariable long postId)  {
//
//        try {
//            File destinationFile = new File(directory + /*File.separator +*/ Objects.requireNonNull(imageFile.getOriginalFilename()));
//            System.out.println(destinationFile);
//            imageFile.transferTo(destinationFile);  // save file to disk
//
//            Image image = new Image();
//            image.setFilePath(destinationFile.getAbsolutePath());
//            ObservationMarker tempObs = observationRepository.findById(postId).get();
//            tempObs.setImage(image);
//            image.setObservation(tempObs);
//            observationRepository.save(tempObs);
//            imageRepository.save(image);
//            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
//            //System.out.println("Made it into the operation");
//            /*File destinationFile = new File(directory + File.separator + imageFile.getOriginalFilename());
//            imageFile.transferTo(destinationFile);  // save file to disk
//
//            Image image = new Image();
//            image.setFilePath(destinationFile.getAbsolutePath());
//            //ObservationMarker tempObs = observationRepository.findById(postId).get();
//            //tempObs.setImage(image);
//            //image.setObservation(tempObs);
//            //observationRepository.save(tempObs);
//            imageRepository.save(image);
//            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
//            */
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to upload file: " + e.getMessage();
//        }
//    }
//
//    @PutMapping("observation/image/{id}")
//    public String imageUpdate(@RequestParam("image") MultipartFile imageFile, @PathVariable long Id) {
//        try {
//            File destinationFile = new File(directory + /*File.separator +*/ Objects.requireNonNull(imageFile.getOriginalFilename()));
//            System.out.println(destinationFile);
//            imageFile.transferTo(destinationFile);  // save file to disk
//
//            Image image = new Image();
//            image.setFilePath(destinationFile.getAbsolutePath());
//            ObservationMarker tempObs = observationRepository.findById(Id).get();
//            tempObs.setImage(image);
//            image.setObservation(tempObs);
//            observationRepository.save(tempObs);
//            imageRepository.save(image);
//            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
//        }catch(Exception e){
//            System.out.println(e);
//            return e.toString();
//        }
//    }
//    @DeleteMapping("observation/image/{Id}")
//    public String deleteImage(@PathVariable long Id){
//        try {
//            Image temp = imageRepository.findById(Id).get();
//            //ObservationMarker tempObs = temp.getObservation();
//            //tempObs.setImage(null);
//            //temp.setObservation(null);
//            // observationRepository.save(tempObs);
//            imageRepository.save(temp);
//            imageRepository.deleteById(Id);
//            return temp.toString();
//        }catch (Exception e){
//            e.printStackTrace();
//            return e.toString();
//        }
//
//    }


}
