package hb403.geoexplore.controllers;

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
import java.util.Objects;

@RestController
public class ImageController {

    // replace this! careful with the operating system in use
    private static String directory = "C:\\Users\\Ethan\\OneDrive\\Documents\\Se-309\\hb4_3\\hb4_3\\Backend\\images\\";

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ObservationRepository observationRepository;

    @Operation(summary = "gets image from repository")
    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable long id) throws IOException {
        Image image = imageRepository.findById(id).get();
        File imageFile = new File(image.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    @PostMapping("observation/image/{postId}")
    public String handleFileUpload(@RequestParam("image") MultipartFile imageFile, @PathVariable long postId)  {

        try {
            File destinationFile = new File(directory + /*File.separator +*/ Objects.requireNonNull(imageFile.getOriginalFilename()));
            System.out.println(destinationFile);
            imageFile.transferTo(destinationFile);  // save file to disk

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            ObservationMarker tempObs = observationRepository.findById(postId).get();
            tempObs.setImage(image);
            image.setObservation(tempObs);
            observationRepository.save(tempObs);
            imageRepository.save(image);
            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
            //System.out.println("Made it into the operation");
            /*File destinationFile = new File(directory + File.separator + imageFile.getOriginalFilename());
            imageFile.transferTo(destinationFile);  // save file to disk

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            //ObservationMarker tempObs = observationRepository.findById(postId).get();
            //tempObs.setImage(image);
            //image.setObservation(tempObs);
            //observationRepository.save(tempObs);
            imageRepository.save(image);
            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
            */
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
    }

    @PutMapping("observation/image/{id}")
    public String imageUpdate(@RequestParam("image") MultipartFile imageFile, @PathVariable long Id) {
        try {
            File destinationFile = new File(directory + /*File.separator +*/ Objects.requireNonNull(imageFile.getOriginalFilename()));
            System.out.println(destinationFile);
            imageFile.transferTo(destinationFile);  // save file to disk

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            ObservationMarker tempObs = observationRepository.findById(Id).get();
            tempObs.setImage(image);
            image.setObservation(tempObs);
            observationRepository.save(tempObs);
            imageRepository.save(image);
            return "File uploaded successfully: " + destinationFile.getAbsolutePath();
        }catch(Exception e){
            System.out.println(e);
            return e.toString();
        }
    }
    @DeleteMapping("observation/image/{Id}")
    public String deleteImage(@PathVariable long Id){
        try {
            Image temp = imageRepository.findById(Id).get();
            //ObservationMarker tempObs = temp.getObservation();
            //tempObs.setImage(null);
            //temp.setObservation(null);
           // observationRepository.save(tempObs);
            imageRepository.save(temp);
            imageRepository.deleteById(Id);
            return temp.toString();
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }

    }



}

