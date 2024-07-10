package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class system_final_date {


    public String getSystemDate() {
       // SystemDate=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        return SystemDate;




    }

    public void setSystemDate(String systemDate) {
        SystemDate = systemDate;
    }

    private String SystemDate;

    public String getSystemDate2() {
       // SystemDate2=new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        return SystemDate2;
    }

    public void setSystemDate2(String systemDate2) {
        SystemDate2 = systemDate2;
    }

    private String SystemDate2;


    public void insertDate(Context context){

        SQLiteDatabase db3 = context.getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
       // system_final_date systemDate = new system_final_date();
        Cursor sysdateCursor = db3.rawQuery("select * from SystemDate", null);
        if (sysdateCursor.getCount()==0){
            //SystemDate=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
//            String BizDate="01/01/0001";
//            String PrevBizDate="01/01/0001";

            String BizDate="0001-01-01";
            String PrevBizDate="0001-01-01";
           // SystemDate2=new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
            settingsDB settingsDB = new settingsDB(context.getApplicationContext());
            settingsDB.insertDate("1",BizDate,PrevBizDate,"x");
            Log.e("DATE",PrevBizDate);
        }
        else{
            while(sysdateCursor.moveToNext()){
                SystemDate=sysdateCursor.getString(1);
                SystemDate2 =sysdateCursor.getString(2);
            }
            Log.e("DATE","PRESENT");
        }






    }


}
