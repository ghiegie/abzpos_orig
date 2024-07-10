package com.abztrakinc.ABZPOS.ADMIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_plu_model;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.system_final_date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class admin_stock_transfer_out extends AppCompatActivity {

    List<String> items = new ArrayList<>();
    Cursor c;
    SQLiteDatabase db;
    String DB_NAME = "POSAndroidDB.db";
    Button btn_select,btn_cancel,btn_process;
    TextView tv_itemID,tv_itemName,tv_itemDescription,tv_date,tv_time,tv_transactionNo,tv_transUser;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("hh:mm aa");
    BroadcastReceiver _broadcastReceiver;
    EditText et_quantity,et_reason;
    Spinner spinnerItem;
    Spinner spinnerRemarks;
    String itemID,remarksType,reason;

    ArrayList<String>TransactionNoList = new ArrayList<>();
    ArrayList<String>ItemNameList = new ArrayList<>();
    ArrayList<String>ItemQtyList = new ArrayList<>();
    ArrayList<String>ItemRemarkList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stock_transfer_out);


        remarksType="Transfer Out";
        loadItemID();
        //loadSpinnerItem();
       // loadSpinnerRemarks();
        loadSpinnerReason();
        loadButtonSelect();
        loadData();



//        btn_cancel =findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        btn_process= findViewById(R.id.btn_process);
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showProceedCancelDialog();
               // insertReceivingData();

               // Toast.makeText(admin_stock_receiving.this, TransactionNoList.toString(), Toast.LENGTH_SHORT).show();
              //  loadTotalReport();
                onBackPressed();
            }
        });

        Button btn_showStockCard = findViewById(R.id.btn_showStockCard);
        btn_showStockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(admin_stock_receiving.this);
