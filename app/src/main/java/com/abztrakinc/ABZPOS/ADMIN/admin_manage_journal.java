package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Printer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.CASHIER.cashier_cash_item;
import com.abztrakinc.ABZPOS.CASHIER.create_journal_entry;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.POSAndroidDB;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.backup_usb_path;
import com.abztrakinc.ABZPOS.pos_user;
import com.abztrakinc.ABZPOS.system_final_date;
import com.abztrakinc.ABZPOS.utils.SunmiPrintHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class admin_manage_journal extends AppCompatActivity {

    SQLiteDatabase db;
    String DB_NAME = "PosOutputDB.db";
    String DB_NAME2 = "PosSettings.db";
    String posSettingsdb = "PosSettings.db";
    Cursor cursor,item2,itemNameSelect,itemNameWDiscount;
    TextView tv_transNo, tv_transType, tv_dateTime, tv_journalText;
    ArrayList<String> transactionNo;//id of category
    ArrayList<String> transactionType;//name of category
    ArrayList<String> transactionDateTime;
    ArrayList<String> itemName;
    Spinner sp_from;
    Spinner sp_to;

    String HeaderContent, footer;
    String   ORNumber,TransNumber,  cashierName, posNumber,TransType;
    String TotalAmount,Cash,Change,ServiceCharge,VatableSales,VatAmount,VatExemptSale,ZeroRatedSales;
    String date,time;

    String shift;
    String TotalItem;
    Button btn_reprint;
    Button btn_generateJournal;
    Button btn_printJournal;
    //SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
    final Calendar myCalendar= Calendar.getInstance();
    Date currentDate = Calendar.getInstance().getTime();
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    com.abztrakinc.ABZPOS.pos_user pos_user = new pos_user();

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    StringBuffer buffer;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    cashier_cash_item cashItem;
    EditText et_from,et_to;
    ImageButton ib_return;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_journal);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar_journal = findViewById(R.id.toolbar_journal);
        setSupportActionBar(toolbar_journal);

        transactionNo = new ArrayList<String>();
        transactionType = new ArrayList<String>();
        transactionDateTime = new ArrayList<String>();
        cashItem = new cashier_cash_item();

        btn_generateJournal = findViewById(R.id.btn_generateJournal);
        btn_generateJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            generateJournal();
            Log.e("TEST","JOURNAL");


            }
        });

        ib_return=findViewById(R.id.ib_return);
        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btn_printJournal = findViewById(R.id.btn_printJournal);
        btn_printJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PrintJournal();
                Log.e("Printing","JOURNAL");


            }
        });


        et_from=(EditText) findViewById(R.id.et_from);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabelFrom();
            }
        };
        et_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(admin_manage_journal.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_to=(EditText) findViewById(R.id.et_to);
        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabelTo();
            }
        };
        et_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(admin_manage_journal.this,date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        KeyBoardMap();
        //        loadReceiptData();
//        showItem();
//        createList();





        getJournalList();

        loadSPinner();

        Button btn_printReceipt = findViewById(R.id.btn_printReceipt);
        btn_printReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printer_settings_class PrinterSettings = new printer_settings_class(getApplicationContext());
                PrinterSettings.OnlinePrinter(tv_journalText.getText().toString(),1,0,1);
            }
        });

//



    }

    //region new recycler view


    int spfrom;
    int spto;
    private void loadSPinner(){
        sp_from = (Spinner) findViewById(R.id.sp_from);
        sp_to = findViewById(R.id.sp_to);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_from.setAdapter(adapter);
        sp_to.setAdapter(adapter);

        sp_from.setSelection(0);
        sp_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the adapter

                spfrom=parent.getSelectedItemPosition();
                if (spfrom>spto){
                   // Toast.makeText(admin_manage_journal.this, "Please select date less than TO", Toast.LENGTH_SHORT).show();

                }else{
                    String selectedItem = (String) parent.getItemAtPosition(position);



                    // Do something with the selected item
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                    Log.d("Spinner", "Item selected: " + selectedItem);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        sp_to.setSelection(dateList.size()-1);
        sp_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the adapter
                spto=parent.getSelectedItemPosition();
                if (spfrom < spto){
                   // Toast.makeText(admin_manage_journal.this, "Please select date greater than from", Toast.LENGTH_SHORT).show();

                }
                else{
                    String selectedItem = (String) parent.getItemAtPosition(position);

                    // Do something with the selected item
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        Button btn_showList = findViewById(R.id.btn_showList);
        btn_showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                journalList.clear();
                int s = sp_from.getSelectedItemPosition();
                int x = sp_to.getSelectedItemPosition();
                for(int start =s;start<=x;start++){

                    admin_manage_journal_list p0 = null;
                    p0=new admin_manage_journal_list(
                            dateList.get(start),fileNameList.get(start),fileSizeList.get(start)
                    );

                    journalList.addAll(Arrays.asList(new admin_manage_journal_list[]{p0}));

                }




                refreshRecycleviewJournal();



            }
        });


    }

    ArrayList<String>dateList = new ArrayList<>();
    ArrayList<String>fileNameList = new ArrayList<>();
    ArrayList<String>fileSizeList = new ArrayList<>();
    RecyclerView rv_journalList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerviewAdapter adapter;
    List<admin_manage_journal_list> journalList = new ArrayList<admin_manage_journal_list>();
    String localConsolidateFile=(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Consolidate");

    private void getJournalList() {
        fileNameList.clear();
        fileSizeList.clear();


        String localConsolidateFile = Environment.getExternalStorageDirectory() + "/ANDROID_POS/Consolidate";
        File directory = new File(localConsolidateFile);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {

                        String extractedDate =  file.getName().replace("_Consolidate.txt","");
                        Date date = convertToDate(extractedDate);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                        String outputDate = outputFormat.format(date);
                        dateList.add(outputDate);

//                        p0=new admin_manage_journal_list(
//                                outputDate,file.getName(),String.valueOf(file.length())
//                        );

//                        journalList.addAll(Arrays.asList(new admin_manage_journal_list[]{p0}));

                        Log.d("TAG", "getJournalList: "+outputDate);
                        fileNameList.add(file.getName());
                        fileSizeList.add(String.valueOf(file.length()));

                    }
                }
            }
        }




    }
    private void showDate(){

    }

    public static Date convertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle parse exception
        }
    }

