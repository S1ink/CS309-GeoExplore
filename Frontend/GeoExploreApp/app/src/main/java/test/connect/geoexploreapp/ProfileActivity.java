package test.connect.geoexploreapp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.ApiClientFactory;
import test.connect.geoexploreapp.api.UserApi;
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
    private static Bundle args;
    private String mParam2;
    private static User user;


    public ProfileActivity() {
        // Required empty public constructor
    }

    public static ProfileActivity newInstance(User user ) {
        ProfileActivity fragment = new ProfileActivity();
        args = new Bundle();
        args.putSerializable("UserObject", user);
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

        View view = inflater.inflate(R.layout.activity_profile, container, false);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("UserObject");

            Log.d("ProfileActivity", "isAdmin: " + user.getIsAdmin());
       //     Long userId = args.getLong("UserId", -1); // -1 as default indicating not found

            TextView userNameDisplay = view.findViewById(R.id.userNameDisplay);
            TextView userEmailDisplay = view.findViewById(R.id.userEmailDisplay);
            //TextView userID = view.findViewById(R.id.userId); // A
            userNameDisplay.setText(user.getName());
            userEmailDisplay.setText(user.getEmailId());
            //userID.setText(userId.toString());
        }

        Button buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(v -> showChangePasswordDialog(v.getContext()));
        return view;

    }

    private void showChangePasswordDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Change Password");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText newPasswordInput = new EditText(context);
        newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordInput.setHint("New password");

        final EditText confirmPasswordInput = new EditText(context);
        confirmPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordInput.setHint("Confirm password");

        layout.addView(newPasswordInput);
        layout.addView(confirmPasswordInput);

        builder.setView(layout);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String newPassword = newPasswordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }else{
                updatePassword(newPassword);
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updatePassword(String newPassword) {
        user.setPassword(newPassword);
        UserApi userApi = ApiClientFactory.GetUserApi();

        userApi.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User userUpdates = response.body();
                    Toast.makeText(getContext(), "Password successfully updated", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Toast.makeText(getContext(), "Password successfully updated", Toast.LENGTH_SHORT).show();

                        Log.e("UpdatePassword", "Failed to update password. Response: " + response.errorBody().string());
//                        Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("UpdatePassword", "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UpdatePassword", "API call failed: ", t);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }


}