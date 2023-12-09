package com.example.fiebasephoneauth.Guardian.page;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeEventLogAdapter extends RecyclerView.Adapter<HomeEventLogAdapter.viewHolder> {
    DatabaseReference docRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-phoneauth-97f7e-default-rtdb.firebaseio.com/");
    private ArrayList<EventCardInfo> EventCardInfo_arrayList;
    private ArrayList<EventCardInfo> data;
    private String idTxt;
    private String title;
    private String[] imgSrc;
    private String time;



    public HomeEventLogAdapter(ArrayList<EventCardInfo> arrayList,String id){
        this.EventCardInfo_arrayList = arrayList;
        this.idTxt = id;
        imgSrc = new String[EventCardInfo_arrayList.size()];
    }

    public void setData(ArrayList<EventCardInfo> data){
        this.data = data;
    }

    public ArrayList<EventCardInfo> getData() {
        return data;
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView eventLogCard;
        TextView eventLogCardTitle;
        TextView eventLogCardDescription;
        TextView eventLogCardTime;
        ImageView eventLogCardIcon;
        Button eventLogCardSeeDetail;
        public viewHolder(View itemView){
            super(itemView);
            eventLogCard = itemView.findViewById(R.id.event_log_card);
            eventLogCardIcon = itemView.findViewById(R.id.event_log_card_icon);
            eventLogCardTitle = itemView.findViewById(R.id.event_log_card_title);
            eventLogCardDescription = itemView.findViewById(R.id.event_log_card_description);
            eventLogCardTime = itemView.findViewById(R.id.event_log_card_timestamp);

            eventLogCardSeeDetail = itemView.findViewById(R.id.event_log_see_detail_button);
            eventLogCardSeeDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.event_log_see_detail_button){
                String Title = eventLogCardTitle.getText().toString();
                String Description = eventLogCardDescription.getText().toString();
                String Time = eventLogCardTime.getText().toString();
                Intent intent = new Intent(v.getContext(), GuardianEventLogDetail.class);
                intent.putExtra("Title",Title);
                intent.putExtra("ImgSrc", imgSrc[getAdapterPosition()]);
                intent.putExtra("Description",Description);
                intent.putExtra("Time",Time);
                intent.putExtra("id",idTxt);

                // 모든 이벤트에 대해 자세히 보기 버튼 클릭했을때 동작이 됨 -> 활동량 응급상황에 대해서만 수행되도록 변경 필요함
                docRef.child("Guardian_list").child(idTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild("CareReceiverID")) {
                            String getCareReceiverId = snapshot.child("CareReceiverID").getValue(String.class);
                            docRef.child("CareReceiver_list").child(getCareReceiverId).child("ActivityData").child("activity").child("time").removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                v.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public HomeEventLogAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_log_card_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeEventLogAdapter.viewHolder holder, int position){
        title = EventCardInfo_arrayList.get(position).getTitle();
        time = EventCardInfo_arrayList.get(position).getTime();
        imgSrc[position] = EventCardInfo_arrayList.get(position).getImgSrc();
        int imgResId = holder.itemView.getContext().getResources().getIdentifier(imgSrc[position], "drawable", holder.itemView.getContext().getPackageName());
        holder.eventLogCardTitle.setText(title);
        holder.eventLogCardDescription.setText(EventCardInfo_arrayList.get(position).getDescription_short());
        holder.eventLogCardIcon.setImageResource(imgResId);
        holder.eventLogCardTime.setText(time);
    }

    @Override
    public int getItemCount(){
        return EventCardInfo_arrayList.size();
    }



}
