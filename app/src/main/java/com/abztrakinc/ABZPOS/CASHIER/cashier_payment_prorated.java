package com.abztrakinc.ABZPOS.CASHIER;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.R;

public class cashier_payment_prorated {
    public int getNumberOfPax() {
        return numberOfPax;
    }

    public void setNumberOfPax(int numberOfPax) {
        this.numberOfPax = numberOfPax;
    }

    public int getNumberOfCard() {
        return numberOfCard;
    }

    public void setNumberOfCard(int numberOfCard) {
        this.numberOfCard = numberOfCard;
    }

    public Double getMaxDiscAmt() {
        return maxDiscAmt;
    }



    public void setMaxDiscAmt(Double maxDiscAmt) {
        this.maxDiscAmt = maxDiscAmt;
    }
    public Double getDiscountPerCard() {
        return discountPerCard;
    }

    public void setDiscountPerCard(Double discountPerCard) {
        this.discountPerCard = discountPerCard;
    }

    public Double getTotalTransactionDiscount() {
        return totalTransactionDiscount;
    }

    public void setTotalTransactionDiscount(Double totalTransactionDiscount) {
        this.totalTransactionDiscount = totalTransactionDiscount;
    }

    public int getPaxIndicator() {
        return paxIndicator;
    }

    public void setPaxIndicator(int paxIndicator) {
        this.paxIndicator = paxIndicator;
    }

    int paxIndicator;
    int numberOfPax;
    int numberOfCard;
    Double maxDiscAmt;
    Double discountPerCard=0.00;
    Double totalTransactionDiscount=0.00;

    public Double getPricePerPax() {
        return PricePerPax;
    }

    public void setPricePerPax(Double pricePerPax) {
        PricePerPax = pricePerPax;
    }

    Double PricePerPax=0.00;


    public void pro_rated_computation(Context context,String transNo,Double totalAmt,String discountValue){
        showDialogPax(context);
        //setNumberOfCard(read_number_card(context,transNo));
        //setDiscountPerCard(discount_per_card(totalAmt,discountValue));





    }

    public void showDialogPax(Context context){



            AlertDialog alertDialog;
            AlertDialog.Builder builder  = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_pax, null);

            builder.setView(alertLayout);
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            EditText noPax = alertLayout.findViewById(R.id.et_noPax);
            Button btn_saveCustInfo = alertLayout.findViewById(R.id.btn_saveCustInfo);
            Button btn_cancelCustInfo = alertLayout.findViewById(R.id.btn_cancelCustInfo);

            //int showMessage=0;

           // Toast.makeText(getContext(), "Dialog:"+DialogCursor + "  MapActi:"+mapCode2Activate, Toast.LENGTH_SHORT).show();

            btn_saveCustInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // saveSCDInfo();

                    if (noPax.getText().length()!=0){
                        setNumberOfPax(numberOfPax);
                    }
                    else{
                        setNumberOfPax(1);
                    }



                }
            });

            btn_cancelCustInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    alertDialog.dismiss();


                }
            });



            alertDialog.show();


    }
    public Double discount_per_card(Double totalAmt,String discountValue,Double MaxDiscount){
        Double discount=0.00;

        Double totalPerCard=0.00;
        setMaxDiscAmt(MaxDiscount);



         setPricePerPax(totalAmt/getNumberOfPax());
        if (getPricePerPax()>getMaxDiscAmt()){

            if (getMaxDiscAmt().equals(0.0)){
                Log.e("Final", "regular discount" );
                discount = (totalAmt/1.12)*(Double.parseDouble(discountValue)/100)*-1;
            }
            else{
                Log.e("Final", "prorated discount" );
                discount = (getMaxDiscAmt()/1.12)*(Double.parseDouble(discountValue)/100)*-1;
            }
        }
        else{
            if (getMaxDiscAmt().equals(0.0)){
                discount=((getPricePerPax())/1.12)*(Double.parseDouble(discountValue)/100)*-1;
            }
            else{
                discount=((getPricePerPax())/1.12)*(Double.parseDouble(discountValue)/100)*-1;
            }

        }

//        totalPerCard=PricePerPax*getNumberOfCard();
//        discount = (totalPerCard/1.12)*Double.parseDouble(discountValue)*-1;
        Log.e("-----------", "----------DISCOUNT_PER_CARD------------");
        Log.e("Final", "price_per_pax: "+PricePerPax.toString() );
        Log.e("Final", "totalAmt: "+totalAmt.toString() );
        Log.e("Final", "numberOfCard: "+getNumberOfCard() );
        Log.e("Final", "max_disc: "+getMaxDiscAmt() );
        Log.e("Final", "discount_value: "+discountValue.toString() );
        Log.e("Final", "discount_per_card: "+discount.toString() );


        if (getNumberOfCard()==0){
            discount = discount;
        }else{
            discount = discount * getNumberOfCard();
        }


        Log.e("Final", "disc*card: "+discount.toString() );
        Log.e("Final", "----------END DISCOUNT_PER_CARD------------");
        return discount;
    }

    public int read_number_card(Context context,String TransNo){

        int count=0;
        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db2.rawQuery("select Count(ID) from DiscountInfoTbl where TransactionID='"+TransNo+"'", null);
        if (cursor.getCount()!=0){
            if (cursor.moveToFirst()){
                count=  cursor.getInt(0);
                Log.e("TAG", "read_number_card: "+count );
            }
        }
        else{
            count =1;
            Log.e("TAG", "read_number_card: "+count );
        }
        setNumberOfCard(count);
       return count;
    }

    public void insertNewID(Context context,String transaction_ID,String disc_buyer_name,String disc_buyer_id,String disc_other){
        DatabaseHandler myDb = new DatabaseHandler(context);
        boolean isInserted2 = myDb.insertDiscountInfo(
                transaction_ID,disc_buyer_name,disc_buyer_id,disc_other);
        Toast.makeText(context, "ADDITIONAL DISCOUNT CARD ADDED", Toast.LENGTH_LONG).show();

    }

    public void checkPaxIndicator(){
        //1 on
        //0 off
        setPaxIndicator(0);
    }




}
