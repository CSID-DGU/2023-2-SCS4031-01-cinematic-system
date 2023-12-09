package com.example.fiebasephoneauth.Guardian.page;

import static android.Manifest.permission.SEND_SMS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    DatabaseReference Gaurdian_Ref;
    // 피보호자 정보
    TextView homeCareReceiverName, homeCareReceiverGenderAge, homeCareReceiverAddress;
    CardView home_Outing_cardView, home_Activity_cardView;
    TextView  home_Outing_description, home_Activity_description;
    Button homeSeeDetailButton;

    //전역 변수
    String getName;
    String getOuting;
    String getCareReceiverId;
    String idTxt;

    //fetchAndCompareTime()메소드 작동할 핸들러 생성
    Handler handler1 = new Handler(Looper.getMainLooper());

    //이벤트 페이지로 넘길 ArrayList
    private static final int MAX_SIZE = 4;
    private ArrayList<Map<String,Object>> dataList;

    // 외출, 활동 및 새로운 알림 리사이클러뷰
    private ArrayList<NewNotificationData> items;
    private HomeNewNotificationAdapter Adapter;
    private RecyclerView recyclerViewNewNotification;

    public GuardianMenuHomeFragment(){
    }

    public static GuardianMenuHomeFragment newInstance(){
        return new GuardianMenuHomeFragment();
    }


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

        // Event 페이지로 전달할 List 생성
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
                                    home_Activity_description.setText(getName+"님은 현재 외출 중 입니다.");
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

        // 외출 복귀 후 활동 로그 수정
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
                                nonActivityRecyclerView(time,type);
                                Map<String, Object> map = new HashMap<>();
                                map.put("type",type);
                                map.put("time",time);
                                addData(map);

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

        Gaurdian_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("latestEvent").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String key = snapshot.getKey();
                        String eventType = snapshot.child("type").getValue(String.class);
                        if(!snapshot.hasChild("sms")) {
                            if (ContextCompat.checkSelfPermission(getActivity(), SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                sendSMS(eventType, key);
                            } else {
                                requestPermissions(new String[]{SEND_SMS}, 1);
                            }
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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

    private void addData(Map<String, Object> map){
        dataList.add(map);

        if(dataList.size() > MAX_SIZE){
            dataList.remove(0);
        }
    }

    public ArrayList<Map<String,Object>> getDataList(){
        return dataList;
    }

    /**
     * 활동 로그 1초 마다 업데이트
     * fetchAndCompareTime() 메소드에서 사용하는 Listener를 Child Listener로 변경하면 안써도 될 거 같음 (회의 후 수정)
     */
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
    //화면을 벗어나면 사용한 ArrayList 초기화
    private void clearData(){
        items.clear();
        dataList.clear();
    }

    //Home 화면 RecyclerView 생성
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
    // SMS 전송 메소드
    private void sendSMS(String type, String key){
        databaseReference.child("CareReceiver_list").child(getCareReceiverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //지자체 담당자(DB에 저장된 CareGiver_phone 값)
                String CareGiver_phone = snapshot.child("CareGiverPhoneNum").getValue(String.class);
                //안전센터(119) 시연을 위해 팀원 중 한명의 Phone 번호로
                String safety = "+821088067574";

                // SMS 서비스 연결 및 피보호자 필수 정보
                SmsManager smsManager = SmsManager.getDefault();
                String message = snapshot.child("Address").getValue(String.class) + "\n" + snapshot.child("name").getValue(String.class) + "\n";

                // sms 전송된 이벤트 확인을 위해 boolean 타입의 sms key를 추가함
                databaseReference.child("CareReceiver_list").child(getCareReceiverId)
                        .child("ActivityData").child("latestEvent").child(key).child("sms").setValue(true);

                // 응급 상황 type 별 메세지 description 발송
                switch (type){
                    case "fire":
                        smsManager.sendTextMessage(CareGiver_phone,null,message+"화재가 발생했습니다.",null,null);
                        smsManager.sendTextMessage(safety,null,message+"화재가 발생했습니다.",null,null);
                        break;
                    case "emergency":
                        smsManager.sendTextMessage(CareGiver_phone,null,message + "응급 상황 발생",null,null);
                        smsManager.sendTextMessage(safety,null,message + "응급 상황 발생",null,null);
                        break;
                    case "no_movement_detected_1":
                        smsManager.sendTextMessage(CareGiver_phone,null,message + "12시간 이상 움직임이 없습니다.",null,null);
                        smsManager.sendTextMessage(safety,null,message + "12시간 이상 움직임이 없습니다.",null,null);
                        break;
                    case "no_movement_detected_2":
                        smsManager.sendTextMessage(CareGiver_phone,null,message + "24시간 이상 움직임이 없습니다.",null,null);
                        smsManager.sendTextMessage(safety,null,message + "24시간 이상 움직임이 없습니다.",null,null);
                        break;

                }


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