//    public void refreshRecycleviewJournal(){
//        rv_journalList = findViewById(R.id.rv_journalList);
//
//        rv_journalList.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        layoutManager.removeAllViews();
//        rv_journalList.setLayoutManager(layoutManager);
//        adapter=new admin_manage_journal.RecyclerviewAdapter(journalList,this.getApplicationContext());
//        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
//        rv_journalList.setAdapter(adapter);
//
//        //invoice item list
//
//
//
//    }

    public void refreshRecycleviewJournal() {
        rv_journalList = findViewById(R.id.rv_journalList);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_journalList.setLayoutManager(layoutManager);

        // Create a new adapter with updated data
        adapter = new admin_manage_journal.RecyclerviewAdapter(journalList, this.getApplicationContext());
        rv_journalList.setAdapter(adapter);

        // Scroll to the top position smoothly
        if (journalList.size() > 0) {
            rv_journalList.smoothScrollToPosition(0);
        }
    }


    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_manage_journal.RecyclerviewAdapter.MyViewHolder>{
        List<admin_manage_journal_list> JournalList;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_manage_journal.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<admin_manage_journal_list> journalList, Context context) {

            this.JournalList = journalList;

            this.context = context;

        }

        private int selectedPosition = RecyclerView.NO_POSITION;
//        public void setSelectedPosition(int position) {
//            selectedPosition = position;
//
////            Log.d("SELECTED ID",headerFooterID.get(position).toString());
//            notifyDataSetChanged();
//        }

//        public void openUpdateDialog(int itemID){
//            int id = (itemID);
//            for (int x=0;x<CategoryList.size();x++){
//
//                //  Log.d("ID",CategoryList.get(x).toString());
//
//
//
//                if (x==id){
//                    Log.d("SELECTED ID",CategoryList.get(id).toString());
//
//                    //  Log.e("item id",tv_itemID.getText().toString());
//                    // deleteID=Integer.parseInt(CategoryList.get(id).toString());
//                    // updateItem();
//                    // return true;
//
//                    //  Toast.makeText(admin_manage_product.this, selectedCategoryID, Toast.LENGTH_SHORT).show();
//                    showItem(String.valueOf(CategoryList.get(id).getItemSubCategoryID()));
//                    refreshRecycleview();
//
//
//                }
//                else{
//                    Log.d("SELECTED ID ELSE","".toString());
//                }
//            }
//        }

        @NonNull
        @Override
        public admin_manage_journal.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_item_list2,parent,false);



            admin_manage_journal.RecyclerviewAdapter.MyViewHolder holder = new admin_manage_journal.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_manage_journal.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

            holder.tv_businessDate.setText(JournalList.get(position).getDateList());
            holder.tv_fileName.setText(journalList.get(position).getFileName());
            holder.tv_fileSize.setText(journalList.get(position).getFileSize());
            holder.ll_selectJournal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_journalText = findViewById(R.id.tv_journalText);
                    Log.d("TAG", "onClick: "+localConsolidateFile+"/"+journalList.get(position).getFileName());
                    tv_journalText.setText(readFile(localConsolidateFile+"/"+journalList.get(position).getFileName()));
                    tv_journalText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    tv_journalText.setGravity(Gravity.LEFT);  // Adjust as needed
