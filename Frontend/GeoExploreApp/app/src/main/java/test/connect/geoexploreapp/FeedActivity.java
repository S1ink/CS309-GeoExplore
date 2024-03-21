package test.connect.geoexploreapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

         recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Assuming you have a method to get your feed items
        List<FeedItem> feedItems = getFeedItems();
        FeedAdapter adapter = new FeedAdapter(feedItems);
        recyclerView.setAdapter(adapter);
    }

    // Implement getFeedItems() to retrieve your data
    private List<FeedItem> getFeedItems() {
        List<FeedItem> allItems = new ArrayList<>();
        // This should return a list of your feed items
        FeedAdapter adapter = new FeedAdapter(allItems);
        recyclerView.setAdapter(adapter);
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        // Fetch ReportMarkers
        reportMarkerApi.GetAllReportMarker().enqueue(new SlimCallback<>(reportMarkers -> {
            allItems.addAll(reportMarkers);
            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
        }, "GetAllReports"));



        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        // Fetch EventMarkers
        eventMarkerApi.GetAllEventMarker().enqueue(new SlimCallback<>(eventMarkers -> {
            allItems.addAll(eventMarkers);
            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
        }, "GetAllEvents"));


        ObservationApi observationApi = ApiClientFactory.GetObservationApi();

        observationApi.getAllObs().enqueue(new SlimCallback<>(observations -> {
            allItems.addAll(observations);
            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
        }, "GetAllObservations"));

        return allItems;
    }
}
