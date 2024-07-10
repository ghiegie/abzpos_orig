package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.DatabaseHandlerSettings;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.utils.SunmiPrintHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import a1088sdk.PrnDspA1088;
import a1088sdk.PrnDspA1088Activity;

public class admin_manage_receipt extends AppCompatActivity {

    EditText   et_footer,et_HeaderContent;
    Button btn_headerTab,btn_footerTab;
    ImageButton btn_addNew,btn_delete,btn_printTest;
    Button btn_save;
    String DB_NAME = "PosSettings.db";
    String HeaderContent,footer;
    RadioButton rad_header,rad_footer;
    RadioGroup rad_radGroup;
    String textToAdd="";
    String finalReceiptDisplay="";
    String doubleWidth="NO"; //
    int radValue=0; // 0 header | 1 footer
    int deleteID;

    String HeaderText;
    String FooterText;
    Context context1;

    //alertdialogView //0 main // 1 updateitem dialog
   // int alertdialogView = 0;
    AlertDialog alertDialog;


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


    //header footer dialog
    EditText et_addNewText;


    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_manage_receipt);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context1=this;

        btn_addNew=findViewById(R.id.btn_addNew);
        btn_delete=findViewById(R.id.btn_delete);
        btn_printTest=findViewById(R.id.btn_printTest);
        btn_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNewItem();



            }
        });;

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteItem();
            }
        });

        btn_printTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            printTest();





              //  TCSPrint();



            }
        });



        rad_header=findViewById(R.id.rad_header);
        rad_footer=findViewById(R.id.rad_footer);
        rad_radGroup=findViewById(R.id.rad_radgroupReceipt);

        rad_radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i==R.id.rad_header){
//                    Log.e("RADIOBUTTON","0");
//                    radValue=0;
//                    showSummary();
//
//                }else if(i==R.id.rad_footer){
//
//                    Log.e("RADIOBUTTON","1");
//                    radValue=1;
//                    showSummary();
//                }
                setupRadioGroupListener();
            }
        });

        //if ()
        Log.e("RADIO BUTTON",String.valueOf(radValue));



