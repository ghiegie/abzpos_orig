package com.abztrakinc.ABZPOS;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abztrakinc.ABZPOS.LOGIN.splash_screen;

import java.util.List;

public class startupOnBootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//
//            Intent activityIntent = new Intent(context, com.abztrakinc.ABZPOS.LOGIN.splash_screen.class);
//
//            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            context.startActivity(activityIntent);
//        }

//        if (!isAppRunning(context)) {
//            Intent startIntent = new Intent(context,  com.abztrakinc.ABZPOS.LOGIN.splash_screen.class);
//            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(startIntent);
//        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, splash_screen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }




    }

//    private boolean isAppRunning(Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (activityManager != null) {
//            List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();
//            if (processInfoList != null) {
//                for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
//                    if (processInfo.processName.equals(context.getPackageName())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
}