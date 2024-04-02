package test.connect.geoexploreapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.EventMarkerApi;
import test.connect.geoexploreapp.api.ObservationApi;
import test.connect.geoexploreapp.api.ReportMarkerApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.AlertMarker;
import test.connect.geoexploreapp.model.EventMarker;
import test.connect.geoexploreapp.model.MarkerTag;
import test.connect.geoexploreapp.model.Observation;
import test.connect.geoexploreapp.model.ReportMarker;
import test.connect.geoexploreapp.model.User;
import test.connect.geoexploreapp.websocket.WebSocketListener;
import test.connect.geoexploreapp.websocket.WebSocketManager;

public class MapsActivity extends Fragment implements OnMapReadyCallback, WebSocketListener {

    private GoogleMap mMap;
    private boolean isUpdateReportMode = false;
    private boolean isUpdateEventMode = false;
    private boolean isUpdateObservationMode = false;
    private boolean isCreateEmergencyNotification = false;
    private int reportIdStatus = 0; // For promptForReportID method. 1 to Read, 2 to Delete, 3 to Update
    private int eventIdStatus = 0; // For promptForEventID method. 1 to Read, 2 to Delete, 3 to Update
    private int observationIdStatus = 0; // For promptForReportID method. 1 to Read, 2 to Delete, 3 to Update
    private TextView reportCreateTextView;
    private TextView eventCreateTextView;
    private TextView reportUpdateTextView;
    private TextView eventUpdateTextView;
    private TextView observationCreateTextView;
    private TextView observationUpdateTextView;
    private User loggedInUser;

