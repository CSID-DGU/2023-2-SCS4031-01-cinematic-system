package com.example.fiebasephoneauth.Guardian.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <h3> 보호자의 홈 메인 페이지 </h3>
 *
 *
 * activityData 및 newNotificationData에 피보호자의 데이터를 입력하면
 */
public class GuardianMenuHomeFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");
    DatabaseReference docRef;
    DatabaseReference Gaurdian_Ref;
    // 피보호자 정보
    TextView homeCareReceiverName, homeCareReceiverGenderAge, homeCareReceiverAddress;
    CardView home_Outing_cardView, home_Activity_cardView;
    TextView  home_Outing_description, home_Activity_description;
    Button homeSeeDetailButton;
    String getName;
    String getOuting;
    String getActivity = "정상 입니다.";
    String getCareReceiverId;
    long time;
    String status;

    boolean isHandlerRunning = false;
    Handler handler = new Handler();
    Handler handler1 = new Handler(Looper.getMainLooper());


    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    String current_Date = dateFormat.format(calendar.getTime());
    String current_Time = timeFormat.format(calendar.getTime());

    private static final int MAX_SIZE = 4;
    private ArrayList<String> dataList;
    String idTxt;

    public GuardianMenuHomeFragment(){
    }

    public static GuardianMenuHomeFragment newInstance(){
        return new GuardianMenuHomeFragment();
    }

    // 외출, 활동 및 새로운 알림 리사이클러뷰
    private ArrayList<NewNotificationData> items;
    private HomeNewNotificationAdapter Adapter;
    private RecyclerView recyclerViewNewNotification;



    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardian_menu_home, container, false);

        // 피보호자 정보
        homeCareReceiverName = (TextView) view.findViewById(R.id.care_receiver_name); // 피보호자 이름
        homeCareReceiverGenderAge = (TextView) view.findViewById(R.id.care_receiver_gender_age); // 피보호자 성별, 나이
        homeCareReceiverAddress = (TextView) view.findViewById(R.id.care_receiver_address); // 피보호자 주소
        homeSeeDetailButton = (Button) view.findViewById(R.id.home_see_detail_button); // 피보호자 상세 정보 보기 버튼

        // 외출, 활동
        home_Outing_cardView = (CardView) view.findViewById(R.id.home_outing_card); // 외출 카드뷰
        home_Activity_cardView = (CardView) view.findViewById(R.id.home_activity_card); // 활동 카드뷰
        home_Outing_description = (TextView) view.findViewById(R.id.home_outing_description); // 외출 설명
        home_Activity_description = (TextView) view.findViewById(R.id.home_activity_description); // 활동 설명

        // 새로운 알림 리사이클러뷰
        recyclerViewNewNotification = (RecyclerView) view.findViewById(R.id.recyclerview_home_new_notification); // 새로운 알림 리사이클러뷰
        recyclerViewNewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        items = new ArrayList<>();
        Adapter = new HomeNewNotificationAdapter(items);
        recyclerViewNewNotification.setAdapter(Adapter);


        dataList = new ArrayList<>();
        //로그인 한 보호자 정보
        Bundle bundle = getArguments();
        idTxt = bundle.getString("id");

        Gaurdian_Ref = databaseReference.child("Guardian_list").child(idTxt);

        // 피보호자 정보 조회
        Gaurdian_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("CareReceiverID")){
                    getCareReceiverId = snapshot.child("CareReceiverID").getValue(String.class);

                    docRef = databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("activity");

                    databaseReference.child("CareReceiver_list").child(getCareReceiverId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            getName = snapshot.child("name").getValue(String.class);
                            homeCareReceiverName.setText(getName);

                            String getCareReceiverAge = snapshot.child("Age").getValue(String.class);
                            String getCareReceiverGender = snapshot.child("gender").getValue(String.class);

                            homeCareReceiverGenderAge.setText(getCareReceiverAge+"/"+getCareReceiverGender);

                            String getCareReceiverAddress = snapshot.child("Address").getValue(String.class);
                            homeCareReceiverAddress.setText(getCareReceiverAddress);


                            if(snapshot.hasChild("ActivityData")){
                                getOuting = snapshot.child("ActivityData").child("door").child("outing").getValue(String.class);
                                if (getOuting.equals("1")){
                                    home_Outing_description.setText(getName+"님은 현재 외출 중 입니다.");
                                    //docRef.child("time").removeValue();
                                    //home_Activity_description.setText(getName+"님은 현재 외출 중 입니다.");

                                }
                                else if (getOuting.equals("0")){
                                    home_Outing_description.setText(getName+"님은 현재 실내에 있습니다.");
                                }
                                else{
                                    home_Outing_description.setText(getName+"님 반갑습니다.");
                                }
                            }


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
        Gaurdian_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("door")
                        .child("outing").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String outingValue = snapshot.getValue(String.class);
                                if(outingValue.equals("0")){
                                    home_Activity_description.setText("정상 입니다.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //latestEvent를 사용해 RecyclerView 생성
        Gaurdian_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("latestEvent").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            clearData();
                            for(DataSnapshot table : snapshot.getChildren()){
                                long time = table.child("time").getValue(Long.class);
                                String type = table.child("type").getValue(String.class);
                                addData(type);
                                nonActivityRecyclerView(time,type);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 활동 조회
        Gaurdian_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                docRef = databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("activity");

                databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("activity")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if("cnt".equals(snapshot.getKey())){
                                    //updateLastActivityTime();
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        startHandler();
        startHandler_log();

        homeSeeDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuardianSeeCareReceiversDetail.class);
                intent.putExtra("id", idTxt);
                intent.putExtra("receiverId", getCareReceiverId);
                startActivity(intent);
            }
        });

        //활동 정보 상세보기
        home_Activity_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuardianActivitiesDetail.class);
                intent.putExtra("id", idTxt);
                intent.putExtra("receiverId", getCareReceiverId);
                startActivity(intent);
            }
        });



        return view;
    }

    private void addData(String data){
        dataList.add(data);

        if(dataList.size() > MAX_SIZE){
            dataList.remove(0);
        }
    }

    public ArrayList<String> getDataList(){
        return dataList;
    }
    private void startHandler_log(){
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchAndCompareTime();

                startHandler_log();
            }
        }, 1000);
    }
    private void fetchAndCompareTime(){
        docRef.child("ACTIVITY_CODE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Long activityCode = snapshot.getValue(Long.class);

                    if(activityCode == 0){
                        home_Activity_description.setText("정상 입니다.");
                    }
                    //주의
                    else if(activityCode == 1){
                        home_Activity_description.setText("8시간 동안 활동이 없습니다.");
                    }
                    //경고
                    else if(activityCode == 2){
                        home_Activity_description.setText("12시간 동안 활동이 없습니다.");
                    }
                    //응급
                    else if(activityCode == 3 || activityCode == 4){
                        home_Activity_description.setText("24시간 동안 활동이 없습니다.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private long lastWarningTime = 0;  // 최초의 경고 범위에 들어온 시간을 저장
    private long lastEmergencyTime = 0;  // 최초의 응급 범위에 들어온 시간을 저장

    private void startHandler(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                compareTimeAndPerformAction();

                startHandler();
            }
            }, 1000);
    }

    private void clearData(){
        items.clear();
        dataList.clear();
    }

    private void nonActivityRecyclerView(long time, String status){
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        NewNotificationData Data = new NewNotificationData(formattedDate, formattedTime, status);
        items.add(0,Data);
        if (items.size() > 4) {
            items.remove(items.size() - 1);
        }
        Adapter.notifyDataSetChanged();
    }
    private void newRecyclerView(String status){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        NewNotificationData Data = new NewNotificationData(currentDate, currentTime, status);
        items.add(0,Data);
        if (items.size() > 4) {
            items.remove(items.size() - 1);
        }
        Adapter.notifyDataSetChanged();
    }

    private void updateLastActivityTime() {
        docRef.child("time").setValue(ServerValue.TIMESTAMP, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
    }


    private void compareTimeAndPerformAction() {
        docRef.child("ACTIVITY_CODE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Long activityCode = snapshot.getValue(Long.class);
                    long currentTime = System.currentTimeMillis();


                    // 경고
                    if (activityCode == 2) {
                        if (lastWarningTime == 0) {
                            // 최초의 경고 범위에 들어온 경우
                            lastWarningTime = currentTime;
                            getActivity = "no_movement_detected_1";
                            //updateLatestEvent(currentTime, getActivity);
                        }
                    }
                    // 응급
                    else if (activityCode == 3) {
                        if (lastEmergencyTime == 0) {
                            // 최초의 응급 범위에 들어온 경우
                            lastEmergencyTime = currentTime;
                            getActivity = "no_movement_detected_2";
                            //updateLatestEvent(currentTime, getActivity);
                        }
                    } else if (activityCode == 4) {
                        // 범위를 벗어난 경우
                        lastWarningTime = 0;
                        lastEmergencyTime = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateLatestEvent(long time, String type){
        Gaurdian_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference ref = databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("latestEvent");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String,Object> latestEvent = (Map<String, Object>) snapshot.getValue();
                        if (latestEvent != null) {
                            int numChildren = latestEvent.size();

                            if(numChildren >= 4){
                                List<String> keys = new ArrayList<>(latestEvent.keySet());
                                Collections.sort(keys);

                                String oldestKey = keys.get(0);
                                ref.child(oldestKey).removeValue();
                            }
                        }
                        Map<String, Object> newData = new HashMap<>();
                        newData.put("time",time);
                        newData.put("type",type);
                        ref.push().updateChildren(newData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

class NewNotificationData {

    String date;
    String time;
    String description;
    String type;

    public NewNotificationData(String date ,String time, String typeCode) {
        this.date = date;
        this.time = time;
        type = typeCode;

        switch (typeCode){
            case "fire":
                description = "화재가 발생했습니다";
                break;
            case "outing":
                description = "외출을 시작했습니다";
                break;
            case "emergency":
                description = "응급 호출";
                break;
            case "no_movement_detected_1":
                description = "12시간 이상 활동이 없습니다";
                break;
            case "no_movement_detected_2":
                description = "24시간 이상 활동이 없습니다";
                break;
        }
    }

    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getDescription() {
        return description;
    }
}