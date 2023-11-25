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
    public String careGiverPhoneNum;
    public String ID;
    public String password;
    public String Age;
    public String Address;
    public String gender;


    public CareReceiverInfo(String name, String phoneNum, String ID, String password, String careGiverName, String careGiverPhoneNum, String Age, String Address, String gender){
        this.name = name;
        this.phoneNum = phoneNum;
        this.careGiverName = careGiverName;
        this.careGiverPhoneNum = careGiverPhoneNum;
        this.ID = ID;
        this.password = password;
        this.Age = Age;
        this.Address = Address;
        this.gender = gender;
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
        result.put("Age", Age);
        result.put("Address",Address);
        result.put("gender",gender);

        return result;
    }
}
