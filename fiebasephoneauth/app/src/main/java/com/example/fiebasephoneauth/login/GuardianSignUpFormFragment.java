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

import com.example.fiebasephoneauth.GuardianInfo;
import com.example.fiebasephoneauth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>보호자 회원가입 입력 폼</h3>
 *
 * 회원가입에서 보호자 버튼을 클릭했을 때 보여지는 입력 폼
 */
public class GuardianSignUpFormFragment extends Fragment implements View.OnClickListener{
    /**
     * jsm512
     * Firebase DB 보호자 정보 저장
     */
    private DatabaseReference mPostreference = FirebaseDatabase
            .getInstance()
            .getReference();

    TextView userInfo;
    TextView nameText;
    TextView phoneNumText;
    TextView accountInfo;
    TextView idText;
    TextView pwText;
    TextView pwConfirmText;
    EditText nameForm;
    EditText phoneNumForm;
    EditText idForm;
    EditText passwordForm;
    EditText passwordConfirmForm;

    Button signup_button;

    String name;
    String ID;
    String phoneNum;
    String password;
    String passwordConfirm;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_guardian_sign_up_form, container, false);

        signup_button = root.findViewById(R.id.signup_button);
        signup_button.setOnClickListener(this);
        userInfo = root.findViewById(R.id.userInfo);
        nameText = root.findViewById(R.id.nameText);
        phoneNumText = root.findViewById(R.id.phoneNumText);
        accountInfo = root.findViewById(R.id.accountInfo);
        idText = root.findViewById(R.id.idText);
        pwText = root.findViewById(R.id.pwText);
        pwConfirmText = root.findViewById(R.id.pwConfirmText);
        nameForm = root.findViewById(R.id.nameForm);
        phoneNumForm = root.findViewById(R.id.phoneNumForm);
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

//    public void postFirebaseDatabase(boolean add){
//        mPostreference = FirebaseDatabase.getInstance().getReference();
//        Map<String, Object> childUpates = new HashMap<>();
//        Map<String, Object> postValues = null;
//        if(add){
//            GuardianInfo post = new GuardianInfo(name,phoneNum,ID,password,passwordConfirm);
//            postValues = post.toMap();
//        }
//        childUpates.put("/Guardian_list/" + phoneNum, postValues);
//        mPostreference.updateChildren(childUpates);
//    }

    /**
     * isExistPhoneNum과 같은 DB에서 PhoneNum 검색 후
     * 같은 PhoneNum이 존재하면 -> 회원가입 실패
     * 등록된 PhoneNum이 존재하지 않으면 -> 회원가입 성공(화면 넘기기까지)
     */
    @Override
    public void onClick(View v) {
        name = nameForm.getText().toString();
        phoneNum = phoneNumForm.getText().toString();
        ID = idForm.getText().toString();
        password  = passwordForm.getText().toString();
        passwordConfirm = passwordConfirmForm.getText().toString();

        if (name.isEmpty() || phoneNum.isEmpty() || ID.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            Toast.makeText(getActivity(), "사용자 정보를 모두 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(passwordConfirm)){
            Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            mPostreference.child("Guardian_list").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(phoneNum)){
                        Toast.makeText(getActivity(), "이미 등록된 번호입니다.", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        Map<String, Object> childUpates = new HashMap<>();
                        Map<String, Object> postValues = null;
                        GuardianInfo post = new GuardianInfo(name,phoneNum,ID,password);
                        postValues = post.toMap();
                        childUpates.put("/Guardian_list/" + phoneNum, postValues);
                        mPostreference.updateChildren(childUpates);

                        setSignupMode();
                        Toast.makeText(getActivity(), "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}