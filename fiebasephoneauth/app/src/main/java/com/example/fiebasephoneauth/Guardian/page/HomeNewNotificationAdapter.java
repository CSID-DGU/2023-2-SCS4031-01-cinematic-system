package com.example.fiebasephoneauth.Guardian.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;

import java.util.ArrayList;

public class HomeNewNotificationAdapter extends RecyclerView.Adapter<HomeNewNotificationAdapter.viewHolder> {

    private ArrayList<NewNotificationData> Notification_arrayList;
    private ArrayList<NewNotificationData> data;

    HomeNewNotificationAdapter(ArrayList<NewNotificationData> arrayList){
        this.Notification_arrayList = arrayList;
    }

    public void setData(ArrayList<NewNotificationData> data) {
        this.data = data;
    }

    public ArrayList<NewNotificationData> getData() {
        return data;
    }

    class viewHolder extends RecyclerView.ViewHolder {
            CardView homeNewNotificationCard;
            TextView newNotificationCardDate, newNotificationCardTime;
            TextView newNotificationCardDescription;

            public viewHolder(View itemView) {
                super(itemView);
                homeNewNotificationCard = itemView.findViewById(R.id.home_new_notification_card);
                newNotificationCardDate = itemView.findViewById(R.id.home_new_notification_date);
                newNotificationCardTime = itemView.findViewById(R.id.home_new_notification_time);
                newNotificationCardDescription = itemView.findViewById(R.id.home_new_notification_description);
            }
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_new_notification_card_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.newNotificationCardDate.setText(Notification_arrayList.get(position).getDate());
        holder.newNotificationCardTime.setText(Notification_arrayList.get(position).getTime());
        holder.newNotificationCardDescription.setText(Notification_arrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return Notification_arrayList.size();
    }

}
