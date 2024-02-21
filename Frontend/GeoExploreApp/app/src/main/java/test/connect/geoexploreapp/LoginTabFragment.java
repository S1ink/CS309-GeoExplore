package test.connect.geoexploreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


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
                String email = UserEmail.getText().toString();
                String passcode = UserPassword.getText().toString();
                // Perform your actions here
                // For example, show an alert dialog
                //showAlert("Login Attempted!");
                if (isValidCredentials(email, passcode)) {
                    // Start MainActivity if login is successful
                    startMainActivity();
                } else {
                    showAlert("Invalid Credentials!");
                }
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
    private boolean isValidCredentials(String email, String password) {
        // CHECK IF USER EXISTS HERE
        return !email.isEmpty() && !password.isEmpty();
    }
    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Call finish on the Activity, not the Fragment
    }
}