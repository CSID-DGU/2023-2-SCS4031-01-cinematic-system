package com.example.fiebasephoneauth.Guardian.page;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <h3> 보호자의 홈 메인 페이지 </h3>
 *
 *
 * activityData 및 newNotificationData에 피보호자의 데이터를 입력하면
 */
public class GuardianMenuHomeFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");
    DatabaseReference docRef;
    // 피보호자 정보
    TextView homeCareReceiverName, homeCareReceiverGenderAge, homeCareReceiverAddress;
    CardView home_Outing_cardView, home_Activity_cardView;
    TextView  home_Outing_description, home_Activity_description;
    Button homeSeeDetailButton;
    String getName;
    String getOuting;
    String getActivity_cnt;
    String getCareReceiverId;


    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    String current_Date = dateFormat.format(calendar.getTime());
    String current_Time = timeFormat.format(calendar.getTime());




    // 외출, 활동 및 새로운 알림 리사이클러뷰
    private ArrayList<NewNotificationData> Main_dataList;
    private HomeNewNotificationAdapter Main_adapter;
    private RecyclerView recyclerViewNewNotification;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Main_dataList = new ArrayList<>();
        Main_adapter = new HomeNewNotificationAdapter(Main_dataList);
        recyclerViewNewNotification.setAdapter(Main_adapter);


        //로그인 한 보호자 정보
        Bundle bundle = getArguments();
        String idTxt = bundle.getString("id");

        // 피보호자 정보 조회
        databaseReference.child("Guardian_list").child(idTxt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("CareReceiverID")){
                    final String getCareReceiver = snapshot.child("CareReceiverID").getValue(String.class);

                    databaseReference.child("CareReceiver_list").child(getCareReceiver).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            getName = snapshot.child("name").getValue(String.class);
                            homeCareReceiverName.setText(getName);

//                            String getCareReceiverAge = snapshot.child(getCareReceiverId).child("Age").getValue(String.class);
//                            String getCareReceiverSex = snapshot.child(getCareReceiverId).child("Sex").getValue(String.class);

//                            homeCareReceiverGenderAge.setText(getCareReceiverAge+"/"+getCareReceiverSex);

//                            String getCareReceiverAddress = snapshot.child(getCareReceiverId).child("Address").getValue(String.class);
//                            homeCareReceiverAddress.setText(getCareReceiverAddress);
                            homeCareReceiverGenderAge.setText("남/72");
                            homeCareReceiverAddress.setText("서울 중구 필동로1길 30");
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

        // 외출, 활동 조회
        databaseReference.child("Guardian_list").child(idTxt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("CareReceiverID")){
                    getCareReceiverId = snapshot.child("CareReceiverID").getValue(String.class);

                    databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getKey().matches("outing")){
                                if (snapshot.getValue().equals("0")){
                                    home_Outing_description.setText(getName+"님은 현재 외출 중 입니다.");

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                    String currentDate = dateFormat.format(calendar.getTime());
                                    String currentTime = timeFormat.format(calendar.getTime());

                                    NewNotificationData Data = new NewNotificationData(currentDate, currentTime, getName + "님이 외출을 시작하였습니다.");
                                    Main_dataList.add(0,Data);
                                    if (Main_dataList.size() > 4) {
                                        Main_dataList.remove(Main_dataList.size() - 1);
                                    }

                                }
                                else if (snapshot.getValue().equals("1")){
                                    home_Outing_description.setText(getName+"님은 현재 실내에 있습니다.");

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                    String currentDate = dateFormat.format(calendar.getTime());
                                    String currentTime = timeFormat.format(calendar.getTime());

                                    NewNotificationData Data = new NewNotificationData(currentDate, currentTime,getName+"님이 외출을 마쳤습니다.");
                                    Main_dataList.add(0,Data);
                                    if (Main_dataList.size() > 4) {
                                        Main_dataList.remove(Main_dataList.size() - 1);
                                    }
                                }
                            }
                            else if(snapshot.getKey().matches("emergency")){
                                if (snapshot.getValue().equals("1")) {

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                    String currentDate = dateFormat.format(calendar.getTime());
                                    String currentTime = timeFormat.format(calendar.getTime());

                                    NewNotificationData Data = new NewNotificationData(currentDate, currentTime, getName + "님이 응급버튼을 눌렀습니다.");
                                    Main_dataList.add(0, Data);
                                    if (Main_dataList.size() > 4) {
                                        Main_dataList.remove(Main_dataList.size() - 1);
                                    }
                                }
                            }
                            else if(snapshot.getKey().matches("fire")){

                                if (snapshot.getValue().equals("1")) {

                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                    String currentDate = dateFormat.format(calendar.getTime());
                                    String currentTime = timeFormat.format(calendar.getTime());

                                    NewNotificationData Data = new NewNotificationData(currentDate, currentTime, getName + "님이 화재가 발생했습니다.");
                                    Main_dataList.add(0, Data);
                                    if (Main_dataList.size() > 4) {
                                        Main_dataList.remove(Main_dataList.size() - 1);
                                    }
                                }
                            }
                            Main_adapter.notifyDataSetChanged();
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
                    docRef = databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("activity");
                    docRef.child("cnt").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            updateLastActivityTime();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            compareTimeAndPerformAction();
                            handler.postDelayed(this,4000);
                        }
                    },4000);

                    docRef.child("cnt").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            updateLastActivityTime();
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

        homeSeeDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 외출, 활동 상세 정보 보기
        home_Outing_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuardianActivitiesDetail.class);
                intent.putExtra("id", idTxt);
                startActivity(intent);
            }
        });

        home_Activity_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuardianActivitiesDetail.class);
                intent.putExtra("id", idTxt);
                startActivity(intent);
            }
        });


        return view;
    }

    private void updateLastActivityTime() {
        Map<String,Object> updateData = new HashMap<>();
        updateData.put("time", ServerValue.TIMESTAMP);

        docRef.updateChildren(updateData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    Log.e(TAG, "not be saved"+ error.getMessage());
                }
                else{
                    Log.e(TAG, "saved success");
                }
            }
        });
    }

    private void compareTimeAndPerformAction() {
        docRef.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long lastActivityTime = snapshot.getValue(Long.class);
                    long currentTime = System.currentTimeMillis();

                    long timeDifference = currentTime - lastActivityTime;

                    long hoursDifference = timeDifference / 1000;

                    if(hoursDifference > 0 &&hoursDifference < 8){
                        home_Activity_description.setText("정상 상태입니다.");
                    }
                    //주의
                    else if (hoursDifference >= 8 && hoursDifference < 12) {
                        home_Activity_description.setText("주의 상태입니다.");
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String currentDate = dateFormat.format(calendar.getTime());
                        String ServerTime = timeFormat.format(calendar.getTime());

                        NewNotificationData Data = new NewNotificationData(currentDate, ServerTime, getName + "님이 8시간 이상 활동이 없습니다.");
                        Main_dataList.add(0, Data);
                        if (Main_dataList.size() > 4) {
                            Main_dataList.remove(Main_dataList.size() - 1);
                        }
                    }
                    //경고
                    else if(hoursDifference >= 12 && hoursDifference < 24){
                        home_Activity_description.setText("경고 상태입니다.");
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String currentDate = dateFormat.format(calendar.getTime());
                        String ServerTime = timeFormat.format(calendar.getTime());

                        NewNotificationData Data = new NewNotificationData(currentDate, ServerTime, getName + "님이 12시간 이상 활동이 없습니다.");
                        Main_dataList.add(0, Data);
                        if (Main_dataList.size() > 4) {
                            Main_dataList.remove(Main_dataList.size() - 1);
                        }
                    }
                    //응급
                    else if (hoursDifference >= 24) {
                        home_Activity_description.setText("응급 상태입니다.");
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String currentDate = dateFormat.format(calendar.getTime());
                        String ServerTime = timeFormat.format(calendar.getTime());

                        NewNotificationData Data = new NewNotificationData(currentDate, ServerTime, getName + "님이 24시간 이상 활동이 없습니다.");
                        Main_dataList.add(0, Data);
                        if (Main_dataList.size() > 4) {
                            Main_dataList.remove(Main_dataList.size() - 1);
                        }

                    }Main_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkLastActivityTimestamp(DataSnapshot snapshot) {
        Object timestamp = snapshot.getValue();

        if(timestamp instanceof Long){
            Date lastActivityTime = new Date((Long)timestamp);

            Date ServerTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long timeDifference = ServerTime.getTime() - lastActivityTime.getTime();

            long hoursDifference = timeDifference /(60*60*1000);

            //정상

        }
    }
}

class NewNotificationData {

    private String date;
    private String time;
    private String description;

    public NewNotificationData(String date ,String time, String description) {
        this.date = date;
        this.time = time;
        this.description = description;
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