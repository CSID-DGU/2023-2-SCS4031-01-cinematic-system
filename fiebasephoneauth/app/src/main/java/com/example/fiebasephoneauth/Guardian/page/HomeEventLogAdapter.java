package com.example.fiebasephoneauth.Guardian.page;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeEventLogAdapter extends RecyclerView.Adapter<HomeEventLogAdapter.viewHolder> {

    private ArrayList<EventCardInfo> EventCardInfo_arrayList;
    private ArrayList<EventCardInfo> data;
    private String idTxt;
    String Title;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH시 mm분");
    String currentDate = dateFormat.format(calendar.getTime());
    String currentTime = timeFormat.format(calendar.getTime());

    public HomeEventLogAdapter(ArrayList<EventCardInfo> arrayList,String id){
        this.EventCardInfo_arrayList = arrayList;
        this.idTxt = id;
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
        ImageView eventLogCardIcon;
        Button eventLogCardSeeDetail;
        public viewHolder(View itemView){
            super(itemView);
            eventLogCard = itemView.findViewById(R.id.event_log_card);
            eventLogCardIcon = itemView.findViewById(R.id.event_log_card_icon);
            eventLogCardTitle = itemView.findViewById(R.id.event_log_card_title);
            eventLogCardDescription = itemView.findViewById(R.id.event_log_card_description);

            eventLogCardSeeDetail = itemView.findViewById(R.id.event_log_see_detail_button);
            eventLogCardSeeDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.event_log_see_detail_button){
                String Title = eventLogCardTitle.getText().toString();
                String Description = eventLogCardDescription.getText().toString();
                Intent intent = new Intent(v.getContext(), GuardianEventLogDetail.class);
                intent.putExtra("Title",Title);
                intent.putExtra("Description",Description);
                intent.putExtra("Date",currentDate);
                intent.putExtra("Time",currentTime);
                intent.putExtra("id",idTxt);

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
        holder.eventLogCardTitle.setText(EventCardInfo_arrayList.get(position).getTitle());
        Title = EventCardInfo_arrayList.get(position).getTitle();
        holder.eventLogCardDescription.setText(EventCardInfo_arrayList.get(position).getDescription_short());
        holder.eventLogCardIcon.setImageResource(R.drawable.fire);
        holder.eventLogCardIcon.setBackgroundColor(0xD64545);
    }

    @Override
    public int getItemCount(){
        return EventCardInfo_arrayList.size();
    }



}
