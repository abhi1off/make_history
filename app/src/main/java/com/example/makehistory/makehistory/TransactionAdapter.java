package com.example.makehistory.makehistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TViewHolder> {
    Context ctx;
    RecyclerView rvTransactions;
    private SelectEventListener listener;

    // List containing data for recyclerview
    ArrayList<EventClass> eventList;


    // Constructor for TransactionAdapter
    public TransactionAdapter(Context ctx, ArrayList<EventClass> eventList, SelectEventListener listener) {
        this.ctx = ctx;
        this.eventList = eventList;
        this.listener = listener;
    }


    // On Create View Holder to Inflate transaction row layout
    @NonNull
    @Override
    public TransactionAdapter.TViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.sample_transaction_layout,parent,false);
        return new TransactionAdapter.TViewHolder(v);
    }




    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TViewHolder holder, int position) {

        // Setting Message to a TextView in Row Layout
        holder.tvMessage.setText(eventList.get(holder.getAdapterPosition()).getMessage());

        holder.eventMainHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(eventList.get(position), position);
            }
        });
    }

    // To get size of the list
    @Override
    public int getItemCount() {
        return eventList.size();
    }


    // View Holder for a Transaction
    public static class TViewHolder extends RecyclerView.ViewHolder{
        TextView tvAmount,tvMessage;
        TextView ivDate;
        LinearLayout eventMainHolder;

        public TViewHolder(@NonNull View itemView) {
            super(itemView);
            eventMainHolder = itemView.findViewById(R.id.eventMainHolder);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
