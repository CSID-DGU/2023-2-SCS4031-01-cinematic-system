package com.example.fiebasephoneauth.Guardian.connection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.Guardian.page.GuardianHome;
import com.example.fiebasephoneauth.databinding.ActivityGuardianGetConnectionBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 *  <h3> 계정 연동 페이지 </h3>
 *
 * 보호자가 계정 연동을 진행하는 페이지
 */
public class GuardianGetConnection extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    private ActivityGuardianGetConnectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianGetConnectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 레이아웃 요소들
        EditText nameForm = binding.nameForm;
        EditText phoneForm = binding.phoneNumForm;
        Button requestAuthNumButton = binding.requestAuthNumButton;
        Button confirmAuthNumButton = binding.confirmAuthNumButton;
        TextView logoutText = binding.logoutText;
        Button singup_button = binding.signupButton;

        singup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idTxt = nameForm.getText().toString();
                final String phoneTxt = phoneForm.getText().toString();

                if(idTxt.isEmpty() || phoneTxt.isEmpty()){
                    Toast.makeText(GuardianGetConnection.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("CareReceiver_list").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(idTxt)){

                                final String getPassword = snapshot.child(idTxt).child("phoneNum").getValue(String.class);

                                if(getPassword.equals(phoneTxt)){
                                    Toast.makeText(GuardianGetConnection.this, "연동 성공 !", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(GuardianGetConnection.this, GuardianHome.class);
                                    intent.putExtra("id",idTxt);
                                    startActivity(intent);
                                    finish();
                                }

                                else{
                                    Toast.makeText(GuardianGetConnection.this, "전화번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(GuardianGetConnection.this, "보호자 아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
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