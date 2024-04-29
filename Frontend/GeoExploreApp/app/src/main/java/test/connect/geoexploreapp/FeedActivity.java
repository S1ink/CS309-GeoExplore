package test.connect.geoexploreapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.CommentApi;
import test.connect.geoexploreapp.api.EventMarkerApi;
import test.connect.geoexploreapp.api.ImageApi;
import test.connect.geoexploreapp.api.ObservationApi;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.api.UserApi;
import test.connect.geoexploreapp.model.Comment;
import test.connect.geoexploreapp.model.EventMarker;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.model.Image;
import test.connect.geoexploreapp.model.Observation;
import test.connect.geoexploreapp.model.ReportMarker;
import test.connect.geoexploreapp.model.User;

public class FeedActivity extends Fragment {
    private RecyclerView recyclerView;
    private TextView noItemsDisplay;
    private ImageButton viewAllCommentsButton;
    private ImageButton searchComment;
    private List<FeedItem> allItems = new ArrayList<>();
    private List<Image> allImages = new ArrayList<>();
    private FeedAdapter adapter;
    private static User user;
    private List<Comment> allComments = new ArrayList<>();

    private static Bundle args;
    public FeedActivity() {
    }


    public static FeedActivity newInstance(User user) {
        FeedActivity fragment = new FeedActivity();
        args = new Bundle();
        args.putSerializable("UserObject", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feed, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFeedItems();
        fetchAllImages();
        viewAllCommentsButton=view.findViewById(R.id.viewAllComments);
        searchComment=view.findViewById(R.id.searchComment);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("UserObject");
        }

        if(user.getIsAdmin()){
            viewAllCommentsButton.setVisibility(View.VISIBLE);
            searchComment.setVisibility(View.VISIBLE);

            viewAllCommentsButton.setOnClickListener(v -> {
                fetchAllComments();
                //showAllCommentsPopup();
            });
            searchComment.setOnClickListener(v -> {
                searchCommentPrompt();
            });
        }else{
            viewAllCommentsButton.setVisibility(View.GONE);
            searchComment.setVisibility(View.GONE);
        }

         noItemsDisplay = view.findViewById(R.id.noItems);
         recyclerView = view.findViewById(R.id.recyclerViewFeed);

         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

         adapter = new FeedAdapter(allItems,allImages, user, getActivity());
         recyclerView.setAdapter(adapter);



//        getFeedItems();
    }

