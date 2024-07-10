package com.abztrakinc.ABZPOS.ADMIN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.tool.processing.Scope;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abztrakinc.ABZPOS.CASHIER.create_journal_entry;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.discount_model;
import com.abztrakinc.ABZPOS.settingsDB;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;


import org.antlr.v4.runtime.misc.Utils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class admin_pos_settings extends AppCompatActivity {
    EditText et_shiftTotal;
    Button btn_save;
    Button btn_addDiscountItem;
    Context context;
    Button btn_bank, btn_otherPayment;
    int radComputation = 0;
    String radDiscountType;
    String DB_NAME = "settings.db";
    String DB_NAME2 = "PosOutputDB.db";
    String cboxCheck = "NO";
    String cboxCheck2 = "NO";
    String cboxCheck3 = "NO";
    int radIndicator = 1;

    String discountType;
    String discCategory;
    int id;

    //item for discount
    RadioButton rad_setPercent, rad_setComputation, rad_normalDiscount, rad_specialDiscount;
    RadioGroup rad_radGroup, rad_radGroup2, rad_radGroup1;
    CheckBox cbox_ExcludeTax, cbox_salesExcludeTax;
    CheckBox cbox_allowReference;
    CheckBox cbox_allowUserDetails;
    EditText et_discAutoID, et_discID, et_discName, et_discountPercent, et_discountComputation;
    Button btn_deleteDiscount, btn_zeroOut, btn_auditTrail, btn_discountSummary, btn_cashbox,btn_printerConfig,btn_stockCard,btn_gdrive,btn_testEmail,btn_itemSalesReport;
    Spinner sp_category, sp_discountType, sp_Label;
    ImageButton btn_addItem;
    EditText et_minTransAmount, et_maxTransAmount, et_maxDiscAmount;
    Button btn_clearReserve;
    Button btn_TenderReport;
    Button btn_monthlyReading;


    //test for printing
    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbInterface mInterface;
    private UsbEndpoint mEndPoint;
    private PendingIntent mPermissionIntent;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static Boolean forceCLaim = true;

    HashMap<String, UsbDevice> mDeviceList;
    Iterator<UsbDevice> mDeviceIterator;
    //end test


    Button btn_paperCut;
    ArrayList<String> DiscountList = new ArrayList<String>();
    ArrayList<String> DiscountListID = new ArrayList<String>();
    ArrayList<String> DiscountListName = new ArrayList<String>();
    ArrayList<String> DiscountListAmount = new ArrayList<String>();
    SQLiteDatabase db2, db;
    RecyclerView rv_discList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    View alertLayout;

    String FinalCategory, FinalDiscountType, FinalDiscountLabel;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pos_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        view = this.getCurrentFocus();
        //().setDisplayHomeAsUpEnabled(true);

        db2 = this.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);




        btn_monthlyReading = findViewById(R.id.btn_monthlyReading);
        btn_monthlyReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthlyReport(view);
            }
        });
        btn_itemSalesReport = findViewById(R.id.btn_itemSalesReport);
        btn_itemSalesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSalesReport(view);
            }
        });




        btn_cashbox = findViewById(R.id.btn_cashbox);
        btn_cashbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.CASHBOX");
                intent.putExtra("cashbox_open", true);
                sendBroadcast(intent);

            }
        });

        btn_zeroOut = findViewById(R.id.btn_zeroOut);
        btn_zeroOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


// add some password

                showPasswordDialog();

//
//                admin_zero_out_function admin_zero_out_function = new admin_zero_out_function();
//                admin_zero_out_function.zeroOutFunction(getApplicationContext());
//                Toast.makeText(getApplicationContext(), "ZERO OUT DATA", Toast.LENGTH_SHORT).show();
//                createDirectory();
//                finish();

            }
        });

        btn_addDiscountItem = findViewById(R.id.btn_addDiscountItem);


        ArrayList<String> DiscCategory = new ArrayList<>();
        ArrayList<String> DiscountType = new ArrayList<>();
        ArrayList<String> DiscountLabel = new ArrayList<>();


        btn_addDiscountItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SIMULATE
                startActivity(new Intent(admin_pos_settings.this, admin_pos_settings_discount.class));

            }
        });

        btn_discountSummary = findViewById(R.id.btn_discountSummary);
        btn_discountSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_pos_settings.this, admin_discount_summary.class));
            }
        });


        btn_deleteDiscount = findViewById(R.id.btn_deleteDiscount);
        btn_deleteDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getDiscountList();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_remove_discount_list, null);
                builder.setView(alertLayout);
                AlertDialog alertDialog = builder.create();
                //getDiscountList();


                SQLiteDatabase db = view.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                Cursor discountListID = db.rawQuery("select * from DiscountList", null);
                if (discountListID.getCount() != 0) {
                    DiscountList.clear();
                    DiscountListID.clear();
                    DiscountListName.clear();
                    DiscountListAmount.clear();
                    while (discountListID.moveToNext()) {
                        DiscountList.add(discountListID.getString(0));
                        DiscountListID.add(discountListID.getString(1));
                        DiscountListName.add(discountListID.getString(2));
                        DiscountListAmount.add(discountListID.getString(3));
                    }


                }

                Spinner s = alertLayout.findViewById(R.id.sp_discountID);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, DiscountListID);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                        EditText et_discountAmount = alertLayout.findViewById(R.id.et_discountAmount);

                        et_discountName.setText(DiscountListName.get(s.getSelectedItemPosition()));
                        et_discountAmount.setText(DiscountListAmount.get(s.getSelectedItemPosition()));
                        id = Integer.valueOf(DiscountList.get(s.getSelectedItemPosition()));


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                Button btn_removeItem = alertLayout.findViewById(R.id.btn_removeItem);
                btn_removeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("ID", String.valueOf(id));
                        SQLiteDatabase db2 = view.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                        String strsql = "DELETE FROM DiscountList where ID='" + id + "'";
                        db2.execSQL(strsql);
                        db2.close();

                        alertDialog.dismiss();
                        // alertDialog.show();


                    }
                });


//                itemListC = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
//                String totalItem = "0";
//                String totalSubtract;
//                Cursor itemListC2 = db2.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp", null);
                db.close();


//                cbox_ExcludeTax = alertLayout.findViewById(R.id.cbox_excludeTax);
//                et_discountPercent = alertLayout.findViewById(R.id.et_discountPercent);
//                et_discountComputation = alertLayout.findViewById(R.id.et_discountComputation);
//                et_discAutoID=alertLayout.findViewById(R.id.et_discAutoID);
//                btn_addItem=alertLayout.findViewById(R.id.btn_addItem);
//                et_discID=alertLayout.findViewById(R.id.et_discID);
//                et_discName=alertLayout.findViewById(R.id.et_discName);
//
//                et_discAutoID.setText(String.valueOf(ID));


                alertDialog.show();
            }
        });

        btn_paperCut = findViewById(R.id.btn_paperCut);
        btn_paperCut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

            salesReport(view);





//                mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//                mDeviceList = mUsbManager.getDeviceList();
//                mDeviceIterator = mDeviceList.values().iterator();
//
//                Toast.makeText(admin_pos_settings.this, "Device List Size: " + String.valueOf(mDeviceList.size()), Toast.LENGTH_SHORT).show();
//
//                mPermissionIntent = PendingIntent.getBroadcast(admin_pos_settings.this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//                IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//                registerReceiver(mUsbReceiver, filter);
//                if (mDevice != null)
//                    mUsbManager.requestPermission(mDevice, mPermissionIntent);


//                UsbManager m = (UsbManager)getApplicationContext().getSystemService(USB_SERVICE);
//                HashMap<String, UsbDevice> usbDevices = m.getDeviceList();
//                Collection<UsbDevice> ite = usbDevices.values();
//                UsbDevice[] usbs = ite.toArray(new UsbDevice[]{});
//                for (UsbDevice usb : usbs) {
//              //      Log.d("Connected usb devices","Connected usb devices are "+ usb.getDeviceName()+ " Device Class : "+usb.getDeviceClass()+ " " + usb.getDeviceId());
//                    Log.d("Connected usb devices","vendor ID "+ usb.getVendorId()+ " productID : "+usb.getProductId()+ " manufacture" + usb.getManufacturerName() +"class "+usb.getDeviceClass() + " subclass" + usb.getDeviceSubclass()+
//                            " protocol " + usb.getDeviceProtocol());
//
//                }
//
//
                //startActivity(new Intent(admin_pos_settings.this, cashier_shift_check_sales.class));
