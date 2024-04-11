package hb403.geoexplore.UserStorage.controller;

import hb403.geoexplore.UserStorage.entity.ReportedUser;
import hb403.geoexplore.UserStorage.repository.ReportedUserRepository;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.annotations.WhereJoinTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReportedUserController {

    @Autowired
    ReportedUserRepository reportedUserRepository;
    @Autowired
    UserRepository userRepository;

    /**Todo
     * Make Update
     * Make Delete
     * Set it up so that a ban can be issued for a period of time
     * Set date and time up for comments
     */

    //c of crudl
    @Operation(summary = "Add a new report of a user to the database")
    @PostMapping(path = "/user/report")
    public @ResponseBody ReportedUser ReportUser(@RequestBody ReportedUser newUser) {
        if (newUser == null ) {
            System.out.println("RequestBody null");
        }
        else if(!newUser.getHarassment() && !newUser.getSpamming() && !newUser.getMisinformation() && !newUser.getInappropriateContent()) {
            System.out.println("false report");
        }
        else {
            List<ReportedUser> getAllReportedUsers = reportedUserRepository.findAll();
            getAllReportedUsers.forEach(report -> {
                if (newUser.getReportedUserId().equals(report.getReportedUserId())) { //checks if user has already been reported to not make more reports than neccesary
                    report = addReport(report,newUser);
                    reportedUserRepository.save(report);
                }
            });
            reportedUserRepository.save(newUser);
            return newUser;
        }
        return newUser;
    }

    //adds report to a user that has already been reported without taking up more space.
    public ReportedUser addReport(ReportedUser report, ReportedUser newUser) {

        report.setNumReports(report.getNumReports() + 1);
        if (!report.getSpamming() && newUser.getSpamming()){
            report.setSpamming(true);
        }
        if (!report.getMisinformation() && newUser.getMisinformation()){
            report.setMisinformation(true);
        }
        if (!report.getInappropriateContent() && newUser.getInappropriateContent()){
            report.setInappropriateContent(true);
        }
        if (!report.getHarassment() && newUser.getHarassment()){
            report.setHarassment(true);
        }
        return report;
    }


    @Operation(summary = "Gets a reported user based on userId")
    @GetMapping(path = "/user/report/{id}")
    public @ResponseBody ReportedUser getReported(@PathVariable Long id){
        try{List<ReportedUser> getAllReportedUsers = reportedUserRepository.findAll();
            final ReportedUser[] temp = new ReportedUser[1];
        getAllReportedUsers.forEach(user -> {
            if(user.getReportedUserId().equals (id)){
                temp[0] = user;
            }
        });
        if (temp[0] != null) {
            return temp[0];
        }

        }catch (Exception e){
            System.out.println(e);
            throw e;
        }
        return null;
    }

    @Operation(summary = "Gets a reported user based on ReportedUserId")
    @GetMapping(path = "/user/report/{id}/mod")
    public @ResponseBody ReportedUser getReportedById(@PathVariable Long id){
        try{
            if(reportedUserRepository.findById(id).isPresent()) {
                System.out.println("ReportedUser form is present");
                return reportedUserRepository.findById(id).get();
            }
        }catch (Exception e){
            System.out.println(e);
            throw e;
        }
        return null;
    }

    @Operation(summary = "Updates a report on a user using the id of the report")
    @PutMapping(path = "user/report/update")
    public @ResponseBody ReportedUser updateReportedUser(@RequestBody ReportedUser updated){
        if (updated == null || updated.getReportedUserId() == null){

        }
        try {
            ReportedUser temp = reportedUserRepository.findById(updated.getId()).get();
            if(updated.getReportedUserId() == null){

            }
        }
        catch (Exception e){
            throw e;
        }
        return null;
    }

}
