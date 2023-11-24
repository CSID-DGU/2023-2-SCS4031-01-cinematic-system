package com.example.fiebasephoneauth.CareReceiver.page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.databinding.ActivityCareReceiverEventLogBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * <h3> 피보호자가 이벤트 발생 로그를 확인할 수 있는 페이지 </h3>
 *
 */
public class CareReceiverEventLog extends AppCompatActivity {
    DatabaseReference checkOuting = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/")
            .child("CareReceiver_list");
    private ActivityCareReceiverEventLogBinding binding;

    String checkOutingValue;
    String outingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareReceiverEventLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //로그인 한 회원 ID
        Intent intent = getIntent();
        String idTxt = intent.getStringExtra("id");

        // 레이아웃 요소들
        Button emerCallButton = binding.emerCallButton;
        TextView signOutText = binding.signOutText;


        checkOuting.child(idTxt).child("ActivityData").child("door").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        checkOutingValue = snapshot.child("checkouting").getValue(String.class);
                        outingValue = snapshot.child("outing").getValue(String.class);
                        if(snapshot.getKey().matches("checkouting")){
                            if (snapshot.getValue().equals("1")){
                                Intent intent1 = new Intent(CareReceiverEventLog.this, CareReceiverIsOutdoorQuery.class);
                                intent1.putExtra("id",idTxt);
                                startActivity(intent1);
                            }
                        }
                        else if(snapshot.getKey().matches("outing")){
                            if(snapshot.getValue().equals("1")){
                                checkOuting.child(idTxt).child("ActivityData").child("door").child("checkouting").setValue("0");
                                checkOuting.child(idTxt).child("ActivityData").child("door").child("outing").setValue("0");
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}