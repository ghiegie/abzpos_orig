package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class monthly_sales_report {

    //region variables
    public Double getGrossSales() {
        return grossSales;
    }
    public void setGrossSales(Double grossSales) {
        this.grossSales = grossSales;
    }

    Double grossSales;

    public Double getVatableSales() {
        return vatableSales;
    }

    public void setVatableSales(Double vatableSales) {
        this.vatableSales = vatableSales;
    }

    public Double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Double getVatExempt() {
        return vatExempt;
    }

    public void setVatExempt(Double vatExempt) {
        this.vatExempt = vatExempt;
    }

    public Double getLessVat() {
        return lessVat;
    }

    public void setLessVat(Double lessVat) {
        this.lessVat = lessVat;
    }

    public Double getZeroRated() {
        return zeroRated;
    }

    public void setZeroRated(Double zeroRated) {
        this.zeroRated = zeroRated;
    }

    public Double getSpecialDisc() {
        return specialDisc;
    }

    public void setSpecialDisc(Double specialDisc) {
        this.specialDisc = specialDisc;
    }

    public Double getNormalDisc() {
        return normalDisc;
    }

    public void setNormalDisc(Double normalDisc) {
        this.normalDisc = normalDisc;
    }

    public Double getNetSales() {
        return netSales;
    }

    public void setNetSales(Double netSales) {
        this.netSales = netSales;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }
//endregion
    Double vatableSales,vatAmount,vatExempt,lessVat,zeroRated,specialDisc,normalDisc,netSales,grandTotal;

    public monthly_sales_report(Context context,String from,String to){
        gross_sales(context,from,to);
        vatable_sales(context,from,to);
        discount(context,from,to);
        net_sales(context,from,to);
        grand_total(context,from,to);

    }

    private void gross_sales(Context context,String from,String to){
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

        Double totalGross=0.00;
                Cursor invoiceReceiptItemFinal =db.rawQuery("select * from InvoiceReceiptItemFinal where TransactionDate between '"+from+"' and '"+to+"' ", null);
                if (invoiceReceiptItemFinal.getCount()!=0){

                    while (invoiceReceiptItemFinal.moveToNext()){
                        totalGross+=Double.parseDouble(invoiceReceiptItemFinal.getString(5));
                    }
                }

                setGrossSales(totalGross);

                db.close();



    }
    private void vatable_sales(Context context,String from,String to){
        vatableSales=0.00;
        vatAmount=0.00;
        vatExempt=0.00;
        zeroRated=0.00;
        lessVat=0.00;
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadOrderPriceTotal =db.rawQuery("select sum(TotalVatableSales),sum(TotalVatAmount),sum(TotalVatExempt),sum(TotalZeroRatedSales),sum(TotalLessVat) from FinalTransactionReport where TransDate between '"+from+"' and '"+to+"'", null);
        //  Cursor cursorReadOrderPriceTotalDiscount =db.rawQuery("select sum(DiscAmount) from FinalTransactionReport where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        // Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'", null);
        if (cursorReadOrderPriceTotal.getCount()!=0){
            cursorReadOrderPriceTotal.moveToFirst();
            //   Toast.makeText(context, cursorReadOrderPriceTotal.getString(0), Toast.LENGTH_SHORT).show();
            if (cursorReadOrderPriceTotal.getString(0)==null){

                vatableSales=0.00;

            }
            else{

                vatableSales = Double.parseDouble(cursorReadOrderPriceTotal.getString(0));
                vatAmount= Double.parseDouble(cursorReadOrderPriceTotal.getString(1));
                vatExempt=Double.parseDouble(cursorReadOrderPriceTotal.getString(2));
                zeroRated=Double.parseDouble(cursorReadOrderPriceTotal.getString(3));
                lessVat=Double.parseDouble(cursorReadOrderPriceTotal.getString(4));

            }

//            Double vatablePercent =1.12;
//            Double taxPercent=.12;
//            Double vatableAmount= totalVatablePrice / vatablePercent;
//

          //  totalVatablesSales=totalVatablePrice;

            setVatableSales(vatableSales);
            setVatAmount(vatAmount);
            setVatExempt(vatExempt);
            setZeroRated(zeroRated);
            setLessVat(lessVat);


        }

        db.close();

    }
    private void discount(Context context,String from,String to){
        specialDisc=0.00;
        normalDisc=0.00;
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor InvoiceReceiptItemFinalWDiscount =db.rawQuery("select DiscountType,DiscAmount from InvoiceReceiptItemFinalWDiscount where TransactionDate between '"+from+"' and '"+to+"' ", null);
        if (InvoiceReceiptItemFinalWDiscount.getCount()!=0){
            while (InvoiceReceiptItemFinalWDiscount.moveToNext()){

                SQLiteDatabase DiscountList= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
                Cursor DiscountType =DiscountList.rawQuery("select DiscountType from DiscountList where DiscountID='"+InvoiceReceiptItemFinalWDiscount.getString(0)+"'", null);
                if (DiscountType.getCount()!=0){
                    while (DiscountType.moveToNext()){
                        if (DiscountType.getString(0).trim().equals("SPECIAL DISCOUNT")){
                            specialDisc+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
                        }
                        if (DiscountType.getString(0).trim().equals("NORMAL DISCOUNT")){
                            normalDisc+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
                        }
                    }
                }
                setSpecialDisc(specialDisc);
                setNormalDisc(normalDisc);

                DiscountList.close();



            }
        }
    }
    private void net_sales(Context context,String from,String to){
//        netSales=grossSales-lessVat-specialDisc-normalDisc-vatAmount;

//        setNetSales(netSales);

    }
    private void grand_total(Context context,String from,String to){
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        grandTotal=0.00;
        // Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice'", null);
        Cursor cursorReadingEndingBalance = db.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '"+from+"' and '"+to+"'", null);
        // Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where typeOfTransaction='invoice'"
        if (cursorReadingEndingBalance.moveToFirst()) {
            do {
                // Get the total amount as a string from the cursor
                String totalAmountString = String.valueOf(cursorReadingEndingBalance.getDouble(0));

                // Check if the total amount string is null or empty
                if (totalAmountString == null || totalAmountString.isEmpty()) {
                    // Handle the case where the total amount is not available
                    System.out.println("Total amount is not available for this record.");
                } else {
                    // Try to parse the total amount string to double
                    try {
                        double totalAmount = Double.parseDouble(totalAmountString);
                        // Add the parsed value to the grand total
                        grandTotal += totalAmount;
                    } catch (NumberFormatException e) {
                        // Handle the case where the total amount is not a valid numeric value
                        System.out.println("Invalid total amount value: " + totalAmountString);
                    }
                }
            } while (cursorReadingEndingBalance.moveToNext());
        } else {
            // Handle the case where no records are found
            grandTotal = 0.00;
        }



        setGrandTotal(grandTotal);

    }


}