//                    tv_journalText.setPadding(10, 0, 0, 0);    // Adjust as needed
                    tv_journalText.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                    Log.e("TAG", "onClick: \n"+readFile(localConsolidateFile+"/"+journalList.get(position).getFileName()) );

                }
            });



        }

        @Override
        public int getItemCount() {
            return JournalList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
         TextView tv_businessDate,tv_fileName,tv_fileSize;
         LinearLayout ll_selectJournal;





            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_businessDate = itemView.findViewById(R.id.tv_businessDate);
                tv_fileName = itemView.findViewById(R.id.tv_fileName);
                tv_fileSize = itemView.findViewById(R.id.tv_fileSize);
                ll_selectJournal = itemView.findViewById(R.id.ll_selectJournal);









            }
        }
    }


    public static String readFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }


    //endregion








    KeyboardDevice kboard;
    KeyCodeManager kManager;
    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;

    }
    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;


        if (input.equalsIgnoreCase("Exit")){

            //triggerRebirth();
            onBackPressed();
        }






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
    private final Handler MyHandler = new Handler(Looper.getMainLooper()) {
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




                                // editTextTextPersonName.setText(kManager.getDefaultKeyName(StringToInt(KeyIndex)));
                                SimulateKeyboard(nKeyIndex);
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

//region others
    private void updateLabelFrom(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_from.setText(dateFormat.format(myCalendar.getTime()));
    }
    private void updateLabelTo(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_to.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_journal, menu);
        return true;
    }

    private void createList() {


        LinearLayout sv = this.findViewById(R.id.ll_journal);
        sv.removeAllViews();


        int totalBtn = transactionNo.size();



        for (int x = 0; x < totalBtn; x++) {
            CardView cardView = new CardView(this);
            sv.addView(cardView);

            View child = getLayoutInflater().inflate(R.layout.journal_item_list, null);//child.xml
            tv_transNo = child.findViewById(R.id.tv_transNo);
            tv_transType = child.findViewById(R.id.tv_transType);
            tv_dateTime = child.findViewById(R.id.tv_dateTime);

            tv_journalText = this.findViewById(R.id.tv_journalText);
            tv_transNo.setText(transactionNo.get(x));
            tv_transType.setText(transactionType.get(x));
            btn_reprint = child.findViewById(R.id.btn_reprint);
            //tv_journalText.setText(HeaderContent);
            tv_dateTime.setText(transactionDateTime.get(x));
            child.setId(x);


            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                                try {
                    TransNumber = transactionNo.get(child.getId());
                    date = transactionDateTime.get(child.getId());
                    TransType=transactionType.get(child.getId());

                    cashItem.setTransactionID(TransNumber);
                    String dateRemove= date.substring(0,10);
                    String FinalDate = dateRemove.replace("/","");



                    Log.e("TransNumber",TransNumber);
                    Log.e("date",FinalDate);
                    Log.e("transaction type",transactionType.get(child.getId()));


                    SQLiteDatabase db2 = view.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                    Cursor item1 = db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
                    Cursor itemTotal = db2.rawQuery("select sum(OrderQtyTotal) from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
                    Cursor itemInvoice = db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
                    Cursor transactionType =db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
                    Cursor cash = db2.rawQuery("select * from CashInOut where TransactionID='"+TransNumber+"'", null);
                    Cursor officialReceipt = db2.rawQuery("select * from OfficialReceipt where TransactionID='"+TransNumber+"'", null);
                    if (cash.getCount()!=0){
                        while (cash.moveToNext()){
                            cashItem.setC1000(cash.getDouble(1) * 1000);
                            cashItem.setC500(cash.getDouble(2) * 500);
                            cashItem.setC200(cash.getDouble(3) * 200);
                            cashItem.setC100(cash.getDouble(4) * 100);
                            cashItem.setC50(cash.getDouble(5) * 50);
                            cashItem.setC20(cash.getDouble(6) * 20);
                            cashItem.setC10(cash.getDouble(7) * 10);
                            cashItem.setC5(cash.getDouble(8) * 5);
                            cashItem.setC1(cash.getDouble(9) * 1);
                            cashItem.setCcents(cash.getDouble(10) * .25);
                            pos_user.setCashierID(cash.getString(12));
                            pos_user.setCashierName(cash.getString(13));
                            pos_user.setPOSCounter(cash.getString(14));
                            cashItem.setDateFloat(cash.getString(15));
                            cashItem.setTimeFloat(cash.getString(16));
                            cashItem.setTotalAmount(cash.getDouble(1) + cash.getDouble(2)+cash.getDouble(3)
                                    +cash.getDouble(4)+cash.getDouble(5)+cash.getDouble(6)
                                    +cash.getDouble(7)+cash.getDouble(8)+cash.getDouble(9)
                                    +cash.getDouble(10));



                        }
                    }

                    // Double change = Double.valueOf(et_cash.getText().toString()) - Double.valueOf(lbl_due.getText().toString());
                    DecimalFormat format = new DecimalFormat("0.00");
                    //Double CashDouble = Double.valueOf(et_cash.getText().toString());
                    // String Cash = format.format(CashDouble);

                    // String TotalAmount = lbl_total.getText().toString();
                    //String Change = format.format(change);
                    ServiceCharge = "0.00";
                    String VatableCharges = "0.00";
                    //String VatableSales = lbl_subtotal.getText().toString();
                    // String VatAmount = lbl_tax.getText().toString();
                    VatExemptSale = "0.00";
                    ZeroRatedSales = "0.00";
                    itemTotal.moveToFirst();
                    itemInvoice.moveToFirst();
                    TotalItem = String.valueOf(itemTotal.getString(0));
                    date = String.valueOf(itemInvoice.getString(9));
                    time = String.valueOf(itemInvoice.getString(10));
                    shift=String.valueOf(itemInvoice.getString(12));



                    buffer = new StringBuffer();

                    transactionType.moveToFirst();

                    File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
                    String fileName = eJournal+"/"+FinalDate+TransNumber+".txt";



// Check if the file already exists
                    if (new File(fileName).exists()) {
                        // File already exists, handle this case

                    } else {



                    }







                    try {
                        File file = new File(fileName);
                        FileReader fr = new FileReader(file);
                        BufferedReader br = new BufferedReader(fr);
                        String line;
                        String text = "";
                        while ((line = br.readLine()) != null) {
                            //process the line
                            System.out.println(line);
                            //  tv_journalText.setText(line);
                            text = text + line + "\n";
                        }
                        String Reprint;
                        if (TransType.equals("XREAD") || TransType.equals("ZREAD")){
                            Reprint =TransType;
                        }
                        else{
                            Reprint =TransType.substring(0,7);
                        }

                        Log.e("Reprint",Reprint);


                        if (Reprint.equals("REPRINT")){
                            //Toast.makeText(admin_manage_journal.this, "", Toast.LENGTH_SHORT).show();

                            tv_journalText.setText(text);


                        }

                        else if (TransType.equals("Terminal Opened")) {

                            String str = text.toString();

                            String formattedStr =
                                    "===============================" + "\n"
                                    + "        R E P R I N T                 " + "\n"
                                    + "==============================="
                                    + "\n"
                                    + str; // 18:00
                            tv_journalText.setText(formattedStr);


                        } else {


                            Header_Footer_class headerClass = new Header_Footer_class();
                            headerClass.HeaderNote(getApplicationContext());
                            //int HeaderTotal = headerClass.getHeaderTotal();
                            int ContentTotal = text.length();


                            String str = text.toString();
                            String str2 = headerClass.getHeaderText();
                            Log.e("str2", str2);

                            final int len = str.length();
                            final int len2 = str2.length();
                            String formattedStr = str2 + "\n"
                                    +   "===============================" + "\n"
                                    + "        R E P R I N T                 " + "\n"
                                    + "==============================="
                                    + "\n"
                                    + str.substring(len2, len); // 18:00
                            tv_journalText.setText(formattedStr);


                        }



                        }
                    catch(Exception ex){
                            Log.e("Error Showing Journal", ex.getMessage());
                        }
                        //  String fileName = "/Users/pankaj/source.txt";


//                    if (transactionType.getString(8).equals("Float IN")){
//
//
//                        buffer.append(
//                                HeaderContent+ "\n" +
//
//
//
//                                        "------------------------------"+"\n"+
//                                        cashItem.getDateFloat()+ "  " + cashItem.getTimeFloat() + "\r\n"+
//                                        "\u001B!\u0002TRANS#: "+cashItem.getTransactionID()+"\u001B!" + "\r\n" +
//                                        "FLOAT" + "\n" +
//                                        "\u001B!\u0002POS COUNTER: "+ pos_user.getPOSCounter()+"\u001B!" + "\r\n" +
//                                        "CASH:" + cashItem.getTotalAmount() +  "\r\n \r\n" +
//                                        "CASHIER ID:" + pos_user.getCashierID() +  "\r\n" +
//                                        "CASHIER NAME:" + pos_user.getCashierName() +  "\r\n \r\n \r\n" +
//
//                                        "  SIGNATURE:______________"+"\n"+
//                                        "1000 x "+cashItem.getC1000() + "PCS = " + cashItem.getC1000() + "\r\n" +
//                                        "500  x "+cashItem.getC500() + "PCS = " + cashItem.getC500() + "\r\n" +
//                                        "200  x "+cashItem.getC200() + "PCS = " + cashItem.getC200() + "\r\n" +
//                                        "100  x "+cashItem.getC100() + "PCS = " + cashItem.getC100() + "\r\n" +
//                                        "50   x "+cashItem.getC50() + "PCS = " + cashItem.getC50() + "\r\n" +
//                                        "20   x "+cashItem.getC20() + "PCS = " + cashItem.getC20() + "\r\n" +
//                                        "10   x "+cashItem.getC10() + "PCS = " + cashItem.getC10() + "\r\n" +
//                                        "5    x "+cashItem.getC5() + "PCS = " + cashItem.getC5() + "\r\n" +
//                                        "1    x "+cashItem.getC1() + "PCS = " + cashItem.getC1() + "\r\n" +
//                                        ".25  x "+cashItem.getCcents() + "PCS = " + cashItem.getCcents() + "\r\n" +
//
//
//                                        "------------------------------" + "\n");
//
//                        tv_journalText.setText(buffer.toString());
//
//
//
//                    }
//
//                    if (transactionType.getString(8).trim().equals("Terminal Opened")){
//
//                        Log.e("CHILD GOOD","TERMINAL OPENED");
//
//                        shift_active shift_active= new shift_active();
//
//
//                        StringBuffer buffer = new StringBuffer();
//                        buffer.append("TERMINAL OPENED" + "\n");
//                        buffer.append(date +  "\t\t\t" + time + "\n");
//                        buffer.append("===========OPEN COUNTER===========" + "\n");
//                        buffer.append("POS:"+shift_active.getPOSCounter() +  "\n");
//                        buffer.append(date + "\t" + time + "\t"  + "TRANS#: " + TransNumber +  "\n");
//                        buffer.append("TERMINAL OPENED" + "\n");
//                        buffer.append("==================================" + "\n\n\n\n.");
//
//                        tv_journalText.setText(buffer.toString());
//
//
//                    }
//
//                    if (transactionType.getString(8).trim().equals("invoice")){
//
//                        if (transactionType.getString(7).equals("Cash")) {
//
//
//                            StringBuffer buffer = new StringBuffer();
//
//
//                            buffer.append(HeaderContent + "\n\n");
//                            buffer.append("OFFICIAL RECEIPT" + "\n");
//                            buffer.append("OR# :" + " "  + "\n" + "TRANS#:" + " " + "\n");
//                            buffer.append("CASHIER: " + " " + " " + " " + "\n");
//                            buffer.append("POS: " + " " + "\t\t\t\t" + date + "\t\t" + time + "\n");
//                            buffer.append("----------------------------------------------" + "\n\n");
//
//
//
//
//
////                                    while (item1.moveToNext()) {
////                                        item2 = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where TransactionID='" + item1.getString(0) + "'and OrderID='" + item1.getString(1) + "'", null);
////                                        Double PricePerItem = Double.valueOf(item1.getString(5));
////                                        Double PriceOfItem=Double.valueOf(item1.getString(4));
////                                        Subtotal+=PricePerItem;
////
////                                        buffer.append(((item1.getString(2) + "                              ").substring(0, 30) + "\r\n"
////
////
////                                                + ("      x" + item1.getString(3) + "                 ").substring(0, 20)) + (String.format("%7.2f",PriceOfItem)+"           ").substring(0,11) + (String.format("%7.2f",PricePerItem)+"           ").substring(0,11)  + "\n");
////
////                                    }
//
//                                item1.close();
//
//
//                                buffer.append("----------------------------------------------" + "\n");
//                                buffer.append(("         SUB-TOTAL" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                                buffer.append(("         LESS-VAT" + "                                ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//
////                                    while (item1B.moveToNext()) {
////                                        item2 = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where TransactionID='" + item1B.getString(0) + "'and OrderID='" + item1B.getString(1) + "'", null);
////                                        if (item2.getCount()!=0) {
////                                            while (item2.moveToNext()) {
//////                            if (item2.getString(8).equals("scd")) {
////                                                Double PricePerItemDiscount = Double.valueOf(item2.getString(11));
////                                                buffer.append((("        " + (item2.getString(12) + "% " + item2.getString(8) + "(" + item2.getString(10)) + ")") + "                                ").substring(0, 31) + (String.format("%7.2f", PricePerItemDiscount * -1) + "           ").substring(0, 11) + "\n");
////                                                totalDiscount += PricePerItemDiscount;
//////                            }
////                                            }
////                                        }
////                                    }
//                                buffer.append(("  Service Charges" + "                        ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                                buffer.append("----------------------------------------------" + "\n");
//                                buffer.append(("            TOTAL" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\u001B\u0001" + "\n");
//                                buffer.append(("             CASH" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                                buffer.append(("           CHANGE" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n\n");
//                                buffer.append(("  Service Charges" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n\n");
//                                buffer.append(("    Vatable Sales" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                                buffer.append(("       VAT Amount" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                                buffer.append((" Vat Exempt Sales" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                                buffer.append((" Zero-Rated Sales" + "                               ").substring(0, 31) + ("           ").substring(0, 11) + "\n");
//                            system_final_date SysDate = new system_final_date();
//                            SysDate.insertDate(getApplicationContext());
//
//
//                                buffer.append("Total Qty:" + TotalItem + "   " + SysDate.getSystemDate() + " " + timeOnly.format(currentDate.getTime()) + "\n\n\n");
//                                buffer.append("      Name:___________________" + "\n");
//                                buffer.append("      ADDRESS:________________" + "\n");
//                                buffer.append("      TIN:____________________" + "\n");
//                                buffer.append("      SIGN:___________________" + "\n");
//                                buffer.append("------------------------------" + "\n");
//                                buffer.append(FooterContent + "\n");
//                                buffer.append("\n\n\n\n\n\n");
//
//
//                                //mmOutputStream.write(buffer.toString().getBytes());// for bt printing
//                                //String printData = buffer.toString();
//
//                                //JMPrinter(printData); // for jolimark
//                               // SunmiPrinter(printData);
//
//
//                               // db2.close();
//
//
//                            tv_journalText.setText(buffer.toString());
//
//                            }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                    }
//                    else{
//                       // Log.e("CHILD ELSE",transactionType.getString(8));
//                      //  Log.e("CHILD2","ERROR");
//                    }


//                    else {
//
//                        officialReceipt.moveToNext();
//                        Toast.makeText(admin_manage_journal.this, "TEST: "+officialReceipt.getString(0), Toast.LENGTH_SHORT).show();
//                        //  File file = new File(receiptFile, "test.txt");
//                        // FileOutputStream stream = new FileOutputStream(file);
//                        buffer.append(
//                                HeaderContent + "\n" +
//
//
//                                        "\u001B!!OFFICIAL RECEIPT\u001B!\u0001" + "\n" +
//                                        "\u001B!\u0002OR#: " + officialReceipt.getString(0) + "\u001B!" + "   " +
//                                        "\u001B!\u0002TRANS#: " + TransNumber + "\u001B!" + "\r\n" +
//                                        "CASHIER:" + cashierName + "\n" +
//                                        "POS:" + posNumber + " " + "\n" +
//                                        "------------------------------" + "\n");
//
//                        //dateFormat.format(currentDate.getTime())
//
//
////                try {
//
////                while (item1.moveToNext()) {
////
////                    item2 = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where TransactionID='"+item1.getString(0)+"'and OrderID='"+item1.getString(1) +"'", null);
////                    buffer.append(((item1.getString(2) + "           ").substring(0,15) + "\r\n"
////                            + "      x"+item1.getString(3)+"          ") +  item1.getString(5)+"          ".substring(0,10) +"\n");
////                    while(item2.moveToNext()){
////                        buffer.append(item2.getString(8)+"x"+item2.getString(10) + "      "+item2.getString(11) + "\n" );
////                    }
////                }
//
//                        while (item1.moveToNext()) {
//                            itemName = new ArrayList<String>();
//                            itemNameSelect = db2.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='" + TransNumber + "'", null);
//                            while (itemNameSelect.moveToNext()) {
//                                itemName.add(itemNameSelect.getString(1));
//                            }
//
//
//                            for (int x = 0; x < itemName.size(); x++) {
//
//                                item2 = db2.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='" + TransNumber + "'and OrderID='" + itemName.get(x) + "'", null);
//                                itemNameWDiscount = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where TransactionID='" + TransNumber + "'and OrderID='" + itemName.get(x) + "'", null);
//                                while (item2.moveToNext()) {
//                                    Double PricePerItem = Double.valueOf(item2.getString(5));
//
//                                    buffer.append(((item2.getString(2) + "           ").substring(0, 15) + "\r\n"
//
//
//                                            + ("      x" + item2.getString(3) + "                 ").substring(0, 25)) + format.format(PricePerItem) + "\n");
//
//                                    while (itemNameWDiscount.moveToNext()) {
//                                        if (itemNameWDiscount.getCount() != 0) {
//                                            Double PricePerItemDiscount = Double.valueOf(itemNameWDiscount.getString(11));
//                                            buffer.append((((itemNameWDiscount.getString(8) + "     ").substring(0, 6) + "x" + itemNameWDiscount.getString(10)) + "                    ").substring(0, 25) + "-" + format.format(PricePerItemDiscount) + "\n");
//                                        }
//                                    }
//                                }
//
//
////
//
//                            }
//                        }
//
//
//                        buffer.append("------------------------------" + "\n"
//                                +
//                                "TOTAL                    " + TotalAmount + "\u001B\u0001" + "\n"
//                                +
//                                "CASH                     " + Cash + "\n\n" +
//                                "CHANGE                   " + Change + "\n\n" +
//                                "Service Charges          " + ServiceCharge + "\n" +
//                                "Vatable Sales            " + VatableSales + "\n" +
//                                "VAT Amount               " + VatAmount + "\n" +
//                                "Vat Exempt Sales         " + VatExemptSale + "\n" +
//                                "Zero-Rated Sales         " + ZeroRatedSales + "\n"
//                                +
//                                "Total Qty:" + TotalItem + "   " + date + " " + time + "\n\n\n"
//                                +
//                                "      Name:___________________" + "\n" +
//                                "      ADDRESS:________________" + "\n" +
//                                "      TIN:____________________" + "\n" +
//                                "      SIGN:___________________" + "\n" +
//                                "------------------------------" + "\n" +
//                                "  This Serves As Your Official Receipt" + "\n" +
//                                "       Thank You . . Come Again . ." + "\n" +
//                                "------------------------------" + "\n" +
//                                "    \"THIS RECEIPT SHALL BE VALID FOR " + "\n" +
//                                "   FIVE (5) YEARS FROM THE DATE OF THE" + "\n" +
//                                "            PERMIT TO USE.\"" + "\n" +
//                                "------------------------------" + "\n"
//                        );
//
//                        //stream.write(buffer.toString().getBytes());
//
//                        //db2.close();
////                } finally {
////
////                    //stream.close();
////                    //db2.close();
////                }
////            } catch (Exception e) {
////
////
////          }
//
//
//                        tv_journalText.setText(buffer.toString());
//
//
//                        //end of button
//                    }



                }
            });


            btn_reprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //           Toast.makeText(admin_manage_journal.this, transactionNo.get(child.getId()), Toast.LENGTH_SHORT).show();

//                                try {
                   // buffer=null;
//                    TransNumber = transactionNo.get(child.getId());
//
//                    SQLiteDatabase db2 = view.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
//                    Cursor item1 = db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
//                    Cursor itemTotal = db2.rawQuery("select sum(OrderQtyTotal) from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
//                    Cursor itemInvoice = db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
//                    Cursor transactionType =db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransNumber+"'", null);
//                    // Double change = Double.valueOf(et_cash.getText().toString()) - Double.valueOf(lbl_due.getText().toString());
//                    DecimalFormat format = new DecimalFormat("0.00");
//                    //Double CashDouble = Double.valueOf(et_cash.getText().toString());
//                    // String Cash = format.format(CashDouble);
//
//                    // String TotalAmount = lbl_total.getText().toString();
//                    //String Change = format.format(change);
//                    ServiceCharge = "0.00";
//                    String VatableCharges = "0.00";
//                    //String VatableSales = lbl_subtotal.getText().toString();
//                    // String VatAmount = lbl_tax.getText().toString();
//                    VatExemptSale = "0.00";
//                    ZeroRatedSales = "0.00";
//                    itemTotal.moveToFirst();
//                    itemInvoice.moveToFirst();
//                    TotalItem = String.valueOf(itemTotal.getString(0));
//                    date = String.valueOf(itemInvoice.getString(9));
//                    time = String.valueOf(itemInvoice.getString(10));
////                    OR_TRANS_ITEM or_trans_item= new OR_TRANS_ITEM();
////                    or_trans_item.readOfficialReceiptNumber(getApplicationContext());
////                    or_trans_item.getOfficialReceiptNo();
//
//               transactionType.moveToFirst();
//                    if (transactionType.getString(8).equals("Float IN")){
//                        buffer = new StringBuffer();
//                        buffer.append(
//                                HeaderContent+ "\n" +
//
//
//
//                                        "------------------------------"+"\n"+
//                                        cashItem.getDateFloat()+ "  " + cashItem.getTimeFloat() + "\r\n"+
//                                        "\u001B!\u0002TRANS#: "+cashItem.getTransactionID()+"\u001B!" + "\r\n" +
//                                        "FLOAT" + "\n" +
//                                        "\u001B!\u0002POS COUNTER: "+ pos_user.getPOSCounter()+"\u001B!" + "\r\n" +
//                                        "CASH:" + cashItem.getTotalAmount() +  "\r\n \r\n" +
//                                        "CASHIER ID:" + pos_user.getCashierID() +  "\r\n" +
//                                        "CASHIER NAME:" + pos_user.getCashierName() +  "\r\n \r\n \r\n" +
//
//                                        "  SIGNATURE:______________"+"\n"+
//                                        "1000 x "+cashItem.getC1000Counter() + "PCS = " + cashItem.getC1000() + "\r\n" +
//                                        "500  x "+cashItem.getC500Counter() + "PCS = " + cashItem.getC500() + "\r\n" +
//                                        "200  x "+cashItem.getC200Counter() + "PCS = " + cashItem.getC200() + "\r\n" +
//                                        "100  x "+cashItem.getC100Counter() + "PCS = " + cashItem.getC100() + "\r\n" +
//                                        "50   x "+cashItem.getC50Counter() + "PCS = " + cashItem.getC50() + "\r\n" +
//                                        "20   x "+cashItem.getC20Counter() + "PCS = " + cashItem.getC20() + "\r\n" +
//                                        "10   x "+cashItem.getC10Counter() + "PCS = " + cashItem.getC10() + "\r\n" +
//                                        "5    x "+cashItem.getC5Counter() + "PCS = " + cashItem.getC5() + "\r\n" +
//                                        "1    x "+cashItem.getC1Counter() + "PCS = " + cashItem.getC1() + "\r\n" +
//                                        ".25  x "+cashItem.getCcentsCounter() + "PCS = " + cashItem.getCcents() + "\r\n" +
//
//
//                                        "------------------------------" + "\n");
//
//
//                    }
//                    else  {
//                       buffer = new StringBuffer();
//
//                        buffer.append(
//                                HeaderContent + "\n" +
//
//
//                                        "\u001B!!OFFICIAL RECEIPT\u001B!\u0001" + "\n" +
//                                        "\u001B!\u0002OR#: " + ORNumber + "\u001B!" + "   " +
//                                        "\u001B!\u0002TRANS#: " + TransNumber + "\u001B!" + "\r\n" +
//                                        "CASHIER:" + cashierName + "\n" +
//                                        "POS:" + posNumber + " " + "\n" +
//                                        "------------------------------" + "\n");
//
//
//                        while (item1.moveToNext()) {
//                            itemName = new ArrayList<String>();
//                            itemNameSelect = db2.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='" + TransNumber + "'", null);
//                            while (itemNameSelect.moveToNext()) {
//                                itemName.add(itemNameSelect.getString(1));
//                            }
//
//
//                            for (int x = 0; x < itemName.size(); x++) {
//
//                                item2 = db2.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='" + TransNumber + "'and OrderID='" + itemName.get(x) + "'", null);
//                                itemNameWDiscount = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where TransactionID='" + TransNumber + "'and OrderID='" + itemName.get(x) + "'", null);
//                                while (item2.moveToNext()) {
//                                    Double PricePerItem = Double.valueOf(item2.getString(5));
//
//                                    buffer.append(((item2.getString(2) + "           ").substring(0, 15) + "\r\n"
//
//
//                                            + ("      x" + item2.getString(3) + "                 ").substring(0, 25)) + format.format(PricePerItem) + "\n");
//
//                                    while (itemNameWDiscount.moveToNext()) {
//                                        if (itemNameWDiscount.getCount() != 0) {
//                                            Double PricePerItemDiscount = Double.valueOf(itemNameWDiscount.getString(11));
//                                            buffer.append((((itemNameWDiscount.getString(8) + "     ").substring(0, 6) + "x" + itemNameWDiscount.getString(10)) + "                    ").substring(0, 25) + "-" + format.format(PricePerItemDiscount) + "\n");
//                                        }
//                                    }
//                                }
//
//
////
//
//                            }
//                        }
//
//
//                        buffer.append("------------------------------" + "\n"
//                                +
//                                "TOTAL                    " + TotalAmount + "\u001B\u0001" + "\n"
//                                +
//                                "CASH                     " + Cash + "\n\n" +
//                                "CHANGE                   " + Change + "\n\n" +
//                                "Service Charges          " + ServiceCharge + "\n" +
//                                "Vatable Sales            " + VatableSales + "\n" +
//                                "VAT Amount               " + VatAmount + "\n" +
//                                "Vat Exempt Sales         " + VatExemptSale + "\n" +
//                                "Zero-Rated Sales         " + ZeroRatedSales + "\n"
//                                +
//                                "Total Qty:" + TotalItem + "   " + date + " " + time + "\n\n\n"
//                                +
//                                "      Name:___________________" + "\n" +
//                                "      ADDRESS:________________" + "\n" +
//                                "      TIN:____________________" + "\n" +
//                                "      SIGN:___________________" + "\n" +
//                                "------------------------------" + "\n" +
//                                "  This Serves As Your Official Receipt" + "\n" +
//                                "       Thank You . . Come Again . ." + "\n" +
//                                "------------------------------" + "\n" +
//                                "    \"THIS RECEIPT SHALL BE VALID FOR " + "\n" +
//                                "   FIVE (5) YEARS FROM THE DATE OF THE" + "\n" +
//                                "            PERMIT TO USE.\"" + "\n" +
//                                "------------------------------" + "\n"
//                        );
//                    }
//
//
//
//
//
//                    //stream.write(buffer.toString().getBytes());
////                    try {
////                        findBT();
////                        openBT();
////                        mmOutputStream.write(buffer.toString().getBytes()); // for printing receipt
////                        closeBT();
////                    } catch (IOException exception) {
////                        exception.printStackTrace();
////                    }
//                   String printData=buffer.toString();
//
//                    //JMPrinter(printData); // for jolimark
//                    SunmiPrinter(printData);
//
//
//
//
//
//


                    try {

                        create_journal_entry createJournal = new create_journal_entry();
                        createJournal.setPrintData(tv_journalText.getText().toString());

                        String dateRemove = date.substring(0, 10);
                        String FinalDate = dateRemove.replace("-", "");

                        OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
                        or_trans_item.readReferenceNumber(getApplicationContext());


                        createJournal.setTransNumber(TransNumber);
                        createJournal.journalEntry(createJournal.getPrintData(), or_trans_item.getTransactionNo(), FinalDate);


                        printer_settings_class printSettings = new printer_settings_class(getApplicationContext());
                        printSettings.OnlinePrinter(tv_journalText.getText().toString(),1,0,1);

                        // SunmiPrinter(tv_journalText.getText().toString());

                        InsertTransaction();
                        loadReceiptData();
                        showItem();
                        createList();
                    }
                    catch (Exception ex){
                        Toast.makeText(admin_manage_journal.this, "PLEASE SELECT TRANSACTION TO REPRINT", Toast.LENGTH_LONG).show();
                    }










                }
            });



            cardView.addView(child);

        }


    }

    private void generateJournal(){

        try {
           // DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



            String DateFrom=et_from.getText().toString();
            String DateTo=et_to.getText().toString();
            String FinalText="";
            String text = "";

            Log.e("DATA","TEST INSIDE");
            db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select TransDate,TransactionID from InvoiceReceiptTotal where TransDate Between '"+DateFrom+"' and '"+DateTo+"'", null);
            Log.e("DATEFrom",DateFrom);
            Log.e("DateTo",DateTo);
            if (cursor.getCount()!=0){
                Log.e("DATA","TEST INSIDE 2");
                while (cursor.moveToNext()) {
                    Log.e("DATA","TEST INSIDE3");

                    String dateRemove= cursor.getString(0);
                    String TransactionID=cursor.getString(1);
                    String FinalDate = dateRemove.replace("/","");


                    File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
                    String fileName = eJournal+"/"+FinalDate+TransactionID+".txt";
                    Log.e("DATA",fileName);


                    File file = new File(fileName);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;

                    while ((line = br.readLine()) != null) {
                        //process the line
                       // System.out.println(line);

                        text = text + line + "\n";
                    }

                }
                FinalText = text;
               // tv_journalText.setText(FinalText);
                File GeneratedEJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Generated E-Journal/");
                //String dateForJournal = FinalDate.replace("/","");
                File file = new File(GeneratedEJournal, DateFrom.replace("/","")+"-"+DateTo.replace("/","")+".txt");
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }
              //  String finalPrintData = printData;
                try {
                    stream.write(FinalText.getBytes());

                    Toast.makeText(this, DateFrom.replace("/","")+"-"+DateTo.replace("/","")+".txt Generated!" , Toast.LENGTH_SHORT).show();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }



            }

            else{
                Log.e("DATA","NO DATA FOUND");
            }






//            String dateRemove1 = DateFrom.substring(0, 8);
//            String dateRemove2 = DateTo.substring(0, 8);
//            String FinalDateFrom = dateRemove1.replace("/", "");
//            String FinalDateTo = dateRemove2.replace("/", "");
//            File eJournal = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/Generated E-Journal/");
//            String fileName = eJournal + "/" + FinalDateFrom +"-"+FinalDateTo+ ".txt";


           // File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
          //  String fileName = eJournal+"/"+FinalDate+TransNumber+".txt";



        }
        catch (Exception ex){

        }

        db.close();
    }


    private void generateJournalSDCARD(){

        try {
            // DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



            String DateFrom=et_from.getText().toString();
            String DateTo=et_to.getText().toString();
            String FinalText="";
            String text = "";

            Log.e("DATA","TEST INSIDE");
            db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select TransDate,TransactionID from InvoiceReceiptTotal where TransDate Between '"+DateFrom+"' and '"+DateTo+"'", null);
            Log.e("DATEFrom",DateFrom);
            Log.e("DateTo",DateTo);
            if (cursor.getCount()!=0){
                Log.e("DATA","TEST INSIDE 2");
                while (cursor.moveToNext()) {
                    Log.e("DATA","TEST INSIDE3");

                    String dateRemove= cursor.getString(0);
                    String TransactionID=cursor.getString(1);
                    String FinalDate = dateRemove.replace("/","");


                    File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
                    String fileName = eJournal+"/"+FinalDate+TransactionID+".txt";
                    Log.e("DATA",fileName);


                    File file = new File(fileName);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;

                    while ((line = br.readLine()) != null) {
                        //process the line
                        // System.out.println(line);

                        text = text + line + "\n";
                    }

                }
                FinalText = text;
                // tv_journalText.setText(FinalText);
                File GeneratedEJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Generated E-Journal/");
                //String dateForJournal = FinalDate.replace("/","");
                File file = new File(GeneratedEJournal, DateFrom.replace("/","")+"-"+DateTo.replace("/","")+".txt");
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }
                //  String finalPrintData = printData;
                try {
                    stream.write(FinalText.getBytes());

                    Toast.makeText(this, DateFrom.replace("/","")+"-"+DateTo.replace("/","")+".txt Generated!" , Toast.LENGTH_SHORT).show();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                }



            }

            else{
                Log.e("DATA","NO DATA FOUND");
            }






//            String dateRemove1 = DateFrom.substring(0, 8);
//            String dateRemove2 = DateTo.substring(0, 8);
//            String FinalDateFrom = dateRemove1.replace("/", "");
//            String FinalDateTo = dateRemove2.replace("/", "");
//            File eJournal = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/Generated E-Journal/");
//            String fileName = eJournal + "/" + FinalDateFrom +"-"+FinalDateTo+ ".txt";


            // File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
            //  String fileName = eJournal+"/"+FinalDate+TransNumber+".txt";



        }
        catch (Exception ex){

        }

        db.close();
    }

    private void PrintJournal(){

        try {
            // DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



            String DateFrom=et_from.getText().toString();
            String DateTo=et_to.getText().toString();
            String FinalText="";
            String text = "";

            Log.e("DATA","TEST INSIDE");
            db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            cursor = db.rawQuery("select TransDate,TransactionID from InvoiceReceiptTotal where TransDate Between '"+DateFrom+"' and '"+DateTo+"'", null);
            Log.e("DATEFrom",DateFrom);
            Log.e("DateTo",DateTo);
            if (cursor.getCount()!=0){
                Log.e("DATA","TEST INSIDE 2");
                while (cursor.moveToNext()) {
                    Log.e("DATA","TEST INSIDE3");

                    String dateRemove= cursor.getString(0);
                    String TransactionID=cursor.getString(1);
                    String FinalDate = dateRemove.replace("/","");


                    File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
                    String fileName = eJournal+"/"+FinalDate+TransactionID+".txt";
                    Log.e("DATA",fileName);


                    File file = new File(fileName);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line;

                    while ((line = br.readLine()) != null) {
                        //process the line
                        // System.out.println(line);

                        text = text + line + "\n";
                    }

                }
                FinalText = text;
                // tv_journalText.setText(FinalText);
                printer_settings_class PrinterSettings = new printer_settings_class(this.getApplicationContext());
                PrinterSettings.OnlinePrinter(FinalText,1,0,1);




            }

            else{
                Log.e("DATA","NO DATA FOUND");
            }






//            String dateRemove1 = DateFrom.substring(0, 8);
//            String dateRemove2 = DateTo.substring(0, 8);
//            String FinalDateFrom = dateRemove1.replace("/", "");
//            String FinalDateTo = dateRemove2.replace("/", "");
//            File eJournal = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/Generated E-Journal/");
//            String fileName = eJournal + "/" + FinalDateFrom +"-"+FinalDateTo+ ".txt";


            // File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
            //  String fileName = eJournal+"/"+FinalDate+TransNumber+".txt";



        }
        catch (Exception ex){

        }

        db.close();
    }


    private void InsertTransaction(){
        OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
        or_trans_item.readReferenceNumber(getApplicationContext()); //read Transaction before clicking xread
        DatabaseHandler databaseHandler=new DatabaseHandler(getApplicationContext());
        shift_active shiftActive= new shift_active();
        system_final_date sysDate = new system_final_date();
        sysDate.insertDate(getApplicationContext());
      //  Log.e("Transaction Number",or_trans_item.getTransactionNo());

//
        databaseHandler.insertInvoiceReceiptTotal(or_trans_item.getTransactionNo(),"","",""
                ,"","","","","REPRINT("+TransNumber+")",sysDate.getSystemDate(),timeOnly.format(currentDate.getTime()),shiftActive.getActiveUserID(),shiftActive.getShiftActive());
        //  readReferenceNumber(context); //read Transaction after clicking xread
//

    }

    String FooterContent;
    private void loadReceiptData(){
        SQLiteDatabase db2 =this.openOrCreateDatabase(posSettingsdb, Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select * from receiptHeader", null);
        if (item1.getCount()==0){
            // db2.close();
        }else{
            while (item1.moveToNext()){
                HeaderContent=(item1.getString(1));

            }
        }

        Cursor item2 = db2.rawQuery("select * from receiptFooter", null);
        if (item2.getCount()==0){
            // db2.close();
        }else{
            while (item1.moveToNext()){
                FooterContent=(item1.getString(1));

            }
        }



        db2.close();
    }

    private void showItem(){
        transactionNo.clear();
        transactionType.clear();
        transactionDateTime.clear();
        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("select * from InvoiceReceiptTotal", null);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{



        while(cursor.moveToNext()){
            transactionNo.add(cursor.getString(0));
            transactionType.add(cursor.getString(8));
            transactionDateTime.add(cursor.getString(9) + " " + cursor.getString(10));

            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

        }
        }

        db.close();



    }

    //endregion




}