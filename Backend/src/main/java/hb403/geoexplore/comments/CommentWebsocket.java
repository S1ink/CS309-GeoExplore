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
        if(session == null || userId == null) {
			System.out.println("[Comment WS]: Failed to start session - one or more params were null.");
			return;
		}
        boolean
            already_present = user_ids.contains(userId),
            valid_user = userRepository.findById(userId).isPresent();
        if (!already_present && valid_user) {
            user_ids.add(userId);
            session_user_ids.put(session, userId);
            System.out.println("[Comment WS]: Successfully added session for user id " + userId);
        } else {
            System.out.println("[Comment WS]: Failed to add session for user id " + userId + " -- already present: " + already_present + ", valid: " + valid_user);
        }
        this.printStatus();
    }
    @OnClose
    public void OnClose(Session session) throws IOException{
        try {
            final Long id = session_user_ids.remove(session);
            user_ids.remove(id);
            System.out.println("[Comment WS]: Successfully closed session for user id " + id);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("[Comment WS]: Failed on close user session!?");
        }
        this.printStatus();
    }
    @OnMessage
    public void OnMessage(Session session, String message){
        if (session == null || message == null || userRepository.findById(session_user_ids.get(session)).isEmpty()){
            System.out.println("[Comment WS]: Invalid message, session, or user!");
            return;
        }
        final ObjectMapper mapperForComments = new ObjectMapper();
        String message_out;
        try {
            CommentEntity temp = mapperForComments.readValue(message, CommentEntity.class);
            temp.setUserId(session_user_ids.get(session));
            if(temp.getPostType().equals("Observation")){
                temp = commentRepository.saveAndFlush(temp);
                ObservationMarker tempObs = observationRepository.findById(temp.getPostId()).get();
                tempObs.getComments().add(temp);
                temp.setPertainsObservationMarker(tempObs);
                observationRepository.saveAndFlush(tempObs);
                System.out.println("[Comment WS]: Successfully saved observation, and tags for comment message:\n" + temp.toString());
                
            }
            else if (temp.getPostType().equals("Event")){
                temp = commentRepository.saveAndFlush(temp);
                EventMarker tempEvent = eventRepository.findById(temp.getPostId()).get();
                tempEvent.getComments().add(temp);
                temp.setPertainsEventMarker(tempEvent);
                eventRepository.saveAndFlush(tempEvent);
                System.out.println("[Comment WS]: Successfully saved event, and tags for comment message:\n" + temp.toString());
            }
            else if (temp.getPostType().equals("Report")){
                temp = commentRepository.saveAndFlush(temp);
                ReportMarker tempReport = reportRepository.findById(temp.getPostId()).get();
                tempReport.getComments().add(temp);
                temp.setPertainsReportMarker(tempReport);
                reportRepository.saveAndFlush(tempReport);
                System.out.println("[Comment WS]: Successfully saved report, and tags for comment message:\n" + temp.toString());
            }
            else {
                System.out.println("[Comment WS]: Failed to parse message due to invalid post type.");
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
        System.out.println("[Comment WS]: Proceeding to send message: " + message_out);
        if(message_out != null){
            session_user_ids.forEach(
                (Session tempSess, Long user_id)-> {
                    // if (tempSess == session) return;
                    try {
                        tempSess.getBasicRemote().sendText(message_out);
                        System.out.println("[Comment WS]: Successfully sent message to user id " + user_id);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
            );
        }
    }
    @OnError
    public void OnError(Session session, Throwable throwable){
        System.out.println("[Comment WS - ERROR]: " + throwable.getMessage());
        this.printStatus();
    }



    private void printStatus() {
		String fmt = "[Alert WS - STATUS]: Users: " + user_ids.size() + ", Sessions: " + session_user_ids.size() + ", IDS: { ";
		for(Long id : user_ids) {
			fmt += (id + ", ");
		}
		fmt += " }";
		System.out.println(fmt);
	}


}
