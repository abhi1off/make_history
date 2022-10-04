package com.example.makehistory.makehistory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    TextView textView2;
    TextView evSend;
    EditText evYear;
    EditText evMessage;
    int myNewInt;
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
        myNewInt = intent.getIntExtra("pos",0);
        Log.i("the int is",String.valueOf(myNewInt));
        loadData();
        
        //
        for(int i=0;i<detailEventList.size();i++){
            if(detailEventList.get(i).getPosition()==myNewInt){
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

        evSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Input Validation
                if(evYear.getText().toString().trim().isEmpty())
                {
                    evYear.setError("Enter Amount!");
                    return;
                }
                if(evMessage.getText().toString().isEmpty())
                {
                    evMessage.setError("Enter a message!");
                    return;
                }
                try {
                    int pus = myNewInt;
                    int amt = Integer.parseInt(evYear.getText().toString().trim());

                    // Adding Transaction to recycler View
                    sendTransaction(pus,amt,evMessage.getText().toString().trim());

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
    private void sendTransaction(int pu,int yr,String msg) {
        subDetailEventList.add(new DetailEventsClass(pu,yr,msg));
        detailEventList.add(new DetailEventsClass(pu,yr,msg));
        detailEventAdapter.notifyDataSetChanged();
//        rvTransactions.smoothScrollToPosition(eventList.size()-1);
    }
}