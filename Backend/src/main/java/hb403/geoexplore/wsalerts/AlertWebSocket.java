package hb403.geoexplore.wsalerts;

import java.io.IOException;
import java.util.*;

import hb403.geoexplore.datatype.marker.repository.AlertRepository;
import hb403.geoexplore.UserStorage.entity.User;
import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.datatype.marker.AlertMarker;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.*;

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
		if(session == null || id == null) {
			System.out.println("[Alert WS]: Failed to start session - one or more params were null.");
			return;
		}
		boolean
			already_present = user_ids.contains(id),
			valid_user = users_repo.findById(id).isPresent();
		if (!already_present && valid_user) {
			user_ids.add(id);
			session_user_ids.put(session, id);
			System.out.println("[Alert WS]: Successfully added session for user id " + id);
		} else {
			System.out.println("[Alert WS]: Failed to add session for user id " + id + " -- already present: " + already_present + ", valid: " + valid_user);
		}
	}
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		// try to parse the message to an alert -- filter by user type so only admins have this ability
		if(session == null || message == null) return;
		System.out.println("[Alerts WS]: Recieved message: " + message);
		try {
			User u = users_repo.findById( session_user_ids.get(session) ).get();
			if(u.getIsAdmin()) {
				System.out.println("[Alerts WS]: Recieved message from admin user. Continuing to parse alert...");
			} else {
				System.out.println("[Alerts WS]: Recieved message from non-admin user. Discontinuing parsing...");
				return;
			}
		} catch(Exception e) {
			System.out.println("[Alerts WS]: Failed to access user info.");
			return;
		}
		final ObjectMapper mapper = new ObjectMapper();
		String out_message;
		try {
			AlertMarker entity = mapper.readValue(message, AlertMarker.class);
			entity.enforceLocationIO();
			entity.nullifyId();	// only allow new entries for WS
			entity.applyNewTimestamp();
			entity = alert_repo.saveAndFlush(entity);
			entity.enforceLocationTable();
			out_message = mapper.writeValueAsString(entity);
		} catch(Exception e) {	// hard fail - don't continue
			System.out.println("[Alert WS]: Failed to parse, save, or serialize alert message.");
			return;
		}
		if(out_message != null) {
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
	}
	@OnClose
	public void onClose(Session session) throws IOException {
		// remove the user/session from the set
		try {
			final Long id = session_user_ids.remove(session);
			user_ids.remove(id);
			System.out.println("[Alerts WS]: Successfully closed session for user id " + id);
		} catch(Exception e) {
			System.out.println("[Alerts WS]: Internal error! Failed to close user session.");
		}
	}
	@OnError
	public void onError(Session session, Throwable throwable) {
		// ha error funny
		System.out.println("[Alerts WS - ERROR]: " + throwable.getMessage());
	}


}
