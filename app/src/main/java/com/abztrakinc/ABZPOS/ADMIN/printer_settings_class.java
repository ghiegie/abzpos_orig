package com.abztrakinc.ABZPOS.ADMIN;

import static com.abztrakinc.ABZPOS.POJO.FunctionCall.getinstance;
import static com.abztrakinc.ABZPOS.POJO.FunctionCall.unLockCashBox;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.print.PrintManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.utils.SunmiPrintHelper;
import com.jolimark.JmPrinter;
import com.rt.printerlibrary.bean.UsbConfigBean;
import com.rt.printerlibrary.driver.usb.UsbPrintDriver;
import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import net.posprinter.IDeviceConnection;
import net.posprinter.POSConnect;
import net.posprinter.POSPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import a1088sdk.a;
import a1088sdk.PrnDspA1088;

import a1088sdk.PrnDspA1088Activity;
import kotlinx.coroutines.channels.ActorKt;




public class printer_settings_class extends PrnDspA1088{
    private String a = "A1088SDK";
    android.content.Context context2;
    PrnDspA1088 prn;


    public printer_settings_class(android.content.Context context) {
        super(context);
      //  this.context=context;
        prn = new PrnDspA1088(context);
        context2 = context;



    }


    int PrinterUnit=2; //1 == sunmi 2==tcs 3==bluetooth

    //Context context;
   ;
    ArrayList<Integer> printerIDList = new ArrayList<>();
    ArrayList<String> printerPathList = new ArrayList<>();
    private void loadPrinters(){

        printerIDList.clear();
        printerPathList.clear();

        SQLiteDatabase db = context2.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor PrinterActivate = db.rawQuery("select PrintID,PrinterAddress from PrinterSettings", null);
        if (PrinterActivate.getCount()!=0){
            while (PrinterActivate.moveToNext()){
                printerIDList.add(PrinterActivate.getInt(0));
                printerPathList.add(PrinterActivate.getString(1));
            }
        }

        db.close();

    }


