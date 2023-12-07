package com.example.fiebasephoneauth.Guardian.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianSeeCareReceiversDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuardianSeeCareReceiversDetail extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference careReceiverRef, guardianRef;
    TextView guardianName, guardianPhone, careReceiverName, careReceiverPhone, careReceiverAddress, careGiverName, careGiverPhone;
    Button backBtn;

    ActivityGuardianSeeCareReceiversDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianSeeCareReceiversDetailBinding.inflate(getLayoutInflater());

        // 컴포넌트 연결
        guardianName = binding.guardianName;
        guardianPhone = binding.guardianPhone;
        careReceiverName = binding.careReceiverName;
        careReceiverPhone = binding.careReceiverPhone;
        careReceiverAddress = binding.careReceiverAddress;
        careGiverName = binding.careGiverName;
        careGiverPhone = binding.careGiverPhone;
        backBtn = binding.backButton;

        // 데이터베이스 연결
        String receiverIdTxt = getIntent().getStringExtra("receiverId");
        String guardianIdTxt = getIntent().getStringExtra("id");
        careReceiverRef = databaseReference.child("CareReceiver_list").child(receiverIdTxt);
        guardianRef = databaseReference.child("Guardian_list").child(guardianIdTxt);

        careReceiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String careReceiverNameTxt = snapshot.child("name").getValue(String.class);
                String careReceiverPhoneTxt = snapshot.child("phoneNum").getValue(String.class);
                careReceiverPhoneTxt = careReceiverPhoneTxt.substring(0,3) + "-" + careReceiverPhoneTxt.substring(3,7) + "-" + careReceiverPhoneTxt.substring(7,11);
                String careReceiverAddressTxt = snapshot.child("Address").getValue(String.class);
                String careGiverNameTxt = snapshot.child("CareGiverName").getValue(String.class);
                String careGiverPhoneTxt = snapshot.child("CareGiverPhoneNum").getValue(String.class);
                careGiverPhoneTxt = careGiverPhoneTxt.substring(0,3) + "-" + careGiverPhoneTxt.substring(3,7) + "-" + careGiverPhoneTxt.substring(7,11);

                careReceiverName.setText(careReceiverNameTxt);
                careReceiverPhone.setText(careReceiverPhoneTxt);
                careReceiverAddress.setText(careReceiverAddressTxt);
                careGiverName.setText(careGiverNameTxt);
                careGiverPhone.setText(careGiverPhoneTxt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        guardianRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String guardianNameTxt = snapshot.child("name").getValue(String.class);
                String guardianPhoneTxt = snapshot.child("phoneNum").getValue(String.class);
                guardianPhoneTxt = guardianPhoneTxt.substring(0,3) + "-" + guardianPhoneTxt.substring(3,7) + "-" + guardianPhoneTxt.substring(7,11);

                guardianName.setText(guardianNameTxt);
                guardianPhone.setText(guardianPhoneTxt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        setContentView(binding.getRoot());
    }
}