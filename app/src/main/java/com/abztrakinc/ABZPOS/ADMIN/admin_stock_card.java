package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_plu_model;
import com.abztrakinc.ABZPOS.CASHIER.cashier_cash;
import com.abztrakinc.ABZPOS.CASHIER.cashier_shift_Zread;
import com.abztrakinc.ABZPOS.LOGIN.LoginActivity;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardClass;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.TCSKeyboard.SoftKeyboard;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class admin_stock_card extends AppCompatActivity {

    List<String> items = new ArrayList<>();
    Cursor c;
    SQLiteDatabase db;
    Button btn_select,btn_cancel,btn_process;
    TextView tv_itemName,tv_itemSKU,tv_itemDescription,tv_itemBarcode;
    AutoCompleteTextView autoCompleteTextView;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("hh:mm aa");
    BroadcastReceiver _broadcastReceiver;
    EditText et_quantity;
    EditText et_searchBar;
    Spinner spinnerItem;
    Spinner spinnerRemarks;
    String itemID,remarksType;
    Button btn_export,btn_print;
    int InitialBalance,DataIn,DataOut;
    private ProgressDialog working_dialog;
    private ProgressBar progressBar;
    private ArrayList<String> ColumnName = new ArrayList<>();
    String DB_NAME = "POSAndroidDB.db";


    private PopupWindow popupWindow;
    private ListView suggestionsListView;


    RecyclerView rv_stock_card_list;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    List<admin_stock_card_item_class> StockCardList = new ArrayList<>();
    KeyboardClass keyboardClass;


    //keyboard typing
    private final static int EXECUTE_MSG = 750;
    public static HashMap<Integer,String> mapCode = new HashMap<>();
    public static HashMap<Integer,Integer> mapCode2 = new HashMap<>();

    public int nKickKeyIndex=0;
    private int nKickCnt=0;
    private Timer timer = null;
    private TimerTask timeOutTask = null;
    private static final int KICK_TIME_PERIOD_MILLS = 450;
    KeyboardDevice kboard;
    KeyCodeManager kManager;
  //  private List<String> itemstest = Arrays.asList("OPTION 1", "option 2", "option 3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stock_card);

        tv_itemName = findViewById(R.id.tv_itemName);
        tv_itemSKU = findViewById(R.id.tv_itemSKU);
        tv_itemDescription = findViewById(R.id.tv_itemDescription);
        tv_itemBarcode = findViewById(R.id.tv_itemBarcode);
        btn_select = findViewById(R.id.btn_select);
   //     et_searchBar = findViewById(R.id.et_searchBar);

//        autoCompleteTextView = findViewById(R.id.et_search);
//        autoCompleteTextView.requestFocus();



//        et_searchBar.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    // Clear the EditText
//                    et_searchBar.getText().clear();
//                    et_searchBar.requestFocus();
//                    // Return true to consume the event and keep the focus in the EditText
//                    return true;
//                }
//                return false;
//            }
//        });

//        et_searchBar.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    // Clear the EditText
//
//                   StockCardList.clear();
//                    loadItemData(et_searchBar.getText().toString());
//                    createData(et_searchBar.getText().toString());
//                    refreshRecycleviewStockCard();
//
//
//                    et_searchBar.getText().clear();
//
//
//
//
//
//                    // Post delayed request focus
//                    et_searchBar.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            et_searchBar.requestFocus();
//                        }
//                    }, 100); // Adjust delay time as needed
//                    // Return true to consume the event and keep the focus in the EditText
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        et_searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                // Check if the EditText has lost focus
//                if (!hasFocus) {
//                    // Request focus back to the EditText
//                    et_searchBar.requestFocus();
//                }
//            }
//        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockCardList.clear();
                InitialBalance=0;
                DataIn=0;
                loadItemData(autoCompleteTextView.getText().toString());
                createData(autoCompleteTextView.getText().toString());
                refreshRecycleviewStockCard();


            }
        });
        btn_export=findViewById(R.id.btn_export);
        btn_print=findViewById(R.id.btn_print);
