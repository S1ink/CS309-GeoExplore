package test.connect.geoexploreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmergencyDashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyDashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText latitudeText, longitudeText;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmergencyDashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyDashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyDashFragment newInstance(String param1, String param2) {
        EmergencyDashFragment fragment = new EmergencyDashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_emergency_dash, container, false);

        // Initialize EditText fields
        latitudeText = view.findViewById(R.id.latitudeText);
        longitudeText = view.findViewById(R.id.longitudeText);

        // Get instance of SharedViewModel from the activity
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe the LiveData for latitude
        viewModel.getLatitude().observe(getViewLifecycleOwner(), latitude -> {
            latitudeText.setText(latitude != null ? String.valueOf(latitude) : "N/A");
        });

        // Observe the LiveData for longitude
        viewModel.getLongitude().observe(getViewLifecycleOwner(), longitude -> {
            longitudeText.setText(longitude != null ? String.valueOf(longitude) : "N/A");
        });



        Button backButton = view.findViewById(R.id.backButton);
        Button setLocationButton = view.findViewById(R.id.setLocationButton);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        setLocationButton.setOnClickListener(v -> {
            // Ensure MapsActivity can notify this fragment when a location is picked
            viewModel.setCreateEmergencyNotification(true);
            Log.d("EmergencyDashFragment","Emergency was set to true");
            MapsActivity mapsFragment = new MapsActivity();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame, mapsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });

        return view;
    }
}