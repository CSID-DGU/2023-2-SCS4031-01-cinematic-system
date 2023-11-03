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

    EventCardInfo[] eventCardInfo;

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
    String description;
    String EmergencyType;

    public EventCardInfo(String date, String time, String description, String EmergencyType) {
        this.date = date;
        this.time = time;
        this.EmergencyType = EmergencyType;
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