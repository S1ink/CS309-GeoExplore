package test.connect.geoexploreapp;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.CommentApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.EventMarker;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.model.Observation;
import test.connect.geoexploreapp.model.ReportMarker;
import test.connect.geoexploreapp.model.User;
import test.connect.geoexploreapp.websocket.WebSocketListener;
import test.connect.geoexploreapp.websocket.WebSocketManager;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> implements WebSocketListener, CommentActionListener{
    private List<FeedItem> items;
    private EditText commentEditText;
    private Button sendCommentButton;
    private Button cancelCommentButton;
    private User user;
    private  FeedItem item;
    private Context context;
    private CommentAdapter commentAdapter;

    public FeedAdapter(List<FeedItem> items, User user, Context context) {
        this.items = items;
        this.user=user;
        this.context = context;
        WebSocketManager.getInstance().setWebSocketListener(this);

    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        WebSocketManager.getInstance().setWebSocketListener(FeedAdapter.this);

        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, @SuppressLint("RecyclerView") int position) {
       item= items.get(position);


        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(item.getDescription());
        }

        holder.title.setText(item.getTitle());
        holder.type.setText(item.getType());
        holder.date.setText(item.getTime_created().toString());
        getLocation(holder, item.getIo_latitude(),  item.getIo_longitude());

        holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        List<Comment> commentsForThisItem = item.getComments();
        CommentAdapter commentAdapter = new CommentAdapter(commentsForThisItem, user, this);
        holder.commentsRecyclerView.setAdapter(commentAdapter);
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComments(holder.commentButton.getContext(),position);
                Log.d("item check", String.valueOf(position));
            }
        });
    }

    private void getLocation(FeedViewHolder holder, double ioLatitude, double ioLongitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(ioLatitude, ioLongitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0); // You might adjust this based on what parts of the address you need
                holder.location.setText("Address: " + addressText);
            } else {
                holder.location.setText("Address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            holder.location.setText("Error fetching address");
        }
    }

    private void showComments(Context context, int itemIndex) {
        Gson gson = new Gson();
        FeedItem item = items.get(itemIndex);
//        String itemType = item.getType().toLowerCase() + "s";
//        String wsUrl = "ws://coms-309-005.class.las.iastate.edu:8080/" + itemType + "/comments/" + user.getEmailId() + "/" + item.getPostID();
//        Log.d("URL", wsUrl);
//        WebSocketManager.getInstance().connectWebSocket(wsUrl);
//
//        Log.d("Websocket Connection ","connected");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog = inflater.inflate(R.layout.activity_comment, null);

        commentEditText = dialog.findViewById(R.id.editTextComment);
        sendCommentButton = dialog.findViewById(R.id.buttonSend);
        cancelCommentButton = dialog.findViewById(R.id.buttonCancel);

        builder.setView(dialog).setTitle("Comments");
        AlertDialog dialogView = builder.create();

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString().trim();
                if(!commentText.isEmpty()) {
                    try {
                       Comment sendComment = new Comment();
                        sendComment.setComment(commentText);
                        sendComment.setPostId(item.getPostID());
                        sendComment.setUserId(user.getId());
                        sendComment.setPostType(item.getType());
                        String sendCommentJson = gson.toJson(sendComment);
                        Log.d("WebSocket", "Message sent testing: " + sendCommentJson);

                        WebSocketManager.getInstance().sendMessage(sendCommentJson);
                        Log.d("WebSocket", "Message sent: " + sendCommentJson);
                        fetchComments(itemIndex, item, item.getType());
//                      item.getComments().add(sendComment);
                        notifyItemChanged(itemIndex);
                    } catch (Exception e) {
                        Log.e("WebSocket", "Error sending message: " + e.getMessage());
                    }
                }
            }
        });

        cancelCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogView.dismiss();

            }
        });

        dialogView.show();
    }

    private void fetchComments(int itemIndex, FeedItem item, String type) {
        if(type.equals("Report")){
            ReportMarker rep = (ReportMarker) item;
            fetchCommentsForReport(itemIndex, rep);

        }else if(type.equals("Observation")){
            Observation obs = (Observation) item;
            fetchCommentsForObservation(itemIndex, obs);

        }else if(type.equals("Event")){
            EventMarker event = (EventMarker) item;
            fetchCommentsForEvent(itemIndex, event);

        }
    }

    private void fetchCommentsForEvent(int itemIndex, EventMarker event) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.getCommentsForEvents(event.getId()).enqueue(new SlimCallback<>(comments -> {
            event.setComments(comments);
            notifyItemChanged(itemIndex);

        }, "GetCommentsForEvent"));
    }

    private void fetchCommentsForObservation(int itemIndex, Observation obs) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();

        commentApi.getCommentsForObs(obs.getId()).enqueue(new SlimCallback<>(comments -> {
            obs.setComments(comments);
            notifyItemChanged(itemIndex);

        }, "GetCommentsForObs"));
    }

    private void fetchCommentsForReport(int itemIndex, ReportMarker rep) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.getCommentsForReports(rep.getId()).enqueue(new SlimCallback<>(comments -> {
            rep.setComments(comments);
            Log.d("FeedActivity", "Fetched Comments: " + comments.toString());
            notifyItemChanged(itemIndex);

        }, "GetCommentsForReport"));
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<FeedItem> allItems) {
        items=allItems;
    }

    public List<FeedItem> getItems() {
        return items;
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("WebSocket", "Connection opened");

    }

    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Received message: " + message);
        Activity activity = (Activity) context;

        if (message != null && !message.isEmpty()) {
            try {
                Comment receivedComment = new Gson().fromJson(message, Comment.class);
//                String actualCommentText = receivedComment.getComment();
//                 Comment receivedComment1 = new Gson().fromJson(actualCommentText, Comment.class);

                activity.runOnUiThread(() -> {
                    setCommment(receivedComment);
                });
            } catch (JsonSyntaxException e) {
                Log.e("WebSocket", "Error parsing message to Comment object", e);
            }
        }
    }

    private void setCommment(Comment receivedComment) {
        Log.d("trying to set comment", "pks wor");
        for (FeedItem item : items) {
            if (item.getPostID().equals(receivedComment.getPostid()) && item.getType().equals(receivedComment.getPostType())) {
                item.getComments().add(receivedComment);
                notifyDataSetChanged();
                break;
            }
        }
    }


    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.d("WebSocket Close", "Code: " + code + ", reason: " + reason);
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("WebSocket Error", ex.toString());
    }

    @Override
    public void onDeleteComment(Long commentId, int position) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        Log.d("checkkk",commentId.toString());
       // Toast.makeText(context, "Fai delete comment", Toast.LENGTH_SHORT).show();

        commentApi.deleteComment(commentId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  JsonReader.setLenient(true);
                Log.d("DeleteComment",  response.body().toString());

                if (response.isSuccessful()) {
                    ResponseBody responseMessage = response.body(); // Now directly a String

                    Log.d("DeleteComment", "Successfully deleted comment: " + response.body());
                    for (FeedItem item : items) {
                        List<Comment> comments = item.getComments();
                        if (comments.removeIf(comment -> comment.getId().equals(commentId))) {
                            notifyDataSetChanged();
                            Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to delete comment", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("DeleteComment", "failedt: " + t.getMessage());

            }
        });
    }

    @Override
    public void onEditComment(Comment updatedComment, String newCommentText, int position) {
        Log.d("checking if edit works",updatedComment.toString() );
        updatedComment.setComment(newCommentText);
//        Comment newComment = new Comment();
//        newComment.setId(updatedComment.getId()); // Set the ID of the comment to update
//        newComment.setComment(newCommentText);
//        newComment.setPostid(updatedComment.getPostid());
//        newComment.setUserId(updatedComment.getUserId());
//        newComment.setPostType(updatedComment.getPostType());
        Gson gson = new Gson();
        String updatedCommentJson = gson.toJson(updatedComment);
        Log.d("UpdateComment", "Updated comment JSON: " + updatedCommentJson);
        updateCommentInBackend(updatedComment, position);

    }

    private void updateCommentInBackend(Comment updatedComment, int position) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.updateComment(updatedComment.getId(), updatedComment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Comment updatedComment = response.body();
                    Log.d("UpdateComment", "Comment updated successfully: " + updatedComment.getComment());
                    for (FeedItem item : items) {
                        if (item.getPostID().equals(updatedComment.getPostid()) && item.getType().equals(updatedComment.getPostType())) {
                           List<Comment> comm= item.getComments();
                            for (Comment comment : comm) {
                                if (comment.getId().equals(updatedComment.getId())) {
                                    comment.setComment(updatedComment.getComment());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    notifyItemChanged(position);
                   Toast.makeText(context, "Comment updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("UpdateComment", "Failed to update comment. Response Code: " + response.code() + ", Message: " + response.message());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("UpdateComment", "Error Body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("UpdateComment", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(context, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UpdateComment", "Error updating comment", t.getCause());
            }
        });
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView commentsRecyclerView;
        TextView title, description, type, date, location;
        ImageButton commentButton;

        FeedViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            type = itemView.findViewById(R.id.itemType);
            date = itemView.findViewById(R.id.itemDate);
            location = itemView.findViewById(R.id.itemLocation);
            commentButton = itemView.findViewById(R.id.commentButton);
            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
        }
    }
}
