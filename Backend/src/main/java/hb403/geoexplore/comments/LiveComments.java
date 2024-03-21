package hb403.geoexplore.comments;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


import hb403.geoexplore.comments.CommentRepo.CommentRepository;
import hb403.geoexplore.comments.Entity.CommentEntity;

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
public class LiveComments {//This is both the comment controller and chat websocket
    private CommentEntity currUser;
    @Autowired
    CommentRepository commentRepository;
    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<Session, Long> sessionObservationMap = new Hashtable<>();

    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private static Map<Long, Session> postSessionMap = new Hashtable<>();

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(LiveComments.class);

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

        this.currUser = new CommentEntity(userid, postid);
        // map current session with username
        sessionUsernameMap.put(session, userid);
        sessionObservationMap.put (session,postid);
        // map current username with session
        usernameSessionMap.put(userid, session);
        //map current post/observation with session
        postSessionMap.put(postid, session);
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
        String tempuserid = sessionUsernameMap.get(session);


        // server side log
        logger.info("[onMessage] " + tempuserid + ": " + message);

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {
            logger.info("It came here for some reason");
            // split by space
            String[] split_msg = message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
            String actualMessage = actualMessageBuilder.toString();
            sendMessageToPArticularUser(destUserName, "[DM from " + currUser.getUserid() + "]: " + actualMessage);
            sendMessageToPArticularUser(currUser.getUserid(), "[DM from " + currUser.getUserid() + "]: " + actualMessage);
        } else { // Message to whole chat underneath observation
            //postSessionMap.get(currUser.getPostid()).getBasicRemote().sendText(message);
            broadcast(message);
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
            String username = sessionUsernameMap.get(session);

            // server side log
            logger.info("[onClose] " + username);

            // remove user from memory mappings
            sessionUsernameMap.remove(session);
            usernameSessionMap.remove(username);

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
            String temp = sessionUsernameMap.get(session);

            // do error handling here
            logger.info("[onError]" + temp + ": " + throwable.getMessage());
        }

        /**
         * Sends a message to a specific user in the chat (DM).
         *
         * @param username The username of the recipient.
         * @param message  The message to be sent.
         */
        private void sendMessageToPArticularUser (String username, String message){
            try {
                logger.info("Something is wrong here");
                usernameSessionMap.get(username).getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.info("[DM Exception] " + e.getMessage());
            }
        }

        /**
         * Broadcasts a message to all users in the chat.
         *
         * @param message The message to be broadcasted to all users.
         */
        private void broadcast (String message){
            sessionObservationMap.forEach((session, postid) -> {
               // sessionUsernameMap.forEach((session, username) -> {
                    try {
                        //commentRepository.save(new CommentEntity(currUser,message));
                        session.getBasicRemote().sendText(message);

                    } catch (IOException e) {
                        logger.info("[Broadcast Exception] " + e.getMessage());
                    }
                });
            //});
        }

    }

