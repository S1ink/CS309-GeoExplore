package test.connect.geoexploreapp;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.java_websocket.handshake.ServerHandshake;

import java.util.List;

import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.websocket.WebSocketListener;
import test.connect.geoexploreapp.websocket.WebSocketManager;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> items;
    private EditText commentEditText;
    private Button sendCommentButton;
    private Button cancelCommentButton;
    private String comment;
    private String userName;

    public CommentAdapter(List<Comment> items, String userName) {
        this.items = items;
        this.userName=userName;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment item = items.get(position);

        holder.commentUser.setText(item.getUserEmailid());
        holder.comment.setText(item.getComment());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Comment> allItems) {
        items=allItems;
    }

    public List<Comment> getItems() {
        return items;
    }



    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUser, comment;

        CommentViewHolder(View itemView) {
            super(itemView);
            commentUser = itemView.findViewById(R.id.commentUser);
            comment = itemView.findViewById(R.id.comment);

        }
    }
}
