package com.example.makehistory.makehistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SelectEventListener{

    TextView tvSign;
    public static TextView tvEmpty, tvBalance, monthYearTV;
    EditText etAmount, etMessage;
    TextView ivSend;
    TextView title;
    boolean positive = true;
    RecyclerView rvTransactions;
    TransactionAdapter adapter;
    ArrayList<EventClass> eventList;
    Date d = new Date();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    String formattedDate = df.format(d);

    // On create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Function to initialize views
        initViews();

        //init spinner
//        Spinner monthSpinner = findViewById(R.id.monthSpinner);
//        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this,adapterView.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        // Function to load data from shared preferences
        loadData();

        // Function to set custom action bar
        setCustomActionBar();

        // To check if there is no transaction
        checkIfEmpty(eventList.size());

        // Initializing recycler view
        rvTransactions.setHasFixedSize(true);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(this,eventList,this);
        rvTransactions.setAdapter(adapter);


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
                EventClass deletedTransaction = eventList.get(viewHolder.getAdapterPosition());
                AlertDialog dialog = new AlertDialog.Builder(adapter.ctx)
                        .setCancelable(false)
                        .setTitle("Are you sure? The transaction will be deleted.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //removing the item, if cancel is done then the action will be undone
                                // this method is called when item is swiped.
                                // below line is to remove item from our array list.
                                eventList.remove(viewHolder.getAdapterPosition());
                                // below line is to notify our item is removed from adapter.
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                // adding on click listener to our action of snack bar.
                                // below line is to add our item to array list with a position.
                                eventList.add(position, deletedTransaction);
                                // below line is to notify item is
                                // added to our adapter class.
                                adapter.notifyItemInserted(position);
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // this method is called when item is swiped.
                                // below line is to remove item from our array list.
                                eventList.remove(viewHolder.getAdapterPosition());
                                // below line is to notify our item is removed from adapter.
                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                dialogInterface.dismiss();
                                checkIfEmpty(eventList.size());
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
        }).attachToRecyclerView(rvTransactions);

        // On click Send Expense
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Input Validation
                if(etMessage.getText().toString().trim().isEmpty())
                {
                    etMessage.setError("Enter Event!");
                    return;
                }

                    // Adding Transaction to recycler View
                    sendTransaction(etMessage.getText().toString().trim());
                    checkIfEmpty(eventList.size());

                    // To update Balance
                    etMessage.setText("");
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

//                catch (Exception e){
//                    etAmount.setError("Amount should be integer greater than zero!");
//                }
            }
        });

    }

    // To set custom action bar
    private void setCustomActionBar() {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null);
        title =v.findViewById(R.id.title);
        // Setting balance
        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setElevation(0);
    }


    // To load data from shared preference
    private void loadData() {
        SharedPreferences pref = getSharedPreferences("com.cs.ec",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("events",null);
        Type type = new TypeToken<ArrayList<EventClass>>(){}.getType();
        if(json!=null)
        {
            eventList=gson.fromJson(json,type);
        }
    }

    // To add transaction
    private void sendTransaction(String msg) {
        eventList.add(new EventClass(msg));
        adapter.notifyDataSetChanged();
        rvTransactions.smoothScrollToPosition(eventList.size()-1);
    }

    // Function to change sign
    private void changeSign() {
        if(positive)
        {
            tvSign.setText("-₹");
            tvSign.setTextColor(Color.parseColor("#F44336"));
            positive = false;
        }
        else {
            tvSign.setText("+₹");
            tvSign.setTextColor(Color.parseColor("#00c853"));
            positive = true;
        }
    }

    // To check if transaction list is empty
    public static void checkIfEmpty(int size) {
        if (size == 0)
        {
            MainActivity.tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            MainActivity.tvEmpty.setVisibility(View.GONE);
        }
    }

    // Initializing Views
    private void initViews() {
        eventList = new ArrayList<EventClass>();
        rvTransactions = findViewById(R.id.rvTransactions);
        etMessage = findViewById(R.id.etMessage);
        ivSend = findViewById(R.id.ivSend);
        tvEmpty = findViewById(R.id.tvEmpty);
    }



    // Storing data locally
    // using shared preferences
    // in onStop() method
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("com.cs.ec",MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(eventList);
        editor.putString("events",json);
        editor.apply();
    }

    @Override
    public void onItemClicked(EventClass eventClass, int pos) {
//        Toast.makeText(this,eventClass.getMessage(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("pos",eventClass.getMessage());
        startActivity(intent);
    }
}