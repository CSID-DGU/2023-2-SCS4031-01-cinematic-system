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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MultiViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> items;
    private static String idTxt;
    private static final int VIEW_TYPE_HOME = 1;
    private static final int VIEW_TYPE_EVENT = 2;

    static Calendar calendar = Calendar.getInstance();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일");
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH시 mm분");
    static String currentDate = dateFormat.format(calendar.getTime());
    static String currentTime = timeFormat.format(calendar.getTime());


    public MultiViewAdapter(ArrayList<Object> items){
        this.items = items;
    }
    public MultiViewAdapter(ArrayList<Object> items, String id){
        this.items = items;
        this.idTxt = id;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HOME){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_new_notification_card_view, parent, false);
            return new HomeViewHolder(view);
        }
        else if(viewType == VIEW_TYPE_EVENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_log_card_view, parent, false);
            return new EventViewHolder(view);
        }

        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_HOME:
                ((HomeViewHolder) holder).setHomeData((NewNotificationData) item);
                break;
            case VIEW_TYPE_EVENT:
                ((EventViewHolder) holder).setEventData((EventCardInfo) item);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getItemViewType(int position){
        Object item = items.get(position);

        if(item instanceof NewNotificationData){
            return VIEW_TYPE_HOME;
        }
        else if(item instanceof EventCardInfo){
            return VIEW_TYPE_EVENT;
        }
        throw new IllegalArgumentException("Invalid item type");
    }

static class HomeViewHolder extends RecyclerView.ViewHolder{
        private CardView homeNewNotificationCard;
        private TextView newNotificationCardDate, newNotificationCardTime;
        private TextView newNotificationCardDescription;

        HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            homeNewNotificationCard = itemView.findViewById(R.id.home_new_notification_card);
            newNotificationCardDate = itemView.findViewById(R.id.home_new_notification_date);
            newNotificationCardTime = itemView.findViewById(R.id.home_new_notification_time);
            newNotificationCardDescription = itemView.findViewById(R.id.home_new_notification_description);
        }
        void setHomeData(NewNotificationData Home){
            newNotificationCardDate.setText(Home.getDate());
            newNotificationCardTime.setText(Home.getTime());
            newNotificationCardDescription.setText(Home.getDescription());
        }
    }
static class EventViewHolder extends RecyclerView.ViewHolder{
        private CardView eventLogCard;
        private TextView eventLogCardTitle;
        private TextView eventLogCardDescription;
        private ImageView eventLogCardIcon;
        private Button eventLogCardSeeDetail;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventLogCard = itemView.findViewById(R.id.event_log_card);
            eventLogCardIcon = itemView.findViewById(R.id.event_log_card_icon);
            eventLogCardTitle = itemView.findViewById(R.id.event_log_card_title);
            eventLogCardDescription = itemView.findViewById(R.id.event_log_card_description);

            eventLogCardSeeDetail = itemView.findViewById(R.id.event_log_see_detail_button);
            eventLogCardSeeDetail.setOnClickListener(new View.OnClickListener() {
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
            });
        }
        void setEventData(EventCardInfo Event){
            eventLogCardTitle.setText(Event.getTitle());
            eventLogCardDescription.setText(Event.getDescription_short());
            eventLogCardIcon.setImageResource(R.drawable.fire);
            eventLogCardIcon.setBackgroundColor(0xD64545);
        }
    }
}
