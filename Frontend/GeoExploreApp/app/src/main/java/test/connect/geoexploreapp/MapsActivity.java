package test.connect.geoexploreapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private boolean isUpdateReportMode = false;
    private boolean isUpdateEventMode = false;
    private int reportIdStatus = 0; // For promptForReportID method. 1 to Read, 2 to Delete, 3 to Update
    private int eventIdStatus = 0; // For promptForEventID method. 1 to Read, 2 to Delete, 3 to Update
    private TextView reportCreateTextView;
    private TextView eventCreateTextView;
    private TextView reportUpdateTextView;
    private TextView eventUpdateTextView;

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


        reportCreateTextView = view.findViewById(R.id.activity_maps_report_create_text_view);
        eventCreateTextView = view.findViewById(R.id.activity_maps_event_create_text_view);

        reportUpdateTextView = view.findViewById(R.id.activity_maps_report_update_text_view);
        eventUpdateTextView = view.findViewById(R.id.activity_maps_event_update_text_view);


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
                    promptForNewReportTitle(latLng);
                    isCreateReportMode = false;
                    reportCreateTextView.setVisibility(View.GONE);
                }else if(isCreateEventMode){
                    promptForNewEventTitle(latLng);
                    isCreateEventMode = false;
                    eventCreateTextView.setVisibility(View.GONE);
                }else if(isUpdateReportMode){
                    isUpdateReportMode = false;
                    reportIdStatus = 3;
                    promptForReportId(latLng);
                    reportUpdateTextView.setVisibility(View.GONE);
                }else if(isUpdateEventMode){
                    isUpdateEventMode = false;
                    eventIdStatus = 3;
                    promptForEventId(latLng);
                    eventUpdateTextView.setVisibility(View.GONE);
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
            reportCreateTextView.setVisibility(View.VISIBLE);
            isCreateReportMode = true;
            bottomSheetDialog.dismiss();
        });
        btnReportRead.setOnClickListener(v -> {
            reportIdStatus = 1;
            promptForReportId();
            bottomSheetDialog.dismiss();
        });
        btnReportUpdate.setOnClickListener(v -> {
            reportUpdateTextView.setVisibility(View.VISIBLE);
            isUpdateReportMode = true;
            bottomSheetDialog.dismiss();
        });
        btnReportDelete.setOnClickListener(v -> {
            reportIdStatus = 2;
            promptForReportId();
            bottomSheetDialog.dismiss();
        });
        btnReportList.setOnClickListener(v -> {
            displayAllReports();
            bottomSheetDialog.dismiss();
        });
        btnEventCreate.setOnClickListener(v -> {
            eventCreateTextView.setVisibility(View.VISIBLE);
            isCreateEventMode = true;
            bottomSheetDialog.dismiss();
        });
        btnEventRead.setOnClickListener(v -> {
            eventIdStatus = 1;
            promptForEventId();
            bottomSheetDialog.dismiss();
        });
        btnEventUpdate.setOnClickListener(v -> {
            eventUpdateTextView.setVisibility(View.VISIBLE);
            isUpdateEventMode = true;
            bottomSheetDialog.dismiss();
        });
        btnEventDelete.setOnClickListener(v -> {
            eventIdStatus = 2;
            promptForEventId();
            bottomSheetDialog.dismiss();
        });
        btnEventList.setOnClickListener(v -> {
            displayAllEvents();
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();
    }


    // Report CRUDL
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

    private void updateExistingReportByID(Long id, String newTitle, LatLng latLng) {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        ReportMarker updatedReportMarker = new ReportMarker();
        updatedReportMarker.setTitle(newTitle);
        updatedReportMarker.setLatitude(latLng.latitude);
        updatedReportMarker.setLongitude(latLng.longitude);

        reportMarkerApi.updateReportById(id, updatedReportMarker).enqueue(new SlimCallback<>(updatedReport -> {
            if (updatedReport != null) {

                mMap.clear();
                displayAllReports();

                Toast.makeText(getActivity(), "Report updated successfully", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void deleteReportByID(Long id){
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.deleteReportById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    mMap.clear();
                    displayAllReports();

                    Toast.makeText(getActivity(), "Report deleted successfully",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(), "Failed to delete report", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting report", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayAllReports() {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.GetAllReportMarker().enqueue(new SlimCallback<>(reportMarkers -> {
            mMap.clear();
            for (ReportMarker reportMarker : reportMarkers) {
                LatLng position = new LatLng(reportMarker.getLatitude(), reportMarker.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title(reportMarker.getId() + " " + reportMarker.getTitle()));
            }
        }, "GetAllReports"));
    }


    // Event CRUDL
    private void createNewEvent(final LatLng latLng, String eventTitle, String cityDepartment) {
        EventMarkerApi reportMarkerApi = ApiClientFactory.getEventMarkerApi();

        EventMarker newEventMarker = new EventMarker();
        newEventMarker.setLatitude(latLng.latitude);
        newEventMarker.setLongitude(latLng.longitude);
        newEventMarker.setTitle(eventTitle);
        newEventMarker.setCity_department(cityDepartment);

        reportMarkerApi.addEvent(newEventMarker).enqueue(new SlimCallback<>(createdEventMarker -> {
            LatLng position = new LatLng(createdEventMarker.getLatitude(), createdEventMarker.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title(createdEventMarker.getTitle() + " Department: " + createdEventMarker.getCity_department()));
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
    private void updateExistingEventByID(Long id, String newTitle, String newCityDepartment, LatLng latLng) {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        EventMarker updatedEventMarker = new EventMarker();
        updatedEventMarker.setTitle(newTitle);
        updatedEventMarker.setCity_department(newCityDepartment);
        updatedEventMarker.setLatitude(latLng.latitude);
        updatedEventMarker.setLongitude(latLng.longitude);


        eventMarkerApi.updateEventById(id, updatedEventMarker).enqueue(new SlimCallback<>(updatedEvent -> {
            if (updatedEvent != null) {

                mMap.clear();
                displayAllEvents();

                Toast.makeText(getActivity(), "Event updated successfully", Toast.LENGTH_SHORT).show();
            }
        }));
    }
    private void deleteEventByID(Long id) {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        eventMarkerApi.deleteEventById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    mMap.clear();
                    displayAllEvents();

                    Toast.makeText(getActivity(), "Event deleted successfully",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting event", Toast.LENGTH_SHORT).show();
            }
        });

    }
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


    // Methods for collecting CRUDL info from user
    private void promptForReportId(LatLng latLng) {
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
                if(reportIdStatus == 3){
                    promptToUpdateReportTitle(id,latLng);
                    reportIdStatus = 0;
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
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
                if(reportIdStatus == 1){
                    displayReportByID(id);
                    reportIdStatus = 0;
                }else if(reportIdStatus == 2){
                    deleteReportByID(id);
                    reportIdStatus = 0;
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    private void promptForEventId(final LatLng latLng) {
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
                if(eventIdStatus == 3){
                    promptUserToUpdateEvent(id,latLng);
                    eventIdStatus = 0;
                }

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
                if(eventIdStatus == 1){
                    displayEventByID(id);
                    eventIdStatus = 0;
                }else if(eventIdStatus == 2){
                    deleteEventByID(id);
                    eventIdStatus = 0;
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void promptForNewReportTitle(final LatLng latLng) {
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

    private void promptForNewEventTitle(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Event");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleInput = new EditText(getActivity());
        titleInput.setHint("New Title");
        layout.addView(titleInput);

        final EditText cityDepartmentInput = new EditText(getActivity());
        cityDepartmentInput.setHint("New City Department");
        layout.addView(cityDepartmentInput);

        builder.setView(layout);


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = titleInput.getText().toString();
                String newCityDepartment = cityDepartmentInput.getText().toString();
                createNewEvent(latLng, newTitle, newCityDepartment);
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


    private void promptUserToUpdateEvent(Long Id,LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Event");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleInput = new EditText(getActivity());
        titleInput.setHint("New Title");
        layout.addView(titleInput);

        final EditText cityDepartmentInput = new EditText(getActivity());
        cityDepartmentInput.setHint("New City Department");
        layout.addView(cityDepartmentInput);

        builder.setView(layout);


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = titleInput.getText().toString();
                String newCityDepartment = cityDepartmentInput.getText().toString();
                updateExistingEventByID(Id, newTitle, newCityDepartment, latLng);
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

    private void promptToUpdateReportTitle(Long Id, LatLng latLng) {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("New Title");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Report Title")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newTitle = input.getText().toString();
                    updateExistingReportByID(Id, newTitle, latLng);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }


}



