package hb403.geoexplore.comments;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


import hb403.geoexplore.UserStorage.repository.UserRepository;
import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;

import hb403.geoexplore.datatype.marker.ObservationMarker;
import hb403.geoexplore.datatype.marker.ReportMarker;
import hb403.geoexplore.datatype.marker.repository.EventRepository;
import hb403.geoexplore.datatype.marker.repository.ObservationRepository;
import hb403.geoexplore.datatype.marker.repository.ReportRepository;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;


@ServerEndpoint("/observations/comments/{userid}/{postid}")
@Component
@RestController
public class ObservationCommentWebsocket {//This is both the comment controller and chat websocket
    private CommentEntity currCommentor;
    private static CommentRepository commentRepository;

    public static ObservationRepository observationRepository;

    private static UserRepository userRepository;
    @Autowired
    public void autoCommentRepository(CommentRepository repo) {
        ObservationCommentWebsocket.commentRepository = repo;
    }

    @Autowired
    public void autoObservationRepository(ObservationRepository repo) {
        ObservationCommentWebsocket.observationRepository = repo;
    }

    @Autowired
    public void autoUserRepository(UserRepository repo) {
        ObservationCommentWebsocket.userRepository = repo;
    }

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();


    private static Map<Session, CommentEntity> sessionUserMap = new Hashtable<>();
    private static Map<CommentEntity, Session> UserSessionMap = new Hashtable<>();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ObservationCommentWebsocket.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param userid  username based of user id.
     * @param postid  id of post for storing
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userid") String userid, @PathParam("postid") long postid) throws IOException {

        // server side log
        logger.info("[onOpen] User " + userid + " joined session on post " + postid);

        this.currCommentor = new CommentEntity(userid, postid);

        // map current session with username
        sessionUsernameMap.put(session, userid);
        //sessionObservationMap.put (session,postid);
        // map current username with session
        usernameSessionMap.put(userid, session);
        //map current post/observation with session


        //users to filter by user id and postid this is more of a commentor entity, will need to clean this up before
        // big demo will hopefully replace both of the other pairs of hash tables
        sessionUserMap.put(session, currCommentor);
        UserSessionMap.put(currCommentor, session);
    }

    /**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // get the username by session
        CommentEntity tempuser = sessionUserMap.get(session);
        String tempuserid = tempuser.getUserId();
        Long tempPostId = tempuser.getPostId();

        // server side log
        logger.info("[onMessage] " + tempuserid + ": " + message);

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {
            // split by space
            String[] split_msg = message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
            String actualMessage = actualMessageBuilder.toString();
            sendMessageToPArticularUser(destUserName, "[DM from " + currCommentor.getUserId() + "]: " + actualMessage + " from post " + currCommentor.getPostId());
            sendMessageToPArticularUser(currCommentor.getUserId(), "[DM from " + currCommentor.getUserId() + "]: " + actualMessage + " from post " + currCommentor.getPostId());
        } else { // Message to whole chat underneath observation
            //postSessionMap.get(currCommentor.getPostId()).getBasicRemote().sendText(message);
            //sendMessageToPArticularObservation (currCommentor.getPostId(), message);
            broadcast(message, tempuser);
            //this.postid = currCommentor.getPostId();
            }
        }

        /**
         * Handles the closure of a WebSocket connection.
         *
         * @param session The WebSocket session that is being closed.
         */
        @OnClose
        public void onClose (Session session) throws IOException {

            // get the username from session-username mapping
            CommentEntity user = sessionUserMap.get(session);
            String username = user.getUserId();
            // server side log
            logger.info("[onClose] " + username);

            // remove user from memory mappings
            sessionUserMap.remove(session);
            UserSessionMap.remove(username);

            // send the message to chat
            //broadcast(username + " disconnected");
        }

        /**
         * Handles WebSocket errors that occur during the connection.
         *
         * @param session   The WebSocket session where the error occurred.
         * @param throwable The Throwable representing the error condition.
         */
        @OnError
        public void onError (Session session, Throwable throwable){

            // get the username from session-username mapping
            CommentEntity temp = sessionUserMap.get(session);

            // do error handling here
            logger.info("[onError]" + temp.getUserId() + ": " + throwable.getMessage());
        }

        /**
         * Sends a message to a specific user in the chat (DM).
         *
         * @param username The username of the recipient.
         * @param message  The message to be sent.
         */
        private void sendMessageToPArticularUser (String username, String message){
            try {
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[DM Exception] " + e.getMessage());
            }
        }

        /**
         * Broadcasts a message to all users in the chat.
         *
         * @param message The message to be broadcasted to all users.
         * @param sender the data going into a message to make it simpler
         */
        private void broadcast (String message, CommentEntity sender){
            CommentEntity toSave = new CommentEntity(sender.getUserId(),sender.getPostId(), "Observation" ,message);
            commentRepository.save(toSave);
                sessionUserMap.forEach((session, user) -> {
                    try {

                        session.getBasicRemote().sendText(message);
                        if (sender.getPostId().equals(user.getPostId())) {
                            //usernameSessionMap.get(user.getUserId()).getBasicRemote().sendText(message);
                            final ObservationMarker tempObservation = this.observationRepository.findById(currCommentor.getPostId()).get();
                            logger.info("Made it into adding to observation repository");
                            //final CommentEntity u = this.commentRepository.findById(toSave.getId()).get();
                            tempObservation.getComments().add(toSave); 	// if successful add
                            toSave.setPertainsObservationMarker(tempObservation);
                            observationRepository.save(tempObservation);
                            commentRepository.save(toSave);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            //});
        }

    }