//
//
//



//                printer_settings_class printer_settings_class = new printer_settings_class(getApplicationContext());
//                printer_settings_class.Cashbox();


                //hourly sales

//                printer_settings_class PrinterSettings = new printer_settings_class(getApplicationContext());
//
//                PrinterSettings.OnlinePrinter("tessttt \r\n \r\n ",1,0);



            }
        });

        btn_TenderReport = findViewById(R.id.btn_tenderReport);
        btn_TenderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tenderReport(view);

            }
        });

        btn_auditTrail = findViewById(R.id.btn_auditTrail);
        btn_auditTrail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_pos_settings.this, admin_audit_trail.class));
            }
        });

        btn_bank = findViewById(R.id.btn_bank);
        btn_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getDiscountList();

                BankList.clear();
                getBankList();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_add_bank, null);
                builder.setView(alertLayout);
                AlertDialog alertDialog = builder.create();
                //getDiscountList();

                RadioButton rad_add = alertLayout.findViewById(R.id.rad_add);
                RadioButton rad_remove = alertLayout.findViewById(R.id.rad_remove);
                Spinner spinner_bankName = alertLayout.findViewById(R.id.spinner_bankName);
                EditText et_BankAutoID = alertLayout.findViewById(R.id.et_BankAutoID);
                EditText et_BankName = alertLayout.findViewById(R.id.et_BankName);
                Button btn_save = alertLayout.findViewById(R.id.btn_save);
                Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
                et_BankAutoID.setText(String.valueOf(ID));

                rad_add.setChecked(true);
                if (rad_add.isChecked()) {
                    et_BankName.requestFocus();
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, BankList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_bankName.setAdapter(adapter);
                spinner_bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                        //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                        // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                        // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                        //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                rad_radGroup = (RadioGroup) alertLayout.findViewById(R.id.rad_radGroup);


                rad_radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.rad_add) {

                            //et_discountPercent.setVisibility(View.VISIBLE);
                            // et_discountComputation.setVisibility(View.GONE);
                            spinner_bankName.setVisibility(View.GONE);
                            et_BankName.requestFocus();
                            et_BankAutoID.setVisibility(View.VISIBLE);
                            et_BankName.setVisibility(View.VISIBLE);
                            btn_save.setText("ADD BANK");
                            radIndicator = 1;


                        } else if (i == R.id.rad_remove) {
                            // et_discountPercent.setVisibility(View.GONE);
                            // et_discountComputation.setVisibility(View.VISIBLE);

                            spinner_bankName.setVisibility(View.VISIBLE);
                            et_BankAutoID.setVisibility(View.GONE);
                            et_BankName.setVisibility(View.GONE);
                            btn_save.setText("REMOVE BANK");
                            radIndicator = 0;
                        }
                    }
                });

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                        databaseHandler.insertBank(et_BankName.getText().toString());

                        alertDialog.dismiss();
                    }
                });


//                rad_radGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                        if (i==R.id.rad_normalDiscount){
//                            discountType="NORMAL DISCOUNT";
//                        }else if(i==R.id.rad_specialDiscount){
//                            discountType="SPECIAL DISCOUNT";
//                        }
//                    }
//                });

                alertDialog.show();

            }
        });


        btn_otherPayment = findViewById(R.id.btn_otherPayment);
        btn_otherPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(admin_pos_settings.this, admin_manage_other_payment.class));
//                getOtherPaymentList();
//                AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext(),R.style.myFullscreenAlertDialogStyle);
//                LayoutInflater inflater = getLayoutInflater();
//                final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_add_other_payment, null);
//                builder.setView(alertLayout);
//                AlertDialog alertDialog = builder.create();
//
//                ImageButton btn_close = alertLayout.findViewById(R.id.btn_close);
//                btn_close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//
//                //getDiscountList();
//
////                RadioButton rad_add=alertLayout.findViewById(R.id.rad_add);
////                RadioButton  rad_remove=alertLayout.findViewById(R.id.rad_remove);
////
////
////                 cbox_allowReference=alertLayout.findViewById(R.id.cbox_allowReference);
////                 cbox_allowUserDetails=alertLayout.findViewById(R.id.cbox_allowUserDetails);
////
////
////                Spinner spinner_bankName=alertLayout.findViewById(R.id.spinner_bankName);
////                EditText et_PaymentAutoID=alertLayout.findViewById(R.id.et_PaymentAutoID);
////                EditText et_OtherPayment=alertLayout.findViewById(R.id.et_OtherPayment);
////                Button btn_save = alertLayout.findViewById(R.id.btn_save);
////                Button btn_cancel=alertLayout.findViewById(R.id.btn_cancel);
////                et_PaymentAutoID.setText(String.valueOf(ID));
////
////                rad_add.setChecked(true);
////                if (rad_add.isChecked()){
////                    et_OtherPayment.requestFocus();
////                }
////
////
////
////                ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,BankList);
////                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                spinner_bankName.setAdapter(adapter);
////                spinner_bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                    @Override
////                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                        //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
////                        //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);
////
////                        // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
////                        // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
////                        //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));
////
////
////
////
////                    }
////
////                    @Override
////                    public void onNothingSelected(AdapterView<?> adapterView) {
////
////                    }
////                });
////
////
////
////
////                rad_radGroup=(RadioGroup)alertLayout.findViewById(R.id.rad_radGroup);
////
////
////
////                rad_radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////                    @Override
////                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
////                        if (i==R.id.rad_add){
////
////                            //et_discountPercent.setVisibility(View.VISIBLE);
////                            // et_discountComputation.setVisibility(View.GONE);
////                            spinner_bankName.setVisibility(View.GONE);
////                            et_OtherPayment.requestFocus();
////                            et_PaymentAutoID.setVisibility(View.VISIBLE);
////                            et_OtherPayment.setVisibility(View.VISIBLE);
////                            cbox_allowReference.setVisibility(View.VISIBLE);
////                            cbox_allowUserDetails.setVisibility(View.VISIBLE);
////                            btn_save.setText("ADD OTHER PAYMENT");
////                            radIndicator=1;
////
////
////
////
////                        }else if(i==R.id.rad_remove){
////                            // et_discountPercent.setVisibility(View.GONE);
////                            // et_discountComputation.setVisibility(View.VISIBLE);
////
////                            spinner_bankName.setVisibility(View.VISIBLE);
////                            et_PaymentAutoID.setVisibility(View.GONE);
////                            et_OtherPayment.setVisibility(View.GONE);
////                            cbox_allowReference.setVisibility(View.GONE);
////                            cbox_allowUserDetails.setVisibility(View.GONE);
////                            btn_save.setText("REMOVE OTHER PAYMENT");
////                            radIndicator=0;
////                        }
////                    }
////                });
////
////
//////                rad_radGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//////                    @Override
//////                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//////                        if (i==R.id.rad_normalDiscount){
//////                            discountType="NORMAL DISCOUNT";
//////                        }else if(i==R.id.rad_specialDiscount){
//////                            discountType="SPECIAL DISCOUNT";
//////                        }
//////                    }
//////                });
////
////                btn_save.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        if (radIndicator==1) {
////                            settingsDB settingsDB = new settingsDB(getApplicationContext());
////                            settingsDB.insertOtherPayment(et_OtherPayment.getText().toString(), cboxCheck2, cboxCheck3);
////                            Toast.makeText(admin_pos_settings.this, "Other Payment Added", Toast.LENGTH_SHORT).show();
////                        }
////                        else{
////
////
////                            SQLiteDatabase db2 = view.getContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
////                            String strsql = "DELETE FROM OtherPayment where PaymentName like %'"+spinner_bankName.getSelectedItem().toString()+"'% ";
////                           // String strsql = "DELETE FROM OtherPayment where PaymentName !='Giftcheck' ";
////
////                            Toast.makeText(admin_pos_settings.this, spinner_bankName.getSelectedItem().toString() + " Payment Deleted", Toast.LENGTH_SHORT).show();
////                            db2.execSQL(strsql);
////                            db2.close();
////
////                        }
////                    }
////                });
//
//                alertDialog.show();


            }
        });

        btn_printerConfig = findViewById(R.id.btn_printerConfig);
        btn_printerConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




