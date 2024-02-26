package test.connect.geoexploreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


public class SignupTabFragment extends Fragment {
    EditText UserEmail,UserPassword, FirstName, LastName, ConfirmPassword;
    CheckBox IsAdmin;
    Button signinSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirstName=view.findViewById(R.id.firstName);
        LastName=view.findViewById(R.id.lastName);
        UserEmail=view.findViewById(R.id.signup_email);
        UserPassword=view.findViewById(R.id.signup_password);
        ConfirmPassword=view.findViewById(R.id.signup_confirm);
        signinSubmit = view.findViewById(R.id.signup_button);
        IsAdmin = (CheckBox) view.findViewById(R.id.is_admin);



        // Set the click listener for the button
        signinSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = FirstName.getText().toString();
                String lastName = LastName.getText().toString();
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
                String confirmPassword = ConfirmPassword.getText().toString();
                boolean isAdmin = IsAdmin.isChecked();
                // Perform your actions here
                // For example, show an alert dialog
                //showAlert("Login Attempted!");
                if (isValidCredentials(firstName, lastName, email, password, confirmPassword)) {
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
    private boolean isValidCredentials(String firstName, String lastName, String email, String password, String confirmPassword) {
        // CHECK IF USER EXISTS HERE
        if (firstName.isEmpty() || lastName.isEmpty() ||  email.isEmpty() ||  password.isEmpty() ||  confirmPassword.isEmpty()){
          return false;
        }else if (password.equals(confirmPassword)==false){
            return false;
        }
        return true;
    }
    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Call finish on the Activity, not the Fragment
    }
}