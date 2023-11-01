package com.example.fiebasephoneauth.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.fiebasephoneauth.databinding.ActivityCareReceiverSignInBinding;

public class CareReceiverSignInActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView backButton;
    ActivityCareReceiverSignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButton = binding.loginButton;
        backButton = binding.backButton;

        loginButton.setOnClickListener(v -> {
            // 로그인을 클릭할 경우
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CareReceiverSignInActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}