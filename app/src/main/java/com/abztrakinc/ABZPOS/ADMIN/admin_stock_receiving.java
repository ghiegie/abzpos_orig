package com.abztrakinc.ABZPOS.ADMIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_plu_model;
import com.abztrakinc.ABZPOS.CASHIER.cashier_invoice;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.system_final_date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class admin_stock_receiving extends AppCompatActivity {

    List<String> items = new ArrayList<>();
    Cursor c;
    SQLiteDatabase db;
    String DB_NAME = "POSAndroidDB.db";
    Button btn_select,btn_cancel,btn_process;
    TextView tv_itemID,tv_itemName,tv_itemDescription,tv_date,tv_time,tv_transactionNo,tv_transUser;
    LinearLayout ll_spinnerRemarksBG;
    AutoCompleteTextView autoCompleteTextView;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("hh:mm aa");
    BroadcastReceiver _broadcastReceiver;
    EditText et_quantity,et_reason;
    Spinner spinnerItem;
   // Spinner spinnerRemarks;
    String itemID,remarksType;

    ArrayList<String>TransactionNoList = new ArrayList<>();
    ArrayList<String>ItemNameList = new ArrayList<>();
    ArrayList<String>ItemQtyList = new ArrayList<>();
    ArrayList<String>ItemRemarkList = new ArrayList<>();

    //region KEYBOARD VARIABLE
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
    Spinner spinnerRemarks;
    CustomSpinnerAdapter spinnerAdapter;


    //endregion






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stock_receiving);

        loadItemID();
      //  loadSpinnerItem();
        loadSpinnerRemarks();
        loadButtonSelect();
        loadData();


        btn_process= findViewById(R.id.btn_process); //end button
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  showProceedCancelDialog();
             //   insertReceivingData();

                Toast.makeText(admin_stock_receiving.this, TransactionNoList.toString(), Toast.LENGTH_SHORT).show();
                loadTotalReport();
                onBackPressed();

            }
        });

        loadTransNo();
        DateTime();
        loadTransUser();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);


        KeyBoardMap();
        InitT9MapCode();


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



//        if (autoCompleteTextView.hasFocus()){
//            int start = Math.max(autoCompleteTextView.getSelectionStart(), 0);
//            int end = Math.max(autoCompleteTextView.getSelectionEnd(), 0);
//            autoCompleteTextView.getText().replace(Math.min(start, end), Math.max(start, end),
//                    strMessage, 0, strMessage.length());
//        }
//        else if(et_quantity.hasFocus()){
//            int start = Math.max(et_quantity.getSelectionStart(), 0);
//            int end = Math.max(et_quantity.getSelectionEnd(), 0);
//            et_quantity.getText().replace(Math.min(start, end), Math.max(start, end),
//                    strMessage, 0, strMessage.length());
//        }

        if (autoCompleteTextView.hasFocus()) {
            int start = Math.max(autoCompleteTextView.getSelectionStart(), 0);
            int end = Math.max(autoCompleteTextView.getSelectionEnd(), 0);
            Editable text = autoCompleteTextView.getText();
            text.replace(Math.min(start, end), Math.max(start, end), strMessage, 0, strMessage.length());
        } else if (et_quantity.hasFocus()) {
            int start = Math.max(et_quantity.getSelectionStart(), 0);
            int end = Math.max(et_quantity.getSelectionEnd(), 0);
            Editable text = et_quantity.getText();
            text.replace(Math.min(start, end), Math.max(start, end), strMessage, 0, strMessage.length());
        }
        else if (et_reason.hasFocus()){
            int start = Math.max(et_reason.getSelectionStart(), 0);
            int end = Math.max(et_reason.getSelectionEnd(), 0);
            Editable text = et_reason.getText();
            text.replace(Math.min(start, end), Math.max(start, end), strMessage, 0, strMessage.length());
        }



        //setFinalMessage(strMessage);


        // Log.e("TAG", "PostMessage: "+getFinalMessage() );
    }
    int selectedItemPosition=0;
    int dropDown=0; //if =1 means focusing on spinnerRemarks
    int newPosition;
    int btnFocus; //1 cancel,2 proces, 0 null
    String DialogView="";
    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        //   Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
