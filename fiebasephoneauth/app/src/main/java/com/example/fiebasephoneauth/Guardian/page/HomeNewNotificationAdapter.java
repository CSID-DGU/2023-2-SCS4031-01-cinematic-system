package com.example.fiebasephoneauth.Guardian.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;

public class HomeNewNotificationAdapter extends RecyclerView.Adapter<HomeNewNotificationAdapter.viewHolder> {

    NewNotificationData[] mData;

    HomeNewNotificationAdapter(NewNotificationData data[]) {
        mData = data;
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
    public HomeNewNotificationAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_new_notification_card_view, parent, false);
        return new HomeNewNotificationAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeNewNotificationAdapter.viewHolder holder, int position) {
        holder.newNotificationCardDate.setText(mData[position].getDate());
        holder.newNotificationCardTime.setText(mData[position].getTime());
        holder.newNotificationCardDescription.setText(mData[position].getDescription());
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

}
