package com.example.fiebasephoneauth.CareReceiver.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityCareReceiverIsEmerQueryBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *  <h3> 피보호자에게 응급 상황인지 묻는 페이지 </h3>
 */
public class CareReceiverIsEmerQuery extends AppCompatActivity {
    DatabaseReference emer = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/")
            .child("CareReceiver_list");

    private ActivityCareReceiverIsEmerQueryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverIsEmerQueryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //로그인 한 회원 ID
        Intent intent = getIntent();
        String idTxt = intent.getStringExtra("id");

        // 레이아웃 요소들
        Button cancelButton = binding.cancelButton;


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emer.child(idTxt).child("ActivityData").child("emergency").setValue("0");

                Intent intent1 = new Intent(CareReceiverIsEmerQuery.this, CareReceiverEventLog.class);
                intent1.putExtra("id",idTxt);
                startActivity(intent1);
            }
        });
    }
}