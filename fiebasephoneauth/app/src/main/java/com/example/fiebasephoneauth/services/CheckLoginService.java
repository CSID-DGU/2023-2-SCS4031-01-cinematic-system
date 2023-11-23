package com.example.fiebasephoneauth.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.fiebasephoneauth.CareReceiver.connection.CareReceiverNotConnected;
import com.example.fiebasephoneauth.Guardian.connection.GuardianNotConnected;
import com.example.fiebasephoneauth.Guardian.page.GuardianHome;
import com.example.fiebasephoneauth.databinding.ActivityCheckLoginServiceBinding;
import com.example.fiebasephoneauth.login.CareReceiverSignInActivity;
import com.example.fiebasephoneauth.login.GuardianSignInActivity;
import com.example.fiebasephoneauth.login.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * <h3>Splash and Start Page.</h3>
 * FireBase 로그인 상태를 확인하고 로그인이 안 되어있다면 로그인 화면으로 이동 <br>
 *
 * 현재는 Timeout 으로만 페이지 넘어가는 것만 구현하고 Firebase Auth 검증 기능은 포함하지 않음
 */
public class CheckLoginService extends AppCompatActivity {

    private ActivityCheckLoginServiceBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckLoginServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //자동 로그인 여부 확인
                SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
                String id = sharedPreferences.getString("id", null);
                String pw = sharedPreferences.getString("password", null);
                String type = sharedPreferences.getString("type", null);

                checkLogin(id, pw, type);
                return;

            }
        }, 3000);

    }

    private void checkLogin(String id, String pw, String type){

        // 로그인 정보가 없을 경우
        if (id == null || pw == null || type == null) {
            Toast.makeText(CheckLoginService.this, "로그인 정보 없음", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CheckLoginService.this, MainActivity.class));
            return;
        }

        // 보호자 로그인
        if (type.equals("Guardian")){
            databaseReference.child("Guardian_list").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(id)){

                        final String getPassword = snapshot.child(id).child("password").getValue(String.class);

                        if (getPassword.equals(pw)) {
                            databaseReference.child("Guardian_list").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild("CareReceiverID")) {

                                        //GuardianHome으로 이동
                                        Intent intent = new Intent(CheckLoginService.this, GuardianHome.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(CheckLoginService.this, GuardianNotConnected.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        // 피보호자 로그인
        else if (type.equals("CareReceiver")) {
            databaseReference.child("CareReceiver_list").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild(id)){

                        final String getPassword = snapshot.child(id).child("password").getValue(String.class);

                        if(getPassword.equals(pw)){
                            Toast.makeText(CheckLoginService.this, "로그인 성공 !", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(CheckLoginService.this, CareReceiverNotConnected.class));
                            finish();
                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }



}