//        Log.d("TAG", "input: "+input);
//        int digitType=1; //1 number //2 letter
//        final int PRESS_INTERVAL =  700;

        if (input.equals("EC")) {


            if (autoCompleteTextView.hasFocus()){
                int length = autoCompleteTextView.getText().length();
                if (length > 0) {
                    autoCompleteTextView.getText().delete(length - 1, length);
                }
            }
           else if (et_quantity.hasFocus()){
                int length = et_quantity.getText().length();
                if (length > 0) {
                    et_quantity.getText().delete(length - 1, length);
                }
            }
           else if (et_reason.hasFocus()){
                int length = et_reason.getText().length();
                if (length > 0) {
                    et_reason.getText().delete(length - 1, length);
                }
            }

        }
        else if (input.equals("Total")){

            if (autoCompleteTextView.hasFocus()){





            loadItemData(autoCompleteTextView.getText().toString());
           // et_quantity.requestFocus();
            //for opening keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_quantity, InputMethodManager.SHOW_IMPLICIT);


            et_quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        et_reason.requestFocus(); // Move focus to et_reason
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(et_reason, InputMethodManager.SHOW_IMPLICIT);
                        return true;
                    }
                    return false;
                }
            });
            }
//            else if (et_quantity.hasFocus()){
//                pushCloseKeyboard();
//            }
//            else if (et_reason.hasFocus()){
//                pushCloseKeyboard();
//            }
//            else if (dropDown==1){
//                //spinnerRemarks.performClick();
//               // spinnerRemarks.setSelection(newPosition);
//
//                //Log.d("TAG", "spinnerRemarksPosition: "+newPosition);
//              //  Log.d("TAG", "spinnerRemarksEnter: "+spinnerRemarks.getSelectedItem());
//                dropDown=0;
//                spinnerRemarks.clearFocus();
//                et_reason.requestFocus();
//            }
//            else if (btn_cancel.hasFocus()){
//                Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
//            }
            else if (DialogView.equalsIgnoreCase("ConfirmationDialog")){
                insertReceivingData();
            }
            else{
                showProceedCancelDialog(tv_transactionNo.getText().toString(),tv_date.getText().toString(),tv_time.getText().toString(),
                        tv_transUser.getText().toString(),tv_itemName.getText().toString(),et_quantity.getText().toString(),spinnerRemarks.getSelectedItem().toString());
            }



        }
        else if (input.equals("Page DN")){
//            if (autoCompleteTextView.hasFocus()){
//                et_quantity.requestFocus();
//            }
             if (et_quantity.hasFocus()){
                et_quantity.clearFocus();
                closeKeyboard();
                dropDown=1;
            }
           else if (dropDown==1){
               // et_quantity.clearFocus();
               // spinnerRemarks.performClick();


//                int selectedItemPosition = spinnerRemarks.getSelectedItemPosition();
//                if (selectedItemPosition < spinnerRemarks.getCount() - 1) {
//                    // Increment the position to select the next item
//                    spinnerRemarks.setSelection(selectedItemPosition + 1);
//                }

                pushCloseKeyboard();
                ll_spinnerRemarksBG = findViewById(R.id.ll_spinnerRemarksBG);
                ll_spinnerRemarksBG.setBackgroundColor(Color.BLUE);

                 newPosition = Math.min(selectedItemPosition, spinnerAdapter.getCount() - 1);
                Log.d("spinnerAdapter", "SelectedItemPosition "+selectedItemPosition);
                Log.d("spinnerAdapter", "position "+newPosition);
                Log.d("spinnerAdapter", "spinnerAdapterCOunt "+spinnerAdapter.getCount());
                spinnerAdapter.setSelectedItemPosition(newPosition);
                spinnerRemarks.setSelection(newPosition);


                if (selectedItemPosition ==2){
                    selectedItemPosition=2;
                    dropDown=0;
                    et_reason.requestFocus();
                    ll_spinnerRemarksBG.setBackgroundColor(Color.WHITE);

                }
                selectedItemPosition = selectedItemPosition + 1;

            }
//            else if (et_reason.hasFocus()) {
//                btnFocus=1;
//                Log.d("TAG", "btn_Cancel has focus" );
//                et_reason.clearFocus();
//                btn_cancel.requestFocus();
//                btn_cancel.setBackgroundColor(Color.BLUE);
//                btn_process.setBackgroundColor(Color.WHITE); // Reset the background color of btn_process
//            } else if(btnFocus==1){
//                btnFocus=2;
//                Log.d("TAG", "btn_process has focus" );
//                btn_cancel.clearFocus();
//                btn_process.requestFocus();
//               // et_reason.requestFocus();
//                btn_cancel.setBackgroundColor(Color.WHITE); // Reset the background color of btn_cancel
//                btn_process.setBackgroundColor(Color.BLUE);
//
//            }

        }
        else if (input.equals("Page UP")){

//            if (et_quantity.hasFocus()){
//                autoCompleteTextView.requestFocus();

//            }

//            if (btnFocus==2){
//                btnFocus=1;
//                //btn_process.clearFocus();
//                //btn_cancel.requestFocus();
//                btn_cancel.setBackgroundColor(Color.BLUE);
//                btn_process.setBackgroundColor(Color.WHITE);
//
//            }
//          else  if (btnFocus==1){
//               // btn_cancel.clearFocus();
//                et_reason.requestFocus();
//                btn_cancel.setBackgroundColor(Color.WHITE);
//                btnFocus=0;
//                //btn_process.setBackgroundColor(Color.WHITE);
//            }


             if (et_reason.hasFocus()){
                 dropDown=1;
                 et_reason.clearFocus();
             }
           else  if (dropDown==1){

                 pushCloseKeyboard();
                 ll_spinnerRemarksBG = findViewById(R.id.ll_spinnerRemarksBG);
                 ll_spinnerRemarksBG.setBackgroundColor(Color.BLUE);

                 newPosition = Math.min(selectedItemPosition, spinnerAdapter.getCount() - 1);
                 Log.d("spinnerAdapter", "SelectedItemPosition "+selectedItemPosition);
                 Log.d("spinnerAdapter", "position "+newPosition);
                 Log.d("spinnerAdapter", "spinnerAdapterCOunt "+spinnerAdapter.getCount());
                 spinnerAdapter.setSelectedItemPosition(newPosition);
                 spinnerRemarks.setSelection(newPosition);

                 if (selectedItemPosition==0){
                     selectedItemPosition=0;
                     dropDown=0;
                     et_quantity.requestFocus();
                     ll_spinnerRemarksBG.setBackgroundColor(Color.WHITE);
                 }
                 selectedItemPosition = selectedItemPosition - 1;
             }
             else if (et_quantity.hasFocus()){
                // autoCompleteTextView.requestFocus();
             }
        }
        else if (input.equals("Exit")){
            closeKeyboard();
            //Log.e("TAG", "SimulateKeyboard: "+input );
        }








    }

    int keyboardFlg=0;
    private void closeKeyboard() {
        View view = getCurrentFocus();


        if (view != null) { // close

            if (keyboardFlg==0) {
                Log.d("TAG", "closeKeyboard: off");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                keyboardFlg=1;
            }
            else{
                Log.d("TAG", "closeKeyboard: on");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                keyboardFlg=0;
            }
        }

    }
    private void pushCloseKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            keyboardFlg=1;
        }

    }