//                printer_settings_configuration printerConfig = new printer_settings_configuration();
//                printerConfig.usbPrinter(getApplicationContext());


                startActivity(new Intent(admin_pos_settings.this, admin_pos_printer_settings.class));









            }
        });

        btn_clearReserve = findViewById(R.id.btn_clearReserve);
        btn_clearReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
                    String deleteReadTable = "delete from ITEM_RESERVE"; // change shift
                    db2.execSQL(deleteReadTable);
                    Toast.makeText(getApplicationContext(), "RESERVE CLEARED", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "ERROR CLEARING RESERVE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_stockCard = findViewById(R.id.btn_stockCard);
        btn_stockCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "on/off printer", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(admin_pos_settings.this, admin_stock_card.class));


                  }





        });


    //    googleDriveManager = new GoogleDriveManager(getApplicationContext(), this);

        btn_gdrive = findViewById(R.id.btn_gdrive);
        btn_gdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




//                googleDriveManager.signIn();
//                // Connect to Google Drive
//                googleDriveManager.connect();
//
//                // Check if GoogleDriveManager is connected before uploading file
//                if (googleDriveManager != null && googleDriveManager.isConnected()) {
//                    String fileName = "example.txt";
//                    String fileContent = "This is the content of the text file.";
//                    googleDriveManager.uploadTextFile(fileName, fileContent);
//                } else {
//                    // Handle the case where GoogleDriveManager is not initialized or connected
//                    Log.e("TAG", "GoogleDriveManager is not initialized or connected");
//                }
            }
        });

        btn_testEmail = findViewById(R.id.btn_testEmail);
        btn_testEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                launchFilePicker();

            }
        });





        KeyBoardMap();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Ensure googleDriveManager is not null
//        if (googleDriveManager == null) {
//            Log.e("TAG", "GoogleDriveManager is null");
//            return;
//        }
//
//        // Pass the result to the GoogleDriveManager
//        googleDriveManager.onActivityResult(requestCode, resultCode, data);
//
//        // If the requestCode matches the sign-in request code and the sign-in was successful,
//        // connect to Google Drive
//        if (requestCode == googleDriveManager.getRcSignIn() && resultCode == Activity.RESULT_OK) {
//            googleDriveManager.connect();
//        } else {
//            // Handle sign-in failure or other cases if needed
//            Log.e("TAG", "Sign-in failed or other result received");
//        }
//    }




    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_PERMISSION = 100;
   // private GoogleDriveManager googleDriveManager;


//    private void connectGDrive(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_CODE_PERMISSION);
//        } else {
//            googleDriveManager = new GoogleDriveManager(this,admin_pos_settings.this);
//            googleDriveManager.connect();
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                googleDriveManager = new GoogleDriveManager(this,admin_pos_settings.this);
//                googleDriveManager.connect();
//            }
//        }
//    }




    private static final int PICK_FILE_REQUEST_CODE = 100;

//    private void launchFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*"); // All file types
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
//    }

    private void launchFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // All file types
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple file selection
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Files"), PICK_FILE_REQUEST_CODE);
    }




    // Handle result from file picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<Uri> uris = new ArrayList<>();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        uris.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else if (data.getData() != null) {
                    uris.add(data.getData());
                }
                admin_send_email_async sendEmailTask = new admin_send_email_async();
                sendEmailTask.SendEmailTask(getApplicationContext(),"bobwendelarcon@gmail.com", "ABZPOS GENERATED EMAIL", "This File is generated by Abzpos for testing", uris);
               sendEmailTask.execute();

                   } else {
                // Handle case where data is null
                Toast.makeText(getApplicationContext(), "Data is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle case where file selection is cancelled or result code mismatch
            Toast.makeText(getApplicationContext(), "File selection cancelled", Toast.LENGTH_SHORT).show();
        }









    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // dismissPasswordDialog(); // Dismiss the password dialog if it's showing
    }

    private AlertDialog passwordDialog;
    EditText inputPassword;
    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        // Set up the input
 inputPassword= new EditText(this);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputPassword);

        // Set up the buttons
        builder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = inputPassword.getText().toString();
                if (password.equals("832815")) { // Replace "your_password" with your actual password
                    // Password is correct, perform admin function
                    admin_zero_out_function admin_zero_out_function = new admin_zero_out_function();
                    admin_zero_out_function.zeroOutFunction(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "ZERO OUT DATA", Toast.LENGTH_SHORT).show();
                    createDirectory();
                    finish();
                } else {
                    // Password is incorrect, show a message or take appropriate action
                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        passwordDialog = builder.create();
        passwordDialog.show();
    }

    // Function to close the password dialog






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

    @Override
    public void onResume() {
        super.onResume();

        KeyBoardMap();
    }
    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;
       // View view =  this.getCurrentFocus();


        String[] allowedInput = {"0","1","2","3","4","5","6","7","8","9"};

        for (String element : allowedInput){
            if (element ==  input){


                if (input.equals(".")){
                    input="-";
                }

                if (passwordDialog != null && passwordDialog.isShowing()) {




                            int start = Math.max(inputPassword.getSelectionStart(), 0);
                            int end = Math.max(inputPassword.getSelectionEnd(), 0);
                            inputPassword.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (inputPassword.length() == 0) {
                                return;
                            }





                }
                else{

                    if (input.equalsIgnoreCase("9")){

                        Intent intent = new Intent("android.intent.action.CASHBOX");
                        intent.putExtra("cashbox_open", true);
                        sendBroadcast(intent);
                    }
                    if (input.equalsIgnoreCase("6")){

                        salesReport(view);
                    }
                    if (input.equalsIgnoreCase("3")){


                        startActivity(new Intent(admin_pos_settings.this, admin_pos_printer_settings.class));
                    }

                }




            }

        }

        if (passwordDialog!=null && passwordDialog.isShowing()){
            if (input.equalsIgnoreCase("Total")){
                String password = inputPassword.getText().toString();
                if (password.equals("832815")) { // Replace "your_password" with your actual password
                    // Password is correct, perform admin function
                    admin_zero_out_function admin_zero_out_function = new admin_zero_out_function();
                    admin_zero_out_function.zeroOutFunction(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "ZERO OUT DATA", Toast.LENGTH_SHORT).show();
                    createDirectory();
                    finish();
                } else {
                    // Password is incorrect, show a message or take appropriate action
                    Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                }


            }
            else if (input.equalsIgnoreCase("CLEAR")){
                inputPassword.setText("");
            }

           else  if ( input.equalsIgnoreCase("EC")) {
                int length = inputPassword.getText().length();
                if (length > 0) {
                    inputPassword.getText().delete(length - 1, length);
                }
            }

        }




        if (input.equalsIgnoreCase("Exit")){


            if (passwordDialog != null && passwordDialog.isShowing()) {
                passwordDialog.dismiss();
            }
            else{
                onBackPressed();
            }


            //triggerRebirth();


        }



        if (input.equalsIgnoreCase("D04")){
            startActivity(new Intent(admin_pos_settings.this, admin_pos_settings_discount.class));
        }
        if (input.equalsIgnoreCase("D03")){
            startActivity(new Intent(admin_pos_settings.this, admin_audit_trail.class));
        }
        if (input.equalsIgnoreCase("D02")){
            try {
                SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
                String deleteReadTable = "delete from ITEM_RESERVE"; // change shift
                db2.execSQL(deleteReadTable);
                Toast.makeText(getApplicationContext(), "RESERVE CLEARED", Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex){
                Toast.makeText(getApplicationContext(), "ERROR CLEARING RESERVE", Toast.LENGTH_SHORT).show();
            }
        }
        if (input.equalsIgnoreCase("D08")){

        }
        if (input.equalsIgnoreCase("D07")){
            BankList.clear();
            getBankList();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_add_bank, null);
            builder.setView(alertLayout);
            AlertDialog alertDialog = builder.create();
            //getDiscountList();

            RadioButton rad_add = alertLayout.findViewById(R.id.rad_add);
            RadioButton rad_remove = alertLayout.findViewById(R.id.rad_remove);
            Spinner spinner_bankName = alertLayout.findViewById(R.id.spinner_bankName);
            EditText et_BankAutoID = alertLayout.findViewById(R.id.et_BankAutoID);
            EditText et_BankName = alertLayout.findViewById(R.id.et_BankName);
            Button btn_save = alertLayout.findViewById(R.id.btn_save);
            Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
            et_BankAutoID.setText(String.valueOf(ID));

            rad_add.setChecked(true);
            if (rad_add.isChecked()) {
                et_BankName.requestFocus();
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, BankList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_bankName.setAdapter(adapter);
            spinner_bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                    //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                    // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                    // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                    //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            rad_radGroup = (RadioGroup) alertLayout.findViewById(R.id.rad_radGroup);


            rad_radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.rad_add) {

                        //et_discountPercent.setVisibility(View.VISIBLE);
                        // et_discountComputation.setVisibility(View.GONE);
                        spinner_bankName.setVisibility(View.GONE);
                        et_BankName.requestFocus();
                        et_BankAutoID.setVisibility(View.VISIBLE);
                        et_BankName.setVisibility(View.VISIBLE);
                        btn_save.setText("ADD BANK");
                        radIndicator = 1;


                    } else if (i == R.id.rad_remove) {
                        // et_discountPercent.setVisibility(View.GONE);
                        // et_discountComputation.setVisibility(View.VISIBLE);

                        spinner_bankName.setVisibility(View.VISIBLE);
                        et_BankAutoID.setVisibility(View.GONE);
                        et_BankName.setVisibility(View.GONE);
                        btn_save.setText("REMOVE BANK");
                        radIndicator = 0;
                    }
                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                    databaseHandler.insertBank(et_BankName.getText().toString());

                    alertDialog.dismiss();
                }
            });


