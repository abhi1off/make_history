package com.example.makehistory.makehistory;

import java.util.ArrayList;

public class EventClass {
    public static ArrayList<EventClass> transactionsList = new ArrayList<>();

    private int amount;
    private String message;
    private boolean positive;
    private String date;
    private String monthYear;

    public EventClass( String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
