package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class admin_manage_report extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView3,dateView2;
    private int year, month, day;
    private ArrayList<String> ColumnName = new ArrayList<>();
    TextView tv_beginningOR,tv_endingOR;
    TextView tv_accumEndBal,tv_accumBegBal,tv_grossSales,tv_vatableSales,tv_vatAmount,tv_vatExemptSales,tv_zeroRatedSales;
    TextView tv_seniorDiscount,tv_pwdDiscount,tv_othersDiscount,tv_returns,tv_void,tv_totalDeductions;
    TextView tv_scOnVat,tv_pwdOnVat,tv_othersOnVat,tv_othersOnVat2,tv_totalVatAdjOnVat,tv_vatPayable,tv_netSales,tv_salesOverRunFlow,tv_totalIncomeSales;
    TextView tv_resetCounter,tv_zCounter,tv_remarks,tv_vatOnReturns;
    TextView tv_Date;
    TextView tv_dateFrom;
    Button btn_setDate,btn_loadData,btn_export;
    EditText et_from,et_to;
    final Calendar myCalendar= Calendar.getInstance();

   // String date1Format1="09/22/2022";
   // String date1Format2="09-22-2022";

    //String date2Format1="01/30/1970";
    //String date2Format2="01-30-1970";
    String date1Format1;
   // String date1Format2;
    private Bitmap bitmap;
    private LinearLayout linearID;
    private HorizontalScrollView scrollView;
    private ProgressDialog working_dialog;
    private ProgressBar progressBar;
    Double totalDiscount=0.00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_report);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        dateView3 = (TextView) findViewById(R.id.et_from);
        dateView2 = (TextView) findViewById(R.id.et_to);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


//
       // tv_dateFrom = findViewById(R.id.tv_dateFrom);
       // btn_setDate = findViewById(R.id.btn_setDate);
        date1Format1=dateView3.getText().toString();
        btn_loadData=findViewById(R.id.btn_loadData);






//        getORTrans();
//        getAccumulatedBalanceBeginning();
//        getAccumulatedBalanceEnding();
//        getGrossSales();
//        getVatableSales();
//        getVatAmount();
//        getVatExemptSales();
//        getZeroRatedSales();
//        getSeniorDiscount();
//        getPWDDiscount();
//        getOtherDiscount();
//
//
//        getSeniorDiscountVat();
//        getPWDDiscountonVat();
//        getOtherDiscountonVat();
//        getnetSales();
//        getZcounter();




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
                new DatePickerDialog(admin_manage_report.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(admin_manage_report.this,date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_export=findViewById(R.id.btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTransaction.clear();
                List<Date> dates = getDates(et_from.getText().toString(), et_to.getText().toString());
                for(Date date:dates){
                    //System.out.println(date);
                    // DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                    String formatted = df1.format(date);
                    Log.e("Dates",formatted);
                    DateTransaction.add(formatted);
                }
                //  progressBar.setVisibility(View.VISIBLE);
                showWorkingDialog();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        removeWorkingDialog();
                        // progressBar.setVisibility(View.GONE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // DO your work here
                                // get the data
                                Looper.prepare();
                                GenerateData();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // update UI

                                    }
                                });


                            }
                        }).start();


                    }

                }, 7000);

            }
        });

        btn_loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Log.e("load data","FROM: "+ et_from.getText().toString() + " To: "+et_to.getText().toString());
                // admin_manage_report_item adminReportItem= new admin_manage_report_item();

                showWorkingDialog();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // progressBar.setVisibility(View.GONE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // DO your work here
                                // get the data
                                Looper.prepare();


                                DateTransaction.clear();
                                List<Date> dates = getDates(et_from.getText().toString(), et_to.getText().toString());
                                for(Date date:dates){
                                    //System.out.println(date);
                                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                                    String formatted = df1.format(date);
                                    Log.e("Dates",formatted);
                                    DateTransaction.add(formatted);
                                }



                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // update UI
                                        showSummary();
                                        removeWorkingDialog();
                                    }
                                });


                            }
                        }).start();


                    }

                }, 5000);


               // removeWorkingDialog();




//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        // progressBar.setVisibility(View.GONE);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // DO your work here
//                                // get the data
//                                Looper.prepare();
//
//
//                               // GenerateData();
//
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // update UI
//
//
//
//
//
//                                    }
//                                });
//
//
//                            }
//                        }).start();
//
//
//                    }
//
//                }, 5000);














            }
        });


        updateLabelFrom();
        updateLabelTo();