//                rad_radGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                        if (i==R.id.rad_normalDiscount){
//                            discountType="NORMAL DISCOUNT";
//                        }else if(i==R.id.rad_specialDiscount){
//                            discountType="SPECIAL DISCOUNT";
//                        }
//                    }
//                });

            alertDialog.show();

        }
        if (input.equalsIgnoreCase("D06")){
            String Datefrom="2023-01-01";
            String DateTo="2023-12-30";
            StringBuffer buffer = new StringBuffer();


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_check_sales_date, null);
            builder.setView(alertLayout);
            AlertDialog alertDialog = builder.create();
            Button btn_submit = alertLayout.findViewById(R.id.btn_submit);

            DatePicker datePickerFrom = (DatePicker) alertLayout.findViewById(R.id.datePickerFrom);
            DatePicker datePickerTo= (DatePicker) alertLayout.findViewById(R.id.datePickerTo);



            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int day = datePickerFrom.getDayOfMonth();
                    int month = datePickerFrom.getMonth() + 1;
                    int year = datePickerFrom.getYear();

                    int dayTo = datePickerTo.getDayOfMonth();
                    int monthTo = datePickerTo.getMonth() + 1;
                    int yearTo = datePickerTo.getYear();

                    // Datefrom = year+"-"+month+"-"+day;
                    //  DateTo=    yearTo+"-"+monthTo+"-"+dayTo;
                    String sMonthFrom = String.valueOf(month);
                    String sDayFrom = String.valueOf(day);

                    String sMonthTo = String.valueOf(monthTo);
                    String sDayTo = String.valueOf(dayTo);

                    if (sMonthFrom.length()==1){
                        sMonthFrom = "0"+sMonthFrom;
                    }

                    if (sDayFrom.length()==1){
                        sDayFrom = "0"+sDayFrom;
                    }


                    if (sMonthTo.length()==1){
                        sMonthTo = "0"+sMonthTo;
                    }

                    if (sDayTo.length()==1){
                        sDayTo = "0"+sDayTo;
                    }

                    Log.d("Date From", year + "-" + month +"-"+ day );

                    String finalFrom = String.valueOf(year)+"-"+sMonthFrom+"-"+sDayFrom;
                    String finalTo = String.valueOf(yearTo)+"-"+sMonthTo+"-"+sDayTo;

                    printTenderReport(finalFrom,finalTo);
                    alertDialog.dismiss();

                }
            });

            alertDialog.show();



        }
        if (input.equalsIgnoreCase("D12")){
            showPasswordDialog();
//            admin_zero_out_function admin_zero_out_function = new admin_zero_out_function();
//            admin_zero_out_function.zeroOutFunction(getApplicationContext());
//            Toast.makeText(getApplicationContext(), "ZERO OUT DATA", Toast.LENGTH_SHORT).show();
//            createDirectory();
//            finish();
        }
        if (input.equalsIgnoreCase("D11")){
            startActivity(new Intent(admin_pos_settings.this, admin_manage_other_payment.class));
        }
        if (input.equalsIgnoreCase("D10")){
            settingsDB settings = new settingsDB(getApplicationContext());

            SQLiteDatabase db = this.getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            Cursor PrinterActivate = db.rawQuery("select Print from PrinterSettings", null);
            if (PrinterActivate.getCount()!=0){
                if (PrinterActivate.moveToFirst()){
                    if (PrinterActivate.getInt(0)==1){
                        //String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
                        String strsql = "UPDATE PrinterSettings set Print=2";
                        db.execSQL(strsql);
                        Toast.makeText(getApplicationContext(), "Printer Off", Toast.LENGTH_SHORT).show();


                    }
                    else{
                        String strsql = "UPDATE PrinterSettings set Print=1";
                        Toast.makeText(getApplicationContext(), "Printer On", Toast.LENGTH_SHORT).show();
                        db.execSQL(strsql);

                        StringBuffer buffer = new StringBuffer();
                        buffer.append("PRINTER : ON " + "\n");
                        buffer.append("T E S T   P R I N T"+"\n\n\n");



                        printer_settings_class print = new printer_settings_class(getApplicationContext());
                        print.OnlinePrinter(buffer.toString(),1,1,1); // cashier invoice


                    }
                }
            }

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


















    public boolean onOptionItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }








    private void FillDataRV(){

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ArrayDiscountList.clear();
        Cursor itemListC = db.rawQuery("select * from DiscountList", null);

        int numberOfItem = itemListC.getCount();
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){



          //  int ItemNo=itemCounter+1;

            if (itemListC.getCount()!=0){
                while (itemListC.moveToNext()){

                    String DiscID=itemListC.getString(0);
                    String DiscountID=itemListC.getString(1);
                    String DiscountName=itemListC.getString(2);
                    String DiscountAmount=itemListC.getString(3);
                    String DiscountComputation=itemListC.getString(4);
                    String DiscountExcludeTax=itemListC.getString(5);
                    String DiscountType=itemListC.getString(6);
                    String DiscCategory=itemListC.getString(7);
                    String DiscLabel=itemListC.getString(8);
                    String MinTransactionAmount=itemListC.getString(9);
                    String MaxTransactionAmount=itemListC.getString(10);
                    String MaxDiscountAmount=itemListC.getString(11);
                    String SalesExcludeTax=itemListC.getString(12);






                    discount_model p0=new discount_model(DiscID,DiscountID,DiscountName,DiscountAmount,DiscountComputation,DiscountExcludeTax,DiscountType,DiscCategory,
                            DiscLabel,MinTransactionAmount,MaxTransactionAmount,MaxDiscountAmount,SalesExcludeTax);
                    //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

                    ArrayDiscountList.addAll(Arrays.asList(new discount_model[]{p0}));

                }
            }



        }
        //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";
        db.close();

    }
    private void RefreshRV(){


        rv_discList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(alertLayout.getContext());
        layoutManager.removeAllViews();
        rv_discList.setLayoutManager(layoutManager);
        mAdapter=new admin_pos_settings.RecyclerviewAdapter(ArrayDiscountList,alertLayout.getContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_discList.setAdapter(mAdapter);

    }




    public void checkbox_clicked(View v)
    {

        if (cbox_ExcludeTax.isChecked()){
            cboxCheck="YES";
        }
        else {
            cboxCheck="NO";
        }

    }

    public void checkbox2_clicked(View v)
    {

        if (cbox_salesExcludeTax.isChecked()){
            cboxCheck2="YES";
        }
        else {
            cboxCheck2="NO";
        }

    }




   int ID;
    AlertDialog alertDialog;
    String Datefrom="2023-01-01";
    String DateTo="2023-12-30";
    private void salesReport(View view){






        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_check_sales_date, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        Button btn_submit = alertLayout.findViewById(R.id.btn_submit);

        DatePicker datePickerFrom = (DatePicker) alertLayout.findViewById(R.id.datePickerFrom);
        DatePicker datePickerTo= (DatePicker) alertLayout.findViewById(R.id.datePickerTo);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = datePickerFrom.getDayOfMonth();
                int month = datePickerFrom.getMonth() + 1;
                int year = datePickerFrom.getYear();

                int dayTo = datePickerTo.getDayOfMonth();
                int monthTo = datePickerTo.getMonth() + 1;
                int yearTo = datePickerTo.getYear();

               // Datefrom = year+"-"+month+"-"+day;
              //  DateTo=    yearTo+"-"+monthTo+"-"+dayTo;
                String sMonthFrom = String.valueOf(month);
                String sDayFrom = String.valueOf(day);

                String sMonthTo = String.valueOf(monthTo);
                String sDayTo = String.valueOf(dayTo);

                if (sMonthFrom.length()==1){
                    sMonthFrom = "0"+sMonthFrom;
                }

                if (sDayFrom.length()==1){
                    sDayFrom = "0"+sDayFrom;
                }


                if (sMonthTo.length()==1){
                    sMonthTo = "0"+sMonthTo;
                }

                if (sDayTo.length()==1){
                    sDayTo = "0"+sDayTo;
                }

                Log.d("Date From", year + "-" + month +"-"+ day );

                String finalFrom = String.valueOf(year)+"-"+sMonthFrom+"-"+sDayFrom;
                String finalTo = String.valueOf(yearTo)+"-"+sMonthTo+"-"+sDayTo;

                printSalesReport(finalFrom,finalTo);
                alertDialog.dismiss();

            }
        });

        //getDiscountList();





    alertDialog.show();





    }
    private void monthlyReport(View view){






        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_check_sales_date2, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        Button btn_submit = alertLayout.findViewById(R.id.btn_submit);

        DatePicker datePickerFrom = (DatePicker) alertLayout.findViewById(R.id.datePickerFrom);
        DatePicker datePickerTo= (DatePicker) alertLayout.findViewById(R.id.datePickerTo);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = datePickerFrom.getDayOfMonth();
                int month = datePickerFrom.getMonth() + 1;
                int year = datePickerFrom.getYear();

                int dayTo = datePickerTo.getDayOfMonth();
                int monthTo = datePickerTo.getMonth() + 1;
                int yearTo = datePickerTo.getYear();

                // Datefrom = year+"-"+month+"-"+day;
                //  DateTo=    yearTo+"-"+monthTo+"-"+dayTo;
                String sMonthFrom = String.valueOf(month);
                String sDayFrom = String.valueOf(day);

                String sMonthTo = String.valueOf(monthTo);
                String sDayTo = String.valueOf(dayTo);

                if (sMonthFrom.length()==1){
                    sMonthFrom = "0"+sMonthFrom;
                }

                if (sDayFrom.length()==1){
                    sDayFrom = "0"+sDayFrom;
                }


                if (sMonthTo.length()==1){
                    sMonthTo = "0"+sMonthTo;
                }

                if (sDayTo.length()==1){
                    sDayTo = "0"+sDayTo;
                }

                Log.d("Date From", year + "-" + month +"-"+ day );

                String finalFrom = String.valueOf(year)+"-"+sMonthFrom+"-"+sDayFrom;
                String finalTo = String.valueOf(yearTo)+"-"+sMonthTo+"-"+sDayTo;

                printMonthlyReport(finalFrom,finalTo);
                alertDialog.dismiss();

            }
        });

        //getDiscountList();





        alertDialog.show();





    }


    private void itemSalesReport(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_check_sales_date3, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        Button btn_submit = alertLayout.findViewById(R.id.btn_submit);

        DatePicker datePickerFrom = (DatePicker) alertLayout.findViewById(R.id.datePickerFrom);
        DatePicker datePickerTo = (DatePicker) alertLayout.findViewById(R.id.datePickerTo);
        Button btn_generateExcel = alertLayout.findViewById(R.id.btn_generateExcel);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePickerFrom.getDayOfMonth();
                int month = datePickerFrom.getMonth() + 1;
                int year = datePickerFrom.getYear();

                int dayTo = datePickerTo.getDayOfMonth();
                int monthTo = datePickerTo.getMonth() + 1;
                int yearTo = datePickerTo.getYear();

                String sMonthFrom = String.valueOf(month);
                String sDayFrom = String.valueOf(day);

                String sMonthTo = String.valueOf(monthTo);
                String sDayTo = String.valueOf(dayTo);

                if (sMonthFrom.length() == 1) {
                    sMonthFrom = "0" + sMonthFrom;
                }

                if (sDayFrom.length() == 1) {
                    sDayFrom = "0" + sDayFrom;
                }

                if (sMonthTo.length() == 1) {
                    sMonthTo = "0" + sMonthTo;
                }

                if (sDayTo.length() == 1) {
                    sDayTo = "0" + sDayTo;
                }

                String finalFrom = year + "-" + sMonthFrom + "-" + sDayFrom;
                String finalTo = yearTo + "-" + sMonthTo + "-" + sDayTo;

                printItemSalesReport(finalFrom, finalTo);
                alertDialog.dismiss();
            }
        });

        btn_generateExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = datePickerFrom.getDayOfMonth();
                int month = datePickerFrom.getMonth() + 1;
                int year = datePickerFrom.getYear();

                int dayTo = datePickerTo.getDayOfMonth();
                int monthTo = datePickerTo.getMonth() + 1;
                int yearTo = datePickerTo.getYear();

                String sMonthFrom = String.valueOf(month);
                String sDayFrom = String.valueOf(day);

                String sMonthTo = String.valueOf(monthTo);
                String sDayTo = String.valueOf(dayTo);

                if (sMonthFrom.length() == 1) {
                    sMonthFrom = "0" + sMonthFrom;
                }

                if (sDayFrom.length() == 1) {
                    sDayFrom = "0" + sDayFrom;
                }

                if (sMonthTo.length() == 1) {
                    sMonthTo = "0" + sMonthTo;
                }

                if (sDayTo.length() == 1) {
                    sDayTo = "0" + sDayTo;
                }

                String finalFrom = year + "-" + sMonthFrom + "-" + sDayFrom;
                String finalTo = yearTo + "-" + sMonthTo + "-" + sDayTo;

                generateItemSalesReportExcel(finalFrom, finalTo);
                alertDialog.dismiss();

            }
        });

        alertDialog.show();

        // Adjust the size of the AlertDialog
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(layoutParams);
    }



    private void printSalesReport(String from,String to){
        StringBuffer buffer = new StringBuffer();
       // Toast.makeText(getApplicationContext(), "Tender Report", Toast.LENGTH_SHORT).show();

        ArrayList listDateFrom = new ArrayList<>();
        ArrayList listDateTo = new ArrayList<>();
//        listDateFrom.add("6:01 AM");
//        listDateFrom.add("9:01 AM");
//        listDateFrom.add("12:01 PM");
//        listDateFrom.add("3:01 PM");
//        listDateFrom.add("6:01 PM");
//        listDateFrom.add("9:01 PM");
//        listDateFrom.add("12:01 AM");
//        listDateFrom.add("3:01 AM");

        listDateFrom.add("6:01");
        listDateFrom.add("9:01");
        listDateFrom.add("12:01");
        listDateFrom.add("3:01");

        listDateTo.add("9:00");
        listDateTo.add("12:00");
        listDateTo.add("3:00");
        listDateTo.add("6:00");


//        listDateTo.add("9:00 AM");
//        listDateTo.add("12:00 PM");
//        listDateTo.add("3:00 PM");
//        listDateTo.add("6:00 PM");
//        listDateTo.add("9:00 PM");
//        listDateTo.add("12:00 AM");
//        listDateTo.add("3:00 AM");
//        listDateTo.add("6:00 AM");




        printer_settings_class PrinterSettings = new printer_settings_class(getApplicationContext());
        buffer.append("--------------------------------" + "\n");
        buffer.append("Hourly Sales Report         " + "\n");
        buffer.append("from : " + from +"\n");
        buffer.append("to   : " + to + "\n");
        buffer.append("--------------------------------" + "\n");


//          buffer.append("6:00  - 9:00 AM -" + "\n");
//        buffer.append("                                " + "\n");
//        buffer.append("9:01  - 12:00 NN " + "\n");
//        buffer.append("                                " + "\n");
//        buffer.append("12:01 - 3:00 PM" + "\n");
//        buffer.append("" + "\n");
//        buffer.append("3:01  - 6:00 PM" + "\n");
//        buffer.append("" + "\n");
//        buffer.append("6:01  - 9:00 PM" + "\n");
//        buffer.append("" + "\n");
//        buffer.append("9:01  - 12:00 MN" + "\n");
//        buffer.append("" + "\n");
//        buffer.append("12:01  - 3:00 AM" + "\n");
//        buffer.append("" + "\n");
//        buffer.append("3:01  - 6:00 AM" + "\n");





        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        for (int x=0;x<listDateFrom.size();x++) {
            Cursor getTenderType = db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate between '" + from + "' and '" + to + "' and TransTime between '"+listDateFrom.get(x)+"' and  '"+listDateTo.get(x)+"'", null);
            if (getTenderType.getCount() != 0) {
                 while(getTenderType.moveToNext()) {

                     String amt = getTenderType.getString(0);
                     DecimalFormat decimalFormat = new DecimalFormat("#.##");

                     if (amt=="" || amt==" " || amt==null){
                         amt="P 0.00";
                     }
                     else{
                         double tmp = Double.parseDouble(amt);
                         amt = "P "+String.valueOf(tmp);
                     }

                     buffer.append( listDateFrom.get(x) + " - " + listDateTo.get(x)  + "\n");
                    buffer.append("                 " + amt+  "\n");
                    buffer.append("" + "\n");
                }
            }
            else{
                buffer.append( listDateFrom.get(x) + " - " + listDateTo.get(x)  + "\n");
                buffer.append("                 " + "0.00" +  "\n");
                buffer.append("" + "\n");
            }

        }

        PrinterSettings.OnlinePrinter(buffer.toString(),1,0,1); // cashier invoice

    }
    private void printMonthlyReport(String from,String to){

        monthly_sales_report monthlySalesReport = new monthly_sales_report(this.getApplicationContext(),from,to);



        StringBuffer buffer = new StringBuffer();
        // Toast.makeText(getApplicationContext(), "Tender Report", Toast.LENGTH_SHORT).show();

        ArrayList listDateFrom = new ArrayList<>();
        ArrayList listDateTo = new ArrayList<>();

//        listDateFrom.add("9:01");
//        listDateFrom.add("12:01");
//        listDateFrom.add("3:01");
//
//        listDateTo.add("9:00");
//        listDateTo.add("12:00");
//        listDateTo.add("3:00");
//        listDateTo.add("6:00");


        Header_Footer_class headerFooterClass = new Header_Footer_class();
        headerFooterClass.HeaderNote(this.getApplicationContext());
        headerFooterClass.FooterNote(this.getApplicationContext());


        printer_settings_class PrinterSettings = new printer_settings_class(getApplicationContext());

        buffer.append(headerFooterClass.getHeaderText());
        buffer.append("--------------------------------" + "\n");
        buffer.append("       Monthly Sales Report     " + "\n");
        buffer.append("from : " + from +"\n");
        buffer.append("to   : " + to + "\n");
        buffer.append("--------------------------------" + "\n");
        buffer.append("GROSS SALES           " + (String.format("%7.2f",monthlySalesReport.getGrossSales())+"           ").substring(0,10)  + "\n");
        buffer.append("VATables Sales        " + (String.format("%7.2f",monthlySalesReport.getVatableSales())+"           ").substring(0,10)  + "\n");
        buffer.append("VAT Amount            " + (String.format("%7.2f",monthlySalesReport.getVatAmount())+"           ").substring(0,10)  + "\n");
        buffer.append("VAT Exempt Sales      " + (String.format("%7.2f",monthlySalesReport.getVatExempt())+"           ").substring(0,10)  + "\n");
        buffer.append("Less VAT              " + (String.format("%7.2f",monthlySalesReport.getLessVat())+"           ").substring(0,10)  + "\n");
        buffer.append("Zero-Rated Sales      " + (String.format("%7.2f",monthlySalesReport.getZeroRated())+"           ").substring(0,10)  + "\n");
        buffer.append(("SPECIAL DISCOUNT"+"                                   ").substring(0,22) + (String.format("%7.2f",monthlySalesReport.getSpecialDisc())+"           ").substring(0,10)  + "\n");
        buffer.append(("NORMAL DISCOUNT"+"                                   ").substring(0,22) + (String.format("%7.2f",monthlySalesReport.getNormalDisc())+"           ").substring(0,10)  + "\n");
       // netSales=grossSales-lessVat-specialDisc-normalDisc-vatAmount;
        monthlySalesReport.setNetSales(monthlySalesReport.getGrossSales()-monthlySalesReport.getLessVat()-monthlySalesReport.getSpecialDisc()-monthlySalesReport.getNormalDisc()-monthlySalesReport.getVatAmount());
        buffer.append("NET SALES             " + (String.format("%7.2f",monthlySalesReport.getNetSales())+"           ").substring(0,10)  + "\n\n");
        buffer.append("Grand Total           " + (String.format("%7.2f",monthlySalesReport.getGrandTotal())+"           ").substring(0,10)  + "\n\n");
        buffer.append("--------------------------------" + "\n");
        buffer.append(headerFooterClass.getFooterText());









//for
//        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        for (int x=0;x<listDateFrom.size();x++) {
//            Cursor getTenderType = db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate between '" + from + "' and '" + to + "' and TransTime between '"+listDateFrom.get(x)+"' and  '"+listDateTo.get(x)+"'", null);
//            if (getTenderType.getCount() != 0) {
//                while(getTenderType.moveToNext()) {
//
//                    String amt = getTenderType.getString(0);
//                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
//
//                    if (amt=="" || amt==" " || amt==null){
//                        amt="P 0.00";
//                    }
//                    else{
//                        double tmp = Double.parseDouble(amt);
//                        amt = "P "+String.valueOf(tmp);
//                    }
//
//                    buffer.append( listDateFrom.get(x) + " - " + listDateTo.get(x)  + "\n");
//                    buffer.append("                 " + amt+  "\n");
//                    buffer.append("" + "\n");
//                }
//            }
//            else{
//                buffer.append( listDateFrom.get(x) + " - " + listDateTo.get(x)  + "\n");
//                buffer.append("                 " + "0.00" +  "\n");
//                buffer.append("" + "\n");
//            }
//
//        }

        PrinterSettings.OnlinePrinter(buffer.toString(),1,0,1); // cashier invoice

    }

    private void printItemSalesReport(String from,String to){





        StringBuffer buffer = new StringBuffer();
        // Toast.makeText(getApplicationContext(), "Tender Report", Toast.LENGTH_SHORT).show();

        ArrayList listDateFrom = new ArrayList<>();
        ArrayList listDateTo = new ArrayList<>();



        Header_Footer_class headerFooterClass = new Header_Footer_class();
        headerFooterClass.HeaderNote(this.getApplicationContext());
        headerFooterClass.FooterNote(this.getApplicationContext());


        printer_settings_class PrinterSettings = new printer_settings_class(getApplicationContext());

        buffer.append(headerFooterClass.getHeaderText());
        buffer.append("--------------------------------" + "\n");
        buffer.append("       ITEM Sales Report     " + "\n");
        buffer.append("from : " + from +"\n");
        buffer.append("to   : " + to + "\n");
        buffer.append("--------------------------------" + "\n");

        buffer.append("");


        ArrayList<String> OrderNameList = new ArrayList<>();

        SQLiteDatabase PosOutputDB = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

// Query to sum quantities for each unique OrderName
        Cursor readPLUItems = PosOutputDB.rawQuery(
                "SELECT OrderID, OrderName, SUM(OrderQty) as TotalQty " +
                        "FROM InvoiceReceiptItemFinal where TransactionDate between '"+from+"' and '"+to+"'" +
                        "GROUP BY OrderID, OrderName ", null);

        if (readPLUItems.getCount() != 0) {
            int itemQty = 1;
            int finalQty=0;
            while (readPLUItems.moveToNext()) {
                String orderID = readPLUItems.getString(0);
                String orderName = readPLUItems.getString(1);
                String totalQty = readPLUItems.getString(2);

                if (!OrderNameList.contains(orderID)) {
                    OrderNameList.add(orderID);
                    buffer.append(itemQty + "). " + orderName +"\n  SKU : ("+orderID+") "
                            +" - Qty: " + totalQty + "\n\n");
                    finalQty+=Integer.parseInt(totalQty);
                    itemQty++;
                }


            }

            buffer.append("------------------------------------------------\n");
            buffer.append("Total Item QTY :"+finalQty);
            readPLUItems.close();
        }

// Don't forget to close the database when done
        PosOutputDB.close();





//        for(int pluctr2=0;pluctr2<OrderNameList.size();pluctr2++) {
//            Cursor readPLUItems = PosOutputDB.rawQuery("select sum(OrderQty),sum(OrderPriceTotal)  from InvoiceReceiptItemFinal where OrderName='" + OrderNameList.get(pluctr2) + "'and TransactionID between'" + TransactionIDList.get(0) + "'and '" + TransactionIDList.get(TransactionIDList.size() - 1) + "'", null);
//            //Cursor readPLUItemsQty =PosOutputDB.rawQuery("select DISTINCT OrderName  from InvoiceReceiptItemFinal where TransactionID='"+TransactionIDList.get(pluctr)+"'", null);
//            if (readPLUItems.getCount() != 0) {
//                while (readPLUItems.moveToNext()) {
//                    // buffer.append("TOTAL: CASH              "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"        ").substring(0,10) + (String.format("%7.2f",finalCashAmount)+"           ").substring(0,11)  + "\n");
//
//
//                    buffer.append((OrderNameList.get(pluctr2) + "                                ").substring(0, 32));
//                    if (OrderNameList.get(pluctr2).length() > 31) {
//                        buffer.append((OrderNameList.get(pluctr2) + "                                ").substring(32, 64));
//                    }
//                    try {
//                        buffer.append((("                " + String.format("%6d", Integer.parseInt(readPLUItems.getString(0)))).substring(0, 22) + ("   " + String.format("%7.2f", Double.parseDouble(readPLUItems.getString(1))) + "           ").substring(0, 10) + "\n"));
//                    } catch (Exception ex) {
//                        Log.e("ERROR", ex.getMessage());
//                    }
//                }
//            }
//
//
//            buffer.append(headerFooterClass.getFooterText());
//
//
//        }



        PrinterSettings.OnlinePrinter(buffer.toString(),1,0,1); // cashier invoice

    }
    private void generateItemSalesReportExcel(String from, String to) {
        StringBuffer buffer = new StringBuffer();
        ArrayList<String> OrderNameList = new ArrayList<>();

        SQLiteDatabase PosOutputDB = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

        // Query to sum quantities for each unique OrderName
        Cursor readPLUItems = PosOutputDB.rawQuery(
                "SELECT OrderID, OrderName, SUM(OrderQty) as TotalQty " +
                        "FROM InvoiceReceiptItemFinal where TransactionDate between '" + from + "' and '" + to + "'" +
                        "GROUP BY OrderID, OrderName ", null);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Item Sales Report");

        // Headers
        Row headerRow = sheet.createRow(0);

        Cell cellDate = headerRow.createCell(0);
        cellDate.setCellValue("DATE ");
        cellDate = headerRow.createCell(1);
        if (from.equalsIgnoreCase(to)){
            cellDate.setCellValue(to);
        }
        else{
            cellDate.setCellValue(from + " : " + to);
        }




        Row headerRow1 = sheet.createRow(1); // Row index 1 for headers
        String[] headers = {"SKU", "Item Name", "Quantity"};
        for (int col = 0; col < headers.length; col++) {
            Cell cell = headerRow1.createCell(col);
            cell.setCellValue(headers[col]);
        }

        // Data Rows
        int rowNum = 2;
        int finalQty=0;
        if (readPLUItems != null && readPLUItems.moveToFirst()) {
            do {
                String orderID = readPLUItems.getString(readPLUItems.getColumnIndex("OrderID"));
                String orderName = readPLUItems.getString(readPLUItems.getColumnIndex("OrderName"));
                String totalQty = readPLUItems.getString(readPLUItems.getColumnIndex("TotalQty"));

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(orderID); // OrderID
                row.createCell(1).setCellValue(orderName); // OrderName
                row.createCell(2).setCellValue(Integer.parseInt(totalQty));    // TotalQty

                // Append to buffer if needed
                if (!OrderNameList.contains(orderID)) {
                    OrderNameList.add(orderID);
                    buffer.append(rowNum + "). " + orderName + "\n  SKU : (" + orderID + ") "
                            + " - Qty: " + totalQty + "\n\n");
                }
                finalQty+=Integer.parseInt(totalQty);
            } while (readPLUItems.moveToNext());

            Row underlineRow = sheet.getRow(rowNum-1); // Get the last created data row
            if (underlineRow != null) {
                CellStyle underlineStyle = workbook.createCellStyle();
                underlineStyle.setBorderBottom(BorderStyle.THIN); // Set bottom border to thin (underline)

                // Apply underline style to the last cell in the row (column index 2)
                Cell cell = underlineRow.getCell(2); // Get or create cell in column index 2
                if (cell == null) {
                    cell = underlineRow.createCell(2);
                }
                cell.setCellStyle(underlineStyle);
            }


            Row rowQty = sheet.createRow(rowNum);
            rowQty.createCell(2).setCellValue(finalQty);

        }

        // Auto-size columns (manually set column widths as autoSizeColumn won't work on Android)
        sheet.setColumnWidth(0, 5000); // Adjust as needed
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 4000);

        // Save the workbook to a file
        try {
            File directory = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/Item Report/");
            if (!directory.exists()) {
                directory.mkdirs(); // creates the directory including any necessary but nonexistent parent directories
            }

            String excelName;
            if (from.equalsIgnoreCase(to)){
                excelName = "ITEM SALES REPORT ("+to+").xlsx";
            }else{
                excelName = "ITEM SALES REPORT ("+from+" - "+to+").xlsx";
            }


            final File excelFile = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/Item Report/"+excelName);

            if (excelFile.exists()) {
                // File already exists, ask the user if they want to overwrite it
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("File Exists");
                builder.setMessage("Excel file already exists. Do you want to overwrite it?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User confirmed, delete existing file and regenerate
                        if (excelFile.delete()) {
                            // File deleted successfully, proceed with generating the new file
                            generateExcelFile(workbook, excelFile);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to delete existing file", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", null); // Do nothing on cancel

                builder.show();
            } else {
                // File does not exist, directly generate the file
                generateExcelFile(workbook, excelFile);
            }



        } finally {
            // Close resources
            if (readPLUItems != null) {
                readPLUItems.close();
            }
            PosOutputDB.close();
        }

        // Continue with your existing code...
    }

    private void generateExcelFile(Workbook workbook, File excelFile) {
        try {
            FileOutputStream fileOut = new FileOutputStream(excelFile);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            Toast.makeText(getApplicationContext(), "Excel file generated", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void tenderReport(View view){
        String Datefrom="2023-01-01";
        String DateTo="2023-12-30";
        StringBuffer buffer = new StringBuffer();


        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_check_sales_date, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        Button btn_submit = alertLayout.findViewById(R.id.btn_submit);

        DatePicker datePickerFrom = (DatePicker) alertLayout.findViewById(R.id.datePickerFrom);
        DatePicker datePickerTo= (DatePicker) alertLayout.findViewById(R.id.datePickerTo);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = datePickerFrom.getDayOfMonth();
                int month = datePickerFrom.getMonth() + 1;
                int year = datePickerFrom.getYear();

                int dayTo = datePickerTo.getDayOfMonth();
                int monthTo = datePickerTo.getMonth() + 1;
                int yearTo = datePickerTo.getYear();

                // Datefrom = year+"-"+month+"-"+day;
                //  DateTo=    yearTo+"-"+monthTo+"-"+dayTo;
                String sMonthFrom = String.valueOf(month);
                String sDayFrom = String.valueOf(day);

                String sMonthTo = String.valueOf(monthTo);
                String sDayTo = String.valueOf(dayTo);

                if (sMonthFrom.length()==1){
                    sMonthFrom = "0"+sMonthFrom;
                }

                if (sDayFrom.length()==1){
                    sDayFrom = "0"+sDayFrom;
                }


                if (sMonthTo.length()==1){
                    sMonthTo = "0"+sMonthTo;
                }

                if (sDayTo.length()==1){
                    sDayTo = "0"+sDayTo;
                }

                Log.d("Date From", year + "-" + month +"-"+ day );

                String finalFrom = String.valueOf(year)+"-"+sMonthFrom+"-"+sDayFrom;
                String finalTo = String.valueOf(yearTo)+"-"+sMonthTo+"-"+sDayTo;

                printTenderReport(finalFrom,finalTo);
                alertDialog.dismiss();

            }
        });

        alertDialog.show();





    }

    private void printTenderReport(String from,String to){

        StringBuffer buffer = new StringBuffer();
        Toast.makeText(getApplicationContext(), "Tender Report", Toast.LENGTH_SHORT).show();

        printer_settings_class PrinterSettings = new printer_settings_class(getApplicationContext());
        buffer.append("--------------------------------" + "\n");
        buffer.append("Tender Report                   " + "\n");
        buffer.append("from : " + from +"\n");
        buffer.append("to   : " + to + "\n");
        buffer.append("--------------------------------" + "\n");


        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor getTenderType = db.rawQuery("select Distinct typeOfPayment from InvoiceReceiptTotal where Date(TransDate) between '"+from+"' and '"+to+"' and typeOfPayment != ''", null);
        if (getTenderType.getCount()!=0){
            while(getTenderType.moveToNext()){
                Cursor getData = db.rawQuery("select Sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='"+getTenderType.getString(0)+"' and Date(TransDate) between '"+from+"' and '"+to+"'", null);
                if(getData.getCount()!=0){
                    if (getData.moveToFirst()){
                        buffer.append(getTenderType.getString(0) + "        " + getData.getString(0) + "\r\n");
                    }
                }
            }
        }

        PrinterSettings.OnlinePrinter(buffer.toString(),1,0,1); // cashier invoice

    }

    private void getDiscountList(){
        SQLiteDatabase db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor discountListID = db.rawQuery("select * from DiscountList", null);
        if (discountListID.getCount()!=0){
            discountListID.moveToLast();
                ID=discountListID.getInt(0)+1;


        }
        else{
            ID=1;
        }


//                itemListC = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
//                String totalItem = "0";
//                String totalSubtract;
//                Cursor itemListC2 = db2.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp", null);
    db.close();
    }
    ArrayList<String> BankList=new ArrayList<String>();





    private void getBankList(){
        BankList.clear();
        SQLiteDatabase db = this.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        Cursor BankID = db.rawQuery("select count(BankID) from BankDetails", null);
        Cursor BankName = db.rawQuery("select * from BankDetails", null);
        if (BankID.getCount()!=0){
            while (BankID.moveToNext()){
                ID=BankID.getInt(0)+1;
            }
            while ((BankName.moveToNext())){
                BankList.add(BankName.getString(1));
            }




        }
        else{
            ID=1;
        }


//                itemListC = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
//                String totalItem = "0";
//                String totalSubtract;
//                Cursor itemListC2 = db2.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp", null);
        db.close();
    }
    private void getOtherPaymentList(){
     BankList.clear();
        SQLiteDatabase db = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor BankID = db.rawQuery("select count(PaymentName) from OtherPayment", null);
        Cursor BankName = db.rawQuery("select * from OtherPayment", null);
        if (BankID.getCount()!=0){
            while (BankID.moveToNext()){
                ID=BankID.getInt(0)+1;
            }
            while ((BankName.moveToNext())){
                BankList.add(BankName.getString(1));
            }




        }
        else{
            ID=1;
        }


//                itemListC = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
//                String totalItem = "0";
//                String totalSubtract;
//                Cursor itemListC2 = db2.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp", null);
        db.close();
    }




    String DiscID;
    String DiscountAmount;
    String DiscountComputation;
    String DiscountExcludeTax;
    String DiscountType;
    String DiscLabel;
    String MinTransactionAmount;
    String MaxTransactionAmount;
    String MaxDiscountAmount;
    String SalesExcludeTax;


    List<discount_model> ArrayDiscountList = new ArrayList<>();


    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_pos_settings.RecyclerviewAdapter.MyViewHolder>{
        List<discount_model> ArrayDiscountList;
        Context context;
        ArrayList<String> selectList = new ArrayList<>();
        private admin_pos_settings.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<discount_model> ArrayDiscountList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.ArrayDiscountList = ArrayDiscountList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_pos_settings.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_discount_layout,parent,false);



            admin_pos_settings.RecyclerviewAdapter.MyViewHolder holder = new admin_pos_settings.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_pos_settings.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

