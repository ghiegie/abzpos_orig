package com.abztrakinc.ABZPOS;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private String Date,Time;



    public void generateDateTime(){
        java.util.Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-mm-dd");
        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        setDate( dateOnly.format(currentDate.getTime()));
        setTime(timeOnly.format(currentDate.getTime()));

    }
}
