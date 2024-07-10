package com.abztrakinc.ABZPOS.ADMIN;

import static java.security.AccessController.getContext;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.settingsDB;
import com.abztrakinc.ABZPOS.utils.SunmiPrintHelper;
import com.rt.printerlibrary.bean.UsbConfigBean;
import com.rt.printerlibrary.driver.usb.UsbPrintDriver;
import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.printer.RTPrinter;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import net.posprinter.IDeviceConnection;
import net.posprinter.POSConnect;
import net.posprinter.POSPrinter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;


public class admin_pos_printer_settings extends AppCompatActivity {

    POSPrinter posPrinter;
    Button btn_testPrint,btn_testPrint2,btn_testPrint3,btn_testPrint4,btn_Refresh;
    Spinner sp_cashierInvoice,sp_kitchenPrinter,sp_orderSummary,sp_stickerPrinter;
    ArrayList<String> usbDevices;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pos_printer_settings);
        mcontext = this.getApplicationContext();


        initialize(this.getApplicationContext());


        settingsDB settingsDB = new settingsDB(this.getApplicationContext());

        try {
            settingsDB.insertPrinterSettings(1);
            settingsDB.insertPrinterSettings(2);
            settingsDB.insertPrinterSettings(3);
            settingsDB.insertPrinterSettings(4);
        }catch (Exception ex){
            //Log.e("TAG", ex.toString() );
        }


    }


    public void initialize(Context context){
     //   printer_settings_configuration printerConfig = new printer_settings_configuration();
     //   printerConfig.usbPrinter(getApplicationContext());
        POSConnect.init(context);
        btn_testPrint = findViewById(R.id.btn_testPrint);
        btn_testPrint2 = findViewById(R.id.btn_testPrint2);
        btn_testPrint3 = findViewById(R.id.btn_testPrint3);
        btn_testPrint4 = findViewById(R.id.btn_testPrint4);
        sp_cashierInvoice = findViewById(R.id.sp_cashierInvoice);
        sp_kitchenPrinter = findViewById(R.id.sp_kitchen);
        sp_orderSummary = findViewById(R.id.sp_orderSummary);
        sp_stickerPrinter = findViewById(R.id.sp_stickerPrinter);
        btn_Refresh = findViewById(R.id.btn_refresh);
        checkUSBDevice();
        loadPrinters(context);

        loadPrinterPath();
        Log.e("TAG", "Printers: "+usbDevices.toString());

        btn_testPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               for (int x=1;x<=4;x++){
//                   testPrint();
//               }

                testPrint("Cashier Invoice",usbDevices.get(sp_cashierInvoice.getSelectedItemPosition()).toString(),1);




            }
        });
        btn_testPrint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               for (int x=1;x<=4;x++){
//                   testPrint();
//               }


                testPrint("Kitchen Printer",usbDevices.get(sp_kitchenPrinter.getSelectedItemPosition()).toString(),2);


            }
        });
        btn_testPrint3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               for (int x=1;x<=4;x++){
//                   testPrint();
//               }


                testPrint("Order Summary Printer",usbDevices.get(sp_orderSummary.getSelectedItemPosition()).toString(),3);


            }
        });
        btn_testPrint4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               for (int x=1;x<=4;x++){
//                   testPrint();
//               }

                testPrint("Sticker label Printer",usbDevices.get(sp_stickerPrinter.getSelectedItemPosition()).toString(),4);


            }
        });
        btn_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                // Find your SurfaceView from your layout file
