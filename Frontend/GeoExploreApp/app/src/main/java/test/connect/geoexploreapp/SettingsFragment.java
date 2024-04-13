package test.connect.geoexploreapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.UserGroupApi;
import test.connect.geoexploreapp.model.UserGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(boolean isAdmin) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putBoolean("IsAdmin", isAdmin); // get user admin status
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Bundle args = getArguments();

        boolean isAdmin = false;

        if(args != null){
            isAdmin = args.getBoolean("IsAdmin",false);
            Log.d("SettingsActivity", "isAdmin: " + isAdmin);

            Button btnEmergencyDashboard = view.findViewById(R.id.emergencyDashButton);
            Button btnMarkerTagManagement = view.findViewById(R.id.markerTagMngmtBtn);
            Button btnCreateUserGroup = view.findViewById(R.id.createUserGroupButton);

            if (isAdmin) {
                btnEmergencyDashboard.setVisibility(View.VISIBLE);
                btnMarkerTagManagement.setVisibility(View.VISIBLE);
                btnCreateUserGroup.setVisibility(View.VISIBLE);


                btnEmergencyDashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment emergencyDashboardFragment = new EmergencyDashboardFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame, emergencyDashboardFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

                btnMarkerTagManagement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment markerTagManagementFragment = new MarkerTagManagementFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame, markerTagManagementFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            } else {
                btnEmergencyDashboard.setVisibility(View.GONE);
            }

            btnCreateUserGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addUserGroupPrompt();
                }
            });

            
        }



        return view;
    }

    private void addUserGroupPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create User Group");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Add the title of the user group");

        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = input.getText().toString();
                // Call your method to handle the creation of the group with the entered name
                UserGroup userGrouptoAdd = new UserGroup();
                userGrouptoAdd.setTitle(groupName);
                addUserGroup(userGrouptoAdd);
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

    private void addUserGroup(UserGroup group) {
        UserGroupApi userGroupApi = ApiClientFactory.GetUserGroupApi();

        userGroupApi.addGroup(group).enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                if(response.isSuccessful()) {
                    UserGroup createdGroup = response.body(); // The created group object, if the API returns it
                    Log.d("addUserGroup", "Group created successfully: " + createdGroup.getTitle());
                } else {
                    // The request failed, handle errors
                    Log.e("addUserGroup", "Failed to create group. Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {
                // An error occurred during the network request
                Log.e("addUserGroup", "Error creating group", t);
            }
        });
    }
}