package com.example.fiebasephoneauth.services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fiebasephoneauth.databinding.ActivityCheckLoginServiceBinding;
import com.example.fiebasephoneauth.login.MainActivity;

/**
 * <h3>Splash and Start Page.</h3>
 * FireBase 로그인 상태를 확인하고 로그인이 안 되어있다면 로그인 화면으로 이동 <br>
 *
 * 현재는 Timeout 으로만 페이지 넘어가는 것만 구현하고 Firebase Auth 검증 기능은 포함하지 않음
 */
public class CheckLoginService extends AppCompatActivity {

    private ActivityCheckLoginServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckLoginServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CheckLoginService.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }



}