//        et_HeaderContent = findViewById(R.id.et_HeaderContent);
//        et_footer=findViewById(R.id.et_footer);
//        btn_save = findViewById(R.id.btn_save);
//
//
//        btn_headerTab=findViewById(R.id.btn_headerTab);
//        btn_headerTab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HeaderContent= et_HeaderContent.getText()+"     ";
//
//
//                et_HeaderContent.setText(HeaderContent);
//                et_HeaderContent.setSelection(et_HeaderContent.getText().length());
//
//            }
//        });
//
//
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (et_HeaderContent.getText().toString().isEmpty()){
//                    HeaderContent=" ";
//
//                }
//
//
//                if (et_footer.getText().toString().isEmpty()){
//                    footer=" ";
//
//                }
//
//
//
////                posSupplier = et_posSupplier.getText().toString();
////                supAddress=et_supplierAddress.getText().toString();
////                supVatRegTin=  et_supVatRegTin.getText().toString();
////                supAccreditNo= et_supAccreditNo.getText().toString();
////                supDateIssuance=et_supDateIssuance.getText().toString();
////                supEffectiveDate=  et_supEffectiveDate.getText().toString();
////                supValidUntil=et_supvalidUntil.getText().toString();
////                supPermitNo= et_supPermitNo.getText().toString();
////                supDateIssude= et_supDateIssued.getText().toString();
////                supValidUntil2=et_supValidUntil2.getText().toString();
////                compName=  et_companyName.getText().toString();
////                compAddress=   et_companyAddress.getText().toString();
////                compVatregTin=et_companyVatRegTin.getText().toString();
////                compMin=       et_companyMin.getText().toString();
//                HeaderContent=et_HeaderContent.getText().toString();
//                footer=et_footer.getText().toString();
//
//
//               checkIfnotEmpty();
//               checkIfnotEmptyFooter();
//            }
//        });


        RadioGroup radioGroup = findViewById(R.id.rad_radgroupReceipt);
        final RadioButton headerRadioButton = findViewById(R.id.rad_header);
        final RadioButton footerRadioButton = findViewById(R.id.rad_footer);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rad_header) {
                    headerRadioButton.setChecked(true);
                    footerRadioButton.setChecked(false);
                    radValue = 0;
                    showSummary();
                } else if (checkedId == R.id.rad_footer) {
                    headerRadioButton.setChecked(false);
                    footerRadioButton.setChecked(true);
                    radValue = 1;
                    showSummary();
                }
            }
        });

        loadData();
        showSummary();
        KeyBoardMap();
        InitT9MapCode();

    }

    @Override
    public void onBackPressed() {
        //alertdialogView=0;
        //super.onBackPressed();
        // Check if the AlertDialog is currently showing
        if (alertDialog != null && alertDialog.isShowing()) {
            // Do nothing or add custom logic as needed
        } else {
            // If no AlertDialog is showing, proceed with the default behavior
            super.onBackPressed();
        }

    }

    private void printTest(){
        if (radValue==0){
            Header();
        }
        else{
            Footer();
        }
    }
    private void deleteItem(){
        Log.e("Final Delete ID",String.valueOf(headerFooterID.get(x).toString()));
        deleteID=headerFooterID.get(x);
        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);

        if (radValue==0){
            String deleteInvoiceReceiptItem= "delete from receiptHeader where HeaderID='"+deleteID+"'";
            PosSettings.execSQL(deleteInvoiceReceiptItem);
        }
        else{
//            String deleteInvoiceReceiptItem= "delete from receiptFooter where FooterID='"+deleteID+"'";
//            PosSettings.execSQL(deleteInvoiceReceiptItem);

            // Assuming deleteID is an integer
            //int deleteID = Integer.parseInt(/* your deleteID value */);

            if (deleteID >= 1 && deleteID <= 10) {
                // Do not delete footer IDs 1-10
                // You can show a message or take appropriate action here
                Toast.makeText(getApplicationContext(), "Cannot delete footer IDs 1-10", Toast.LENGTH_SHORT).show();
            } else {
                String deleteInvoiceReceiptItem = "DELETE FROM receiptFooter WHERE FooterID='" + deleteID + "'";
                PosSettings.execSQL(deleteInvoiceReceiptItem);
            }
        }
        showSummary();
    }
    private void setupRadioGroupListener() {
        rad_radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                handleRadioButtonCheckedChange(i);
            }
        });
    }
    private void handleRadioButtonCheckedChange(int checkedId) {
        Log.d("TAG", "handleRadioButtonCheckedChange: "+ checkedId);
        if (checkedId == R.id.rad_header || checkedId == 0) {
            Log.e("RADIOBUTTON", "0");
            rad_header.setChecked(true);
            rad_footer.setChecked(false);
            radValue = 0;
            showSummary();
        } else if (checkedId == R.id.rad_footer || checkedId == 1) {
            Log.e("RADIOBUTTON", "1");
            rad_footer.setChecked(true);
            rad_header.setChecked(false);
            radValue = 1;
            showSummary();
        }

    }






    //region Keyboard function


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
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD0, "0 -_!:");
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
//    public void DoInput(int nKeyIndex, int nCnt) {
//        int nLayer = nCnt > 0 ? nCnt - 1 : 0;  // Ensure nLayer is at least 0
//
//        if (mapCode.containsKey(nKeyIndex)) {
//            try {
//                String codeT9 = mapCode.get(nKeyIndex);
//                if (codeT9 != null) {
//                    List<String> lstSel = new ArrayList<>();
//                    char[] charT9 = codeT9.toCharArray();
//
//                    for (char chInput : charT9) {
//                        lstSel.add(String.valueOf(chInput));
//                    }
//
//                    String selIn = nLayer < lstSel.size() ? lstSel.get(nLayer) : lstSel.get(lstSel.size() - 1);
//
//                    // Append the selected letter to the existing text
//                    et_addNewText.append(selIn);
//
//                    // Log or do other actions if needed
//                    // Log.i(TAG, String.format("Do T9Input:%d %d %s", nKeyIndex, nCnt, selIn));
//                    // PostMessage(EXECUTE_MSG, selIn);
//
//                    // Increment nLayer for the next letter
//                    nLayer++;
//
//                    // Reset nCnt and call the method again to display the next letter
//                    if (nLayer >= lstSel.size()) {
//                        nCnt++;
//                        DoInput(nKeyIndex, nCnt);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }

    public void DoInput(int nKeyIndex, int nCnt) {
        int nLayer = nCnt > 0 ? nCnt - 1 : 0;

        if (mapCode.containsKey(nKeyIndex)) {
            try {
                String codeT9 = mapCode.get(nKeyIndex);
                if (codeT9 != null) {
                    List<String> lstSel = new ArrayList<>();
                    char[] charT9 = codeT9.toCharArray();

                    for (char chInput : charT9) {
                        lstSel.add(String.valueOf(chInput));
                    }


                    String selIn = nLayer < lstSel.size() ? lstSel.get(nLayer) : lstSel.get(lstSel.size() - 1);

                    et_addNewText.getText().insert(et_addNewText.getSelectionStart(), selIn);

                    nLayer++;

                    if (nLayer >= lstSel.size()) {
                        nCnt++;
                        DoInput(nKeyIndex, nCnt);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public void PostMessage(int nMsg, String strMessage) {
        if (et_addNewText.hasFocus()){
            int start = Math.max(et_addNewText.getSelectionStart(), 0);
            int end = Math.max(et_addNewText.getSelectionEnd(), 0);
            et_addNewText.getText().replace(Math.min(start, end), Math.max(start, end),
                    strMessage, 0, strMessage.length());
        }




    }

    //endregion


    int selectedItemPosition=0;
    int dropDown=0; //if =1 means focusing on spinnerRemarks
    int newPosition;
    int btnFocus; //1 cancel,2 proces, 0 null
    String DialogView="";
    int x=-1;

    //region keyboard control
    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        String input = kManager.getDefaultKeyName(keyCode);
      //  Toast.makeText(context1, input, Toast.LENGTH_SHORT).show();

        switch(input){
            case "EC":
                ECFunction();
                break;


            case "Total":

                totalFunction();


                break;

            case "Exit":
              //  pushCloseKeyboard();
              //  closeKeyboard();
                if (alertDialog!=null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                }else {


                    onBackPressed();
                }
                Log.d("TAG", "SimulateKeyboard: Exit");
                break;

            case "Page UP":
                //functionPageUP();
                navigateRight();
                break;

            case "Page DN":
                navigateLeft();
               // functionPageDN();
                break;

            case "D01":
                D01Function();
                break;

            case "D05":
                //insertNewItem();
                D05Function();
                break;

            case "D09":
                D09Function();
                //deleteItem();
                break;

            case "D13":
                D13Function();
               // printTest();
                break;


            case "RA":
                handleRadioButtonCheckedChange(0);
                break;

            case "PO":
                handleRadioButtonCheckedChange(1);
                break;

            case "Sub Total":
                break;








        }









    }

    //note!

    int keyboardFlg=0;
    private void totalFunction(){


        if (alertDialog != null && alertDialog.isShowing()) {
            // Do nothing or add custom logic as needed
           // functionTab();

            saveButton(saveBtn);
            alertDialog.dismiss();


        } else {
            // If no AlertDialog is showing, proceed with the default behavior
            RecyclerviewAdapter myAdapter = (RecyclerviewAdapter) mAdapter;
            myAdapter.openUpdateDialog(x);
            Log.d("SELECTED ID",headerFooterID.get(x).toString());
        }


    }
    private void D01Function(){
        if (alertDialog != null && alertDialog.isShowing()) {
            // Do nothing or add custom logic as needed
            functionTab();
        } else {
            // If no AlertDialog is showing, proceed with the default behavior
            deleteItem();
        }
    }

    private void ECFunction(){
        int cursorPosition = et_addNewText.getSelectionStart();
        int length = et_addNewText.getText().length();

        if (cursorPosition > 0 && cursorPosition <= length) {
            // Delete the character at the current cursor position
            et_addNewText.getText().delete(cursorPosition - 1, cursorPosition);
        }
    }
    private void D05Function(){
//        if (alertdialogView==1){
//            functionLeft();
//        }else{
//            insertNewItem();
//        }


        if (alertDialog != null && alertDialog.isShowing()) {
            // Do nothing or add custom logic as needed
            functionLeft();
        } else {
            // If no AlertDialog is showing, proceed with the default behavior
         //  functionRight();
            insertNewItem();

        }

    }
    int saveBtn; //0 addnewitem //1 updateitem
    private void D09Function(){
//        if (alertdialogView==1){
//            functionCenter();
//        }
//        else{
//            deleteItem();
//        }
        if (alertDialog != null && alertDialog.isShowing()) {
            // Do nothing or add custom logic as needed
            functionCenter();
        } else {
            // If no AlertDialog is showing, proceed with the default behavior
deleteItem();
        }
    }
    private void D13Function(){
//        if (alertdialogView==1){
//            functionRight();
//        }else{
//            printTest();
//        }

        if (alertDialog != null && alertDialog.isShowing()) {
            // Do nothing or add custom logic as needed
            functionRight();
        } else {
            // If no AlertDialog is showing, proceed with the default behavior
            printTest();
        }

    }
    private void functionPageUP(){
        if (x > 0) {
            x = x - 1; // Decrease the selected position
            RecyclerviewAdapter myAdapter = (RecyclerviewAdapter) mAdapter;
            myAdapter.setSelectedPosition(x);
            Log.d("TAG", "PAGE UP" + x);
            rv_headerFooterList.smoothScrollToPosition(x);
            Log.d("SELECTED ID",headerFooterID.get(x).toString());
        }
    }
    private void functionPageDN(){
        RecyclerviewAdapter myAdapter = (RecyclerviewAdapter) mAdapter;

        if (x < myAdapter.getItemCount() - 1) {
            x = x + 1;
            myAdapter.setSelectedPosition(x);
            Log.d("TAG", "PAGE DOWN" + x);
            rv_headerFooterList.smoothScrollToPosition(x);


        }
    }
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
        else{


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
    private void functionTab(){




        String textLength = et_addNewText.getText().toString();
        Log.d("TAG", "Text Lenght : " + textLength.length());
       // textLength = textLength.trim();
        textLength = "     " + textLength;
        et_addNewText.setText(textLength);



    }
    private void functionLeft(){
        String textLength = et_addNewText.getText().toString();
        Log.d("TAG", "Text Lenght : " + textLength.length());
        textLength = textLength.trim();

        et_addNewText.setText(textLength);
    }
    private void functionRight(){
        String textLength = et_addNewText.getText().toString();
        Log.d("TAG", "Text Lenght : " + textLength.length());
        textLength = textLength.trim();

        Log.d("TAG", "Text Trim : " + textLength.length());
        int txLength = textLength.length();
        int maxChar = 32;
        int charLeft =  (maxChar-txLength);
        // int charRight = maxChar - txLength - charLeft;
        String spaceLeft="";
        for(int x=0;x<charLeft;x++){
            spaceLeft = spaceLeft + " ";
        }

        String finalText = spaceLeft + textLength;
        Log.d("TAG", "Final Text :" + finalText);
        et_addNewText.setText(finalText);
    }
    private void functionCenter(){

        String textLength = et_addNewText.getText().toString();
        Log.d("TAG", "Text Lenght : " + textLength.length());
        textLength = textLength.trim();

        Log.d("TAG", "Text Trim : " + textLength.length());
        int txLength = textLength.length();
        int maxChar = 32;
        int charLeft =  (maxChar-txLength)/2;
        int charRight = maxChar - txLength - charLeft;
        String spaceLeft="";
        for(int x=0;x<charLeft;x++){
            spaceLeft = spaceLeft + " ";
        }

        String spaceRight="";
        for(int x=0;x<charRight;x++){
            spaceRight = spaceRight + " ";
        }
        String finalText = spaceLeft + textLength + spaceRight;
        Log.d("TAG", "Final Text :" + finalText);
        et_addNewText.setText(finalText);


    }

    private void navigateLeft(){
        if (alertDialog != null && alertDialog.isShowing()) {
            int cursorPosition = et_addNewText.getSelectionStart();

            // Move the cursor one position to the left
            if (cursorPosition > 0) {
                et_addNewText.setSelection(cursorPosition - 1);
            }
        }
        else{
            functionPageDN();

        }
    }

    private void navigateRight(){

        if (alertDialog != null && alertDialog.isShowing()) {
            int cursorPosition = et_addNewText.getSelectionStart();

            // Move the cursor one position to the left
            if (cursorPosition > 0) {
                et_addNewText.setSelection(cursorPosition + 1);
            }
        }
        else{
            functionPageUP();
        }
    }

    private void saveButton( int btn){

        if (btn==0) {
            textToAdd = et_addNewText.getText().toString();
//        if (cbx_doubleWidth.isChecked()){
            doubleWidth = "YES";
//        }
//        else{
//            doubleWidth="NO";
//        }

            if (radValue == 0) {
                insertReceiptSettings();
                showSummary();
            } else {
                insertReceiptSettingsFooter();
                showSummary();
            }

            alertDialog.dismiss();
        }
        else{

            Log.e("Final Update ID",String.valueOf(deleteID));
            SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);

                doubleWidth="YES";

            if (radValue==0){
                String updateReceiptHeader= "update receiptHeader set HeaderContent='"+ et_addNewText.getText() +"',HeaderDouble='"+doubleWidth+"'where HeaderID='"+deleteID+"'";
                PosSettings.execSQL(updateReceiptHeader);
            }
            else{
                String updateReceiptFooter= "update receiptFooter set FooterContent='"+ et_addNewText.getText() +"',FooterDouble='"+doubleWidth+"'where FooterID='"+deleteID+"'";
                PosSettings.execSQL(updateReceiptFooter);
            }
            showSummary();
            PosSettings.close();
        }
    }






//endregion


    private void Header(){

        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);


        Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader", null);
        if (getTextToUpdate.getCount()!=0){
            StringBuffer buffer = new StringBuffer();
            while (getTextToUpdate.moveToNext()){
                buffer.append(getTextToUpdate.getString(1) + "\n");
            }

//            Log.e("TESTPRINT","HEADER");
//            printer_settings_class prnDsp=new printer_settings_class(this);
//            prnDsp.OnlinePrinter(buffer.toString(),1,0,1);

            Log.d("TAG", "Header: "+ "\n" + buffer.toString());

            printer_settings_class PrinterSettings = new printer_settings_class(this.getApplicationContext());
            PrinterSettings.OnlinePrinter(buffer.toString(), 1, 0, 1);
//            PrnDspA1088 prn2 = new PrnDspA1088(this);
//            //prn2.PRN_PrintText("testSht \n \n \n \n \n \n \n ",1,1,1);
//            prn2.DSP_LED_SetStatus(51);


        }




        PosSettings.close();





    }
    private void Footer(){

        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);


        Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptFooter", null);
        if (getTextToUpdate.getCount()!=0){
            StringBuffer buffer = new StringBuffer();
            while (getTextToUpdate.moveToNext()){
                buffer.append(getTextToUpdate.getString(1) + "\n");
                //HeaderText = HeaderText + getTextToUpdate.getString(1) + "\n";
            }

            printer_settings_class PrinterSettings = new printer_settings_class(this.getApplicationContext());
            PrinterSettings.OnlinePrinter(buffer.toString(), 1, 0, 1);
        }




        PosSettings.close();





    }




    String text;
    int charLeft;
    int charRight;
    int totalChar=32;
    int finalTotalChar;
    String TextLeft;
    String TextRight="";


    private void insertNewItem(){
        saveBtn=0;
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_update_headerfooter2, null);
        builder.setView(alertLayout);
         alertDialog = builder.create();
      //  Button btn_yes = alertLayout.findViewById(R.id.btn_yes);
        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
         et_addNewText= alertLayout.findViewById(R.id.et_addNewText);
        Button btn_save = alertLayout.findViewById(R.id.btn_save);
        CheckBox cbx_doubleWidth=alertLayout.findViewById(R.id.cbx_doubleWidth);

        ImageButton ib_left = alertLayout.findViewById(R.id.ib_left);
        ImageButton ib_right = alertLayout.findViewById(R.id.ib_right);
        ImageButton ib_center = alertLayout.findViewById(R.id.ib_center);
        ImageButton ib_tab = alertLayout.findViewById(R.id.ib_tab);



      //  et_addNewText.setText(finalReceiptDisplay);
        et_addNewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        et_addNewText.setGravity(Gravity.LEFT);  // Adjust as needed
        et_addNewText.setPadding(0, 0, 0, 0);    // Adjust as needed
        et_addNewText.setTypeface(Typeface.create("monospace", Typeface.NORMAL));

        et_addNewText.requestFocus();


        ib_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionCenter();
            }
        });


        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionLeft();
            }
        });


        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionRight();
            }
        });

        ib_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                functionTab();
            }
        });





        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textToAdd=et_addNewText.getText().toString();
                if (cbx_doubleWidth.isChecked()){
                    doubleWidth="YES";
                }
                else{
                    doubleWidth="NO";
                }

                if (radValue==0){
                    insertReceiptSettings();
                    showSummary();
                }
                else{
                    insertReceiptSettingsFooter();
                    showSummary();
                }

                alertDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToAdd="";
                doubleWidth="NO";
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }

