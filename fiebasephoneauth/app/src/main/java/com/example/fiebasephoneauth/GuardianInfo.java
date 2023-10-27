package com.example.fiebasephoneauth;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * 피보호자 DB에 저장할 데이터 클래스 정의
 */
public class GuardianInfo {
    public String name;
    public Long phoneNum;

    public String ID;
    public Long password;
    public Long passwordConfirm;

    public GuardianInfo(String name, long phoneNum, String ID, long password, long passwordConfirm){
        this.name = name;
        this.phoneNum = phoneNum;
        this.ID = ID;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phoneNum", phoneNum);
        result.put("id", ID);
        result.put("password", password);
        result.put("passwordConfirm", passwordConfirm);

        return result;
    }

}
