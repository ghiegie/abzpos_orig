package com.abztrakinc.ABZPOS.TCSKeyboard;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.zqcom.zqcom;
import com.zqprintersdk.PrinterConst;

import java.io.File;
import java.nio.charset.Charset;

@SuppressLint("DefaultLocale")
public class KeyboardDevice {
    private final static String TAG = "KeyboardDevice";
    private static zqcom mSerialPort = null;
    public static Handler mHandler = null;
    public static boolean bExit = false;
    public static RecvThread thdRecv = null;
    ////////////////////
    public static final int MSG_KEY = 10;
    private static String KEY_PORT = "/dev/ttyS5";
    public static final int KEY_BAUD_RATE = 115200;
    public static final int m_nFlow = PrinterConst.FlowControl.NONE;


    public KeyboardDevice() {
    }

    public static void Init() {
        try {
            bExit = false;
            Log.e(TAG, "KeyBoard Device:" + KEY_PORT);
            File fileDev = new File(KEY_PORT);
            if (fileDev.exists()) {
                mSerialPort = new zqcom(new File(KEY_PORT), KEY_BAUD_RATE,
                        m_nFlow);
                String devParam = String.format("%s BaudRate:%d", KEY_PORT, KEY_BAUD_RATE);
                PostMessage(MSG_KEY, "0:KeyboardDevice" + devParam);
                thdRecv = new RecvThread();
                thdRecv.start();
            } else {
                PostMessage(MSG_KEY, "0:" + KEY_PORT + " Not Exist or support");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            PostMessage(MSG_KEY, "0:KeyboardDevice Open Exception " + ex.getMessage());
        }
    }

    public static void UnInit() {
        try {
            bExit = true;
            mSerialPort.Close();
            Log.e("UNINIT","EXIT");
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("UNINIT",ex.getMessage());
        }
    }

    public static void BeepOnOff(boolean bOn) {
        String strSend = "BE:ON\r\n";
        if (bOn) {
        } else {
            strSend = "BE:OFF\r\n";
        }
        if (mSerialPort != null) {
            byte[] bySend = strSend.getBytes(Charset.forName("gbk"));
            mSerialPort.write(bySend);
        }
    }

    private static void PostMessage(int nMsg, String strMessage) {
        Log.i(TAG, strMessage);
        if (mHandler != null) {
            try {
                Message msg = mHandler.obtainMessage();
                if (msg == null) {
                    Log.i(TAG, "Message Handler No Init.  " + strMessage);
                } else {
                    msg.what = nMsg;
                    Bundle bd = new Bundle();
                    bd.putString("msg_info", strMessage);
                    msg.setData(bd);
                    msg.sendToTarget();
                }
            } catch (Exception e) {
                Log.i(TAG, "Message Send Exception");
                e.printStackTrace();
            }
        }
    }


    public static class RecvThread extends Thread {
        byte[] byRecv;

        public RecvThread() {
            byRecv = new byte[1024];
        }

        public int bytes2short(byte byHigh, byte byLow) {
            int high = byHigh;
            int low = byLow;
            return (high << 8 & 0xFF00) | (low & 0xFF);
        }

        @Override
        public void run() {
            for (; ; ) {
                if (bExit) {
                    break;
                } else {
                    try {
                        int nAvailable = mSerialPort.getInputStream()
                                .available();
                        if (nAvailable > 0) {
                            int nRead = mSerialPort.read(byRecv, 2);
                            if (nRead > 0) {
                                int nKeyIndex = 0;
                                if (nRead >= 2) {
                                    nKeyIndex = bytes2short(byRecv[0], byRecv[1]);
                                }
                                String strHex = ByteArrToHex(byRecv, 0, nRead);
                                String strSend = String.format("%d:", nKeyIndex) + strHex;
                                PostMessage(MSG_KEY, strSend);
                            }
                            Thread.sleep(20);
                        } else {
                            Thread.sleep(20);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    public static String ByteArrToHex(byte[] byData, int nStart, int nLen) {
        StringBuilder sbHex = new StringBuilder(1024);
        for (int i = nStart; i < nLen; i++) {
            String strItem = String.format("0x%x ", byData[i]);
            sbHex.append(strItem);
        }
        return sbHex.toString();
    }
}