//                SurfaceView cameraView = findViewById(R.id.cameraView);
//
//                // Instantiate the cashier_invoice_scanner_class and start the camera
//                cashier_invoice_scanner_class  cameraScanner = new cashier_invoice_scanner_class(mcontext,cameraView);
//                cameraScanner.startBarcodeScanning();

            }
        });





    }



    private void print() {

        checkUSBDevice();

    }

    public void loadPrinters(Context context){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, usbDevices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cashierInvoice.setAdapter(adapter);
        sp_cashierInvoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));

                updatePrinterPath(usbDevices.get(sp_cashierInvoice.getSelectedItemPosition()),1);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_kitchenPrinter.setAdapter(adapter);
        sp_kitchenPrinter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));

                updatePrinterPath(usbDevices.get(sp_kitchenPrinter.getSelectedItemPosition()),2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_orderSummary.setAdapter(adapter);
        sp_orderSummary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));
                updatePrinterPath(usbDevices.get(sp_orderSummary.getSelectedItemPosition()),3);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_stickerPrinter.setAdapter(adapter);
        sp_stickerPrinter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));

                updatePrinterPath(usbDevices.get(sp_stickerPrinter.getSelectedItemPosition()),4);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    public void checkUSBDevice(){
         usbDevices= getUsbDevices(this.getApplicationContext());

        Log.d("TAG", "checkUSBDevice: "+usbDevices.toString());
        // Now you can use the list of USB devices as adb devices
        for (String deviceName : usbDevices) {
            Log.e("TAG", "usbPrinter: "+deviceName );

//            usbDevice.getProductName() + "\n"
//                    + "ProductID:" + usbDevice.getProductId() + "\n"
//                    + "VendoID:" + usbDevice.getVendorId() + "\n"

        }
    }

    public static ArrayList<String> getUsbDevices(Context context) {
        ArrayList<String> usbDeviceList = new ArrayList<>();
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        if (usbManager != null) {
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            usbDeviceList.add("OFF");
            usbDeviceList.add("SUNMI");
            Log.d("TAG", "getUsbDevices: "+usbManager.getDeviceList().toString());
            for (UsbDevice device : deviceList.values()) {
                // You can access information about the USB device here.
                // For example, you can use device.getDeviceName() to get the device's name.
                usbDeviceList.add(device.getDeviceName());
            }
        }
        return usbDeviceList;
    }

    public void testPrint(String printerLoc,String printerAddress ,int printerCode){



       if (printerAddress.equalsIgnoreCase("SUNMI")){
//            if (printLoc == 1) {
//
////                SunmiPrintHelper.getInstance().printText(data + "\n\n\n\n\n\n\n",24,true,false,null);
////                SunmiPrintHelper.getInstance().cutpaper();
//
//                //printer.printText(data + "\n\n\n\n\n\n\n", 0, 0, 0);
//            } else if (printLoc == 2) {
//                SunmiPrintHelper.getInstance().printText(data + "\n\n\n\n\n\n\n",24,true,false,null);
//                SunmiPrintHelper.getInstance().cutpaper();
//            } else if (printLoc == 3) {
//                SunmiPrintHelper.getInstance().printText(data + "\n\n\n\n\n\n\n",24,true,false,null);
//                SunmiPrintHelper.getInstance().cutpaper();
//            }
//            else if (printLoc==4){
//                printTSPL(data,context2,PrinterAddress);
//            }

           StringBuffer buffer = new StringBuffer();
           if (printerCode==1){
               buffer.append("----------------------------" + "\n");
               buffer.append("CASHIER INVOICE PRINTER" + "\n");
               buffer.append("----------------------------" + "\n");
               buffer.append(""+ "\n\n\n\n\n\n");
               SunmiPrintHelper.getInstance().printText(buffer.toString(),24,true,false,null);
                SunmiPrintHelper.getInstance().cutpaper();
           }
           else if (printerCode==2){
               buffer.append("----------------------------" + "\n");
               buffer.append("KITCHEN PRINTER TEST" + "\n");
               buffer.append("----------------------------" + "\n");
               buffer.append(""+ "\n\n\n\n\n\n");
               SunmiPrintHelper.getInstance().printText(buffer.toString(),24,true,false,null);
               SunmiPrintHelper.getInstance().cutpaper();
           }
           else if (printerCode==3){
               buffer.append("----------------------------" + "\n");
               buffer.append("ORDER SUMMARY TEST" + "\n");
               buffer.append("----------------------------" + "\n");
               buffer.append(""+ "\n\n\n\n\n\n");
               SunmiPrintHelper.getInstance().printText(buffer.toString(),24,true,false,null);
               SunmiPrintHelper.getInstance().cutpaper();
           }
           else if (printerCode==4){

               buffer.append("TEXT 10,10,\"2\",0,1,1,\"" + "line1" + "\"\n");
               buffer.append("TEXT 10,30,\"2\",0,1,1,\"" + "line2" + "\"\n");
               printTSPL(buffer.toString());

           }


        }
       else if (!printerAddress.equalsIgnoreCase("OFF") || !printerAddress.equalsIgnoreCase("SUNMI")){

           Log.d("Testprint", " Location : "+ printerLoc + " PrinterAdd : " + printerAddress);
           IDeviceConnection connect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_USB);
           connect.connect(printerAddress, (code, msg) -> {
               if (code == POSConnect.CONNECT_SUCCESS) {
                   Log.i("tag", "device connect success");
                   POSPrinter printer  = new POSPrinter(connect);
                   //printer.printString("test print text \n\n\n\n");
                   StringBuffer buffer = new StringBuffer();
//                buffer.append("LINE1 : " + "\n");
//                buffer.append("LINE2 : "+ "\n");
//                buffer.append("LINE3 : "+ "\n");
//                buffer.append("LINE4 : "+ "\n");


                   if (printerCode==1){
                       buffer.append("----------------------------" + "\n");
                       buffer.append("CASHIER INVOICE PRINTER" + "\n");
                       buffer.append("----------------------------" + "\n");
                       buffer.append(""+ "\n\n\n\n\n\n");
                       printer.printText(buffer.toString(), 1,0,0);
                       printer.cutPaper();
                   }
                   else if (printerCode==2){
                       buffer.append("----------------------------" + "\n");
                       buffer.append("KITCHEN PRINTER TEST" + "\n");
                       buffer.append("----------------------------" + "\n");
                       buffer.append(""+ "\n\n\n\n\n\n");
                       printer.printText(buffer.toString(), 1,0,0);
                       printer.cutPaper();
                   }
                   else if (printerCode==3){
                       buffer.append("----------------------------" + "\n");
                       buffer.append("ORDER SUMMARY TEST" + "\n");
                       buffer.append("----------------------------" + "\n");
                       buffer.append(""+ "\n\n\n\n\n\n");
                       printer.printText(buffer.toString(), 1,0,0);
                       printer.cutPaper();
                   }
                   else if (printerCode==4){

                       buffer.append("TEXT 10,10,\"2\",0,1,1,\"" + "line1" + "\"\n");
                       buffer.append("TEXT 10,30,\"2\",0,1,1,\"" + "line2" + "\"\n");
                       printTSPL(buffer.toString());

                   }




                   // printer.printBarCode("")

                   Log.d("TAG", "testPrint: printerCode "+printerCode);





               }



               else if (code == POSConnect.CONNECT_FAIL) {
                   Log.i("tag", "device connect fail");
               }
           });
           //
//
//

//        connect.close();
           // static List<String> getUsbDevices(Conte);



       }





    }
    RTPrinter rtPrinter;




    public void updatePrinterPath(String printerAddress,int printID){
        settingsDB settings = new settingsDB(getApplicationContext());

        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor PrinterActivate = db.rawQuery("select PrintID,PrinterAddress from PrinterSettings where PrintID='"+printID+"'", null);
        if (PrinterActivate.getCount()!=0){


            if (PrinterActivate.moveToFirst()){
//                if (PrinterActivate.getInt(0)==1){
                    //String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
                    String strsql = "UPDATE PrinterSettings set PrinterAddress='"+printerAddress+"' where PrintID='"+printID+"'";
                    db.execSQL(strsql);



            }
        }
        db.close();


    }

    public void loadPrinterPath(){


       // settingsDB settings = new settingsDB(getApplicationContext());

        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor PrinterActivate = db.rawQuery("select PrintID,PrinterAddress from PrinterSettings", null);
        if (PrinterActivate.getCount()!=0){


            while (PrinterActivate.moveToNext()){

                Log.e("TAG", "loadPrinterPath: " + usbDevices.indexOf(PrinterActivate.getString(1)));
                if (PrinterActivate.getInt(0)==1){
                    sp_cashierInvoice.setSelection(usbDevices.indexOf(PrinterActivate.getString(1)));
                }
               if (PrinterActivate.getInt(0)==2){
                    sp_kitchenPrinter.setSelection(usbDevices.indexOf(PrinterActivate.getString(1)));
                }
              if (PrinterActivate.getInt(0)==3){
                    sp_orderSummary.setSelection(usbDevices.indexOf(PrinterActivate.getString(1)));
                }
           if (PrinterActivate.getInt(0)==4){
                    sp_stickerPrinter.setSelection(usbDevices.indexOf(PrinterActivate.getString(1)));
                }
            }

        }
        db.close();

    }
    private static final String USB_PERMISSION_ACTION = "com.abztrakinc.ABZPOS.USB_PERMISSION";

    private void printTSPL(String msg) {


        try {
            String usbDeviceId = usbDevices.get(sp_stickerPrinter.getSelectedItemPosition()).toString();
            Context context = this.getApplicationContext();
            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            UsbDevice usbDevice = getUsbDevice(context, usbManager, usbDeviceId);

            if (usbDevice != null) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(USB_PERMISSION_ACTION), 0);

                if (usbManager.hasPermission(usbDevice)) {
                    UsbConfigBean usbConfig = new UsbConfigBean(context, usbDevice, permissionIntent);
                    UsbPrintDriver usbPrintDriver = new UsbPrintDriver(usbConfig);
                    usbPrintDriver.connect(usbDevice, context, permissionIntent);

                    try {
                        if (usbPrintDriver.getConnectState() == ConnectStateEnum.Connected) {
//                            String tsplCommand = "CLS\n" +                            // Clear label segment
//                                    "SIZE 40 mm, 46 mm\n" +              // Set label size
//                                    "GAP 2 mm\n" +                        // Specify the label gap
//                                    "CODEPAGE UTF-8\n" +                  // Specify the code page (e.g., UTF-8)
//                                    "TEXT 10,10,\"2\",0,1,1,\""+msg2+"\"\n" + // Print text using Font 3
//
//                                   // "BARCODE 10,50,\"128\",50,1,0,2,2,\"123456\"\n" + // Print Code 128 barcode
////                                    "BITMAP 10,10,\"/storage/emulated/0/ANDROID_POS/snail.bmp\"\n" +
//                                    "PRINT 1\nCUT\n";                     // Print one copy and cut
                            String msg2 = "Trans# : 00000000000 ";
                            String tsplCommand = "CLS\n" +                            // Clear label segment
                                    "SIZE 40 mm, 46 mm\n" +              // Set label size
                                    "GAP 2 mm\n" +                        // Specify the label gap
                                    "CODEPAGE UTF-8\n" +                  // Specify the code page (e.g., UTF-8)
                                    msg +
                                    "PRINT 1\nCUT\n";



                            // Convert TSPL commands to bytes using ASCII encoding
                            byte[] printCommand = tsplCommand.getBytes("ASCII");

                            // Send printing commands to the printer asynchronously
                            usbPrintDriver.writeMsgAsync(printCommand);

                            // Display success message
                            Log.d("TAG", "printTSPL: " + tsplCommand);
                            Toast.makeText(context, "Printing successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // Display error message if printer is not connected
                            Toast.makeText(context, "Printer not connected", Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        // Handle encoding errors
                        e.printStackTrace();
                        Toast.makeText(context, "Encoding error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    usbManager.requestPermission(usbDevice, permissionIntent);
                }


            } else {
                Log.e("testlabelPrint", "NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PendingIntent getUsbPermissionIntent(Context context) {
        // Use a unique action for your PendingIntent
        Intent intent = new Intent("com.abztrakinc.ABZPOS.USB_PERMISSION");

        // Replace "com.your.package" with your actual package name

        // Create a PendingIntent with the intent
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
    private static UsbDevice getUsbDevice(Context context,UsbManager usbManager, String usbDeviceId) {
        // Get a list of connected USB devices
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();

        // Iterate through the list to find the USB device with the specified ID
        for (UsbDevice device : usbDeviceList.values()) {
            if (usbDeviceId.equals(device.getDeviceName())) {
             //   Toast.makeText(context, device.toString(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "getUsbDevice: "+device.toString());
                Log.e("TAG", "getUsbDevice: "+usbDeviceId );
                Log.e("TAG", "getDeviceID: "+device.getDeviceId() );
                Log.e("TAG", "getDeviceName: "+device.getDeviceName() );
                return device; // Found the matching USB device
            }
            else{
                Log.e("TAG", "getUsbDevice: "+usbDeviceId );
                Log.e("TAG", "getDeviceID: "+device.getDeviceId() );
                Log.e("TAG", "getDeviceName: "+device.getDeviceName() );

            }
        }

        return null; // USB device with the specified ID not found
    }

    private void convertPNG(){
        // Read the PNG image from resources
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);

        // Create the directory if it doesn't exist
        File directory = new File(Environment.getExternalStorageDirectory(), "ANDROID_POS");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the BMP file to the directory
        File outputFile = new File(directory, "output.bmp");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
            originalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();

            Log.d("TAG","Conversion completed successfully. File saved at: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("TAG","Error occurred: " + e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





}