package com.example.fiebasephoneauth.Guardian.connection;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianGetConnectionBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *  <h3> 계정 연동 페이지 </h3>
 *
 * 보호자가 계정 연동을 진행하는 페이지
 */
public class GuardianGetConnection extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");




    TextView nameInfo;
    TextView phoneInfo;
    TextView phoneNumConfirmText;
    TextView logoutText;
    EditText nameForm;
    EditText phoneForm;
    EditText codeForm;
    Button requestAuthNumButton;
    Button confirmAuthNumButton;
    Button signupButton;

    private ActivityGuardianGetConnectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianGetConnectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_guardian_get_connection);

        nameInfo = findViewById(R.id.nameText);
        phoneInfo = findViewById(R.id.phoneNumText);
        phoneNumConfirmText = findViewById(R.id.phoneNumConfirmText);
        logoutText = findViewById(R.id.logoutText);
        nameForm = findViewById(R.id.nameForm);
        phoneForm = findViewById(R.id.phoneNumForm);
        codeForm = findViewById(R.id.phoneNumConfirmForm);
        requestAuthNumButton = findViewById(R.id.requestAuthNum_button);
        confirmAuthNumButton = findViewById(R.id.confirmAuthNum_button);
        signupButton = findViewById(R.id.signup_button);

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = nameForm.getText().toString();
            }
        });

    }
    public void setMode(){

    }

}