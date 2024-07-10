package com.abztrakinc.ABZPOS.ADMIN;

import android.text.Html;

public class POS_TYPE {

    public String getBusiness_mode() {


         business_mode= (
                 "--------------------------------\n"
                +"    S A L E S  I N V O I C E    \n" +
                 "--------------------------------\n");
        return business_mode;
    }

    public void setBusiness_mode(String business_mode) {


        //sales of services

        this.business_mode = business_mode;
    }

    public String getReceipt_type() {
        getBusiness_mode();
        if (business_mode.equals("SALES INVOICE"))
        {
            receipt_type="INVOICE";
        }
        else{
            receipt_type="INVOICE";
        }
        return receipt_type;
    }

    public void setReceipt_type(String receipt_type) {
        this.receipt_type = receipt_type;
    }

    String business_mode;
    String receipt_type;
}
