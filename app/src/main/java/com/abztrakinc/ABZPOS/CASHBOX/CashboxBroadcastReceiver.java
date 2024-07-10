package com.abztrakinc.ABZPOS.CASHBOX;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;


import android.os.Handler;


public class CashboxBroadcastReceiver extends BroadcastReceiver {
    public static Handler mHandlerMsg = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.CASHBOXSTATUS"))
        {
            if (intent.getAction().equals("android.intent.action.CASHBOXSTATUS"))
            {
                Bundle bundle = intent.getExtras();
                if (bundle.getBoolean("cashbox_status"))
                    Log.v("CashboxTest", "cashbox is open");
                else
                    Log.v("CashboxTest", "cashbox is close");
                if (mHandlerMsg != null)
                {
                    Message msg = mHandlerMsg.obtainMessage();
                    if (bundle.getBoolean("cashbox_status"))
                        msg.what = 1;
                    else
                        msg.what = 0;
                    msg.sendToTarget();
                }
				/*if (mHandlerMsg != null)
				{
					Message msg = mHandlerMsg.obtainMessage();
					if (intent.getIntExtra("cashbox_status", 0) == 1)
						msg.what = 1;
					else
						msg.what = 0;
					msg.sendToTarget();
				}*/
            }
        }
    }
}
