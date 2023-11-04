package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fiebasephoneauth.R;

/**
 * <h3> 내 정보 페이지 </h3>
 *
 * 로그아웃 및 정보 수정이 가능하도록 함
 */
public class GuardianMenuProfileFragment extends Fragment {

    TextView guardianName, guardianPhone, careReceiverName, careReceiverPhone, careReceiverAddress, careGiverName, careGiverPhone;
    TextView settingBtn;
    Button signOutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guardian_menu_profile, container, false);

        guardianName = (TextView) view.findViewById(R.id.guardian_name);
        guardianPhone = (TextView) view.findViewById(R.id.guardian_phone);
        careReceiverName = (TextView) view.findViewById(R.id.care_receiver_name);
        careReceiverPhone = (TextView) view.findViewById(R.id.care_receiver_phone);
        careReceiverAddress = (TextView) view.findViewById(R.id.care_receiver_address);
        careGiverName = (TextView) view.findViewById(R.id.care_giver_name);
        careGiverPhone = (TextView) view.findViewById(R.id.care_giver_phone);
        settingBtn = (TextView) view.findViewById(R.id.setting_button);
        signOutBtn = (Button) view.findViewById(R.id.sign_out_button);


        return view;
    }
}