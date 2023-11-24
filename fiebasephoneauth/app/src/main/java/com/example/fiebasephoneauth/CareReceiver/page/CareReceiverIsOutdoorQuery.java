package com.example.fiebasephoneauth.CareReceiver.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityCareReceiverIsOutdoorQueryBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * <h3> 피보호자에게 외출인지 묻는 페이지 </h3>
 */
public class CareReceiverIsOutdoorQuery extends AppCompatActivity {
    DatabaseReference checkOuting = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/")
            .child("CareReceiver_list");

    private ActivityCareReceiverIsOutdoorQueryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverIsOutdoorQueryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String idTxt = intent.getStringExtra("id");

        // 레이아웃 요소들
        Button yesButton = binding.yesButton;
        Button noButton = binding.noButton;


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOuting.child(idTxt).child("ActivityData").child("door").child("checkouting").setValue("0");
                checkOuting.child(idTxt).child("ActivityData").child("door").child("outing").setValue("1");
                Intent intent1 = new Intent(CareReceiverIsOutdoorQuery.this,CareReceiverEventLog.class);
                intent1.putExtra("id",idTxt);
                startActivity(intent1);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOuting.child(idTxt).child("ActivityData").child("door").child("checkouting").setValue("0");
                checkOuting.child(idTxt).child("ActivityData").child("door").child("outing").setValue("0");
                Intent intent1 = new Intent(CareReceiverIsOutdoorQuery.this,CareReceiverEventLog.class);
                intent1.putExtra("id",idTxt);
                startActivity(intent1);
            }
        });
    }
}