package com.example.makehistory.makehistory;

import java.util.ArrayList;

public class DetailEventsClass {
    public static ArrayList<EventClass> transactionsList = new ArrayList<>();

    private String detailMessage;
    private int year;

    public DetailEventsClass( int year, String message) {

        this.detailMessage = message;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String message) {
        this.detailMessage = message;
    }


}


