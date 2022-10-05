package com.example.makehistory.makehistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity2 extends AppCompatActivity {
    public DetailEventsClass deletedTransaction;
    TextView title;
    TextView evSend;
    EditText evYear;
    EditText evMessage;
    String myNewInt;
    ArrayList<DetailEventsClass> detailEventList;
    ArrayList<DetailEventsClass> subDetailEventList;
    DetailEventAdapter detailEventAdapter;
    RecyclerView devTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        detailEventList = new ArrayList<DetailEventsClass>();
        subDetailEventList = new ArrayList<DetailEventsClass>();

        devTransactions = findViewById(R.id.devTransactions);
        evSend = findViewById(R.id.evSend);
        evYear = findViewById(R.id.evYear);
        evMessage = findViewById(R.id.evMessage);

        Intent intent = getIntent();
        myNewInt = intent.getExtras().getString("pos");
        Log.d("the int is: ",myNewInt);
        loadData();

            this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            View v = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null);
            title =v.findViewById(R.id.title);
            title.setText(myNewInt);
            // Setting balance
            getSupportActionBar().setCustomView(v);
            getSupportActionBar().setElevation(0);

        //
        for(int i=0;i<detailEventList.size();i++){
            if(detailEventList.get(i).getPosition().equals(myNewInt)){
                subDetailEventList.add(detailEventList.get(i));
            }
        }
        //

        // Initializing recycler view
        devTransactions.setHasFixedSize(true);
        devTransactions.setLayoutManager(new LinearLayoutManager(this));
        detailEventAdapter = new DetailEventAdapter(this,subDetailEventList);
        devTransactions.setAdapter(detailEventAdapter);
        // detailEventAdapter.getFilter().filter(myNewInt)

        theSorter();

        evSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Input Validation
                if(evYear.getText().toString().trim().isEmpty() || evYear.getText().toString().trim().length() != 4)
                {
                    evYear.setError("Enter Valid Year!");
                    return;
                }
                if(evMessage.getText().toString().isEmpty())
                {
                    evMessage.setError("Enter a message!");
                    return;
                }
                try {
                    String pus = myNewInt;
                    int amt = Integer.parseInt(evYear.getText().toString().trim());

                    // Adding Transaction to recycler View
                    sendTransaction(pus,amt,evMessage.getText().toString().trim());
                    theSorter();
                    evYear.setText("");
                    evMessage.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e){
                    evYear.setError("Amount should be integer greater than zero!");
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                DetailEventsClass understandEvent = subDetailEventList.get(viewHolder.getAdapterPosition());

                for(int k=0;k<subDetailEventList.size();k++){
                    if(subDetailEventList.get(k).getDetailMessage().equals(understandEvent.getDetailMessage()) &&
                            subDetailEventList.get(k).getYear()==understandEvent.getYear()){
                        deletedTransaction = understandEvent;
                        break;
                    }
                }
                AlertDialog dialog = new AlertDialog.Builder(detailEventAdapter.ctxs)
                        .setCancelable(false)
                        .setTitle("Are you sure? The transaction will be deleted.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //removing the item, if cancel is done then the action will be undone
                                // this method is called when item is swiped.
                                // below line is to remove item from our array list.
                                subDetailEventList.remove(viewHolder.getAdapterPosition());
                                // below line is to notify our item is removed from adapter.
                                detailEventAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                // adding on click listener to our action of snack bar.
                                // below line is to add our item to array list with a position.
                                subDetailEventList.add(position, deletedTransaction);
                                // below line is to notify item is
                                // added to our adapter class.
                                detailEventAdapter.notifyItemInserted(position);
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // this method is called when item is swiped.
                                // below line is to remove item from our array list.

                                //deleting from view array
                                subDetailEventList.remove(viewHolder.getAdapterPosition());
                                //deleting from storage array
                                for(int j=0;j<detailEventList.size();j++){
                                    if(detailEventList.get(j).getDetailMessage().equals(deletedTransaction.getDetailMessage()) &&
                                    detailEventList.get(j).getYear()==deletedTransaction.getYear()&&
                                    detailEventList.get(j).getPosition().equals(deletedTransaction.getPosition())){
                                        detailEventList.remove(j);
                                    }
                                }
                                //
//                                detailEventList.remove(viewHolder.getAdapterPosition());
                                // below line is to notify our item is removed from adapter.
                                detailEventAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                dialogInterface.dismiss();
//                                checkIfEmpty(subDetailEventList.size());
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();





                // below line is to display our snack bar with action.
//                Snackbar.make(rvTransactions, deletedTransaction.getMessage(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // adding on click listener to our action of snack bar.
//                        // below line is to add our item to array list with a position.
//                        eventList.add(position, deletedTransaction);
//
//                        // below line is to notify item is
//                        // added to our adapter class.
//                        adapter.notifyItemInserted(position);
//                    }
//                }).show();
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(devTransactions);


    }

    // To load data from shared preference
    private void loadData() {
        SharedPreferences pref = getSharedPreferences("com.cs.ec",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("detailEvents",null);
        Type type = new TypeToken<ArrayList<DetailEventsClass>>(){}.getType();
        if(json!=null)
        {
            detailEventList=gson.fromJson(json,type);
        }
    }

    private void theSorter(){
        Collections.sort(subDetailEventList, new Comparator<DetailEventsClass>() {
            @Override
            public int compare(DetailEventsClass o1, DetailEventsClass o2) {
                int diff = o1.getYear() - o2.getYear();
                if(diff == 0 ){
                    return o1.getDetailMessage().compareTo(o2.getDetailMessage());
                }else{
                    return diff;
                }
            }
        });
    }

    // Storing data locally
    // using shared preferences
    // in onStop() method
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("com.cs.ec",MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(detailEventList);
        editor.putString("detailEvents",json);
        editor.apply();
    }

    // To add transaction
    private void sendTransaction(String pu,int yr,String msg) {
        subDetailEventList.add(new DetailEventsClass(pu,yr,msg));
        detailEventList.add(new DetailEventsClass(pu,yr,msg));
        detailEventAdapter.notifyDataSetChanged();
    }
}