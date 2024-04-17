package test.connect.geoexploreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.ReportedUserApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.ReportedUser;

public class ReportedUserActivity extends Fragment {

    private TextView noItemsDisplay;
    private ReportedUserAdapter reportedUserAdapter;
    private RecyclerView recyclerView;
    private List<ReportedUser> allReportedUsers = new ArrayList<>();


    public ReportedUserActivity() {
    }


    public static ReportedUserActivity newInstance() {
        ReportedUserActivity fragment = new ReportedUserActivity();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reported_users, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchReportedUsers();
        recyclerView = view.findViewById(R.id.reportedUsersRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noItemsDisplay = view.findViewById(R.id.noItems);

        reportedUserAdapter = new ReportedUserAdapter(allReportedUsers, getActivity());
        recyclerView.setAdapter(reportedUserAdapter);
    }

    private void fetchReportedUsers() {
        Log.d("fetchReportedUsers", "Fetching reports for users ");

        ReportedUserApi reportedUserApi = ApiClientFactory.GetReportedUserApi();
        reportedUserApi.ListOfReports().enqueue(new Callback<List<ReportedUser>>() {
            @Override
            public void onResponse(Call<List<ReportedUser>> call, Response<List<ReportedUser>> response) {
                if(response.isSuccessful()&& response.body() != null&&!response.body().isEmpty()){
                    allReportedUsers.clear();
                    allReportedUsers.addAll(response.body());
                    reportedUserAdapter.notifyDataSetChanged();
                    Log.d("fetchReportedUsers", "Fetching reports for users: Successful ");
                    updateUI(allReportedUsers);
                }
            }

            @Override
            public void onFailure(Call<List<ReportedUser>> call, Throwable t) {
                Log.d("fetchReportedUsers", "Fetching reports for users: Failed");
            }
        });

    }

    private void updateUI(List<ReportedUser> allReportedUsers) {
        if (allReportedUsers.isEmpty()) {
            noItemsDisplay.setVisibility(View.VISIBLE);
        } else {
            noItemsDisplay.setVisibility(View.GONE);
        }
    }
}
