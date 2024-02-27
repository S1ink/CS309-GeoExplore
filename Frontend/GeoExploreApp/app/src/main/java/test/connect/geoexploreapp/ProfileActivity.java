package test.connect.geoexploreapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import test.connect.geoexploreapp.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileActivity() {
        // Required empty public constructor
    }

    public static ProfileActivity newInstance(String param1, String param2) {
        ProfileActivity fragment = new ProfileActivity();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_profile, container, false);
        TextView userNameDisplay = view.findViewById(R.id.userNameDisplay);
//        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        String json = sh.getString("user", "");
//        Gson gson = new Gson();
//        User user = gson.fromJson(json, User.class);
//        userNameDisplay.setText(user.getName());
        return view;
    }
}