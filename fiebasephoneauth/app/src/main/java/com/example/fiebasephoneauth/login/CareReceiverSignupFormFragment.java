package com.example.fiebasephoneauth.login;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>피보호자 회원가입 입력 폼</h3>
 *
 * 회원가입에서 피보호자 버튼을 클릭했을 때 보여지는 입력 폼
 */
public class CareReceiverSignupFormFragment extends Fragment implements View.OnClickListener {
    /**
     * Firebase DB에서 테이블 값 호출 메소드
     */


    /**
     * jsm512
     * Firebase DB 피보호자 정보 저장
     */
    private DatabaseReference mPostreference = FirebaseDatabase.getInstance().getReference();


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
    String phoneNum;
    String careGiverName;
    String careGiverPhoneNum;
    String ID;
    String password;
    String passwordConfirm;

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

    public void setSignupMode() {
        nameForm.setText("");
        phoneNumForm.setText("");
        idForm.setText("");
        careGiverNameForm.setText("");
        careGiverPhoneNumForm.setText("");
        passwordForm.setText("");
        passwordConfirmForm.setText("");
        signup_button.setEnabled(true);
    }

    /**
     * isExistPhoneNum() DB에서 PhoneNum 검색 후
     * 같은 PhoneNum이 존재하면 -> 회원가입 실패
     * 등록된 PhoneNum이 존재하지 않으면 -> 회원가입 성공
     */

    @Override
    public void onClick(View v) {
        name = nameForm.getText().toString();
        phoneNum = phoneNumForm.getText().toString();
        ID = idForm.getText().toString();
        careGiverName = careGiverNameForm.getText().toString();
        careGiverPhoneNum = careGiverPhoneNumForm.getText().toString();
        password = passwordForm.getText().toString();
        passwordConfirm = passwordConfirmForm.getText().toString();

        if (name.isEmpty() || phoneNum.isEmpty() || ID.isEmpty() || careGiverName.isEmpty()
                || careGiverPhoneNum.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(getActivity(), "사용자 정보를 모두 입력해주세요!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        } else {
            mPostreference.child("CareReceiver_list").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(ID)){
                        Toast.makeText(getActivity(), "이미 등록된 번호입니다.", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Map<String, Object> childUpates = new HashMap<>();
                        Map<String, Object> postValues = null;
                        CareReceiverInfo post = new CareReceiverInfo(name,phoneNum,ID,password, careGiverName, careGiverPhoneNum);
                        postValues = post.toMap();
                        childUpates.put("/CareReceiver_list/" + ID, postValues);
                        mPostreference.updateChildren(childUpates);

                        setSignupMode();
                        Toast.makeText(getActivity(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }
}