//            holder.tv_No.setText(String.valueOf((AuditTrailList.get(position).getNo())));
//            holder.tv_TransactionNo.setText((String.valueOf(AuditTrailList.get(position).getTransactionNo())));
//            holder.tv_Activity.setText((String.valueOf(AuditTrailList.get(position).getActivity())));
//            holder.tv_User.setText((String.valueOf(AuditTrailList.get(position).getUser())));
//            holder.tv_Date.setText((String.valueOf(AuditTrailList.get(position).getDate())));
//            holder.tv_Time.setText((String.valueOf(AuditTrailList.get(position).getTime())));
//            holder.tv_UserShift.setText((String.valueOf(AuditTrailList.get(position).getDate())));

            holder.tv_category.setText(ArrayDiscountList.get(position).getDiscCategory());
            holder.tv_discID.setText(ArrayDiscountList.get(position).getDiscountID());
            holder.tv_desc.setText(ArrayDiscountList.get(position).getDiscountName());
            holder.FinalDiscID=ArrayDiscountList.get(position).getID();
            holder.FinalDiscountAmount=ArrayDiscountList.get(position).getDiscountAmount();
            holder.FinalDiscountComputation=ArrayDiscountList.get(position).getDiscountComputation();
            holder.FinalDiscountExcludeTax=ArrayDiscountList.get(position).getDiscountExcludeTax();
            holder.FinalDiscountType=ArrayDiscountList.get(position).getDiscountType();
            holder.FinalDiscLabel=ArrayDiscountList.get(position).getDiscLabel();
            holder.FinalMinTransactionAmount=ArrayDiscountList.get(position).getMinTransactionAmount();
            holder. FinalMaxTransactionAmount=ArrayDiscountList.get(position).getMaxTransactionAmount();
            holder.FinalMaxDiscountAmount=ArrayDiscountList.get(position).getMaxDiscountAmount();
            holder.FinalSalesExcludeTax=ArrayDiscountList.get(position).getSalesExcludeTax();














        }

        @Override
        public int getItemCount() {
            return ArrayDiscountList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_category;
            TextView tv_discID;
            TextView tv_desc;
            LinearLayout ll_discountSelect;

            private String FinalDiscID;
            private String FinalDiscountAmount;
            private String FinalDiscountComputation;
            private String FinalDiscountExcludeTax;
            private String FinalDiscountType;
            private String FinalDiscLabel;
            private String FinalMinTransactionAmount;
            private String FinalMaxTransactionAmount;
            private String FinalMaxDiscountAmount;
            private String FinalSalesExcludeTax;




            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_category = itemView.findViewById(R.id.tv_category);
                tv_discID = itemView.findViewById(R.id.tv_discID);
                tv_desc = itemView.findViewById(R.id.tv_desc);
                ll_discountSelect=itemView.findViewById(R.id.ll_discountSelect);

//             // String DiscID2="";
//                DiscountAmount="";
//                DiscountComputation="";
//                DiscountExcludeTax="";
//                DiscountType="";
//                DiscLabel="";
//                MinTransactionAmount="";
//                MaxTransactionAmount="";
//                MaxDiscountAmount="";
//                SalesExcludeTax="";



                ll_discountSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_discID.setText(tv_discID.getText().toString());
                        DiscID=FinalDiscID;
                        DiscountAmount= FinalDiscountAmount;
                        DiscountComputation=FinalDiscountComputation;
                        DiscountExcludeTax=FinalDiscountExcludeTax;
                        DiscountType=FinalDiscountType;
                        DiscLabel=FinalDiscLabel;
                        MinTransactionAmount=FinalMinTransactionAmount;
                        MaxTransactionAmount=FinalMaxTransactionAmount;
                        MaxDiscountAmount=FinalMaxDiscountAmount;
                        SalesExcludeTax=FinalSalesExcludeTax;





                        Log.e("CATEGORY",tv_discID.getText().toString());
                       Log.e("DiscID",DiscID);
                    }
                });



                // parentLayout = itemView.findViewById(R.id.linear_orderlist_layout2);



