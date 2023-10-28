package com.example.fiebasephoneauth.CareReceiver.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityCareReceiverIsEmerQueryBinding;

/**
 *  <h3> 피보호자에게 응급 상황인지 묻는 페이지 </h3>
 */
public class CareReceiverIsEmerQuery extends AppCompatActivity {

    private ActivityCareReceiverIsEmerQueryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverIsEmerQueryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 레이아웃 요소들
        Button cancelButton = binding.cancelButton;

    }
}