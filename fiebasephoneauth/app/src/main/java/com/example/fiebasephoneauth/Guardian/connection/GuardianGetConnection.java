package com.example.fiebasephoneauth.Guardian.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianGetConnectionBinding;

/**
 *  <h3> 계정 연동 페이지 </h3>
 *
 * 보호자가 계정 연동을 진행하는 페이지
 */
public class GuardianGetConnection extends AppCompatActivity {

    private ActivityGuardianGetConnectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianGetConnectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 레이아웃 요소들
        EditText nameForm = binding.nameForm;
        EditText phoneForm = binding.phoneNumForm;
        Button requestAuthNumButton = binding.requestAuthNumButton;
        Button confirmAuthNumButton = binding.confirmAuthNumButton;

    }
}