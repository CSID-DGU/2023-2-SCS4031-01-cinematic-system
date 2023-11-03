package com.example.fiebasephoneauth.Guardian.page;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class HomeEventLogAdapter extends RecyclerView.Adapter<HomeEventLogAdapter.viewHolder> {

    EventCardInfo[] mData;

    public HomeEventLogAdapter(EventCardInfo[] eventCardInfo){
        this.mData = eventCardInfo;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        public viewHolder(View itemView){
            super(itemView);
        }
    }

    @Override
    public HomeEventLogAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return null;
    }

    @Override
    public void onBindViewHolder(HomeEventLogAdapter.viewHolder holder, int position){

    }

    @Override
    public int getItemCount(){
        return 0;
    }



}
