package com.example.fiebasephoneauth.Guardian.connection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityGuardianGetConnectionBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *  <h3> 계정 연동 페이지 </h3>
 *
 * 보호자가 계정 연동을 진행하는 페이지
 */
public class GuardianGetConnection extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth;
    String verificationID;

    private ActivityGuardianGetConnectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianGetConnectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 레이아웃 요소들
        EditText nameForm = binding.nameForm;
        EditText phoneForm = binding.phoneNumForm;
        EditText phoneNumConfirmForm = binding.phoneNumConfirmForm;
        Button requestAuthNumButton = binding.requestAuthNumButton;
        Button confirmAuthNumButton = binding.confirmAuthNumButton;
        TextView logoutText = binding.logoutText;
        Button singup_button = binding.signupButton;
        singup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuardianGetConnection.this, GuardianConnected.class);
                            EditText nameForm = binding.nameForm;
                            final String idTxt = nameForm.getText().toString();
                            intent.putExtra("id",idTxt);
                            startActivity(intent);
                            finish();
            }
        });
//        mAuth = FirebaseAuth.getInstance();
//
//        requestAuthNumButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(TextUtils.isEmpty(phoneForm.getText().toString())){
//                    Toast.makeText(GuardianGetConnection.this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    String number = phoneForm.getText().toString();
//                    sendverificationcode(number);
//                }
//            }
//        });
//
//        confirmAuthNumButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(TextUtils.isEmpty(phoneNumConfirmForm.getText().toString())){
//                    Toast.makeText(GuardianGetConnection.this, "인증번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    verifycode(phoneNumConfirmForm.getText().toString());
//                }
//            }
//        });
//    }
//
//    private void sendverificationcode(String phoneNumber) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+82" + phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // (optional) Activity for callback binding
//                        // If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//            final String code = credential.getSmsCode();
//            if(code!=null){
//                verifycode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(GuardianGetConnection.this,"인증 실패",Toast.LENGTH_SHORT).show();
//
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s,
//                               PhoneAuthProvider.@NonNull ForceResendingToken token) {
//
//            super.onCodeSent(s, token);
//            verificationID = s;
//        }
//    };
//
//    private void verifycode(String Code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
//        signinbyCredentials(credential);
//    }
//
//    private void signinbyCredentials(PhoneAuthCredential credential) {
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
//
//                        if(task.isSuccessful()){
//                            Toast.makeText(GuardianGetConnection.this,"연동 성공",Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(GuardianGetConnection.this, GuardianConnected.class);
//                            EditText nameForm = binding.nameForm;
//                            final String idTxt = nameForm.getText().toString();
//                            intent.putExtra("id",idTxt);
//                            startActivity(intent);
//                            finish();
//
//                        }
//
//                    }
//                });
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(GuardianGetConnection.this, GuardianHome.class);
//            EditText nameForm = binding.nameForm;
//            final String idTxt = nameForm.getText().toString();
//            intent.putExtra("id",idTxt);
//            startActivity(intent);
//            finish();
//        }
    }
}