//endregion




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

        itemCode.clear();

        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemVatIndicator.clear();
        priceOverride.clear();
        x=0;
        po2=null;
        ButtonList.clear();



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
        rv_pluListReceiving.removeAllViews();
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



//                et_quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                         //   et_reason.requestFocus(); // Move focus to et_reason
//                        //    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            //    imm.showSoftInput(et_reason, InputMethodManager.SHOW_IMPLICIT);
//                            return true;
//                        }
//                        return false;
//                    }
//                });


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadItemID(){
        db = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        items.clear();


        c = db.rawQuery("select * from ITEM", null);



        if (c.getCount() == 0) {
           // Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(c.moveToNext()){
                items.add(c.getString(0));
            }
        }

        c.close();

    }
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
    private void loadSpinnerRemarks(){
         spinnerRemarks = findViewById(R.id.sp_remarkSpinner);

        // Create a list of items
        List<String> itemsRemarks = new ArrayList<>();
        itemsRemarks.add("");
        itemsRemarks.add("TRANSFER IN");
        itemsRemarks.add("RETURN");
        itemsRemarks.add("PURCHASE");



        spinnerAdapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, itemsRemarks,spinnerRemarks);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter to your Spinner
        spinnerRemarks.setAdapter(spinnerAdapter);

        spinnerRemarks.setSelection(0);

        spinnerRemarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerRemarks.getSelectedItemPosition()!=0){
                    showProceedCancelDialog(tv_transactionNo.getText().toString(),tv_date.getText().toString(),tv_time.getText().toString(),
                            tv_transUser.getText().toString(),tv_itemName.getText().toString(),et_quantity.getText().toString(),spinnerRemarks.getSelectedItem().toString());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemsRemarks);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinnerRemarks.setAdapter(adapter);
