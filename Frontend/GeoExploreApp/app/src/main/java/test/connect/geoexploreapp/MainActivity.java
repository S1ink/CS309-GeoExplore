package test.connect.geoexploreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import test.connect.geoexploreapp.databinding.ActivityMainBinding;
import test.connect.geoexploreapp.model.User;
import test.connect.geoexploreapp.websocket.WebSocketManager;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String userName = getIntent().getStringExtra("UserName");
        String userEmail = getIntent().getStringExtra("UserEmail");
        boolean isAdmin = getIntent().getBooleanExtra("IsAdmin",false);

        String userJson = getIntent().getStringExtra("UserJson");
        //Long userId= getIntent().getLongExtra("UserId",-1);


        if(userJson!= null){
            Gson gson = new Gson();

            User user = gson.fromJson(userJson,User.class);

            SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

            viewModel.setLoggedInUser(user);

            Log.d("MainActivity", "User: " + user);


        }

        binding.bottomNavigationView.setSelectedItemId(R.id.maps);
        replaceFragment(new MapsActivity());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//
            int itemId = item.getItemId();

            if (itemId == R.id.profile) {
                ProfileActivity profileFragment = ProfileActivity.newInstance(userName, userEmail,isAdmin);
                replaceFragment(profileFragment);
            } else if (itemId == R.id.maps) {
                replaceFragment(new MapsActivity());
            } else if(itemId == R.id.show_feed){
                WebSocketManager.getInstance().connectWebSocket("wss://socketsbay.com/wss/v2/1/demo/"); //URL ADD LATER
                FeedActivity feedActivity = FeedActivity.newInstance(userName);
                replaceFragment(feedActivity);
            } else if (itemId == R.id.settings) {
                SettingsActivity settingsFragment = SettingsActivity.newInstance(isAdmin);
                replaceFragment(settingsFragment);
            }

            return true;


        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();

    }
}