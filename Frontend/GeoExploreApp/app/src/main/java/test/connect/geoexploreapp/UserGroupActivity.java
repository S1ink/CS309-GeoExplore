package test.connect.geoexploreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.UserGroupApi;
import test.connect.geoexploreapp.model.User;
import test.connect.geoexploreapp.model.UserGroup;

public class UserGroupActivity extends Fragment {
    private RecyclerView recyclerView;
    private UserGroupAdapter adapter;
    private List<UserGroup> userGroups = new ArrayList<>();
    private TextView noUserGroupsText;
    private static Bundle args;

    private User user;
    public static UserGroupActivity newInstance(User user) {
        UserGroupActivity fragment = new UserGroupActivity();
        args = new Bundle();
        args.putSerializable("UserObject", user);

        fragment.setArguments(args);
            return fragment;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_groups, container, false);
        recyclerView = view.findViewById(R.id.userGroupsRecyclerView);
        noUserGroupsText = view.findViewById(R.id.noUserGroupsText);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("UserObject");
        }
        fetchUserGroups();
        return view;
    }

    private void fetchUserGroups() {
            UserGroupApi userGroupApi = ApiClientFactory.GetUserGroupApi();
            userGroupApi.getAllGroups().enqueue(new Callback<List<UserGroup>>() {
                @Override
                public void onResponse(Call<List<UserGroup>> call, Response<List<UserGroup>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        userGroups.clear();
                        userGroups.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        noUserGroupsText.setVisibility(userGroups.isEmpty() ? View.VISIBLE : View.GONE);
                    } else {
                        Log.e("UserGroupActivity", "Failed to fetch user groups: " + response.message());
                        noUserGroupsText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<UserGroup>> call, Throwable t) {
                    Log.e("UserGroupActivity", "Error fetching user groups", t);
                }
            });
            Log.d("user indo", user.getEmailId());
        adapter = new UserGroupAdapter(getContext(), userGroups, user);
        recyclerView.setAdapter(adapter);
    }

}