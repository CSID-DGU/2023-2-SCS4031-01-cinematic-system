package com.example.fiebasephoneauth.CareReceiver.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.fiebasephoneauth.CareReceiver.page.CareReceiverEventLog;
import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityCareReceiverNotConnectedBinding;
import com.example.fiebasephoneauth.login.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

/**
 * <h3> 피보호자가 아직 보호자와 연결되지 않았을 때 노출되는 페이지</h3>
 *
 * 보호자를 통해 계정을 연동할 것을 요청하는 문구를 띄움
 */
public class CareReceiverNotConnected extends AppCompatActivity {

    private ActivityCareReceiverNotConnectedBinding binding;
    DatabaseReference database = com.google.firebase.database.FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverNotConnectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 새로고침 버튼
        Button reloadButton = binding.reloadButton;

        // 로그아웃 버튼
        Button logoutButton = binding.logoutButton;
        String idTxt = getIntent().getStringExtra("id");


        reloadButton.setOnClickListener(v -> {
            // 보호자와 연결되었는지 확인
            DatabaseReference ref = database.child("CareReceiver_list").child(idTxt);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("checkConnection") && dataSnapshot.child("checkConnection").getValue(String.class).equals("1")){
                        Intent intent = new Intent(CareReceiverNotConnected.this, CareReceiverEventLog.class);
                        intent.putExtra("id", idTxt);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CareReceiverNotConnected.this, "보호자와 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        });


        logoutButton.setOnClickListener(v -> {
            //자동 로그인 정보 삭제
            SharedPreferences AutoLoginsharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
            SharedPreferences.Editor AutoLoginEditor = AutoLoginsharedPreferences.edit();
            AutoLoginEditor.clear();
            AutoLoginEditor.commit();

            //deviceToken 삭제
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    Log.i("token", token);

                    DatabaseReference ref = database.child("CareReceiver_list").child(idTxt);
                    ref.child("deviceToken").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> deviceToken = (ArrayList<String>) dataSnapshot.getValue();

                            if(deviceToken.contains(token)){
                                deviceToken.remove(token);
                                ref.child("deviceToken").setValue(deviceToken);
                                return;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            });

            Intent intent = new Intent(CareReceiverNotConnected.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}