//                tv_itemText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e("item id",tv_itemID.getText().toString());
//                        deleteID=Integer.parseInt(tv_itemID.getText().toString());
//                        updateItem();
//
//
//                    }
//                });



            }
        }
    }

    private void createDirectory(){
        //String folder_main = "NewFolderss";
        boolean success = true;
        File file =  new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS");
        File DATABASE=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/DATABASE");
        File sendFile=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/SEND FILE");
        File receiptFile=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/RECEIPT FILE");
        File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal");
        File generatedEJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Generated E-Journal");
        if (!file.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();
            file.mkdir();
        }


        if (!sendFile.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, sendFile.toString(), Toast.LENGTH_SHORT).show();
            sendFile.mkdir();
        }

        if (!receiptFile.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, receiptFile.toString(), Toast.LENGTH_SHORT).show();
            receiptFile.mkdir();
        }


        if (!eJournal.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, eJournal.toString(), Toast.LENGTH_SHORT).show();
            eJournal.mkdir();
        }

        if (!generatedEJournal.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, generatedEJournal.toString(), Toast.LENGTH_SHORT).show();
            generatedEJournal.mkdir();
        }



        if (!DATABASE.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();
            DATABASE.mkdir();
        }
        if (success){

        }
        else
        {
            Toast.makeText(this, "Failed error", Toast.LENGTH_SHORT).show();
        }





    }





}