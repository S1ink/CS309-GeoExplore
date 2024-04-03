package test.connect.geoexploreapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.CommentApi;
import test.connect.geoexploreapp.api.EventMarkerApi;
import test.connect.geoexploreapp.api.ObservationApi;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.EventMarker;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.model.Observation;
import test.connect.geoexploreapp.model.ReportMarker;
import test.connect.geoexploreapp.model.User;

public class FeedActivity extends Fragment {
    private RecyclerView recyclerView;
    private TextView noItemsDisplay;
    private List<FeedItem> allItems = new ArrayList<>();
    private FeedAdapter adapter;
    private static User user;
    private static Bundle args;
    public FeedActivity() {
        // Required empty public constructor
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

         noItemsDisplay = view.findViewById(R.id.noItems);
         recyclerView = view.findViewById(R.id.recyclerViewFeed);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("UserObject");
        }
         adapter = new FeedAdapter(allItems, user, getActivity());
         recyclerView.setAdapter(adapter);
         getFeedItems();
    }

    // Implement getFeedItems() to retrieve your data
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
        CommentApi commentApi = ApiClientFactory.GetCommentApi();
        commentApi.getCommentsForReports(reportMarker.getId()).enqueue(new SlimCallback<>(comments -> {
            reportMarker.setComments(comments);
            Log.d("FeedActivity", "Fetched Comments: " + comments.toString());

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
