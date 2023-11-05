package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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




    // 외출, 활동 및 새로운 알림 리사이클러뷰
    private RecyclerView recyclerViewMain, recyclerViewNewNotification;
    private RecyclerView.Adapter mainViewAdapter, newNotificationAdapter;


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


        home_Outing_description = (TextView) view.findViewById(R.id.home_Outing_description);
        home_Activity_description = (TextView) view.findViewById(R.id.home_Activity_description);

        // 활동 정보 리사이클러뷰
//        recyclerViewMain = (RecyclerView) view.findViewById(R.id.recyclerview_home_main);
//        recyclerViewMain.setHasFixedSize(true);
//        mainViewAdapter = new HomeMainAdapter(activityData);
//        recyclerViewMain.setAdapter(mainViewAdapter);
//        recyclerViewMain.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 새로운 알림 리사이클러뷰
        recyclerViewNewNotification = (RecyclerView) view.findViewById(R.id.recyclerview_home_new_notification);
        recyclerViewNewNotification.setHasFixedSize(true);
        newNotificationAdapter = new HomeNewNotificationAdapter(newNotificationData);
        recyclerViewNewNotification.setAdapter(newNotificationAdapter);
        recyclerViewNewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        String idTxt = bundle.getString("id");
        databaseReference.child("CareReceiver_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(idTxt)){
                    getName = snapshot.child(idTxt).child("name").getValue(String.class);
                    getOuting = snapshot.child(idTxt).child("ActivityData").child("외출").getValue(String.class);
                    homeCareReceiverName.setText(getName);
                    homeCareReceiverGenderAge.setText("남/72");
                    homeCareReceiverAddress.setText("서울 중구 필동로1길 30");
                    /**
                     *
                     */
                    if ("0".equals(getOuting)) {
                        // "외출" DB에 저장된 값이 "0"이면 -> 외출 중
                        home_Outing_description.setText(getName + "님은 현재 외출 중 입니다.");
                    }
                    else if ("1".equals(getOuting)) {
                        // "외출" DB에 저장된 값이 "1"이면 -> 외출 X
                        home_Outing_description.setText(getName + "님은 현재 실내에 있습니다.");
                    }



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }
    // 더미 데이터 -----------------------------------
    // 데이터를 받아올 떄 CareReceiverActivityData[] activityData = { ... },
    // NewNotificationData[] newNotificationData = { ... } 부분을 수정하면 됨
    CareReceiverActivityData activityData[] = {
            new CareReceiverActivityData("외출", "님은 현재 외출중입니다."),
            new CareReceiverActivityData("활동", "최근 24시간 동안 5번의 활동 감지가 있었습니다")
    };

    NewNotificationData newNotificationData[] = {
            new NewNotificationData("2020-05-01", "12:00:00", getName+"님이 외출을 시작하였습니다."),
            new NewNotificationData("2020-05-01", "12:00:00", getName+"님이 외출을 시작하였습니다."),
            new NewNotificationData("2020-05-01", "12:00:00", getName+"님이 외출을 시작하였습니다."),
    };
    // ----------------------------------------------
}

class CareReceiverActivityData {
    String title;
    String description;

    public CareReceiverActivityData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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