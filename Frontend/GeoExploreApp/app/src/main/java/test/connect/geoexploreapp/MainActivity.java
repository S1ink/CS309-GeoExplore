package test.connect.geoexploreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import test.connect.geoexploreapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String userName = getIntent().getStringExtra("UserName");
        String userEmail = getIntent().getStringExtra("UserEmail");
        //Long userId= getIntent().getLongExtra("UserId",-1);

        binding.bottomNavigationView.setSelectedItemId(R.id.maps);
        replaceFragment(new MapsActivity());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//
            int itemId = item.getItemId();

            if (itemId == R.id.profile) {
                ProfileActivity profileFragment = ProfileActivity.newInstance(userName, userEmail);
                replaceFragment(profileFragment);
            } else if (itemId == R.id.maps) {
                replaceFragment(new MapsActivity());
            } else if(itemId == R.id.show_feed){
                replaceFragment(new FeedActivity());
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsActivity());
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