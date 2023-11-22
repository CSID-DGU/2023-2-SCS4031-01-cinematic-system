package com.example.fiebasephoneauth.Guardian.page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianEventLogDetailBinding;

/**
 * <h3> 발생한 이벤트의 상세 내용을 확인할 수 있는 페이지</h3>
 */
public class GuardianEventLogDetail extends AppCompatActivity {

    ActivityGuardianEventLogDetailBinding binding;

    TextView emer_type;
    TextView event_data_time;
    TextView event_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianEventLogDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String Title = intent.getStringExtra("Title");
        String Description = intent.getStringExtra("Description");

        emer_type = (TextView) findViewById(R.id.emer_type);
        event_description = (TextView) findViewById(R.id.event_description_text);
        emer_type.setText(Title);
        event_description.setText(Description);


        binding.back.setOnClickListener(v -> {
            Intent intent1 = new Intent(GuardianEventLogDetail.this, GuardianHome.class);
            //set bottom bar
            intent1.putExtra("SELECTED_ITEM", R.id.menu_event);
            startActivity(intent1);
        });

    }
}