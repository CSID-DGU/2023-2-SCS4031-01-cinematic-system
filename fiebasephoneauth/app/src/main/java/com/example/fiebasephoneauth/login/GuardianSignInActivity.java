package com.example.fiebasephoneauth.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.fiebasephoneauth.databinding.ActivityGuardianSignInBinding;

public class GuardianSignInActivity extends AppCompatActivity {

    Button loginButton;
    TextView backButton;

    ActivityGuardianSignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButton = binding.loginButton;
        backButton = binding.backButton;

        loginButton.setOnClickListener(v -> {
            // 로그인을 클릭할 경우
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuardianSignInActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}