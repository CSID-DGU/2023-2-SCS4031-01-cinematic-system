package com.example.fiebasephoneauth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    // firebase Database 연동
//    private FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//    //DatabaseReference를 통해 데이터베이스의 특정 위치로 이동이 가능하게 연결함
//    private DatabaseReference databaseReference = database.getReference();
//    FirebaseAuth firebaseAuth;

    /**
     *
     *10/22 firebase realtime database connection
     */
    private DatabaseReference mPostreference;

    /**
     * activity_profile.xml에서 선언한 widget 변수 설정
     */
    Button btn_Update;
    Button btn_Insert;
    Button btn_Select;
    EditText edit_ID;
    EditText edit_Name;
    EditText edit_Age;
    TextView text_ID;
    TextView text_Name;
    TextView text_Age;
    TextView text_Gender;
    CheckBox check_Man;
    CheckBox check_Woman;
    CheckBox check_ID;
    CheckBox check_Name;
    CheckBox check_Age;

    String ID;
    String name;
    long age;
    String gender = "";
    String sort = "id";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
/**
 * MainActivity에서 받은 전화 번호 ProfileActivity로 가져오는 코드
 */
//        Intent intent = getIntent();
//
//        String text = intent.getStringExtra("text");
//        /**
//         * TextView import 안되는 문제 해결 TextView에 해당하는 id를 직접 찾아서 입력 해야 import가 됨
//         */
//        TextView text_tv = findViewById(R.id.text_tv);
//        text_tv.setText(text);
        /**
         * 10/22일 firebase realtime database connection
         * 각 widget에 대한 id 값 불러오기
         */
        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);
        btn_Select = (Button) findViewById(R.id.btn_select);
        btn_Select.setOnClickListener(this);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Age = (EditText) findViewById(R.id.edit_age);
        text_ID = (TextView) findViewById(R.id.text_id);
        text_Name = (TextView) findViewById(R.id.text_name);
        text_Age = (TextView) findViewById(R.id.text_age);
        text_Gender= (TextView) findViewById(R.id.text_gender);
        check_Man = (CheckBox) findViewById(R.id.check_man);
        check_Man.setOnClickListener(this);
        check_Woman = (CheckBox) findViewById(R.id.check_woman);
        check_Woman.setOnClickListener(this);
        check_ID = (CheckBox) findViewById(R.id.check_userid);
        check_ID.setOnClickListener(this);
        check_Name = (CheckBox) findViewById(R.id.check_name);
        check_Name.setOnClickListener(this);
        check_Age = (CheckBox) findViewById(R.id.check_age);
        check_Age.setOnClickListener(this);
/**
 * database에 등록된 user 정보를 출력하는 list
 */
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        check_ID.setChecked(true);
        getFirebaseDatabase();

        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);
    }

    /**
     * 초기 widget들 상태 설정
     * Text는 빈칸으로
     * check -> false 체크 안함 default
     * btn -> true : 버튼 활성화, fasle : 비활성
     */
    public void setInsertMode(){
        edit_ID.setText("");
        edit_Name.setText("");
        edit_Age.setText("");
        check_Man.setChecked(false);
        check_Woman.setChecked(false);
        btn_Insert.setEnabled(true);
    }


    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);
            edit_ID.setText(tempData[0].trim());
            edit_Name.setText(tempData[1].trim());
            edit_Age.setText(tempData[2].trim());
            if(tempData[3].trim().equals("Man")){
                check_Man.setChecked(true);
                gender = "Man";
            }else{
                check_Woman.setChecked(true);
                gender = "Woman";
            }
            edit_ID.setEnabled(false);
            btn_Insert.setEnabled(false);
            btn_Update.setEnabled(true);
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            final String[] nowData = arrayData.get(position).split("\\s+");
            ID = nowData[0];
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3];
            AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postFirebaseDatabase(false);
                            getFirebaseDatabase();
                            setInsertMode();
                            edit_ID.setEnabled(true);
                            Toast.makeText(ProfileActivity.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ProfileActivity.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            setInsertMode();
                            edit_ID.setEnabled(true);
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public boolean IsExistID(){
        boolean IsExist = arrayIndex.contains(ID);
        return IsExist;
    }

    /**
     * firebase DB의 데이터를 저장/업데이트/삭제
     * Update를 진행할 child와 해당 child에 입력할 HashMap 선언
     * boolean 타입으로 데이터의 저장/업데이트/삭제를 구별 -> false : 삭제, true : UserInfo를 사용해 id를 key로 사용해 DB로 전달
     * 기존 id가 존재하는 경우 Update, 존재하지 않는 경우 DB에 새로운 데이터(사용자 정보) 추가
     */
    public void postFirebaseDatabase(boolean add){
        mPostreference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            UserInfo post = new UserInfo(ID, name, age, gender);
            postValues = post.toMap();
        }
        childUpdates.put("/user_list/" + ID, postValues);
        mPostreference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                arrayData.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    UserInfo get = postSnapshot.getValue(UserInfo.class);
                    String[] info = {get.id, get.name, String.valueOf(get.age), get.gender};
                    String Result = setTextLength(info[0],10) + setTextLength(info[1],10) + setTextLength(info[2],10) + setTextLength(info[3],10);
                    arrayData.add(Result);
                    arrayIndex.add(key);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2] + info[3]);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(arrayData);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("user_list").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_insert) {
            ID = edit_ID.getText().toString();
            name = edit_Name.getText().toString();
            age = Long.parseLong(edit_Age.getText().toString());
            if (!IsExistID()) {
                postFirebaseDatabase(true);
                getFirebaseDatabase();
                setInsertMode();
            } else {
                Toast.makeText(ProfileActivity.this, "이미 존재하는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
            }
            edit_ID.requestFocus();
            edit_ID.setCursorVisible(true);
        } else if (id == R.id.btn_update) {
            ID = edit_ID.getText().toString();
            name = edit_Name.getText().toString();
            age = Long.parseLong(edit_Age.getText().toString());
            postFirebaseDatabase(true);
            getFirebaseDatabase();
            setInsertMode();
            edit_ID.setEnabled(true);
            edit_ID.requestFocus();
            edit_ID.setCursorVisible(true);
        }
        else if (id == R.id.btn_select) {
            getFirebaseDatabase();
        }
        else if (id == R.id.check_man) {
            check_Woman.setChecked(false);
            gender = "Man";
        } else if (id == R.id.check_woman) {
            check_Man.setChecked(false);
            gender = "Woman";
        }
        else if (id == R.id.check_userid) {
            check_Name.setChecked(false);
            check_Age.setChecked(false);
            sort = "id";
        } else if (id == R.id.check_name) {
            check_ID.setChecked(false);
            check_Age.setChecked(false);
            sort = "name";
        } else if (id == R.id.check_age) {
            check_ID.setChecked(false);
            check_Name.setChecked(false);
            sort = "age";
        }
    }

}
