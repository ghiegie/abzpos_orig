package com.abztrakinc.ABZPOS.USBPrinter;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class USBPrinter {
    private UsbManager mUsbManager;
    public UsbDevice mDevice;
    private PendingIntent mPermissionIntent;
    UsbDeviceConnection connection;
    String TAG = "USB";

    public USBPrinter(){

    }

    public void createConn(Context context) {





         mUsbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        final String ACTION_USB_PERMISSION = "com.abztrakinc.ABZPOS.ADMIN.admin_pos_settings.USB_PERMISSION";
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);
        Log.e(TAG,"SAMPLE TEST1");




    }

    private static final String ACTION_USB_PERMISSION =
            //"com.pradeep.usbprinter.USB_PERMISSION";
    "com.abztrakinc.ABZPOS.ADMIN.admin_pos_settings.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG,"SAMPLE TEST2");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    mDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(mDevice != null){
                            //call method to set up device communication
                            Log.d(TAG,"permission accepted for device" + mDevice);
                        }
                    }
                    else {
                        Log.e(TAG, "permission denied for device " + mDevice);
                    }
                }
            }
            else{
                Log.e(TAG, "permission denied for device " + mDevice);
            }
        }
    };

    @SuppressLint("NewApi")
    public void printMessage(Context context,String msg) {
        // TODO Auto-generated method stub
        final String printdata = msg;
        final UsbEndpoint mEndpointBulkOut;
//        try {
            if (mUsbManager.hasPermission(mDevice)) {
                UsbInterface intf = mDevice.getInterface(0);
                for (int i = 0; i < intf.getEndpointCount(); i++) {
                    UsbEndpoint ep = intf.getEndpoint(i);
                    if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                        if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                            mEndpointBulkOut = ep;
                            connection = mUsbManager.openDevice(mDevice);
                            if (connection != null) {
                                Log.e("Connection:", " connected");
                                Toast.makeText(context, "Device connected", Toast.LENGTH_SHORT).show();
                            }
                            boolean forceClaim = true;
                            connection.claimInterface(intf, forceClaim);
                            Integer res = connection.bulkTransfer(mEndpointBulkOut, printdata.getBytes(), printdata.getBytes().length, 10000);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    Log.i("Thread:", "in run thread");
                                    byte[] bytes = printdata.getBytes();
                                    int b = connection.bulkTransfer(mEndpointBulkOut, bytes, bytes.length, 100000);
                                    Log.i("Return Status", "b-->" + b);
                                }
                            }).start();

                            connection.releaseInterface(intf);
                            break;
                        }
                    }
                }
            } else {
                mUsbManager.requestPermission(mDevice, mPermissionIntent);
                Toast.makeText(context, "Device have no permission", Toast.LENGTH_SHORT).show();
            }
//        }
//        catch (Exception ex){
//            Log.e(TAG,ex.getMessage());
//        }
    }

    @SuppressLint("NewApi")
    public void closeConnection(Context context){
        BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        Toast.makeText(context, "Device closed", Toast.LENGTH_SHORT).show();
                        connection.close();
                    }
                }
            }
        };
    }


}
