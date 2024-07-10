package com.abztrakinc.ABZPOS.CASHIER;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DecimalFormat;

public class cashier_payment_item {

    String cashTender;
    double totalAmountToPay;
    double totalAmountRender;
    double vatableAmount;
    double vatablePercent;
    double taxAmount;
    double taxPercent;
    double totalVatExempt;

    public double getZeroRatedSales() {
        return zeroRatedSales;
    }

    public void setZeroRatedSales(double zeroRatedSales) {
        this.zeroRatedSales = zeroRatedSales;
    }

    double zeroRatedSales=0.00;
    Context context;
    double regularDiscount;
    String item1Total;
    String item2Total;
    double amountDiscount;
    String transactionIdFinal;

    public String getNegVat() {
        return negVat;
    }

    public void setNegVat(String negVat) {
        this.negVat = negVat;
    }

    String negVat;




    public double getVatExemptSale() {
        return vatExemptSale;
    }

    public void setVatExemptSale(double vatExemptSale) {
        this.vatExemptSale = vatExemptSale;
    }

    double vatExemptSale;

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    double serviceCharge;

    public Double getTotalDueAmount() {
        return totalDueAmount;
    }

    public void setTotalDueAmount(Double totalDueAmount) {
        this.totalDueAmount = totalDueAmount;
    }

    Double totalDueAmount=0.00;


    public double getLessVat() {
        return lessVat;
    }

    public void setLessVat(double lessVat) {
        this.lessVat = lessVat;
    }

    double lessVat;



    String TransactionID,OrderID,OrderName,OrderQty,OrderPrice,OrderPriceTotal,TransactionTime,TransactionDate,DiscountType,ItemRemarks;
    String DiscQty,DiscAmount,DiscPercent,DiscBuyerName,DiscIdNumber,DiscOther;



    public cashier_payment_item(Context context) {



    }
    Double totalVat=0.00;
    Double totalOrderPriceSCD=0.00;
    String DiscountExcludeTax="";
    String SalesExcludeTax="";
    String ProRated="";
    Double totalOrderPriceotherDisc=0.00;
    Double totalOrderDiplomat=0.00;
    String RemoveDiscountOnVatsales ="YES";
    Double totalOrderAllDiscount=0.00;
    Double totalDiscountAll=0.00;


    Double totalDiscountInTemp=0.00;