//
//        // Set a listener for item selections
//        spinnerRemarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // Handle the selected item
//                String selectedValue = itemsRemarks.get(position);
//                //Toast.makeText(admin_stock_receiving.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
//                remarksType = selectedValue;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
    }
    private void loadButtonSelect(){

        tv_itemID=findViewById(R.id.tv_itemID);
        tv_itemName = findViewById(R.id.tv_itemName);
        tv_itemDescription = findViewById(R.id.tv_itemDescription);
        btn_select = findViewById(R.id.btn_select);
        et_quantity = findViewById(R.id.et_quantity);
        et_reason = findViewById(R.id.et_reason);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadItemData(autoCompleteTextView.getText().toString());
                et_quantity.requestFocus();
                //for opening keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_quantity, InputMethodManager.SHOW_IMPLICIT);


                et_quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            et_reason.requestFocus(); // Move focus to et_reason
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(et_reason, InputMethodManager.SHOW_IMPLICIT);
                            return true;
                        }
                        return false;
                    }
                });

            }
        });

    }
    private void loadItemData(String itemID){
        Cursor cursor = db.rawQuery("select * from ITEM where ItemID='"+itemID+"'", null);



        if (cursor.getCount() == 0) {
           // Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
               // items.add(cursor.getString(0));
                tv_itemID.setText(cursor.getString(0));
                tv_itemName.setText(cursor.getString(1));
                tv_itemDescription.setText(cursor.getString(2));

            }
            et_quantity.requestFocus();
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
       // Toast.makeText(this, "Date : " + SystemSettings.getSystemDate(), Toast.LENGTH_SHORT).show();

    }
    private void loadTransUser(){
        tv_transUser = findViewById(R.id.tv_transUser);
        shift_active shift_active=new shift_active();
        shift_active.getShiftingTable(this.getApplicationContext());
        shift_active.getAccountInfo(this.getApplicationContext());
        tv_transUser.setText(shift_active.getActiveUserID());
    }
    AlertDialog.Builder builder;

    private void showProceedCancelDialog(String transNo,String date,String time,String user,String itemName,String qty,String remarks) {
//        DialogView="ConfirmationDialog";
//        builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Confirmation")
//                .setMessage("Do you want to proceed?")
//                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Handle "Proceed" button click
//                        // Add your custom logic here
//                        insertReceivingData();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Handle "Cancel" button click or dismiss the dialog
//                        dialog.dismiss();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();


        AlertDialog.Builder builder2 = new AlertDialog.Builder(admin_stock_receiving.this);

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
                update_inventory.update_insert(tv_itemID.getText().toString(),Integer.parseInt(et_quantity.getText().toString()));



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

        //String transaction_ID,String OrderId,String OrderName,String OrderQty,String TransactionTime,String TransactionDate,String Remarks,String TransUser
    boolean isInserted1=    databaseHandler.insertReceivingTbl(tv_transactionNo.getText().toString(),tv_itemID.getText().toString(),tv_itemName.getText().toString(),et_quantity.getText().toString(),tv_time.getText().toString(),tv_date.getText().toString(),spinnerRemarks.getSelectedItem().toString(),tv_transUser.getText().toString(),et_reason.getText().toString());
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
                spinnerRemarks.getSelectedItem().toString(),
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
            //update inventory add





        } else {
            // Handle the case where insertion failed
            Toast.makeText(this, "failed to  process", Toast.LENGTH_SHORT).show();
        }
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);

    }

    private void loadTotalReport(){


        if (TransactionNoList.size()!=0){


            AlertDialog.Builder builder = new AlertDialog.Builder(admin_stock_receiving.this);

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