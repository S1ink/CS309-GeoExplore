package test.connect.geoexploreapp;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.CommentApi;
import test.connect.geoexploreapp.api.ObservationApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.model.Observation;
import test.connect.geoexploreapp.model.User;
import test.connect.geoexploreapp.websocket.WebSocketListener;
import test.connect.geoexploreapp.websocket.WebSocketManager;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> implements WebSocketListener, CommentActionListener{
    private List<FeedItem> items;
    private EditText commentEditText;
    private Button sendCommentButton;
    private Button cancelCommentButton;
    private String comment;
    private User user;
    private  FeedItem item;
    private Context context;
    private List<Comment> comments = new ArrayList<>();
    private CommentAdapter commentAdapter;

    public FeedAdapter(List<FeedItem> items, User user, Context context) {
        this.items = items;
        this.user=user;
        this.context = context;
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

        if (item.getDepartment() == null || item.getDepartment().isEmpty()) {
            holder.department.setVisibility(View.GONE);
        } else {
            holder.department.setVisibility(View.VISIBLE);
            holder.department.setText(item.getDepartment());
        }

        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(item.getDescription());
        }

        holder.title.setText(item.getTitle());
        holder.type.setText(item.getType());
        holder.date.setText(item.getTime_created().toString());
       // holder.location.setText("Address: " + item.getLocation());

        holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        //get Comments using Read

        getComments(item.getType(), item.getPostID());

        commentAdapter = new CommentAdapter(comments, user, (CommentActionListener) this);
        holder.commentsRecyclerView.setAdapter(commentAdapter);
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComments(holder.commentButton.getContext(),position);
                Log.d("item check", String.valueOf(position));
            }
        });
    }

    private void getComments(String type, Long postId) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();

        if(type.equals("Report")){
            commentApi.getCommentsForReports(postId).enqueue(new SlimCallback<>(reportComments->{
                for (Comment reportComment : reportComments) {
                    comments.add(reportComment);
                }
            }, "getCommentsForReports"));



        }else if(type.equals("Observation")){
            commentApi.getCommentsForObs(postId).enqueue(new SlimCallback<>(obsComments->{
                for (Comment obsComment : obsComments) {
                    comments.add(obsComment);
                }
            }, "getCommentsForReports"));

        }else if(type.equals("Event")){
            commentApi.getCommentsForEvents(postId).enqueue(new SlimCallback<>(eventComments->{
                for (Comment eventComment : eventComments) {
                    comments.add(eventComment);
                }
            }, "getCommentsForReports"));

        }


    }

    private void showComments(Context context, int itemIndex) {
        Gson gson = new Gson();
        Log.d("Websocket Connection ","connected");

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
                comment = commentEditText.getText().toString().trim();
                if(comment.length()>0 && comment!=null) {
                    try {
                        FeedItem item = items.get(itemIndex);

                        Comment sendComment = new Comment(user.getEmailId(), item.getPostID(), item.getType(), comment);

                        String sendCommentJson = gson.toJson(sendComment);
                        WebSocketManager.getInstance().sendMessage(sendCommentJson);
                        Log.d("WebSocket", "Message sent: " + comment);
                        //
                        comments.add(sendComment);
                        notifyDataSetChanged();
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

    @Override
    public void onWebSocketMessage(String message) {
        Log.d("WebSocket", "Received message: " + message);

       //message show up in the recycler view
        Activity activity = (Activity) context;
        if(message.length()>0 && message!=null&&message.contains("postid")&&message.contains("userEmailid")&&message.contains("comment")) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Comment newComment = new Gson().fromJson(message, Comment.class);

                        comments.add(newComment);
                      notifyDataSetChanged();
                        //display comment

                    } catch (JsonSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }


    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("WebSocket Error", ex.toString());
    }

    @Override
    public void onDeleteComment(Long commentId, int position) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.deleteComment(commentId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    comments.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditComment(Comment comment, String newComment) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        comment.setComment(newComment);
        Log.d("Updating...", comment.getComment());

        commentApi.updateComment(comment.getId(), comment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Comment updated successfully", Toast.LENGTH_SHORT).show();
                    });
                    Log.d("Update", "Updated correctly");
                } else {
                    Log.d("Update failed", response.message());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                 t.getCause();

            }
        });
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView commentsRecyclerView;
        TextView title, description, department, type, date, location;
        ImageButton commentButton;

        FeedViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.itemDescription);
            department = itemView.findViewById(R.id.itemDepartment);
            type = itemView.findViewById(R.id.itemType);
            date = itemView.findViewById(R.id.itemDate);
            location = itemView.findViewById(R.id.itemLocation);
            commentButton = itemView.findViewById(R.id.commentButton);
            commentsRecyclerView = itemView.findViewById(R.id.commentsRecyclerView);
        }
    }
}
