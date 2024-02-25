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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.model.ReportMarker;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isCreateReportMode = false;
    private TextView infoTextView;

    public MapsActivity() {

    }

    private void fetchAndDisplayReports() {

        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.GetAllReportMarker().enqueue(new Callback<List<ReportMarker>>() {
            @Override
            public void onResponse(Call<List<ReportMarker>> call, Response<List<ReportMarker>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReportMarker> reportMarkers = response.body();
                    for (ReportMarker reportMarker : reportMarkers) {
                        LatLng position = new LatLng(reportMarker.getLatitude(), reportMarker.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(position).title(reportMarker.getReportTitle()));
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<ReportMarker>> call, Throwable t) {

            }
        });
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

        Button btnShowReports = view.findViewById(R.id.activity_maps_show_report_button);
        Button btnCreateReport = view.findViewById(R.id.activity_maps_create_report_button);
        infoTextView = view.findViewById(R.id.activity_maps_info_text_view);
        btnShowReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndDisplayReports();
            }
        });

        btnCreateReport.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCreateReportMode = true;
                infoTextView.setVisibility(View.VISIBLE);

            }
        }));


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


}