package test.connect.geoexploreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.AlertMarkerApi;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.AlertMarker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmergencyDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView alertInfoTextView;
    private EditText alertIdEditText;

    public EmergencyDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyDashboardFragment newInstance(String param1, String param2) {
        EmergencyDashboardFragment fragment = new EmergencyDashboardFragment();
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

        View view = inflater.inflate(R.layout.fragment_emergency_dashboard, container, false);
        Button getAllAlertsButton = view.findViewById(R.id.getAllAlertsBtn);
        Button menuButton = view.findViewById(R.id.menuButton);
        Button backButton = view.findViewById(R.id.backButton);
        alertIdEditText = view.findViewById(R.id.alertIdEditText);
        alertInfoTextView = view.findViewById(R.id.alertInfoTextview);


        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        menuButton.setOnClickListener(v -> showPopupMenu(v));

        getAllAlertsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printAllAlerts();
            }
        });

        return view;
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.alert_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_get_alert_by_id) {
                String IdString = alertIdEditText.getText().toString();
                if(!IdString.isEmpty()){
                    try{
                        Long id = Long.parseLong(IdString);
                        displayAlertByID(id);
                    } catch(NumberFormatException e){
                        Toast.makeText(getContext(),"Enter Valid ID", Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(getContext(),"Enter Valid ID", Toast.LENGTH_LONG).show();
                }
                return true;
            } else if (itemId == R.id.action_delete_alert_by_id) {
                String IdString = alertIdEditText.getText().toString();
                if(!IdString.isEmpty()){
                    try{
                        Long id = Long.parseLong(IdString);
                        deleteAlertByID(id);
                    } catch(NumberFormatException e){
                        Toast.makeText(getContext(),"Enter Valid ID", Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(getContext(),"Enter Valid ID", Toast.LENGTH_LONG).show();
                }
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }


    private void printAllAlerts() {
        AlertMarkerApi alertMarkerApi = ApiClientFactory.getAlertMarkerApi();

        alertMarkerApi.GetAllAlertMarker().enqueue(new SlimCallback<>(alertMarkers -> {
            StringBuilder alertInfo = new StringBuilder();
            for (AlertMarker alertMarker : alertMarkers) {
                alertInfo.append("ID: ").append(alertMarker.getId()).append(", Title: ").append(alertMarker.getTitle()).append(", Time: ")
                        .append(alertMarker.getTime_created()).append("\n");
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> alertInfoTextView.setText(alertInfo.toString()));
            }
        }, "GetAllAlerts"));
    }

    private void displayAlertByID(Long id) {
        AlertMarkerApi alertMarkerApi = ApiClientFactory.getAlertMarkerApi();

        alertMarkerApi.getAlertById(id).enqueue(new SlimCallback<>(alertMarker -> {
            if (alertMarker != null) {
                StringBuilder alertInfo = new StringBuilder();
                alertInfo = alertInfo.append("ID: ").append(alertMarker.getId()).append(", Title: ").append(alertMarker.getTitle()).append(", Time: ")
                        .append(alertMarker.getTime_created()).append("\n");
                if (getActivity() != null) {
                    StringBuilder finalAlertInfo = alertInfo;
                    getActivity().runOnUiThread(() -> alertInfoTextView.setText(finalAlertInfo));
                }
            }
        }, "getAlertByID"));
    }

    private void deleteAlertByID(Long id){
        AlertMarkerApi alertMarkerApi = ApiClientFactory.getAlertMarkerApi();
        alertMarkerApi.deleteAlertById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    String deleteSuccessText = "Alert with ID: " + id + " deleted successfully";
                    alertInfoTextView.setText(deleteSuccessText);

                } else{
                    Toast.makeText(getActivity(), "Failed to delete alert", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting alert", Toast.LENGTH_SHORT).show();
            }
        });

    }
}