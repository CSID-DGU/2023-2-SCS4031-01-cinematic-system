package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * <h3> 보호자 이벤트 로그 페이지 </h3>
 *
 * 보호자가 피보호자의 이벤트 로그를 확인할 수 있는 페이지
 */
public class GuardianMenuEventFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");

    String getCareReceiverId;

    private ArrayList<Object> Event_dataList = new ArrayList<>();
    private MultiViewAdapter Event_adapter;
    private RecyclerView recyclerViewEventLog;

    public void onAttachFragment(Fragment GuardianMenuEventFragment){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardian_menu_event, container, false);
        Bundle bundle = getArguments();
        String idTxt = bundle.getString("id");

        recyclerViewEventLog = (RecyclerView) view.findViewById(R.id.recent_notification_recycler_view);
        recyclerViewEventLog.setHasFixedSize(true);
        recyclerViewEventLog.setLayoutManager(new LinearLayoutManager(getActivity()));
        Event_adapter = new MultiViewAdapter(Event_dataList, idTxt);
        recyclerViewEventLog.setAdapter(Event_adapter);


        databaseReference.child("Guardian_list").child(idTxt).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            if(snapshot.getKey().matches("emergency")){
                                if (snapshot.getValue().equals("1")) {
                                    String status = "emergency";

                                    newRecyclerView(status);
                                }
                            }
                            else if(snapshot.getKey().matches("fire")){

                                if (snapshot.getValue().equals("1")) {
                                    String status = "fire";
                                    newRecyclerView(status);
                                }
                            }
                            Event_adapter.notifyDataSetChanged();
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


    private void newRecyclerView(String status){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentDate = dateFormat.format(calendar.getTime());
        String currentTime = timeFormat.format(calendar.getTime());

        EventCardInfo Data = new EventCardInfo(currentDate, currentTime, status);
        Event_dataList.add(0,Data);
        if (Event_dataList.size() > 4) {
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