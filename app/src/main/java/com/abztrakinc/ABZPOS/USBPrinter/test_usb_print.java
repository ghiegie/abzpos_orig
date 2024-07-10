package com.abztrakinc.ABZPOS.USBPrinter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.BuildConfig;
import com.abztrakinc.ABZPOS.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import a1088sdk.PrnDspA1088;
import a1088sdk.PrnDspA1088Activity;


public class test_usb_print extends PrnDspA1088Activity {


    private Button butBack;
    private Button butStart;
    private Button butIMEI;
    private Button butPrint;
    private Button butResetPwd;
    private Button butUpdateMainPwd;
    private Button butUpdateWorkPwd;
    private Button butGetKeyNum;
    private Button butInputPwd;
    private EditText etCount;
    private EditText etOK;
    private EditText etOpenError;
    TextView tvShow						= null;
    ScrollView tvShowScrol				= null;
    boolean m_bThreadExit	= false;
    boolean m_bThreadRunning = false;
    private Handler mHandler = null;
    private int m_nCount = 0;
    private int m_nOK = 0;
    private int m_nOpenError = 0;
    //
    private static final int TYPE_INFO	= 0;
    private static final int TYPE_WARN	= 1;
    private static final int TYPE_ERROR	= 2;
    private static final int TYPE_NOTIME = 3;
    private static Handler handlerScroll = new Handler();
    //
    TelephonyManager tm;


    UsbDeviceConnection connection;
    String TAG = "USB";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_usb_print);






    }





}