    public void OnlinePrinter(String var1, int qty,int headerQty,int printLoc){
        loadPrinters();

        Log.e("TAG2", "OnlinePrinter: "+PrinterUnit );



//        SQLiteDatabase db = this.context2.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//        Cursor PrinterActivate = db.rawQuery("select PrintID from PrinterSettings", null);
//        if (PrinterActivate.getCount()!=0){
//            if (PrinterActivate.moveToFirst()){
//                if (PrinterActivate.getInt(0)==1){
                              if (PrinterUnit==1){
                                SunmiPrintHelper.getInstance().printText(var1, 24,true,false,null);
                                SunmiPrintHelper.getInstance().cutpaper();
                            }
                            else if(PrinterUnit==2){

                                if (qty==2){
                                    for (int x=0;x<qty;x++) {

                                        String addVar;
                                        Log.d("ENTRY 123",String.valueOf(x));



                                        if (x==0) {

                                            String line;
                                            String text = "";
                                            int ctrH=0;


        //

                                            BufferedReader bufReader = new BufferedReader(new StringReader(var1));

                                            String line2=null;
        //                        while( (line2=bufReader.readLine()) != null )
        //                        {
        //
        //                        }


                                            StringBuffer buffer = new StringBuffer();
                                            buffer.append("--------------------------------" + "\n");
                                            buffer.append("    S  T  O  R  E   C  O  P  Y" + "\n");
                                            buffer.append("--------------------------------");
                                            Log.e("VARIABLECOUNT",String.valueOf(var1.length()));

                                            try {
                                                while ((line = bufReader.readLine()) != null) {
                                                    ctrH++;
                                                    //process the line
                                                    if (ctrH == headerQty) {
                                                        text = text + buffer.toString();
                                                    }
                                                    System.out.println(line);
                                                    //  tv_journalText.setText(line);
                                                    text = text + line + "\n";
                                                }
                                            }
                                            catch (Exception ex){
                                                Log.e("TAG", "OnlinePrinter: "+ex.getMessage().toString() );
                                            }





                                            addVar=text;

                                            //   addVar = buffer.toString() + var1;
                                           // prn.PRN_PrintText(addVar + "\n \n \n \n \n \n", 0, 0, 0);
                                            Print(addVar,printLoc);

                                        }
                                        else{

                                            String line;
                                            String text = "";
                                            int ctrH=0;


        //

                                            BufferedReader bufReader = new BufferedReader(new StringReader(var1));

                                            String line2=null;
        //                        while( (line2=bufReader.readLine()) != null )
        //                        {
        //
        //                        }


                                            StringBuffer buffer = new StringBuffer();
                                            buffer.append("--------------------------------" + "\n");
                                            buffer.append("    C U S T O M E R  C O P Y" + "\n");
                                            buffer.append("--------------------------------");
                                            Log.e("VARIABLECOUNT",String.valueOf(var1.length()));

                                            try {
                                                while ((line = bufReader.readLine()) != null) {
                                                    ctrH++;
                                                    //process the line
                                                    if (ctrH == headerQty) {
                                                        text = text + buffer.toString();
                                                    }
                                                    System.out.println(line);
                                                    //  tv_journalText.setText(line);
                                                    text = text + line + "\n";
                                                }
                                            }
                                            catch (Exception ex){
                                                Log.e("TAG", "OnlinePrinter: "+ex.getMessage().toString() );
                                            }





                                            addVar=text;

                                            //   addVar = buffer.toString() + var1;
                                           // prn.PRN_PrintText(addVar + "\n \n \n \n \n \n", 0, 0, 0);
                                            Print(addVar,printLoc);
                                        }






                                    }
                                }

                                else{
                                    if (qty==0){
                                        qty=1;
                                    }
                                    for (int x=0;x<qty;x++) {


                                      //

                                        if(printLoc==4){
                                          //  prn.PRN_PrintText(var1 + "\n \n \n \n \n \n",0,0,0);
                                            Print(var1,printLoc);
                                        }
                                        else{
                                            Print(var1,printLoc);
                                        }

                                        //LAnprinter(var1);
                                       // connect.close();

                                    }
                                }
                            }
                            else if(PrinterUnit==3){

                                printer_bluetooth_class printer_bluetooth_class = new printer_bluetooth_class(context2);

                                if (qty==2){
                                    for (int x=0;x<qty;x++) {

                                        String addVar;
                                        Log.d("ENTRY 123",String.valueOf(x));



                                        if (x==0) {

                                            String line;
                                            String text = "";
                                            int ctrH=0;


        //

                                            BufferedReader bufReader = new BufferedReader(new StringReader(var1));

                                            String line2=null;
        //                        while( (line2=bufReader.readLine()) != null )
        //                        {
        //
        //                        }


                                            StringBuffer buffer = new StringBuffer();
                                            buffer.append("--------------------------------" + "\n");
                                            buffer.append("    S  T  O  R  E   C  O  P  Y" + "\n");
                                            buffer.append("--------------------------------");
                                            Log.e("VARIABLECOUNT",String.valueOf(var1.length()));

                                            try {
                                                while ((line = bufReader.readLine()) != null) {
                                                    ctrH++;
                                                    //process the line
                                                    if (ctrH == headerQty) {
                                                        text = text + buffer.toString();
                                                    }
                                                    System.out.println(line);
                                                    //  tv_journalText.setText(line);
                                                    text = text + line + "\n";
                                                }
                                            }
                                            catch (Exception ex){
                                                Log.e("TAG", "OnlinePrinter: "+ex.getMessage().toString() );
                                            }





                                            addVar=text;

                                            //   addVar = buffer.toString() + var1;
                                            //  prn.PRN_PrintText(addVar + "\n \n \n \n \n \n", 0, 0, 0);
                                            try {
                                                printer_bluetooth_class.findBT();
                                                printer_bluetooth_class.openBT();
                                                printer_bluetooth_class.sendData(addVar + "\n \n \n \n \n \n");
                                                printer_bluetooth_class.closeBT();
                                            }
                                            catch (Exception ex){

                                            }


                                        }
                                        else{

                                            String line;
                                            String text = "";
                                            int ctrH=0;


        //

                                            BufferedReader bufReader = new BufferedReader(new StringReader(var1));

                                            String line2=null;
        //                        while( (line2=bufReader.readLine()) != null )
        //                        {
        //
        //                        }


                                            StringBuffer buffer = new StringBuffer();
                                            buffer.append("--------------------------------" + "\n");
                                            buffer.append("    C U S T O M E R  C O P Y" + "\n");
                                            buffer.append("--------------------------------");
                                            Log.e("VARIABLECOUNT",String.valueOf(var1.length()));

                                            try {
                                                while ((line = bufReader.readLine()) != null) {
                                                    ctrH++;
                                                    //process the line
                                                    if (ctrH == headerQty) {
                                                        text = text + buffer.toString();
                                                    }
                                                    System.out.println(line);
                                                    //  tv_journalText.setText(line);
                                                    text = text + line + "\n";
                                                }
                                            }
                                            catch (Exception ex){
                                                Log.e("TAG", "OnlinePrinter: "+ex.getMessage().toString() );
                                            }





                                            addVar=text;

                                            //   addVar = buffer.toString() + var1;
                                            //   prn.PRN_PrintText(addVar + "\n \n \n \n \n \n", 0, 0, 0);

                                            try {
                                                printer_bluetooth_class.findBT();
                                                printer_bluetooth_class.openBT();
                                                printer_bluetooth_class.sendData(addVar + "\n \n \n \n \n \n");
                                                printer_bluetooth_class.closeBT();
                                            }
                                            catch (Exception ex){

                                            }
                                        }






                                    }
                                }

                                else{
                                    if (qty==0){
                                        qty=1;
                                    }
                                    for (int x=0;x<qty;x++) {
                                        //  prn.PRN_PrintText(var1 + "\n \n \n \n \n \n", 0, 0, 0);
                                        try {
                                            printer_bluetooth_class.findBT();
                                            printer_bluetooth_class.openBT();
                                            printer_bluetooth_class.sendData(var1 + "\n \n \n \n \n \n");
                                            printer_bluetooth_class.closeBT();
                                        }
                                        catch (Exception ex){

                                        }
                                    }
                                }
                            }




    //printLoc 1=cashier 2=kitchen 3=order summary 4=stickerlabel



    }






