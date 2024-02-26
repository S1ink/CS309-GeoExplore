package test.connect.geoexploreapp;

import static test.connect.geoexploreapp.api.ApiClientFactory.GetUserApi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.List;

import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.model.User;


public class LoginTabFragment extends Fragment {
    EditText UserEmail,UserPassword;
    Button loginSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserEmail=view.findViewById(R.id.login_email);
        UserPassword=view.findViewById(R.id.login_password);
         loginSubmit = view.findViewById(R.id.login_button);

        // Set the click listener for the button
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString().toLowerCase().trim();
                String passcode = UserPassword.getText().toString();
                // Perform your actions here
                // For example, show an alert dialog
                //showAlert("Login Attempted!");

                isValidCredentials(email, passcode, new CredentialsCallback() {
                    @Override
                    public void onResult(boolean isValid) {
                        if (isValid) {
                            startMainActivity();
                        } else {
                            showAlert("Invalid Credentials!");
                        }
                    }
                });

            }
        });
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

    private void isValidCredentials(String email, String password, CredentialsCallback callback) {
        // CHECK IF USER EXISTS HERE
        if (email.isEmpty() || password.isEmpty()) {
            // Immediate feedback for empty fields
            callback.onResult(false);
            return;
        }

        GetUserApi().getAllUsers().enqueue(new SlimCallback<List<User>>(users->{

            for(int i = 0; i<users.size(); i++){
                User temp = users.get(i);
                Log.d("LoginCheck", "Checking user: " + temp.getEmailId() + ", " + temp.getPassword());
                boolean emailMatch = email != null && email.equals(temp.getEmailId());
                boolean passwordMatch = password != null && password.equals(temp.getPassword());
                if (emailMatch && passwordMatch) {
                    Log.d("LoginCheck", "Match found");
                    callback.onResult(true);
                    return;
                }
            }
            Log.d("LoginCheck", "No match found");
            callback.onResult(false);

        }, "getAllUsers"));
    }
    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Call finish on the Activity, not the Fragment
    }
}

