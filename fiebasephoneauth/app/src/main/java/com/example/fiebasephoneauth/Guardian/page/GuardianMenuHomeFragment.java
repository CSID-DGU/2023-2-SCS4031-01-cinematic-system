package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;

/**
 * <h3> Home Tap Fragment </h3>
 *
 *
 * activityData 및 newNotificationData에 피보호자의 데이터를 입력하면
 */
public class GuardianMenuHomeFragment extends Fragment {

    // 피보호자 정보
    TextView homeCareReceiverName, homeCareReceiverGenderAge, homeCareReceiverAddress;
    Button homeSeeDetailButton;



    // 외출, 활동 및 새로운 알림 리사이클러뷰
    private RecyclerView recyclerViewMain, recyclerViewNewNotification;
    private RecyclerView.Adapter mainViewAdapter, newNotificationAdapter;

    // 더미 데이터 -----------------------------------
    // 데이터를 받아올 떄 CareReceiverActivityData[] activityData = { ... },
    // NewNotificationData[] newNotificationData = { ... } 부분을 수정하면 됨
    CareReceiverActivityData activityData[] = {
            new CareReceiverActivityData("외출", "김보호님은 현재 외출중입니다"),
            new CareReceiverActivityData("활동", "최근 24시간 동안 5번의 활동 감지가 있었습니다")
    };

    NewNotificationData newNotificationData[] = {
        new NewNotificationData("2020-05-01", "12:00:00", "김보호님이 외출을 시작하였습니다"),
        new NewNotificationData("2020-05-01", "12:00:00", "김보호님이 외출을 시작하였습니다"),
        new NewNotificationData("2020-05-01", "12:00:00", "김보호님이 외출을 시작하였습니다"),
    };
    // ----------------------------------------------

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

        // 활동 정보 리사이클러뷰
        recyclerViewMain = (RecyclerView) view.findViewById(R.id.recyclerview_home_main);
        recyclerViewMain.setHasFixedSize(true);
        mainViewAdapter = new HomeMainAdapter(activityData);
        recyclerViewMain.setAdapter(mainViewAdapter);
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 새로운 알림 리사이클러뷰
        recyclerViewNewNotification = (RecyclerView) view.findViewById(R.id.recyclerview_home_new_notification);
        recyclerViewNewNotification.setHasFixedSize(true);
        newNotificationAdapter = new HomeNewNotificationAdapter(newNotificationData);
        recyclerViewNewNotification.setAdapter(newNotificationAdapter);
        recyclerViewNewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;

    }
}

class CareReceiverActivityData {
    private String title;
    private String description;

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