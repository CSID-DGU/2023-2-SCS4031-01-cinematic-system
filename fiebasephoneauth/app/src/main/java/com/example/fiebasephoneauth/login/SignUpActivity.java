package com.example.fiebasephoneauth.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivitySignupBinding;
import com.example.fiebasephoneauth.databinding.FragmentGuardianSignUpFormBinding;

public class SignUpActivity extends AppCompatActivity {

    private final int GUARDIAN = 0;
    private final int CARE_RECEIVER = 1;

    private ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                CareReceiverSignupFormFragment careReceiverSignupFormFragment = new CareReceiverSignupFormFragment();
                transaction.replace(R.id.frameLayout, careReceiverSignupFormFragment);
                transaction.commit();
                break;
        }
    }

}