//    private void updateItem(){
//        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_update_headerfooter, null);
//        builder.setView(alertLayout);
//        AlertDialog alertDialog = builder.create();
//        //  Button btn_yes = alertLayout.findViewById(R.id.btn_yes);
//        EditText et_addNewText= alertLayout.findViewById(R.id.et_addNewText);
//        Button btn_save = alertLayout.findViewById(R.id.btn_save);
//        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
//        Button btn_delete = alertLayout.findViewById(R.id.btn_delete);
//        Button btn_alignCenter=alertLayout.findViewById(R.id.btn_alignCenter);
//        CheckBox cbx_doubleWidth=alertLayout.findViewById(R.id.cbx_doubleWidth);
//        Button btn_tab = alertLayout.findViewById(R.id.btn_tab);
//
//
//        et_addNewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//        et_addNewText.setGravity(Gravity.LEFT);  // Adjust as needed
//        et_addNewText.setPadding(0, 0, 0, 0);    // Adjust as needed
//        et_addNewText.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
//
//
//
//        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
//
//        if (radValue==0){
//            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader where HeaderID='"+deleteID+"'", null);
//            if (getTextToUpdate.getCount()!=0){
//                while (getTextToUpdate.moveToNext()){
//                    et_addNewText.setText(getTextToUpdate.getString(1));
//                    if (getTextToUpdate.getString(2).equals("YES")){
//                        cbx_doubleWidth.setChecked(true);
//                    }
//                    else{
//                        cbx_doubleWidth.setChecked(false);
//                    }
//                }
//                text=et_addNewText.getText().toString();
//
//            }
//        }
//        else{
//            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptFooter where FooterID='"+deleteID+"'", null);
//            if (getTextToUpdate.getCount()!=0){
//                while (getTextToUpdate.moveToNext()){
//                    et_addNewText.setText(getTextToUpdate.getString(1));
//                    if (getTextToUpdate.getString(2).equals("YES")){
//                        cbx_doubleWidth.setChecked(true);
//                    }
//                    else{
//                        cbx_doubleWidth.setChecked(false);
//                    }
//                }
//            }
//            text=et_addNewText.getText().toString();
//        }
//
//
//        btn_alignCenter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                centerFunction();
//                et_addNewText.setText(TextLeft+text.trim());
//            }
//        });
//
//
//
//        PosSettings.close();
//
//
//
//
//
//
//
//
//        btn_tab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                et_addNewText.setText(et_addNewText.getText().toString()+"     ");
//                et_addNewText.setSelection(et_addNewText.getText().length());
//            }
//        });
//
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.e("Final Update ID",String.valueOf(deleteID));
//                SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
//                if (cbx_doubleWidth.isChecked()){
//                    doubleWidth="YES";
//                }
//                else{
//                    doubleWidth="NO";
//                }
//
//                if (radValue==0){
//                    String updateReceiptHeader= "update receiptHeader set HeaderContent='"+ et_addNewText.getText() +"',HeaderDouble='"+doubleWidth+"'where HeaderID='"+deleteID+"'";
//                    PosSettings.execSQL(updateReceiptHeader);
//                }
//                else{
//                    String updateReceiptFooter= "update receiptFooter set FooterContent='"+ et_addNewText.getText() +"',FooterDouble='"+doubleWidth+"'where FooterID='"+deleteID+"'";
//                    PosSettings.execSQL(updateReceiptFooter);
//                }
//                showSummary();
//                PosSettings.close();
//
//
//
//            }
//        });
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                textToAdd="";
//                doubleWidth="NO";
//                alertDialog.dismiss();
//            }
//        });
//
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.e("Final Delete ID",String.valueOf(deleteID));
//                SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
//
//                if (radValue==0){
//                    String deleteInvoiceReceiptItem= "delete from receiptHeader where HeaderID='"+deleteID+"'";
//                    PosSettings.execSQL(deleteInvoiceReceiptItem);
//                }
//                else{
//                    String deleteInvoiceReceiptItem= "delete from receiptFooter where FooterID='"+deleteID+"'";
//                    PosSettings.execSQL(deleteInvoiceReceiptItem);
//                }
//                showSummary();
//
//            }
//        });
//
//
//        alertDialog.show();
//    }

    private void updateItem(){

       // alertdialogView=1;
        saveBtn=1;
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_update_headerfooter2, null);
        builder.setView(alertLayout);


         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Handle back button press inside the AlertDialog
                    // You can add custom logic here if needed
                    return true; // Consume the back button press
                }
                return false;
            }
        });


        //  Button btn_yes = alertLayout.findViewById(R.id.btn_yes);
         et_addNewText= alertLayout.findViewById(R.id.et_addNewText);
        Button btn_save = alertLayout.findViewById(R.id.btn_save);
        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);


       // Button btn_alignCenter=alertLayout.findViewById(R.id.btn_alignCenter);
        CheckBox cbx_doubleWidth=alertLayout.findViewById(R.id.cbx_doubleWidth);
