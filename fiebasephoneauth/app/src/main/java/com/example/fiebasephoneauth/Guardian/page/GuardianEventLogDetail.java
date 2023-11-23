package com.example.fiebasephoneauth.Guardian.page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianEventLogDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * <h3> 발생한 이벤트의 상세 내용을 확인할 수 있는 페이지</h3>
 */
public class GuardianEventLogDetail extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    ActivityGuardianEventLogDetailBinding binding;

    TextView emer_type;
    TextView event_time;
    TextView event_description;
    TextView care_receiver_name;
    TextView care_receiver_text;
    TextView care_giver_name;
    TextView care_giver_text;
    CardView care_receiver_phone_number;
    CardView care_giver_phone_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianEventLogDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String Title = intent.getStringExtra("Title");
        String Description = intent.getStringExtra("Description");
        String Date = intent.getStringExtra("Date");
        String Time = intent.getStringExtra("Time");
        String idTxt = intent.getStringExtra("id");

        emer_type = (TextView) findViewById(R.id.emer_type);
        event_description = (TextView) findViewById(R.id.event_description_text);
        event_time = (TextView) findViewById(R.id.event_date_time_text);

        care_receiver_phone_number = (CardView) findViewById(R.id.care_receiver_phone_number);
        care_receiver_name = care_receiver_phone_number.findViewById(R.id.phone_number_name);
        care_receiver_text = care_receiver_phone_number.findViewById(R.id.phone_number_text);

        care_giver_phone_number = (CardView) findViewById(R.id.care_giver_phone_number);
        care_giver_name = care_giver_phone_number.findViewById(R.id.phone_number_name);
        care_giver_text = care_giver_phone_number.findViewById(R.id.phone_number_text);


        emer_type.setText(Title);
        event_description.setText(Description);
        event_time.setText(Date + " " +Time);

        databaseReference.child("Guardian_list").child(idTxt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("CareReceiverID")){
                    final String getCareReceiver = snapshot.child("CareReceiverID").getValue(String.class);

                    databaseReference.child("CareReceiver_list").child(getCareReceiver).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final String getCareReceiverName = snapshot.child("name").getValue(String.class);
                            final String getCareReceiverPhoneNum = snapshot.child("phoneNum").getValue(String.class);
                            final String getGiverName = snapshot.child("CareGiverName").getValue(String.class);
                            final String getGiverPhoneNum = snapshot.child("CareGiverPhoneNum").getValue(String.class);
                            care_receiver_name.setText(getCareReceiverName);
                            care_receiver_text.setText(getCareReceiverPhoneNum);
                            care_giver_name.setText(getGiverName);
                            care_giver_text.setText(getGiverPhoneNum);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.back.setOnClickListener(v -> {
            Intent intent1 = new Intent(GuardianEventLogDetail.this, GuardianHome.class);
            //set bottom bar
            intent1.putExtra("SELECTED_ITEM", R.id.menu_event);
            startActivity(intent1);
        });

    }
}