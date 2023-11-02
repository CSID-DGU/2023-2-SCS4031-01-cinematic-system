package com.example.fiebasephoneauth.Guardian.connection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityGuardianNotConnectedBinding;

/**
 * <h3> 보호자가 아직 피보호자와 연결되지 않았을 때 노출되는 페이지 </h3>
 *
 * 계정을 연동하도록 연동하기 버튼을 띄움
 */
public class GuardianNotConnected extends AppCompatActivity {

    private ActivityGuardianNotConnectedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianNotConnectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 연동하기 버튼
        Button getConnectButton = binding.getConnectButton;

        getConnectButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuardianNotConnected.this, GuardianGetConnection.class);
            startActivity(intent);
        });
    }
}