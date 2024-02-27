package test.connect.geoexploreapp;

import static test.connect.geoexploreapp.api.ApiClientFactory.GetUserApi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.connect.geoexploreapp.api.SlimCallback;
import test.connect.geoexploreapp.api.UserApi;
import test.connect.geoexploreapp.model.User;

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
                String fullName = FirstName.getText().toString() + " " + LastName.getText().toString();
                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
                String confirmPassword = ConfirmPassword.getText().toString();
                boolean isAdmin = IsAdmin.isChecked();

                if (fullName.isEmpty() ||  email.isEmpty() ||  password.isEmpty() ||  confirmPassword.isEmpty()){
                    showAlert("Invalid Credentials!");

                }else if (password.equals(confirmPassword)==false){
                    showAlert("Invalid Credentials!");

                }else {
                    User newUser = new User();
                    newUser.setName(fullName);
                    newUser.setEmailId(email);
                    newUser.setIsAdmin(isAdmin);
                    newUser.setPassword(password);

                    createUser(newUser);
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


    public void createUser(User newUser){
        Log.d("LoginCheck", "Checking user " + newUser.toString());

        GetUserApi().UserCreate(newUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User createdUser = response.body();
                    Log.d("API_SUCCESS", "User created: " + createdUser.toString());
                    startMainActivity();
                } else {
                    Log.e("API_FAILURE", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_FAILURE", "Call failed", t);
            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Call finish on the Activity, not the Fragment
    }
}