    private void searchCommentPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Comment's ID: ");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            Long id = null;
            try {
                id = Long.parseLong(input.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Please enter a valid ID.", Toast.LENGTH_SHORT).show();
            }

            if (id != null) {
                fetchCommentById(id);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void fetchCommentById(Long id) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        Log.d("fetchCommentById", "Fetching comment with ID: " + id);
        commentApi.getComment(id).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.isSuccessful() &&response!=null){
                    Comment comment = response.body();
                    Log.d("fetchCommentById", "Fetch successful. Comment: " + comment.toString());

                    getUserById(comment, comment.getUserId());
                }else{


                    if (response.errorBody() != null) {
                        try {
                            Log.e("fetchCommentById", "Error fetching comment: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e("fetchCommentById", "Error parsing error body", e);
                        }
                    } else {
                        Log.e("fetchCommentById", "Unsuccessful fetch, but no error body.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("fetchCommentById", "Fetch failed", t); // Debug log for failure

            }
        });
    }

    private void getUserById(Comment comment, Long userId) {
        UserApi userApi = ApiClientFactory.GetUserApi();
        userApi.getUser(userId).enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    showCommentDetails(comment, user);

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


    private void showCommentDetails(Comment comment, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Comment Details");

        String message = "Comment: " + comment.getComment() + "\nUser name: " + user.getName() + "\nPost Id: " + comment.getPostid() + "\nPost Type: " + comment.getPostType();
        builder.setMessage(message);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void showAllCommentsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View popupView = getLayoutInflater().inflate(R.layout.all_comments, null);
        RecyclerView commentsRecyclerView = popupView.findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CommentAdapter adapter = new CommentAdapter(allComments, user, null, false);
        commentsRecyclerView.setAdapter(adapter);

        builder.setView(popupView)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void fetchAllComments() {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.getAllComments().enqueue(new Callback<List<Comment>>(){
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allComments.clear();
                    allComments.addAll(response.body());

                    showAllCommentsPopup();
                } else {
                    Log.e("fetchAllComments", "Failed to fetch comments: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("fetchAllComments", "API call failed: " + t.getMessage());
            }
        });
    }

    private void fetchAllImages() {
        Log.e("fetchAllImages", "fetching images: " );

        ImageApi imageApi = ApiClientFactory.GetImageApi();
        imageApi.listImageEntities().enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("fetchAllImages", "fetching images: Successful" );

                    allImages.clear();
                    allImages.addAll(response.body());
                } else {
                    Log.e("fetchAllImages", "Failed to fetch images: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.e("fetchAllImages", "API call failed: " + t.getMessage());

            }
        });
    }

    private void getFeedItems() {
        allItems.clear();
        fetchReports();
        fetchEvents();
        fetchObservations();
    }

    private void fetchReports() {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();
        reportMarkerApi.GetAllReportMarker().enqueue(new SlimCallback<>(reportMarkers -> {
            for (ReportMarker reportMarker : reportMarkers) {
                allItems.add(reportMarker);
                fetchCommentsForReport(reportMarker);
            }

            updateUI(adapter, allItems);
        }, "GetAllReports"));
    }

    private void fetchCommentsForReport(ReportMarker reportMarker) {
        Log.d("comment fetching", " fetching for " + reportMarker.getId());
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.getCommentsForReports(reportMarker.getId()).enqueue(new SlimCallback<>(comments -> {
            reportMarker.setComments(comments);
            Log.d("FeedActivity", "Fetched Comments for Report " + reportMarker.getId() + ": " + comments);


            updateUI(adapter, allItems);

        }, "GetCommentsForReport"));
    }

    private void fetchEvents() {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        // Fetch EventMarkers
        eventMarkerApi.GetAllEventMarker().enqueue(new SlimCallback<>(eventMarkers -> {
            for (EventMarker eventMarker : eventMarkers) {
                allItems.add(eventMarker);
                fetchCommentsForEvent(eventMarker);
            }
            updateUI(adapter, allItems);
        }, "GetAllEvents"));
    }

    private void fetchCommentsForEvent(EventMarker eventMarker) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.getCommentsForEvents(eventMarker.getId()).enqueue(new SlimCallback<>(comments -> {
            eventMarker.setComments(comments);
            updateUI(adapter, allItems);

        }, "GetCommentsForEvent"));
    }

    private void fetchObservations() {
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();
        observationApi.getAllObs().enqueue(new SlimCallback<>(observations -> {
            for (Observation obs : observations) {
                allItems.add(obs);
                fetchCommentsForObservation(obs);
            }
            updateUI(adapter, allItems);
        }, "GetAllObservations"));
    }

    private void fetchCommentsForObservation(Observation obs) {
        CommentApi commentApi = ApiClientFactory.GetCommentApi();

        commentApi.getCommentsForObs(obs.getId()).enqueue(new SlimCallback<>(comments -> {
            obs.setComments(comments);
            updateUI(adapter, allItems);

        }, "GetCommentsForObs"));
    }

    private void updateUI(FeedAdapter adapter, List<FeedItem> allItems) {
        adapter.setItems(allItems);
        adapter.notifyDataSetChanged();

        if (allItems.isEmpty()) {
            noItemsDisplay.setVisibility(View.VISIBLE);
        } else {
            noItemsDisplay.setVisibility(View.GONE);
        }
    }

}
