package test.connect.geoexploreapp;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.ReportedUserApi;
import test.connect.geoexploreapp.api.UserApi;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.ReportedUser;
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
        User commentUser = getUserById(holder, item.getUserId());;

        if(commentUser!=null){
            holder.commentUser.setText(commentUser.getName());
        }
       // holder.commentUser.setText(commentUser.getName());
        holder.comment.setText(item.getComment());

        boolean isUserCommenter = item.getUserId().equals(user.getId());
        boolean isAdmin =  user.getIsAdmin();

        holder.reportButton.setVisibility(isUserCommenter ? View.GONE : View.VISIBLE );
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

        holder.reportButton.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                Log.d("report test", String.valueOf(pos));
                if(pos != RecyclerView.NO_POSITION) {
                    reportCommentPrompt(v.getContext(),commentUser, pos);
                }
            }
        });

    }

    private void reportCommentPrompt(Context context, User commentUser, int position) {
        Comment comment = comments.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Report User");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.activity_report_user, null);
        CheckBox harassmentCheck = dialogView.findViewById(R.id.harassment);
        CheckBox missingInformationCheck = dialogView.findViewById(R.id.missingInformation);
        CheckBox spammingCheck = dialogView.findViewById(R.id.spamming);
        CheckBox inappropriateContentCheck = dialogView.findViewById(R.id.inappropriateContent);
        builder.setView(dialogView);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReportedUser reportedUser = new ReportedUser();
                reportedUser.setReportedUserId(comment.getUserId());
                reportedUser.setReportedUser(commentUser);
                reportedUser.setHarassment(harassmentCheck.isChecked());
                reportedUser.setSpamming(spammingCheck.isChecked());
                reportedUser.setMisinformation(missingInformationCheck.isChecked());
                reportedUser.setInappropriateContent(inappropriateContentCheck.isChecked());
                reportedUser.setNumReports(0);
                createReport(context,reportedUser);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    private void createReport(Context context, ReportedUser reportedUser) {
        ReportedUserApi reportedUserApi = ApiClientFactory.GetReportedUserApi();
        reportedUserApi.ReportUser(reportedUser).enqueue(new Callback<ReportedUser>() {
            @Override
            public void onResponse(Call<ReportedUser> call, Response<ReportedUser> response) {
                if (response.isSuccessful()) {
                    ReportedUser reportedUser = response.body();
                    String successMessage = "Report created successfully with ID: " + reportedUser.getId();
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to create tag", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportedUser> call, Throwable t) {
                Toast.makeText(context, "Error creating report: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private User getUserById(@NonNull CommentViewHolder holder, Long userId) {
        UserApi userApi = ApiClientFactory.GetUserApi();
        User user = null;
        userApi.getUser(userId).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();

                    Log.d("getting a user",  "got  user");
                } else{
                    Log.d("getting a user",  "Failed to get user");
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("getting a user",  "Failed");
            }
        });
        return user;

    }

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
        ImageButton editButton, deleteButton, reportButton;
        CommentViewHolder(View itemView) {
            super(itemView);
            commentUser = itemView.findViewById(R.id.commentUser);
            comment = itemView.findViewById(R.id.comment);
            editButton = itemView.findViewById(R.id.commentEdit); 
            deleteButton = itemView.findViewById(R.id.commentDelete);
            reportButton = itemView.findViewById(R.id.commentReport);

        }
    }
}
