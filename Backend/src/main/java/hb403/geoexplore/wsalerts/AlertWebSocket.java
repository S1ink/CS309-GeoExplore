package hb403.geoexplore.wsalerts;

import java.io.IOException;
import java.util.*;

import hb403.geoexplore.datatype.marker.repository.AlertRepository;
import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.datatype.marker.AlertMarker;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.websocket.*;
import jakarta.websocket.server.*;


@Controller
@ServerEndpoint(value = "/live/alerts/{user_id}")
public class AlertWebSocket {

	private static AlertRepository alert_repo;
	private static UserRepository users_repo;

	@Autowired
	public void autoAlertRepository(AlertRepository repo) {
		AlertWebSocket.alert_repo = repo;
	}
	@Autowired
	public void autoUsersRepository(UserRepository repo) {
		AlertWebSocket.users_repo = repo;
	}


	private static Map<Session, Long> session_user_ids = new HashMap<>();
	private static Set<Long> user_ids = new HashSet<>();


	@OnOpen
	public void onOpen(Session session, @PathParam("user_id") Long id) throws IOException {
		// add the user and session to a set of sessions
		if(session == null) return;
		if(id == null || user_ids.contains(id) || users_repo.findById(id).isEmpty()) {
			session.close();
			System.out.println("Session closed due to invalid user id: " + id);
		} else {
			user_ids.add(id);
			session_user_ids.put(session, id);
			System.out.println("Added user session with id: " + id);
		}
	}
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// try to parse the message to an alert -- filter by user type so only admins have this ability
		if(session == null || message == null) return;
		System.out.println("Recieved message: " + message);
		try {
			User u = users_repo.findById( session_user_ids.get(session) ).get();
			if(u.getIsAdmin()) {
				System.out.println("Recived message from admin user. Continuing to parse alert...");
			} else {
				System.out.println("Recieved alert message from non-admin user. Not continuing to parse.");
				return;
			}
		} catch(Exception e) {
			System.out.println("Failed to access user info.");
			return;
		}
		AlertMarker entity = new AlertMarker();	// TODO: actual parsing from json
		// if successful parse, add or update the database tables
		entity = alert_repo.saveAndFlush(entity);
		// finally, broadcast the alert back out to all users
		String out_message = "blank message";	// TODO: actual serialization
		session_user_ids.forEach(
			(Session s, Long id) -> {
				if(s == session) return;	// don't send the message back to the original poster
				try {
					s.getBasicRemote().sendText(out_message);
				} catch(Exception e) {

				}
			}
		);
	}
	@OnClose
	public void onClose(Session session) throws IOException {
		// remove the user/session from the set
		try {
			user_ids.remove( session_user_ids.remove(session) );	// this is probably a little unsafe (should check for nulls)
			System.out.println("Successfully closed user session.");
		} catch(Exception e) {
			System.out.println("Internal error: failed to remove user id or session on close.");
		}
	}
	@OnError
	public void onError(Session session, Throwable throwable) {
		// ha error funny
		System.out.println("WS Error: " + throwable.getMessage());
	}


}
