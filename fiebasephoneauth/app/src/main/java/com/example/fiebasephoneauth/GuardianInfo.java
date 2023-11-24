package com.example.fiebasephoneauth;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * 피보호자 DB에 저장할 데이터 클래스 정의
 */
public class GuardianInfo {
    public String name;
    public String phoneNum;
    public String ID;
    public String password;
    public String deviceToken;

    public GuardianInfo(String name, String phoneNum, String ID, String password, String deviceToken){
        this.name = name;
        this.phoneNum = phoneNum;
        this.ID = ID;
        this.password = password;
        this.deviceToken = deviceToken;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phoneNum", phoneNum);
        result.put("id", ID);
        result.put("password", password);
        result.put("deviceToken", deviceToken);

        return result;
    }

}
