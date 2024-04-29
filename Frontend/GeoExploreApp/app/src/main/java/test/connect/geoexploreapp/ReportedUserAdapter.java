package test.connect.geoexploreapp;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import test.connect.geoexploreapp.model.ReportedUser;

public class ReportedUserAdapter extends RecyclerView.Adapter<ReportedUserAdapter.ReportedUsersViewHolder> {
    private List<ReportedUser> allReportedUsers;
    private Context context;

    public ReportedUserAdapter(List<ReportedUser> allReportedUsers, Context context) {
        this.allReportedUsers = allReportedUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportedUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reported_user_item, parent, false);

        return new ReportedUsersViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReportedUsersViewHolder holder, int position) {
        ReportedUser reportedUser = allReportedUsers.get(position);
        Log.d("getting report for user", "getting user for" + reportedUser.getReportedUserId());

//        if (reportedUser.getReportedUser() != null) {
//            holder.reportedUserName.setText("Reported User: " + reportedUser.getReportedUser().getName());
//            holder.reportedUserEmailId.setText("Email: " + reportedUser.getReportedUser().getEmailId());
//        } else {
//            holder.reportedUserName.setText("Reported User: Unknown");
//            holder.reportedUserEmailId.setText("Email: Unknown");
//        }

        holder.report.setText("Reported for: ");

        if(reportedUser.getMisinformation()!=null&&reportedUser.getMisinformation()){
            holder.report.append("Missing Information, ");
        }

        if(reportedUser.getHarassment()!=null&&reportedUser.getHarassment()){
            holder.report.append("Harassment, ");
        }

        if(reportedUser.getSpamming()!=null&&reportedUser.getSpamming()){
            holder.report.append("Spam, ");
        }


        if(reportedUser.getInappropriateContent()!=null&&reportedUser.getInappropriateContent()){
            holder.report.append("Inappropriate Content, ");
        }


//
//        // holder.commentUser.setText(commentUser.getName());
//        holder.comment.setText(item.getComment());
//
//        boolean isUserCommenter = item.getUserId().equals(user.getId());
//        boolean isAdmin =  user.getIsAdmin();
//
//        holder.reportButton.setVisibility(!isUserCommenter && showFeatures ? View.VISIBLE : View.GONE);
//        holder.editButton.setVisibility(isUserCommenter && showFeatures? View.VISIBLE : View.GONE);
//        holder.deleteButton.setVisibility((isUserCommenter || isAdmin ) &&showFeatures ? View.VISIBLE : View.GONE);
//        holder.commentPostType.setVisibility(!showFeatures?View.VISIBLE : View.GONE);
//        holder.commentPostType.setText("Comment made for a " + item.getPostType());
//        holder.editButton.setOnClickListener(v -> {
//            if (listener != null) {
//                int pos = holder.getAdapterPosition();
//                if(pos != RecyclerView.NO_POSITION) {
//                    editCommentPrompt(v.getContext(), comments.get(pos), position);
//                }
//            }
//        });
//
//        holder.deleteButton.setOnClickListener(v -> {
//            if (listener != null) {
//                int pos = holder.getAdapterPosition();
//                Log.d("delete test", String.valueOf(pos));
//                if(pos != RecyclerView.NO_POSITION) {
//                    deleteCommentPrompt(v.getContext(), pos);
//                }
//            }
//        });
//
//        holder.reportButton.setOnClickListener(v -> {
//            User taggedUser = (User) holder.commentUser.getTag();
//            if (listener != null&&taggedUser!=null) {
//                int pos = holder.getAdapterPosition();
//                Log.d("report test", String.valueOf(pos));
//                if(pos != RecyclerView.NO_POSITION) {
//                    reportCommentPrompt(v.getContext(),taggedUser, pos);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return allReportedUsers.size();
    }

    public void setItems(List<ReportedUser> allReportedUsers) {
        this.allReportedUsers =allReportedUsers;
        notifyDataSetChanged();
    }


    static class ReportedUsersViewHolder extends RecyclerView.ViewHolder {
        TextView reportedUserName, report, reportedUserEmailId;
        ImageButton reportEdit, reportDelete, userDelete;
        ReportedUsersViewHolder(View itemView) {
            super(itemView);
            reportedUserName = itemView.findViewById(R.id.reportedUserName);
            reportedUserEmailId = itemView.findViewById(R.id.reportedUserEmailId);
            report = itemView.findViewById(R.id.report);
            reportEdit = itemView.findViewById(R.id.reportEdit);
            reportDelete = itemView.findViewById(R.id.reportDelete);
            userDelete = itemView.findViewById(R.id.userDelete);

        }
    }
}
