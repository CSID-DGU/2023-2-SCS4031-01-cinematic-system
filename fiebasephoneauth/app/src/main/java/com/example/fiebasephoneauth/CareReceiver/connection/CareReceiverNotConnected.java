package com.example.fiebasephoneauth.CareReceiver.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityCareReceiverNotConnectedBinding;
import com.example.fiebasephoneauth.login.MainActivity;

/**
 * <h3> 피보호자가 아직 보호자와 연결되지 않았을 때 노출되는 페이지</h3>
 *
 * 보호자를 통해 계정을 연동할 것을 요청하는 문구를 띄움
 */
public class CareReceiverNotConnected extends AppCompatActivity {

    private ActivityCareReceiverNotConnectedBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverNotConnectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 새로고침 버튼
        Button reloadButton = binding.reloadButton;

        // 로그아웃 버튼
        Button logoutButton = binding.logoutButton;

        logoutButton.setOnClickListener(v -> {
            // 로그아웃 버튼을 누르면 로그인 화면으로 이동
            SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(CareReceiverNotConnected.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}