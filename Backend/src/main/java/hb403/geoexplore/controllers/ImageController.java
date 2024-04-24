package hb403.geoexplore.controllers;

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
    private static final String directory = "/hb403/images";

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
            ObservationMarker tempObs = observationRepository.findById(postId).get(); //Checks if post id is null or invalid
            if (imageFile == null) {
                System.out.println("[Post] Image file is null");
            } else if (postId == 0) {
                System.out.println("[Post] Post Id is invalid");
            }
            /*else if (!imageFile.toString().contains("jpg")){ //Might try to implement this enforcement but it didn't work at first, I'll look into it a bit more though
                System.out.println("[Post] File is not a JPEG");
            }*/
            else {
                AtomicBoolean isRepeat = new AtomicBoolean(false);

                File destinationFile = new File(directory + Objects.requireNonNull(imageFile.getOriginalFilename()));//File.separator was removed because it wasn't letting me upload with it
                System.out.println(destinationFile);
                Image image = new Image();
                image.setFilePath(destinationFile.getAbsolutePath());
                if (!directory.contains(imageFile.toString())) { //checks just the file itself
                    imageFile.transferTo(destinationFile);  // save file to disk only if image is not a repeat

                }

                tempObs.setImage(image);
                image.setObservation(tempObs);
                observationRepository.save(tempObs);
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
            try {
                Image image = imageRepository.findById(id).get();
                File imageFile = new File(image.getFilePath());
                return Files.readAllBytes(imageFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            System.out.println("[GET] Id is null");
            return null;
        }

    }

    @Operation(summary = "gets image from repository using observation id")
    @GetMapping(value = "/observation/image/{post_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageByPostId(@PathVariable long post_id) throws IOException {
        if (post_id != 0) {
            try {
                ObservationMarker temp = observationRepository.findById(post_id).get();
                Image image = temp.getImage();
                File imageFile = new File(image.getFilePath());
                return Files.readAllBytes(imageFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            System.out.println("[GET] Id is null");
            return null;
        }
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
            if (imageFile == null || id == 0) {
                System.out.println("[PUT] imageFile is null or id is invalid");
            } else {
                imageRepository.findById(id).isPresent();
                AtomicBoolean isRepeat = new AtomicBoolean(false);
                File destinationFile = new File(directory + Objects.requireNonNull(imageFile.getOriginalFilename()));
                System.out.println(destinationFile);
                Image image;

                image = imageRepository.findById(id).get();
                findRepeat(image);
                isRepeat.set(false);

                /*sort.forEach(imageInRepo -> { // checks if the new image is
                    if (imageInRepo.getFilePath().equals(destinationFile.getAbsolutePath())) {
                        System.out.println(image.getFilePath());
                        System.out.println(imageInRepo.getFilePath());
                        isRepeat.set(true);
                    }
                });*/
                if (!directory.contains(imageFile.toString())) { //checks just the file itself
                    imageFile.transferTo(destinationFile);  // save file to disk only if image is not a repeat

                }
                System.out.println(isRepeat.get());
                /*if (!isRepeat.get()) {
                    imageFile.transferTo(destinationFile);  // save file to disk only if image is not a repeat
                }*/
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

            Image temp = imageRepository.findById(Id).get();

            findRepeat(temp);

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


    //First checks if there are multiple images in the repository that use the same file, and if there is it doesn't delete it but if it's the only one using it, the file is deleted
    private void findRepeat(Image image) {
        AtomicBoolean isRepeat = new AtomicBoolean(false);
        List<Image> sort = imageRepository.findAll();
        sort.forEach(imageInRepo -> {
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
    }

}

