package test.connect.geoexploreapp;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.UserGroupApi;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.User;
import test.connect.geoexploreapp.model.UserGroup;

public class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.UserGroupViewHolder> {
    private View.OnClickListener  onDeleteClickListener;
    private List<UserGroup> userGroups;
    private LayoutInflater inflater;
    private View.OnClickListener onJoinClickListener;
    private User user;

    public UserGroupAdapter(Context context, List<UserGroup> userGroups, User user, View.OnClickListener onJoinClickListener) {

        this.inflater = LayoutInflater.from(context);
        this.userGroups = userGroups;
        this.onJoinClickListener = onJoinClickListener;
        this.user = user;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public UserGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_group_item, parent, false);
        return new UserGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserGroupViewHolder holder, int position) {
        UserGroup userGroup = userGroups.get(position);
        holder.groupName.setText(userGroup.getTitle());
        holder.joinButton.setOnClickListener(v -> {
            v.setTag(userGroup);
            joinGroup(userGroup, position);
        });
        boolean isMember = false;
        for (User member : userGroup.getMembers()) {
            if (member.getId()==(user.getId())) {
                isMember = true;
                break;
            }
        }
        Log.d("UserGroupAdapter", "User: " + user);

        if (user != null && user.getIsAdmin()) {
            Log.d("UserGroupAdapter", "User is admin, showing buttons.");
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.updateButton.setVisibility(View.VISIBLE);

            holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(v.getContext(), userGroup.getId(), position));
            holder.updateButton.setOnClickListener(v -> showUpdatePrompt(v.getContext(), userGroup, position));
        } else {
            Log.d("UserGroupAdapter", "User is not admin, hiding buttons.");
            holder.deleteButton.setVisibility(View.GONE);
            holder.updateButton.setVisibility(View.GONE);
        }


        holder.joinButton.setText(isMember ? "Joined" : "Join");
        holder.joinButton.setEnabled(!isMember);
    }

    private void showUpdatePrompt(Context context, UserGroup userGroup,  int position) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(userGroup.getTitle());
        builder.setTitle("Edit Title")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String editedCommentText = input.getText().toString();
                    if(editedCommentText.length()!=0) {
                        updateGroup(userGroup, editedCommentText, position);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();




    }

    private void updateGroup(UserGroup userGroup, String editedCommentText, int position) {
        userGroup.setTitle(editedCommentText);
        UserGroupApi userGroupApi = ApiClientFactory.GetUserGroupApi(); userGroupApi.updateGroupById(userGroup.getId(), userGroup).enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userGroups.set(position, response.body());
                    notifyItemChanged(position);
                    Log.d("UserGroupAdapter", "Group updated successfully");
                } else {
                    Log.e("UserGroupAdapter", "Failed to update group");
                }
            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {
                Log.e("UserGroupAdapter", "Error updating group", t);
                // Optionally, show a Toast message here
            }
        });
    }

    private void joinGroup(UserGroup addToUserGroup, int position) {
        UserGroupApi userGroupApi = ApiClientFactory.GetUserGroupApi();

        Long userId = user.getId();

        userGroupApi.addMemberToGroupById(addToUserGroup.getId(), userId).enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                if (response.isSuccessful()) {
                    UserGroup updatedGroup = response.body();
                    updatedGroup.getMembers().add(user);
                    // Update your local userGroups list with the updatedGroup
                    userGroups.set(position, updatedGroup);
                    notifyItemChanged(position);
                    Log.d("UserGroupAdapter", "Successfully joined the group");
                    // Optionally, show a toast or update the UI
                } else {
                    Log.e("UserGroupAdapter", "Failed to join the group");
                    // Handle the error, show a toast, etc.
                }
            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {
                Log.e("UserGroupAdapter", "Error joining the group", t);
                // Handle the network error, show a toast, etc.
            }
        });
    }
    private void showDeleteConfirmationDialog(Context context, Long groupId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Group")
                .setMessage("Are you sure you want to delete this group?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteGroup(groupId, position))
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteGroup(Long id, int position) {
        UserGroupApi userGroupApi = ApiClientFactory.GetUserGroupApi();
        userGroupApi.deleteGroupById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserGroupAdapter", "Group deleted successfully");
                    userGroups.remove(position);

                    notifyDataSetChanged();
                } else {
                    Log.e("UserGroupAdapter", "Failed to delete group");
                   // Toast.makeText(context, "Failed to delete group", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserGroupAdapter", "Error deleting group", t);
               // Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return userGroups.size();
    }

    static class UserGroupViewHolder extends RecyclerView.ViewHolder {
         Button deleteButton;
        TextView groupName;
        Button joinButton;
        Button updateButton;
        public UserGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            joinButton = itemView.findViewById(R.id.joinButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            updateButton = itemView.findViewById(R.id.updateButton);

        }
    }
}
