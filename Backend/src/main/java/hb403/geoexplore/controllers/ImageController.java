package hb403.geoexplore.controllers;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.datatype.Image;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.repository.ImageRepository;
import hb403.geoexplore.datatype.marker.repository.ObservationRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class ImageController {


    /**
     * TODO:
     * 1. Filter post to only have it save to disk if the image isn't already saved, I think that's the bug -- Done
     * 2. Filter put similarly to post -- Done
     * 3. Find a way for it to delete images that are no longer used -- DONE
     * 4. Find a way for it to store to the server since it can't with my local computer -- will need some testing after deployment
     */


    //FOR ACTUAL IMAGE FILE STORING
    //for local testing in backend String directory = "C:\\Users\\Ethan\\OneDrive\\Documents\\Se-309\\hb4_3\\hb4_3\\Backend\\images\\"
    //On server String directory = "\\hb403\\images\\";
    private static final String directory = "\\hb403\\images\\";

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ObservationRepository observationRepository;
    @Autowired
    private UserRepository userRepository;


    @Operation(summary = "Upload an image to an observation")
    @PostMapping("observation/image/{postId}")
    public String observationFileUpload(@RequestParam("image") MultipartFile imageFile, @PathVariable long postId) {

        try {
            if (imageFile == null || postId == 0){

            }
            else {
                AtomicBoolean isRepeat = new AtomicBoolean(false);
                File destinationFile = new File(directory + Objects.requireNonNull(imageFile.getOriginalFilename()));//File.separator was removed because it wasn't letting me upload with it
                System.out.println(destinationFile);
                Image image = new Image();
                image.setFilePath(destinationFile.getAbsolutePath());
                List<Image> sort = imageRepository.findAll();
                sort.forEach(imageInRepo -> { // a way to tell if the image is a repeat within the file storage
                    if (imageInRepo.getFilePath().equals(image.getFilePath())) {
                        isRepeat.set(true);
                    }
                });

                if (!isRepeat.get()) {
                    imageFile.transferTo(destinationFile);  // save file to disk only if image is not a repeat
                }
                //image.setFilePath(destinationFile.getAbsolutePath());
                ObservationMarker tempObs = observationRepository.findById(postId).get();
                tempObs.setImage(image);
                image.setObservation(tempObs);
                observationRepository.save(tempObs);
                //imageRepository.save(image);
                return "File uploaded successfully: " + destinationFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
        return null;
    }

   /* @Operation(summary = "Uploads an image to a user as a profile picture")
    @PostMapping("user/image/{userId}")
    public String userFileUpload(@RequestParam("image") MultipartFile imageFile, @PathVariable long userId) {

        try {
            AtomicBoolean isRepeat = new AtomicBoolean(false);
            File destinationFile = new File(directory + Objects.requireNonNull(imageFile.getOriginalFilename()));//File.separator was removed because it wasn't letting me upload with it
            System.out.println(destinationFile);
            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            List<Image> sort = imageRepository.findAll();
            sort.forEach(imageInRepo -> { // a way to tell if the image is a repeat within the file storage
                if (imageInRepo.getFilePath().equals(image.getFilePath())) {
                    isRepeat.set(true);
                }
            });

            if (!isRepeat.get()) {
                imageFile.transferTo(destinationFile);  // save file to disk only if image is not a repeat
            }
            //image.setFilePath(destinationFile.getAbsolutePath());
            ObservationMarker tempObs = observationRepository.findById(userId).get();
            tempObs.setImage(image);
            image.setObservation(tempObs);
            observationRepository.save(tempObs);
            //imageRepository.save(image);
            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
    }*/


    @Operation(summary = "gets image from repository using image id")
    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable long id) throws IOException {
        if (id != 0) {
            Image image = imageRepository.findById(id).get();
            File imageFile = new File(image.getFilePath());
            return Files.readAllBytes(imageFile.toPath());
        }
        else {
            return null;
        }
    }

    @Operation(summary = "gets image from repository using observation id")
    @GetMapping(value = "/observation/image/{post_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageByPostId(@PathVariable long post_id) throws IOException {
        ObservationMarker temp = observationRepository.findById(post_id).get();
        Image image = temp.getImage();
        File imageFile = new File(image.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    /*@Operation(summary = "gets image from repository using user id")
    @GetMapping(value = "/observation/images/{user_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageByUserId(@PathVariable long user_id) throws IOException {
        User temp = userRepository.findById(user_id).get();
        Image image = temp.getImage();
        File imageFile = new File(image.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }*/




    @PutMapping("/observation/image/{id}")
    public String imageUpdate(@RequestParam("image") MultipartFile imageFile, @PathVariable long id) { //this one is complicated for image file management
        try {
            if (imageFile == null || id == 0){

            }
            else {
                imageRepository.findById(id).isPresent();
                AtomicBoolean isRepeat = new AtomicBoolean(false);
                File destinationFile = new File(directory +  Objects.requireNonNull(imageFile.getOriginalFilename()));
                System.out.println(destinationFile);
                Image image;

                image = imageRepository.findById(id).get();
                List<Image> sort = imageRepository.findAll();
                sort.forEach(imageInRepo -> { // a way to tell if the image is a repeat within the file storage
                    if (imageInRepo.getFilePath().equals(image.getFilePath()) && !Objects.equals(imageInRepo.getId(), image.getId())) {
                        isRepeat.set(true);
                    }
                });
                if (!isRepeat.get()) {//deletes image file from storage if the image is not a repeat
                    try {
                        File imageFileDelete = new File(image.getFilePath());
                        Files.delete(imageFileDelete.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                isRepeat.set(false);


                sort.forEach(imageInRepo -> { // a way to tell if the image is a repeat within the file storage
                    if (imageInRepo.getFilePath().equals(destinationFile.getAbsolutePath())) {
                        System.out.println(image.getFilePath());
                        System.out.println(imageInRepo.getFilePath());
                        isRepeat.set(true);
                    }
                });
                System.out.println(isRepeat.get());
                if (!isRepeat.get()) {
                    imageFile.transferTo(destinationFile);  // save file to disk only if image is not a repeat
                }
                image.setFilePath(destinationFile.getAbsolutePath());
                System.out.println(isRepeat.get());//expected false
                ObservationMarker tempObs = image.getObservation();
                tempObs.setImage(image);
                observationRepository.save(tempObs);
                imageRepository.save(image);
                return "File uploaded successfully: " + destinationFile.getAbsolutePath();
            }
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
        return null;
    }

    @Operation(summary = "Deletes the image using the image ID")
    @DeleteMapping("/image/{Id}")
    public String deleteImage(@PathVariable long Id) {
        try {
            AtomicBoolean isRepeat = new AtomicBoolean(false);
            Image temp = imageRepository.findById(Id).get();

            List<Image> sort = imageRepository.findAll();
            sort.forEach(image -> { // a way to tell if the image is a repeat within the file storage
                if (image.getFilePath().equals(temp.getFilePath()) && !Objects.equals(image.getId(), temp.getId())) {
                    isRepeat.set(true);
                }
            });

            if (!isRepeat.get()) {//deletes image file from storage if the image is not a repeat
                try {
                    File imageFile = new File(temp.getFilePath());
                    Files.delete(imageFile.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            ObservationMarker tempObs = temp.getObservation();
            tempObs.setImage(null);
            temp.setObservation(null);
            observationRepository.save(tempObs);
            imageRepository.save(temp);
            imageRepository.deleteById(Id);
            return temp.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    @Operation(summary = "List of all the image entitys but it won't actually output them as images just the object")
    @GetMapping("/images/list")
    public @ResponseBody List<Image> listImageEntities() {
        return imageRepository.findAll();
    }


}

