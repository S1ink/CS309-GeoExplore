package hb403.geoexplore.UserStorage.controller;

import hb403.geoexplore.UserStorage.entity.ReportedUser;
import hb403.geoexplore.UserStorage.repository.ReportedUserRepository;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportedUserController {

    @Autowired
    ReportedUserRepository reportedUserRepository;
    @Autowired
    UserRepository userRepository;

    //c of crudl
    @Operation(summary = "Add a new report of a user to the database")
    @PostMapping(path = "/user/report")
    public @ResponseBody ReportedUser ReportUser(@RequestBody ReportedUser newUser) {
        List<ReportedUser> allReportedUsers = reportedUserRepository.findAll();
        allReportedUsers.forEach(user ->{
            /*if(user.getReportedUser().getId().equals(newUser.getReportedUser().getId())){

            }*/
        });
        reportedUserRepository.save(newUser);
    return newUser;
    }
    public ReportedUser addReport(Long id){
        return null;
    }


}
