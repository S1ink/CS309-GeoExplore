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
    private EditText tagIdEditText;

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
        Button getAllTagsButton = view.findViewById(R.id.getAllTagsBtn);
        Button menuButton = view.findViewById(R.id.menuButton);
        tagInfoTextView = view.findViewById(R.id.tagInfoTextView);

        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        menuButton.setOnClickListener(v -> showTagOptionsPopup(v));




        getAllTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllTags();
            }
        });

        return view;
    }

    public void showTagOptionsPopup(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.tag_options_menu);
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.action_get_tag_by_id) {
                String IdString = tagIdEditText.getText().toString();
                if(!IdString.isEmpty()){
                    try{
                        Long id = Long.parseLong(IdString);
                        displayTagByID(id);
                    } catch(NumberFormatException e){
                        Toast.makeText(getContext(),"Enter Valid ID", Toast.LENGTH_LONG).show();

                    }
                }else {
                    Toast.makeText(getContext(),"Enter Valid ID", Toast.LENGTH_LONG).show();
                }
                return true;
            } else if (itemId == R.id.action_delete_tag_by_id) {
                String IdString = tagIdEditText.getText().toString();
                if(!IdString.isEmpty()){
                    try{
                        Long id = Long.parseLong(IdString);
                        deleteTagByID(id);
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
        popup.show();
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

    private void displayTagByID(Long id) {
        MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();

        markerTagApi.getMarkerTagById(id).enqueue(new SlimCallback<>(markerTag -> {
            if (markerTag != null) {
                StringBuilder tagInfo = new StringBuilder();
                tagInfo.append("ID: ").append(markerTag.getId()).append(", Name: ").append(markerTag.getName()).append("\n");
                if (getActivity() != null) {
                    StringBuilder finalTagInfo = tagInfo;
                    getActivity().runOnUiThread(() -> tagInfoTextView.setText(finalTagInfo));
                }
            }
        }, "getTagByID"));
    }

    private void deleteTagByID(Long id){
        MarkerTagApi markerTagApi = ApiClientFactory.getMarkerTagApi();
        markerTagApi.deleteMarkerTagById(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    String deleteSuccessText = "Tag with ID: " + id + " deleted successfully";
                    tagInfoTextView.setText(deleteSuccessText);

                } else{
                    Toast.makeText(getActivity(), "Failed to delete tag", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Error deleting tag", Toast.LENGTH_SHORT).show();
            }
        });

    }

}