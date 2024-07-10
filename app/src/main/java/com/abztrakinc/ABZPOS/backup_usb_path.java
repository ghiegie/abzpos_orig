package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class backup_usb_path {

    public String getUsb_name() {
       usb_name = "/4493-19E8"; // set this if you change the sdcard
     //   usb_name = "/8519-19ED";
        return usb_name;
    }

    public void setUsb_name(String usb_name) {
        this.usb_name = usb_name;
    }

    private  String usb_name;


//
//    public String getUsbName(Context context) {
//        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//        UsbDevice usbDevice = getConnectedUSBDevice(usbManager);
//        if (usbDevice != null) {
//            return usbDevice.getDeviceName();
//        } else {
//            return null; // No USB device connected
//        }
//    }
//
//    private UsbDevice getConnectedUSBDevice(UsbManager usbManager) {
//        for (UsbDevice usbDevice : usbManager.getDeviceList().values()) {
//            if (usbDevice != null) {
//                // Add your conditions to check if this is the USB device you want
//                return usbDevice;
//            }
//        }
//        return null; // No USB device connected
//    }




}
