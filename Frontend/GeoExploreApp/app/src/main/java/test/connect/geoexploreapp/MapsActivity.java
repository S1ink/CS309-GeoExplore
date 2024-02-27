package test.connect.geoexploreapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isCreateReportMode = false;
    private boolean isCreateEventMode = false;
    private int reportIdStatus = 0; // For promptForReportID method. 1 to Read, 2 to Delete, 3 to Update
    private int eventIdStatus = 0; // For promptForEventID method. 1 to Read, 2 to Delete, 3 to Update
    private TextView reportInfoTextView;
    private TextView eventInfoTextView;


    public MapsActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        reportInfoTextView = view.findViewById(R.id.activity_maps_report_info_text_view);
        eventInfoTextView = view.findViewById(R.id.activity_maps_event_info_text_view);


        FloatingActionButton fab = view.findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        LatLng ames = new LatLng(42.026224,-93.646256);
        mMap.addMarker(new MarkerOptions().position(ames).title("Test Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ames,14));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(isCreateReportMode){
                    //promptForReportTitle(latLng);
                    isCreateReportMode = false;
                    reportInfoTextView.setVisibility(View.GONE);
                }
                if(isCreateEventMode){
                   // promptForEventTitle(latLng);
                    isCreateEventMode = false;
                    eventInfoTextView.setVisibility(View.GONE);
                }
            }
        });



    }

    private void showBottomSheetDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_menu);


        Button btnReportCreate = bottomSheetDialog.findViewById(R.id.btn_report_create);
        Button btnReportRead = bottomSheetDialog.findViewById(R.id.btn_report_read);
        Button btnReportUpdate = bottomSheetDialog.findViewById(R.id.btn_report_update);
        Button btnReportDelete = bottomSheetDialog.findViewById(R.id.btn_report_delete);
        Button btnReportList = bottomSheetDialog.findViewById(R.id.btn_report_list);
        Button btnEventCreate = bottomSheetDialog.findViewById(R.id.btn_event_create);
        Button btnEventRead = bottomSheetDialog.findViewById(R.id.btn_event_read);
        Button btnEventUpdate = bottomSheetDialog.findViewById(R.id.btn_event_update);
        Button btnEventDelete = bottomSheetDialog.findViewById(R.id.btn_event_delete);
        Button btnEventList = bottomSheetDialog.findViewById(R.id.btn_event_list);
        Button btnObservationAdd = bottomSheetDialog.findViewById(R.id.btn_observation_add);

        btnReportCreate.setOnClickListener(v -> {
            reportInfoTextView.setVisibility(View.VISIBLE);
            isCreateReportMode = true;
            bottomSheetDialog.dismiss();
        });
        btnReportRead.setOnClickListener(v -> {
           // promptForReportId();
            bottomSheetDialog.dismiss();
        });
        btnReportUpdate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnReportDelete.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnReportList.setOnClickListener(v -> {
            //displayAllReports();
            bottomSheetDialog.dismiss();
        });
        btnEventCreate.setOnClickListener(v -> {
            eventInfoTextView.setVisibility(View.VISIBLE);
            isCreateEventMode = true;
            bottomSheetDialog.dismiss();
        });
        btnEventRead.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            //promptForEventId();
        });
        btnEventUpdate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventDelete.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventList.setOnClickListener(v -> {
            //displayAllEvents();
            bottomSheetDialog.dismiss();
        });
        btnObservationAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ObservationForm.class);
            startActivity(intent);
            bottomSheetDialog.dismiss();
        });
//        btnObservationAdd.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), AllObservations.class);
//            startActivity(intent);
//            bottomSheetDialog.dismiss();
//        });


        bottomSheetDialog.show();
    }





}
