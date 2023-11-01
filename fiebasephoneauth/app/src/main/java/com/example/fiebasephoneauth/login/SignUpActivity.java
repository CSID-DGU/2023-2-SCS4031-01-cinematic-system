package com.example.fiebasephoneauth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivitySignupBinding;
import com.example.fiebasephoneauth.databinding.FragmentGuardianSignUpFormBinding;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    private final int GUARDIAN = 0;
    private final int CARE_RECEIVER = 1;

    private TextView backButton;
    private ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backButton = binding.backButton;

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });

        binding.guardianButton.setOnClickListener(v -> {
            FragmentView(GUARDIAN);
        });

        binding.careReceiverButton.setOnClickListener(v -> {
            FragmentView(CARE_RECEIVER);
        });

        FragmentView(GUARDIAN);
    }

    private void FragmentView(int Fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (Fragment){
            case GUARDIAN:
                GuardianSignUpFormFragment guardianSignUpFormFragment = new GuardianSignUpFormFragment();
                transaction.replace(R.id.frameLayout, guardianSignUpFormFragment);
                transaction.commit();
                break;
            case CARE_RECEIVER:
                CareReceiverSignUpFormFragment careReceiverSignupFormFragment = new CareReceiverSignUpFormFragment();
                transaction.replace(R.id.frameLayout, careReceiverSignupFormFragment);
                transaction.commit();
                break;
        }
    }

}