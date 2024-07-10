package com.abztrakinc.ABZPOS.CASHIER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.abztrakinc.ABZPOS.R;

public class cashier_invoice_dialog_cust_info {

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustIDNo() {
        return custIDNo;
    }

    public void setCustIDNo(String custIDNo) {
        this.custIDNo = custIDNo;
    }

    public String getCustTIN() {
        return custTIN;
    }

    public void setCustTIN(String custTIN) {
        this.custTIN = custTIN;
    }

    private String custName=" ";
    private String custIDNo;
    private String custTIN;

    public void showDialog(Context context){

        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_scd_pwd_info, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        EditText et_custName= alertLayout.findViewById(R.id.et_custName);
        EditText et_custIDNo= alertLayout.findViewById(R.id.et_custIDNo);
        EditText et_custTIN= alertLayout.findViewById(R.id.et_custTIN);
        Button btn_saveCustInfo = alertLayout.findViewById(R.id.btn_saveCustInfo);

        btn_saveCustInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                custName=et_custName.getText().toString();
//                custIDNo=et_custIDNo.getText().toString();
//                custTIN=(et_custTIN.getText().toString());

                setCustName(et_custName.getText().toString());
                setCustIDNo((et_custIDNo.getText().toString()));
                setCustTIN((et_custTIN.getText().toString()));
                alertDialog.dismiss();
            }
        });











        alertDialog.show();

    }

    public void getInfo(Context context){



    }

}
