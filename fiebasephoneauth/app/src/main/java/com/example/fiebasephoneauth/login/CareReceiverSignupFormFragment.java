package com.example.fiebasephoneauth.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fiebasephoneauth.CareReceiverInfo;
import com.example.fiebasephoneauth.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>피보호자 회원가입 입력 폼</h3>
 *
 * 회원가입에서 피보호자 버튼을 클릭했을 때 보여지는 입력 폼
 */
public class CareReceiverSignupFormFragment extends Fragment implements View.OnClickListener{
    /**
     * Firebase DB에서 테이블 값 호출 메소드
     */




    /**
     * jsm512
     * Firebase DB 피보호자 정보 저장
     */
    private DatabaseReference mPostreference;
    private FirebaseDatabase mFirebaseDatabase;


    TextView userInfo;
    TextView nameText;
    TextView phoneNumText;
    TextView care_giver_Info;
    TextView careGiverNameText;
    TextView careGiverphoneNumText;
    TextView accountInfo;
    TextView idText;
    TextView pwText;
    TextView pwConfirmText;

    EditText nameForm;
    EditText phoneNumForm;
    EditText careGiverNameForm;
    EditText careGiverPhoneNumForm;
    EditText idForm;
    EditText passwordForm;
    EditText passwordConfirmForm;

    Button signup_button;

    String name;
    long phoneNum;
    String careGiverName;
    long careGiverPhoneNum;
    String ID;
    long password;
    long passwordConfirm;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_care_receiver_signup_form, container, false);

        signup_button = root.findViewById(R.id.signup_button);
        signup_button.setOnClickListener(this);
        userInfo = root.findViewById(R.id.userInfoText);
        nameText = root.findViewById(R.id.nameText);
        phoneNumText = root.findViewById(R.id.phoneNumText);
        care_giver_Info = root.findViewById(R.id.care_giver_Info);
        careGiverNameText = root.findViewById(R.id.careGiverNameText);
        careGiverphoneNumText = root.findViewById(R.id.careGiverphoneNumText);
        accountInfo = root.findViewById(R.id.accountInfo);
        idText = root.findViewById(R.id.idText);
        pwText = root.findViewById(R.id.pwText);
        pwConfirmText = root.findViewById(R.id.pwConfirmText);
        nameForm = root.findViewById(R.id.nameForm);
        phoneNumForm = root.findViewById(R.id.phoneNumForm);
        careGiverNameForm = root.findViewById(R.id.careGiverNameForm);
        careGiverPhoneNumForm = root.findViewById(R.id.careGiverPhoneNumForm);
        idForm = root.findViewById(R.id.idForm);
        passwordForm = root.findViewById(R.id.passwordForm);
        passwordConfirmForm = root.findViewById(R.id.passwordConfirmForm);

        return root;
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setSignupMode(){
        nameForm.setText("");
        phoneNumForm.setText("");
        idForm.setText("");
        passwordForm.setText("");
        passwordConfirmForm.setText("");
        signup_button.setEnabled(true);
    }
    public void postFirebaseDatabase(boolean add){
        mPostreference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            CareReceiverInfo post = new CareReceiverInfo(name, phoneNum, ID, careGiverName, careGiverPhoneNum, password, passwordConfirm);
            postValues = post.toMap();
        }
        childUpates.put("/CareReceiver_list/" + phoneNum, postValues);
        mPostreference.updateChildren(childUpates);
    }
    /**
     * isExistPhoneNum() DB에서 PhoneNum 검색 후
     * 같은 PhoneNum이 존재하면 -> 회원가입 실패
     * 등록된 PhoneNum이 존재하지 않으면 -> 회원가입 성공(화면 넘기기까지)
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signup_button){
            name = nameForm.getText().toString();
            phoneNum = Long.parseLong(phoneNumForm.getText().toString());
            ID = idForm.getText().toString();
            careGiverName = careGiverNameForm.getText().toString();
            careGiverPhoneNum = Long.parseLong(careGiverPhoneNumForm.getText().toString());
            password  = Long.parseLong(passwordForm.getText().toString());
            passwordConfirm = Long.parseLong(passwordConfirmForm.getText().toString());

            postFirebaseDatabase(true);
            setSignupMode();
            Toast.makeText(getActivity(),"회원가입 완료!",Toast.LENGTH_SHORT).show();
        }
    }
}