package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.abztrakinc.ABZPOS.R;

public class admin_field_authorized {

    public Integer getCashInAuthorization() {
        setCashInAuthorization(0);
        return CashInAuthorization;
    }

    public void setCashInAuthorization(Integer cashInAuthorization) {
        ; //if 1 need authorize if 0 no need
        CashInAuthorization = cashInAuthorization;
    }

    private Integer CashInAuthorization;


    private String adminID;
    private String adminPass;
    public void Authorization(Context context){
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
       // LayoutInflater inflater = context.getApplicationContext().getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_authorization, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {

                return i == KeyEvent.KEYCODE_BACK;
                // return false;
            }
        });


        alertDialog.show();

    }


}
