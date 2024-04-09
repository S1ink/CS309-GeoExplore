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
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        user = (User) getIntent().getSerializableExtra("UserObject");

        String userID = String.valueOf(user.getId());
        WebSocketManager.getInstance().connectWebSocket("ws://coms-309-005.class.las.iastate.edu:8080/live/alerts/" + userID);

        String userName = getIntent().getStringExtra("UserName");
        Long userId = getIntent().getLongExtra("UserID",-1);
        String userEmail = getIntent().getStringExtra("UserEmail");
        boolean isAdmin = getIntent().getBooleanExtra("IsAdmin",false);


        if(user!= null){
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
                ProfileActivity profileFragment = ProfileActivity.newInstance(user);
                replaceFragment(profileFragment);
            } else if (itemId == R.id.maps) {
                replaceFragment(new MapsActivity());
            } else if(itemId == R.id.show_feed){
                WebSocketManager.getInstance().connectWebSocket("ws://coms-309-005.class.las.iastate.edu:8080/comments/"+user.getId()); //URL ADD LATER

                FeedActivity feedActivity = FeedActivity.newInstance(user);
                replaceFragment(feedActivity);
            } else if (itemId == R.id.settings) {
                SettingsActivity settingsFragment = SettingsActivity.newInstance(user.getIsAdmin());
                replaceFragment(settingsFragment);
            }else if(itemId == R.id.usergroups){
                UserGroupActivity userGroupsFragment = UserGroupActivity.newInstance(user);
                replaceFragment(userGroupsFragment);
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