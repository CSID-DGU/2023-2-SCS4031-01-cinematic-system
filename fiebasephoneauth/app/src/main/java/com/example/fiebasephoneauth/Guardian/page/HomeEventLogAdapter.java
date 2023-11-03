package com.example.fiebasephoneauth.Guardian.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;

public class HomeEventLogAdapter extends RecyclerView.Adapter<HomeEventLogAdapter.viewHolder> {

    EventCardInfo[] mData;



    public HomeEventLogAdapter(EventCardInfo[] eventCardInfo){
        this.mData = eventCardInfo;
    }

    class viewHolder extends RecyclerView.ViewHolder{

        CardView eventLogCard;
        TextView eventLogCardTitle;
        TextView eventLogCardDescription;
        ImageView eventLogCardIcon;
        public viewHolder(View itemView){
            super(itemView);
            eventLogCard = itemView.findViewById(R.id.event_log_card);
            eventLogCardIcon = itemView.findViewById(R.id.event_log_card_icon);
            eventLogCardTitle = itemView.findViewById(R.id.event_log_card_title);
            eventLogCardDescription = itemView.findViewById(R.id.event_log_card_description);
        }
    }

    @Override
    public HomeEventLogAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_log_card_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeEventLogAdapter.viewHolder holder, int position){
        holder.eventLogCardTitle.setText(mData[position].getTitle());
        holder.eventLogCardDescription.setText(mData[position].getDescription_short());
        holder.eventLogCardIcon.setImageResource(R.drawable.fire);
        holder.eventLogCardIcon.setBackgroundColor(0xD64545);
    }

    @Override
    public int getItemCount(){
        return mData.length;
    }



}
