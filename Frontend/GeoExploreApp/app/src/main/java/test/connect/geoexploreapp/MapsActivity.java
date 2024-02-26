package test.connect.geoexploreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.ReportMarker;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isCreateReportMode = false;
    private TextView infoTextView;

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


        infoTextView = view.findViewById(R.id.activity_maps_info_text_view);


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
                    isCreateReportMode = false;
                    infoTextView.setVisibility(View.GONE);
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

        btnReportCreate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnReportRead.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnReportUpdate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnReportDelete.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnReportList.setOnClickListener(v -> {

            displayAllReports();
            bottomSheetDialog.dismiss();
        });
        btnEventCreate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventRead.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventUpdate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventDelete.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventList.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();
    }



    private void displayAllReports() {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.GetAllReportMarker().enqueue(new SlimCallback<>(reportMarkers -> {
            mMap.clear();
            for (ReportMarker reportMarker : reportMarkers) {
                LatLng position = new LatLng(reportMarker.getLatitude(), reportMarker.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title(reportMarker.getReportTitle()));
            }
        }, "GetAllReports"));
    }

    private void createNewReport(final LatLng latLng){

    }

    private void displayReportByID(Long id){

    }

    private void updateExistingReport(){

    }

    private void deleteReport(){

    }


}