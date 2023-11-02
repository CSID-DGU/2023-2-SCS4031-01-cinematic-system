package com.example.fiebasephoneauth.Guardian.page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fiebasephoneauth.R;

public class HomeMainAdapter extends RecyclerView.Adapter<HomeMainAdapter.viewHolder> {
    CareReceiverActivityData[] mData;

    public HomeMainAdapter(CareReceiverActivityData[] data) {
        mData = data;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        CardView homeMainCard;
        TextView mainCardText;
        TextView mainCardDescription;

        public viewHolder(View itemView) {
            super(itemView);
            homeMainCard = itemView.findViewById(R.id.home_main_card);
            mainCardText = itemView.findViewById(R.id.home_main_card_text);
            mainCardDescription = itemView.findViewById(R.id.home_main_card_description);
        }
    }


        @Override
        public HomeMainAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_main_card_view, parent, false);
            return new viewHolder(view);
        }

        @Override
        public void onBindViewHolder(HomeMainAdapter.viewHolder holder, int position) {
            holder.mainCardText.setText(mData[position].getTitle());
            holder.mainCardDescription.setText(mData[position].getDescription());
        }

        @Override
        public int getItemCount() {
            return mData.length;
        }
}
