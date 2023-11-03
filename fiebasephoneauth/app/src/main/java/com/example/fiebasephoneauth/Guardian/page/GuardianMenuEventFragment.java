package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fiebasephoneauth.R;

/**
 * <h3> 보호자 이벤트 로그 페이지 </h3>
 *
 * 보호자가 피보호자의 이벤트 로그를 확인할 수 있는 페이지
 */
public class GuardianMenuEventFragment extends Fragment {


    // 더미 데이터 (테스트용) -------------------------
    EventCardInfo[] eventCardInfo = {
        new EventCardInfo("2020-11-11", "12:00","fire"),
        new EventCardInfo("2020-11-11", "12:00","fire"),
    };
    // ---------------------------------------------

    RecyclerView recyclerViewEventLog;
    RecyclerView.Adapter eventLogAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardian_menu_event, container, false);

        recyclerViewEventLog = (RecyclerView) view.findViewById(R.id.recent_notification_recycler_view);
        recyclerViewEventLog.setHasFixedSize(true);
        eventLogAdapter = new HomeEventLogAdapter(eventCardInfo);
        recyclerViewEventLog.setAdapter(eventLogAdapter);
        recyclerViewEventLog.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}


class EventCardInfo {
    String date;
    String time;
    String title;
    String description_short;
    String description_long;
    String emergencyType;
    String imgSrc;

    public EventCardInfo(String date, String time, String emerCode) {
        this.date = date;
        this.time = time;
        emergencyType = emerCode;

        switch(emerCode){
            case "fire" :
                title = "화재 발생";
                description_short = "화재가 감지되어 자동신고 처리되었습니다.";
                description_long = "화재 감지기를 통해 화재 상황이 감지되어 \n" +
                        "담당자 전달 및 자동 신고 처리되었습니다. \n" +
                        "아래 연락처를 통해 상황을 파악하시길 바랍니다.";
                imgSrc = "fire";
                break;
            case "emergency" :
                title = "응급 버튼 눌림";
                description_short = "응급 버튼이 눌려 자동 신고 처리되었습니다.";
                description_long = "응급 버튼이 눌렀습니다.\n" +
                        "담당자에게 응급 상황이 전달되었으며,\n" +
                        "아래 연락처를 통해 상황을 파악하시길 바랍니다.";
                imgSrc = "emergency";
                break;
            case "no_movement_detected_1" :
                title = "장기 미활동 감지";
                description_short = "12시간 내의 활동이 감지되지 않았어요!";
                description_long = "12시간 동안 활동이 감지되지 않았습니다. \n" +
                        "아래 번호에 연락하여 안부를 여쭈어보세요.";
                imgSrc = "no_movement";
                break;
            case "no_movement_detected_2" :
                title = "장기 미활동 신고";
                description_short = "장기 미활동 판단으로 신고처리되었습니다";
                description_long = "24시간 활동이 감지되지 않아,\n" +
                        "자동신고 접수가 되었습니다. ";
                imgSrc = "no_movement";
                break;
        }

    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription_short() {
        return description_short;
    }

    public String getDescription_long() {
        return description_long;
    }

    public String getEmergencyType() {
        return emergencyType;
    }

    public String getImgSrc() {
        return imgSrc;
    }

}