package test.connect.geoexploreapp;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.CommentApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.api.UserApi;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private User user;
    private CommentActionListener listener;

    public CommentAdapter(List<Comment> comments, User user, CommentActionListener listener) {
        this.comments = comments;
       this.user = user;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment item = comments.get(position);
        Log.d("username", item.getUserId());
        int atIndex = item.getUserId().indexOf("@");
        String userName;
        if (atIndex != -1) {
            userName = item.getUserId().substring(0, atIndex);
        } else {
            userName = item.getUserId();
        }
        holder.commentUser.setText(userName);
        holder.comment.setText(item.getComment());

        boolean isUserCommenter = item.getUserId().equals(user.getEmailId());
        boolean isAdmin =  user.getIsAdmin();

        holder.editButton.setVisibility(isUserCommenter ? View.VISIBLE : View.GONE);
        holder.deleteButton.setVisibility(isUserCommenter || isAdmin ? View.VISIBLE : View.GONE);

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    editCommentPrompt(v.getContext(), comments.get(pos));
                }
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                Log.d("delete test", String.valueOf(pos));
                if(pos != RecyclerView.NO_POSITION) {
                    deleteCommentPrompt(v.getContext(), pos);
                }
            }
        });

    }


    private void deleteCommentPrompt(Context context, int position) {
        Comment comment = comments.get(position);
        Log.d("delete", String.valueOf(comment.getId()) + comment.getUserId() + comment.getComment() + comment.getPostid()+comment.getPostType());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this comment?")

                .setPositiveButton("Delete", (dialog, which) -> {
                    listener.onDeleteComment(comment.getId(), position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();


    }


    private void editCommentPrompt(Context context,  Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(comment.getComment());
        builder.setTitle("Edit Comment")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String editedCommentText = input.getText().toString();
                    if(editedCommentText.length()!=0) {
                      //  edit();
                        listener.onEditComment(comment, editedCommentText);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();



    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setItems(List<Comment> allItems) {
        comments=allItems;
    }

    public List<Comment> getItems() {
        return comments;
    }



    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentUser, comment;
        ImageButton editButton, deleteButton;
        CommentViewHolder(View itemView) {
            super(itemView);
            commentUser = itemView.findViewById(R.id.commentUser);
            comment = itemView.findViewById(R.id.comment);
            editButton = itemView.findViewById(R.id.commentEdit); 
            deleteButton = itemView.findViewById(R.id.commentDelete); 

        }
    }
}
