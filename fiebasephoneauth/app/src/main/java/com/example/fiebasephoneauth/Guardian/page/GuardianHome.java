package com.example.fiebasephoneauth.Guardian.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuardianHome extends AppCompatActivity {

    ActivityGuardianHomeBinding binding;

    FragmentManager fragmentManager = getSupportFragmentManager();
    GuardianMenuHomeFragment guardianMenuHomeFragment = new GuardianMenuHomeFragment();
    GuardianMenuEventFragment guardianMenuEventFragment = new GuardianMenuEventFragment();
    GuardianMenuProfileFragment guardianMenuProfileFragment = new GuardianMenuProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, guardianMenuHomeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (item.getItemId() == R.id.menu_home) {
                transaction.replace(R.id.frameLayout, guardianMenuHomeFragment).commitAllowingStateLoss();
            } else if (item.getItemId() == R.id.menu_event) {
                transaction.replace(R.id.frameLayout, guardianMenuEventFragment).commitAllowingStateLoss();
            } else if (item.getItemId() == R.id.menu_profile) {
                transaction.replace(R.id.frameLayout, guardianMenuProfileFragment).commitAllowingStateLoss();
            }
            return true;
        }
    }
}