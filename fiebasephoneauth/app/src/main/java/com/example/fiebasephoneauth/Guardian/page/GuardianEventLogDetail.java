package com.example.fiebasephoneauth.Guardian.page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianEventLogDetailBinding;

/**
 * <h3> 발생한 이벤트의 상세 내용을 확인할 수 있는 페이지</h3>
 */
public class GuardianEventLogDetail extends AppCompatActivity {

    ActivityGuardianEventLogDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianEventLogDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> {
            Intent intent = new Intent(GuardianEventLogDetail.this, GuardianHome.class);
            //set bottom bar
            intent.putExtra("SELECTED_ITEM", R.id.menu_event);
            startActivity(intent);
        });

    }
}