package test.connect.geoexploreapp;

import static com.google.android.material.internal.ContextUtils.getActivity;

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
import test.connect.geoexploreapp.model.Observation;
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
        Log.d("getting user", "getting user for" + item.getUserId());
         getUserById(holder, item.getUserId());;

        holder.comment.setText(item.getComment());

        boolean isUserCommenter = item.getUserId().equals(user.getId());
        boolean isAdmin =  user.getIsAdmin();

        holder.editButton.setVisibility(isUserCommenter ? View.VISIBLE : View.GONE);
        holder.deleteButton.setVisibility(isUserCommenter || isAdmin ? View.VISIBLE : View.GONE);

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION) {
                    editCommentPrompt(v.getContext(), comments.get(pos), position);
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

    private void getUserById(@NonNull CommentViewHolder holder, Long userId) {
        UserApi userApi = ApiClientFactory.GetUserApi();
        userApi.getUser(userId).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    holder.commentUser.setText(user.getName());

                    Log.d("getting a user",  "got  user");
                } else{
                    Log.d("getting a user",  "Failed to get user");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("getting a user",  "Failed");
            }
        });}

    private void deleteCommentPrompt(Context context, int position) {
        Comment commentDel = comments.get(position);
        Log.d("delete", commentDel.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this comment?")

                .setPositiveButton("Delete", (dialog, which) -> {
                    listener.onDeleteComment(commentDel.getId(), position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();


    }


    private void editCommentPrompt(Context context,  Comment comment, int position) {
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
                        listener.onEditComment(comment, editedCommentText, position);
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