//        btn_export.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showWorkingDialog();
//
////                new Handler().postDelayed(new Runnable() {
////
////                    @Override
////                    public void run() {
////                        removeWorkingDialog();
////                        // progressBar.setVisibility(View.GONE);
////                        new Thread(new Runnable() {
////                            @Override
////                            public void run() {
////                                // DO your work here
////                                // get the data
////                                Looper.prepare();
////                                GenerateData();
////
////                                runOnUiThread(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        // update UI
////
////                                    }
////                                });
////
////
////                            }
////                        }).start();
////
////
////                    }
////
////                }, 7000);
//
//            }
//        });


//        et_searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // Check if the EditText has gained focus
//                if (hasFocus) {
//                    // Call the closeKeyboard function
//                    closeKeyboard();
//                }
//            }
//        });




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
//        autoCompleteTextView.setAdapter(adapter);
//        autoCompleteTextView.setThreshold(1); // Set the number of characters to trigger suggestions


        loadItemID();
        loadSpinnerItem();
        loadAutoCompleteTextView();
        KeyBoardMap();
        InitT9MapCode();
        closeKeyboard();

        loadData();

    }




    //region recyclerview




    RecyclerView rv_pluListReceiving;

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
                StockCardList.clear();
                InitialBalance=0;
                DataIn=0;
                loadItemData(buttonPrefix);
                createData(buttonPrefix);
                refreshRecycleviewStockCard();


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

    private void closeKeyboard() {
//        View view = getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }

        // Create an instance of the SoftKeyboard class
        SoftKeyboard keyboardSettings = new SoftKeyboard();

// Get the value indicating whether the soft keyboard should be shown or not
        int showKeyboard = keyboardSettings.getShowKboard();

// Check if the soft keyboard should be hidden
        if (showKeyboard != 1) {
            // Hide the soft keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }
    private void loadAutoCompleteTextView(){


        View popupView = getLayoutInflater().inflate(R.layout.activity_admin_stock_card_popup, null);
        suggestionsListView = popupView.findViewById(R.id.suggestionsListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        suggestionsListView.setAdapter(adapter);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);

        // Set item click listener for the suggestions ListView
        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedText = (String) parent.getItemAtPosition(position);
                // Handle selection, perform navigation or other actions if needed
                Toast.makeText(admin_stock_card.this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });



    }
    private void navigateSuggestions(int direction) {
        int currentSelection = suggestionsListView.getSelectedItemPosition();
        int newPosition = currentSelection + direction;

        if (newPosition >= 0 && newPosition < suggestionsListView.getCount()) {
            suggestionsListView.setSelection(newPosition);
        }
    }
    private void showWorkingDialog() {
        working_dialog = ProgressDialog.show(this, "","GENERATING STOCK CARD PLEASE WAIT...", true);
    }
    private void removeWorkingDialog() {
        if (working_dialog != null) {
            working_dialog.dismiss();
            working_dialog = null;
        }
    }
    private void loadItemData(String itemID){
        db = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from ITEM where ItemID='"+itemID+"'", null);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                // items.add(cursor.getString(0));
                tv_itemName.setText(cursor.getString(1));
                tv_itemSKU.setText(cursor.getString(0));
                tv_itemDescription.setText(cursor.getString(2));
                tv_itemBarcode.setText(cursor.getString(9));
            }
        }

        cursor.close();
    }
    public void  createData(String itemID){

        loadInitialBalance(itemID);
        loadReceivingData(itemID);
        loadSaleData(itemID);

        Comparator<admin_stock_card_item_class> transactionNoComparator = new Comparator<admin_stock_card_item_class>() {
            @Override
            public int compare(admin_stock_card_item_class item1, admin_stock_card_item_class item2) {
                // Assuming TransactionNo is a String. If it's a number, you can directly compare.
                return item1.getTransactionNo().compareTo(item2.getTransactionNo());
            }
        };



        Collections.sort(StockCardList, transactionNoComparator);
    }
    public void  refreshRecycleviewStockCard(){
//        createSampleData();


        rv_stock_card_list = findViewById(R.id.rv_stock_card_list);
        rv_stock_card_list.removeAllViews();
        rv_stock_card_list.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManager.removeAllViews();
        rv_stock_card_list.setLayoutManager(layoutManager);
        adapter=new admin_stock_card.RecyclerviewAdapter(StockCardList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_stock_card_list.setAdapter(adapter);

        //invoice item list



    }
    private void loadItemID(){
        items.clear();
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
    private void loadSpinnerItem(){
//        spinnerItem = findViewById(R.id.spinner);
//
//        // Create a list of items
////        List<String> items = new ArrayList<>();
////        items.add("TRANSFER IN");
////        items.add("RETURN");
////        items.add("PURCHASE");
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinnerItem.setAdapter(adapter);
//
//        // Set a listener for item selections
//        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // Handle the selected item
//                String selectedValue = items.get(position);
//                //Toast.makeText(admin_stock_receiving.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
//                itemID=selectedValue;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
    }
    private void loadInitialBalance(String itemID){
      //  StockCardList.clear();
//        rv_stock_card_list.removeAllViews();
        //load from POSAndroidDb itemQty
        db = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        c = db.rawQuery("select * from ITEM where ItemID='"+itemID+"'", null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
               // InitialBalance = (int) Math.round(Double.parseDouble(c.getString(4))); ;
                InitialBalance = 0;
               // int initialBalanceAsInt = (int) Math.round(InitialBalance);
                StockCardList.add(new admin_stock_card_item_class("","Initial Balance","","","-","-",String.valueOf(InitialBalance),"",""));
            }
        }

        c.close();

    }
    private void loadReceivingData(String itemID){
        //TransferIn,Return,Purchase
        //load from POSAndroidDb itemQty

        db = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        c = db.rawQuery("select * from ReceivingTbl where OrderID='"+itemID+"'", null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
               // InitialBalance = (int) Math.round(Double.parseDouble(c.getString(4))); ;

                if(c.getString(4).equalsIgnoreCase("")){
                    DataIn=0;
                }
                else {
                    DataIn = Integer.parseInt(c.getString(4));
                }
               // InitialBalance = InitialBalance + DataIn;
                String TransactionNo = c.getString(1);
                String TransactionDate=c.getString(6);
                String TransactionTime= c.getString(5);
                String TransUser = c.getString(8);
                String DialogContent = c.getString(9);

                String remark = c.getString(7);
                // int initialBalanceAsInt = (int) Math.round(InitialBalance);
                if (remark.equalsIgnoreCase("Transfer Out")){
                    StockCardList.add(new admin_stock_card_item_class(TransactionNo,remark,TransactionDate,TransactionTime,"-",String.valueOf(DataIn),String.valueOf(InitialBalance),TransUser,DialogContent));


                }else{
                    StockCardList.add(new admin_stock_card_item_class(TransactionNo,remark,TransactionDate,TransactionTime,String.valueOf(DataIn),"-",String.valueOf(InitialBalance),TransUser,DialogContent));
                }

            }
        }

        c.close();
    }
    private void loadSaleData(String itemID){
        //TransferIn,Return,Purchase
        //load from POSAndroidDb itemQty

        db = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        c = db.rawQuery("  select InvoiceReceiptItemFinal.TransactionID,TransUser,TransactionDate,TransactionTime,InvoiceReceiptItemFinal.OrderQty from InvoiceReceiptTotal inner join InvoiceReceiptItemFinal on InvoiceReceiptTotal.TransactionID = InvoiceReceiptItemFinal.TransactionID" +
                " where OrderID='"+itemID+"'", null);

        //c = db.rawQuery("select * from InvoiceReceiptItemFinal where OrderID='"+itemID+"'", null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
                // InitialBalance = (int) Math.round(Double.parseDouble(c.getString(4))); ;
                DataOut = Integer.parseInt(c.getString(4));
              //  InitialBalance = InitialBalance - DataOut;
                String TransactionNo = c.getString(0);
                String TransactionDate=c.getString(2);
                String TransactionTime= c.getString(3);
                String TransactionUser = c.getString(1);



                //find



                String remark = "SALES";
                // int initialBalanceAsInt = (int) Math.round(InitialBalance);
                StockCardList.add(new admin_stock_card_item_class(TransactionNo,remark,TransactionDate,TransactionTime,"-",String.valueOf(DataOut),String.valueOf(InitialBalance),TransactionUser,"ITEM SALES"));
            }
        }

        c.close();
    }





    //region Keyboard typing


    Handler MyHandler = new Handler(Looper.getMainLooper()) {
    @SuppressLint({"StringFormatMatches", "DefaultLocale"})
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case KeyboardDevice.MSG_KEY: {
                String strMsgInfo = "";
                try {
                    strMsgInfo = msg.getData().getString("msg_info");
                    String[] keyData = strMsgInfo.split(":", 2);
                    if (keyData != null) {

                        int nKeyIndex = StringToInt(keyData[0]);
                        if (nKeyIndex == 0) {

                        } else {
                            String KeyIndex = String.format("%d", nKeyIndex);
                            KeyCodeManager kManager = new KeyCodeManager();


                            if (mapCode.containsKey(nKeyIndex)) {
                                setNewKeyEvent(nKeyIndex);
                            } else {
                                SimulateKeyboard(nKeyIndex);
                            }




                            // Log.e("RouteKeyIndex",String.valueOf(nKeyIndex));

                        }
                        String keyName=KeyCodeManager.getDefaultKeyName(nKeyIndex);
                        String strShow = String.format("KeyIndex:%d", nKeyIndex) + " ScanCode:" + keyData[1]+" KeyName:"+keyName;
                        // ShowMsg(strShow);
                    } else {
                        //Toast.makeText(cashier_invoice.this, strMsgInfo, Toast.LENGTH_SHORT).show();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            default:
                break;
        }
    }
};
    public  void  setNewKeyEvent(int nKeyIndex){
        StopTimeOutTask();
        if(nKeyIndex ==0){
            nKickKeyIndex = nKeyIndex;
            nKickCnt =1;
        }
        else{
            if (nKeyIndex == nKickKeyIndex){
                nKickCnt = nKickCnt+1;
            }
            else{
                DoInput(nKickKeyIndex,nKickCnt);
                nKickKeyIndex = nKeyIndex;
                nKickCnt=1;
            }
        }
        InitTimeOutTask();

    }


    public static int StringToInt(String strParse) {
        int iValue = 0;
        try {
            if (strParse.length() > 0) {
                iValue = Integer.parseInt(strParse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iValue;
    }
    public void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;

    }
    public void InitT9MapCode() {
        mapCode.clear();
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD0, "0");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD1, "1PQRS");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD2, "2TUV");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD3, "3WXYZ");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD4, "4GHI");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD5, "5JKL");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD6, "6MNO");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD7, "7@[\\]");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD8, "8ABC");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD9, "9DEF");
//        KEY_INDEX_NUMPADDEC
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPADDEC, "-.");
        mapCode2.put(KeyCodeManager.KEY_INDEX_TOTAL2, KeyEvent.KEYCODE_ENTER);
        mapCode2.put(KeyCodeManager.KEY_INDEX_PAGEDN, KeyEvent.KEYCODE_PAGE_DOWN);
        mapCode2.put(KeyCodeManager.KEY_INDEX_PAGEUP, KeyEvent.KEYCODE_PAGE_UP);
    }
    private void StopTimeOutTask(){
        if (timeOutTask!=null){
            timeOutTask.cancel();
            timeOutTask = null;
        }
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
    }
    private void InitTimeOutTask(){
        {
            try{
                timer = new Timer();
                timeOutTask = new TimerTask() {
                    @Override
                    public void run() {
                        DoInput(nKickKeyIndex,nKickCnt);
                        nKickKeyIndex =0;
                        nKickCnt = 0;
                    }
                };
                timer.schedule(timeOutTask,KICK_TIME_PERIOD_MILLS);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public void DoInput(int nKeyIndex,int nCnt){
        int nLayer = nCnt;
        if (nLayer>0){
            nLayer= nLayer-1;
        }

        if (mapCode.containsKey(nKeyIndex)) {
            try {
                String codeT9 = mapCode.get(nKeyIndex);
                if (codeT9 != null) {
                    List<String> lstSel = new ArrayList<>();
                    ///////////////
                    char[] charT9 = codeT9.toCharArray();
                    for (char chInput : charT9) {
                        String strInput = String.valueOf(chInput);
                        lstSel.add(strInput);
                    }
                    /////////////
                    String selIn = "";
                    if (nLayer < lstSel.size()) {
                        selIn = lstSel.get(nLayer);
                    } else {
                        selIn = lstSel.get(lstSel.size() - 1);
                    }
                    ////
                    int nKeyCode = KeyEvent.keyCodeFromString(selIn);
                    //Log.i(TAG, String.format("Do T9Input:%d %d %s=%d", nKeyIndex, nCnt, selIn, nKeyCode));
                    //PostMessage(T9_INPUT_EXECUTE_MSG, selIn);
//                Log.i("NKeyCOde",String.valueOf(nKeyCode));
//                Log.i("ACTION NEXT",String.valueOf(EditorInfo.IME_ACTION_NEXT));
//                Log.i("TAG",String.valueOf(KeyCodeManager.KEY_INDEX_EC));

                    //    if (nKeyCode == EditorInfo.IME_ACTION_NEXT)
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }

                    PostMessage(EXECUTE_MSG, selIn);


                }
            }
            catch (Exception ex){

            }
        }


    }
    public void PostMessage(int nMsg, String strMessage) {


        int start = Math.max(et_searchBar.getSelectionStart(), 0);
        int end = Math.max(et_searchBar.getSelectionEnd(), 0);
        et_searchBar.getText().replace(Math.min(start, end), Math.max(start, end),
                strMessage, 0, strMessage.length());


        //setFinalMessage(strMessage);


        // Log.e("TAG", "PostMessage: "+getFinalMessage() );
    }


    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
     //   Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
//        Log.d("TAG", "input: "+input);
//        int digitType=1; //1 number //2 letter
//        final int PRESS_INTERVAL =  700;




        String[] allowedInput = {"0","1","2","3","4","5","6","7","8","9"};
        for (String element : allowedInput){
            if (element ==  input){


                if (input.equals(".")){
                    input="-";
                }






                    int start = Math.max(et_searchBar.getSelectionStart(), 0);
                    int end = Math.max(et_searchBar.getSelectionEnd(), 0);
                et_searchBar.getText().replace(Math.min(start, end), Math.max(start, end),
                            input, 0, input.length());


                    if (et_searchBar.length() == 0) {
                        return;
                    }













            }

        }






        if (input.equalsIgnoreCase("EXIT")){
            onBackPressed();
        }
        if (input.equals("EC")) {

//                int length = autoCompleteTextView.getText().length();
//                if (length > 0) {
//                    autoCompleteTextView.getText().delete(length - 1, length);
//                }

                            int length = et_searchBar.getText().length();
                if (length > 0) {
                    et_searchBar.getText().delete(length - 1, length);
                }


        }
        else if (input.equals("Total")){
//            StockCardList.clear();
//            InitialBalance=0;
//            DataIn=0;
//            loadItemData(autoCompleteTextView.getText().toString());
//            createData(autoCompleteTextView.getText().toString());
//            refreshRecycleviewStockCard();
//            closeKeyboard();

            StockCardList.clear();
            InitialBalance=0;
            DataIn=0;
            loadItemData(et_searchBar.getText().toString());
            createData(et_searchBar.getText().toString());
            refreshRecycleviewStockCard();
            closeKeyboard();
            et_searchBar.setText("");


        }
        else{
closeKeyboard();
            Log.e("TAG", "SimulateKeyboard: "+input );
        }








    }



//endregion

    //for Recyclerview adapter
    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_stock_card.RecyclerviewAdapter.MyViewHolder>{
        List<admin_stock_card_item_class> StockCardList;
        Context context;
        String TransactionNo;
        String UserAccount;
        String DialogContent;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_stock_card.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<admin_stock_card_item_class> stockCardList, Context context) {

            this.StockCardList = stockCardList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_stock_card.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_stock_card_layout,parent,false);
            admin_stock_card.RecyclerviewAdapter.MyViewHolder holder = new admin_stock_card.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_stock_card.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

//            holder.tv_ItemName.setText(String.valueOf((ProductList.get(position).getItemName())));
//            holder.tv_ItemDesc.setText((String.valueOf(ProductList.get(position).getItemDescription())));
//            holder.tv_ItemID.setText((String.valueOf(ProductList.get(position).getItemID())));
//            holder.tv_ItemPrice.setText((String.valueOf(ProductList.get(position).getItemPrice())));
//            holder.tv_ItemQty.setText((String.valueOf(ProductList.get(position).getItemQty())));
//            holder.tv_FastMoving.setText((String.valueOf(ProductList.get(position).getFastMovingItem())));
//            holder.tv_FastMovingButton.setText((String.valueOf(ProductList.get(position).getFastMovingButton())));
//            holder.tv_ItemCode.setText((String.valueOf(ProductList.get(position).getItemCode())));
//            holder.tv_ItemBarcode.setText((String.valueOf(ProductList.get(position).getBarcode())));
//            holder.tv_VatIndicator.setText((String.valueOf(ProductList.get(position).getVATIndicator())));
//            holder.tv_itemCount.setText(String.valueOf(position+1));

            holder.tv_dataDate.setText(String.valueOf(StockCardList.get(position).getDate()));;
            holder.tv_dataTime.setText(String.valueOf(StockCardList.get(position).getTime()));;



            if (!(StockCardList.get(position).getDataIn()).equals("-")){
             //   DataIn = Integer.parseInt(StockCardList.get(position).getDataIn());
                DataIn = Integer.parseInt(StockCardList.get(position).getDataIn());
                holder.tv_dataIn.setText(String.valueOf(DataIn));;
            }
            else{
                DataIn=0;
                holder.tv_dataIn.setText("-");;
            }



            if (!(StockCardList.get(position).getDataOut()).equals("-")){
                DataOut = Integer.parseInt(StockCardList.get(position).getDataOut());
                holder.tv_dataOut.setText(String.valueOf(DataOut));;
            }
            else{
                DataOut=0;
                holder.tv_dataOut.setText("-");
            }

            InitialBalance=InitialBalance+DataIn-DataOut;
            holder.tv_dataBalance.setText((String.valueOf(InitialBalance)));;
            holder.tv_dataRemarks.setText(String.valueOf((StockCardList.get(position).getRemarks())));;
            if ((StockCardList.get(position).getRemarks()).equalsIgnoreCase("Initial Balance")){
                holder.iv_info.setVisibility(View.INVISIBLE);
            }

            holder.tv_transactionNo.setText(String.valueOf((StockCardList.get(position).getTransactionNo())));;

//            TransactionNo = (StockCardList.get(position).getTransactionNo());
//            UserAccount = (StockCardList.get(position).getUserAccount());
//            DialogContent =StockCardList.get(position).getDialogContent();

            holder.ll_selectInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.activity_admin_stock_card_info_layout, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(admin_stock_card.this);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    ImageView closeButton = dialogView.findViewById(R.id.iv_close);
                    TextView tv_transactionNo = dialogView.findViewById(R.id.tv_transactionNo);
                    TextView tv_userAccount = dialogView.findViewById(R.id.tv_userAccount);
                    TextView tv_dialogContent = dialogView.findViewById(R.id.tv_dialogContent);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                         //   Toast.makeText(admin_stock_card.this, StockCardList.get(position).getDialogContent(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
                    tv_transactionNo.setText((StockCardList.get(position).getTransactionNo()));
                    tv_dialogContent.setText(StockCardList.get(position).getDialogContent());
                    tv_userAccount.setText(StockCardList.get(position).getUserAccount());



                    alertDialog.show();

                }
            });









        }

        @Override
        public int getItemCount() {
            return StockCardList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_dataDate;
            TextView tv_dataTime;
            TextView tv_dataIn;
            TextView tv_dataOut;
            TextView tv_dataBalance;
            TextView tv_dataRemarks;
            TextView tv_transactionNo;
            ImageView iv_info;
            LinearLayout ll_selectInfo;
            //CardView parentLayout;




            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_dataDate = itemView.findViewById(R.id.tv_dataDate);
                tv_dataIn    = itemView.findViewById(R.id.tv_dataIn);
                tv_dataOut = itemView.findViewById(R.id.tv_dataOut);
                tv_dataBalance = itemView.findViewById(R.id.tv_dataBalance);
                tv_dataRemarks = itemView.findViewById(R.id.tv_dataRemarks);
                tv_dataTime = itemView.findViewById(R.id.tv_dataTime);
                tv_transactionNo = itemView.findViewById(R.id.tv_dataTransNo);
                iv_info = itemView.findViewById(R.id.iv_info);
                ll_selectInfo = itemView.findViewById(R.id.ll_selectInfo);
//                ll_selectInfo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                       // showDialogInfo();
//
//
//
//
//
//
//                    }
//                });







            }
        }
    }
}