//        tv_Date.setText(dateView3.getText().toString());
//
//
//
//        btn_loadData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                date1Format1=dateView3.getText().toString();
//              //  date1Format1 = dateView3.getText().toString();
//                //  date1Format1 = dateView3.getText().toString();
//                tv_Date.setText(date1Format1);
//
//                getORTrans();
//                getAccumulatedBalanceBeginning();
//                getAccumulatedBalanceEnding();
//                getGrossSales();
//                getVatableSales();
//                getVatAmount();
//                getVatExemptSales();
//                getZeroRatedSales();
//                getSeniorDiscount();
//                getPWDDiscount();
//                getOtherDiscount();
//
//
//                getSeniorDiscountVat();
//                getPWDDiscountonVat();
//                getOtherDiscountonVat();
//                getnetSales();
//                getZcounter();
//            }
//        });


        KeyBoardMap();
    }


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


    private void showWorkingDialog() {
        working_dialog = ProgressDialog.show(this, "","GENERATING BIR REPORT PLEASE WAIT...", true);
    }

    private void removeWorkingDialog() {
        if (working_dialog != null) {
            working_dialog.dismiss();
            working_dialog = null;
        }
    }


    private void GenerateData(){
        File dir = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_Report");
        try{
            if(dir.mkdir()) {
                Log.d("Directory","Directory Created");
            } else {
                Log.d("Directory","Directory Not Created");
                // System.out.println("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        try {
            // String rootPath = Environment.getExternalStorageDirectory()
            //    .getAbsolutePath() + "/MyFolder/";
            String rootPath=Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_Report/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            File f = new File(rootPath + "BIR_REPORT.xlsx");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            FileOutputStream out = new FileOutputStream(f);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createFile();
        removeWorkingDialog();
        // openPdf();
        openXLS();

    }


    private void openPdf() {
       // File file= new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR Report/BirReport.pdf");
        File file= new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_Report/BIR_REPORT.xlsx");

        //File currDir = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR Report/");
        // String path = currDir.getAbsolutePath();
      // String fileLocation = currDir + "BirReport.xlsx";


        if (file.exists()){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri,"application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{
                startActivity(intent);
            }
            catch (ActivityNotFoundException e){
                Log.e("error",e.getMessage());
                Toast.makeText(this, "NO application for Excel file", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openXLS(){
        File xls = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_REPORT", "BIR_REPORT.xlsx");
        Uri path = Uri.fromFile(xls);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No Application available to view XLS", Toast.LENGTH_SHORT).show();
        }
    }


    private void createColumnName(){
        ColumnName.add("Date");
        ColumnName.add("Beginning \n SI/OR No");
        ColumnName.add("Ending \n SI/OR No");
        ColumnName.add("Grand Accum \n Ending\n Balance");
        ColumnName.add("Grand Accum \n Beginning \n Balance");
        ColumnName.add("Sales Issued \n w/ Manual \n SI/OR(per RR\n16-2018");
        ColumnName.add("Gross Sales \n For the day");
        ColumnName.add("VATable \n Sales");
        ColumnName.add("VAT \n Amount");
        ColumnName.add("VAT \n Exempt \n Sales");
        ColumnName.add("Zero \n Rated \n Sales");           //11
        ColumnName.add("Deductions"); // head title         //12
        ColumnName.add("Adjustment On Vat");                //13


        ColumnName.add("VAT \n Payable");                   //14
        ColumnName.add("Net \n Sales");                     //15
        ColumnName.add("Sales \n Overrun/ \nOverflow");     //16
        ColumnName.add("Total \n Income \nSales");          //17
        ColumnName.add("Reset \n Counter");                 //18
        ColumnName.add("Z - \n Counter");                   //19
        ColumnName.add("Remark");                           //20



       //Deduction

//        ColumnName.add("SC"); //12
//        ColumnName.add("PWD"); //13
//        ColumnName.add("Others");//14
//        ColumnName.add("Returns");//15
//        ColumnName.add("Void");//16
//        ColumnName.add("Total\n Deduction"); //17
//
//
//
//        //Adjustment
//
//        ColumnName.add("SC"); //18
//        ColumnName.add("PWD"); //19
//        ColumnName.add("Others");//20
//        ColumnName.add("VAT on \n Returns");
//        ColumnName.add("Others");
//        ColumnName.add("Total \n VAT \n Adjustment");
//
//
//        ColumnName.add("VAT \n Payable");
//        ColumnName.add("Net \n Sales");
//        ColumnName.add("Sales \n Overrun/\nOverflow");
//        ColumnName.add("Total Income Sales");
//        ColumnName.add("Reset Counter");
//        ColumnName.add("Z - \nCounter");
//        ColumnName.add("Remarks");
//
//
//        ColumnName.add("Deductions"); // head title //31
//        ColumnName.add("Discount"); //32
//        ColumnName.add("Adjutment On Vat"); //33



    }
    private void createFile(){
        db = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        ColumnName.clear();
        createColumnName();

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("BIR REPORT");
        sheet.addMergedRegion(new CellRangeAddress(0,3,0,0));
        sheet.addMergedRegion(new CellRangeAddress(0,3,1,1));
        sheet.addMergedRegion(new CellRangeAddress(0,3,2,2));
        sheet.addMergedRegion(new CellRangeAddress(0,3,3,3));
        sheet.addMergedRegion(new CellRangeAddress(0,3,4,4));
        sheet.addMergedRegion(new CellRangeAddress(0,3,5,5));
        sheet.addMergedRegion(new CellRangeAddress(0,3,6,6));
        sheet.addMergedRegion(new CellRangeAddress(0,3,7,7));
        sheet.addMergedRegion(new CellRangeAddress(0,3,8,8));
        sheet.addMergedRegion(new CellRangeAddress(0,3,9,9));
        sheet.addMergedRegion(new CellRangeAddress(0,3,10,10));
        //deductions
        sheet.addMergedRegion(new CellRangeAddress(0,0,11,16));
       //Discount
        sheet.addMergedRegion(new CellRangeAddress(1,1,11,13));
        sheet.addMergedRegion(new CellRangeAddress(2,3,11,11)); //sc
        sheet.addMergedRegion(new CellRangeAddress(2,3,12,12)); // pwd
        sheet.addMergedRegion(new CellRangeAddress(2,3,13,13)); // others
        sheet.addMergedRegion(new CellRangeAddress(1,3,14,14)); // returns
        sheet.addMergedRegion(new CellRangeAddress(1,3,15,15));// void
        sheet.addMergedRegion(new CellRangeAddress(1,3,16,16)); //total deduction



        //Adjustment on vat

        sheet.addMergedRegion(new CellRangeAddress(0,0,17,22));   // adjustment on vat
        sheet.addMergedRegion(new CellRangeAddress(1,1,17,19)); //Discount


        sheet.addMergedRegion(new CellRangeAddress(2,3,17,17)); //sc
        sheet.addMergedRegion(new CellRangeAddress(2,3,18,18)); // pwd
        sheet.addMergedRegion(new CellRangeAddress(2,3,19,19)); // others

        sheet.addMergedRegion(new CellRangeAddress(1,3,20,20)); // returns
        sheet.addMergedRegion(new CellRangeAddress(1,3,21,21));// void
        sheet.addMergedRegion(new CellRangeAddress(1,3,22,22)); //total deduction


        sheet.addMergedRegion(new CellRangeAddress(0,3,23,23)); //Vat payable
        sheet.addMergedRegion(new CellRangeAddress(0,3,24,24)); // net sales
        sheet.addMergedRegion(new CellRangeAddress(0,3,25,25)); // Sales Overrun/OverFlow
        sheet.addMergedRegion(new CellRangeAddress(0,3,26,26)); //Total Income sales
        sheet.addMergedRegion(new CellRangeAddress(0,3,27,27)); // Reset Counter
        sheet.addMergedRegion(new CellRangeAddress(0,3,28,28)); //Z counter
        sheet.addMergedRegion(new CellRangeAddress(0,3,29,29)); //Remarks


        for(int x=0;x<30;x++){
            sheet.setColumnWidth(x, 6000);
        }


        //for header

        Row header = sheet.createRow(0);



        Cell headercell;
        for (int colName=0;colName<20;colName++){

            CellStyle headerStyle = workbook.createCellStyle();

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();

            if (colName<3){
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            }
            else  if ( colName>2 && colName<5  ){ //
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());

            }
         else if (colName==5){
                headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            }
         else if(colName==6){
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            }
        else  if (colName>6&&colName<11){
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            }
        else if (colName==11){
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            }



         else if (colName==12){
                headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            }
         else{
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            }
//         else if (colName>=17){
//                headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//            }
//            else if (colName>24&&colName<30)
//           // else
//            {
////                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//            }





            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setWrapText(true);
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 13);
            font.setBold(true);
            headerStyle.setFont(font);

            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());


            if (colName==12){
                headercell=header.createCell(17);
                headercell.setCellValue(ColumnName.get(colName));
                headercell.setCellStyle(headerStyle);
            }
            else if(colName>12){

                    headercell=header.createCell(colName+10);
                    headercell.setCellValue(ColumnName.get(colName));
                    headercell.setCellStyle(headerStyle);

            }
            else{
                headercell=header.createCell(colName);
                headercell.setCellValue(ColumnName.get(colName));
                headercell.setCellStyle(headerStyle);
            }




        }


//
//
//
//
//
//
//
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

//        Row row = sheet.createRow(2);
//        Cell cell = row.createCell(0);
//        cell.setCellValue("John Smith");
//        cell.setCellStyle(style);
//
//        cell = row.createCell(1);
//        cell.setCellValue(20);
//        cell.setCellStyle(style);


//        File currDir = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_REPORT/");
//       // String path = currDir.getAbsolutePath();
//        String fileLocation = currDir + "BIR_REPORT.xlsx";

        // for column


        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 13);
        font.setBold(true);



        CellStyle headerStyle = workbook.createCellStyle();
        CellStyle headerStyle2 = workbook.createCellStyle();
        CellStyle headerStyle3 = workbook.createCellStyle();



        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setWrapText(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //headerstyle green
        headerStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle2.setAlignment(HorizontalAlignment.CENTER);
        headerStyle2.setWrapText(true);
        headerStyle2.setFont(font);
        headerStyle2.setAlignment(HorizontalAlignment.CENTER);
        headerStyle2.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle2.setBorderBottom(BorderStyle.THIN);
        headerStyle2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle2.setBorderLeft(BorderStyle.THIN);
        headerStyle2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle2.setBorderRight(BorderStyle.THIN);
        headerStyle2.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle2.setBorderTop(BorderStyle.THIN);
        headerStyle2.setTopBorderColor(IndexedColors.BLACK.getIndex());
        //headerstyle non
        headerStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle3.setAlignment(HorizontalAlignment.CENTER);
        headerStyle3.setWrapText(true);
        headerStyle3.setFont(font);
        headerStyle3.setAlignment(HorizontalAlignment.CENTER);
        headerStyle3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle3.setBorderBottom(BorderStyle.THIN);
        headerStyle3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle3.setBorderLeft(BorderStyle.THIN);
        headerStyle3.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle3.setBorderRight(BorderStyle.THIN);
        headerStyle3.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle3.setBorderTop(BorderStyle.THIN);
        headerStyle3.setTopBorderColor(IndexedColors.BLACK.getIndex());


        /*  Generating header */




                    // row 1
                    Row row = sheet.createRow(1);
                    Cell cell = row.createCell(11);
                    cell.setCellValue("Discount");
                    cell.setCellStyle(headerStyle);


                    /* item for adjustment on vat row 1 */
                    cell = row.createCell(17);
                    cell.setCellValue("Discount");
                    cell.setCellStyle(headerStyle2);

                    cell = row.createCell(20);
                    cell.setCellValue("Vat On Returns");
                    cell.setCellStyle(headerStyle2);

                    cell = row.createCell(20);
                    cell.setCellValue("Vat On \n Returns");
                    cell.setCellStyle(headerStyle2);

                    cell = row.createCell(21);
                    cell.setCellValue("Others");
                    cell.setCellStyle(headerStyle2);


                    cell = row.createCell(22);
                    cell.setCellValue("Total \n Vat \n Adjustment");
                    cell.setCellStyle(headerStyle2);
                    /* end of item for adj vat */



                    /* item for deduction row 1 */

                    cell = row.createCell(14);
                    cell.setCellValue("Returns");
                    cell.setCellStyle(headerStyle);


                    cell = row.createCell(15);
                    cell.setCellValue("Void");
                    cell.setCellStyle(headerStyle);


                    cell = row.createCell(16);
                    cell.setCellValue("Total \n Deductions");
                    cell.setCellStyle(headerStyle);

                    /* End  of item for deduction row 1 */



                    /* item for deduction row 2 */

                    row = sheet.createRow(2);
                    cell = row.createCell(11);
                    cell.setCellValue("SC");
                    cell.setCellStyle(headerStyle);


                    cell = row.createCell(12);
                    cell.setCellValue("PWD");
                    cell.setCellStyle(headerStyle);

                    cell = row.createCell(13);
                    cell.setCellValue("Others");
                    cell.setCellStyle(headerStyle);

                    /* End item for deduction row 2 */


                    /* item for adjust on vat row 2 */
                    cell = row.createCell(17);
                    cell.setCellValue("SC");
                    cell.setCellStyle(headerStyle2);

                    cell = row.createCell(18);
                    cell.setCellValue("PWD");
                    cell.setCellStyle(headerStyle2);

                    cell = row.createCell(19);
                    cell.setCellValue("Others");
                    cell.setCellStyle(headerStyle2);
                    /* End item for  adjust on vat row 2 */ //headerstyle orange

        /* End generting header */



        /* generating data */

//        row = sheet.createRow(2);
//        cell = row.createCell(11);
//        cell.setCellValue("SC");
//        cell.setCellStyle(headerStyle);

        int x=4;


        for (int ctr=0;ctr<DateTransaction.size();ctr++) {
            int y=0;



            ORTrans(DateTransaction.get(ctr));
            AccumulatedBalanceBeginning(DateTransaction.get(ctr));
            AccumulatedBalanceEnding(DateTransaction.get(ctr));
            SalesIssuedManual(DateTransaction.get(ctr));
            GrossSales(DateTransaction.get(ctr));
            VatableSales(DateTransaction.get(ctr));
            VatAmount(DateTransaction.get(ctr));
            VatExemptSales(DateTransaction.get(ctr));
            ZeroRatedSales(DateTransaction.get(ctr));

            SeniorDiscount(DateTransaction.get(ctr));
            PWDDiscount(DateTransaction.get(ctr));
            OtherDiscount(DateTransaction.get(ctr));
            ReturnDiscount(DateTransaction.get(ctr));
            VoidDiscount(DateTransaction.get(ctr));
            TotalDiscount(DateTransaction.get(ctr));

            SCDDiscountAdj(DateTransaction.get(ctr));
            PWDDiscountAdj(DateTransaction.get(ctr));
            OtherDiscountAdj(DateTransaction.get(ctr));
            VatOnReturnAdj(DateTransaction.get(ctr));
            OtherDiscountAdj2(DateTransaction.get(ctr));
            TotalVatAdj(DateTransaction.get(ctr));
            VatPayable(DateTransaction.get(ctr));
            LessVat(DateTransaction.get(ctr));
            FinalLessvat(DateTransaction.get(ctr));
            NetSales(DateTransaction.get(ctr));
            SalesOverRunFlow(DateTransaction.get(ctr));
            TotalIncomeSales(DateTransaction.get(ctr));
            ResetCounter(DateTransaction.get(ctr));
            ZCounter(DateTransaction.get(ctr));









            //for date
            row = sheet.createRow(x);
            cell = row.createCell(y);
            cell.setCellValue(DateTransaction.get(ctr));
            cell.setCellStyle(headerStyle3);
            y++;
            //beginning si/or no
            cell = row.createCell(y);
            cell.setCellValue(BeginningOR);
            cell.setCellStyle(headerStyle3);
            y++;
            //ending si/or no
            cell = row.createCell(y);
            cell.setCellValue(EndingOR);
            cell.setCellStyle(headerStyle3);
            y++;
            //Grand Accum Ending balance
            cell = row.createCell(y);
            cell.setCellValue(EndingAccumulatedBalance);
            cell.setCellStyle(headerStyle3);
            y++;
            //Grand Accum Beginning balance
            cell = row.createCell(y);
            cell.setCellValue(BeginningAccumulatedBalance);
            cell.setCellStyle(headerStyle3);
            y++;
            //sales issued
            cell = row.createCell(y);
            cell.setCellValue(SalesIssuedManual);
            cell.setCellStyle(headerStyle3);
            y++;
            //gross sales
            cell = row.createCell(y);
            cell.setCellValue(GrossSales);
            cell.setCellStyle(headerStyle3);
            y++;
            //VATable Sales
            cell = row.createCell(y);
            cell.setCellValue(VatableSales);
            cell.setCellStyle(headerStyle3);
            y++;
            //Vat amount
            cell = row.createCell(y);
            cell.setCellValue(VatAmount);
            cell.setCellStyle(headerStyle3);
            y++;
            //Vat exempt
            cell = row.createCell(y);
            cell.setCellValue(VatExempt);
            cell.setCellStyle(headerStyle3);
            y++;
            //Zero rated
            cell = row.createCell(y);
            cell.setCellValue(ZeroRatedSales);
            cell.setCellStyle(headerStyle3);
            y++;
            //deduction sc
            cell = row.createCell(y);
            cell.setCellValue(SCDDiscount);
            cell.setCellStyle(headerStyle3);
            y++;
            //deduction pwd
            cell = row.createCell(y);
            cell.setCellValue(PWDDiscount);
            cell.setCellStyle(headerStyle3);
            y++;
            //Others
            cell = row.createCell(y);
            cell.setCellValue(OtherDiscount);
            cell.setCellStyle(headerStyle3);
            y++;
            //returns
            cell = row.createCell(y);
            cell.setCellValue(ReturnDiscount);
            cell.setCellStyle(headerStyle3);
            y++;
            //void
            cell = row.createCell(y);
            cell.setCellValue(VoidDiscount);
            cell.setCellStyle(headerStyle3);
            y++;
            //Net sales
            cell = row.createCell(y);
            cell.setCellValue(TotalDiscount);
            cell.setCellStyle(headerStyle3);
            y++;
            //sc adj
            cell = row.createCell(y);
            cell.setCellValue(SCDDiscountAdj);
            cell.setCellStyle(headerStyle3);
            y++;
            //pwd adj
            cell = row.createCell(y);
            cell.setCellValue(PWDDiscountAdj);
            cell.setCellStyle(headerStyle3);
            y++;
            //others
            cell = row.createCell(y);
            cell.setCellValue(OtherDiscountAdj);
            cell.setCellStyle(headerStyle3);
            y++;
            //vat on returns
            cell = row.createCell(y);
            cell.setCellValue(VatOnReturnAdj);
            cell.setCellStyle(headerStyle3);
            y++;
            //Others
            cell = row.createCell(y);
            cell.setCellValue(OtherDiscountAdj2);
            cell.setCellStyle(headerStyle3);
            y++;
            //total vat adj
            cell = row.createCell(y);
            cell.setCellValue(TotalVatAdj);
            cell.setCellStyle(headerStyle3);
            y++;

            //vat payable
            cell = row.createCell(y);
            cell.setCellValue(VatPayable);
            cell.setCellStyle(headerStyle3);
            y++;

            //Net sales
            cell = row.createCell(y);
            cell.setCellValue(NetSales);
            cell.setCellStyle(headerStyle3);
            y++;
            //SalesOverun
            cell = row.createCell(y);
            cell.setCellValue(SalesOverRunFlow);
            cell.setCellStyle(headerStyle3);
            y++;
            //total income sales
            cell = row.createCell(y);
            cell.setCellValue(TotalIncomeSales);
            cell.setCellStyle(headerStyle3);
            y++;
            //Reset counter
            cell = row.createCell(y);
            cell.setCellValue(ResetCounter);
            cell.setCellStyle(headerStyle3);
            y++;
            //Z counter
            cell = row.createCell(y);
            cell.setCellValue(ZCounter);
            cell.setCellStyle(headerStyle3);
            y++;
            //remarks
            cell = row.createCell(y);
            cell.setCellValue(" ");
            cell.setCellStyle(headerStyle3);


            x++;



        }




        /* end  generating data */

















//
//




//        row = sheet.createRow(1);
//        cell = row.createCell(17);
//        cell.setCellValue("Discount");
//        cell.setCellStyle(headerStyle);
//
//        row = sheet.createRow(2);
//        cell = row.createCell(17);
//        cell.setCellValue("SC");
//        cell.setCellStyle(headerStyle);
//
//        cell = row.createCell(18);
//        cell.setCellValue("PWD");
//        cell.setCellStyle(headerStyle);
//
//        cell = row.createCell(19);
//        cell.setCellValue("Others");
//        cell.setCellStyle(headerStyle);
//
//
//        row = sheet.createRow(1);
//        cell = row.createCell(20);
//        cell.setCellValue("Vat on Returns");
//        cell.setCellStyle(headerStyle);
//
//        row = sheet.createRow(1);
//        cell = row.createCell(21);
//        cell.setCellValue("Others");
//        cell.setCellStyle(headerStyle);
//
//        row = sheet.createRow(1);
//        cell = row.createCell(22);
//        cell.setCellValue("Total Vat Adjustment");
//        cell.setCellStyle(headerStyle);













//        cell = row.createCell(1);
//        cell.setCellValue(20);
//        cell.setCellStyle(style);


        File xls = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_REPORT", "BIR_REPORT.xlsx");

        try {
            FileOutputStream outputStream = new FileOutputStream(xls);
            workbook.write(outputStream);
            workbook.close();
        }
        catch (Exception ex){

        }
    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
            Log.e("date1", String.valueOf(date1.getMonth()+"/"+date1.getDate()+"/"+date1.getYear()));
            Log.e("date2", String.valueOf(date2.getMonth()+"/"+date2.getDate()+"/"+date2.getYear()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);


//            dates.add(output.format(cal1.time))
//            cal1.add(Calendar.DATE, 1)
        }
        return dates;
    }


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


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    List<admin_manage_report_item> AdminReportItem = new ArrayList<>();
    private ArrayList<String> DateTransaction = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();

    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_manage_report.RecyclerviewAdapter.MyViewHolder>{
        List<admin_manage_report_item> AdminReportItem;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_manage_report.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<admin_manage_report_item> AdminReportItem, ArrayList<String> selectList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.AdminReportItem = AdminReportItem;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_manage_report.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_layout,parent,false);



            admin_manage_report.RecyclerviewAdapter.MyViewHolder holder = new admin_manage_report.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_manage_report.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());



                holder.tv_Date.setText(String.valueOf(AdminReportItem.get(position).getDate()));
            holder.tv_beginningOR.setText(String.valueOf((AdminReportItem.get(position).getBeginningOR())));
            holder.tv_endingOR.setText(String.valueOf((AdminReportItem.get(position).getEndingOR())));
            holder.tv_accumEndBal.setText(String.valueOf((AdminReportItem.get(position).getEndingAccumulateBalance())));
            holder.tv_accumBegBal.setText(String.valueOf((AdminReportItem.get(position).getBeginningAccumulateBalance())));
            holder.tv_grossSales.setText(String.valueOf((AdminReportItem.get(position).getGrossSales())));
            holder.tv_salesIssuedManual.setText(String.valueOf((AdminReportItem.get(position).getSalesIssuedManual())));
            holder.tv_vatableSales.setText(String.valueOf((AdminReportItem.get(position).getVatableSales())));
            holder.tv_vatAmount.setText(String.valueOf((AdminReportItem.get(position).getVatAmount())));
            holder.tv_vatExemptSales.setText(String.valueOf((AdminReportItem.get(position).getVatExempt())));
            holder.tv_zeroRatedSales.setText(String.valueOf((AdminReportItem.get(position).getZeroRated())));
            holder.tv_seniorDiscount.setText(String.valueOf((AdminReportItem.get(position).getSeniorDiscount())));
            holder.tv_pwdDiscount.setText(String.valueOf((AdminReportItem.get(position).getPWDDiscount())));
            holder.tv_othersDiscount.setText(String.valueOf((AdminReportItem.get(position).getOtherDiscount())));

            holder.tv_returns.setText(String.valueOf((AdminReportItem.get(position).getReturnsDiscount())));


            holder.tv_void.setText(String.valueOf((AdminReportItem.get(position).getVoidDiscount())));
            holder.tv_totalDeductions.setText(String.valueOf((AdminReportItem.get(position).getTotalDeduction())));

            holder.tv_scOnVat.setText(String.valueOf((AdminReportItem.get(position).getSeniorDiscountAdj())));
            holder.tv_pwdOnVat.setText(String.valueOf((AdminReportItem.get(position).getPWDDiscountAdj())));
            holder.tv_othersOnVat.setText(String.valueOf((AdminReportItem.get(position).getOtherDiscountAdj())));
            holder.tv_vatOnReturns.setText(String.valueOf((AdminReportItem.get(position).getReturnDiscountAdj())));

            holder.tv_othersOnVat2.setText(String.valueOf((AdminReportItem.get(position).getOthersDiscAdj2())));
            holder.tv_totalVatAdjOnVat.setText(String.valueOf((AdminReportItem.get(position).getTotalVatAdj())));
            holder.tv_vatPayable.setText(String.valueOf((AdminReportItem.get(position).getVatPayable())));
            holder.tv_netSales.setText(String.valueOf((AdminReportItem.get(position).getNetSales())));
            holder.tv_salesOverRunFlow.setText(String.valueOf((AdminReportItem.get(position).getSalesOverrunFlow())));
            holder.tv_totalIncomeSales.setText(String.valueOf((AdminReportItem.get(position).getTotalIncomeSales())));
            holder.tv_resetCounter.setText(String.valueOf((AdminReportItem.get(position).getTotalResetCounter())));
            holder.tv_zCounter.setText(String.valueOf((AdminReportItem.get(position).getZCounter())));





        }

        @Override
        public int getItemCount() {
            return AdminReportItem.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tv_beginningOR,tv_endingOR;
            TextView tv_accumEndBal,tv_accumBegBal,tv_grossSales,tv_vatableSales,tv_vatAmount,tv_vatExemptSales,tv_zeroRatedSales;
            TextView tv_seniorDiscount,tv_pwdDiscount,tv_othersDiscount,tv_returns,tv_void,tv_totalDeductions;
            TextView tv_scOnVat,tv_pwdOnVat,tv_othersOnVat,tv_othersOnVat2,tv_totalVatAdjOnVat,tv_vatPayable,tv_netSales,tv_salesOverRunFlow,tv_totalIncomeSales;
            TextView tv_resetCounter,tv_zCounter,tv_remarks,tv_vatOnReturns;
            TextView tv_Date;
            TextView tv_salesIssuedManual;




            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_beginningOR = itemView.findViewById(R.id.tv_beginningOR);
                tv_endingOR = itemView.findViewById(R.id.tv_endingOR);
                tv_accumEndBal= itemView.findViewById(R.id.tv_accumEndBal);
                tv_accumBegBal= itemView.findViewById(R.id.tv_accumBegBal);
                tv_salesIssuedManual=itemView.findViewById(R.id.tv_salesIssuedManual);
                tv_grossSales=itemView.findViewById(R.id.tv_grossSales);
                tv_vatableSales = itemView.findViewById(R.id.tv_vatableSales);
                tv_vatAmount = itemView.findViewById(R.id.tv_vatAmount);
                tv_vatExemptSales = itemView.findViewById(R.id.tv_vatExemptSale);
                tv_zeroRatedSales = itemView.findViewById(R.id.tv_zeroRatedSales);
                tv_seniorDiscount = itemView.findViewById(R.id.tv_seniorDiscount);
                tv_pwdDiscount = itemView.findViewById(R.id.tv_pwdDiscount);
                tv_othersDiscount=itemView.findViewById(R.id.tv_otherDiscount);
                tv_returns=itemView.findViewById(R.id.tv_returns);
                tv_void=itemView.findViewById(R.id.tv_void);
                tv_totalDeductions=itemView.findViewById(R.id.tv_totalDeduction);
                tv_scOnVat=itemView.findViewById(R.id.tv_scOnVat);
                tv_pwdOnVat=itemView.findViewById(R.id.tv_pwdOnVat);
                tv_othersOnVat=itemView.findViewById(R.id.tv_othersOnVat);
                tv_vatOnReturns=itemView.findViewById(R.id.tv_vatOnReturns);
                tv_othersOnVat2=itemView.findViewById(R.id.tv_othersOnvat2);
                tv_totalVatAdjOnVat=itemView.findViewById(R.id.tv_totVatAdjOnVat);
                tv_vatPayable=itemView.findViewById(R.id.tv_vatPayable);
                tv_netSales=itemView.findViewById(R.id.tv_netSales);
                tv_salesOverRunFlow=itemView.findViewById(R.id.tv_salesOverRunFlow);
                tv_totalIncomeSales=itemView.findViewById(R.id.tv_totalIncomeSales);
                tv_resetCounter=itemView.findViewById(R.id.tv_resetCounter);
                tv_zCounter=itemView.findViewById(R.id.tv_zCounter);
                tv_remarks=itemView.findViewById(R.id.tv_remarks);
                tv_Date = itemView.findViewById(R.id.tv_date);



            }
        }
    }



    private void showSummary(){

        fillOrderList();
        refreshRecycleview();



    }

    RecyclerView sv_report;
    public void refreshRecycleview(){
        sv_report = findViewById(R.id.sv_report);
        sv_report.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        sv_report.setLayoutManager(layoutManager);
        mAdapter=new admin_manage_report.RecyclerviewAdapter(AdminReportItem,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        sv_report.setAdapter(mAdapter);

        //invoice item list



    }

    private ArrayList<String> TransactionNoList;
//    private ArrayList<String>  ActivityList;
//    private ArrayList<String>  UserList;




    private void fillOrderList() {
       // TransactionNoList = new ArrayList<>();

       AdminReportItem.clear();


      //  CheckItemDatabase2();
        db = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        for (int ctr=0;ctr<DateTransaction.size();ctr++){


            ORTrans(DateTransaction.get(ctr));
            AccumulatedBalanceBeginning(DateTransaction.get(ctr));
            AccumulatedBalanceEnding(DateTransaction.get(ctr));
            SalesIssuedManual(DateTransaction.get(ctr));
            GrossSales(DateTransaction.get(ctr));
            VatableSales(DateTransaction.get(ctr));
            VatAmount(DateTransaction.get(ctr));
            VatExemptSales(DateTransaction.get(ctr));
            ZeroRatedSales(DateTransaction.get(ctr));

            SeniorDiscount(DateTransaction.get(ctr));
            PWDDiscount(DateTransaction.get(ctr));
            OtherDiscount(DateTransaction.get(ctr));
            ReturnDiscount(DateTransaction.get(ctr));
            VoidDiscount(DateTransaction.get(ctr));
            TotalDiscount(DateTransaction.get(ctr));

            SCDDiscountAdj(DateTransaction.get(ctr));
            PWDDiscountAdj(DateTransaction.get(ctr));
            OtherDiscountAdj(DateTransaction.get(ctr));
            VatOnReturnAdj(DateTransaction.get(ctr));
            OtherDiscountAdj2(DateTransaction.get(ctr));
            TotalVatAdj(DateTransaction.get(ctr));
            VatPayable(DateTransaction.get(ctr));
            LessVat(DateTransaction.get(ctr));
            FinalLessvat(DateTransaction.get(ctr));
            NetSales(DateTransaction.get(ctr));
            SalesOverRunFlow(DateTransaction.get(ctr));
            TotalIncomeSales(DateTransaction.get(ctr));
            ResetCounter(DateTransaction.get(ctr));
            ZCounter(DateTransaction.get(ctr));



            admin_manage_report_item p0 = new admin_manage_report_item(

                    DateTransaction.get(ctr),
                    BeginningOR,
                    EndingOR,
                    EndingAccumulatedBalance,
                    BeginningAccumulatedBalance,

                    SalesIssuedManual,
                    GrossSales,
                    VatableSales,
                    VatAmount,
                    VatExempt,
                    ZeroRatedSales,
                    SCDDiscount,
                    PWDDiscount,
                    OtherDiscount,
                    ReturnDiscount,
                    VoidDiscount,
                    TotalDiscount,
                    SCDDiscountAdj,
                    PWDDiscountAdj,
                    OtherDiscountAdj,
                    VatOnReturnAdj,
                    OtherDiscountAdj2,
                    TotalVatAdj,
                    VatPayable,
                    NetSales,
                    SalesOverRunFlow,
                    TotalIncomeSales,
                    ResetCounter,
                    ZCounter


            );
            Log.e("RESET COUNTER",ResetCounter);
            //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

            AdminReportItem.addAll(Arrays.asList(new admin_manage_report_item[]{p0}));




        }

       db.close();
        //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";



    }



    String BeginningOR;
    String EndingOR;
    String BeginningAccumulatedBalance;
    String EndingAccumulatedBalance;
    String SalesIssuedManual;
    String GrossSales;
    String VatableSales;
    String VatAmount;
    String VatExempt;
    String ZeroRatedSales;
    String SCDDiscount;
    String PWDDiscount;
    String OtherDiscount;
    String ReturnDiscount;
    String VoidDiscount;
    String TotalDiscount;

    String SCDDiscountAdj;
    String PWDDiscountAdj;
    String OtherDiscountAdj;
    String VatOnReturnAdj;
    String OtherDiscountAdj2;
    String TotalVatAdj;

    String VatPayable;
    String NetSales;
    String SalesOverRunFlow;
    String TotalIncomeSales;
    String ResetCounter;
    String ZCounter;


    String FinalLessVat;


    SQLiteDatabase db;
    public void ORTrans(String TransDate){
        try {



            //

            //Cursor getBegOR =db.rawQuery("select ORTrans from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
            //Cursor getEndOR =db.rawQuery("select ORTrans from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);

            Cursor getBegOR = db.rawQuery("select ORTrans from FinalTransactionReport where TransDate = '" + TransDate + "'", null);
            Cursor getEndOR = db.rawQuery("select ORTrans from FinalTransactionReport where TransDate = '" + TransDate + "'", null);

            if (getBegOR.getCount() != 0) {
                getBegOR.moveToFirst();
                // setB.setText(getBegOR.getString(0));
                // setBeginningOR(getBegOR.getString(0));
                BeginningOR = getBegOR.getString(0);
            }
            if (getBegOR.getCount() == 0) {
                getBegOR.moveToFirst();
                BeginningOR = "0";
                // setBeginningOR("NO DATA");
                // tv_beginningOR.setText("NO DATA");
            }

            if (getEndOR.getCount() != 0) {
                getEndOR.moveToLast();
                EndingOR = getEndOR.getString(0);
                //  setEndingOR(getEndOR.getString(0));
                // tv_endingOR.setText(getEndOR.getString(0));
            }
            if (getEndOR.getCount() == 0) {
                getEndOR.moveToLast();
                EndingOR = "0";
                //  setEndingOR("NO DATA");
                //tv_endingOR.setText("NO DATA");
            } else {
                Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                Log.e("OR TRANS", "NO DATA FOUND");
            }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }

    public void AccumulatedBalanceBeginning(String TransDate){ // beginning balance

        try {


            Cursor AccumBegBal = db.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate < '" + TransDate + "'", null);


            //  Cursor AccumBegBal =db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate < strftime('%m/%d/%Y','"+TransDate+"')",null);


            if (AccumBegBal.getCount() != 0) {
                AccumBegBal.moveToFirst();

//            setBegBal(AccumBegBal.getString(0));
//            if (getBegBal()==null){
//                setBegBal("0");
//            }
                BeginningAccumulatedBalance = AccumBegBal.getString(0);
                // setBeginningAccumulateBalance(AccumBegBal.getString(0));
                if (AccumBegBal.getString(0) == null) {
                    BeginningAccumulatedBalance = "0.00";
                    // setBeginningAccumulateBalance("0.00");
                    //tv_accumBegBal.setText("0.00");
                }


            }
            if (AccumBegBal.getCount() == 0) {
//            Cursor BeginningORNoZ2 = db3.rawQuery("select * from FinalTransactionReport ", null);
//            if (BeginningORNoZ2.getCount()!=0){
//                BeginningORNoZ2.moveToLast();
//                setBegORNo(BeginningORNoZ.getString(1));
//
//            }
//            else{
//                setBegORNo("0");
//            }
                BeginningAccumulatedBalance = "0.00";
                //  setBeginningAccumulateBalance("0.00");
                //tv_accumBegBal.setText(0);
            }
        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }
    private void AccumulatedBalanceEnding(String TransDate){
        try{



            Log.e("TransDate",TransDate);


        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor AccumBegBal = db.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate <= '" + TransDate +"'", null);
           // Cursor AccumBegBal = db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate <= '" + TransDate +"'", null);
      //  Cursor AccumBegBal =db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate = strftime('%m/%d/%Y','"+TransDate+"')",null);




        if (AccumBegBal.getCount()!=0){
            AccumBegBal.moveToFirst();

//            setBegBal(AccumBegBal.getString(0));
//            if (getBegBal()==null){
//                setBegBal("0");
//            }

            if (AccumBegBal.getString(0) == null || AccumBegBal.getString(0) =="" || AccumBegBal.getString(0) == " ") {
                EndingAccumulatedBalance="0.00";
             //   setEndingAccumulateBalance("0.00");
            }
            else{
                EndingAccumulatedBalance=AccumBegBal.getString(0);
               // setEndingAccumulateBalance(AccumBegBal.getString(0));
                // tv_accumEndBal.setText(AccumBegBal.getString(0));
            }



        }
        if (AccumBegBal.getCount()==0){
//            Cursor BeginningORNoZ2 = db3.rawQuery("select * from FinalTransactionReport ", null);
//            if (BeginningORNoZ2.getCount()!=0){
//                BeginningORNoZ2.moveToLast();
//                setBegORNo(BeginningORNoZ.getString(1));
//
//            }
//            else{
//                setBegORNo("0");
//            }
            //tv_accumEndBal.setText(0);
            EndingAccumulatedBalance="0.00";
           // setEndingAccumulateBalance("0.00");
        }


        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }

    }

    private void SalesIssuedManual(String TransDate){
        SalesIssuedManual="0.00";
    }
    private void GrossSales(String TransDate){

        try{

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptItemFinal where TransactionDate = '" + TransDate + "'", null);
        if (GrossSalesCursor.getCount()!=0){
            Double  Gross=0.00;
            GrossSalesCursor.moveToFirst();
            if (GrossSalesCursor.getString(0)==null){
                Gross=0.00;
            }
            else{

                Gross=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }
            GrossSales= String.format("%7.2f",Gross);
          //  GrossSales(String.valueOf(Gross));
        }
        else{
            GrossSales=String.valueOf("0.00");
          //  setGrossSales(String.valueOf("0.00"));
            //  tv_grossSales.setText(String.valueOf("0.00"));
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }

    private void VatableSales(String TransDate){

        try{

        Double vatableSales=0.00;
        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(TotalVatableSales) from FinalTransactionReport where TransDate = '" + TransDate +"'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();
            if (GrossSalesCursor.getString(0)==null){
                vatableSales=0.00;
            }
            else{
                vatableSales=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }
            VatableSales= String.format("%7.2f",vatableSales);
          //  V(String.valueOf(vatableSales));
        }
        else{
            VatableSales= String.format("%7.2f",vatableSales);
           // setVatableSales(String.valueOf(vatableSales));
            // tv_vatableSales.setText(String.valueOf(vatableSales));
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }



    }

    private void VatAmount(String TransDate){
        try{

        Double vatAmount=0.00;

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(TotalVatAmount) from FinalTransactionReport where TransDate = '" + TransDate +"'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();
            if (GrossSalesCursor.getString(0)==null){
                vatAmount=0.00;
            }else{
                vatAmount=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }
            VatAmount= String.format("%7.2f",vatAmount);
          //  setVatAmount(String.valueOf(vatAmount));
        }
        else{
            VatAmount= String.format("%7.2f",vatAmount);
            //setVatAmount(String.valueOf(vatAmount));
        }


        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }

    }

    private void VatExemptSales(String TransDate){
        try{

         Double   vatExemptSales=0.00;

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(TotalVatExempt) from FinalTransactionReport where TransDate = '" + TransDate +"'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();
            if (GrossSalesCursor.getString(0)==null){
                vatExemptSales=0.00;
            }
            else{
                vatExemptSales=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }
            VatExempt= String.format("%7.2f",vatExemptSales);
           // setVatExempt(String.valueOf(vatExemptSales));
        }
        else{
            VatExempt= String.format("%7.2f",vatExemptSales);
          //  setVatExempt(String.valueOf(vatExemptSales));
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }

    private void ZeroRatedSales(String TransDate){

        try {


            Double zeroRatedSales = 0.00;

            //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
            Cursor GrossSalesCursor = db.rawQuery("select sum(TotalZeroRatedSales) from FinalTransactionReport where TransDate = '" + TransDate + "'", null);
            if (GrossSalesCursor.getCount() != 0) {
                GrossSalesCursor.moveToNext();
                Log.e("ZeroRatedValue1", zeroRatedSales.toString());
                if (GrossSalesCursor.getString(0) != null) {
                    zeroRatedSales = (Double.parseDouble(GrossSalesCursor.getString(0)));
                    Log.e("ZeroRatedValue2", zeroRatedSales.toString());
                }
                ZeroRatedSales =  String.format("%7.2f",zeroRatedSales);
                //setZeroRated(String.valueOf(zeroRatedSales));
            } else {
                ZeroRatedSales =  String.format("%7.2f",zeroRatedSales);
                // setZeroRated(String.valueOf(zeroRatedSales));
                Log.e("ZeroRatedValue3", zeroRatedSales.toString());
            }
        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }



    }

    Double seniorDiscount=0.00;
    Double pwdDiscount=0.00;
    Double otherDiscount=0.00;
    //Deduction
    private void SeniorDiscount(String TransDate){
        try{
            seniorDiscount=0.00;


        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        //    Cursor GrossSalesCursor = db3.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscount where DiscountType='scd' and TransactionDate = '" + date1Format1 +"'", null);
        Log.e("SENIORDISC DATE", date1Format1);
        Cursor GrossSalesCursor = db.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscount where DiscountType Like '%SCD%' and TransactionDate='"+TransDate+"'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();
            if (GrossSalesCursor.getString(0)==null){
                seniorDiscount=0.00;
            }
            else{
                seniorDiscount=(Double.parseDouble(GrossSalesCursor.getString(0)));
                totalDiscount+=seniorDiscount;
            }

            SCDDiscount=( String.format("%7.2f",seniorDiscount));
        }
        else{
            SCDDiscount=( String.format("%7.2f",seniorDiscount));
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }

    private void PWDDiscount(String TransDate){
        try{

            pwdDiscount=0.00;
        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscount where TransactionDate = '" + TransDate +"'and DiscountType like '%pwd%' ", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();


            if (GrossSalesCursor.getString(0)==null){
                pwdDiscount=0.00;

            }
            else{
                pwdDiscount=(Double.parseDouble(GrossSalesCursor.getString(0)));
                totalDiscount+=pwdDiscount;
            }

            PWDDiscount=( String.format("%7.2f",pwdDiscount));
        }
        else{
            PWDDiscount=( String.format("%7.2f",pwdDiscount));
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }

    }

    private void OtherDiscount(String TransDate){
        try{
            otherDiscount=0.00;

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscount where TransactionDate = '" + TransDate +"' and  DiscountType not like '%scd%' and DiscountType not like '%pwd%'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();


            if (GrossSalesCursor.getString(0)==null){
                otherDiscount=0.00;

            }
            else{
                otherDiscount=(Double.parseDouble(GrossSalesCursor.getString(0)));
                totalDiscount+=otherDiscount;
            }

            OtherDiscount=( String.format("%7.2f",otherDiscount));
        }
        else{
            OtherDiscount=( String.format("%7.2f",otherDiscount));
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }

    private void ReturnDiscount(String TransDate){
        ReturnDiscount="0.00";
    }
    private void VoidDiscount(String TransDate){
        VoidDiscount="0.00";
    }
    private void TotalDiscount(String TransDate){
        TotalDiscount= String.format("%7.2f",seniorDiscount+pwdDiscount+otherDiscount);
    }

    //Adj
    Double seniorDiscountVat=0.00;
    Double pwdDiscountVAT=0.00;
    private void SCDDiscountAdj(String TransDate){
        try{
        seniorDiscountVat=0.00;

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(VAT) from InvoiceReceiptItemFinalWDiscount where TransactionDate = '" + TransDate +"' and DiscountType Like '%SCD%'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();
            if (GrossSalesCursor.getString(0)==null){
                seniorDiscountVat=0.00;
            }
            else{
                seniorDiscountVat=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }

            SCDDiscountAdj=(String.valueOf(seniorDiscountVat));
        }
        else{
            SCDDiscountAdj=(String.valueOf(seniorDiscountVat));
            //setSeniorDiscountVat(seniorDiscountVat);
        }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }


    }

    private void PWDDiscountAdj(String TransDate){
        try{
         pwdDiscountVAT=0.00;

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(VAT) from InvoiceReceiptItemFinalWDiscount where TransactionDate = '" + TransDate +"'and DiscountType Like '%PWD%'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();


            if (GrossSalesCursor.getString(0)==null){
                pwdDiscountVAT=0.00;

            }
            else{
                pwdDiscountVAT=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }

            PWDDiscountAdj=(String.valueOf(pwdDiscountVAT));
        }
        else{
            PWDDiscountAdj=(String.valueOf(pwdDiscountVAT));
        }
        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
        }



    }

    private void OtherDiscountAdj(String TransDate){
//    try{
//
//        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
//        Cursor GrossSalesCursor = db.rawQuery("select sum(VAT) from InvoiceReceiptItemFinalWDiscount where TransactionDate = '" + TransDate +"'and DiscountType!='scd' or DiscountType!='pwd'", null);
//        if (GrossSalesCursor.getCount()!=0){
//            GrossSalesCursor.moveToFirst();
//            Double otherDiscountVAT=0.00;
//
//            if (GrossSalesCursor.getString(0)==null){
//                otherDiscountVAT=0.00;
//
//            }
//            else{
//                otherDiscountVAT=(Double.parseDouble(GrossSalesCursor.getString(0)));
//            }
//
//            OtherDiscountAdj=(String.valueOf(otherDiscountVAT));
//        }
//        else{
//            OtherDiscountAdj=("0.00");
//        }
//
//    }
//    catch (Exception ex){
//        Log.e("CATCH",ex.toString());
//    }
        OtherDiscountAdj="0.00";


    }

    private void LessVat(String TransDate){
    try{

        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select sum(VAT) from InvoiceReceiptItemFinalWDiscount where TransactionDate = '" + TransDate +"'and DiscountType='DIPLOMAT'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();
            Double lessvat=0.00;

            if (GrossSalesCursor.getString(0)==null){
                lessvat=0.00;

            }
            else{
                lessvat=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }

            FinalLessVat=(String.valueOf(lessvat));
            Log.e("less Vat",FinalLessVat);
        }
        else{
            FinalLessVat=("0.00");
        }

    }
    catch (Exception ex){
        Log.e("CATCH",ex.toString());
        FinalLessVat="0.00";
    }



    }


    private void VatOnReturnAdj(String TransDate){
        VatOnReturnAdj="0.00";
    }
    private void OtherDiscountAdj2(String TransDate){
        OtherDiscountAdj2="0.00";
    }
    private void TotalVatAdj(String TransDate){


        TotalVatAdj= String.format("%7.2f",seniorDiscountVat+pwdDiscountVAT);

    }

    private void VatPayable(String TransDate){
        VatPayable =  String.format("%7.2f",Double.parseDouble(VatAmount)+(Double.parseDouble(TotalVatAdj)));
    }

    private void NetSales(String TransDate){


        String.format("%7.2f",(Double.parseDouble(GrossSales)-(-Double.parseDouble(TotalDiscount))-Double.parseDouble(VatAmount)-FinalLessVatitem));
        //NetSales=String.valueOf(Double.parseDouble(GrossSales)-(-Double.parseDouble(TotalDiscount))-Double.parseDouble(VatAmount)-FinalLessVatitem);
        NetSales =  String.format("%7.2f",(Double.parseDouble(GrossSales)-(-Double.parseDouble(TotalDiscount))-Double.parseDouble(VatAmount)-FinalLessVatitem));
        Log.e("NETSALES",NetSales);
        Log.e("GROSS SALES",GrossSales);
        Log.e("TotalDiscount",TotalDiscount);
        Log.e("VatAmount",VatAmount);
        Log.e("FinalLessVat",String.valueOf(FinalLessVatitem));


    }

    double FinalLessVatitem=0.00;
    private void FinalLessvat(String TransDate){
        try{

            //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
            Cursor GrossSalesCursor = db.rawQuery("select sum(TotalLessVat) from FinalTransactionReport where TransDate = '" + TransDate +"'", null);
            if (GrossSalesCursor.getCount()!=0){
                GrossSalesCursor.moveToFirst();
                Double lessvat=0.00;



                if (GrossSalesCursor.getString(0)==null){
                    FinalLessVatitem=0.00;

                }
                else{
                    FinalLessVatitem=(Double.parseDouble(GrossSalesCursor.getString(0)));
                }



               // FinalLessVatitem=Double.parseDouble(FinalLessVatitem);
                Log.e("less Vat",String.valueOf(FinalLessVatitem));
            }
            else{
                FinalLessVatitem=0.00;
            }

        }
        catch (Exception ex){
            Log.e("CATCH",ex.toString());
            FinalLessVat="0.00";
        }

    }
    private void SalesOverRunFlow(String TransDate){



//        try{
            Cursor SalesOverrun = db.rawQuery("select sum(TotalSalesOverrun) from FinalTransactionReport where TransDate = '" + TransDate +"'", null);
            if (SalesOverrun.getCount()!=0){
                SalesOverrun.moveToFirst();
                Double lessvat=0.00;

               // Log.e("if OVERRUN", SalesOverRunFlow);

                if (SalesOverrun.getString(0)==null){
                    SalesOverRunFlow="0.00";
                    Log.e("IF if OVERRUN", SalesOverRunFlow);

                }
                else{
                    SalesOverRunFlow= String.format("%7.2f",(Double.parseDouble(SalesOverrun.getString(0))));
                    Log.e("IF ELSE OVERRUN", SalesOverRunFlow);
                }



                // FinalLessVatitem=Double.parseDouble(FinalLessVatitem);
            //    Log.e("less Vat",String.valueOf(FinalLessVatitem));
            }
            else{
                Log.e("ELSE OVERRUN", SalesOverRunFlow);
                FinalLessVatitem=0.00;
            }
//        }
//        catch (Exception ex){
//            SalesOverRunFlow="0.00";
//        }




    }
    private void TotalIncomeSales(String TransDate){
        TotalIncomeSales=String.valueOf(Double.parseDouble(NetSales)+Double.parseDouble(SalesOverRunFlow));
    }
    private void ResetCounter(String TransDate){

    }
    private void ZCounter(String TransDate){


try{


        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db.rawQuery("select * from InvoiceReceiptTotal where TransDate <= '" + TransDate +"' and typeOfTransaction='"+"ZREAD"+"'", null);
        if (GrossSalesCursor.getCount()!=0){
            ZCounter=(String.valueOf(GrossSalesCursor.getCount()));
        }
        else{
            ZCounter=("0");
        }




        long modx=Integer.parseInt(ZCounter);
        Log.e("modx",String.valueOf(modx));
        long mody=999999999999L;
        long resetCount = modx/mody;
        Log.e("resetCt",String.valueOf(modx/mody));
       ResetCounter = String.format("%02d", resetCount);

        String formattedTrans =  String.valueOf(modx % mody);
        ZCounter=formattedTrans;
        ZCounter =String.format("%09d",Integer.parseInt(ZCounter));







        //db3.close();

}
catch (Exception ex){
    Log.e("CATCH",ex.toString());
}
    }















}