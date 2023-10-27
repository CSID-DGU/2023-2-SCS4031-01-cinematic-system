package com.example.fiebasephoneauth.CareReceiver.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityCareReceiverNotConnectedBinding;

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
    }
}