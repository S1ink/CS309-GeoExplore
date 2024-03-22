package test.connect.geoexploreapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import test.connect.geoexploreapp.api.EventMarkerApi;
import test.connect.geoexploreapp.api.ObservationApi;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.EventMarker;
import test.connect.geoexploreapp.model.FeedItem;
import test.connect.geoexploreapp.model.Observation;
import test.connect.geoexploreapp.model.ReportMarker;

public class FeedActivity extends Fragment {
    private RecyclerView recyclerView;
    private TextView noItemsDisplay;
    private List<FeedItem> allItems = new ArrayList<>();
    private FeedAdapter adapter;

    public FeedActivity() {
        // Required empty public constructor
    }


    public static FeedActivity newInstance(String param1, String param2) {
        FeedActivity fragment = new FeedActivity();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_feed, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         noItemsDisplay = view.findViewById(R.id.noItems);
         recyclerView = view.findViewById(R.id.recyclerViewFeed);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         adapter = new FeedAdapter(allItems);
         recyclerView.setAdapter(adapter);
         getFeedItems();
    }

    // Implement getFeedItems() to retrieve your data
    private List<FeedItem> getFeedItems() {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();

        // Fetch reports
        reportMarkerApi.GetAllReportMarker().enqueue(new SlimCallback<>(reportMarkers -> {
            allItems.addAll(reportMarkers);
            updateUI(adapter, allItems);
        }, "GetAllReports"));

        // Fetch EventMarkers
        eventMarkerApi.GetAllEventMarker().enqueue(new SlimCallback<>(eventMarkers -> {
            allItems.addAll(eventMarkers);
            updateUI(adapter, allItems);
            }, "GetAllEvents"));

        // Fetch observations
        observationApi.getAllObs().enqueue(new SlimCallback<>(observations -> {
            allItems.addAll(observations);
            updateUI(adapter, allItems);
        }, "GetAllObservations"));

        return allItems;
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
