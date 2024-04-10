package test.connect.geoexploreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.AlertMarkerApi;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.MarkerTagApi;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.AlertMarker;
import test.connect.geoexploreapp.model.MarkerTag;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarkerTagManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarkerTagManagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tagInfoTextView;
    private EditText tagIdEditText, tagNameEditText;

    public MarkerTagManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarkerTagManagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarkerTagManagementFragment newInstance(String param1, String param2) {
        MarkerTagManagementFragment fragment = new MarkerTagManagementFragment();
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

        View view = inflater.inflate(R.layout.fragment_marker_tag_management, container, false);

        Button backButton = view.findViewById(R.id.backButton);
        tagIdEditText = view.findViewById(R.id.tagIdEditText);
        tagInfoTextView = view.findViewById(R.id.tagInfoTextView);
        tagNameEditText = view.findViewById(R.id.tagNameEditText);

        tagInfoTextView.setMovementMethod(new ScrollingMovementMethod());


        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());


        String[] operations = new String[]{"Create", "Read", "Update", "Delete", "List"};
        Spinner tagOperationSpinner = view.findViewById(R.id.tagOperationSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, operations);
        tagOperationSpinner.setAdapter(adapter);


        Button confirmOperationBtn = view.findViewById(R.id.confirmOperationBtn);
        confirmOperationBtn.setOnClickListener(v -> {
            String selectedOperation = tagOperationSpinner.getSelectedItem().toString();
            switch (selectedOperation) {
                case "Create":
                    createTag();
                    break;
                case "Read":
                    displayTagByID();
                    break;
                case "Update":
                    updateTagByID();
                    break;
                case "Delete":
                    deleteTagByID();
                    break;
                case "List":
                    displayAllTags();
                    break;
            }
        });

        return view;
    }



    private void displayAllTags() {
        MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();

        markerTagApi.getAllMarkerTags().enqueue(new SlimCallback<>(markerTags -> {
            StringBuilder tagInfo = new StringBuilder();
            for (MarkerTag markerTag : markerTags) {
                tagInfo.append("ID: ").append(markerTag.getId()).append(", Name: ").append(markerTag.getName()).append("\n");
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> tagInfoTextView.setText(tagInfo.toString()));
            }
        }, "GetAllTags"));
    }

    private void displayTagByID() {
        String idString = tagIdEditText.getText().toString();
        if (!idString.isEmpty()) {
            try {
                Long id = Long.parseLong(idString);
                MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();
                markerTagApi.getMarkerTagById(id).enqueue(new SlimCallback<>(markerTag -> {
                    if (markerTag != null) {
                        String tagInfo = "ID: " + markerTag.getId() + ", Name: " + markerTag.getName();
                        tagInfoTextView.setText(tagInfo);
                    }
                }, "getTagByID"));
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Enter a valid ID", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "ID cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteTagByID() {
        String idString = tagIdEditText.getText().toString();
        if (!idString.isEmpty()) {
            try {
                Long id = Long.parseLong(idString);
                MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();
                markerTagApi.deleteMarkerTagById(id).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            String deleteSuccessText = "Tag with ID: " + id + " deleted successfully";
                            tagInfoTextView.setText(deleteSuccessText);
                        } else {
                            Toast.makeText(getActivity(), "Failed to delete tag", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error deleting tag: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Enter a valid ID", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "ID cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    private void updateTagByID() {
        String idString = tagIdEditText.getText().toString();
        String newName = tagNameEditText.getText().toString();

        if (!idString.isEmpty() && !newName.isEmpty()) {
            try {
                Long id = Long.parseLong(idString);
                MarkerTag updatedTag = new MarkerTag();
                updatedTag.setName(newName);

                MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();
                markerTagApi.updateMarkerTag(id, updatedTag).enqueue(new Callback<MarkerTag>() {
                    @Override
                    public void onResponse(Call<MarkerTag> call, Response<MarkerTag> response) {
                        if (response.isSuccessful()) {
                            String updateSuccessText = "Tag with ID: " + id + " updated successfully to " + newName;
                            tagInfoTextView.setText(updateSuccessText);
                            Toast.makeText(getActivity(), updateSuccessText, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Failed to update tag", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MarkerTag> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error updating tag", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Enter a valid ID", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "ID and new name cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    private void createTag() {
        String tagName = tagNameEditText.getText().toString().trim();

        if (!tagName.isEmpty()) {
            MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();

            MarkerTag newTag = new MarkerTag();
            newTag.setName(tagName);
            markerTagApi.addMarkerTag(newTag).enqueue(new Callback<MarkerTag>() {
                @Override
                public void onResponse(Call<MarkerTag> call, Response<MarkerTag> response) {
                    if (response.isSuccessful()) {
                        MarkerTag createdTag = response.body();
                        String successMessage = "Tag created successfully with ID: " + createdTag.getId();
                        tagInfoTextView.setText(successMessage);
                        Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to create tag", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MarkerTag> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error creating tag: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Tag name cannot be empty", Toast.LENGTH_LONG).show();
        }
    }
}