package test.connect.geoexploreapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.text.InputType;
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

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.EventMarkerApi;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.EventMarker;
import test.connect.geoexploreapp.model.ReportMarker;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isCreateReportMode = false;
    private boolean isCreateEventMode = false;
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
                    promptForReportTitle(latLng);
                    isCreateReportMode = false;
                    reportInfoTextView.setVisibility(View.GONE);
                }
                if(isCreateEventMode){
                    promptForEventTitle(latLng);
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

        btnReportCreate.setOnClickListener(v -> {
            reportInfoTextView.setVisibility(View.VISIBLE);
            isCreateReportMode = true;
            bottomSheetDialog.dismiss();
        });
        btnReportRead.setOnClickListener(v -> {
            promptForReportId();
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
            eventInfoTextView.setVisibility(View.VISIBLE);
            isCreateEventMode = true;
            bottomSheetDialog.dismiss();
        });
        btnEventRead.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            promptForEventId();
        });
        btnEventUpdate.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventDelete.setOnClickListener(v -> {

            bottomSheetDialog.dismiss();
        });
        btnEventList.setOnClickListener(v -> {
            displayAllEvents();
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();
    }


    // Report CRUDL
    private void displayAllReports() {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.GetAllReportMarker().enqueue(new SlimCallback<>(reportMarkers -> {
            mMap.clear();
            for (ReportMarker reportMarker : reportMarkers) {
                LatLng position = new LatLng(reportMarker.getLatitude(), reportMarker.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title(reportMarker.getTitle()));
            }
        }, "GetAllReports"));
    }

    private void createNewReport(final LatLng latLng, String reportTitle) {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        ReportMarker newReportMarker = new ReportMarker();
        newReportMarker.setLatitude(latLng.latitude);
        newReportMarker.setLongitude(latLng.longitude);
        newReportMarker.setTitle(reportTitle);

        reportMarkerApi.addReport(newReportMarker).enqueue(new SlimCallback<>(createdReportMarker -> {
            LatLng position = new LatLng(createdReportMarker.getLatitude(), createdReportMarker.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(createdReportMarker.getTitle()));
        }, "CreateNewReport"));
    }
    private void displayReportByID(Long id) {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.getReportById(id).enqueue(new SlimCallback<>(reportMarker -> {
            if (reportMarker != null) {
                LatLng position = new LatLng(reportMarker.getLatitude(), reportMarker.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(position).title(reportMarker.getTitle()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            }
        }, "getReportByID"));
    }

    private void updateExistingReport(){

    }

    private void deleteReportByID(){

    }


    // Event CRUDL
    private void displayAllEvents() {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        eventMarkerApi.GetAllEventMarker().enqueue(new SlimCallback<>(eventMarkers -> {
            mMap.clear();
            for (EventMarker eventMarker : eventMarkers) {
                LatLng position = new LatLng(eventMarker.getLatitude(), eventMarker.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title(eventMarker.getTitle() + " Department: " + eventMarker.getCity_department()));
            }
        }, "GetAllEvents"));
    }

    private void createNewEvent(final LatLng latLng, String eventTitle) {
        EventMarkerApi reportMarkerApi = ApiClientFactory.getEventMarkerApi();

        EventMarker newEventMarker = new EventMarker();
        newEventMarker.setLatitude(latLng.latitude);
        newEventMarker.setLongitude(latLng.longitude);
        newEventMarker.setTitle(eventTitle);

        reportMarkerApi.addEvent(newEventMarker).enqueue(new SlimCallback<>(createdEventMarker -> {
            LatLng position = new LatLng(createdEventMarker.getLatitude(), createdEventMarker.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(createdEventMarker.getTitle()));
        }, "CreateNewEvent"));
    }

    private void displayEventByID(Long id) {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        eventMarkerApi.getEventById(id).enqueue(new SlimCallback<>(eventMarker -> {
            if (eventMarker != null) {
                LatLng position = new LatLng(eventMarker.getLatitude(), eventMarker.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(position).title(eventMarker.getTitle()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            }
        }, "getEventByID"));
    }

    private void updateExistingEvent(){

    }

    private void deleteEventByID(){

    }



    // Methods for collecting CRUDL info from user
    private void promptForReportId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Report ID");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            Long id = null;
            try {
                id = Long.parseLong(input.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Please enter a valid ID.", Toast.LENGTH_SHORT).show();
            }

            if (id != null) {
                displayReportByID(id);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void promptForEventId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Event ID");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            Long id = null;
            try {
                id = Long.parseLong(input.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Please enter a valid ID.", Toast.LENGTH_SHORT).show();
            }

            if (id != null) {
                displayEventByID(id);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void promptForReportTitle(final LatLng latLng) {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Report Title")
                .setView(input)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String reportTitle = input.getText().toString();
                    createNewReport(latLng, reportTitle);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }


    private void promptForEventTitle(final LatLng latLng) {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Report Title")
                .setView(input)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String reportTitle = input.getText().toString();
                    createNewEvent(latLng, reportTitle);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }
}