//
//                // Inflate the layout file associated with admin_stock_card
//                LayoutInflater inflater = getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.activity_admin_stock_card, null);
//
//                // Set the custom view of the AlertDialog to the inflated layout
//                builder.setView(dialogView);
//
//                // Create and show the AlertDialog
//                AlertDialog dialog = builder.create();
//                dialog.show();

                startActivity(new Intent(view.getContext(), admin_stock_card.class));

            }
        });




        loadTransNo();
        DateTime();
        loadTransUser();


    }

    private void loadTotalReport(){


        if (TransactionNoList.size()!=0){


            AlertDialog.Builder builder = new AlertDialog.Builder(admin_stock_transfer_out.this);

            // Inflate the layout file associated with admin_stock_card
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_admin_stock_receiving_total_summary, null);

            // Set the custom view of the AlertDialog to the inflated layout
            builder.setView(dialogView);

            TextView tv_transactionList = dialogView.findViewById(R.id.tv_transactionList);
            TextView tv_itemNameList = dialogView.findViewById(R.id.tv_itemNameList);
            TextView tv_quantityList = dialogView.findViewById(R.id.tv_quantityList);
            TextView tv_remarksList = dialogView.findViewById(R.id.tv_remarksList);


            String Transaction="";
            String ItemName="";
            String quantity="";
            String remark="";
            for (int x=0;x<TransactionNoList.size();x++){
                Transaction += TransactionNoList.get(x) + "\n";
                ItemName += ItemNameList.get(x) + "\n";
                quantity += ItemQtyList.get(x) + "\n";
                remark += ItemRemarkList.get(x) + " \n";

            }

            tv_transactionList.setText(Transaction);
            tv_itemNameList.setText(ItemName);
            tv_quantityList.setText(quantity);
            tv_remarksList.setText(remark);




            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }



    }

    private void loadItemID(){
        db = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        items.clear();


        c = db.rawQuery("select * from ITEM", null);



        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
                items.add(c.getString(0));
            }
        }

        c.close();

    }


    //region recyclerview




    RecyclerView rv_pluListReceiving;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    ArrayList<invoice_plu_model> ButtonList = new ArrayList<>();
    invoice_plu_model po2=null;
    ArrayList<String>   itemCode = new ArrayList<>();
    ArrayList<String>   itemName = new ArrayList<>();
    ArrayList<String>   recptDesc = new ArrayList<>();
    ArrayList<String>   itemPrice = new ArrayList<>();
    ArrayList<String>   itemQuantity = new ArrayList<>();
    ArrayList<String>   itemVatIndicator=new ArrayList<>();
    ArrayList<String>   priceOverride =new ArrayList<>();




    int x=0;
    private void loadData(){
        db = this.getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from ITEM", null);
        if (c.getCount()!=0){
            while (c.moveToNext()){
                itemCode.add(c.getString(0));
                itemName.add(c.getString(1));
                recptDesc.add(c.getString(2));
                itemPrice.add(c.getString(3));
                itemQuantity.add(c.getString(4));
                itemVatIndicator.add(c.getString(15));
                priceOverride.add(c.getString(16));
                x++;
            }
        }

        Log.e("TAG", "loadData: "+String.valueOf(x));


        for(int xctr=0;xctr<x;xctr++) {
            // String buttonID,String itemCode,String itemName,String recptDesc,String itemPrice,String itemQty,String itemVatIndicator,String priceOverride
            po2 = new invoice_plu_model(String.valueOf(xctr + 1), itemCode.get(xctr), itemName.get(xctr), recptDesc.get(xctr), itemPrice.get(xctr), itemQuantity.get(xctr), itemVatIndicator.get(xctr), priceOverride.get(xctr));
            ButtonList.addAll(Arrays.asList(new invoice_plu_model[]{po2}));

        }


        refreshPLURecyclerview();





    }

    private void refreshPLURecyclerview(){






        rv_pluListReceiving = findViewById(R.id.rv_pluListReceiving);

        rv_pluListReceiving.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.removeAllViews();
        rv_pluListReceiving.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
        //rv_ItemMasterList.setLayoutManager(layoutManager);
        //mAdapter=new invoice_plu_recyclerviewadapter(this.getContext(),ButtonList);
        admin_stock_receiving_recyclerview.OnClickListener customListener = new admin_stock_receiving_recyclerview.OnClickListener() {
            @Override
            public void onClick(String buttonPrefix) {
                // Handle the click event here

                loadItemData(buttonPrefix);
                et_quantity.requestFocus();
                //for opening keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_quantity, InputMethodManager.SHOW_IMPLICIT);


            }
        };



        mAdapter=new admin_stock_receiving_recyclerview(getApplicationContext(),ButtonList,customListener);
        rv_pluListReceiving.setAdapter(mAdapter);





//// Initialize your adapter and pass the listener
//        mAdapter = new admin_stock_receiving_recyclerview(getApplicationContext(), ButtonList, customListener);
//
//// Set the adapter to the RecyclerView
//        rv_pluListReceiving.setAdapter(mAdapter);


    }

