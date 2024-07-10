package com.abztrakinc.ABZPOS.ADMIN;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class AppService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ClearService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ClearService", "Service Destroyed");
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearService", "END");
        //Code here
        printer_settings_class printerSettings = new printer_settings_class(getApplicationContext());

        StringBuffer buffer = new StringBuffer();
        buffer.append("TERMINAL CLOSED" + "\n");

        buffer.append("=============EXIT POS=============" + "\n");
        //    buffer.append("SHF:"+shift_active.getShiftActive() +"\t\t\t" +"POS:"+shift_active.getPOSCounter() +  "\n");

//        buffer.append("POS:"+shift_active.getPOSCounter() +  "\n");
//        buffer.append(Sysdate.getSystemDate() + "\t" + timeOnly.format(currentDate.getTime()) + "\t"  + "TRANS#: " + transactionNumber +  "\n");
        buffer.append("TERMINAL CLOSED" + "\n");
        buffer.append("==================================" + "\n\n");


        printerSettings.OnlinePrinter(buffer.toString(),1,0,1);

        stopSelf();
    }



}