//


        //pushCloseKeyboard();

       // Button btn_tab = alertLayout.findViewById(R.id.btn_tab);


            ImageButton ib_left = alertLayout.findViewById(R.id.ib_left);
            ImageButton ib_right = alertLayout.findViewById(R.id.ib_right);
            ImageButton ib_center = alertLayout.findViewById(R.id.ib_center);
            ImageButton ib_tab = alertLayout.findViewById(R.id.ib_tab);



            et_addNewText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        et_addNewText.setGravity(Gravity.LEFT);  // Adjust as needed
        et_addNewText.setPadding(0, 0, 0, 0);    // Adjust as needed
        et_addNewText.setTypeface(Typeface.create("monospace", Typeface.NORMAL));






        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);

        if (radValue==0){
            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader where HeaderID='"+deleteID+"'", null);
            if (getTextToUpdate.getCount()!=0){
                while (getTextToUpdate.moveToNext()){
                    et_addNewText.setText(getTextToUpdate.getString(1));
                    if (getTextToUpdate.getString(2).equals("YES")){
                        cbx_doubleWidth.setChecked(true);
                    }
                    else{
                        cbx_doubleWidth.setChecked(false);
                    }
                }
                text=et_addNewText.getText().toString();

            }
        }
        else{
            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptFooter where FooterID='"+deleteID+"'", null);
            if (getTextToUpdate.getCount()!=0){
                while (getTextToUpdate.moveToNext()){
                    et_addNewText.setText(getTextToUpdate.getString(1));
                    if (getTextToUpdate.getString(2).equals("YES")){
                        cbx_doubleWidth.setChecked(true);
                    }
                    else{
                        cbx_doubleWidth.setChecked(false);
                    }
                }
            }
            text=et_addNewText.getText().toString();
        }


        et_addNewText.requestFocus();
        et_addNewText.setSelection(et_addNewText.getText().length());
        pushCloseKeyboard();



        ib_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               functionCenter();
            }
        });


            ib_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   functionLeft();
                }
            });


            ib_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   functionRight();
                }
            });

            ib_tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    functionTab();
                }
            });


