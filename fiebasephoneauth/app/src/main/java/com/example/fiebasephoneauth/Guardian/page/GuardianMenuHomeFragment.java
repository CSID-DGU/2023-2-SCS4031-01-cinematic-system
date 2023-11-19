package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Calendar;

/**
 * <h3> 보호자의 홈 메인 페이지 </h3>
 *
 *
 * activityData 및 newNotificationData에 피보호자의 데이터를 입력하면
 */
public class GuardianMenuHomeFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    // 피보호자 정보
    TextView homeCareReceiverName, homeCareReceiverGenderAge, homeCareReceiverAddress;
    TextView  home_Outing_description, home_Activity_description;
    Button homeSeeDetailButton;
    String getName;
    String getOuting;
    String getActivity_cnt;




    // 외출, 활동 및 새로운 알림 리사이클러뷰
    private ArrayList<NewNotificationData> Main_dataList;
    private HomeNewNotificationAdapter Main_adapter;
    private RecyclerView recyclerViewNewNotification;
    private LinearLayoutManager linearLayoutManager;


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


        home_Outing_description = (TextView) view.findViewById(R.id.home_outing_description);
        home_Activity_description = (TextView) view.findViewById(R.id.home_activity_description);

        // 새로운 알림 리사이클러뷰
        recyclerViewNewNotification = (RecyclerView) view.findViewById(R.id.recyclerview_home_new_notification);
        recyclerViewNewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        Main_dataList = new ArrayList<>();
        Main_adapter = new HomeNewNotificationAdapter(Main_dataList);
        recyclerViewNewNotification.setAdapter(Main_adapter);


        //로그인 한 보호자 정보
        Bundle bundle = getArguments();
        String idTxt = bundle.getString("id");


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


        databaseReference.child("Guardian_list").child(idTxt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("CareReceiverID")){
                    String getCareReceiverId = snapshot.child("CareReceiverID").getValue(String.class);

                    databaseReference.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getKey().matches("외출")){
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
                            else if(snapshot.getKey().matches("응급")){
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
                            else if(snapshot.getKey().matches("화재")){

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
                            else if(snapshot.getKey().matches("활동")){
                                getActivity_cnt = snapshot.getValue().toString();
                                home_Activity_description.setText("최근 24시간 동안 "+getActivity_cnt+"번의 활동 감지가 있었습니다");

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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

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