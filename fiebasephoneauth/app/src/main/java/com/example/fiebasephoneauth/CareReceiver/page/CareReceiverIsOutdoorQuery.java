package com.example.fiebasephoneauth.CareReceiver.page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityCareReceiverIsOutdoorQueryBinding;

/**
 * <h3> 피보호자에게 외출인지 묻는 페이지 </h3>
 */
public class CareReceiverIsOutdoorQuery extends AppCompatActivity {

    private ActivityCareReceiverIsOutdoorQueryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverIsOutdoorQueryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 레이아웃 요소들
        Button yesButton = binding.yesButton;
        Button noButton = binding.noButton;
    }
}