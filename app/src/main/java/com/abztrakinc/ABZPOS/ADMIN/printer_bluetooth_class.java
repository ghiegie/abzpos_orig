package com.abztrakinc.ABZPOS.ADMIN;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class printer_bluetooth_class {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    //Button btn_buttonOpen,btn_send,btn_close;
    volatile boolean stopWorker;
    android.content.Context contextfinal;


    public printer_bluetooth_class(android.content.Context context){
    contextfinal=context;
    }


    public void findBT() {

        Toast.makeText(contextfinal, "TEST", Toast.LENGTH_SHORT).show();
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.e("findBT", "findBT");
            if (mBluetoothAdapter == null) {
                //myLabel.setText("No bluetooth adapter available");
                Log.e("PRINTER TEXT", "NO bluetooth printer");
            }
            Log.e("findBT", "findBT2");
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(contextfinal, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
               //contextfinal.startact;
                contextfinal.startActivity(enableBluetooth);
            //startActivityForResult(enableBluetooth, 0);
                Log.e("findBT", "findBT3");
            }
            Log.e("findBT", "findBT4");

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            List<String> s = new ArrayList<String>();
            if (pairedDevices.size() > 0) {
                Log.e("findBT", "findBT5");
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().trim().equals("printer001")) {
                        Log.e("PRINTER TEXT", "Bluetooth device found");
                        mmDevice = device;
                        break;
                    }
                       s.add(device.getName());


                }

            }
            Log.e("findBT", "findBT6");
            for (int x = 0; x < s.size(); x++) {
                Log.e("Bluetooth", s.get(x));
//                if (device.getName().equals("printer001")) {
////                        Log.e("PRINTER TEXT", "Bluetooth device found");
////                        mmDevice = device;
////                        break;
////                    }
//                if (s.get(x).trim().toString().equals("printer001")){
//                    mmDevice=s
//                }

            }

            // myLabel.setText("Bluetooth device found.");
            //Log.e("PRINTER TEXT", "Bluetooth device found");

        } catch (Exception e) {
            Toast.makeText(contextfinal, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("findBT",e.getMessage());
            e.printStackTrace();
        }
    }



    // tries to open a connection to the bluetooth printer device
    @SuppressLint("MissingPermission")
    public void openBT() throws IOException {
        try {

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            Toast.makeText(contextfinal, "Bluetooth opended", Toast.LENGTH_SHORT).show();
           // myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
           // Toast.makeText(contextfinal, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("OpenBT",e.getMessage());
        }
    }

    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
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
                                              //  myLabel.setText(data);
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
        }
    }


    // this will send text data to be printed by the bluetooth printer
    void sendData(String data) throws IOException {
        try {

            // the text typed by the user
            String msg = "-------------------------------- \n test ---------------------------";
            msg += "\n";

           // mmOutputStream.write(data.getBytes());

            mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            String str = new String(data.getBytes(StandardCharsets.UTF_8));
            Log.d("FinalPrint",str);
            Thread.sleep(5000);    // add this line

            // tell the user data were sent
          //  myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("sendData",e.getMessage());
        }
    }
    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
           // myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("closeBT",e.getMessage());
        }
    }


}
