package hb403.geoexplore.comments;

import java.io.IOException;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;
import hb403.geoexplore.datatype.marker.EventMarker;
import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.ReportMarker;
import hb403.geoexplore.datatype.marker.repository.EventRepository;
import hb403.geoexplore.datatype.marker.repository.ObservationRepository;
import hb403.geoexplore.datatype.marker.repository.ReportRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.*;


@ServerEndpoint("/comments/{user_id}")
@Controller
public class CommentWebsocket {


    private static CommentRepository commentRepository;

    public static ObservationRepository observationRepository;

    private static UserRepository userRepository;

    private static EventRepository eventRepository;
    
    private static ReportRepository reportRepository;


    @Autowired
    public void autoCommentRepository(CommentRepository repo) {
        CommentWebsocket.commentRepository = repo;
    }

    @Autowired
    public void autoObservationRepository(ObservationRepository repo) {
        CommentWebsocket.observationRepository = repo;
    }

    @Autowired
    public void autoUserRepository(UserRepository repo) {
        CommentWebsocket.userRepository = repo;
    }

    @Autowired
    public void autoEventRepository(EventRepository repo){
        CommentWebsocket.eventRepository = repo;
    }


    @Autowired
    public void autoReportRepository(ReportRepository repo){
        CommentWebsocket.reportRepository = repo;
    }

    private static Map<Session, Long> session_user_ids = new HashMap<>();
	private static Set<Long> user_ids = new HashSet<>();
    

    @OnOpen
    public void onOpenSession (Session session, @PathParam("user_id") Long userId) throws IOException {
        if (session != null && userId != null && !user_ids.contains(userId) && userRepository.findById(userId).isPresent()){
         user_ids.add(userId);
         session_user_ids.put(session, userId);
         System.out.println("[Comment.WS] Completed startup");
        }
        else {
        System.out.println("[Comment WS] Not valid");
        }
    }
    @OnClose
    public void OnClose(Session session) throws IOException{
        try {
            final Long id = session_user_ids.remove(session);
            System.out.println("[Comment WS] Successfully Closed");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("[Comment WS] Failed on close");
        }
    }
    @OnMessage
    public void OnMessage(Session session, String message){
        if (session == null || message == null || userRepository.findById(session_user_ids.get(session)).isEmpty()){
            return;
        }
        
        final ObjectMapper mapperForComments = new ObjectMapper();
        String message_out;
        try {
            CommentEntity temp = mapperForComments.readValue(message, CommentEntity.class);
            temp.setUserId(session_user_ids.get(session));
            if(temp.getPostType().equals("Observation")){
                System.out.println("Made it into Obs" + temp.toString());
                temp = commentRepository.saveAndFlush(temp);
                System.out.println("Made it past line 2");
                ObservationMarker tempObs = observationRepository.findById(temp.getPostId()).get();
                System.out.println("Made it past line 3" + temp.getPostId());
                tempObs.getComments().add(temp);
                System.out.println("Made it past line 4" + tempObs.getComments());
                temp.setPertainsObservationMarker(tempObs);
                observationRepository.saveAndFlush(tempObs);
                System.out.println("Made it past line 5");
                
            }
            else if (temp.getPostType().equals("Event")){
                System.out.println("Made it into Event" + temp.toString());
                temp = commentRepository.saveAndFlush(temp);
                EventMarker tempEvent = eventRepository.findById(temp.getPostId()).get();
                tempEvent.getComments().add(temp);
                temp.setPertainsEventMarker(tempEvent);
                eventRepository.saveAndFlush(tempEvent);
            }
            else if (temp.getPostType().equals("Report")){
                System.out.println("Made it into Report" + temp.toString());
                temp = commentRepository.saveAndFlush(temp);
                ReportMarker tempReport = reportRepository.findById(temp.getPostId()).get();
                tempReport.getComments().add(temp);
                temp.setPertainsReportMarker(tempReport);
                reportRepository.saveAndFlush(tempReport);
            }
            else {
                System.out.println("Invalid Type");
                return;
            }
            User tempUser = userRepository.findById(temp.getUserId()).get();
                tempUser.getComments().add(temp);
                temp.setPertainsUser(tempUser);
                userRepository.saveAndFlush(tempUser);
                
            message_out = mapperForComments.writeValueAsString(temp);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        System.out.println("Made it to sending phase" + message_out);
        if(message_out != null){
            session_user_ids.forEach((Session tempSess, Long user_ids)-> {
                if (tempSess == session) return;
                try {
                    tempSess.getBasicRemote().sendText(message_out);
                    System.out.println("sent message");
                }catch (Exception e){
                    System.out.println(e);
                }
            });
        }
    }
    @OnError
    public void OnError(Session session, Throwable throwable){
        System.out.println("[Comment WS] Error: " + throwable.getMessage());
    }

}
