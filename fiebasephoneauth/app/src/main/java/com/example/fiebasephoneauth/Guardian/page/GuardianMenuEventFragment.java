package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * <h3> 보호자 이벤트 로그 페이지 </h3>
 *
 * 보호자가 피보호자의 이벤트 로그를 확인할 수 있는 페이지
 */
public class GuardianMenuEventFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    String getCareReceiverId;
    private ArrayList<Map<String, Object>> receivedDataList = new ArrayList<>();
    private String receivedId;
    private ArrayList<EventCardInfo> Event_dataList = new ArrayList<>();
    private HomeEventLogAdapter Event_adapter;
    private RecyclerView recyclerViewEventLog;

    public static GuardianMenuEventFragment newInstance(String idTxt, ArrayList<Map<String, Object>> dataList) {
        GuardianMenuEventFragment fragment = new GuardianMenuEventFragment();
        Bundle args = new Bundle();
        args.putString("id",idTxt);
        args.putSerializable("data_list",dataList);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        receivedId = bundle.getString("id");

        if(getArguments() != null && bundle.containsKey("data_list")){
            receivedDataList = (ArrayList<Map<String, Object>>) getArguments().getSerializable("data_list");

            if(receivedDataList != null){
                for(Map<String, Object> data : receivedDataList){
                    long timeValue = (long) data.get("time");
                    String typeValue = (String) data.get("type");

                    if(!typeValue.equals("outing")) {
                        newRecyclerView(timeValue, typeValue);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardian_menu_event, container, false);

        recyclerViewEventLog = (RecyclerView) view.findViewById(R.id.recent_notification_recycler_view);
        recyclerViewEventLog.setHasFixedSize(true);
        recyclerViewEventLog.setLayoutManager(new LinearLayoutManager(getActivity()));
        Event_adapter = new HomeEventLogAdapter(Event_dataList, receivedId);
        recyclerViewEventLog.setAdapter(Event_adapter);


        return view;
    }


    private void newRecyclerView(long time, String status){
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH시 mm분 ss초");

        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        EventCardInfo Data = new EventCardInfo(formattedDate, formattedTime, status);
        Event_dataList.add(0,Data);
        if (Event_dataList.size() > 8) {
            Event_dataList.remove(Event_dataList.size() - 1);
        }

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
                title = "응급 호출";
                description_short = "응급 버튼이 눌려 자동 신고 처리되었습니다.";
                description_long = "응급 버튼이 눌렀습니다.\n" +
                        "담당자에게 응급 상황이 전달되었으며,\n" +
                        "아래 연락처를 통해 상황을 파악하시길 바랍니다.";
                imgSrc = "emer";
                break;
            case "no_movement_detected_1" :
                title = "장기 미활동 감지";
                description_short = "12시간 내의 활동이 감지되지 않았어요!";
                description_long = "12시간 동안 활동이 감지되지 않았습니다. \n" +
                        "아래 번호에 연락하여 안부를 여쭈어보세요.";
                imgSrc = "secu1";
                break;
            case "no_movement_detected_2" :
                title = "장기 미활동 신고";
                description_short = "장기 미활동 판단으로 신고처리되었습니다";
                description_long = "24시간 활동이 감지되지 않아,\n" +
                        "자동신고 접수가 되었습니다. ";
                imgSrc = "secu2";
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