package com.example.fiebasephoneauth;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CareReceiverInfo {
    /**
     * 10/27 피보호자 정보 클래스 정의
     */
    public String name;
    public long phoneNum;
    public String careGiverName;
    public long careGiverPhoneNum;
    public String ID;
    public long password;
    public long passwordConfirm;

    public CareReceiverInfo(String name, long phoneNum, String ID, String careGiverName, long careGiverPhoneNum, long password, long passwordConfirm){
        this.name = name;
        this.phoneNum = phoneNum;
        this.careGiverName = careGiverName;
        this.careGiverPhoneNum = careGiverPhoneNum;
        this.ID = ID;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", name);
        result.put("PhoneNum", phoneNum);
        result.put("Id", ID);
        result.put("CareGiverName", careGiverName);
        result.put("CareGiverPhoneNum", careGiverPhoneNum);
        result.put("Password", password);
        result.put("PasswordConfirm", passwordConfirm);

        return result;
    }
}
