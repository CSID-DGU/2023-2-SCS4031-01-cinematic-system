package com.example.fiebasephoneauth.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.CareReceiver.connection.CareReceiverNotConnected;
import com.example.fiebasephoneauth.CareReceiver.page.CareReceiverEventLog;
import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianSignInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class CareReceiverSignInActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");
    Button loginButton;
    TextView backButton;

    ActivityGuardianSignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButton = binding.loginButton;
        backButton = binding.backButton;


        setContentView(R.layout.activity_guardian_sign_in);

        final EditText id = findViewById(R.id.idForm);
        final EditText password = findViewById(R.id.pwForm);
        final Button loginBtn = findViewById(R.id.loginButton);
        final TextView backBtn = findViewById(R.id.backButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CareReceiverSignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idTxt = id.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(idTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(CareReceiverSignInActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("CareReceiver_list").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(idTxt)){

                                final String getPassword = snapshot.child(idTxt).child("password").getValue(String.class);

                                if(getPassword.equals(passwordTxt)){
                                    Toast.makeText(CareReceiverSignInActivity.this, "로그인 성공 !", Toast.LENGTH_SHORT).show();

                                    databaseReference.child("CareReceiver_list").child(idTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.hasChild("checkConnection") &&  snapshot.child("checkConnection").getValue(String.class).equals("1")){
                                                //기존 로그인 정보 삭제
                                                SharedPreferences sharedPreferences = getSharedPreferences("autoLogin",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.clear();
                                                editor.commit();

                                                //자동 로그인을 위한 로그인 정보 저장
                                                editor.putString("id",idTxt);
                                                editor.putString("password",passwordTxt);
                                                editor.putString("type", "CareReceiver");
                                                editor.commit();

                                                //파이어베이스에 deviceToken 저장
                                                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        String token = task.getResult();
                                                        Log.i("token", token);

                                                        DatabaseReference ref = databaseReference.child("CareReceiver_list").child(idTxt);
                                                        ref.child("deviceToken").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                ArrayList<String> deviceToken = (ArrayList<String>) dataSnapshot.getValue();
                                                                if (deviceToken == null) {
                                                                    deviceToken = new ArrayList<>();
                                                                }
                                                                if(deviceToken.contains(token)){
                                                                    Log.i("deviceToken", "이미 등록된 deviceToken");
                                                                    return;
                                                                }
                                                                deviceToken.add(token);
                                                                ref.child("deviceToken").setValue(deviceToken);
                                                                Log.i("deviceToken", "deviceToken 저장 완료");
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                                // Handle error
                                                            }
                                                        });
                                                    }
                                                });

                                                // CareReceiverEventLog로 이동
                                                Intent intent = new Intent(CareReceiverSignInActivity.this, CareReceiverEventLog.class);
                                                intent.putExtra("id",idTxt);
                                                startActivity(intent);
                                                finish();
                                            }

                                            else{
                                                Intent intent = new Intent(CareReceiverSignInActivity.this, CareReceiverNotConnected.class);
                                                intent.putExtra("id",idTxt);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                else{
                                    Toast.makeText(CareReceiverSignInActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(CareReceiverSignInActivity.this, "아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });


    }
}