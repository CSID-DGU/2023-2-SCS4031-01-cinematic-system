package com.example.fiebasephoneauth.Guardian.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fiebasephoneauth.Guardian.page.GuardianHome;
import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianConnectedBinding;

public class GuardianConnected extends AppCompatActivity {

    ActivityGuardianConnectedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianConnectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String idTxt = intent.getStringExtra("id");

        // 연동하기 버튼
        Button get_start_button = binding.getStartButton;

        get_start_button.setOnClickListener(v -> {
            Intent intent2 = new Intent(GuardianConnected.this, GuardianHome.class);
            intent2.putExtra("id",idTxt);
            startActivity(intent2);
        });

    }
}