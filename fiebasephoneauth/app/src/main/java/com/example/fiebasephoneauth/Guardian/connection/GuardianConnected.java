package com.example.fiebasephoneauth.Guardian.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianConnectedBinding;

public class GuardianConnected extends AppCompatActivity {

    ActivityGuardianConnectedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGuardianConnectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}