    int diplomatIndicator;
    public void loadCashierPaymentDataTemp(Context context,int vatIndicator){
        String DB_NAME2 = "PosOutputDB.db";
        String DB_NAME = "settings.db";
        //  db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        SQLiteDatabase db2 = context.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        SQLiteDatabase db= context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        // String id=orderItemList.get(position).getItemId() ;
        Cursor item1 = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
        Cursor item2 = db2.rawQuery("select sum(DiscAmount),sum(OrderPriceTotal) from InvoiceReceiptItemFinalWDiscountTemp", null);



        if (item1.moveToFirst()) {
            if (item1.getCount() != 0) {
                if (item1.getString(0) == null) {
                    item1Total = "0.00";
                } else {
                    double FinalAmount = Double.valueOf(item1.getString(0));
                    DecimalFormat format = new DecimalFormat("0.00");
                    String formatted = format.format(FinalAmount);
                    item1Total = formatted;



                    //totAmountDue.setText(String.valueOf(formatted));
                    //tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
                }
            }
        }
        if (item2.moveToFirst()) {


            if (item2.getCount() != 0) {
                if (item2.getString(0) == null) {
                    item2Total = "0.00";
                } else {
                    double FinalAmount = Double.valueOf(item2.getString(0));
                    DecimalFormat format = new DecimalFormat("0.00");
                    String formatted = format.format(FinalAmount);
                    item2Total = formatted;
                    Log.e("FINAL DISCOUNT","AMOUNT "+ String.valueOf(formatted));

                    double FinalOrderPrice=Double.parseDouble(item2.getString(1));
                    String formatted2=format.format(FinalOrderPrice);
                    totalDiscountInTemp=Double.parseDouble(formatted2);


                    //  diplomatIndicator=0;
                    //totAmountDue.setText(String.valueOf(formatted));
                    //tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
                }
            }
        }

        amountDiscount=Double.valueOf(item2Total);
        totalAmountToPay=(Double.valueOf(item1Total));




        double vatableAmountFinal = (totalAmountToPay-totalDiscountInTemp)/1.12;
        DecimalFormat formatv = new DecimalFormat("0.00");
        String formattedvat = formatv.format(vatableAmountFinal);
        vatableAmount=Double.parseDouble(formattedvat);  // vatable sales

        String formattedTax=formatv.format(vatableAmount*.12);
        taxAmount=Double.parseDouble(formattedTax);
        Log.e("First Tax Amt",String.valueOf(taxAmount));
        totalDueAmount=(totalAmountToPay);







        Cursor DiscTypeChecker =  db2.rawQuery("select DiscountType from InvoiceReceiptItemFinalWDiscountTemp", null);
        if (DiscTypeChecker.getCount()!=0){
            while (DiscTypeChecker.moveToNext()){
                if (!DiscTypeChecker.getString(0).trim().equals("DIPLOMAT")) {
                    Log.d("PAYMENT ITEM","NOT DIPLOMAT");


                    Cursor DiscCategoryChecker = db.rawQuery("select DiscCategory,DiscountExcludeTax,SalesExcludeTax,ProRated,MaxDiscountAmount from DiscountList where DiscountID='" + DiscTypeChecker.getString(0) + "'", null);
                    if (DiscCategoryChecker.getCount() != 0) {


                        while (DiscCategoryChecker.moveToNext()) {

                            DiscountExcludeTax=DiscCategoryChecker.getString(1).trim();
                            SalesExcludeTax=DiscCategoryChecker.getString(2).trim();
                            ProRated=DiscCategoryChecker.getString(3);
                            Log.e("DISCOUNT EXCLUDE TAX",DiscountExcludeTax);
                            Log.e("SALES EXCLUDE TAX",SalesExcludeTax);

                            if (DiscCategoryChecker.getString(0).equals("SCD")) {
                                Cursor item3 = db2.rawQuery("select sum(OrderPriceTotal),sum(VAT),sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp where  DiscountType='" + DiscTypeChecker.getString(0) + "'", null); // total order for discount temp

                                if (item3.moveToFirst()) {





                                    if (item3.getCount() != 0) {
                                        if (item3.getString(0) == null) {
                                            // totalOrderPriceSCD = 0.00;

                                        } else {
                                            Log.e("--","---------------");
                                            Log.e("DISCOUNT", "SCD GOODS");
                                            DecimalFormat format = new DecimalFormat("0.00");

                                            double FinalAmount = Double.valueOf(item3.getString(0));
                                            String formatted = format.format(FinalAmount);
                                            totalOrderPriceSCD= Double.parseDouble(formatted);
                                            String vatFormatted = format.format(Double.parseDouble(item3.getString(1)));
                                            totalVat=Double.parseDouble(vatFormatted);
                                            Log.e("SCD TOTAL", String.valueOf(totalOrderPriceSCD));
                                            Log.e("--","---------------");
                                            totalOrderAllDiscount=totalOrderPriceSCD;
                                            //  String Discount=format.format());
                                            double DiscountDouble=Double.parseDouble((item3.getString(2)));
                                            totalDiscountAll=Double.parseDouble(format.format(DiscountDouble));


                                            diplomatIndicator = 0;






                                            //totAmountDue.setText(String.valueOf(formatted));
                                            //tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
                                        }
                                    }


                                }

                                ;







                            }
                            else if (DiscCategoryChecker.getString(0).equals("PWD")) {

                                Cursor item4 = db2.rawQuery("select sum(OrderPriceTotal),sum(VAT),sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp where  DiscountType='" + DiscTypeChecker.getString(0) + "'", null); // total order for discount temp
                                if (item4.moveToFirst()) {


                                    if (item4.getCount() != 0) {
                                        if (item4.getString(0) == null) {
                                            //totalOrderPriceSCD = 0.00;
                                            // Log.e("pwd",String.valueOf(totalOrderPriceSCD));
                                        } else {
                                            Log.e("--","---------------");
                                            Log.e("DISCOUNT", "PWD GOODS");
//                                            double FinalAmount = Double.valueOf(item4.getString(0));
//                                            DecimalFormat format = new DecimalFormat("0.00");
//                                            String formatted = format.format(FinalAmount);
//                                            totalOrderPriceSCD += Double.parseDouble(formatted);
//                                            diplomatIndicator = 0;
//                                            Log.e("pwd", String.valueOf(totalOrderPriceSCD));
//                                            String vatFormatted = format.format(Double.parseDouble(item4.getString(1)));
//                                            totalVat+=Double.parseDouble(vatFormatted);
                                            double FinalAmount = Double.valueOf(item4.getString(0));
                                            DecimalFormat format = new DecimalFormat("0.00");
                                            String formatted = format.format(FinalAmount);
                                            totalOrderPriceSCD = Double.parseDouble(formatted);
                                            String vatFormatted = format.format(Double.parseDouble(item4.getString(1)));
                                            totalVat=Double.parseDouble(vatFormatted);
                                            Log.e("PWD TOTAL", String.valueOf(totalOrderPriceSCD));
                                            Log.e("--","---------------");
                                            totalOrderAllDiscount=totalOrderPriceSCD;
                                            //String Discount=format.format((item4.getString(2)));

                                            double DiscountDouble=Double.parseDouble((item4.getString(2)));
                                            totalDiscountAll=Double.parseDouble(format.format(DiscountDouble));




                                        }
                                    }
                                }




                            }
                            else if (DiscCategoryChecker.getString(0).equals("REG")) {

                                Cursor item6 = db2.rawQuery("select sum(OrderPriceTotal),sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp where DiscountType='" + DiscTypeChecker.getString(0) + "'", null); // total order for discount temp
                                if (item6.moveToFirst()) {


                                    if (item6.getCount() != 0) {
                                        if (item6.getString(0) == null) {
                                            //totalOrderPriceSCD = 0.00;
                                            // Log.e("pwd",String.valueOf(totalOrderPriceSCD));
                                        } else {
                                            Log.e("--","---------------");
                                            double FinalAmount = Double.valueOf(item6.getString(0));
                                            DecimalFormat format = new DecimalFormat("0.00");
                                            String formatted = format.format(FinalAmount);
                                            totalOrderPriceotherDisc= Double.parseDouble(formatted);
                                            diplomatIndicator=0;
                                            Log.e("REGULAR TOTAL",String.valueOf(totalOrderPriceotherDisc));
                                            //totAmountDue.setText(String.valueOf(formatted));
                                            //tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
                                            Log.e("--","---------------");
                                            totalOrderAllDiscount=totalOrderPriceotherDisc;
                                            double DiscountDouble=Double.parseDouble((item6.getString(1)));
                                            totalDiscountAll=Double.parseDouble(format.format(DiscountDouble));


                                        }
                                    }
                                }

                            }


                            // if (diplomatIndicator==0){
                            if (DiscountExcludeTax.equals("YES")){
                                //discTransDiscount= (discTransTotal / 1.12 )* (Double.parseDouble(discountValue)/100)*-1;
                                Log.e("DISCOUNT EXLUDE","YES");

                                lessVat=totalVat*-1;

                                // vatExemptSale=0.00;

                                vatablePercent =1.12;
                                taxPercent=.12; //vat

                                //  vatExemptSale=(totalOrderPriceSCD/1.12)-(-amountDiscount);
                                vatExemptSale=(totalOrderPriceSCD/1.12)+amountDiscount;
                                // totalVatExempt=+vatExemptSale;

                                Log.e("Amount To Pay ",String.valueOf(totalAmountToPay));
                                Log.e("Discount ",String.valueOf(amountDiscount));
                                Log.e("Less Vat ",String.valueOf(lessVat));


                            }
                            else if(DiscountExcludeTax.equals("NO") && ProRated.equals("NO")){
                                Log.e("DISCOUNT EXLUDE","NO");
                                lessVat+=0.00;
//                                    amountDiscount =Double.valueOf(item2Total);
                                vatExemptSale+=0.00;
//                                    totalAmountToPay=(Double.valueOf(item1Total));
                                vatablePercent =1.12;
                                taxPercent=.12; //vat

                                if (RemoveDiscountOnVatsales.equals("YES")){
                                    Log.e("REMOVE DISC ON VAT","YES");
                                    //  vatableAmount+=(totalOrderPriceSCD-totalOrderPriceotherDisc-(-amountDiscount))/1.12;

                                    double vatableAmountFinal2 = (totalAmountToPay-(-totalDiscountAll))/1.12;
                                    Log.e("totalOrderAllDiscount",String.valueOf(totalOrderAllDiscount));
                                    Log.e("TotalDiscountAll",String.valueOf(totalDiscountAll));

                                    DecimalFormat formatv2 = new DecimalFormat("0.00");
                                    String formattedvat2 = formatv2.format(vatableAmountFinal2);
                                    vatableAmount=Double.parseDouble(formattedvat2);  // vatable sale



                                    //vatableAmount+=;
                                    // totalOrderPriceotherDisc
                                }
                                else{


                                    double vatableAmountFinal2 = (totalOrderAllDiscount)/1.12;
                                    DecimalFormat formatv2 = new DecimalFormat("0.00");
                                    String formattedvat2 = formatv2.format(vatableAmountFinal2);
                                    vatableAmount+=Double.parseDouble(formattedvat2);  // vatable sale


                                    Log.e("REMOVE DISC ON VAT","NO");
                                    //vatableAmount+=;
                                }


                                // vatableAmount=0.00;  // vatable sales
                                taxAmount=(vatableAmount*.12);



                                Log.e("Total Amount",String.valueOf(totalDueAmount));
                                Log.e("Amount To Pay ",String.valueOf(totalAmountToPay));
                                Log.e("Discount ",String.valueOf(amountDiscount));
                                Log.e("Less Vat ",String.valueOf(lessVat));


                            }
                            else if(ProRated.equals("YES")){
                                Log.e("ProRated","YES");


                                lessVat+=0.00;
                                vatExemptSale+=0.00;
                                vatablePercent =1.12;
                                taxPercent=.12; //vat
                                double vatableAmountFinal2;
                                if (SalesExcludeTax.equals("YES")){

                                    vatableAmountFinal2 = (((totalAmountToPay-(-amountDiscount))/1.12));
                                    taxAmount = (vatableAmountFinal2*.12);
                                }
                                else{
                                    taxAmount = ((totalAmountToPay/1.12)*.12);
                                    vatableAmountFinal2 = ((totalAmountToPay-(-amountDiscount))-taxAmount);
                                }

                            //   // double vatableAmountFinal2 = ((totalAmountToPay-(-amountDiscount))-taxAmount);
                             //   Log.e("totalOrderAllDiscount",String.valueOf(totalOrderAllDiscount));
                              //  Log.e("TotalDiscountAll",String.valueOf(totalDiscountAll));

                                DecimalFormat formatv2 = new DecimalFormat("0.00");
                                String formattedvat2 = formatv2.format(vatableAmountFinal2);
                                vatableAmount=Double.parseDouble(formattedvat2);  // vatable sale



                                //vatableAmount+=;
                                // totalOrderPriceotherDisc
//                                }
//                                else{
//
//
//                                    double vatableAmountFinal2 = (totalOrderAllDiscount)/1.12;
//                                    DecimalFormat formatv2 = new DecimalFormat("0.00");
//                                    String formattedvat2 = formatv2.format(vatableAmountFinal2);
//                                    vatableAmount+=Double.parseDouble(formattedvat2);  // vatable sale
//
//
//                                    Log.e("REMOVE DISC ON VAT","NO");
//                                    //vatableAmount+=;
//                                }


                                // vatableAmount=0.00;  // vatable sales
                               // taxAmount=(vatableAmount*.12);



                                Log.e("Total Amount",String.valueOf(totalDueAmount));
                                Log.e("Amount To Pay ",String.valueOf(totalAmountToPay));
                                Log.e("Discount ",String.valueOf(amountDiscount));
                                Log.e("Less Vat ",String.valueOf(lessVat));


                            }
                            else{
                                Log.e("DISCOUNT EXLUDE","ELSE");
//                lessVat=0.00;
//                amountDiscount = 0.00;
//                vatExemptSale=0.00;
//                totalAmountToPay=0.00;
//                vatablePercent =1.12;
//                taxPercent=.12; //vat
//                vatableAmount=0.00;  // vatable sales
//                taxAmount=0.00;
//                totalDueAmount=(totalAmountToPay-totalOrderPriceSCD)+(totalOrderPriceSCD-amountDiscount-lessVat);
                                lessVat=totalVat;
                                amountDiscount = Double.valueOf(item2Total);
                                vatExemptSale=totalOrderPriceSCD/1.12;
                                totalAmountToPay=(Double.valueOf(item1Total));
                                vatablePercent =1.12;
                                taxPercent=.12; //vat
                                vatableAmount=(totalAmountToPay-totalOrderPriceotherDisc-totalOrderPriceSCD)/1.12;  // vatable sales
                                taxAmount=((totalAmountToPay-totalOrderPriceSCD-totalOrderPriceotherDisc)/vatablePercent)*taxPercent;
                                totalDueAmount=(totalAmountToPay-totalOrderPriceSCD)+(totalOrderPriceSCD-amountDiscount-lessVat);

                            }
                            //}



                        }
                        totalDueAmount=totalAmountToPay-(-amountDiscount)-lessVat;
//                        vatableAmount=(totalAmountToPay/1.12)-vatExemptSale;
//                        taxAmount=vatableAmount*.12;
                    }
                }
                else{
                    Log.d("PAYMENT ITEM","DIPLOMAT");
                    Cursor item5 = db2.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptItemFinalWDiscountTemp where  DiscountType  LIKE '%DIPLOMAT%'", null); // total order for discount temp
                    if (item5.moveToFirst()) {


                        if (item5.getCount() != 0) {
                            if (item5.getString(0) == null) {
                                totalOrderDiplomat = 0.00;
                                Log.e("DISCOUNT TYPE ITEM","0");
                            } else {
                                //double lessVat=
                                double FinalAmount = Double.valueOf(item5.getString(0));
                                DecimalFormat format = new DecimalFormat("0.00");
                                String formatted = format.format(FinalAmount);
                                totalOrderDiplomat= Double.parseDouble(formatted);
                                diplomatIndicator=1;
                                Log.e("DISCOUNT TYPE ITEM","1");
                                //totAmountDue.setText(String.valueOf(formatted));
                                //tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
                            }
                        }
                    }
                }




            }


            if (diplomatIndicator==1){

                lessVat=totalOrderDiplomat/1.12*.12;
                amountDiscount =0.00;
                vatExemptSale=0.00;
                totalAmountToPay=(Double.valueOf(item1Total));
                vatablePercent =0.00;
                taxPercent=0.00; //vat
                vatableAmount=0.00;
                taxAmount=0.00;

                zeroRatedSales=totalOrderDiplomat/1.12;
                totalDueAmount=(totalAmountToPay-totalOrderDiplomat)+(totalOrderDiplomat-amountDiscount-lessVat);



            }

        }
        else{
//            lessVat=totalVat;
//            amountDiscount = Double.valueOf(item2Total);
//            vatExemptSale=totalOrderPriceSCD/1.12;
//            totalAmountToPay=(Double.valueOf(item1Total));
//            vatablePercent =1.12;
//            taxPercent=.12; //vat
//            vatableAmount=(totalAmountToPay-totalOrderPriceotherDisc-totalOrderPriceSCD)/1.12;  // vatable sales
//            taxAmount=((totalAmountToPay-totalOrderPriceSCD-totalOrderPriceotherDisc)/vatablePercent)*taxPercent;
//            totalDueAmount=(totalAmountToPay);
        }


        if (vatIndicator==0){
            Log.e("TAG", "loadCashierPaymentDataTemp: "+String.valueOf(vatIndicator) );
           // vatableAmount=0.00;
            taxAmount=0.00;
            //setVatableAmount(vatableAmount);
            vatableAmount=totalAmountToPay;
        }
       // Log.e("TAG", "loadCashierPaymentDataTemp: "+String.valueOf(vatIndicator) );






        //scd computation









        //end of scd computation





        db2.close();
    }


