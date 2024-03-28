package test.connect.geoexploreapp;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.websocket.WebSocketListener;
import test.connect.geoexploreapp.websocket.WebSocketManager;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> implements WebSocketListener{
    private List<FeedItem> items;
    private EditText commentEditText;
    private Button sendCommentButton;
    private Button cancelCommentButton;
    private String comment;
    private String userName;
    private  FeedItem item;
    private Context context;

    public FeedAdapter(List<FeedItem> items, String userName, Context context) {
        this.items = items;
        this.userName=userName;
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
        CommentAdapter commentAdapter = new CommentAdapter(item.getComments());
        holder.commentsRecyclerView.setAdapter(commentAdapter);

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComments(holder.commentButton.getContext(),position);
                Log.d("item check", String.valueOf(position));
            }
        });

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
                        Comment sendComment = new Comment(comment, userName, (long) itemIndex);

                        String sendCommentJson = gson.toJson(sendComment);
                        WebSocketManager.getInstance().sendMessage(sendCommentJson);
                        Log.d("WebSocket", "Message sent: " + comment);
                        FeedItem item = items.get(itemIndex);
                        item.getComments().add(sendComment);
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
                        JSONObject jsonMessage = new JSONObject(message);
                        String userEmailid = jsonMessage.getString("userEmailid");
                        String comment = jsonMessage.getString("comment");
                        Long postid = jsonMessage.getLong("postid");
                        Comment newComment = new Comment(comment, userEmailid, postid);
                        int index = Math.toIntExact(postid);
                        FeedItem item = items.get(index);
                        item.getComments().add(newComment);
                        notifyDataSetChanged();
                        //display comment

                    } catch (JSONException e) {
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
