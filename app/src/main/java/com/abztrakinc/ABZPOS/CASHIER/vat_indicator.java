package com.abztrakinc.ABZPOS.CASHIER;

public class vat_indicator {
    public int getVatIndicator() {
        setVatIndicator(1);
        return vatIndicator;
    }

    public void setVatIndicator(int vatIndicator) {
        //0 non//1 vat
       // vatIndicator=0;
        this.vatIndicator = vatIndicator;
    }

    int vatIndicator;


}
