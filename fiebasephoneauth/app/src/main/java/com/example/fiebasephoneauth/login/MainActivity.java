package com.example.fiebasephoneauth.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.fiebasephoneauth.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Button btnCreateAccount, btnLoginAsCareReceiver, btnLoginAsGuardian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnLoginAsCareReceiver = binding.loginAsCareReceiverButton;
        btnLoginAsGuardian = binding.loginAsGuardianButton;
        btnCreateAccount = binding.signupButton;

        btnLoginAsCareReceiver.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CareReceiverSignInActivity.class);
            startActivity(intent);
        });

        btnLoginAsGuardian.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GuardianSignInActivity.class);
            startActivity(intent);
        });

        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        getFCMToken();
    }

    void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i("My token", token);
            }
        });
    }

}