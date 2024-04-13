package test.connect.geoexploreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import test.connect.geoexploreapp.model.MarkerTag;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarkerTagDetailedViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarkerTagDetailedViewFragment extends Fragment {

    private static final String ARG_MARKER_TAG = "marker_tag";
    private MarkerTag markerTag;


    public MarkerTagDetailedViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MarkerTagDetailedViewFragment newInstance(MarkerTag markerTag) {
        MarkerTagDetailedViewFragment fragment = new MarkerTagDetailedViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MARKER_TAG, markerTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_MARKER_TAG)) {
            markerTag = (MarkerTag) getArguments().getSerializable(ARG_MARKER_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marker_tag_detailed_view, container, false);inflater.inflate(R.layout.fragment_marker_tag_detailed_view, container, false);
        Button backButton = view.findViewById(R.id.backButton);

        TextView idTextView = view.findViewById(R.id.idTextView);
        TextView nameTextView = view.findViewById(R.id.nameTextView);


        if (markerTag != null) {
            idTextView.setText(String.format("ID: %s", markerTag.getId()));
            nameTextView.setText(String.format("Name: %s", markerTag.getName()));
        }

        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        return view;
    }
}