//    private void setupRecyclerView() {
//        admin_stock_receiving_recyclerview adapter = new admin_stock_receiving_recyclerview(buttonList, new YourAdapter.OnClickListener() {
//            @Override
//            public void onClick(String buttonPrefix) {
//                // Update the TextView with the buttonPrefix value
//                textView.setText("Button Prefix: " + buttonPrefix);
//            }
//        });
//        recyclerView.setAdapter(adapter);
//    }


    //endregion

    private void loadSpinnerItem(){
        spinnerItem = findViewById(R.id.spinner);

        // Create a list of items
//        List<String> items = new ArrayList<>();
//        items.add("TRANSFER IN");
//        items.add("RETURN");
//        items.add("PURCHASE");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerItem.setAdapter(adapter);

        // Set a listener for item selections
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedValue = items.get(position);
                //Toast.makeText(admin_stock_receiving.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
                itemID=selectedValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void loadSpinnerReason(){
         spinnerRemarks = findViewById(R.id.sp_remarkSpinner);

        // Create a list of items
        List<String> itemsRemarks = new ArrayList<>();
        itemsRemarks.add("");
        itemsRemarks.add("WASTAGE");
        itemsRemarks.add("DAMAGE");


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsRemarks);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerRemarks.setAdapter(adapter);

        spinnerRemarks.setSelection(0);

        // Set a listener for item selections
        spinnerRemarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedValue = itemsRemarks.get(position);
                //Toast.makeText(admin_stock_receiving.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
                reason = selectedValue;

                if (spinnerRemarks.getSelectedItemPosition()!=0){
                    Toast.makeText(admin_stock_transfer_out.this, "SPINNER REMAKRS", Toast.LENGTH_SHORT).show();
                    showProceedCancelDialog(tv_transactionNo.getText().toString(),tv_date.getText().toString(),tv_time.getText().toString(),
                            tv_transUser.getText().toString(),tv_itemName.getText().toString(),et_quantity.getText().toString(),spinnerRemarks.getSelectedItem().toString());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void loadButtonSelect(){

        tv_itemID=findViewById(R.id.tv_itemID);
        tv_itemName = findViewById(R.id.tv_itemName);
        tv_itemDescription = findViewById(R.id.tv_itemDescription);
        btn_select = findViewById(R.id.btn_select);
        et_quantity = findViewById(R.id.et_quantity);
       // et_reason = findViewById(R.id.et_reason);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadItemData(spinnerItem.getSelectedItem().toString());
                et_quantity.requestFocus();
                //for opening keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_quantity, InputMethodManager.SHOW_IMPLICIT);


//                et_quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == EditorInfo.IME_ACTION_DONE) {
//                           // et_reason.requestFocus(); // Move focus to et_reason
//                           //// InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                           // imm.showSoftInput(et_reason, InputMethodManager.SHOW_IMPLICIT);
//                            return true;
//                        }
//                        return false;
//                    }
//                });

            }
        });

    }

    private void loadItemData(String itemID){
        Cursor cursor = db.rawQuery("select * from ITEM where ItemID='"+itemID+"'", null);



        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                // items.add(cursor.getString(0));
                tv_itemID.setText(cursor.getString(0));
                tv_itemName.setText(cursor.getString(1));
                tv_itemDescription.setText(cursor.getString(2));
            }
        }

        cursor.close();
    }

    private void loadTransNo(){
        OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
        or_trans_item.readOfficialReceiptNumber(this.getApplicationContext());
        or_trans_item.readReferenceNumber(this.getApplicationContext());
        tv_transactionNo = findViewById(R.id.tv_transactionNo);
        tv_transactionNo.setText(or_trans_item.getTransactionNo());


    }
    private void DateTime(){
        system_final_date SystemSettings=new system_final_date();
        SystemSettings.insertDate(this.getApplicationContext());
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_date.setText(SystemSettings.getSystemDate());
        tv_time.setText(_sdfWatchTime.format(new Date()));
        Toast.makeText(this, "Date : " + SystemSettings.getSystemDate(), Toast.LENGTH_SHORT).show();

    }

    private void loadTransUser(){
        tv_transUser = findViewById(R.id.tv_transUser);
        shift_active shift_active=new shift_active();
        shift_active.getShiftingTable(this.getApplicationContext());
        shift_active.getAccountInfo(this.getApplicationContext());
        tv_transUser.setText(shift_active.getActiveUserID());
    }



    private void showProceedCancelDialog(String transNo,String date,String time,String user,String itemName,String qty,String remarks) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(admin_stock_transfer_out.this);

        // Inflate the layout file associated with admin_stock_card
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_admin_stock_receiving_summary, null);

        // Set the custom view of the AlertDialog to the inflated layout
        builder2.setView(dialogView);





        // Create and show the AlertDialog
        AlertDialog dialog = builder2.create();

        TextView tv_alertTransNo = dialogView.findViewById(R.id.tv_alertTransNo);
        TextView tv_alertDate = dialogView.findViewById(R.id.tv_alertDate);
        TextView tv_alertTime = dialogView.findViewById(R.id.tv_alertTime);
        TextView tv_alertUser = dialogView.findViewById(R.id.tv_alertUser);
        TextView tv_alertItemName = dialogView.findViewById(R.id.tv_alertItemName);
        TextView tv_alertQty = dialogView.findViewById(R.id.tv_alertQty);
        TextView tv_alertRemarks = dialogView.findViewById(R.id.tv_alertRemarks);
        Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);

        tv_alertTransNo.setText(transNo);
        tv_alertDate.setText(date);
        tv_alertTime.setText(time);
        tv_alertUser.setText(user);
        tv_alertItemName.setText(itemName);
        tv_alertQty.setText(qty);
        tv_alertRemarks.setText(remarks);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                tv_itemID.setText("");
                tv_itemName.setText("");
                tv_itemDescription.setText("");
                et_quantity.setText("");
                spinnerRemarks.setSelection(0);
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertReceivingData();

                SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", android.content.Context.MODE_PRIVATE, null);
                admin_stock_card_update_inventory update_inventory = new admin_stock_card_update_inventory(db2);
                update_inventory.update_subtract(tv_itemID.getText().toString(),Integer.parseInt(et_quantity.getText().toString()));



                dialog.dismiss();
                tv_itemID.setText("");
                tv_itemName.setText("");
                tv_itemDescription.setText("");
                et_quantity.setText("");
                spinnerRemarks.setSelection(0);
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
            }
        });


        dialog.setCanceledOnTouchOutside(false);





        dialog.show();

    }

    private void insertReceivingData(){
        DatabaseHandler databaseHandler = new DatabaseHandler(this.getApplicationContext());
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        shift_active shift_active=new shift_active();
        shift_active.getShiftingTable(this.getApplicationContext());
        shift_active.getAccountInfo(this.getApplicationContext());

//        //String transaction_ID,String OrderId,String OrderName,String OrderQty,String TransactionTime,String TransactionDate,String Remarks,String TransUser
//        boolean isInserted1=    databaseHandler.insertReceivingTbl(tv_transactionNo.getText().toString(),itemID,tv_itemName.getText().toString(),et_quantity.getText().toString(),tv_time.getText().toString(),tv_date.getText().toString(),remarksType,tv_transUser.getText().toString(),reason);
//        // DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
//        //  shift_active shift_active = new shift_active();
//        //  shift_active.getShiftingTable(getApplicationContext());
//        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
//                tv_transactionNo.getText().toString(),
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                remarksType,
//                tv_date.getText().toString(),
//                timeOnly.format(currentDate.getTime()),
//                shift_active.getActiveUserID(),
//                shift_active.getShiftActive()
//
//
//        );
//
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);

        boolean isInserted1=    databaseHandler.insertReceivingTbl(
                tv_transactionNo.getText().toString(),
                tv_itemID.getText().toString(),
                tv_itemName.getText().toString(),
                et_quantity.getText().toString(),
                tv_time.getText().toString(),
                tv_date.getText().toString(),
                "TRANSFER OUT",
                tv_transUser.getText().toString(),
                reason);
        // DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        //  shift_active shift_active = new shift_active();
        //  shift_active.getShiftingTable(getApplicationContext());
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                tv_transactionNo.getText().toString(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "TRANSFER OUT",
                tv_date.getText().toString(),
                timeOnly.format(currentDate.getTime()),
                shift_active.getActiveUserID(),
                shift_active.getShiftActive()


        );
        if (isInserted) {
            // Show a success message
            Toast.makeText(this, "successfully processed", Toast.LENGTH_SHORT).show();

            TransactionNoList .add(tv_transactionNo.getText().toString());
            ItemNameList.add(tv_itemName.getText().toString());
            ItemQtyList.add(et_quantity.getText().toString());
            ItemRemarkList.add(spinnerRemarks.getSelectedItem().toString());
            loadTransNo();
            loadData();


        } else {
            // Handle the case where insertion failed
            Toast.makeText(this, "failed to  process", Toast.LENGTH_SHORT).show();
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }

    @Override
    public void onStart() {
        super.onStart();




        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    tv_time.setText(_sdfWatchTime.format(new Date()));
                    //  getUSB();
                }

            }
        };
        this.registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));


//        filter.addAction("android.hardware.usb.action.USB_STATE");
//        _broadcastReceiver2 = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context ctx, Intent intent) {
//              //  if (intent.getExtras().getBoolean("connected")) {
//                    //do your stuff
//                    getUSB();
//                //}
//
//            }
//        };
//
//        getActivity().registerReceiver(_broadcastReceiver2,new IntentFilter(filter));




    }



}