    public MapsActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, container, false);

        WebSocketManager.getInstance().connectWebSocket("wss://socketsbay.com/wss/v2/1/demo/"); // CHANGE URL FOR WEBSOCKET
        WebSocketManager.getInstance().setWebSocketListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // check if admin needs location
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getCreateEmergencyNotification().observe(getViewLifecycleOwner(), isCreateEmergency -> {
            isCreateEmergencyNotification = isCreateEmergency;
        });

        viewModel.getLoggedInUser().observe(getViewLifecycleOwner(), loggedInUser -> {
            this.loggedInUser = loggedInUser;
            if (loggedInUser != null){
                String userID = String.valueOf(loggedInUser.getId());
                WebSocketManager.getInstance().connectWebSocket("wss://socketsbay.com/wss/v2/1/demo/"); // CHANGE URL FOR WEBSOCKET "ws://coms-309-005.class.las.iastate.edu:8080/live/alerts/" + userID
                WebSocketManager.getInstance().setWebSocketListener(this);
            }else {
                Log.e("WebSocket", "Logged in user is null. Cannot establish WebSocket connection.");
            }
        });



        reportCreateTextView = view.findViewById(R.id.activity_maps_report_create_text_view);
        eventCreateTextView = view.findViewById(R.id.activity_maps_event_create_text_view);
        observationCreateTextView = view.findViewById(R.id.activity_maps_observation_create_text_view);
        reportUpdateTextView = view.findViewById(R.id.activity_maps_report_update_text_view);
        eventUpdateTextView = view.findViewById(R.id.activity_maps_event_update_text_view);
        observationUpdateTextView = view.findViewById(R.id.activity_maps_observation_update_text_view);


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
    public void onWebSocketMessage(String message){
        Log.d("WebSocket", "Received message: " + message);

        try {
            AlertMarker alertMarker = new Gson().fromJson(message, AlertMarker.class);

            showEmergencyNotification(alertMarker.getTitle(), alertMarker.getDescription(),
                    alertMarker.getIo_latitude(), alertMarker.getIo_longitude());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote){

    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        LatLng ames = new LatLng(42.026224,-93.646256);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ames,14));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(isUpdateReportMode){
                    isUpdateReportMode = false;
                    reportIdStatus = 3;
                    promptForReportId(latLng);
                    reportUpdateTextView.setVisibility(View.GONE);
                }else if(isUpdateEventMode){
                    isUpdateEventMode = false;
                    eventIdStatus = 3;
                    promptForEventId(latLng);
                    eventUpdateTextView.setVisibility(View.GONE);
                }else if(isUpdateObservationMode){
                    isUpdateObservationMode = false;
                    observationIdStatus = 3;
                    promptForObservationId(latLng);
                    observationUpdateTextView.setVisibility(View.GONE);
                }else if(isCreateEmergencyNotification){
                    Log.d("MapsActivity", "Entering isCreateEmergencyNotification mode");
                    isCreateEmergencyNotification = false;
                    SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                    viewModel.setLocation(latLng.latitude, latLng.longitude);
                    viewModel.setCreateEmergencyNotification(false);
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showCreatePrompt(latLng);
            }
        });

    }

    public void showEmergencyNotification(String title, String message, double latitude, double longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Show on map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        displayEmergencyOnMap(latitude, longitude, title);
                    }
                })
                .setNegativeButton("Dismiss", null)
                .setIcon(R.drawable.baseline_crisis_alert_24);

        // ui thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });
    }

    public void displayEmergencyOnMap(double latitude, double longitude, String emergencyTitle){

        LatLng emergencyLocation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions()
                .position(emergencyLocation)
                .title(emergencyTitle)
                .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_crisis_alert_24)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(emergencyLocation, 14));
    }


    private void showCreatePrompt(LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What do you want to create?");
        CharSequence[] options = {"Observation", "Report", "Event"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0: // Observation
                        showAddDialog(latLng, "Observation");
                        break;
                    case 1: // Report
                        showAddDialog(latLng, "Report");
                        break;
                    case 2: // Event
                        showAddDialog(latLng, "Event");
                        break;
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddDialog(LatLng latLng, String type) {
        // Inflate the custom layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_forms, null);

        EditText editTextTitle = view.findViewById(R.id.editTextTitle);
        EditText editTextDescription = view.findViewById(R.id.editTextDescription);
        EditText editTextCityDepartment = view.findViewById(R.id.editTextCityDepartment);
        EditText editTextMarkerTag = view.findViewById(R.id.editTextMarkerTag);

        if ("Report".equals(type)) {
            editTextDescription.setVisibility(View.GONE); // Hide description for Report
            editTextCityDepartment.setVisibility(View.GONE);
        } else if ("Event".equals(type)) {
            editTextDescription.setVisibility(View.GONE);
            editTextCityDepartment.setVisibility(View.VISIBLE); // Show city/department for Event
        } else {
            editTextDescription.setVisibility(View.VISIBLE);
            editTextCityDepartment.setVisibility(View.GONE); // Hide city/department for Observation
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(type)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Here, you can access editTextTitle and editTextDescription for their values
                        String title = editTextTitle.getText().toString().trim();
                        String description = editTextDescription.getText().toString().trim();
                        String cityDepartment = editTextCityDepartment.getText().toString().trim();
                        String markerTagsInput = editTextMarkerTag.getText().toString().trim();

                        List<MarkerTag> markerTags = parseMarkerTags(markerTagsInput);

                        if("Report".equals(type)){
                            createNewReport(latLng, title, markerTags);

                        }else if("Event".equals(type)){
                            createNewEvent(latLng, title, cityDepartment, markerTags);

                        }else{
                            createNewObservation(latLng, title,description, markerTags);
                        }

                    }
                })
                .setNegativeButton("Cancel", null); // Dismiss dialog without doing anything

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<MarkerTag> parseMarkerTags(String input) {
        List<MarkerTag> markerTags = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            String[] tags = input.split(",");
            for (String tag : tags) {
                String trimmedTag = tag.trim();
                if (!trimmedTag.isEmpty()) {
                    MarkerTag newTag = new MarkerTag();
                    newTag.setName(trimmedTag);
                    markerTags.add(newTag);
                }
            }
        }
        return markerTags;
    }

    private void showBottomSheetDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_menu);



        Button btnReportRead = bottomSheetDialog.findViewById(R.id.btn_report_read);
        Button btnReportUpdate = bottomSheetDialog.findViewById(R.id.btn_report_update);
        Button btnReportDelete = bottomSheetDialog.findViewById(R.id.btn_report_delete);
        Button btnReportList = bottomSheetDialog.findViewById(R.id.btn_report_list);
        Button btnEventRead = bottomSheetDialog.findViewById(R.id.btn_event_read);
        Button btnEventUpdate = bottomSheetDialog.findViewById(R.id.btn_event_update);
        Button btnEventDelete = bottomSheetDialog.findViewById(R.id.btn_event_delete);
        Button btnEventList = bottomSheetDialog.findViewById(R.id.btn_event_list);
        Button btnObservationRead = bottomSheetDialog.findViewById(R.id.btn_observation_read);
        Button btnObservationUpdate = bottomSheetDialog.findViewById(R.id.btn_observation_update); //not updating
        Button btnObservationDelete = bottomSheetDialog.findViewById(R.id.btn_observation_delete);
        Button btnObservationList = bottomSheetDialog.findViewById(R.id.btn_observation_all);


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
        //done
        btnObservationRead.setOnClickListener(v -> {
            observationIdStatus = 1;
            promptForObservationId();
            bottomSheetDialog.dismiss();
        });
        btnObservationUpdate.setOnClickListener(v -> {
            observationUpdateTextView.setVisibility(View.VISIBLE);
            isUpdateObservationMode = true;
            bottomSheetDialog.dismiss();
        });
        btnObservationDelete.setOnClickListener(v -> {
            observationIdStatus = 2;
            promptForObservationId();
            bottomSheetDialog.dismiss();
        });
        btnObservationList.setOnClickListener(v -> {
            displayAllObservations();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    public String getLocation(double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            return address.toString();
        }
        return "No address found!";
    }
    
    // Report CRUDL
    private void createNewReport(final LatLng latLng, String reportTitle, List<MarkerTag> markerTags) {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        ReportMarker newReportMarker = new ReportMarker();
        newReportMarker.setIo_latitude(latLng.latitude);
        newReportMarker.setIo_longitude(latLng.longitude);
        newReportMarker.setTitle(reportTitle);
        newReportMarker.setCreator(loggedInUser);
//        newReportMarker.setTime_created(new Date());
//        newReportMarker.setTime_updated(new Date());
        newReportMarker.setTags(markerTags);

        reportMarkerApi.addReport(newReportMarker).enqueue(new SlimCallback<>(createdReportMarker -> {
            LatLng position = new LatLng(createdReportMarker.getIo_latitude(), createdReportMarker.getIo_longitude());
            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(createdReportMarker.getId() + " " + createdReportMarker.getTitle())
                    .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_report_24)));
        }, "CreateNewReport"));

    }
    private void displayReportByID(Long id) {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        reportMarkerApi.getReportById(id).enqueue(new SlimCallback<>(reportMarker -> {
            if (reportMarker != null) {
                LatLng position = new LatLng(reportMarker.getIo_latitude(), reportMarker.getIo_longitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(reportMarker.getId() + " " + reportMarker.getTitle())
                        .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_report_24)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            }
        }, "getReportByID"));
    }
    private void updateExistingReportByID(Long id, String newTitle, LatLng latLng) {
        ReportMarkerApi reportMarkerApi = ApiClientFactory.getReportMarkerApi();

        ReportMarker updatedReportMarker = new ReportMarker();
        updatedReportMarker.setTitle(newTitle);
        //updatedReportMarker.setTime_updated(new Date());
        updatedReportMarker.setIo_latitude(latLng.latitude);
        updatedReportMarker.setIo_longitude(latLng.longitude);

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
                LatLng position = new LatLng(reportMarker.getIo_latitude(), reportMarker.getIo_longitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(reportMarker.getId() + " " + reportMarker.getTitle())
                        .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_report_24)));
            }
        }, "GetAllReports"));
    }

    // Observation CRUDL
    private void createNewObservation(final LatLng latLng, String observationTitle, String observationDescription, List<MarkerTag> markerTags) {
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();

        Observation observation = new Observation();
        observation.setLatitude(latLng.latitude);
        observation.setLongitude(latLng.longitude);
        observation.setCreator(loggedInUser);
        observation.setTitle(observationTitle);
//        observation.setTime_created(new Date());
//        observation.setTime_updated(new Date());
        observation.setDescription(observationDescription);
        observation.setTags(markerTags);

        observationApi.saveObs(observation).enqueue(new SlimCallback<>(obs -> {
            LatLng position = new LatLng(obs.getIo_latitude(), obs.getIo_longitude());
            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(obs.getId() + " " + obs.getTitle())
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.baseline_photo_camera_24)));
        }, "CreateNewObservation"));

    }
    private void displayObservationByID(Long id) {
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();

        observationApi.getObs(id).enqueue(new SlimCallback<>(obj -> {
            if (obj != null) {
                LatLng position = new LatLng(obj.getIo_latitude(), obj.getIo_longitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(obj.getId() + " " + obj.getTitle())
                        .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_photo_camera_24)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            }
        }, "getObservationByID"));
    }
    private void updateExistingObservationByID(Long id, String newTitle, LatLng latLng, String newDescription) {
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();

        Observation updatedObservation = new Observation();
        updatedObservation.setId(id);
        updatedObservation.setTitle(newTitle);
        updatedObservation.setLatitude(latLng.latitude);
        updatedObservation.setLongitude(latLng.longitude);
        //updatedObservation.setTime_updated(new Date());
        updatedObservation.setDescription(newDescription);
        Log.d("Updating...", updatedObservation.getTitle() + " "+ updatedObservation.getId()+" " +updatedObservation.getDescription());
        observationApi.updateObs(id, updatedObservation).enqueue(new SlimCallback<>(obs -> {
            Log.d("Update 1", "Update check");
            if (obs != null) {

                mMap.clear();
                displayAllObservations();
                Log.d("Update", "Updated correctly");

                Toast.makeText(getActivity(), "Report updated successfully", Toast.LENGTH_SHORT).show();
            }
            Log.d("Update 2", "Update failed");

        }));
    }
    private void deleteObservationByID(Long id){
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();
        observationApi.deleteObs(id).enqueue(new Callback<Observation>() {
            @Override
            public void onResponse(Call<Observation> call, Response<Observation> response) {
                if(response.isSuccessful()){
                    mMap.clear();
                    displayAllObservations();
                    Toast.makeText(getActivity(), "Report deleted successfully",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(), "Failed to delete report", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Observation> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting report", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void displayAllObservations() {
        ObservationApi observationApi = ApiClientFactory.GetObservationApi();

        observationApi.getAllObs().enqueue(new SlimCallback<>(obs -> {
            mMap.clear();
            for (Observation ob : obs) {
                LatLng position = new LatLng(ob.getIo_latitude(), ob.getIo_longitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(ob.getId() + " " + ob.getTitle())
                        .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_photo_camera_24)));
            }
        }, "GetAllObservations"));
    }

    // Event CRUDL
    private void createNewEvent(final LatLng latLng, String eventTitle, String cityDepartment, List<MarkerTag> markerTags) {
        EventMarkerApi reportMarkerApi = ApiClientFactory.getEventMarkerApi();

        EventMarker newEventMarker = new EventMarker();
        newEventMarker.setLatitude(latLng.latitude);
        newEventMarker.setLongitude(latLng.longitude);
        newEventMarker.setCreator(loggedInUser);
        newEventMarker.setTitle(eventTitle);
//        newEventMarker.setTime_created(new Date());
//        newEventMarker.setTime_updated(new Date());
        newEventMarker.setCity_department(cityDepartment);
        newEventMarker.setTags(markerTags);

        reportMarkerApi.addEvent(newEventMarker).enqueue(new SlimCallback<>(createdEventMarker -> {
            LatLng position = new LatLng(createdEventMarker.getIo_latitude(), createdEventMarker.getIo_longitude());
            mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(createdEventMarker.getId() + " " + createdEventMarker.getTitle() + " Department: " + createdEventMarker.getCity_department())
                    .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_celebration_24)));
        }, "CreateNewEvent"));

//        try {
//            newEventMarker.setLocation(getLocation(latLng.latitude,latLng.longitude));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
    private void displayEventByID(Long id) {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        eventMarkerApi.getEventById(id).enqueue(new SlimCallback<>(eventMarker -> {
            if (eventMarker != null) {
                LatLng position = new LatLng(eventMarker.getIo_latitude(), eventMarker.getIo_longitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(eventMarker.getId() + " " + eventMarker.getTitle() + " Department: " + eventMarker.getCity_department())
                        .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_celebration_24)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
            }
        }, "getEventByID"));
    }
    private void updateExistingEventByID(Long id, String newTitle, String newCityDepartment, LatLng latLng) {
        EventMarkerApi eventMarkerApi = ApiClientFactory.getEventMarkerApi();

        EventMarker updatedEventMarker = new EventMarker();
        updatedEventMarker.setTitle(newTitle);
        updatedEventMarker.setCity_department(newCityDepartment);
        updatedEventMarker.setTime_updated(new Date());
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
                LatLng position = new LatLng(eventMarker.getIo_latitude(), eventMarker.getIo_longitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(eventMarker.getId() + " " + eventMarker.getTitle() + " Department: " + eventMarker.getCity_department())
                        .icon(bitmapDescriptorFromVector(getContext(),R.drawable.baseline_celebration_24)));
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
    private void promptForObservationId(LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Observation ID");

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
                if(observationIdStatus == 3){
                    promptToUpdateObservationTitle(id,latLng);
                    observationIdStatus = 0;
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

    private void promptForObservationId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Observation ID");

        final EditText idInput = new EditText(getActivity());
        idInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(idInput);

        builder.setPositiveButton("OK", (dialog, which) -> {
            Long id = null;
            try {
                id = Long.parseLong(idInput.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Please enter a valid ID.", Toast.LENGTH_SHORT).show();
            }

            if (id != null) {
                if(observationIdStatus == 1){
                    displayObservationByID(id);
                    observationIdStatus = 0;
                }else if(observationIdStatus == 2){
                    deleteObservationByID(id);
                    observationIdStatus = 0;
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
    private void promptToUpdateObservationTitle(Long Id, LatLng latLng) {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("New Title");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Observation Title")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newTitle = input.getText().toString();
                    promptToUpdateObservationDescription(Id, latLng, newTitle);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }
    private void promptToUpdateObservationDescription(Long Id, LatLng latLng, String newTitle) {
        final EditText descriptionInput = new EditText(getActivity());
        descriptionInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        descriptionInput.setHint("New Description");
        descriptionInput.setLines(5);
        descriptionInput.setMaxLines(10);
        descriptionInput.setGravity(Gravity.START | Gravity.TOP);

        AlertDialog.Builder descriptionDialogBuilder = new AlertDialog.Builder(getActivity());
        descriptionDialogBuilder.setTitle("Update Observation Description")
                .setView(descriptionInput)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newDescription = descriptionInput.getText().toString();
                    updateExistingObservationByID(Id, newTitle, latLng, newDescription);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicHeight(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



}




