package com.example.fiebasephoneauth;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * 10/22일
 * firebasseRealtimeDatabase에서 읽고 저장할 때 사용하는 클래스 데이터 구조에 따라 변경 가능
 */
public class UserInfo {
   public String id;
   public String name;
   public Long age;
   public String gender;

   public UserInfo(){

   }
   public UserInfo(String id, String name, Long age, String gender){
       this.id = id;
       this.name = name;
       this.age = age;
       this.gender = gender;
   }

   @Exclude
   public Map<String, Object> toMap(){
       HashMap<String, Object> result = new HashMap<>();
       result.put("id",id);
       result.put("name", name);
       result.put("age", age);
       result.put("gender", gender);

       return result;

   }
}
