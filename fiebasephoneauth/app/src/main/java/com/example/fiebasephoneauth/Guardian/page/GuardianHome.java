package com.example.fiebasephoneauth.Guardian.page;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianHomeBinding;
import com.example.fiebasephoneauth.login.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

/**
 * <h3> Guardian Home Activity </h3>
 *
 * GuardianMenuHomeFragment, GuardianMenuEventFragment, GuardianMenuProfileFragment를
 * BottomNavigationView를 통해 전환할 수 있음
 */
public class GuardianHome extends AppCompatActivity {
    ActivityGuardianHomeBinding binding;

    Intent intent;
    String idTxt;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    FragmentManager fragmentManager = getSupportFragmentManager();
    GuardianMenuHomeFragment guardianMenuHomeFragment = new GuardianMenuHomeFragment();
    GuardianMenuEventFragment guardianMenuEventFragment = new GuardianMenuEventFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        idTxt = intent.getStringExtra("id");

        binding = ActivityGuardianHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, guardianMenuHomeFragment).commit();

        Bundle bundle = new Bundle();
        bundle.putString("id",idTxt);
        Log.d(TAG, "onCreate: "+idTxt);
        guardianMenuHomeFragment.setArguments(bundle);
        guardianMenuEventFragment.setArguments(bundle);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        int selectedItemId = getIntent().getIntExtra("SELECTED_ITEM", 0);
        if (selectedItemId != 0) {
            // 선택된 아이템이 있다면 해당 아이템을 선택하도록 함
            bottomNavigationView.setSelectedItemId(selectedItemId);
        }

    }



    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (item.getItemId() == R.id.menu_home) {
                transaction.replace(R.id.frameLayout, guardianMenuHomeFragment).commit();
            } else if (item.getItemId() == R.id.menu_event) {
                ArrayList<String> dataList = guardianMenuHomeFragment.getDataList();
                transaction.replace(R.id.frameLayout, guardianMenuEventFragment.newInstance(idTxt,dataList)).commit();
            } else if (item.getItemId() == R.id.menu_logout) {
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

                        DatabaseReference ref = databaseReference.child("Guardian_list").child(idTxt);
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


                Intent intent = new Intent(GuardianHome.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            return true;
        }
    }
}

