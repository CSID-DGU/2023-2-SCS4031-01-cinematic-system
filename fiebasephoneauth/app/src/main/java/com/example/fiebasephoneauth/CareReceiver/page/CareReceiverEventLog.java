package com.example.fiebasephoneauth.CareReceiver.page;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityCareReceiverEventLogBinding;

/**
 * <h3> 피보호자가 이벤트 발생 로그를 확인할 수 있는 페이지 </h3>
 *
 */
public class CareReceiverEventLog extends AppCompatActivity {

    private ActivityCareReceiverEventLogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverEventLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 레이아웃 요소들
        Button emerCallButton = binding.emerCallButton;
        TextView signOutText = binding.signOutText;
    }
}