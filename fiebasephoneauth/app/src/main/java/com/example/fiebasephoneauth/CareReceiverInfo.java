package com.example.fiebasephoneauth;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CareReceiverInfo {
    /**
     * 10/27 피보호자 정보 클래스 정의
     */
    public String name;
    public String phoneNum;
    public String careGiverName;
    public String  careGiverPhoneNum;
    public String ID;
    public String password;
    public String passwordConfirm;

    public CareReceiverInfo(String name, String phoneNum, String ID, String password, String careGiverName, String careGiverPhoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
        this.careGiverName = careGiverName;
        this.careGiverPhoneNum = careGiverPhoneNum;
        this.ID = ID;
        this.password = password;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phoneNum", phoneNum);
        result.put("id", ID);
        result.put("CareGiverName", careGiverName);
        result.put("CareGiverPhoneNum", careGiverPhoneNum);
        result.put("password", password);

        return result;
    }
}
