package com.abztrakinc.ABZPOS;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class printSend extends Activity{
   // private Context context;
    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel;


    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;



//    public void openBluetooth(){
////        try {
//            findBT();
////            openBT();
////
////        } catch (IOException ex) {
////            ex.printStackTrace();
////        }
//    }



    public void sendDatatoPrinter(){
//        try {
//            //sendData();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    @SuppressLint("MissingPermission")
   public void findBT(Context context2) throws IOException {
        //Toast.makeText(context2, "findbt", Toast.LENGTH_SHORT).show();
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
                Toast.makeText(context2, "No bluetooth adapter available", Toast.LENGTH_SHORT).show();

            }

            if (mBluetoothAdapter != null) {
//                myLabel.setText("No bluetooth adapter available");
                Toast.makeText(context2, "bluetooth adapter available", Toast.LENGTH_SHORT).show();

            }

          //  Toast.makeText(context2, "check", Toast.LENGTH_SHORT).show();

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
                Toast.makeText(context2, "adapter enabled", Toast.LENGTH_SHORT).show();

              //  context2.startActivity(enableBluetooth);
             // finishActivity(0);

            }




      Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    Toast.makeText(context2, device.getName(), Toast.LENGTH_SHORT).show();


                    if (device.getName().trim().equals("MP-38")) {
                        Toast.makeText(context2, "paired", Toast.LENGTH_SHORT).show();
                        mmDevice = device;
                        break;
                    }
                }
            }

//            myLabel.setText("Bluetooth device found.");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context2, "error", Toast.LENGTH_SHORT).show();
            closeBT();
        }

    }


    @SuppressLint("MissingPermission")
    public void openBT(Context context2) throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData(context2);

//            myLabel.setText("Bluetooth Opened");
            Toast.makeText(context2, "Bluetooth Opened", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context2, "error bt", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            closeBT();
        }
    }

    public void beginListenForData(Context context2) throws IOException {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                               // myLabel.setText(data);
                                                Toast.makeText(context2, data, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
                Toast.makeText(context2, "error", Toast.LENGTH_SHORT).show();
                closeBT();
        }
    }


    public void sendData() throws IOException {


        // the text typed by the user
//        String msg = "test";
//        msg += "\n";
//
//        mmOutputStream.write(msg.getBytes());
//        mmOutputStream.flush();
//        mmOutputStream.close();
        String text="asda";
        String msg = text;
        msg += "\n";

        mmOutputStream.write(msg.getBytes());



    }


    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
          //  myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

