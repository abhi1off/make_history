package com.example.makehistory.makehistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailEventAdapter extends RecyclerView.Adapter<DetailEventAdapter.EViewHolder> {
    Context ctxs;
    RecyclerView rvDetailEvents;

    ArrayList<DetailEventsClass> detailEventList;

    public DetailEventAdapter(Context ctxs,ArrayList<DetailEventsClass> detailEventList){
        this.ctxs = ctxs;
        this.detailEventList = detailEventList;
    }

    // On Create View Holder to Inflate transaction row layout
    @NonNull
    @Override
    public DetailEventAdapter.EViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctxs).inflate(R.layout.detail_event_layout,parent,false);
        return new DetailEventAdapter.EViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailEventAdapter.EViewHolder holder, int position) {
        // Setting Message to a TextView in Row Layout
        holder.evDetYear.setText(String.valueOf(detailEventList.get(position).getYear()));
        holder.evDetEvent.setText(detailEventList.get(position).getDetailMessage());
    }

    // To get size of the list
    @Override
    public int getItemCount() {
        return detailEventList.size();
    }

    // View Holder for a Transaction
    public static class EViewHolder extends RecyclerView.ViewHolder{
        TextView evDetYear;
        TextView evDetEvent;
        public EViewHolder(@NonNull View itemView) {
            super(itemView);
            evDetYear = itemView.findViewById(R.id.evDetYear);
            evDetEvent = itemView.findViewById(R.id.evDetEvent);
        }
    }
}
