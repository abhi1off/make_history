package com.example.makehistory.makehistory;

import java.util.ArrayList;

public class DetailEventsClass {
    public static ArrayList<EventClass> transactionsList = new ArrayList<>();

    private int position;
    private String detailMessage;
    private int year;

    public DetailEventsClass( int position, int year, String message) {

        this.position = position;
        this.detailMessage = message;
        this.year = year;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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


