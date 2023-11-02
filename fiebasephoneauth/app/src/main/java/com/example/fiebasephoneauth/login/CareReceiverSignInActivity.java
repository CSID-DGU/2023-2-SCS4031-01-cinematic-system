package com.example.fiebasephoneauth.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fiebasephoneauth.CareReceiver.connection.CareReceiverNotConnected;
import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityCareReceiverSignInBinding;
import com.example.fiebasephoneauth.databinding.ActivityGuardianSignInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

                                    startActivity(new Intent(CareReceiverSignInActivity.this, CareReceiverNotConnected.class));
                                    finish();
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