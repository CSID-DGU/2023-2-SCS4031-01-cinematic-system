package com.example.fiebasephoneauth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        String text = intent.getStringExtra("text");

        TextView text_tv = findViewById(R.id.text_tv);
        text_tv.setText(text);
//
//        firebaseAuth = FirebaseAuth.getInstance();
////        checkUserStatus();
//
//        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.signOut();
////                checkUserStatus();
//            }
//        });
    }
//    private void checkUserStatus() {
//
//        setContentView(R.layout.activity_profile);
//
//        TextView textView = (TextView)findViewById(R.id.phoneTv);
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if(firebaseUser != null){
//            String phone = firebaseUser.getPhoneNumber();
//            binding.phoneTv.setText(phone);
//        }
//        else{
//            finish();
//        }
//    }
}