    private void Print(String data,int printLoc){







            POSConnect.init(context2);
            Log.e("OnlinePrinterData", data);


            try {

                IDeviceConnection connect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_USB);
                String PrinterAddress = printerPathList.get(printerIDList.indexOf(printLoc));

                Log.e("TAG", "Print: "+PrinterAddress.toString() );


                if (PrinterAddress.equalsIgnoreCase("SUNMI")){
                    if (printLoc == 1) {

                        SunmiPrintHelper.getInstance().printText(data + "\n\n\n\n\n\n\n",24,true,false,null);
                        SunmiPrintHelper.getInstance().cutpaper();

                        //printer.printText(data + "\n\n\n\n\n\n\n", 0, 0, 0);
                    } else if (printLoc == 2) {
                        SunmiPrintHelper.getInstance().printText(data + "\n\n\n\n\n\n\n",24,true,false,null);
                        SunmiPrintHelper.getInstance().cutpaper();
                    } else if (printLoc == 3) {
                        SunmiPrintHelper.getInstance().printText(data + "\n\n\n\n\n\n\n",24,true,false,null);
                        SunmiPrintHelper.getInstance().cutpaper();
                    }
                    else if (printLoc==4){
                        printTSPL(data,context2,PrinterAddress);
                    }
                }
                else if (!PrinterAddress.equalsIgnoreCase("OFF")) {

                    Log.e("TAG", "Print: 1" );

                    connect.connect(PrinterAddress, (code, msg) -> {
                        if (code == POSConnect.CONNECT_SUCCESS) {
                            Log.i("tag", "device connect success : ");
                            POSPrinter printer = new POSPrinter(connect);
                            if (printLoc == 1) {
                                printer.printText(data + "\n\n\n\n\n\n\n", 0, 0, 0);
                            } else if (printLoc == 2) {
                                printer.printText(data + "\n\n\n\n", 0, 0, 0);
                            } else if (printLoc == 3) {
                                printer.printText(data + "\n\n\n\n", 0, 0, 0);
                            }
                            else if (printLoc==4){
                                printTSPL(data,context2,PrinterAddress);
                            }
                            printer.cutPaper();

                        } else if (code == POSConnect.CONNECT_FAIL) {
                            Log.i("tag", "device connect fail");
                        }
                    });


                }

            } catch (Exception ex) {
                Toast.makeText(context2, "NO CONNECTED PRINTER", Toast.LENGTH_SHORT).show();
            }


    }



    public int TCSPrint(String var1){

        return PRN_PrintText(var1,0,0,0);
    }


    public void LAnprinter(String data){
        Socket socket = new Socket();
        String printerAddress = "IP_ADDRESS_OR_HOSTNAME"; // Replace with the actual IP address or hostname of the network printer
        int port = 9100; // This is a common port used for printing on network printers

        try {
            socket.connect(new InetSocketAddress(printerAddress, port), 5000); // Connect to the printer
            if (socket.isConnected()) {
                Log.i("tag", "Device connected successfully");

                // Now, you can send your print commands to the printer using the socket.
                OutputStream outputStream = socket.getOutputStream();

                // Send your print data to the printer
                String printData = data.toString().trim() + "\n\n\n\n";
                outputStream.write(printData.getBytes());

                // Cut paper or perform other operations as needed

                // Close the socket when you're done
                socket.close();
            } else {
                Log.i("tag", "Device connection failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("tag", "An error occurred while connecting to the device");
        } finally {
            try {
                socket.close(); // Make sure to close the socket even if there's an error
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void OpenCashBox(){

            //put this code inside button for opening drawer

        //        Intent intent = new Intent("android.intent.action.CASHBOX");
        //        intent.putExtra("cashbox_open", true);
        //        sendBroadcast(intent);






    }
    public void UserDisplayTotalAmount(String amount){
      //  int ActivateCode=0;

        if(PrinterUnit==2){
            Log.e("UDisplay","testDisplay");
            try {
                prn.DSP_Open("/dev/ttyS4", 9600, 5); // /dev/ttyS4  //// for built in display
            }
            catch (Exception ex){
                Log.e("ERROR DISPLAY",ex.getMessage());
            }
            // String test="2.00";
            prn.DSP_Clear();

//            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//            formatter.applyPattern("###,###,###.###");
//            Double amtDouble = Double.parseDouble(amount);
//            String finalAmount = String.valueOf(formatter.format(amtDouble));

            prn.DSP_DisplayString("Total : P "+ amount + "\r\n");
            //prn.DSP_LED_SetStatus(49); //49 = price , 50 = total amount , 51 = paid , 52= change
            //prn.PRN_PrintText("test"+"\n \n \n",1,1,1);
            //
            //prn.DSP_Close();
        }
        else{

        }




    }

    public void PaperFeed(){
        prn.PRN_LineFeed(3);
    }



    public void Cashbox(){

//        String s = "asddddddddddddddddddddddddddddddddd \n \n \n \n ";
//        prn.COM_Open("/dev/ttyUSB1",9600,0);
//        prn.COM_SendData(s.getBytes(),1);
//        prn.PRN_Close();
//        prn.PRN_Status();
        prn.PRN_Open("/dev/ttyS1",9600,1);

    }

//region Sticker Printer
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
    private static final String USB_PERMISSION_ACTION = "com.abztrakinc.ABZPOS.USB_PERMISSION";
    private void printTSPL(String msg ,Context context,String PrinterAddress) {
        try {
            String usbDeviceId = PrinterAddress;

            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            UsbDevice usbDevice = getUsbDevice(context, usbManager, usbDeviceId);

            if (usbDevice != null) {
                PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(USB_PERMISSION_ACTION), 0);

                if (usbManager.hasPermission(usbDevice)) {
                    UsbConfigBean usbConfig = new UsbConfigBean(context, usbDevice, permissionIntent);
                    UsbPrintDriver usbPrintDriver = new UsbPrintDriver(usbConfig);
                    usbPrintDriver.connect(usbDevice, context, permissionIntent);

                    try {
                        if (usbPrintDriver.getConnectState() == ConnectStateEnum.Connected) {

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
//endregion


















}
