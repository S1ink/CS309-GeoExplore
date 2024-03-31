package test.connect.geoexploreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsActivity() {
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
    public static SettingsActivity newInstance(boolean isAdmin) {
        SettingsActivity fragment = new SettingsActivity();
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
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        Bundle args = getArguments();

        boolean isAdmin = false;

        if(args != null){
            isAdmin = args.getBoolean("IsAdmin",false);
            Log.d("SettingsActivity", "isAdmin: " + isAdmin);

            Button btnSendEmergency = view.findViewById(R.id.sendEmergencyButton);
            Button btnEmergencyDashboard = view.findViewById(R.id.emergencyDashButton);
            Button btnMarkerTagManagement = view.findViewById(R.id.markerTagMngmtBtn);


            if (isAdmin) {
                btnSendEmergency.setVisibility(View.VISIBLE);
                btnEmergencyDashboard.setVisibility(View.VISIBLE);
                btnMarkerTagManagement.setVisibility(View.VISIBLE);
                btnSendEmergency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment emergencySendFragment = new EmergencySendFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame, emergencySendFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

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
                btnSendEmergency.setVisibility(View.GONE);
                btnEmergencyDashboard.setVisibility(View.GONE);
            }
        }



        return view;
    }
}