    public String getItem1Total() {
        return item1Total;
    }

    public void setItem1Total(String item1Total) {
        this.item1Total = item1Total;
    }

    public String getItem2Total() {
        return item2Total;
    }

    public void setItem2Total(String item2Total) {
        this.item2Total = item2Total;
    }

    public double getRegularDiscount() {
        return regularDiscount;
    }

    public void setRegularDiscount(double regularDiscount) {
        this.regularDiscount = regularDiscount;
    }


    public double getVatablePercent() {
        return vatablePercent;
    }

    public void setVatablePercent(double vatablePercent) {
        this.vatablePercent = vatablePercent;
    }
    public String getCashTender() {
        return cashTender;
    }

    public void setCashTender(String cashTender) {


        this.cashTender = cashTender;
    }

    public double getTotalAmountToPay() {
        // totalAmountToPay=20.02;
        return totalAmountToPay;
    }

    public void setTotalAmountToPay(double totalAmountToPay) {

        this.totalAmountToPay = totalAmountToPay;
    }

    public double getTotalAmountRender() {
        return totalAmountRender;
    }

    public void setTotalAmountRender(double totalAmountRender) {
        this.totalAmountRender = totalAmountRender;
    }

    public double getVatableAmount() {
        return vatableAmount;
    }

    public void setVatableAmount(double vatableAmount) {
        this.vatableAmount = vatableAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(double amountDiscount) {
        this.amountDiscount = amountDiscount;
    }


    //for final transaction record

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public String getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(String orderQty) {
        OrderQty = orderQty;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderPriceTotal() {
        return OrderPriceTotal;
    }

    public void setOrderPriceTotal(String orderPriceTotal) {
        OrderPriceTotal = orderPriceTotal;
    }

    public String getTransactionTime() {
        return TransactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        TransactionTime = transactionTime;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getItemRemarks() {
        return ItemRemarks;
    }

    public void setItemRemarks(String itemRemarks) {
        ItemRemarks = itemRemarks;
    }

    // for final record with discount


    public String getDiscQty() {
        return DiscQty;
    }

    public void setDiscQty(String discQty) {
        DiscQty = discQty;
    }

    public String getDiscAmount() {
        return DiscAmount;
    }

    public void setDiscAmount(String discAmount) {
        DiscAmount = discAmount;
    }

    public String getDiscPercent() {
        return DiscPercent;
    }

    public void setDiscPercent(String discPercent) {
        DiscPercent = discPercent;
    }

    public String getDiscBuyerName() {
        return DiscBuyerName;
    }

    public void setDiscBuyerName(String discBuyerName) {
        DiscBuyerName = discBuyerName;
    }

    public String getDiscIdNumber() {
        return DiscIdNumber;
    }

    public void setDiscIdNumber(String discIdNumber) {
        DiscIdNumber = discIdNumber;
    }

    public String getDiscOther() {
        return DiscOther;
    }

    public void setDiscOther(String discOther) {
        DiscOther = discOther;
    }

    public String getTransactionIdFinal() {
        return transactionIdFinal;
    }

    public void setTransactionIdFinal(String transactionIdFinal) {
        this.transactionIdFinal = transactionIdFinal;
    }



    public void readReferenceNumber(Context context) {

        //primary key 00000001

        // int readPK;
        String DB_NAME2 = "PosOutputDB.db";
        //  db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        SQLiteDatabase db2 = context.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);

        Cursor itemlist = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal ORDER BY TransactionID DESC LIMIT 1 ", null);
        if (itemlist.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%010d", origRefNumber);
            setTransactionIdFinal(formatted);
        } else {

            // Cursor itemListC = db2.rawQuery("SELECT * FROM FinalReceipt", null);
            //while(itemListC.moveToLast()){
            while (itemlist.moveToNext()){
                //origRefNumber = 1;

                int readPK = Integer.parseInt(itemlist.getString(0));

                int incrementPK = readPK + 1;
                String incrementPKString = String.format("%010d", incrementPK);
                setTransactionIdFinal(incrementPKString);
            }





            // }
        }
        itemlist.close();


    }



}
