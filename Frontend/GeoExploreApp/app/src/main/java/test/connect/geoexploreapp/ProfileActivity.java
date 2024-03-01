package test.connect.geoexploreapp;

import static android.content.Context.MODE_PRIVATE;

import static test.connect.geoexploreapp.api.ApiClientFactory.GetUserApi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import test.connect.geoexploreapp.api.SlimCallback;
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

    public static ProfileActivity newInstance(String userName, String userEmail) {
        ProfileActivity fragment = new ProfileActivity();
        Bundle args = new Bundle();
        args.putString("UserName", userName);
        args.putString("UserEmail", userEmail);
        //args.putLong("UserId", userId);
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
        Bundle args = getArguments();
        if (args != null) {
            String userName = args.getString("UserName", "N/A"); // Default value as "N/A"
            String userEmail = args.getString("UserEmail", "N/A");
       //     Long userId = args.getLong("UserId", -1); // -1 as default indicating not found

            TextView userNameDisplay = view.findViewById(R.id.userNameDisplay);
            TextView userEmailDisplay = view.findViewById(R.id.userEmailDisplay); // Assuming you have a TextView for email
            //TextView userID = view.findViewById(R.id.userId); // A
            userNameDisplay.setText(userName);
            userEmailDisplay.setText(userEmail);
            //userID.setText(userId.toString());
        }

//        Button buttonEditPassword = view.findViewById(R.id.buttonEditPassword);
//        buttonEditPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Inflate the custom layout for the AlertDialog
//                LayoutInflater inflater = getActivity().getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.change_password, null);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setView(dialogView)
//                        .setTitle("Change Password")
//                        .setPositiveButton("Change", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EditText editTextOldPassword = dialogView.findViewById(R.id.editTextOldPassword);
//                                EditText editTextNewPassword = dialogView.findViewById(R.id.editTextNewPassword);
//                                EditText editTextConfirmPassword = dialogView.findViewById(R.id.editTextConfirmPassword);
//                                String userId = view.findViewById(R.id.userId).toString();
//
//                                String oldPassword = editTextOldPassword.getText().toString();
//                                String newPassword = editTextNewPassword.getText().toString();
//                                String confirmPassword = editTextConfirmPassword.getText().toString();
//
//                                if(confirmPassword.equals(newPassword)) {
//                                    changePassword(oldPassword, newPassword, userId);
//                                }else{
//                                    showAlert("Email already exists!");
//                                }
//                            }
//                        })
//                        .setNegativeButton("Cancel", null);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });

        return view;
    }
    private void showAlert(String message) {
        // Context should be getActivity() since you're in a fragment
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setTitle("Alert")
                .setPositiveButton("OK", null) // Optionally, add an OnClickListener
                .create()
                .show();
    }
//    public void changePassword(String oldPassword, String newPassword, Long userId){
//
//    }

}