//        btn_alignCenter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                centerFunction();
//                et_addNewText.setText(TextLeft+text.trim());
//            }
//        });



        PosSettings.close();
//
//        btn_tab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                et_addNewText.setText(et_addNewText.getText().toString()+"     ");
//                et_addNewText.setSelection(et_addNewText.getText().length());
//            }
//        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                Log.e("Final Update ID",String.valueOf(deleteID));
                SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
                if (cbx_doubleWidth.isChecked()){
                    doubleWidth="YES";
                }
                else{
                    doubleWidth="NO";
                }

                if (radValue==0){

//                    String content = et_addNewText.getText().toString();
//                    String updateReceiptHeader= "update receiptHeader set HeaderContent='"+ content +"',HeaderDouble='"+doubleWidth+"'where HeaderID='"+deleteID+"'";
//                    PosSettings.execSQL(updateReceiptHeader);


                    String content = et_addNewText.getText().toString();
                    String updateReceiptHeader = "UPDATE receiptHeader SET HeaderContent=?, HeaderDouble=? WHERE HeaderID=?";
                    SQLiteStatement statement = PosSettings.compileStatement(updateReceiptHeader);
                    statement.bindString(1, content);
                    statement.bindString(2, doubleWidth);
                    statement.bindString(3, String.valueOf(deleteID));
                    statement.execute();

                }
                else{
//                    String updateReceiptFooter= "update receiptFooter set FooterContent='"+ et_addNewText.getText().toString() +"',FooterDouble='"+doubleWidth+"'where FooterID='"+deleteID+"'";
//                    PosSettings.execSQL(updateReceiptFooter);

                    if (deleteID >= 1 && deleteID <= 10) {
                        // Do not update footer IDs 1-10
                        // You can show a message or take appropriate action here
                        Toast.makeText(getApplicationContext(), "Cannot update footer IDs 1-10", Toast.LENGTH_SHORT).show();
                    } else {
                       String updateReceiptFooter = "UPDATE receiptFooter SET FooterContent='" + et_addNewText.getText().toString() + "', FooterDouble='" + doubleWidth + "' WHERE FooterID='" + deleteID + "'";
                        PosSettings.execSQL(updateReceiptFooter);
                    }
                }
                showSummary();
                PosSettings.close();



            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToAdd="";
                doubleWidth="NO";
                alertDialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Final Delete ID",String.valueOf(deleteID));
                SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);

                if (radValue==0){
                    String deleteInvoiceReceiptItem= "delete from receiptHeader where HeaderID='"+deleteID+"'";
                    PosSettings.execSQL(deleteInvoiceReceiptItem);
                }
                else{
                    String deleteInvoiceReceiptItem= "delete from receiptFooter where FooterID='"+deleteID+"'";
                    PosSettings.execSQL(deleteInvoiceReceiptItem);
                }
                showSummary();

            }
        });


        alertDialog.show();
    }
    private void insertReceiptSettings() {
        DatabaseHandlerSettings dbSettings= new DatabaseHandlerSettings(this);

        boolean isInserted = dbSettings.insertHeaders(
                textToAdd,
                doubleWidth
        );
        if (isInserted = true) {
            Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();
//            textToAdd="";
//            doubleWidth="NO";
        }
        else{
            Toast.makeText(this, "NOT INSERTED", Toast.LENGTH_SHORT).show();
//            textToAdd="";
//            doubleWidth="NO";

        }
    }

    private void insertReceiptSettingsFooter() {
        DatabaseHandlerSettings dbSettings= new DatabaseHandlerSettings(this);

        boolean isInserted = dbSettings.insertFooters(
                textToAdd,
                doubleWidth


        );
        if (isInserted = true) {
            Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "NOT INSERTED", Toast.LENGTH_SHORT).show();

        }
    }
    private void checkIfnotEmpty(){
        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select * from receiptHeader", null);
        if (item1.getCount()==0){
            insertReceiptSettings();
        }
        else{
           // SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            String strsql = "UPDATE receiptHeader SET " +
                    "HeaderContent='" + HeaderContent + "'" +


                    " where HeaderID='01'";
            db2.execSQL(strsql);
        }

    }
    private void checkIfnotEmptyFooter(){
        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select * from receiptFooter", null);
        if (item1.getCount()==0){
            insertReceiptSettingsFooter();
        }
        else{
            // SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            String strsql = "UPDATE receiptFooter SET " +
                    "FooterContent='" + footer + "'" +


                    " where FooterID='01'";
            db2.execSQL(strsql);
        }

    }

    private void loadData(){
        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select * from receiptHeader", null);
        Cursor item2 = db2.rawQuery("select * from receiptFooter", null);
        if (item1.getCount()==0){

        }else{
           while (item1.moveToNext()){
              // et_HeaderContent.setText(item1.getString(1));

           }
        }
        if (item2.getCount()==0){

        }else{
            while (item2.moveToNext()){
                //et_footer.setText(item2.getString(1));

            }
        }
    }


    private ArrayList<Integer> headerFooterID;
    private ArrayList<String> headerFooterText;
    private ArrayList<String> headerFooterWidth;
    List<HeaderFooterItem> HeaderFooterList = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();
    String DB_NAME2 = "PosSettings.db";
    Cursor itemListC;
    RecyclerView rv_headerFooterList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    private void showSummary(){
        fillOrderList();
        refreshRecycleview();







    }

    private void resetAutoIncrementHeader(){

        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select Count(HeaderID) from receiptHeader", null);
        if(item1.getCount()!=0){

        }


        db2.close();

    }

    private void fillOrderList() {
        headerFooterID = new ArrayList<>();
        headerFooterText = new ArrayList<>();
        headerFooterWidth = new ArrayList<>();
        HeaderFooterList.clear();

        CheckItemDatabase2();
        TextView tv_receiptDisplay = findViewById(R.id.tv_receiptDisplay);

        int numberOfItem = itemListC.getCount();
        int Ctr=0;
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){

            Ctr++;

           // int ID= headerFooterID.get(itemCounter);
            String itemText=headerFooterText.get(itemCounter);
            String itemDouble=headerFooterWidth.get(itemCounter);




            HeaderFooterItem p0=new HeaderFooterItem(Ctr,itemText,itemDouble);







            finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

            HeaderFooterList.addAll(Arrays.asList(new HeaderFooterItem[]{p0}));

        }

        tv_receiptDisplay.setText((finalReceiptDisplay));
        tv_receiptDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv_receiptDisplay.setGravity(Gravity.LEFT);  // Adjust as needed
        tv_receiptDisplay.setPadding(0, 0, 0, 0);    // Adjust as needed
        tv_receiptDisplay.setTypeface(Typeface.create("monospace", Typeface.NORMAL));


       // tv_receiptDisplay.setText(finalReceiptDisplay);
        finalReceiptDisplay="";

    }
    private void CheckItemDatabase2() {



        headerFooterID.clear();
        headerFooterText.clear();
        headerFooterWidth.clear();


        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        if (radValue==0){
            itemListC = db2.rawQuery("select * from receiptHeader", null);
        }
        else{
            itemListC = db2.rawQuery("select * from receiptFooter", null);
        }



        if (itemListC.getCount() == 0) {
            // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }

        while(itemListC.moveToNext()){
            headerFooterID.add(itemListC.getInt(0));
            headerFooterText.add(itemListC.getString(1));
            headerFooterWidth.add(itemListC.getString(2));


        }
        itemListC.close();
        db2.close();









    }
    public void refreshRecycleview(){
        rv_headerFooterList = findViewById(R.id.rv_headerFooterList);
        rv_headerFooterList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        rv_headerFooterList.setLayoutManager(layoutManager);
        mAdapter=new admin_manage_receipt.RecyclerviewAdapter(HeaderFooterList,selectList,this.getApplicationContext());

        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());


        rv_headerFooterList.setAdapter(mAdapter);





        //invoice item list



    }

    //note!

    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_manage_receipt.RecyclerviewAdapter.MyViewHolder>{
        List<HeaderFooterItem> HeaderFooterList;
        Context context;



        private ActionBar.OnNavigationListener navigationListener;
        ArrayList<String> selectList = new ArrayList<>();
        private MyViewHolder holder;
        private int position;


        private int selectedPosition = RecyclerView.NO_POSITION;

        private int selectedID;

        public void openUpdateDialog(int itemID){
            int id = (itemID);
            for (int x=0;x<headerFooterID.size();x++){

                Log.d("ID",headerFooterID.get(x).toString());



                if (x==id){
                    Log.d("SELECTED ID",headerFooterID.get(id).toString());

                    //  Log.e("item id",tv_itemID.getText().toString());
                    deleteID=Integer.parseInt(headerFooterID.get(id).toString());
                    updateItem();
                    // return true;


                }
                else{
                    Log.d("SELECTED ID ELSE","".toString());
                }
            }
        }

        // ... existing code ...

        public void setSelectedPosition(int position) {
            selectedPosition = position;

//            Log.d("SELECTED ID",headerFooterID.get(position).toString());
            notifyDataSetChanged();
        }
        public RecyclerviewAdapter(List<HeaderFooterItem> HeaderFooterList, ArrayList<String> selectList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.HeaderFooterList = HeaderFooterList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_manage_receipt.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_footer_layout,parent,false);



            admin_manage_receipt.RecyclerviewAdapter.MyViewHolder holder = new admin_manage_receipt.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_manage_receipt.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
         //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

            holder.tv_itemID.setText(String.valueOf((HeaderFooterList.get(position).getItemID())));
            holder.tv_itemText.setText((String.valueOf(HeaderFooterList.get(position).getItemText())));
            holder.tv_itemDoubleWeight.setText((String.valueOf(HeaderFooterList.get(position).getItemDoubleWidth())));


            if (position == selectedPosition) {
              //  holder.ll_linearLayout1.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
              //  holder.ll_linearLayout2.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
                holder.tv_itemID.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
                holder.tv_itemText.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
                holder.tv_itemDoubleWeight.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));




            } else {
              //  holder.ll_linearLayout1.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
               // holder.ll_linearLayout2.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                holder.tv_itemID.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.tv_itemText.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.tv_itemDoubleWeight.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }




        }

        @Override
        public int getItemCount() {
            return HeaderFooterList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_itemID;
            TextView tv_itemText;
            TextView tv_itemDoubleWeight;
            ImageView iv_check;
            LinearLayout ll_linearLayout1;
            LinearLayout ll_linearLayout2;

            //CardView parentLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_itemID = itemView.findViewById(R.id.tv_itemID);
                tv_itemText = itemView.findViewById(R.id.tv_itemText);
                tv_itemDoubleWeight = itemView.findViewById(R.id.tv_itemDoubleWidth);
                iv_check = itemView.findViewById(R.id.iv_check);
                ll_linearLayout1 = itemView.findViewById(R.id.ll_linearLayout1);
                ll_linearLayout2 = itemView.findViewById(R.id.ll_linearLayout2);
               // parentLayout = itemView.findViewById(R.id.linear_orderlist_layout2);


                tv_itemText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tv_itemText.setGravity(Gravity.LEFT);  // Adjust as needed

                tv_itemText.setPadding(1, 1, 1, 1);    // Adjust as needed
                tv_itemText.setTypeface(Typeface.create("monospace", Typeface.NORMAL));


                tv_itemID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int id = Integer.parseInt(tv_itemID.getText().toString())-1;
                        for (int x=0;x<headerFooterID.size();x++){

                            //Log.d("ID",headerFooterID.get(x).toString());



                            if (x==id){
                                Log.d("SELECTED ID",headerFooterID.get(id).toString());
                            }
                        }




                    }
                });
                tv_itemText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {



                        int id = Integer.parseInt(tv_itemID.getText().toString())-1;
                        for (int x=0;x<headerFooterID.size();x++){

                            //Log.d("ID",headerFooterID.get(x).toString());



                            if (x==id){
                                Log.d("SELECTED ID",headerFooterID.get(id).toString());

                              //  Log.e("item id",tv_itemID.getText().toString());
                                deleteID=Integer.parseInt(headerFooterID.get(id).toString());
                                updateItem();
                                return true;


                            }
                        }


                        return false;

                    }
                });










            }





        }

    }






}