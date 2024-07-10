package com.abztrakinc.ABZPOS.CASHIER;




import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_discount_model;
import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_fragment_button_model;
import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_plu_model;
import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_transaction_model;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.InvoiceItem;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.MainActivity;
import com.abztrakinc.ABZPOS.MyKeyboard;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.POJO.Invoice;
import com.abztrakinc.ABZPOS.POSAndroidDB;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.ORDERSTATION.orderItem;
import com.abztrakinc.ABZPOS.ORDERSTATION.orderItemDiscount;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.TCSKeyboard.SoftKeyboard;
import com.abztrakinc.ABZPOS.system_final_date;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class cashier_invoice extends Fragment {



    //Arraylist for DISCOUNT


    private ArrayList<String> DiscountAutoIDList;
    private ArrayList<String> DiscountIDList;
    private ArrayList<String> DiscountNameList;
    private ArrayList<String> DiscountAmountList;
    private ArrayList<String> DiscountComputationList;
    private ArrayList<String> DiscountExcludeTaxList;
    private ArrayList<String> ProRatedTaxList;
    String ProRated;
    private ArrayList<String> DiscountTypeList;
    private ArrayList<String> DiscountCategoryList;
    private ArrayList<String> OpenDiscountList;


    //

    private ArrayList<String> itemCode;
    private ArrayList<String> itemName;
    private ArrayList<String> recptDesc;
    private ArrayList<String> itemPrice;
    private ArrayList<String> itemQuantity;
    private ArrayList<String> itemVatIndicator;
    private ArrayList<String> priceOverride;

    //for ordered invoice item list

    private ArrayList<String> invoiceItemName;
    private ArrayList<String> invoiceItemCode;
    private ArrayList<Integer> invoiceItemPrice;
    private ArrayList<String> invoiceItemPriceTotal;
    private ArrayList<String> invoiceItemQty;
    private ArrayList<String> invoiceRemarks;
    private ArrayList<String> invoiceVatIndicator;


    //for invoice item list

    private ArrayList<String> invoiceNameList;
    private ArrayList<String> invoicePriceList;
    private ArrayList<String> invoiceQtyAvailable;
    private ArrayList<String> invoiceItemPriceTotalDisc;
    private ArrayList<String> invoiceItemQtyDisc;


    //Transaction List Reprint
    private ArrayList<String> TransactionList;
        ArrayList<String> transactionType;




    List<orderItem> orderItemList = new ArrayList<>();
    List<InvoiceItem> InvoiceItemList = new ArrayList<>();

    List<orderItemDiscount> orderItemListDisc = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private RecyclerView recyclerView2;
    private RecyclerView.Adapter mAdapter2;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;


    ArrayList<String> invoiceSelectedItem = new ArrayList<>();

    String DB_NAME = "POSAndroidDB.db";
    String DB_NAME2 = "PosOutputDB.db";
    String DB_NAME3 = "settings.db";
    String DB_NAME4 = "PosSettings.db";
    SQLiteDatabase db;
    SQLiteDatabase db2;
    OR_TRANS_ITEM or_trans_item;
    DatabaseHandler myDb;
    Cursor itemListC;
    Cursor itemListCDiscount;
    String order_ID;
    String order_Name;
    String order_Qty;
    String order_Price;
    String order_PriceTotal;
    String transaction_ID;
    String transaction_Time;
   // String transaction_Date;
    String discount_type;
    String item_remarks;
    String DiscAmount;
    String DiscPercent;
    String FinalItemQty="1";
    int totalAmountDue=0;

    int ItemCursor;
    String readRefNumber;
    View view;
    TextView totAmountDue;
    TextView tv_totalQty;
    ImageButton btn_cashier_delete;
    ImageButton btn_delete;
    Button btn_regDiscount,btn_remarks,btn_insert,btn_deleteRemarks,btn_scDiscount,btn_cancel,btn_cashierFunction,btn_diplomatPayment,btn_managerFunction;
    LinearLayout ll_checkout;
    Button btn_addItem;
    String spinnerSelected;
    EditText etProductID,et_remarks,et_scanItem;

    MyKeyboard keyboard;

    private TextView dateTimeDisplay;
    private Calendar calendar;
   // private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat2;
   // private String date,time;
    EditText et_discQty;
    EditText et_discAmount;
    int openFragment=0;
    DecimalFormat Finalformat = new DecimalFormat("0.00");
    cashier_invoice_dialog_cust_info CustInfo = new cashier_invoice_dialog_cust_info();
    TextView tv_date,tv_transNumber,tv_userID,tv_posNumber,tv_shift;
    BroadcastReceiver AttachReceiver;

    BroadcastReceiver detachReceiver;
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    ImageButton ib_back,ib_forward;
    FrameLayout fl_next,fl_back;


    //variables for accessing item keypad layers
    int layer=1;  //layer 1 first layer
    TextView tv_keypadLayer;
    vat_indicator vatIndicatorclass;


    int i;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        fillOrderList();
        fillInvoiceItemList();
        closeKeyboard();

        getAttachedUSB();
        getDetachedUSB();
        formatter.applyPattern("###,###,###.###");

       // posAndroidDB.onUpgrade();







      //  view= inflater.inflate(R.layout.fragment_cashier_invoice2, container, false);
        view= inflater.inflate(R.layout.cashier_invoice_main, container, false);
        vatIndicatorclass = new vat_indicator();


        shift_active shift_active=new shift_active();
        shift_active.getShiftingTable(getContext());
        shift_active.getAccountInfo(getContext());
        system_final_date SystemSettings=new system_final_date();
        SystemSettings.insertDate(getContext());
        OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
        or_trans_item.readOfficialReceiptNumber(getContext());
        or_trans_item.readReferenceNumber(getContext());

        totAmountDue =  view.findViewById(R.id.totAmountDue);
        tv_totalQty =  view.findViewById(R.id.tv_totalQty) ;
        btn_cashier_delete = view.findViewById(R.id.btn_delete);
        btn_cashierFunction = view.findViewById(R.id.btn_cashierFunction);
        et_scanItem = view.findViewById(R.id.et_scanItem);
        et_scanItem.requestFocus();
        btn_diplomatPayment=view.findViewById(R.id.btn_diplomat);
        btn_cancel=view.findViewById(R.id.btn_cancel);

        _tvTime=view.findViewById(R.id.tv_time);
        _tvIndicator=view.findViewById(R.id.tv_indicator);
        tv_date=view.findViewById(R.id.tv_date);
        tv_transNumber=view.findViewById(R.id.tv_transNumber);
        tv_userID=view.findViewById(R.id.tv_userID);
        tv_posNumber=view.findViewById(R.id.tv_posNumber);
        tv_shift=view.findViewById(R.id.tv_shift);
        ib_back = view.findViewById(R.id.ib_back);
        ib_forward = view.findViewById(R.id.ib_forward);
        fl_back =view.findViewById(R.id.fl_back);
        fl_next=view.findViewById(R.id.fl_next);
        tv_keypadLayer=view.findViewById(R.id.tv_keypadLayer);


        tv_date.setText(SystemSettings.getSystemDate());

        tv_transNumber.setText("Trans # " + or_trans_item.getTransactionNo());
        _tvTime.setText(_sdfWatchTime.format(new Date()));



        tv_userID.setText("USER : "+shift_active.getActiveUserID() + "/ " + shift_active.getActiveUserName());
        tv_posNumber.setText("POS : " + shift_active.getPOSCounter());
        tv_shift.setText("Shift Active : " +shift_active.getShiftActive());
        ll_checkout=view.findViewById(R.id.ll_checkout);





       // LoadReserveButton(view);





        et_scanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_scanItem.requestFocus();
                closeKeyboard();
            }
        });



        et_scanItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
               // et_scanItem.getText().clear();
                if ((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && (i== keyEvent.KEYCODE_ENTER)){
                   // Toast.makeText(getContext(), et_scanItem.getText().toString(), Toast.LENGTH_SHORT).show();

                 //   Toast.makeText(getContext(), et_scanItem.getText().toString(), Toast.LENGTH_SHORT).show();
                    insertItemCode();

//
                   // et_scanItem.requestFocus();
//
                    et_scanItem.getText().clear();
//                    closeKeyboard();
                    return true;

                }
                return false;
            }
        });


        ImageButton img_SubtractItem = view.findViewById(R.id.img_SubtractItem);
        ImageButton btn_cashier_add = view.findViewById(R.id.btn_cashier_add);

        btn_cashier_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToInvoice();
            }
        });
        btn_scDiscount = view.findViewById(R.id.btn_scDiscount);
        btn_managerFunction = view.findViewById(R.id.btn_ManagerFunction);


        btn_cashier_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                deleteFinalReceipt();


            }
        });
        img_SubtractItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubtractFromInvoice();
            }
        });





        btn_cashierFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showCashierFunction();

                showingCashierList();



            }
        });
//        btn_remarks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());
//                //RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,selectList,getActivity());
//                if (recyclerviewAdapter.selectList.size()==0){
//                    Toast.makeText(getActivity(), "PLEASE SELECT ORDERED ITEM", Toast.LENGTH_SHORT).show();
//                }else{
//                    showRemarksDialog();
//                }
//
//            }
//        });
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
//                String strsql3 = "Delete From InvoiceReceiptItemFinalWDiscountTemp";
//                db2.execSQL(strsql3);
//                db2.close();
//            }
//        });
//        btn_regDiscount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDiscount();
//            }
//        });

        btn_scDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showSCDiscount();

                if (openFragment==0){
                    showingDiscountList();
                    openFragment=1;
                }
                else{
                  //  showingItem();
                    openFragment=0;
                }

            }
        });


        btn_managerFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showSCDiscount();


                    showingManagerList();



            }
        });



//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                RecyclerviewAdapterInvoice recyclerviewAdapter = new RecyclerviewAdapterInvoice(InvoiceItemList,invoiceSelectedItem,getActivity());
////                Toast.makeText(getActivity(), recyclerviewAdapter.invoiceSelectedItem.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

//Button for diplomat

        btn_diplomatPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discountType="DIPLOMAT";
                discountValue="0.00";
                discountExlude="YES";
                discountComputation();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_cancelFunction();


            }
        });

        ll_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "checkout", Toast.LENGTH_SHORT).show();
                checkOut();

            }
        });

      // showingItem("D01");

        checkTotalAmount();
        refreshRecycleview();
        KeyBoardMap();
        getUSB();
        rv_pluList = view.findViewById(R.id.rv_pluList);
        showingAllItem();
     //   showingDeptKeys();
        keypadLayer(1);



        return view;



    }


    private void keypadLayer(int layer){
        tv_keypadLayer.setText("ITEM KEYPAD LAYER : "+String.valueOf(layer));
    }

    private void loadFirstDept(){
        try{
            if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                //note : when master list is shown
                alertDialog.dismiss();
                MainFunctionCursor=1;
            }}
        catch (Exception ex){

        }
        Log.e("TAG", "SimulateKeyboard: "+"DO2" );
        showingItem("D01",layer);
    }

    int cancelFuncKeyb;
    private void btn_cancelFunction(){
        cancelFuncKeyb=1;
        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_exit_confirmation, null);
        builder.setView(alertLayout);
        alertDialog = builder.create();

        Button btn_logout = alertLayout.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  onBackPressed();

                //  kboard.UnInit();
                getActivity().finish();
                cancelFuncKeyb=0;
                // onBackPressed();

            }
        });

        Button btn_exitPOS = alertLayout.findViewById(R.id.btn_exitPOS);
        btn_exitPOS.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                cancelFuncKeyb=0;



            }
        });



        alertDialog.show();
    }


    private void checkOut(){
        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
        buttoninv.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonpayment.setBackgroundColor(getResources().getColor(R.color.Python));
        buttonshift.setBackgroundColor(getResources().getColor(R.color.gray));

        replaceFragment(new cashier_payment());

    }
    private void shift(){
        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
        buttoninv.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonpayment.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonshift.setBackgroundColor(getResources().getColor(R.color.Python));

        replaceFragment(new cashier_shift());

    }

    private void invoice(){
        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
        buttoninv.setBackgroundColor(getResources().getColor(R.color.Python));
        buttonpayment.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonshift.setBackgroundColor(getResources().getColor(R.color.gray));

        replaceFragment(new cashier_invoice());

    }






  private void getAttachedUSB(){



      AttachReceiver = new BroadcastReceiver() {
          public void onReceive(Context context, Intent intent) {
              if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED))
                  getUSB();
              // stopSelf();
          }
      };

      IntentFilter filter2 = new IntentFilter();
      filter2.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
      getActivity().registerReceiver(AttachReceiver, filter2);




  }
  private void getDetachedUSB(){
      detachReceiver = new BroadcastReceiver() {
          public void onReceive(Context context, Intent intent) {
              if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED))
                  getUSB();
              // stopSelf();
          }
      };

      IntentFilter filter = new IntentFilter();
      filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
      getActivity().registerReceiver(detachReceiver, filter);
  }

  private void getUSB(){
      UsbManager usbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
      Collection<UsbDevice> dList = usbManager.getDeviceList().values();
      ArrayList<UsbDevice> deviceList = new ArrayList<>();
      ArrayList<String> deviceListVendorID = new ArrayList<>();
      deviceList.addAll(dList);

      for (int i = 0; i < deviceList.size(); i++) {
          //storages.add(new Storage(drives.get(i), deviceList.get(i), Storage.USB_DRIVE));
          Log.e("DEVICE LIST",String.valueOf(deviceList.get(i).getVendorId()));
          deviceListVendorID.add(String.valueOf(deviceList.get(i).getVendorId()));
          if (deviceListVendorID.contains("1921")){
              _tvIndicator.setBackgroundColor(Color.parseColor("#32cd32"));
              _tvIndicator.setText("ONLINE");
          }
          else{
              _tvIndicator.setBackgroundColor(Color.parseColor("#cd5c5c"));
              _tvIndicator.setText("OFFLINE");
          }
      }
  }

    BroadcastReceiver _broadcastReceiver;

    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("hh:mm aa");
    TextView _tvTime;
    TextView _tvIndicator;

    IntentFilter filter = new IntentFilter();




    @Override
    public void onStart() {
        super.onStart();



        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    _tvTime.setText(_sdfWatchTime.format(new Date()));
                  //  getUSB();
                }

            }
        };
   getActivity().registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));


//        filter.addAction("android.hardware.usb.action.USB_STATE");
//        _broadcastReceiver2 = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context ctx, Intent intent) {
//              //  if (intent.getExtras().getBoolean("connected")) {
//                    //do your stuff
//                    getUSB();
//                //}
//
//            }
//        };
//
//        getActivity().registerReceiver(_broadcastReceiver2,new IntentFilter(filter));




    }


    @Override
    public void onStop() {
        super.onStop();
        if (_broadcastReceiver != null)
            getActivity().unregisterReceiver(_broadcastReceiver);
  //     kboard.UnInit();
        Log.d("onUserLeaveHint","Home button pressed");

        //getActivity().unregisterReceiver(AttachReceiver);
      //  getActivity().unregisterReceiver(detachReceiver);




//        super.onUserLeaveHint();
    }

    @Override
    public void onPause() {
        super.onPause();
        //if (_broadcastReceiver != null)
          //  getActivity().unregisterReceiver(_broadcastReceiver);
        kboard.UnInit();
        Log.d("onUserLeaveHint", "FRAGMENT LEAVE");


//        super
//
//        .onUserLeaveHint();
    }




    KeyboardDevice kboard;
    KeyCodeManager kManager;



    @Override
    public void onDestroy() {
       // KeyboardDevice.UnInit();
       // kboard.UnInit();
        super.onDestroy();
    }

    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;


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


                                if (InvoiceCursor==5 || InvoiceCursor==6){

                                    Log.e("setNewKeyEvent", "proceed");
                                    setNewKeyEvent(nKeyIndex);

                                    //  SimulateKeyboard(nKeyIndex);
                                }
                                else{
                                    SimulateKeyboard(nKeyIndex);
                                }

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





    private void RouteKeyIndex(int nKeyIndex) {

        int nVKCode = KeyCodeManager.getVKCode(nKeyIndex);
        String nVKCode2 = KeyCodeManager.getDefaultKeyName(nKeyIndex);
        if (nVKCode == -100) {
        } else {
            Log.e("nVKCode",String.valueOf(nVKCode));
            SimulateKeyboard(nVKCode);

            if (nVKCode==123){
                if (et_scanItem.hasFocus()){
                    et_scanItem.setText("");

                }

            }
            if (nVKCode==203){
                showDialogMasterlist(1);
            }


        }



//        if (nVKCode==131) {  //131 for calling invoice
//            //replaceFragment(new cashier_invoice());
//          //  Log.e("new FragInv","1");
//
////            cashierInvoiceFragment.setBackgroundColor(Color.parseColor("#32cd32"));
////            cashierPaymentFragment.setBackgroundColor(Color.parseColor("#cd5c5c"));
////            cashierShiftFragment.setBackgroundColor(Color.parseColor("#cd5c5c"));
//
//
//
//        }
//        if (nVKCode==132) {  //131 for calling invoice
//
//
//            replaceFragment(new cashier_payment());
//
//        }
//        if (nVKCode==133) {  //131 for calling invoice
//            replaceFragment(new cashier_shift());
//            Log.e("new FragInv","3");
//        }

        if (nVKCode>=188 && nVKCode<=201){
            //String nvkString="D01"
            db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            Cursor checkItemCode = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='" + nVKCode2 + "'", null);
            if (checkItemCode.getCount() != 0) {
                while (checkItemCode.moveToNext()) {

                    Log.e("shortCutKeys",checkItemCode.getString(0));
                    Log.e("shortCutID",checkItemCode.getString(1));

                    Cursor checkItemCode2 = db.rawQuery("select * from ITEM where ItemID='" + checkItemCode.getString(1) + "'", null);
//                    ItemCursor = itemCode.indexOf(checkItemCode.getString(0));
                    if (checkItemCode2.getCount()!=0){
                        while (checkItemCode2.moveToNext()){
                            ItemCursor = itemCode.indexOf(checkItemCode2.getString(0));
                            Log.e("INPUT",et_scanItem.getText().toString());
                            InsertItemBcode2();
                        }
                    }
//
                    //ItemCursor=1000000;
                }
            }

            db.close(); // test
        }
        if (nVKCode==202){
            printer_settings_class prn = new printer_settings_class(this.getContext());
            prn.PaperFeed();

        }


    }

    private void replaceFragment(Fragment fragment){


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment).commitNow();



    }

    //keyboard simulation
    int xCursor=0;
    int MainFunctionCursor=0;
    int MainButtonCursor=0;
    private void SimulateKeyboard(int keyCode) {


        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;





        String[] allowedInput = {"0","1","2","3","4","5","6","7","8","9","00","PLU","Btn","."};
//        if (et_scanItem.getText().toString().equalsIgnoreCase("PLU")){
//            et_scanItem.setText("PLU");
//        }


        for (String element : allowedInput){
            if (element ==  input){


//                if (input.equals(".")){
//                    input="-";
//                }


                if (alertDialogDiscount!=null && alertDialogDiscount.isShowing()){

                    int start = Math.max(et_quantity.getSelectionStart(), 0);
                    int end = Math.max(et_quantity.getSelectionEnd(), 0);
                    et_quantity.getText().replace(Math.min(start, end), Math.max(start, end),
                            input, 0, input.length());


                }
                else {


                    if (InvoiceCursor == 0) {
                        if (et_scanItem.hasFocus()) {
                            if (et_scanItem.hasFocus()) {

//            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
//            KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
//
//            editTextTextPersonName.onKeyDown(keyCode, keyEventDown);
//            editTextTextPersonName.onKeyUp(keyCode, keyEventUp);


                                int start = Math.max(et_scanItem.getSelectionStart(), 0);
                                int end = Math.max(et_scanItem.getSelectionEnd(), 0);
                                et_scanItem.getText().replace(Math.min(start, end), Math.max(start, end),
                                        input, 0, input.length());


                                if (et_scanItem.length() == 0) {
                                    return;
                                }
                            }


                        }
                    } else if (InvoiceCursor == 1 || InvoiceCursor == 101 || InvoiceCursor == 102) { // inputting qty


                        int start = Math.max(et_quantity.getSelectionStart(), 0);
                        int end = Math.max(et_quantity.getSelectionEnd(), 0);
                        et_quantity.getText().replace(Math.min(start, end), Math.max(start, end),
                                input, 0, input.length());


                        if (et_quantity.length() == 0) {
                            return;
                        }


                    } else if (InvoiceCursor == 2) {
                        int start = Math.max(et_command.getSelectionStart(), 0);
                        int end = Math.max(et_command.getSelectionEnd(), 0);
                        et_command.getText().replace(Math.min(start, end), Math.max(start, end),
                                input, 0, input.length());


                        if (et_command.length() == 0) {
                            return;
                        }

                    } else if (InvoiceCursor == 3) {
                        int start = Math.max(et_command.getSelectionStart(), 0);
                        int end = Math.max(et_command.getSelectionEnd(), 0);
                        et_command.getText().replace(Math.min(start, end), Math.max(start, end),
                                input, 0, input.length());


                        if (et_command.length() == 0) {
                            return;
                        }

                    } else if (InvoiceCursor == 4) {
                        int start = Math.max(et_ReprintQty.getSelectionStart(), 0);
                        int end = Math.max(et_ReprintQty.getSelectionEnd(), 0);
                        et_ReprintQty.getText().replace(Math.min(start, end), Math.max(start, end),
                                input, 0, input.length());


                        if (et_ReprintQty.length() == 0) {
                            return;
                        }

                    } else if (InvoiceCursor == 5) {

                    }

                }



            }

        }
        int PLUNumber=0;
        int BtnNumber=0;

        if (input.equalsIgnoreCase("Total")){


            if (alertDialogDiscount!=null && alertDialogDiscount.isShowing()){


                discountComputationOpenPrice(et_quantity.getText().toString());

//                alertDialog.dismiss();
                alertDialogDiscount.dismiss();
                invoice();

            }
            else {


                if (et_scanItem.length() != 0 && !et_scanItem.getText().toString().contains("PLU") && !et_scanItem.getText().toString().contains("Btn")) {
//                Toast.makeText(getActivity(), "PLU1"+et_scanItem.getText().toString(), Toast.LENGTH_SHORT).show();
                    insertItemCode();
                } else if (et_scanItem.getText().toString().contains("PLU")) {
                    if (PLUList.size() != 0) {
                        // Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                        String str = removeWords(et_scanItem.getText().toString().trim(), "PLU");
                        et_scanItem.setText("");
                        PLUNumber = Integer.parseInt(str);

                        Log.d("Total ", "PLU NUmber " + String.valueOf(PLUNumber) + "  " + "PLU MAX LIST " + String.valueOf(PLUList.size()));

                        if (PLUNumber > PLUList.size()) {
                            Toast.makeText(getActivity(), "PLU" + String.valueOf(PLUNumber) + " OUT OF RANGE", Toast.LENGTH_SHORT).show();
                        } else {
                            if (priceOverride.get(PLUNumber - 1).equalsIgnoreCase("YES")) {
                                Toast.makeText(getActivity(), "OPEN PRICE " + PLUNumber, Toast.LENGTH_SHORT).show();
                                openPrice("PLU" + String.valueOf(PLUNumber));
                            } else {
                                Toast.makeText(getActivity(), "FIX PRICE " + PLUNumber, Toast.LENGTH_SHORT).show();
                                openQtyDialog("PLU" + String.valueOf(PLUNumber));

                            }
                            // insertPLU(PLUNumber);
                        }
//
//
                    } else {
                        Toast.makeText(getActivity(), "PLU is out of Range", Toast.LENGTH_SHORT).show();
                    }
                    et_scanItem.setText("");

                } else if (et_scanItem.getText().toString().contains("Btn")) { //1 for CashierFunction //2 for ManagerFunction
//                if (PLUList.size()!=0) {
                    Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "Btn"), Toast.LENGTH_SHORT).show();
                    String str = removeWords(et_scanItem.getText().toString().trim(), "Btn");
                    et_scanItem.setText("");
                    BtnNumber = Integer.parseInt(str);
//
//                    Log.d("Total ", "PLU NUmber "+ String.valueOf(PLUNumber) + "  " + "PLU MAX LIST "+String.valueOf( PLUList.size()) );
//
//                    if (PLUNumber>PLUList.size()){
//                        Toast.makeText(getActivity(), "PLU"+String.valueOf(PLUNumber) + " OUT OF RANGE", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        openQtyDialog("PLU"+String.valueOf(PLUNumber));
//                        // insertPLU(PLUNumber);
//                    }
////
////
//                }
//                else{
//                    Toast.makeText(getActivity(), "PLU is out of Range", Toast.LENGTH_SHORT).show();
//                }


                    if (BtnNumber == 11) {
                        showingCashierList();
                        MainFunctionCursor = 1;

                    } else if (BtnNumber == 12) {
                        showingManagerList();
                        MainFunctionCursor = 2;
                    } else if (BtnNumber == 13) {
                        showingDiscountList();
                        MainFunctionCursor = 3;
                    } else if (BtnNumber == 14) { // diplomat
                        discountType = "DIPLOMAT";
                        discountValue = "0.00";
                        discountExlude = "YES";
                        discountComputation();
                        // MainFunctionCursor=3;

                    } else if (BtnNumber == 15) { // diplomat
                        btn_cancelFunction();
                    } else if (BtnNumber == 8) {
                        if (reserveButton1.getText().toString().trim().length() == 0) {
                            addReserveDialog(DeptNameUni + "reserve01");
                        } else {
                            addInvoiceReserve(reserveItemcodeList.get(0).toString(), reserveItemNameList.get(0), reserveItemPriceList.get(0), "1");
                        }


                    } else if (BtnNumber == 9) {
                        if (reserveButton2.getText().toString().trim().length() == 0) {
                            addReserveDialog(DeptNameUni + "reserve02");
                        } else {
                            addInvoiceReserve(reserveItemcodeList.get(1).toString(), reserveItemNameList.get(1), reserveItemPriceList.get(1), "1");
                        }


                    } else if (BtnNumber == 10) {
                        if (reserveButton3.getText().toString().trim().length() == 0) {
                            addReserveDialog(DeptNameUni + "reserve03");
                        } else {
                            addInvoiceReserve(reserveItemcodeList.get(2).toString(), reserveItemNameList.get(2), reserveItemPriceList.get(2), "1");
                        }


                    }


                    if (MainFunctionCursor == 1) {
                        Log.d("TAG", "MainFunctionCashier: 1");
                        if (BtnNumber == 2) {
                            String s = "SEARCH ITEM";

                            ShowCashierFunctionButton(s);
                        }
                        if (BtnNumber == 1) {
                            String s = "ALL ITEM";
                            ShowCashierFunctionButton(s);
                        }
                        if (BtnNumber == 3) {
                            String s = "SUSPEND TRANS";
                            ShowCashierFunctionButton(s);
                        }
                        if (BtnNumber == 4) {
                            String s = "RESUME TRANS";
                            ShowCashierFunctionButton(s);
                        }
                    }
                    if (MainFunctionCursor == 2) {



                        if (BtnNumber == 1) {
                            String s = "REPRINT";

                            ShowManagerFunctionButton(s);
                        }
                        if (BtnNumber == 2) {
                            String s = "RETURN AND EXCHANGE";
                            ShowManagerFunctionButton(s);
                        }
                        if (BtnNumber == 3) {
                            String s = "REFUND";
                            ShowManagerFunctionButton(s);
                        }
                        if (BtnNumber == 4) {
                            String s = "SUSPEND TRANSACTION";
                            ShowManagerFunctionButton(s);
                        }
                        if (BtnNumber == 5) {
                            String s = "RESUME TRANSACTION";
                            ShowManagerFunctionButton(s);
                        }
                    }
                    if (MainFunctionCursor == 3) {


                        discountType = DiscountIDList.get(BtnNumber - 1);
                        discountValue = DiscountAmountList.get(BtnNumber - 1);
                        discountExlude = DiscountExcludeTaxList.get(BtnNumber - 1);
                        ProRated = ProRatedTaxList.get(BtnNumber - 1);
                        discountCategory = DiscountCategoryList.get(BtnNumber - 1);


                        //  CustInfo.showDialog(getContext());


                        Log.e("DISCOUNT TYPE SUBS", discountType.substring(0, 3));

//                            alertDialog.dismiss();

                        //
                        if (discountCategory.equals("SCD") || discountCategory.equals("PWD")) {
                            showDialog();
                        } else {
                            discountComputation();
                        }


                    }


//                if (s.equalsIgnoreCase("REPRINT")){
//                    Log.d("TEST","REPRINT");
//                    showDialogReprint();
//                }
//                if (s.equalsIgnoreCase("RETURN AND EXCHANGE")){
//                    Log.d("TEST","RETURN AND EXCHANGE");
//
//                    showDialogReturnExchange();
//
//
//                }
//                if (s.equalsIgnoreCase("REFUND")){
//                    Log.d("TEST","REFUND");
//                }
//                if (s.equalsIgnoreCase("SUSPEND TRANSACTION")){
//                    Log.d("TEST","SUSPEND ");
//                    SuspendTransaction();
//                }
//                if (s.equalsIgnoreCase("RESUME TRANSACTION")){
//                    Log.d("TEST"," RESUME");
//                    SelectResumeTransaction();
//                }


                    Log.d("TAG", "MainFunctionCashier:" + MainFunctionCursor);

                    et_scanItem.setText("");

                } else if (InvoiceCursor == 1) {

                    int itemQty = 1;
                    String quantityText = et_quantity.getText().toString();
                    if (!quantityText.matches("")) {
                        FinalItemQty = et_quantity.getText().toString().trim();
                        Log.e("ItemQty", "NOT EMPTY" + et_quantity.getText().toString());
                    } else {
                        FinalItemQty = String.valueOf(itemQty);
                        Log.e("ItemQty", "EMPTY");
                    }
                    //FinalItemQty = et_quantity.getText().toString().trim();
                    //   ItemCursor = child.getId();

                    //   insertPLU();


                    if (PLUList.size() != 0) {
                        Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                        String str = removeWords(FinalButtonPrefix, "PLU");
                        // et_scanItem.setText("");
                        // insertPLU(Integer.parseInt(str));

                        if (PLUList.size() != 0) {
                            ItemCursor = Integer.parseInt(str) - 1;
                            // FinalItemQty = "1";
                            addInvoice();
                        }

                    }


//                            addInvoice();
                    FinalItemQty = "";
                    alertDialogQty.dismiss();
                    InvoiceCursor = 0;


                } else if (InvoiceCursor == 101) {

                    int itemQty = 0;
                    String quantityText = et_quantity.getText().toString();
                    if (!quantityText.matches("")) {
                        FinalItemQty = et_quantity.getText().toString().trim();
                        Log.e("ItemQty", "NOT EMPTY" + et_quantity.getText().toString());

                        if (PLUList.size() != 0) {
                            Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                            String str = removeWords(FinalButtonPrefix, "PLU");
                            // et_scanItem.setText("");
                            // insertPLU(Integer.parseInt(str));

                            if (PLUList.size() != 0) {
                                ItemCursor = Integer.parseInt(str) - 1;
                                // FinalItemQty = "1";
                                //  addInvoice();
                            }

                        }

                        // Toast.makeText(getActivity(), "open price? or price Quanity", Toast.LENGTH_SHORT).show();


                        alertDialogQty.dismiss();
                        openPriceQty(FinalButtonPrefix, et_quantity.getText().toString());
                        FinalItemQty = "";
                        InvoiceCursor = 102;


                    } else {
                        FinalItemQty = String.valueOf(itemQty);
                        Toast.makeText(getContext(), "PLEASE INPUT AMOUNT", Toast.LENGTH_SHORT).show();
                        Log.e("ItemQty", "EMPTY");
                    }
                    //FinalItemQty = et_quantity.getText().toString().trim();
                    //   ItemCursor = child.getId();

                    //   insertPLU();


                } else if (InvoiceCursor == 102) {

                    int itemQty = 1;
                    String quantityText = et_quantity.getText().toString();
                    if (!quantityText.matches("")) {
                        FinalItemQty = et_quantity.getText().toString().trim();
                        Log.e("ItemQty", "NOT EMPTY" + et_quantity.getText().toString());
                    } else {
                        FinalItemQty = String.valueOf(itemQty);
                        Log.e("ItemQty", "EMPTY");
                    }
                    //FinalItemQty = et_quantity.getText().toString().trim();
                    //   ItemCursor = child.getId();

                    //   insertPLU();


                    if (PLUList.size() != 0) {
                        Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                        String str = removeWords(FinalButtonPrefix, "PLU");
                        // et_scanItem.setText("");
                        // insertPLU(Integer.parseInt(str));

                        if (PLUList.size() != 0) {
                            ItemCursor = Integer.parseInt(str) - 1;
                            // FinalItemQty = "1";
                            addInvoiceOpenPrice(FinalOpenAmount, (FinalItemQty));
                        }

                    }


//                            addInvoice();
                    FinalItemQty = "";
                    alertDialogQty.dismiss();
                    InvoiceCursor = 0;


                } else if (cancelFuncKeyb == 1) {
                    getActivity().finish();
                    cancelFuncKeyb = 0;
                } else if (InvoiceCursor == 2) {

                    //  Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "Btn"), Toast.LENGTH_SHORT).show();
                    String str = removeWords(et_command.getText().toString().trim(), "Btn");
                    String transNum = SuspendTransactionList.get(Integer.parseInt(str) - 1);
                    Toast.makeText(getContext(), "RESUME TRANSACTION :" + transNum, Toast.LENGTH_SHORT).show();
                    MoveGreaterTransaction(transNum);
                    ReturnInvoiceReceiptItemSuspend(transNum);
                    ReturnInvoiceReceiptItemWDiscountSuspend(transNum);
                    InvoiceCursor = 0;
                }
                //note!
                else if (InvoiceCursor == 3) {
                    InvoiceCursor = 4;
                    String str = removeWords(et_command.getText().toString().trim(), "Btn");
                    String transNum = ReprintTransactionList.get(Integer.parseInt(str) - 1);
                    Toast.makeText(getContext(), "RESUME TRANSACTION :" + transNum, Toast.LENGTH_SHORT).show();
                    TransReprint = transNum;


                } else if (InvoiceCursor == 4) {
                    if (TransReprint == "" || TransReprint.equals("")) {
                        Toast.makeText(getContext(), "PLEASE SELECT TRANSACTION", Toast.LENGTH_SHORT).show();
                        InvoiceCursor = 3;
                        et_command.setText("");
                    } else {
                        int qty;
                        if (et_ReprintQty.getText().length() == 0) {
                            qty = 1;
                        } else {
                            qty = Integer.parseInt(et_ReprintQty.getText().toString());
                        }
                        ReprintData(tv_DateToDay.getText().toString(), TransReprint, qty);
                        InsertTransaction(TransReprint);
                        InvoiceCursor = 0;
                        alertDialog.dismiss();
                    }
                } else {
                    checkOut();
                }
            }



        }
        if  (input.equalsIgnoreCase("Sub Total")){
            if (cancelFuncKeyb==1){
                alertDialog.dismiss();
                cancelFuncKeyb=0;
            }

            if (InvoiceCursor==2){
                alertDialog.dismiss();
                InvoiceCursor=0;
            }
        }

//region Button D01-D13
        if (input.equalsIgnoreCase("D01")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"DO1" );
            showingItem("D01",layer);
        }
        if (input.equalsIgnoreCase("D02")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"DO2" );
            showingItem("D02",layer);
        }
        if (input.equalsIgnoreCase("D03")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"DO3" );
            showingItem("D03",layer);
        }
//        if (input.equalsIgnoreCase("D03")){
//            try{
//                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
//                    //note : when master list is shown
//                    alertDialog.dismiss();
//                    MainFunctionCursor=1;
//                }}
//            catch (Exception ex){
//
//            }
//            Log.e("TAG", "SimulateKeyboard: "+"DO3" );
//            showingItem("D03");
//        }
        if (input.equalsIgnoreCase("D04")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"DO4" );
            showingItem("D04",layer);
        }
        if (input.equalsIgnoreCase("D05")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D05" );
            showingItem("D05",layer);
        }
        if (input.equalsIgnoreCase("D06")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D06" );
            showingItem("D06",layer);
        }
        if (input.equalsIgnoreCase("D07")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D07" );
            showingItem("D07",layer);
        }
        if (input.equalsIgnoreCase("D08")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D08" );
            showingItem("D08",layer);
        }
        if (input.equalsIgnoreCase("D09")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D09" );
            showingItem("D09",layer);
        }
        if (input.equalsIgnoreCase("D10")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D10" );
            showingItem("D10",layer);
        }
        if (input.equalsIgnoreCase("D11")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D11" );
            showingItem("D11",layer);
        }
        if (input.equalsIgnoreCase("D12")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D12" );
            showingItem("D12",layer);
        }
        if (input.equalsIgnoreCase("D13")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            Log.e("TAG", "SimulateKeyboard: "+"D13" );
            showingItem("D13",layer);
        }
        //endregion


        if (input.equalsIgnoreCase("Clear")){
            if (et_scanItem.hasFocus()){
                et_scanItem.setText("");
                alertDialog.dismiss();
                InvoiceCursor=0;
            }


        }

        if (input.equalsIgnoreCase("EC")){

            if (alertDialogDiscount!=null && alertDialogDiscount.isShowing()){

                int length = et_quantity.getText().length();
                if (length > 0) {
                    et_quantity.getText().delete(length - 1, length);

                }
            }
            else {


                if (InvoiceCursor == 0) {

                    if (et_scanItem.hasFocus()) {
                        int length = et_scanItem.getText().length();
                        if (length > 0) {
                            et_scanItem.getText().delete(length - 1, length);
                        }
                    }
                } else if (InvoiceCursor == 1) {

                    int length = et_quantity.getText().length();
                    if (length > 0) {
                        et_quantity.getText().delete(length - 1, length);

                    }

                } else if (InvoiceCursor == 2 || InvoiceCursor == 3) {
                    int length = et_command.getText().length();
                    if (length > 0) {
                        et_command.getText().delete(length - 1, length);
                    }
                } else if (InvoiceCursor == 4) {
                    int length = et_ReprintQty.getText().length();
                    if (length > 0) {
                        et_ReprintQty.getText().delete(length - 1, length);
                    }
                } else if (InvoiceCursor == 101) {
                    //et_quantity
                    int length = et_quantity.getText().length();
                    if (length > 0) {
                        et_quantity.getText().delete(length - 1, length);
                    }
                } else if (InvoiceCursor == 102) {
                    //et_quantity
                    int length = et_quantity.getText().length();
                    if (length > 0) {
                        et_quantity.getText().delete(length - 1, length);
                    }
                }
            }


        }

        if (input.equalsIgnoreCase("Invoice")){

            try{
            if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                //note : when master list is shown
                alertDialog.dismiss();
                MainFunctionCursor=1;
                invoice();

                Log.d("TAG", "SimulateKeyboard: invoice- closing qty");
            }}
            catch (Exception ex){
//                alertDialog.dismiss();
                Log.d("TAG", ex.getMessage());
                invoice();
            }
            alertDialog.dismiss();
           //
        }
        if (input.equalsIgnoreCase("Shift")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                    shift();
                }}
            catch (Exception ex){
                shift();
            }

        }
        if (input.equalsIgnoreCase("Payment")){
            try{
                if(finalMasterListIndicator==1|| finalMasterListIndicator==0){
                    //note : when master list is shown
                    alertDialog.dismiss();
                    MainFunctionCursor=1;
                }}
            catch (Exception ex){

            }
            checkOut();
        }

        if(input.equalsIgnoreCase("PO") || input.equalsIgnoreCase("RA")){
           // openCashBox();
            startActivity(new Intent(view.getContext(), cashier_cash.class));
        }
        if (input.equalsIgnoreCase("FEED")){
            openCashBox();
        }

        if (input.equalsIgnoreCase("Page UP")){


//
//            if (finalMasterListIndicator==1 || finalMasterListIndicator==0){
//
//
//
//
//                // rv_Masterlist.requestFocus(xCursor);
//                if  (xCursor>=-1) {
//                    xCursor += -7;
//
//                    if (finalMasterListIndicator==0){
////                        if (cursbackward==1) {
//                        if (StartPaging>0) {
//                         //   Toast.makeText(getContext(), "back button", Toast.LENGTH_SHORT).show();
//                            StartPaging -= 15;
//                            EndPaging -= 15;
//                            setPLUPage(StartPaging, EndPaging);
//                            Log.d("Page Pagination if", "Backward " + String.valueOf(StartPaging) + "  " + EndPaging);
////                        }
//                        }
//                    }
//                    else{
//                        rv_Masterlist.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                rv_Masterlist.scrollToPosition(xCursor);
//                                // Here adapter.getItemCount()== child count
//                                Log.d("TAG if", "SimulateKeyboard:" + xCursor);
//
//
//                            }
//
//                        });
//
//                    }
//
//
//                }
//
//
//
//
//            }
//            else {
//
//
//
//                    if (StartPaging > 0) {
//                        StartPaging -= 15;
//                        EndPaging -= 15;
//
//
//                        setPLUPage(StartPaging, EndPaging);
//
//                    }
//
//                Log.d("Page Pagination else", "Backward " + String.valueOf(StartPaging) + "  " + EndPaging);
//
//
//
//            }
//
//
//
//            Log.d("Page Pagination", "Backward " + String.valueOf(StartPaging) + "  " + EndPaging + " finalmasterlist " + finalMasterListIndicator + "acbkward "+cursbackward + "xcursor "+ xCursor);



            if (layer==1){
                keypadLayer(1);
            }
            else{
                keypadLayer(--layer);
            }




        }
        if (input.equalsIgnoreCase("Page DN")){


//            if (finalMasterListIndicator==1 || finalMasterListIndicator==0){
//                Log.d("Page Pagination if", "Backward " + String.valueOf(StartPaging) + "  " + EndPaging);
//                if (xCursor< ArrayDataList2.size()) {
//                    xCursor += 7;
//                    Log.d("TAG", "SimulateKeyboard: page DN size : "+xCursor + " of " + ArrayDataList2.size());
//                    // rv_Masterlist.requestFocus(xCursor);
//
//                    if(finalMasterListIndicator==0){
//                        rv_pluList.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (cursforward==1) {
//                                   // Toast.makeText(getContext(), "forward button", Toast.LENGTH_SHORT).show();
//                                    StartPaging += 15;
//                                    EndPaging += 15;
//                                    setPLUPage(StartPaging, EndPaging);
//
//                                    Log.d("Page Pagination1", "Forward " + String.valueOf(StartPaging) + "  " + EndPaging);
//                                }
//                                else{
//                                    setPLUPage(StartPaging, EndPaging);
//                                    Log.d("Page Pagination2", "Forward " + String.valueOf(StartPaging) + "  " + EndPaging);
//                                }
//
//
//                            }
//                        });
//                    }
//                    else{
//
//                        rv_Masterlist.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                rv_Masterlist.scrollToPosition(xCursor);
//                                // Here adapter.getItemCount()== child count
//                                Log.d("TAG", "SimulateKeyboard:" + xCursor);
//
//
//                            }
//                        });
//                    }
//
//
//
//
//
//
//                }
//                else if(xCursor >= ArrayDataList2.size()-1){
//                    xCursor = ArrayDataList2.size()-1;
//
//                if (finalMasterListIndicator==0){
//                    rv_pluList.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (cursforward==1) {
//                               // Toast.makeText(getContext(), "forward button", Toast.LENGTH_SHORT).show();
//                                StartPaging += 15;
//                                EndPaging += 15;
//                                setPLUPage(StartPaging, EndPaging);
//
//                                Log.d("Page Pagination2", "Forward " + String.valueOf(StartPaging) + "  " + EndPaging);
//                            }
//
//
//                        }
//                    });
//                }
//                else{
//                    rv_Masterlist.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            rv_Masterlist.scrollToPosition(xCursor);
//                            // Here adapter.getItemCount()== child count
//                            Log.d("TAG", "SimulateKeyboard:" + xCursor);
//
//                           // Toast.makeText(getContext(), xCursor + 1 + " last row data", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    });
//
//                }
//
//
//
//
//
//
//
//
//
//
//
//                }
//            }
//            else {
//
//                if (EndPaging >= PLUList.size()) {
//
//                } else {
//                    if (cursforward==1) {
//                        Log.d("TAG", "SimulateKeyboard: Page DOWN");
//                        StartPaging += 15;
//                        EndPaging += 15;
//
//
//                        setPLUPage(StartPaging, EndPaging);
//                    }
//
//
//                }
//                Log.d("Page Pagination else", "Backward " + String.valueOf(StartPaging) + "  " + EndPaging);
//
//            }


            keypadLayer(++layer);


        }

        if (input.equalsIgnoreCase("Exit")){

            if (alertDialogDiscount!=null && alertDialogDiscount.isShowing()){

                alertDialogQty.dismiss();
                alertDialog.dismiss();
                MainFunctionCursor=1;
                InvoiceCursor=0;
            }
            else{




              //  alertDialog.dismiss();
                MainFunctionCursor=1;
                InvoiceCursor=0;
                invoice();

            }







        }

        if (input.equalsIgnoreCase("Discount")){
            showingDiscountList();
        }


        //keyboard for up and down







//        if (keyCode==141){
//            checkOut();
//        }






    }
    private void openCashBox(){
        Intent intent = new Intent("android.intent.action.CASHBOX");
        intent.putExtra("cashbox_open", true);
       // this.sendBroadcast(intent);
        this.getActivity().sendBroadcast(intent);
    }



    public static String removeWords(String word ,String remove) {
        return word.replace(remove,"");
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






    LinearLayout linear_Invoicelist_layout,linearLayoutButton;

    TextView tv_itemName;
    TextView tv_itemPrice;
    TextView tv_ItemAvailable;


    ImageView iv_check;
    Button btn_subtractItem;
    TextView tv_itemSubtotal;
    private int checkedPosition = 0;

    private void insertItemCode(){
     //   Log.e("item",et_scanItem.getText().toString());
        //

        if (!et_scanItem.getText().toString().equals("")) {

            db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            Cursor checkItemCode = db.rawQuery("select ItemID from ITEM where ItemCode='" + et_scanItem.getText().toString() + "'", null);
            if (checkItemCode.getCount() != 0) {


             //   fillInvoiceItemList();

                while (checkItemCode.moveToNext()) {

                    ItemCursor = itemCode.indexOf(checkItemCode.getString(0));
                    Log.e("INPUT",et_scanItem.getText().toString());
                    Log.e("ItemCursor",String.valueOf(ItemCursor));


                    InsertItemBcode2();
                    //ItemCursor=1000000;
                }
            }
        }
        else {
            Log.e("INPUT","NO INPUT");
        }

        et_scanItem.setText("");


        db.close(); //test
    }

//    private void showingItem(String s){
//       int btnCounter=0;
//
//        itemCode = new ArrayList<>();
//        itemName = new ArrayList<>();
//        recptDesc = new ArrayList<>();
//        itemPrice = new ArrayList<>();
//        itemQuantity = new ArrayList<>();
//        itemVatIndicator=new ArrayList<>();
//
//        CheckItemDatabaseFastMoving(s);
//        PLUItem(fastmovingItem);
//
//        int numberOfItem = fastmovingItem;
//        LinearLayout rl = (LinearLayout) view.findViewById(R.id.linearlayout);
//        rl.removeAllViewsInLayout();
//        int totalPerRow=5;
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//       getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels/2/totalPerRow;
//
//
//        for (int i2 = 1; i2 <= 50; i2++) {
//            LinearLayout row2 = new LinearLayout(view.getContext());
//            row2.setOrientation(LinearLayout.HORIZONTAL);
//            row2.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
//
//
//
//            for (int j2 = 0; j2 < totalPerRow; j2++) {
//
//                rl.removeView(row2);
//                LinearLayout rootView = new LinearLayout(view.getContext());
//                rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                rootView.setOrientation(LinearLayout.VERTICAL);
//
//
//                btnCounter++;
//
//                if (btnCounter <= numberOfItem) {
//
//
//                    CardView cardView = new CardView(view.getContext());
//
//                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(width, width);
//                    cardView.setLayoutParams(cardViewParams);
//
////                    ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
////                    cardViewMarginParams.setMargins(2, 2, 2, 2);
//                    cardView.requestLayout();  //Dont forget this line
//
//
//                    View child = getLayoutInflater().inflate(R.layout.invoice_item_list2, null);//child.xml
//                    tv_itemName = child.findViewById(R.id.tv_itemName);
//                    tv_itemName.setText(itemName.get(btnCounter-1));
//                   // linearLayoutButton = child.findViewById(R.id.linearlayoutInsert);
//                    LinearLayout ll_itemClick = child.findViewById(R.id.ll_itemClick);
//                    child.setId(btnCounter-1);
//
//
//                    ll_itemClick.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                            // AlertDialog alert = builder.create();
//                            // List<Integer>al=new ArrayList<>();
//                            Log.e("CHILD ID",String.valueOf(child.getId()));
//
//
//                            LayoutInflater inflater = getLayoutInflater();
//                            final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
//                            EditText et_quantity = alertLayout.findViewById(R.id.et_quantity);
//                            Button btn_0 = alertLayout.findViewById(R.id.btn_0);
//                            String text = et_quantity.getText().toString();
//                            btn_0.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "0");
//
//                                }
//                            });
//
//                            Button btn_1 = alertLayout.findViewById(R.id.btn_1);
//                            btn_1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "1");
//                                }
//                            });
//                            Button btn_2 = alertLayout.findViewById(R.id.btn_2);
//                            btn_2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "2");
//                                }
//                            });
//                            Button btn_3 = alertLayout.findViewById(R.id.btn_3);
//                            btn_3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "3");
//                                }
//                            });
//                            Button btn_4 = alertLayout.findViewById(R.id.btn_4);
//                            btn_4.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "4");
//                                }
//                            });
//                            Button btn_5 = alertLayout.findViewById(R.id.btn_5);
//                            btn_5.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "5");
//                                }
//                            });
//                            Button btn_6 = alertLayout.findViewById(R.id.btn_6);
//                            btn_6.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "6");
//                                }
//                            });
//                            Button btn_7 = alertLayout.findViewById(R.id.btn_7);
//                            btn_7.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "7");
//                                }
//                            });
//                            Button btn_8 = alertLayout.findViewById(R.id.btn_8);
//                            btn_8.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "8");
//                                }
//                            });
//                            Button btn_9 = alertLayout.findViewById(R.id.btn_9);
//                            btn_9.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    et_quantity.setText(et_quantity.getText().toString() + "9");
//                                }
//                            });
//                            ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
//                            ibtn_delete.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    int index = et_quantity.getText().length();
//                                    if (index > 0) {
//                                        et_quantity.getText().delete(index - 1, index);
//
//                                    } else {
//
//                                    }
//                                }
//                            });
//
//
//                            ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);
//
//                            builder.setView(alertLayout);
//
//                            AlertDialog alertDialog = builder.create();
//                            alertDialog.setCanceledOnTouchOutside(false);
//                            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                            alertDialog.show();
//                            alertDialog.setCanceledOnTouchOutside(true);
//
//                            ibtn_confirm.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    int itemQty=1;
//                                    String quantityText=et_quantity.getText().toString();
//                                    if(!quantityText.matches("")){
//                                        FinalItemQty = et_quantity.getText().toString().trim();
//                                        Log.e("ItemQty","NOT EMPTY"+et_quantity.getText().toString());
//                                    }else{
//                                        FinalItemQty = String.valueOf(itemQty);
//                                        Log.e("ItemQty","EMPTY");
//                                    }
//                                    //FinalItemQty = et_quantity.getText().toString().trim();
//                                    ItemCursor = child.getId();
//                                    addInvoice();
//                                    FinalItemQty = "";
//                                    alertDialog.dismiss();
//
//                                }
//                            });
//
//
//                        }
//                    });
//
//                    cardView.addView(child);
//
//
//                    rootView.addView(cardView);
//
//
//
//                }
//                row2.addView(rootView);
//
//            }
//            rl.addView(row2);
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//    }

    ArrayList<invoice_plu_model> ButtonList = new ArrayList<>();
    int StartPaging;
    int EndPaging;
    int reserve1;
    int reserve2;
    int reserve3;
    Button reserveButton1;
    Button reserveButton2;
    Button reserveButton3;
    String reserveButtonSubCatg,reserveButtonCatg; // for adding reserve item
    ArrayList<String>reserveItemcodeList = new ArrayList<>();
    ArrayList<String>reserveItemNameList = new ArrayList<>();
    ArrayList<String>reserveReceptDescList = new ArrayList<>();
    ArrayList<String>reserveItemPriceList = new ArrayList<>();
    ArrayList<String>reserveItemQuantityList = new ArrayList<>();
    ArrayList<String>reserveItemVatIndicatorList = new ArrayList<>();
    String DeptNameUni;


    private void LoadReserveButton(View view,String DeptName){

        reserveItemcodeList.clear();
        reserveItemNameList.clear();
        reserveReceptDescList.clear();
        reserveItemPriceList.clear();
        reserveItemQuantityList.clear();
        reserveItemVatIndicatorList.clear();
        DeptNameUni=DeptName;



        reserveButton1 = view.findViewById(R.id.btn_reserve1);
        reserveButton2 = view.findViewById(R.id.btn_reserve2);
        reserveButton3 = view.findViewById(R.id.btn_reserve3);

        ll_linearLayoutbtn8=view.findViewById(R.id.ll_linearLayoutbtn8);
        ll_linearLayoutbtn9=view.findViewById(R.id.ll_linearLayoutbtn9);
        ll_linearLayoutbtn10=view.findViewById(R.id.ll_linearLayoutbtn10);




        ll_linearLayoutbtn8.setVisibility(View.VISIBLE);
        ll_linearLayoutbtn9.setVisibility(View.VISIBLE);
        ll_linearLayoutbtn10.setVisibility(View.VISIBLE);

        itemListC = db.rawQuery("select * from ITEM_RESERVE where SubCatgID='"+reserveButtonSubCatg+"'", null);
       if (itemListC.getCount()!=0) {
           while (itemListC.moveToNext()) {
               //fastmovingItem++;
               Log.d("ITEM", itemListC.getString(1));
               reserveItemcodeList.add(itemListC.getString(0));
               reserveItemNameList.add(itemListC.getString(1));
               reserveReceptDescList.add(itemListC.getString(2));
               reserveItemPriceList.add(itemListC.getString(3));
               reserveItemQuantityList.add(itemListC.getString(4));
               reserveItemVatIndicatorList.add(itemListC.getString(15));

               if (itemListC.getString(0).equalsIgnoreCase(DeptName+"reserve01")){
                   reserveButton1.setText(itemListC.getString(1));
               }
               else if (itemListC.getString(0).equalsIgnoreCase(DeptName+"reserve02")){
                   reserveButton2.setText(itemListC.getString(1));
               }
               else if (itemListC.getString(0).equalsIgnoreCase(DeptName+"reserve03")){
                   reserveButton3.setText(itemListC.getString(1));
               }


           }


          // reserveButton1.setText(reserveItemNameList.get(0));

           if (itemListC.getCount()==1){
               reserveButton1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   reserveItemcodeList.clear();
//                   reserveItemNameList.clear();
//                   reserveReceptDescList.clear();
//                   reserveItemPriceList.clear();
//                   reserveItemQuantityList.clear();
//                   reserveItemVatIndicatorList.clear();



                       //Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
                       //Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);

                       //  Log.d("TAG", "reserve: "+ reserveItemNameList.indexOf(reserveButton1.getText()));

                       try{
                           Log.d("Reserve1", " : "+DeptName+reserveItemcodeList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemNameList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveReceptDescList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemPriceList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemQuantityList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemVatIndicatorList.get(0).toString());



                           //  String orderID,String orderName,String orderPrice,String qty
                           addInvoiceReserve(reserveItemcodeList.get(0).toString(),reserveItemNameList.get(0),reserveItemPriceList.get(0),"1");
                           //openQtyDialog(ButtonPrefix);
                       }catch(Exception ex){
                           Toast.makeText(getContext(), "ERROR INSERTING", Toast.LENGTH_SHORT).show();
                       }


                   }
               });
               reserveButton2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       addReserveDialog(DeptName+"reserve02");
                   }
               });
               reserveButton3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       addReserveDialog(DeptName+"reserve03");
                   }
               });
           }
           else if(itemListC.getCount()==2){

               reserveButton1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   reserveItemcodeList.clear();
//                   reserveItemNameList.clear();
//                   reserveReceptDescList.clear();
//                   reserveItemPriceList.clear();
//                   reserveItemQuantityList.clear();
//                   reserveItemVatIndicatorList.clear();



                       //Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
                       //Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);

                       //  Log.d("TAG", "reserve: "+ reserveItemNameList.indexOf(reserveButton1.getText()));

                       try{
                           Log.d("Reserve1", " : "+DeptName+reserveItemcodeList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemNameList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveReceptDescList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemPriceList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemQuantityList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemVatIndicatorList.get(0).toString());



                           //  String orderID,String orderName,String orderPrice,String qty
                           addInvoiceReserve(reserveItemcodeList.get(0).toString(),reserveItemNameList.get(0),reserveItemPriceList.get(0),"1");
                           //openQtyDialog(ButtonPrefix);
                       }catch(Exception ex){
                           Toast.makeText(getContext(), "ERROR INSERTING", Toast.LENGTH_SHORT).show();
                       }


                   }
               });
               reserveButton2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   reserveItemcodeList.clear();
//                   reserveItemNameList.clear();
//                   reserveReceptDescList.clear();
//                   reserveItemPriceList.clear();
//                   reserveItemQuantityList.clear();
//                   reserveItemVatIndicatorList.clear();



                       //Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
                       //Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);

                       //  Log.d("TAG", "reserve: "+ reserveItemNameList.indexOf(reserveButton1.getText()));

                       try{
                           Log.d("Reserve1", " : "+DeptName+reserveItemcodeList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemNameList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveReceptDescList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemPriceList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemQuantityList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemVatIndicatorList.get(1).toString());



                           //  String orderID,String orderName,String orderPrice,String qty
                           addInvoiceReserve(reserveItemcodeList.get(1).toString(),reserveItemNameList.get(1),reserveItemPriceList.get(1),"1");
                           //openQtyDialog(ButtonPrefix);

                       }catch(Exception ex){

                           Toast.makeText(getContext(), "ERROR INSERTING", Toast.LENGTH_SHORT).show();
                       }


                   }
               });
               reserveButton3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       addReserveDialog(DeptName+"reserve03");
                   }
               });
           }

           else if(itemListC.getCount()==3){

               reserveButton1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   reserveItemcodeList.clear();
//                   reserveItemNameList.clear();
//                   reserveReceptDescList.clear();
//                   reserveItemPriceList.clear();
//                   reserveItemQuantityList.clear();
//                   reserveItemVatIndicatorList.clear();



                       //Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
                       //Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);

                       //  Log.d("TAG", "reserve: "+ reserveItemNameList.indexOf(reserveButton1.getText()));

                       try{
                           Log.d("Reserve1", " : "+DeptName+reserveItemcodeList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemNameList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveReceptDescList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemPriceList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemQuantityList.get(0).toString());
                           Log.d("Reserve1", " : "+reserveItemVatIndicatorList.get(0).toString());



                           //  String orderID,String orderName,String orderPrice,String qty
                           addInvoiceReserve(reserveItemcodeList.get(0).toString(),reserveItemNameList.get(0),reserveItemPriceList.get(0),"1");
                           //openQtyDialog(ButtonPrefix);
                       }catch(Exception ex){
                           Toast.makeText(getContext(), "ERROR INSERTING", Toast.LENGTH_SHORT).show();
                       }


                   }
               });
               reserveButton2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   reserveItemcodeList.clear();
//                   reserveItemNameList.clear();
//                   reserveReceptDescList.clear();
//                   reserveItemPriceList.clear();
//                   reserveItemQuantityList.clear();
//                   reserveItemVatIndicatorList.clear();



                       //Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
                       //Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);

                       //  Log.d("TAG", "reserve: "+ reserveItemNameList.indexOf(reserveButton1.getText()));

                       try{
                           Log.d("Reserve1", " : "+DeptName+reserveItemcodeList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemNameList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveReceptDescList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemPriceList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemQuantityList.get(1).toString());
                           Log.d("Reserve1", " : "+reserveItemVatIndicatorList.get(1).toString());



                           //  String orderID,String orderName,String orderPrice,String qty
                           addInvoiceReserve(reserveItemcodeList.get(1).toString(),reserveItemNameList.get(1),reserveItemPriceList.get(1),"1");
                           //openQtyDialog(ButtonPrefix);

                       }catch(Exception ex){

                           Toast.makeText(getContext(), "ERROR INSERTING", Toast.LENGTH_SHORT).show();
                       }


                   }
               });
               reserveButton3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   reserveItemcodeList.clear();
//                   reserveItemNameList.clear();
//                   reserveReceptDescList.clear();
//                   reserveItemPriceList.clear();
//                   reserveItemQuantityList.clear();
//                   reserveItemVatIndicatorList.clear();

                       try {

                           //Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
                           //Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);

                           //  Log.d("TAG", "reserve: "+ reserveItemNameList.indexOf(reserveButton1.getText()));

                           Log.d("Reserve1", " : " + DeptName + reserveItemcodeList.get(2).toString());
                           Log.d("Reserve1", " : " + reserveItemNameList.get(2).toString());
                           Log.d("Reserve1", " : " + reserveReceptDescList.get(2).toString());
                           Log.d("Reserve1", " : " + reserveItemPriceList.get(2).toString());
                           Log.d("Reserve1", " : " + reserveItemQuantityList.get(2).toString());
                           Log.d("Reserve1", " : " + reserveItemVatIndicatorList.get(2).toString());


                           //  String orderID,String orderName,String orderPrice,String qty
                           addInvoiceReserve(reserveItemcodeList.get(2).toString(), reserveItemNameList.get(2), reserveItemPriceList.get(2), "1");

                       }catch(Exception ex){
                           Log.e("TAG", "onClick: "+ex.toString() );
                           Toast.makeText(getContext(), "ERROR INSERTING", Toast.LENGTH_SHORT).show();
                       }

                       //openQtyDialog(ButtonPrefix);

                   }
               });
           }
           else{
               Log.d("TAG", "LoadReserveButton: else");
               reserveButton1.setText(" ");
               reserveButton2.setText(" ");
               reserveButton3.setText(" ");

               reserveButton1.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
//                   Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);
//                   Log.d("Reserve1", "ItemPrice : "+reserveItemPriceList.get(0));
//
//
//                   addInvoiceReserve(0,"1");
//                   //openQtyDialog(ButtonPrefix);

                       addReserveDialog(DeptName+"reserve01");

                   }
               });
               reserveButton2.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
//                   Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);
//                   Log.d("Reserve1", "ItemPrice : "+reserveItemPriceList.get(0));
//
//
//                   addInvoiceReserve(0,"1");
//                   //openQtyDialog(ButtonPrefix);

                       addReserveDialog(DeptName+"reserve02");

                   }
               });
               reserveButton3.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
//                   Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
//                   Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);
//                   Log.d("Reserve1", "ItemPrice : "+reserveItemPriceList.get(0));
//
//
//                   addInvoiceReserve(0,"1");
//                   //openQtyDialog(ButtonPrefix);

                       addReserveDialog(DeptName+"reserve03");

                   }
               });

               // Toast.makeText(getActivity(), "opening empty reserve", Toast.LENGTH_SHORT).show();




           }





       }
       else{
           reserveButton1.setText(" ");
           reserveButton2.setText(" ");
           reserveButton3.setText(" ");

           reserveButton1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
//                   Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
//                   Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);
//                   Log.d("Reserve1", "ItemPrice : "+reserveItemPriceList.get(0));
//
//
//                   addInvoiceReserve(0,"1");
//                   //openQtyDialog(ButtonPrefix);

                   addReserveDialog(DeptName+"reserve01");

               }
           });
           reserveButton2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
//                   Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
//                   Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);
//                   Log.d("Reserve1", "ItemPrice : "+reserveItemPriceList.get(0));
//
//
//                   addInvoiceReserve(0,"1");
//                   //openQtyDialog(ButtonPrefix);

                   addReserveDialog(DeptName+"reserve02");

               }
           });
           reserveButton3.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
//                   Log.d("Reserve1", "CatgID : "+reserveButtonCatg);
//                   Log.d("Reserve1", "SubCatgID : "+reserveButtonSubCatg);
//                   Log.d("Reserve1", "ItemPrice : "+reserveItemPriceList.get(0));
//
//
//                   addInvoiceReserve(0,"1");
//                   //openQtyDialog(ButtonPrefix);

                   addReserveDialog(DeptName+"reserve03");

               }
           });

          // Toast.makeText(getActivity(), "opening empty reserve", Toast.LENGTH_SHORT).show();




       }











        // reserveButton1.setText("");

        Toast.makeText(view.getContext(), reserveButton1.getText().toString(), Toast.LENGTH_SHORT).show();



    }

    private void findReserveData(String itemID,String ItemName){
       Cursor curs = db.rawQuery("select * from ITEM_RESERVE where ItemID='"+itemID+"' and ItemName='"+ItemName+"'", null);
       if (curs.getCount()!=0){
           while (curs.moveToFirst()){

           }
       }
    }

    EditText et_reserveName;
    EditText et_reserveDesc;
    EditText et_reserveAmount;
    Button btn_addNewReserve;
    EditText et_reserveID;
    EditText et_reserveCatgID;
    EditText et_reserveSubCatgID;

    //note!!!
    private void addReserveDialog(String id) { // invoice cursor 6


        InvoiceCursor=6;
        InitT9MapCode();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogSlide);


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_reserve, null);
         et_reserveID = alertLayout.findViewById(R.id.et_reserveID);
         et_reserveCatgID = alertLayout.findViewById(R.id.et_reserveCatgID);
         et_reserveSubCatgID = alertLayout.findViewById(R.id.et_reserveSubCatgID);
         et_reserveName = alertLayout.findViewById(R.id.et_reserveName);
         et_reserveDesc = alertLayout.findViewById(R.id.et_reserveDesc);
         et_reserveAmount= alertLayout.findViewById(R.id.et_reserveAmount);
        et_reserveName.requestFocus();

        et_reserveID.setText(id);
        et_reserveCatgID.setText(reserveButtonCatg);
        et_reserveSubCatgID.setText(reserveButtonSubCatg);




        ImageButton imgb_exit = alertLayout.findViewById(R.id.imgb_exit);
        imgb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                //InvoiceCursor=0;
                alertDialog.dismiss();
                InvoiceCursor=0;
            }
        });


         btn_addNewReserve = alertLayout.findViewById(R.id.btn_addNewReserve);

        builder.setView(alertLayout);

        alertDialog  = builder.create();

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        btn_addNewReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_reserveName.getText().length()==0){
                    Toast.makeText(getContext(), "Please input Reserve Name", Toast.LENGTH_SHORT).show();
                }
                else if (et_reserveDesc.getText().length()==0){
                    Toast.makeText(getContext(), "Please input Reserve Name", Toast.LENGTH_SHORT).show();
                }

                else if (et_reserveAmount.getText().length()==0){
                    Toast.makeText(getContext(), "Please input Reserve Name", Toast.LENGTH_SHORT).show();
                }
                else{

                    insertNewReserve(et_reserveID.getText().toString(),et_reserveName.getText().toString(),et_reserveDesc.getText().toString(),et_reserveAmount.getText().toString());
                    InvoiceCursor=0;
                }



                //InvoiceCursor=0;
                //alertDialog.dismiss();
                //alertDialog.dismiss();
            }
        });






    }

//cashbox



    private void insertNewReserve(String id,String colName,String colDesc,String price){

        POSAndroidDB posAndroidDB = new POSAndroidDB(getContext());
          posAndroidDB.insertProductReserve(id,colName,colDesc,
                price,"100",reserveButtonCatg,reserveButtonSubCatg);
          alertDialog.dismiss();

    }
//working 02-22
    private void showingItem(String Button,int layer){

      //  alertDialog.dismiss();
       // alertDialogQty.dismiss();

        db = this.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+Button+"' and pageLayer='"+layer+"'", null);
        if (cursor.getCount()!=0){
            if (cursor.moveToFirst()){
                if (cursor.getString(1).equalsIgnoreCase("ALL")){
                    showingAllItem();
                }
                else{
//                    int btnCounter=0;
//                    StartPaging=0;
//                    EndPaging=15;
//                    finalMasterListIndicator=0;
//
//                    itemCode = new ArrayList<>();
//                    itemName = new ArrayList<>();
//                    recptDesc = new ArrayList<>();
//                    itemPrice = new ArrayList<>();
//                    itemQuantity = new ArrayList<>();
//                    itemVatIndicator=new ArrayList<>();
//                    priceOverride=new ArrayList<>();
//
//                    CheckItemDatabaseFastMoving(Button);
//                    PLUItem(fastmovingItem);
//                    setPLUPage(StartPaging,EndPaging);
//
//                    Log.d("TAG", "String s:"+Button);

                    if(alertDialogQty!=null && alertDialogQty.isShowing()){
                        alertDialogQty.dismiss();
                    }
                    checkButtonFunction(cursor.getString(1),Button);


                }
            }


        }
        else{

            Toast.makeText(getContext(), "BUTTON NOT SET ", Toast.LENGTH_SHORT).show();

        }
        cursor.close();






    }

    private void checkButtonFunction(String shortCutId,String Button){
      //  String shortCutId = cursor.getString(1);
        Cursor cursor1 = db.rawQuery("select * from SUBCATEGORY where SubCatgID='" + shortCutId + "'", null);
        if (cursor1.getCount() != 0) {
            if (cursor1.moveToFirst()) {


                    int btnCounter=0;
                    StartPaging=0;
                    EndPaging=15;
                    finalMasterListIndicator=0;

                    itemCode = new ArrayList<>();
                    itemName = new ArrayList<>();
                    recptDesc = new ArrayList<>();
                    itemPrice = new ArrayList<>();
                    itemQuantity = new ArrayList<>();
                    itemVatIndicator=new ArrayList<>();
                    priceOverride=new ArrayList<>();

                    CheckItemDatabaseFastMoving(Button);
                    PLUItem(fastmovingItem);
                    setPLUPage(StartPaging,EndPaging);

                    Log.d("TAG", "String s:"+Button);

            }
        } else {
            Cursor cursor2 = db.rawQuery("select ItemID,PriceOverride from ITEM where ItemID='" + shortCutId + "'", null);
            if (cursor2.getCount() != 0) {
                if (cursor2.moveToFirst()) {

                    showingAllItem();

                    //add to invoice

                    Log.d("TAG", "insert 1:1 :" + shortCutId );
                    Log.d("TAG", "Price Override : " + cursor2.getString(1));
                   // Log.d("TAG", "Item Code: " + itemCode.toString());



                    ItemCursor = itemCode.indexOf(shortCutId);
                    FinalItemQty = "1";
                    int finalItemCursor = ItemCursor+1;
                    if (cursor2.getString(1).equalsIgnoreCase("YES")){
                        openPrice("PLU"+finalItemCursor);
                    }else{

                        openQtyDialog("PLU"+finalItemCursor);
                       // Log.d("TAG", "ItemCursor: "+finalItemCursor );


                        //Log.d("TAG", "ItemCursor: " + ItemCursor);
                       // openPrice("PLU"+ItemCursor+1);

                    }


                    //  ItemCursor = child.getId();
                    //addInvoice();
                    FinalItemQty = "";


                   // InsertItemBcode2();



                }
            }

            cursor2.close();
        }
    }



    //region showItem

    private void showingDeptKeys(){

        int btnCounter=0;
        StartPaging=0;
        EndPaging=14;
        finalMasterListIndicator=0;

        itemCode = new ArrayList<>();
        itemName = new ArrayList<>();
        recptDesc = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemQuantity = new ArrayList<>();
        itemVatIndicator=new ArrayList<>();
        priceOverride =new ArrayList<>();

        CheckItemDatabaseDeptKeys();
        DeptKeysItem(fastmovingItem);

//
//        CheckItemDatabaseAll();
//        PLUItem(fastmovingItem);
//        setPLUPage(StartPaging,EndPaging);
//


    }
    private void showingAllItem(){
        int btnCounter=0;
        StartPaging=0;
        EndPaging=14;
        finalMasterListIndicator=0;

        itemCode = new ArrayList<>();
        itemName = new ArrayList<>();
        recptDesc = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemQuantity = new ArrayList<>();
        itemVatIndicator=new ArrayList<>();
        priceOverride =new ArrayList<>();


        CheckItemDatabaseAll();
        PLUItem(fastmovingItem);
        setPLUPage(StartPaging,EndPaging);




    }

    //endregion

//    int backVisible; //0 invisible //1 visible
//    int forwardVisible;
    int cursforward=0;
    int cursbackward=0;
    private void setDeptPage(int StartPage,int EndPage){
        invoice_plu_model po2=null;

        ButtonList.clear();



        if (StartPage==0){
            ib_back.setEnabled(false);
            ib_back.setVisibility(View.INVISIBLE);
            ib_forward.setVisibility(View.VISIBLE);

            fl_back.setVisibility(View.INVISIBLE);
            fl_next.setVisibility(View.VISIBLE);

            cursforward=1;
            cursbackward=0;


//            backVisible=0;
//            forwardVisible=1;
        }
        else if (StartPage>0){
            ib_back.setEnabled(true);
            ib_back.setVisibility(View.VISIBLE);
            ib_forward.setVisibility(View.VISIBLE);

            fl_back.setVisibility(View.VISIBLE);
            fl_next.setVisibility(View.VISIBLE);

            cursforward=1;
            cursbackward=1;

//            reserveButton1.setVisibility(View.VISIBLE);
//            reserveButton2.setVisibility(View.VISIBLE);
//            reserveButton3.setVisibility(View.VISIBLE);

//            backVisible=1;
//            forwardVisible=1;
        }


        int xx=0;
//        PLUList = new ArrayList<>();


        Log.d("finalPLu page", "start :"+StartPage + " End" + EndPage);
        for (int x=StartPage;x<=EndPage;x++){
            try {

                po2 = new invoice_plu_model(String.valueOf(x+1), itemCode.get(x), itemName.get(x), recptDesc.get(x), itemPrice.get(x), itemQuantity.get(x), itemVatIndicator.get(x),priceOverride.get(x));
                ButtonList.addAll(Arrays.asList(new invoice_plu_model[]{po2}));
//                Log.d("TAG", "itemcode: " + IDList.get(xx));

                reserveButton1.setVisibility(View.VISIBLE);
                reserveButton2.setVisibility(View.VISIBLE);
                reserveButton3.setVisibility(View.VISIBLE);


            }
            catch (Exception ex){
//                Log.e("TAG", "setPLUPage: "+IDList.get(xx) );
                Log.e("Error setPluPage",ex.toString());
//                ib_forward.setVisibility(View.INVISIBLE);
//                fl_next.setVisibility(View.INVISIBLE);

                cursforward=0;
                cursbackward=0;





            }

            xx++;



        }



        resfreshPLURecyclerview();


    }
    private void setPLUPage(int StartPage,int EndPage){
        invoice_plu_model po2=null;

        ButtonList.clear();



        if (StartPage==0){
            ib_back.setEnabled(false);
            ib_back.setVisibility(View.INVISIBLE);
            ib_forward.setVisibility(View.VISIBLE);

            fl_back.setVisibility(View.INVISIBLE);
            fl_next.setVisibility(View.VISIBLE);

             cursforward=1;
             cursbackward=0;


//            backVisible=0;
//            forwardVisible=1;
        }
        else if (StartPage>0){
            ib_back.setEnabled(true);
            ib_back.setVisibility(View.VISIBLE);
            ib_forward.setVisibility(View.VISIBLE);

            fl_back.setVisibility(View.VISIBLE);
            fl_next.setVisibility(View.VISIBLE);

            cursforward=1;
            cursbackward=1;

//            reserveButton1.setVisibility(View.VISIBLE);
//            reserveButton2.setVisibility(View.VISIBLE);
//            reserveButton3.setVisibility(View.VISIBLE);

//            backVisible=1;
//            forwardVisible=1;
        }


        int xx=0;
//        PLUList = new ArrayList<>();


        Log.d("finalPLu page", "start :"+StartPage + " End" + EndPage);
        for (int x=StartPage;x<=EndPage;x++){
            try {

                po2 = new invoice_plu_model(String.valueOf(x+1), itemCode.get(x), itemName.get(x), recptDesc.get(x), itemPrice.get(x), itemQuantity.get(x), itemVatIndicator.get(x),priceOverride.get(x));
                ButtonList.addAll(Arrays.asList(new invoice_plu_model[]{po2}));
//                Log.d("TAG", "itemcode: " + IDList.get(xx));

                reserveButton1.setVisibility(View.VISIBLE);
                reserveButton2.setVisibility(View.VISIBLE);
                reserveButton3.setVisibility(View.VISIBLE);


            }
            catch (Exception ex){
//                Log.e("TAG", "setPLUPage: "+IDList.get(xx) );
                Log.e("Error setPluPage",ex.toString());
//                ib_forward.setVisibility(View.INVISIBLE);
//                fl_next.setVisibility(View.INVISIBLE);

                cursforward=0;
                cursbackward=0;





            }

            xx++;



        }



        resfreshPLURecyclerview();


    }



    RecyclerView rv_pluList;
    private void resfreshPLURecyclerview(){

    // rv_pluList  = this.getActivity().findViewById(R.id.rv_pluList);
         //  rv_prodList = findViewById(R.id.rv_prodList);
       rv_pluList.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.removeAllViews();
        rv_pluList.setLayoutManager(new GridLayoutManager(this.getContext(), 5));
        //rv_ItemMasterList.setLayoutManager(layoutManager);
        //mAdapter=new invoice_plu_recyclerviewadapter(this.getContext(),ButtonList);
         mAdapter=new RecyclerviewAdapterPLU(this.getContext(),ButtonList);
        rv_pluList.setAdapter(mAdapter);

        //int firstVisible = mAdapter.() - 1;
        //int lastVisible = layoutManager.findLastVisibleItemPosition() + 1;


        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(getContext(), "back button", Toast.LENGTH_SHORT).show();
                StartPaging -= 15;
                EndPaging -=15;
                setPLUPage(StartPaging,EndPaging);
                Log.d("Page Pagination", "Backward " + String.valueOf(StartPaging) + "  " + EndPaging);




            }
        });
        ib_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getContext(), "forward button", Toast.LENGTH_SHORT).show();
                StartPaging += 15;
                EndPaging += 15;
                setPLUPage(StartPaging,EndPaging);

                Log.d("Page Pagination", "Forward " + String.valueOf(StartPaging) + "  " + EndPaging);



            }
        });

    }



    ArrayList<String> PLUList; // name
    private void PLUItem(int sCount){
        PLUList = new ArrayList<>();
        Log.d("TAG", "PLUItem: "+sCount);
        for(int x=1;x<=sCount;x++) {
            PLUList.add("PLU"+x);
            Log.d("TAG", "PLUItem: "+"PLU"+x);
        }
    }

    ArrayList<String> deptKeyList; // name
    private void DeptKeysItem(int sCount){
        deptKeyList = new ArrayList<>();
        Log.d("TAG", "deptKeys: "+sCount);
        for(int x=1;x<=sCount;x++) {
            if (x<10){
                deptKeyList.add("D0"+x);
            }
            else{
                deptKeyList.add("D"+x);
            }

           // Log.d("TAG", "DEptkeys: "+"PLU"+x);
        }
    }

    private void insertPLU(int id){
        if (PLUList.size()!=0) {
            ItemCursor = id-1;
            FinalItemQty = "1";
            addInvoice();
        }
    }

    private void InsertItemBcode(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // AlertDialog alert = builder.create();
        // List<Integer>al=new ArrayList<>();


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
        EditText et_quantity = alertLayout.findViewById(R.id.et_quantity);
        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                FinalItemQty = et_quantity.getText().toString().trim();


              //  ItemCursor = child.getId();
                addInvoice();
                FinalItemQty = "";
                alertDialog.dismiss();

            }
        });




    }
    private void InsertItemBcode2(){
        //auto insert

        FinalItemQty = "1";


        //  ItemCursor = child.getId();
        addInvoice();
        FinalItemQty = "";
      //  alertDialog.dismiss();

    }


    private void showingDiscountList() {
        int btnCounter=0;


        discountType="";
        DiscountAutoIDList= new ArrayList<>();
        DiscountIDList= new ArrayList<>(); //discountType for getting name/id
        DiscountNameList= new ArrayList<>();
        DiscountAmountList= new ArrayList<>();
        DiscountComputationList= new ArrayList<>();
        DiscountExcludeTaxList= new ArrayList<>();
        ProRatedTaxList=new ArrayList<>();
        DiscountTypeList= new ArrayList<>();
        DiscountCategoryList = new ArrayList<>();
        OpenDiscountList = new ArrayList<>();



        CheckItemDatabase3();
        int numberOfItem = totalDiscountButtonList;
//        LinearLayout rl = (LinearLayout) view.findViewById(R.id.linearlayout);
//        rl.removeAllViewsInLayout();
//
//        int totalPerRow=5;

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels/2/totalPerRow;

//
//        for (int i2 = 1; i2 <= 20; i2++) {
//            LinearLayout row2 = new LinearLayout(view.getContext());
//            row2.setOrientation(LinearLayout.HORIZONTAL);
//            row2.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
//
//
//
//            for (int j2 = 0; j2 < totalPerRow; j2++) {
//
//                rl.removeView(row2);
//                LinearLayout rootView = new LinearLayout(view.getContext());
//                rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                rootView.setOrientation(LinearLayout.VERTICAL);
//
//
//                btnCounter++;
//
//                if (btnCounter <= numberOfItem) {
//
//
//                    CardView cardView = new CardView(view.getContext());
//
////                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(width, width);
//                    cardView.setLayoutParams(cardViewParams);
//
//                    ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
//                    cardViewMarginParams.setMargins(2, 2, 2, 2);
//                    cardView.requestLayout();  //Dont forget this line
//
//
//                    View child = getLayoutInflater().inflate(R.layout.invoice_item_list2, null);//child.xml
//                    tv_itemName = child.findViewById(R.id.tv_itemName);
//                    // tv_itemName.setText(itemName.get(btnCounter-1));
//                    tv_itemName.setText(DiscountIDList.get(btnCounter-1));
//                    // linearLayoutButton = child.findViewById(R.id.linearlayoutInsert);
//                    LinearLayout ll_itemClick = child.findViewById(R.id.ll_itemClick);
//                    child.setId(btnCounter-1);
//
//
//                    ll_itemClick.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//
//                            discountType=DiscountIDList.get(child.getId());
//                            discountValue=DiscountAmountList.get(child.getId());
//                            discountExlude=DiscountExcludeTaxList.get(child.getId());
//                            ProRated = ProRatedTaxList.get(child.getId());
//                            discountCategory=DiscountCategoryList.get(child.getId());
//
//                          //  CustInfo.showDialog(getContext());
//
//
//                            Log.e("DISCOUNT TYPE SUBS",discountType.substring(0,3));
//
//                            //
//                            if (discountCategory.equals("SCD") ||  discountCategory.equals("PWD")){
//                                showDialog();
//                            }
//                            else{
//                                discountComputation();
//                            }
//
//                         //   showingItem();
//
//
//
//                            // showSCDiscount();
//
//
//
//                            //need authorization
//
//
//
//
//
//
//
//
//
//                        }
//                    });
//
//                    cardView.addView(child);
//
//
//                    rootView.addView(cardView);
//
//
//
//                }
//                row2.addView(rootView);
//
//            }
//            rl.addView(row2);
//        }
        invoice_discount_model po2=null;
        DiscountModelList.clear();

        for (int x=0;x<numberOfItem;x++){
         //   po2 = new invoice_discount_model(DiscountIDList.get(x),DiscountNameList.get(x));
               po2 = new invoice_discount_model(String.valueOf(x+1),DiscountNameList.get(x));
            DiscountModelList.addAll(Arrays.asList(new invoice_discount_model[]{po2}));
          //  Log.d("TAG", "itemcode: "+itemCode.get(x));

        }
        refreshButtonRecyclerviewDiscount();








    }
    ArrayList<String> ManagerList = new ArrayList<>();
    String ManagerSelection;

    private void showingManagerList() {

        ManagerSelection="";
        ManagerList.clear();

       ManagerList.add("REPRINT");
       ManagerList.add("RETURN AND EXCHANGE");
        ManagerList.add("ABZUTILS");

      // ManagerList.add("REFUND");
//        ManagerList.add("SUSPEND TRANSACTION");
//        ManagerList.add("RESUME TRANSACTION");





        FunctionList.clear();

        invoice_fragment_button_model po2=null;

        for (int x=0;x<ManagerList.size();x++){
            po2 = new invoice_fragment_button_model(x+1,ManagerList.get(x));
            FunctionList.addAll(Arrays.asList(new invoice_fragment_button_model[]{po2}));
           // Log.d("TAG", "itemcode: "+itemCode.get(x));

        }
        refreshButtonRecyclerviewCashier(2);



//        for (int i2 = 1; i2 <= 20; i2++) {
//            LinearLayout row2 = new LinearLayout(view.getContext());
//            row2.setOrientation(LinearLayout.HORIZONTAL);
//            row2.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
//
//
//
//            for (int j2 = 0; j2 < totalPerRow; j2++) {
//
//                rl.removeView(row2);
//                LinearLayout rootView = new LinearLayout(view.getContext());
//                rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                rootView.setOrientation(LinearLayout.VERTICAL);
//
//
//                btnCounter++;
//
//                if (btnCounter <= numberOfItem) {
//
//
//                    CardView cardView = new CardView(view.getContext());
//
////                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(width, width);
//                    cardView.setLayoutParams(cardViewParams);
//
//                    ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
//                    cardViewMarginParams.setMargins(2, 2, 2, 2);
//                    cardView.requestLayout();  //Dont forget this line
//
//
//                    View child = getLayoutInflater().inflate(R.layout.invoice_item_list2, null);//child.xml
//                    tv_itemName = child.findViewById(R.id.tv_itemName);
//                    // tv_itemName.setText(itemName.get(btnCounter-1));
//                    //tv_itemName.setText(DiscountIDList.get(btnCounter-1));
//                    tv_itemName.setText(ManagerList.get(btnCounter-1));
//                    // linearLayoutButton = child.findViewById(R.id.linearlayoutInsert);
//                    LinearLayout ll_itemClick = child.findViewById(R.id.ll_itemClick);
//                    child.setId(btnCounter-1);
//                  //  ManagerSelection=ManagerList.get(btnCounter-1);
//
//
//
//
//
//                    ll_itemClick.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            ManagerSelection = ManagerList.get(child.getId());
//                            if (ManagerSelection.equalsIgnoreCase("REPRINT")){
//                                Log.d("TEST","REPRINT");
//                                showDialogReprint();
//                            }
//                            if (ManagerSelection.equalsIgnoreCase("RETURN AND EXCHANGE")){
//                                Log.d("TEST","RETURN AND EXCHANGE");
//
//                                showDialogReturnExchange();
//
//
//                            }
//                            if (ManagerSelection.equalsIgnoreCase("REFUND")){
//                                Log.d("TEST","REFUND");
//                            }
//                            if (ManagerSelection.equalsIgnoreCase("SUSPEND TRANSACTION")){
//                                Log.d("TEST","SUSPEND ");
//                                SuspendTransaction();
//                            }
//                            if (ManagerSelection.equalsIgnoreCase("RESUME TRANSACTION")){
//                                Log.d("TEST"," RESUME");
//                                SelectResumeTransaction();
//                            }
//
//                           //
//
//
//
//                        }
//                    });
//
//                    cardView.addView(child);
//
//
//                    rootView.addView(cardView);
//
//
//
//                }
//                row2.addView(rootView);
//
//            }
//            rl.addView(row2);
//        }





    }

    ArrayList<invoice_fragment_button_model> FunctionList= new ArrayList<>();
    ArrayList<String>SelectlistCashier = new ArrayList<String>();
    private void showingCashierList() {

        ManagerSelection="";
        ManagerList.clear();

        ManagerList.add("ALL ITEM");
        ManagerList.add("SEARCH ITEM");
        // ManagerList.add("FAST MOVING ITEM");
        ManagerList.add("SUSPEND TRANS");
        ManagerList.add("RESUME TRANS");
        ManagerList.add("ADD ITEM");


        FunctionList.clear();
        //itemCode = new ArrayList<>();

        invoice_fragment_button_model po2=null;

        for (int x=0;x<ManagerList.size();x++){
            po2 = new invoice_fragment_button_model(x+1,ManagerList.get(x));
            FunctionList.addAll(Arrays.asList(new invoice_fragment_button_model[]{po2}));
           // Log.d("TAG", "itemcode: "+itemCode.get(x));

        }

        refreshButtonRecyclerviewCashier(1);
        Log.d("TAG", "showingCashierList: "+SelectlistCashier.toString());





//        ManagerList.add("RETURN AND EXCHANGE");
//        ManagerList.add("REFUND");
//        ManagerList.add("SUSPEND TRANSACTION");
//        ManagerList.add("RESUME TRANSACTION");



//        int btnCounter=0;
//        int numberOfItem = ManagerList.size();
//        LinearLayout rl = (LinearLayout) view.findViewById(R.id.linearlayout);
//        rl.removeAllViewsInLayout();
//
//        int totalPerRow=5;
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels/2/totalPerRow;


//        for (int i2 = 1; i2 <= 20; i2++) {
//            LinearLayout row2 = new LinearLayout(view.getContext());
//            row2.setOrientation(LinearLayout.HORIZONTAL);
//            row2.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
//
//
//
//            for (int j2 = 0; j2 < totalPerRow; j2++) {
//
//                rl.removeView(row2);
//                LinearLayout rootView = new LinearLayout(view.getContext());
//                rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                rootView.setOrientation(LinearLayout.VERTICAL);
//
//
//                btnCounter++;
//
//                if (btnCounter <= numberOfItem) {
//
//
//                    CardView cardView = new CardView(view.getContext());
//
////                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(width, width);
//                    cardView.setLayoutParams(cardViewParams);
//
//                    ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
//                    cardViewMarginParams.setMargins(2, 2, 2, 2);
//                    cardView.requestLayout();  //Dont forget this line
//
//
//                    View child = getLayoutInflater().inflate(R.layout.invoice_item_list2, null);//child.xml
//                    tv_itemName = child.findViewById(R.id.tv_itemName);
//                    // tv_itemName.setText(itemName.get(btnCounter-1));
//                    //tv_itemName.setText(DiscountIDList.get(btnCounter-1));
//                    tv_itemName.setText(ManagerList.get(btnCounter-1));
//                    // linearLayoutButton = child.findViewById(R.id.linearlayoutInsert);
//                    LinearLayout ll_itemClick = child.findViewById(R.id.ll_itemClick);
//                    child.setId(btnCounter-1);
//                    //  ManagerSelection=ManagerList.get(btnCounter-1);
//
//
//
//
//
//                    ll_itemClick.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            ManagerSelection = ManagerList.get(child.getId());
//                            if (ManagerSelection.equalsIgnoreCase("SEARCH ITEM")){
//                              //  Log.d("TEST","REPRINT");
//                               // showDialogReprint();
//                                showDialogMasterlist(0);
//                            }
//                            if (ManagerSelection.equalsIgnoreCase("ALL ITEM")){
//                                //  Log.d("TEST","REPRINT");
//                                // showDialogReprint();
//                                showDialogMasterlist(1);
//                            }
////                            if (ManagerSelection.equalsIgnoreCase("FAST MOVING ITEM")){
////                                //  Log.d("TEST","REPRINT");
////                                // showDialogReprint();
////                                showingItem();
////                            }
////                            if (ManagerSelection.equalsIgnoreCase("RETURN AND EXCHANGE")){
////                                Log.d("TEST","RETURN AND EXCHANGE");
////
////                                showDialogReturnExchange();
////
////
////                            }
////                            if (ManagerSelection.equalsIgnoreCase("REFUND")){
////                                Log.d("TEST","REFUND");
////                            }
//                            if (ManagerSelection.equalsIgnoreCase("SUSPEND TRANS")){
//                                Log.d("TEST","SUSPEND ");
//                                SuspendTransaction();
//                            }
//                            if (ManagerSelection.equalsIgnoreCase("RESUME TRANS")){
//                                Log.d("TEST"," RESUME");
//                                SelectResumeTransaction();
//                            }
//
//                            //
//
//
//
//                        }
//                    });
//
//                    cardView.addView(child);
//
//
//                    rootView.addView(cardView);
//
//
//
//                }
//                row2.addView(rootView);
//
//            }
//            rl.addView(row2);
//        }





    }


    private void refreshButtonRecyclerviewCashier(int switchModeButton){ //1-- cashier 2-- Manager

        rv_pluList  = getActivity().findViewById(R.id.rv_pluList);
        //   rv_prodList = findViewById(R.id.rv_prodList);
        rv_pluList.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.removeAllViews();
        rv_pluList.setLayoutManager(new GridLayoutManager(this.getContext(), 5));
        //rv_ItemMasterList.setLayoutManager(layoutManager);


        mAdapter=new RecyclerviewAdapterButton(this.getContext(),FunctionList,SelectlistCashier,switchModeButton);


        rv_pluList.setAdapter(mAdapter);


    }

    private void refreshButtonRecyclerviewDiscount(){

        rv_pluList  = getActivity().findViewById(R.id.rv_pluList);
        //   rv_prodList = findViewById(R.id.rv_prodList);
        rv_pluList.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.removeAllViews();
        rv_pluList.setLayoutManager(new GridLayoutManager(this.getContext(), 5));
        //rv_ItemMasterList.setLayoutManager(layoutManager);


        mAdapter=new RecyclerviewAdapterDiscount(this.getContext(),DiscountModelList);

        //  mAdapter=new invoice_button_function_recyclerviewadapter(this.getContext(),FunctionList,SelectlistCashier);
        rv_pluList.setAdapter(mAdapter);


//        final invoice_button_function_recyclerviewadapter adp = new invoice_button_function_recyclerviewadapter(this.getContext(),FunctionList,SelectlistCashier);
//        adp.setOnClickListener(new invoice_button_function_recyclerviewadapter.OnClickListener() {
//            @Override
//            public void onClick(String s) {
//                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
//            }
//        });

        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        //


    }



    public void ShowCashierFunctionButton(String s){


       // ManagerSelection = ManagerList.get(child.getId());
                            if (s.equalsIgnoreCase("SEARCH ITEM")){

                              //  showDialogMasterlist(0);
                                showingAllItem();
                            }
                            if (s.equalsIgnoreCase("ALL ITEM")){
                                //  Log.d("TEST","REPRINT");
                                // showDialogReprint();
                                showDialogMasterlist(1);
                            }

                            if (s.equalsIgnoreCase("SUSPEND TRANS")){
                                Log.d("TEST","SUSPEND ");
                                SuspendTransaction();
                            }
                            if (s.equalsIgnoreCase("RESUME TRANS")){
                                Log.d("TEST"," RESUME");
                                SelectResumeTransaction();
                            }
                            if (s.equalsIgnoreCase("ADD ITEM")){
                                Toast.makeText(getContext(), "ADD ITEM", Toast.LENGTH_SHORT).show();
                                SelectAddItem();

                            }

                            //


    }
    public void ShowManagerFunctionButton(String s){

      //  ManagerSelection = ManagerList.get(child.getId());
                            if (s.equalsIgnoreCase("REPRINT")){
                                Log.d("TEST","REPRINT");
                                showDialogReprint();
                            }
                            if (s.equalsIgnoreCase("RETURN AND EXCHANGE")){
                                Log.d("TEST","RETURN AND EXCHANGE");

                                showDialogReturnExchange();


                            }
                            if (s.equalsIgnoreCase("REFUND")){
                                Log.d("TEST","REFUND");
                            }
                            if (s.equalsIgnoreCase("SUSPEND TRANSACTION")){
                                Log.d("TEST","SUSPEND ");
                                SuspendTransaction();
                            }
                            if (s.equalsIgnoreCase("RESUME TRANSACTION")){
                                Log.d("TEST"," RESUME");
                                SelectResumeTransaction();
                            }
                            if (s.equalsIgnoreCase("ABZUTILS")){
                                Log.e("TAG", "ShowManagerFunctionButton: ABZUTILS" );

                                String targetPackageName = "com.abztrakinc.abzutils"; // Replace with the target app's package name
                                PackageManager pm = getContext().getPackageManager();
                                Intent launchIntent = pm.getLaunchIntentForPackage(targetPackageName);

                                if (launchIntent != null) {
                                    getContext().startActivity(launchIntent);
                                } else {
                                    // Log installed packages
                                    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                                    for (ApplicationInfo packageInfo : packages) {
                                        Log.d("TAG", "Installed package :" + packageInfo.packageName);
                                        Log.d("TAG", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                                    }

                                    // Try explicit intent as a fallback
                                    Intent explicitIntent = new Intent();
                                    explicitIntent.setComponent(new ComponentName(targetPackageName, "com.abztrakinc.abzutils.MainActivity")); // Adjust if your main activity class is different

                                    try {
                                        getContext().startActivity(explicitIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(getContext(), "Target app not installed or unable to start activity", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

        //


    }




    //for return and exchange

    public void showDialogReturnExchange(){



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_invoice_refund, null);
        builder.setView(alertLayout);





         alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final Button btn_Enter = alertLayout.findViewById(R.id.btn_Enter);
        final Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        final EditText et_invoiceNumber = alertLayout.findViewById(R.id.et_invoiceNumber);
        // final




        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_invoiceNumber.getText().toString().equalsIgnoreCase("null") || et_invoiceNumber.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "PLEASE INPUT INVOICE #", Toast.LENGTH_SHORT).show();
                }else {
                    String invoiceNo = (String.format("%010d", Integer.parseInt(et_invoiceNumber.getText().toString())));
                    String transactionId = "";
                    //check


                    if (invoiceNo.equals(null) || invoiceNo.equals("")) {
                        Toast.makeText(getActivity(), "NO INPUTTED INVOICE", Toast.LENGTH_SHORT).show();
                        Log.d("Invoice #", "invoice : null ");

                    } else {
                        Toast.makeText(getActivity(), "invoice number :" + invoiceNo, Toast.LENGTH_SHORT).show();
                        Log.d("Invoice #", "invoice :" + invoiceNo);


                        SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
                        Cursor transactionID = db.rawQuery("select TransactionID from FinalTransactionReport where ORTrans='" + invoiceNo + "'", null);
                        if (transactionID.getCount() != 0) {

                            while (transactionID.moveToNext()) {
                                Log.d("TransactionID #", "TransactionID :" + transactionID.getString(0));
                                transactionId = transactionID.getString(0);
                            }
                            alertDialog.dismiss();
                            showDialogReturnExchangeData(invoiceNo, transactionId);


                        } else {
                            Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_LONG).show();
                        }


                        db.close();

                    }
                }












            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



//        ImageButton btn_endShift = alertLayout.findViewById(R.id.btn_endShift);
//        ImageButton btn_zRead = alertLayout.findViewById(R.id.btn_zRead);
//
//        btn_endShift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // triggerRebirth(LoginActivity.class);
//                getActivity().finish();
//                Toast.makeText(builder.getContext(), "L O G O U T", Toast.LENGTH_SHORT).show();
//            }
//        });
//        btn_zRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
//                cashier_shift_Zread.printReport(getContext(),"0");
//
//
//
//
//
//                if (activateCode==1){
//                    getActivity().finish();
//                }
//
//
//
//
//            }
//        });


        alertDialog.show();














    }
    private void insertInvoiceReceiptReturn(String TransID,String Order_ID,String OrderName,String finalItemQty,String OrderPrice,String OrderPriceTotal,String TransTime,String Date,String discType,String Remarks) {

        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());



        myDb = new DatabaseHandler(getActivity());
        boolean isInserted = myDb.insertInvoiceReceipt( TransID,Order_ID, OrderName,finalItemQty,OrderPrice,OrderPriceTotal
                ,TransTime,Date,discType,Remarks );

        if (isInserted = true) {
            Toast.makeText(getActivity(), "INSERTED", Toast.LENGTH_SHORT).show();



        } else {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }


    }
    private void showDialogReturnExchangeData(String invoice,String transactionID) {



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_invoice_refund_data, null);

        builder.setView(alertLayout);
        // ReasonList.clear();
//        ReasonList.add("product defective/damage");
//        ReasonList.add("Expired");

        loadItemChangeList();






         alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final Button btn_Enter = alertLayout.findViewById(R.id.btn_Enter);
        final Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        TextView et_invoiceNumber = alertLayout.findViewById(R.id.et_invoiceNumber);
        TextView et_transID= alertLayout.findViewById(R.id.et_transID);
         rv_refund_list = alertLayout.findViewById(R.id.rv_refund_list);
         Spinner sp_reason = alertLayout.findViewById(R.id.sp_reason);
        tv_totalRefundAmount=alertLayout.findViewById(R.id.tv_totalRefundAmount);
        LinearLayout ll_invoice_refund = alertLayout.findViewById(R.id.ll_invoice_refund);
        // ll_invoice_refund.setVisibility(View.GONE);
        TextView tv_amountText = alertLayout.findViewById(R.id.tv_amountText);
        tv_amountText.setText("RETURN/EXCHANGE AMOUNT");
        TextView tv_spinnerText = alertLayout.findViewById(R.id.tv_spinnerText);
        tv_spinnerText.setText("ITEM EXCHANGE");
        tv_amountText.setVisibility(View.GONE);
        tv_spinnerText.setVisibility(View.GONE);
        sp_reason.setVisibility(View.GONE);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,ExchangeItemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_reason.setAdapter(adapter);
        sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        et_invoiceNumber.setText(invoice);
        et_transID.setText(transactionID);
        FillDataRV(transactionID,invoice);
       RefreshRV();

        // final




        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invoiceNo=(String.format("%010d",Integer.parseInt(et_invoiceNumber.getText().toString())));
                //check





                if (tv_totalRefundAmount.equals(null) || tv_totalRefundAmount.equals("")){
                    //  Toast.makeText(getActivity(), "NO INPUTTED INVOICE", Toast.LENGTH_SHORT).show();
                    //  Log.d("Invoice #", "invoice : null ");

                }
                else{





            Log.d("EXCHANGE TRANS ID",et_transID.getText().toString());
                    for (int x=0;x<finalItemList.size();x++){

                        Log.d("ITEM",finalItemList.get(x));

                        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
                        Cursor TempData = db2.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID ='"+et_transID.getText().toString()+"' and OrderID='"+finalItemList.get(x)+"'", null);
                        Cursor TempDataDiscount = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscount  where TransactionID ='"+et_transID.getText().toString()+"' and OrderID='"+finalItemList.get(x)+"'", null);
                        if (TempData.getCount()!=0){
                            while (TempData.moveToNext()){

                                insertInvoiceReceiptReturn(TempData.getString(0),
                                        TempData.getString(1),
                                        TempData.getString(2),
                                        String.valueOf(Integer.parseInt(TempData.getString(3))*-1),
                                        String.valueOf(Double.parseDouble(TempData.getString(4))*-1),
                                        String.valueOf(Double.parseDouble(TempData.getString(5))*-1),
                                        TempData.getString(6),
                                        TempData.getString(7),
                                        TempData.getString(8),
                                        TempData.getString(9));

                                if (TempDataDiscount.getCount()!=0){
                                     DatabaseHandler dbhandler = new DatabaseHandler(getActivity());
                                    while (TempDataDiscount.moveToNext()) {

                                        dbhandler.insertInvoiceReceiptDiscountTemp(
                                                TempDataDiscount.getString(0),

                                                TempDataDiscount.getString(1),
                                                TempDataDiscount.getString(2),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(3), 0) * -1),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(4), 0.0) * -1),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(5), 0.0) * -1),
                                                TempDataDiscount.getString(6),
                                                TempDataDiscount.getString(7),
                                                TempDataDiscount.getString(8),
                                                TempDataDiscount.getString(9),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(10), 0) * -1),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(11), 0.0) * -1),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(12), 0.0) * -1),
                                                String.valueOf(parseOrDefault(TempDataDiscount.getString(13), 0.0) * -1),
                                                TempDataDiscount.getString(14),
                                                TempDataDiscount.getString(15),
                                                TempDataDiscount.getString(16)
                                        );
                                    }


                                }


                            }
                            Log.d("REXCHANGE","TRANS " + et_transID.getText().toString());
                            Log.d("REXCHANGE","INV " + invoiceNo);
                            insertReturnExchangeTemp(et_transID.getText().toString(),invoiceNo);
                        }




                        db2.close();



                    }

                    alertDialog.dismiss();

                    refreshRecycleview();
                    fillOrderList();
                    checkTotalAmount();
                  //  showingItem();



                }








            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



        alertDialog.show();




    }

    private int parseOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    private void insertReturnExchangeTemp(String TransNo,String InvNo){
        deleteReturnExchangeTemp();
        Log.d("REXCHANGE2Trans","TRANS " + TransNo );
        Log.d("REXCHANGE2INV", "INV " + InvNo);
        DatabaseHandler dbhandler = new DatabaseHandler(getActivity());
        dbhandler.insertReturnExchangeTemp(TransNo,InvNo);

    }
    private void deleteReturnExchangeTemp(){
        SQLiteDatabase PosOutputDB = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        String InvoiceReturnExchangeTemp= "delete from ReturnExchangeTemp";
        PosOutputDB.execSQL(InvoiceReturnExchangeTemp);
        PosOutputDB.close();
    }


    //suspend and resume function
    private void checkIfInvoiceSuspendNotEmpty(){

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor query = db2.rawQuery("select * from InvoiceReceiptTotalSuspend", null);
        if (query.getCount()!=0){
            while (query.moveToNext()){
                DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
                dbHandler.insertInvoiceReceiptTotal(query.getString(0),
                        query.getString(1),
                        query.getString(2),
                        query.getString(3),
                        query.getString(4),
                        query.getString(5),
                        query.getString(6),
                        query.getString(7),
                        query.getString(8),
                        query.getString(9),
                        query.getString(10),
                        query.getString(11),
                        query.getString(12));
            }
        }

        String strsql = "delete From InvoiceReceiptTotalSuspend ";
        db2.execSQL(strsql);
        db2.close();

    }
    private void SuspendTransaction(){

        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = Calendar.getInstance().getTime();
        or_trans_item = new OR_TRANS_ITEM();
        or_trans_item.readReferenceNumber(getContext()); //read Transaction before clicking xread
        DatabaseHandler databaseHandler=new DatabaseHandler(getContext());
        shift_active shiftActive= new shift_active();
        system_final_date sysDate = new system_final_date();
        sysDate.insertDate(getContext());
        //  Log.e("Transaction Number",or_trans_item.getTransactionNo());

//
        databaseHandler.insertInvoiceReceiptTotal(or_trans_item.getTransactionNo(),"","",""
                ,"","","","","SUSPEND",sysDate.getSystemDate(),timeOnly.format(currentDate.getTime()),shiftActive.getActiveUserID(),shiftActive.getShiftActive());
        //  readReferenceNumber(context); //read Transaction after clicking xread
//


        createReferenceTextfile(or_trans_item.getTransactionNo());




        MoveInvoiceReceiptItemSuspend();
        MoveInvoiceReceiptItemWDiscountSuspend();
        checkIfInvoiceSuspendNotEmpty();

        this.getActivity().finish();
        this.getActivity().overridePendingTransition(0,0);
        startActivity(getActivity().getIntent());
        this.getActivity().overridePendingTransition(0,0);


    }

    private String convertdate(String finalDate){
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            String inputDateStr = finalDate;
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            return outputDateStr;
        }
        catch (Exception ex){
            Log.e("convertdate","ERROR");
        }
        return finalDate;
    }

    private void createReferenceTextfile(String TransactionNo){
        StringBuffer buffer = new StringBuffer();
        buffer.append("--------------------------------"+"\n");
        buffer.append("       SUSPEND TRANSACTION      "+"\n");
        buffer.append("--------------------------------"+"\n");
        buffer.append("Date : "+ convertdate(tv_date.getText().toString())  +"\n");
        buffer.append("Time :"+ _tvTime.getText().toString() +"\n");
        buffer.append("TRANSACTION No. : "+ TransactionNo +"\n");
        buffer.append("Cashier "+ tv_userID.getText().toString() +"\n");
        buffer.append("--------------------------------"+"\n");

        printer_settings_class print = new printer_settings_class(getActivity());
        print.OnlinePrinter(buffer.toString(),1,1,1);

    }


    private void MoveInvoiceReceiptItemSuspend(){

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor SuspendTransaction = db2.rawQuery("select * from InvoiceReceiptItem", null);
        if (SuspendTransaction.getCount()!=0){
            while (SuspendTransaction.moveToNext()){
                DatabaseHandler dbhandler=new DatabaseHandler(getActivity());
                dbhandler.insertInvoiceReceiptSuspend(SuspendTransaction.getString(0),
                        SuspendTransaction.getString(1),
                        SuspendTransaction.getString(2),
                        SuspendTransaction.getString(3),
                        SuspendTransaction.getString(4),
                        SuspendTransaction.getString(5),
                        SuspendTransaction.getString(6),
                        SuspendTransaction.getString(7),
                        SuspendTransaction.getString(8),
                        SuspendTransaction.getString(9));
            }
        }
        //delete temp data
        String strsql = "delete From InvoiceReceiptItem";
        db2.execSQL(strsql);


        db2.close();


    }
    private void MoveInvoiceReceiptItemWDiscountSuspend(){

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor SuspendTransaction = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp", null);
        if (SuspendTransaction.getCount()!=0){
            while (SuspendTransaction.moveToNext()){
                DatabaseHandler dbhandler=new DatabaseHandler(getActivity());
                dbhandler.insertInvoiceReceiptDiscountSuspend(SuspendTransaction.getString(0),
                        SuspendTransaction.getString(1),
                        SuspendTransaction.getString(2),
                        SuspendTransaction.getString(3),
                        SuspendTransaction.getString(4),
                        SuspendTransaction.getString(5),
                        SuspendTransaction.getString(6),
                        SuspendTransaction.getString(7),
                        SuspendTransaction.getString(8),
                        SuspendTransaction.getString(9),
                        SuspendTransaction.getString(10),
                        SuspendTransaction.getString(11),
                        SuspendTransaction.getString(12),
                        SuspendTransaction.getString(13),
                        SuspendTransaction.getString(14),
                        SuspendTransaction.getString(15),
                        SuspendTransaction.getString(16));
            }
        }
        //delete temp data
        String strsql = "delete From InvoiceReceiptItemFinalWDiscountTemp";
        db2.execSQL(strsql);


        db2.close();


    }


    ArrayList<String> SuspendTransactionList = new ArrayList<>();
    ArrayList<String> SuspendTransactionListDate = new ArrayList<>();
    ArrayList<String> SuspendTransactionListTime = new ArrayList<>();



    private void LoadSuspendedTransaction(){
        invoice_transaction_model po = null;
        SuspendTransactionList.clear();
        SuspendTransactionListDate.clear();
        SuspendTransactionListTime.clear();
        arraylistTransaction.clear();
        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor SuspendTransaction = db2.rawQuery("select * from InvoiceReceiptTotal where typeOfTransaction='"+"SUSPEND"+"'", null);
        if (SuspendTransaction.getCount()!=0){
            while (SuspendTransaction.moveToNext()){
                SuspendTransactionList.add(SuspendTransaction.getString(0));
                SuspendTransactionListDate.add(SuspendTransaction.getString(9));
                SuspendTransactionListTime.add(SuspendTransaction.getString(10));


            }

        }

        for (int x =0; x< SuspendTransactionList.size();x++){
            po = new invoice_transaction_model(x+1,SuspendTransactionList.get(x),SuspendTransactionListDate.get(x),SuspendTransactionListTime.get(x));
            arraylistTransaction.addAll(Arrays.asList(new invoice_transaction_model[]{po}));
        }

        db2.close();
    }


//   for (int x=0;x<BankListDebit.size();x++){
//        po2 = new invoice_fragment_button_model(x+1,BankListDebit.get(x));
//        FunctionList.addAll(Arrays.asList(new invoice_fragment_button_model[]{po2}));
//        // Log.d("TAG", "itemcode: "+itemCode.get(x));
//
//    }


  //  String ResumeTransID;


    RecyclerView rv_transactionList;
    EditText et_command;



    private void SelectResumeTransaction(){
        InvoiceCursor=2;
        LoadSuspendedTransaction();
        AlertDialog.Builder builder  = new AlertDialog.Builder(this.getActivity(),R.style.DialogSlide);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_resume_transaction, null);

        builder.setView(alertLayout);
         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        et_command= alertLayout.findViewById(R.id.et_command);

        Button btn_Resume = alertLayout.findViewById(R.id.btn_Print);
        Button btn_Cancel = alertLayout.findViewById(R.id.btn_Cancel);

//        Spinner TransactionSpinner = alertLayout.findViewById(R.id.TransactionSpinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,SuspendTransactionList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        TransactionSpinner.setAdapter(adapter);
//        TransactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                TransReprint = TransactionSpinner.getSelectedItem().toString();
//
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


        rv_transactionList = alertLayout.findViewById(R.id.rv_transactionList);
        rv_transactionList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.removeAllViews();
        rv_transactionList.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mAdapter=new cashier_invoice.RecyclerviewAdapterResumeTrans(this.getContext(),arraylistTransaction);
        rv_transactionList.setAdapter(mAdapter);




        btn_Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               MoveGreaterTransaction(TransReprint);
                ReturnInvoiceReceiptItemSuspend(TransReprint);
                ReturnInvoiceReceiptItemWDiscountSuspend(TransReprint);
                InvoiceCursor=0;
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                InvoiceCursor=0;
            }
        });

        alertDialog = builder.create();

        alertDialog.  getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations=R.anim.slide_left;











        alertDialog.show();


    }



//ad new item
    ArrayList<String>  subCatgID=new ArrayList<>();
    ArrayList<String>  catgID=new ArrayList<>();
    String finalSubCatg;
    String finalCatg="";
    private void SelectAddItem(){

       // InvoiceCursor=1000;
        LoadSuspendedTransaction();
        AlertDialog.Builder builder  = new AlertDialog.Builder(this.getActivity(),R.style.DialogSlide);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_add_new_item, null);


        builder.setView(alertLayout);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);


        subCatgID.clear();
        catgID.clear();
        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        Cursor POSItem = db2.rawQuery("select * from SUBCATEGORY", null);
        if (POSItem.getCount()!=0){
            while (POSItem.moveToNext()){
                subCatgID.add(POSItem.getString(0));
                //     ExchangeItemList.add((POSItem.getString(0) + "     ").substring(0,5) + (POSItem.getString(1) + "--------------------").substring(0,20) + (POSItem.getString(3) + "----------").substring(0,10));
                catgID.add(POSItem.getString(3));

            }

        }
        db2.close();




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,subCatgID);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp_subCatgID = alertLayout.findViewById(R.id.sp_subCatgID);
        sp_subCatgID.setSelection(0);
        sp_subCatgID.setAdapter(adapter);
        sp_subCatgID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
                finalSubCatg = subCatgID.get(sp_subCatgID.getSelectedItemPosition());
                finalCatg = catgID.get(sp_subCatgID.getSelectedItemPosition());

                Toast.makeText(getActivity(), "finalSubCatg "+finalSubCatg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText et_ItemID = alertLayout.findViewById(R.id.et_ItemID);
        EditText et_ItemName = alertLayout.findViewById(R.id.et_ItemName);
        EditText et_ItemAmt = alertLayout.findViewById(R.id.et_ItemAmount);

        RadioGroup radioGroup = alertLayout.findViewById(R.id.radio_group_id);

// Get the ID of the selected RadioButton
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String priceOverride;

// If no RadioButton is selected, selectedRadioButtonId will be -1
        if (selectedRadioButtonId != -1) {
            // Find the selected RadioButton
            RadioButton selectedRadioButton = alertLayout.findViewById(selectedRadioButtonId);

            // Get the text of the selected RadioButton
            String selectedText = selectedRadioButton.getText().toString();

            // Use the selected text as needed
            // For example, show it in a Toast
            Toast.makeText(getActivity(), "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
            priceOverride="yes";
        } else {
            priceOverride="no";
            // No RadioButton is selected
            // Handle this case if needed
        }


        Button btn_addNewItem = alertLayout.findViewById(R.id.btn_addNewItem);
        btn_addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_ItemAmt.getText().toString().isEmpty()) {
                    et_ItemAmt.setText("0.00");
                }
                POSAndroidDB posAndroidDB = new POSAndroidDB(getContext());
                boolean isInserted = posAndroidDB.insertProduct(
                        et_ItemID.getText().toString(),
                        et_ItemName.getText().toString(),
                        et_ItemName.getText().toString(),
                        et_ItemAmt.getText().toString(),
                        "1",
                        " ",
                        finalCatg,
                        finalSubCatg,
                        priceOverride,
                        "VATable"





                );
                if (isInserted = true)
                {
                    Toast.makeText(getContext(), finalCatg+","+finalSubCatg+","+et_ItemID.getText().toString()+","+et_ItemName.getText().toString()+","+et_ItemAmt.getText().toString()+","+priceOverride, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    showingAllItem();

                }
            }
        });


















        alertDialog = builder.create();

        alertDialog.  getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations=R.anim.slide_left;

        alertDialog.show();






    }




    private void MoveGreaterTransaction(String TransID){
        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor moveGreater = db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID >= '"+TransID+"'", null);
        if (moveGreater.getCount()!=0){
            while (moveGreater.moveToNext()){
               // SuspendTransactionList.add(moveGreater.getString(0));
               // Log.d("TRANSACTION",moveGreater.getString(0));
                DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
                dbHandler.insertInvoiceReceiptTotalSuspend(moveGreater.getString(0),
                        moveGreater.getString(1),
                        moveGreater.getString(2),
                        moveGreater.getString(3),
                        moveGreater.getString(4),
                        moveGreater.getString(5),
                        moveGreater.getString(6),
                        moveGreater.getString(7),
                        moveGreater.getString(8),
                        moveGreater.getString(9),
                        moveGreater.getString(10),
                        moveGreater.getString(11),
                        moveGreater.getString(12));
            }

            String strsql = "delete From InvoiceReceiptTotal  where TransactionID >= '"+TransID+"'";
            db2.execSQL(strsql);

            this.getActivity().finish();
            this.getActivity().overridePendingTransition(0,0);
            startActivity(getActivity().getIntent());
            this.getActivity().overridePendingTransition(0,0);


        }
        else{
            Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        db2.close();
    }

    private void ReturnInvoiceReceiptItemSuspend(String TransID){

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor SuspendTransaction = db2.rawQuery("select * from InvoiceReceiptItemSuspend where TransactionID='"+TransID+"'", null);
        if (SuspendTransaction.getCount()!=0){
            while (SuspendTransaction.moveToNext()){
                DatabaseHandler dbhandler=new DatabaseHandler(getActivity());
                dbhandler.insertInvoiceReceipt(SuspendTransaction.getString(0),
                        SuspendTransaction.getString(1),
                        SuspendTransaction.getString(2),
                        SuspendTransaction.getString(3),
                        SuspendTransaction.getString(4),
                        SuspendTransaction.getString(5),
                        SuspendTransaction.getString(6),
                        SuspendTransaction.getString(7),
                        SuspendTransaction.getString(8),
                        SuspendTransaction.getString(9));
            }
        }
        //delete temp data
        String strsql = "delete From InvoiceReceiptItemSuspend where TransactionID='"+TransID+"'";
        db2.execSQL(strsql);


        db2.close();


    }
    private void ReturnInvoiceReceiptItemWDiscountSuspend(String TransID){

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor SuspendTransaction = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountSuspend where TransactionID='"+TransID+"'", null);
        if (SuspendTransaction.getCount()!=0){
            while (SuspendTransaction.moveToNext()){
                DatabaseHandler dbhandler=new DatabaseHandler(getActivity());
                dbhandler.insertInvoiceReceiptDiscountTemp(SuspendTransaction.getString(0),
                        SuspendTransaction.getString(1),
                        SuspendTransaction.getString(2),
                        SuspendTransaction.getString(3),
                        SuspendTransaction.getString(4),
                        SuspendTransaction.getString(5),
                        SuspendTransaction.getString(6),
                        SuspendTransaction.getString(7),
                        SuspendTransaction.getString(8),
                        SuspendTransaction.getString(9),
                        SuspendTransaction.getString(10),
                        SuspendTransaction.getString(11),
                        SuspendTransaction.getString(12),
                        SuspendTransaction.getString(13),
                        SuspendTransaction.getString(14),
                        SuspendTransaction.getString(15),
                        SuspendTransaction.getString(16));
            }
        }
        //delete temp data
        String strsql = "delete From InvoiceReceiptItemFinalWDiscountSuspend where TransactionID='"+TransID+"'";
        db2.execSQL(strsql);


        db2.close();


    }


    ArrayList<String> ExchangeItemID=new ArrayList<>();
    ArrayList<String> ExchangeItemName=new ArrayList<>();
    ArrayList<String> ExchangeItemPrice=new ArrayList<>();
    ArrayList<String> ExchangeItemList=new ArrayList<>();



    private void loadItemChangeList(){

        ExchangeItemName.clear();
        ExchangeItemPrice.clear();
        ExchangeItemID.clear();
        ExchangeItemList.clear();

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        Cursor POSItem = db2.rawQuery("select * from ITEM", null);
        if (POSItem.getCount()!=0){
            while (POSItem.moveToNext()){
                ExchangeItemID.add(POSItem.getString(0));
                ExchangeItemName.add(POSItem.getString(1));
                ExchangeItemPrice.add(POSItem.getString(3));
                //     ExchangeItemList.add((POSItem.getString(0) + "     ").substring(0,5) + (POSItem.getString(1) + "--------------------").substring(0,20) + (POSItem.getString(3) + "----------").substring(0,10));
                ExchangeItemList.add((POSItem.getString(1) + "                              ").substring(0,30) + "\n" +(POSItem.getString(3) + "          ").substring(0,10));

            }

        }
        db2.close();

    }




        //note!
        //
        ArrayList<String> ReprintTransactionList= new ArrayList<>();
    ArrayList<String> ReprintTransactionListDate = new ArrayList<>();
    ArrayList<String> ReprintTransactionListTime = new ArrayList<>();
    private void loadTransactionNumber(){
        invoice_transaction_model po = null;
        ReprintTransactionList.clear();
        ReprintTransactionListDate.clear();
        ReprintTransactionListTime.clear();
        arraylistTransaction.clear();

        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select TransactionID,TransDate,TransTime,typeOfTransaction from InvoiceReceiptTotal where TransDate='"+tv_date.getText().toString()+"'", null);
        if (itemListC.getCount()!=0){
            while (itemListC.moveToNext()){
                if (itemListC.getString(3).contains("REPRINT")){

                }
                else{
                    ReprintTransactionList.add(itemListC.getString(0));
                    ReprintTransactionListDate.add(itemListC.getString(1));
                    ReprintTransactionListTime.add(itemListC.getString(2));
                }

            }




        }
        else{
            Toast.makeText(getContext(), "NO TRANSACTION FOUND", Toast.LENGTH_SHORT).show();
        }

        if (ReprintTransactionList.size()!=0) {
            for (int x = 0; x < ReprintTransactionList.size(); x++) {
                Log.d("TAG", "loadTransactionNumber: " + ReprintTransactionList.get(x));
                po = new invoice_transaction_model(x+1,ReprintTransactionList.get(x),ReprintTransactionListDate.get(x),ReprintTransactionListTime.get(x));
                arraylistTransaction.addAll(Arrays.asList(new invoice_transaction_model[]{po}));
            }
        }







    }

    String TransReprint;



    //broadcast for changing TransNumber

    //end broadcast

    public interface myBroadcastListener{
        public void dosomething(String result);
    }



//note!
TextView tv_DateToDay;
    EditText et_ReprintQty;
    public void showDialogReprint(){


        InvoiceCursor=3;
        TransactionList= new ArrayList<>();
        loadTransactionNumber();
        TransReprint="";







        AlertDialog.Builder builder  = new AlertDialog.Builder(this.getContext(),R.style.DialogSlide);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_reprint, null);

        builder.setView(alertLayout);
         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
         et_ReprintQty= alertLayout.findViewById(R.id.et_PrintQty);
         tv_DateToDay = alertLayout.findViewById(R.id.tv_DateToDay);
        tv_DateToDay.setText(tv_date.getText().toString());
        Button btn_Print = alertLayout.findViewById(R.id.btn_Print);
        Button btn_Cancel = alertLayout.findViewById(R.id.btn_Cancel);
        et_command = alertLayout.findViewById(R.id.et_command);

        rv_transactionList = alertLayout.findViewById(R.id.rv_transactionList);
//        rv_transactionList.setFocusable(false);
//        rv_transactionList.setFocusableInTouchMode(false);
        rv_transactionList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.removeAllViews();
        rv_transactionList.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mAdapter=new cashier_invoice.RecyclerviewAdapterResumeTrans(this.getContext(),arraylistTransaction);
        rv_transactionList.setAdapter(mAdapter);


        et_command.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                String command = et_command.getText().toString().trim();
                Toast.makeText(this.getContext(), command, Toast.LENGTH_SHORT).show();
                // Clear text after processing command

                int qty;
                if (et_ReprintQty.getText().length()==0){
                    qty=1;
                }
                else{
                    qty = Integer.parseInt(et_ReprintQty.getText().toString());
                }
//                TransReprint = arraylistTransaction.get(position).getTransaction();
                ReprintData(tv_DateToDay.getText().toString(),TransReprint,qty);
                InsertTransaction(TransReprint);
                InvoiceCursor=0;
                alertDialog.dismiss();
                et_command.setText("");

              //  et_command.requestFocus(); // Request focus back to et_command

                return true;
            }
            return false;
        });


//




        //int showMessage=0;

        btn_Print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TransReprint=="" || TransReprint.equals("")){
                    Toast.makeText(getContext(), "PLEASE SELECT TRANSACTION", Toast.LENGTH_SHORT).show();
                }
                else{
                    int qty;
                    if (et_ReprintQty.getText().length()==0){
                        qty=1;
                    }
                    else{
                        qty = Integer.parseInt(et_ReprintQty.getText().toString());
                    }
                    ReprintData(tv_DateToDay.getText().toString(),TransReprint,qty);
                    InsertTransaction(TransReprint);
                    InvoiceCursor=0;
                    alertDialog.dismiss();
                }




            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                InvoiceCursor=0;

            }
        });

        alertDialog.  getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations=R.anim.slide_left;

        alertDialog.show();

    }



    private void checkHeaderCount(){
        db2 = getActivity().openOrCreateDatabase(DB_NAME4, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from receiptHeader", null);
        if (itemListC.getCount()!=0){

               Log.e("Header Count",String.valueOf(itemListC.getCount()));
        }
        else{
            Log.e("Header Count","0");
        }


    }

    private void ReprintData(String date,String Transaction,int qty){
        try {

            checkHeaderCount();
            int ctrH=0;
            File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");

            File file = new File(eJournal+"/"+date+Transaction+".txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String text = "";
            String formattedStr =
                              "===============================" + "\n"
                            + "          R E P R I N T        " + "\n"
                            + "==============================="
                            + "\n";
            while ((line = br.readLine()) != null) {
                ctrH++;
                //process the line
                if (ctrH==12){
                    text = text + formattedStr ;
                }
                System.out.println(line);
                //  tv_journalText.setText(line);
                text = text + line + "\n";
            }
            String Reprint;

            Log.e("REPRINT",text);
            printer_settings_class print = new printer_settings_class(this.getContext());
          //  for (int x=0;x<qty;x++){
                print.OnlinePrinter(text,qty,0,1);
            //}






        }
        catch(Exception ex){
            Toast.makeText(getContext(), "Error : No sdcard found", Toast.LENGTH_SHORT).show();
            Log.e("Error Showing Journal", ex.getMessage());








        }

    }
    private void InsertTransaction(String Transaction){
        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = Calendar.getInstance().getTime();
        or_trans_item = new OR_TRANS_ITEM();
        or_trans_item.readReferenceNumber(getContext()); //read Transaction before clicking xread
        DatabaseHandler databaseHandler=new DatabaseHandler(getContext());
        shift_active shiftActive= new shift_active();
        system_final_date sysDate = new system_final_date();
        sysDate.insertDate(getContext());
        //  Log.e("Transaction Number",or_trans_item.getTransactionNo());

//
        databaseHandler.insertInvoiceReceiptTotal(or_trans_item.getTransactionNo(),"","",""
                ,"","","","","REPRINT("+Transaction+")",sysDate.getSystemDate(),timeOnly.format(currentDate.getTime()),tv_userID.getText().toString(),tv_shift.getText().toString());
        //  readReferenceNumber(context); //read Transaction after clicking xread
//

        this.getActivity().finish();
        this.getActivity().overridePendingTransition(0,0);
        startActivity(getActivity().getIntent());
        this.getActivity().overridePendingTransition(0,0);


    }



        //note!!!
    public void PostMessage(int nMsg, String strMessage) {
        Log.i("TAG", String.valueOf(nMsg) +" "+strMessage);
        Log.i("POSTMESSAGEs",strMessage);
        //  editTextTextPersonName.setText(strMessage);



         if (InvoiceCursor==5){

            if (et_custName.hasFocus()){
                int start = Math.max(et_custName.getSelectionStart(), 0);
                int end = Math.max(et_custName.getSelectionEnd(), 0);
                et_custName.getText().replace(Math.min(start, end), Math.max(start, end),
                        strMessage, 0, strMessage.length());



            }
            else if (et_custIDNo.hasFocus()){
                int start = Math.max(et_custIDNo.getSelectionStart(), 0);
                int end = Math.max(et_custIDNo.getSelectionEnd(), 0);
                et_custIDNo.getText().replace(Math.min(start, end), Math.max(start, end),
                        strMessage, 0, strMessage.length());
            }
            else if (et_custTIN.hasFocus()){
                int start = Math.max(et_custTIN.getSelectionStart(), 0);
                int end = Math.max(et_custTIN.getSelectionEnd(), 0);
                et_custTIN.getText().replace(Math.min(start, end), Math.max(start, end),
                        strMessage, 0, strMessage.length());
            }


        }
        if (InvoiceCursor==6){

//            EditText et_reserveName;
//            EditText et_reserveDesc;
//            EditText et_reserveAmount;

            if (et_reserveName.hasFocus()){
                int start = Math.max(et_reserveName.getSelectionStart(), 0);
                int end = Math.max(et_reserveName.getSelectionEnd(), 0);
                et_reserveName.getText().replace(Math.min(start, end), Math.max(start, end),
                        strMessage, 0, strMessage.length());



            }
            else if (et_reserveDesc.hasFocus()){
                int start = Math.max(et_reserveDesc.getSelectionStart(), 0);
                int end = Math.max(et_reserveDesc.getSelectionEnd(), 0);
                et_reserveDesc.getText().replace(Math.min(start, end), Math.max(start, end),
                        strMessage, 0, strMessage.length());
            }
            else if (et_reserveAmount.hasFocus()){
                int start = Math.max(et_reserveAmount.getSelectionStart(), 0);
                int end = Math.max(et_reserveAmount.getSelectionEnd(), 0);
                et_reserveAmount.getText().replace(Math.min(start, end), Math.max(start, end),
                        strMessage, 0, strMessage.length());
            }


        }







    }





    EditText et_custName;
    EditText et_custIDNo;
    EditText et_custTIN;
    Button btn_saveCustInfo;
    //note!!

    public void moveEdittext(int nMsg,String message){


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mInfo.setText(str);
                //Log.i("moveEdittext", String.valueOf(nMsg) +" "+message);

                if (InvoiceCursor==5) {



                    if (et_custName.hasFocus()) {
                        Log.e("Enter key", "proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_custIDNo.requestFocus();
                    } else if (et_custIDNo.hasFocus()) {
                        //Log.e("Enter key","proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_custTIN.requestFocus();
                    } else if (et_custTIN.hasFocus()) {
                        Log.e("Enter key", "et_custTin");
                        btn_saveCustInfo.setFocusableInTouchMode(true);
                        btn_saveCustInfo.requestFocus();

                    }
                    else if(btn_saveCustInfo.hasFocus()){

//                        addedCustomerName = et_custName.getText().toString();
//                        addedCustomerAddress = et_custIDNo.getText().toString();
//                        addedCustomerTin = et_custTIN.getText().toString();
//                        alertDialog.dismiss();
//                        mapCode2Activate=0;

                    }
                    else{
                        alertDialog.dismiss();
                       // mapCode2Activate=0;
                        InvoiceCursor=0;
                    }
                }

            }
        });



        // Log.i("TAG", String.valueOf(nMsg) +" "+message);
        //  Log.i("POSTMESSAGEs",strMessage);


    }

    public void showDialog(){


        //mapCode2Activate=3;
        InvoiceCursor=5;
        InitT9MapCode();



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_scd_pwd_info, null);

        builder.setView(alertLayout);
         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
         et_custName= alertLayout.findViewById(R.id.et_custName);
         et_custIDNo= alertLayout.findViewById(R.id.et_custIDNo);
         et_custTIN= alertLayout.findViewById(R.id.et_custTIN);
         btn_saveCustInfo = alertLayout.findViewById(R.id.btn_saveCustInfo);
        Button btn_cancelCustInfo = alertLayout.findViewById(R.id.btn_cancelCustInfo);

        et_custName.requestFocus();

        //int showMessage=0;

        btn_saveCustInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustInfo.setCustName(et_custName.getText().toString());
                CustInfo.setCustIDNo((et_custIDNo.getText().toString()));
                CustInfo.setCustTIN((et_custTIN.getText().toString()));
                discountComputation();
                Toast.makeText(getContext(), discountType + " Applied", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                InvoiceCursor=0;


            }
        });

        btn_cancelCustInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CustInfo.setCustName(et_custName.getText().toString());
//                CustInfo.setCustTIN((et_custIDNo.getText().toString()));
//                CustInfo.setCustTIN((et_custTIN.getText().toString()));
//                discountComputation();
                Toast.makeText(getContext(), discountType + " Canceled", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                InvoiceCursor=0;


            }
        });












        alertDialog.show();

    }

    private void showCashierFunction() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // AlertDialog alert = builder.create();
        // List<Integer>al=new ArrayList<>();


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
        EditText et_quantity = alertLayout.findViewById(R.id.et_quantity);
        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    private void showingItem2(){
        LinearLayout rl = (LinearLayout) view.findViewById(R.id.linearlayout);
        // showOrderInvoice();
        itemCode = new ArrayList<>();
        itemName = new ArrayList<>();
        recptDesc = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemQuantity = new ArrayList<>();

        CheckItemDatabase();


        int numberOfItem = itemListC.getCount();

        for ( i=0;i<numberOfItem;i++) {
            CardView cardView = new CardView(getActivity());
            LinearLayout.LayoutParams layoutparams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,50
            );

            cardView.setLayoutParams(layoutparams2);
            //cardView.setRadius(0);
            cardView.setPadding(25, 25, 25 ,25);


            LinearLayout rootViewI = new LinearLayout(getActivity());
            rootViewI.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rootViewI.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout rootViewPrice = new LinearLayout(getActivity());
            rootViewPrice.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rootViewPrice.setOrientation(LinearLayout.HORIZONTAL);

            for (int ii=0;ii<1;ii++){

                TextView TextView = new TextView(getActivity());
                TextView.setText("NAME");
                TextView.setId(i);

                rootViewI.addView(TextView);

                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                TextView TextView2 = new TextView(getActivity());
                TextView2.setLayoutParams(layoutparams);
                //cardView.setRadius(0);
                TextView2.setPadding(50, 0, 0, 0);


                TextView2.setText(itemName.get(i));
                rootViewI.addView(TextView2);


                LinearLayout.LayoutParams layoutparams3 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                TextView TextView3 = new TextView(getActivity());
                TextView3.setLayoutParams(layoutparams3);
                //cardView.setRadius(0);
                TextView3.setPadding(50, 0, 0, 0);


                TextView3.setText("P "+itemPrice.get(i));
                cardView.setId(i);





                rootViewI.addView(TextView3);


                cardView.addView(rootViewI);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ItemCursor=cardView.getId();
                        addInvoice();
                        etProductID.clearFocus();

                    }
                });
                rl.removeView(cardView);


            }

            rl.addView(cardView);


        }


    }


    private void showRemarksDialog() {
        readReferenceNumber();
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
        // AlertDialog alert = builder.create();
        builder.setTitle("INPUT REMARKS");
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_remarks, null);
        builder.setView(alertLayout);


        et_remarks =(EditText) alertLayout.findViewById(R.id.et_remarks);
        btn_insert =(Button) alertLayout.findViewById(R.id.btn_insert);
        btn_deleteRemarks = alertLayout.findViewById(R.id.btn_deleteRemarks);

         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();

        btn_insert = alertLayout.findViewById(R.id.btn_insert);


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    for (int i=0;i<selectList.size();i++){
                        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                        String strsql = "UPDATE InvoiceReceiptItem SET ItemRemarks='" + et_remarks.getText().toString() + "'  where TransactionID='" + readRefNumber + "' and OrderID='"+String.valueOf(selectList.get(i))+"'";
                        db2.execSQL(strsql);
                    }
                    refreshRecycleview();
                    fillOrderList();

                    alertDialog.dismiss();
                Toast.makeText(getActivity(), "REMARKS UPDATED", Toast.LENGTH_SHORT).show();




            }
        });
        String NULL="";

        btn_deleteRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<selectList.size();i++) {
                    db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                    String strsql = "UPDATE InvoiceReceiptItem SET ItemRemarks='"+NULL + "'  where TransactionID='" + readRefNumber + "' and OrderID='" + String.valueOf(selectList.get(i)) + "'";
                    db2.execSQL(strsql);
                }
                refreshRecycleview();
                fillOrderList();
                alertDialog.dismiss();
            }
        });

    }


    private void fillOrderList() {

        try {
            invoiceItemCode = new ArrayList<>();
            invoiceItemName = new ArrayList<>();
            invoiceItemPrice = new ArrayList<>();
            invoiceItemQty = new ArrayList<>();
            invoiceItemPriceTotal = new ArrayList<>();
            invoiceRemarks = new ArrayList<>();
            invoiceItemQtyDisc = new ArrayList<>();
            invoiceVatIndicator = new ArrayList<>();
            orderItemList.clear();
            CheckItemDatabase2();
            int numberOfItem = itemListC.getCount();
            for (int itemCounter = 0; itemCounter < numberOfItem; itemCounter++) {

//                int itemQty = Integer.valueOf(invoiceItemQty.get(itemCounter));
                double itemQty = Double.parseDouble(invoiceItemQty.get(itemCounter));

                String itemSubtotal = (invoiceItemPriceTotal.get(itemCounter));
                Log.e("fillOrderList", itemSubtotal);
                String itemname = String.valueOf(invoiceItemName.get(itemCounter));
                String id = String.valueOf(invoiceItemCode.get(itemCounter));
                String remarks = String.valueOf(invoiceRemarks.get(itemCounter));
                String vatIndicator = String.valueOf(invoiceVatIndicator.get(itemCounter));

                double doubleValue = itemQty; // Example double value
                int intValue = (int) doubleValue; // Convert double to int


                orderItem p0 = new orderItem(id, itemname, itemQty, itemSubtotal, remarks, vatIndicator);
                orderItemList.addAll(Arrays.asList(new orderItem[]{p0}));
            }
        }
        catch (Exception ex){
            Log.e("TAG", "fillOrderListsss: "+ex.getMessage() );
        }

    }

    private void fillInvoiceItemList() {

        itemCode = new ArrayList<>();
        itemName = new ArrayList<>();
        recptDesc = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemQuantity = new ArrayList<>();
        itemVatIndicator=new ArrayList<>();
        priceOverride=new ArrayList<>();
        CheckItemDatabase();
        int numberOfItem = itemListC.getCount();
        InvoiceItemList.clear();


        for (int itemCounter = 0; itemCounter < numberOfItem; itemCounter++) {

            String invoiceItemCode = String.valueOf(itemCode.get(itemCounter));
            String invoiceItemName = String.valueOf(itemName.get(itemCounter));
            String invoiceItemPrice = String.valueOf(itemPrice.get(itemCounter));
            String invoiceItemQty = String.valueOf(itemQuantity.get(itemCounter));
            String invoiceVatIndicator= String.valueOf(itemVatIndicator.get(itemCounter));
            String invoicePriceOverride= String.valueOf(priceOverride.get(itemCounter));
            InvoiceItem p0 = new InvoiceItem(invoiceItemCode,invoiceItemName,invoiceItemPrice,invoiceItemQty,invoiceVatIndicator,invoicePriceOverride);
            InvoiceItemList.addAll(Arrays.asList(new InvoiceItem[]{p0}));

        }
    }
    private void CheckItemDatabase() {





                itemCode.clear();
                itemName.clear();
                recptDesc.clear();
                itemPrice.clear();
                itemQuantity.clear();
                itemVatIndicator.clear();
                priceOverride.clear();
                db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                itemListC = db.rawQuery("select * from ITEM", null);


                if (itemListC.getCount() == 0) {
                    // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                }

                while(itemListC.moveToNext()){
                    itemCode.add(itemListC.getString(0));
                    itemName.add(itemListC.getString(1));
                    recptDesc.add(itemListC.getString(2));
                    itemPrice.add(itemListC.getString(3));
                    itemQuantity.add(itemListC.getString(4));
                    itemVatIndicator.add(itemListC.getString(15));
                    priceOverride.add(itemListC.getString(16));
                    // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

                }
                itemListC.close();
                db.close();









    }

    int fastmovingItem=0;
    //android:id="@+id/ll_linearLayoutbtn8"
    LinearLayout ll_linearLayoutbtn8,ll_linearLayoutbtn9,ll_linearLayoutbtn10;
    private void CheckItemDatabaseFastMoving(String s) {

   fastmovingItem=0;



        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemVatIndicator.clear();
        priceOverride.clear();
        db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor itemKeysMap = db.rawQuery("select * from ShortCutKeysMap WHEre shortCutKeys='"+s+"'",null);
        Log.d("TAG", "CheckItemDatabaseFastMoving1: "+s);
        if (itemKeysMap.getCount()!=0){
            Log.d("TAG", "CheckItemDatabaseFastMoving2: "+s);

            while (itemKeysMap.moveToNext()){
              //  fastmovingItem++;
                Log.d("SHORTCUT KEYS",itemKeysMap.getString(1));
                itemListC = db.rawQuery("select * from ITEM where SubCatgID='"+itemKeysMap.getString(1)+"' ", null);
                while (itemListC.moveToNext()){
                    fastmovingItem++;
                    Log.d("ITEM",itemListC.getString(1));
                    itemCode.add(itemListC.getString(0));
                    itemName.add(itemListC.getString(1));
                    recptDesc.add(itemListC.getString(2));
                    itemPrice.add(itemListC.getString(3));
                    itemQuantity.add(itemListC.getString(4));
                    itemVatIndicator.add(itemListC.getString(15));
                    priceOverride.add(itemListC.getString(16));

                    reserveButtonCatg=itemListC.getString(6);
                    reserveButtonSubCatg=itemListC.getString(7);

                }


            }

            LoadReserveButton(view,s);
        }
        else{
            Log.e("TAG", "CheckItemDatabaseFastMoving: "+"error" );
            ll_linearLayoutbtn8.setVisibility(View.INVISIBLE);
            ll_linearLayoutbtn9.setVisibility(View.INVISIBLE);
            ll_linearLayoutbtn10.setVisibility(View.INVISIBLE);





        }
        Log.d("TotalItem",String.valueOf(fastmovingItem));
        for (int x=0;x<fastmovingItem;x++) {
            Log.d("FINAL ITEM", itemName.get(x));
        }






//        if (itemListC.getCount() == 0) {
//            // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
//        }
//
//        while(itemListC.moveToNext()){

            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

//        }
        itemListC.close();
        db.close();









    }




    private void CheckItemDatabaseDeptKeys() {

        fastmovingItem=0;



        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemVatIndicator.clear();
        priceOverride.clear();
        db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        Cursor keysMap = db.rawQuery("select shortCutID from ShortCutKeysMap where pageLayer='"+layer+"'",null);
        if (keysMap.getCount()!=0){
            while (keysMap.moveToNext()) {
//                itemListC = db.rawQuery("select * from ITEM where ItemID ='" + keysMap.getString(0) + "'", null);
//                if (itemListC.getCount() != 0) {
//                    while (itemListC.moveToNext()) {
//                        fastmovingItem++;
//                        Log.d("ITEM", itemListC.getString(1));
//                        itemCode.add(itemListC.getString(0));
//                        itemName.add(itemListC.getString(1));
//                        recptDesc.add(itemListC.getString(2));
//                        itemPrice.add(itemListC.getString(3));
//                        itemQuantity.add(itemListC.getString(4));
//                        itemVatIndicator.add(itemListC.getString(15));
//                        priceOverride.add(itemListC.getString(16));
//
//
//                        reserveButtonCatg = itemListC.getString(6);
//                        reserveButtonSubCatg = itemListC.getString(7);
//
//                    }
//                }
//
//                Log.d("TotalItem", String.valueOf(fastmovingItem));
//                for (int x = 0; x < fastmovingItem; x++) {
//                    Log.d("FINAL ITEM", itemName.get(x));
//                }
            }
        }




        itemListC.close();
        db.close();









    }
    private void CheckItemDatabaseAll() {

        fastmovingItem=0;



        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemVatIndicator.clear();
        priceOverride.clear();
        db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

               itemListC = db.rawQuery("SELECT * FROM ITEM ORDER BY CASE WHEN priceOverride = 'yes' THEN 0 ELSE 1 END, priceOverride", null);
        //itemListC = db.rawQuery("SELECT * FROM ITEM WHERE ItemID LIKE '%DEPARTMENT%'", null);
                while (itemListC.moveToNext()){
                    fastmovingItem++;
                    Log.d("ITEM",itemListC.getString(1));
                    itemCode.add(itemListC.getString(0));
                    itemName.add(itemListC.getString(1));
                    recptDesc.add(itemListC.getString(2));
                    itemPrice.add(itemListC.getString(3));
                    itemQuantity.add(itemListC.getString(4));
                    itemVatIndicator.add(itemListC.getString(15));
                    priceOverride.add(itemListC.getString(16));


                    reserveButtonCatg=itemListC.getString(6);
                    reserveButtonSubCatg=itemListC.getString(7);

                }

//
//            }

          //  LoadReserveButton(view,s);
//        }
//        else{
//            Log.e("TAG", "CheckItemDatabaseFastMoving: "+"error" );
//            ll_linearLayoutbtn8.setVisibility(View.INVISIBLE);
//            ll_linearLayoutbtn9.setVisibility(View.INVISIBLE);
//            ll_linearLayoutbtn10.setVisibility(View.INVISIBLE);
//
//
//
//
//
//        }
        Log.d("TotalItem",String.valueOf(fastmovingItem));
        for (int x=0;x<fastmovingItem;x++) {
            Log.d("FINAL ITEM", itemName.get(x));
        }

        itemListC.close();
        db.close();









    }

    private void CheckItemDatabase2() {



                invoiceItemCode.clear();
                invoiceItemName.clear();
                invoiceItemPrice.clear();
                invoiceItemQty.clear();
                invoiceItemPriceTotal.clear();
                invoiceRemarks.clear();
                invoiceVatIndicator.clear();

                SQLiteDatabase db = getActivity().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
                db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                itemListC = db2.rawQuery("select * from InvoiceReceiptItem", null);


                if (itemListC.getCount() == 0) {
                    // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                }

                while(itemListC.moveToNext()){
                    invoiceItemCode.add(itemListC.getString(1));
                    invoiceItemName.add(itemListC.getString(2));
                    invoiceItemPrice.add(itemListC.getInt(4));



                   Cursor itemListCvat = db.rawQuery("select * from ITEM where ItemID='"+itemListC.getString(1)+"'", null);
                  if(itemListCvat.getCount()!=0) {
                      while (itemListCvat.moveToNext()) {
                          if (itemListCvat.getString(15).trim().equals("VATable")) {
                              invoiceVatIndicator.add("v");
                          } else {
                              invoiceVatIndicator.add("nv");
                          }
                      }
                  }
                  else{
                      invoiceVatIndicator.add("v");
                  }





                    if (itemListC.getString(3)!=null){
                        invoiceItemQty.add(itemListC.getString(3));
                    }
                    else{
                        invoiceItemQty.add("100");
                    }

                    invoiceItemPriceTotal.add(itemListC.getString(5));
                    Log.e("TEST",itemListC.getString(5));
                    invoiceRemarks.add(itemListC.getString(9));

                    // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

                }
        itemListC.close();
        db2.close();









    }


    private void CheckItemDatabase3() {
        // discountList

//        private ArrayList<String> DiscountAutoIDList;
//        private ArrayList<String> DiscountIDList;
//        private ArrayList<String> DiscountNameList;
//        private ArrayList<String> DiscountAmountList;
//        private ArrayList<String> DiscountComputationList;
//        private ArrayList<String> DiscountExcludeTaxList;
//        private ArrayList<String> DiscountTypeList;



        DiscountAutoIDList.clear();
        DiscountIDList.clear();
        DiscountNameList.clear();
        DiscountAmountList.clear();
        DiscountComputationList.clear();
        DiscountExcludeTaxList.clear();
        ProRatedTaxList.clear();
        DiscountTypeList.clear();
        DiscountCategoryList.clear();
        OpenDiscountList.clear();


        SQLiteDatabase dbDiscountList = getActivity().openOrCreateDatabase(DB_NAME3, Context.MODE_PRIVATE, null);
        Cursor DiscountList = dbDiscountList.rawQuery("select * from DiscountList", null);


        if (DiscountList.getCount() == 0) {
            // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        int btnCtr=0;
        while(DiscountList.moveToNext()){
            btnCtr++;
            DiscountAutoIDList.add(DiscountList.getString(0));
            DiscountIDList.add(DiscountList.getString(1));
            DiscountNameList.add(DiscountList.getString(2));
            DiscountAmountList.add(DiscountList.getString(3));
            DiscountComputationList.add(DiscountList.getString(4));
            DiscountExcludeTaxList.add(DiscountList.getString(5));
            ProRatedTaxList.add(DiscountList.getString(14));
            DiscountTypeList.add(DiscountList.getString(6));
            DiscountCategoryList.add(DiscountList.getString(7));
            OpenDiscountList.add(DiscountList.getString(15));


            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

        }
        totalDiscountButtonList=btnCtr;
        DiscountList.close();
        dbDiscountList.close();









    }



    int totalDiscountButtonList=0;
    String discountType;
    String discountCategory="";
    String discountOpenDiscount="";
    String discountValue="0.00";
    String discountExlude;




    Double negVat=0.00;



    private void checkTotalAmount(){
        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
        String totalItem = "0";
        String totalSubtract;

        printer_settings_class printerSettings = new printer_settings_class(getContext());
        while (itemListC.moveToNext()){
            if (itemListC.getString(0)==null){
                totAmountDue.setText("0.00");
                tv_totalQty.setText("0.00");
                printerSettings.UserDisplayTotalAmount("0.00");

            }
            else{

                NumberFormat formatter = new DecimalFormat("#.##");

                //order_PriceTotal=(formatter.format(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty)));

                cashier_payment_item cashierItem = new cashier_payment_item(getContext());
                cashierItem.loadCashierPaymentDataTemp(getContext(),vatIndicatorclass.getVatIndicator());


                String FinalAmount = String.valueOf(cashierItem.getTotalDueAmount());
                DecimalFormat format = new DecimalFormat("#,###.00");
                Log.e("NEGVAT", String.valueOf(negVat));

                String formatted = format.format(Double.parseDouble(FinalAmount));

               // totalItem = formatted;
                 totAmountDue.setText(formatted);

                printerSettings.UserDisplayTotalAmount(formatted);



                Log.e("checkamount",FinalAmount);
                tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
                totalItem=FinalAmount;



            }

        }
//
//
//
//        Cursor itemListC2 = db2.rawQuery("select sum(DiscAmount),sum(VAT) from InvoiceReceiptItemFinalWDiscountTemp", null);
//        if (itemListC2.getCount()!=0){
//            if (itemListC2.moveToFirst()) {
//
//                if (itemListC2.getString(0) != null) {
//                    double FinalAmount = Double.valueOf(itemListC2.getString(0));
//                    negVat=Double.valueOf(itemListC2.getString(1));
//                    DecimalFormat format = new DecimalFormat("0.00");
//
//                    String formatted = format.format(FinalAmount);
//                    totalSubtract = formatted;
//
//                    Log.e("Total Item",totalItem);
//                    Log.e("total subtract",totalSubtract);
//                    Log.e("negvat",String.valueOf(negVat));
//
//                    totAmountDue.setText(String.valueOf(Finalformat.format(Double.valueOf(totalItem) + Double.valueOf(totalSubtract) + negVat)));
//
//                }
//            }
//        }
//
//
//
//        itemListC.close();
//        db2.close();


//        tv_totalQty.setText(String.valueOf(itemListC.getString(1)));
//                totalItem=FinalAmount;
//        DecimalFormat totalAmountToPayFormat = new DecimalFormat("0.00");


        //  totalAmountToPayFormatted = totalAmountToPayFormat.format(cashierItem.getTotalDueAmount());




//        totalAmountToPayFormatted = totalAmountToPayFormat.format(cashierItem.getTotalDueAmount());
//        //totalAmountToPayFormatted = totalAmountToPayFormat.format(cashierItem.getTotalAmountToPay()+cashierItem.getAmountDiscount()-cashierItem.getLessVat());
//        lbl_due.setText(String.valueOf(totalAmountToPayFormatted));
        //  lbl_due.setText(String.valueOf(a));




    }

    int oldQty;
    Double price;
    String time;
    public void addInvoice(){




                Date currentTime = Calendar.getInstance().getTime();


               // dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                dateFormat2 = new SimpleDateFormat("h:mm a");
               // date = dateFormat.format(currentTime.getTime());
              time=dateFormat2.format(currentTime.getTime());

              //  dateTimeDisplay.setText(date);
                readReferenceNumber();
                transaction_ID=readRefNumber;
                order_ID=itemCode.get(ItemCursor);
                order_Name=itemName.get(ItemCursor);
               // order_Qty=etEnterQuantity.getText().toString();
              //  order_Qty="1";
                order_Price=itemPrice.get(ItemCursor);


                NumberFormat formatter = new DecimalFormat("#.##");
                order_PriceTotal=(formatter.format(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty)));

//                double n = (Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
//                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//                String val = nf.format(n);
//
               // order_PriceTotal=String.valueOf(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
                //order_PriceTotal=val;


                discount_type="";
                item_remarks="";


              //  transaction_Date=date.toString();
                transaction_Time=time.toString();

               // Toast.makeText(getActivity(),String.valueOf(ItemCursor), Toast.LENGTH_SHORT).show();
                //insert into database
                db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
                if (itemListC.getCount()==0){
                    insertInvoiceReceipt();
                    refreshRecycleview();
                    fillOrderList();
                  //  showOrderInvoice();
                    checkTotalAmount();

                  //  ad.dismiss();


                }

                else{
                    while(itemListC.moveToNext()){

                        oldQty  = itemListC.getInt(3);
                        price = Double.parseDouble(itemListC.getString(4));

                    //    order_PriceTotal=(formatter.format(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty)));

                    }
                    int newQty = oldQty+Integer.valueOf(FinalItemQty);
                    String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));

                   // int newPrice = price * newQty;
                    String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
                    db2.execSQL(strsql);

                    //showOrderInvoice();
                    refreshRecycleview();
                    fillOrderList();
                    checkTotalAmount();



                }





          //  }
//        });



        itemListC.close();
        db2.close();

      //  ad.show();


    }

    public void addInvoiceOpenPrice(String priceAmount,String itemQty){




        Date currentTime = Calendar.getInstance().getTime();


        // dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("h:mm a");
        // date = dateFormat.format(currentTime.getTime());
        time=dateFormat2.format(currentTime.getTime());

        //  dateTimeDisplay.setText(date);
        readReferenceNumber();
        transaction_ID=readRefNumber;
        order_ID=itemCode.get(ItemCursor);
        order_Name=itemName.get(ItemCursor);

       // order_Price=itemPrice.get(ItemCursor);
        order_Price = priceAmount;


        NumberFormat formatter = new DecimalFormat("#.##");
        order_PriceTotal=(formatter.format(Double.parseDouble(order_Price)*(Double.parseDouble(itemQty))));

//                double n = (Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
//                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//                String val = nf.format(n);
//
        // order_PriceTotal=String.valueOf(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
        //order_PriceTotal=val;


        discount_type="";
        item_remarks="";


        //  transaction_Date=date.toString();
        transaction_Time=time.toString();

        // Toast.makeText(getActivity(),String.valueOf(ItemCursor), Toast.LENGTH_SHORT).show();
        //insert into database
        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
        if (itemListC.getCount()==0){

            insertInvoiceReceiptOpenPrice((itemQty),order_Price,order_PriceTotal);
            refreshRecycleview();
            fillOrderList();
            //  showOrderInvoice();
            checkTotalAmount();

            //  ad.dismiss();


        }

        else{
            while(itemListC.moveToNext()){

                oldQty  = itemListC.getInt(3);
                price = Double.parseDouble(itemListC.getString(4));

              //  price = Double.parseDouble(priceAmount);

                //    order_PriceTotal=(formatter.format(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty)));

            }
            int newQty = oldQty+Integer.valueOf(FinalItemQty);
            Double newInputPrice = Double.parseDouble(priceAmount);
            //String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));
            String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))+newInputPrice));

            // int newPrice = price * newQty;
            String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
            db2.execSQL(strsql);

            //showOrderInvoice();
            refreshRecycleview();
            fillOrderList();
            checkTotalAmount();



        }





        //  }
//        });



        itemListC.close();
        db2.close();
        showingAllItem();

        //  ad.show();


    }
    public void addInvoiceReserve(String orderID,String orderName,String orderPrice,String qty){




        Date currentTime = Calendar.getInstance().getTime();


        // dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("h:mm a");
        // date = dateFormat.format(currentTime.getTime());
        time=dateFormat2.format(currentTime.getTime());

        //  dateTimeDisplay.setText(date);
        readReferenceNumber();
        transaction_ID=readRefNumber;


        order_ID=orderID;
        order_Name=orderName;
        order_Price=orderPrice;


        NumberFormat formatter = new DecimalFormat("#.##");
        order_PriceTotal=(formatter.format(Double.parseDouble( orderPrice)  *  Double.parseDouble(qty)));

//                double n = (Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
//                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//                String val = nf.format(n);
//
        // order_PriceTotal=String.valueOf(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
        //order_PriceTotal=val;


        discount_type="";
        item_remarks="";


        //  transaction_Date=date.toString();
        transaction_Time=time.toString();

        // Toast.makeText(getActivity(),String.valueOf(ItemCursor), Toast.LENGTH_SHORT).show();
        //insert into database
        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
        if (itemListC.getCount()==0){
            insertInvoiceReceipt();
            refreshRecycleview();
            fillOrderList();
            //  showOrderInvoice();
            checkTotalAmount();

            //  ad.dismiss();


        }

        else{
            while(itemListC.moveToNext()){

                oldQty  = itemListC.getInt(3);
                price = Double.parseDouble(itemListC.getString(4));

                //    order_PriceTotal=(formatter.format(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty)));

            }
            int newQty = oldQty+Integer.valueOf(FinalItemQty);
            String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));

            // int newPrice = price * newQty;
            String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
            db2.execSQL(strsql);

            //showOrderInvoice();
            refreshRecycleview();
            fillOrderList();
            checkTotalAmount();


//                    while(itemListC.moveToNext()){
//                        oldQty  = itemListC.getInt(3);
//                        price = itemListC.getInt(4);
//
//                    }
//                    int newQty = oldQty+Integer.valueOf(FinalItemQty);
//                    int newPrice = price * newQty;
//                    String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
//                    db2.execSQL(strsql);
//
//                    //showOrderInvoice();
//                    refreshRecycleview();
//                    fillOrderList();
//                    checkTotalAmount();

            // ad.dismiss();

        }





        //  }
//        });



        itemListC.close();
        db2.close();

        //  ad.show();


    }
    public void subtractInvoice(){


        Date currentTime = Calendar.getInstance().getTime();


       // dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("h:mm a");
        //date = dateFormat.format(currentTime.getTime());
        time=dateFormat2.format(currentTime.getTime());
        readReferenceNumber();
        transaction_ID=readRefNumber;
        order_ID=itemCode.get(ItemCursor);
        order_Name=itemName.get(ItemCursor);
        // order_Qty=etEnterQuantity.getText().toString();
        order_Qty="1";
        order_Price=itemPrice.get(ItemCursor);
        order_PriceTotal=String.valueOf(Integer.valueOf(itemPrice.get(ItemCursor))*Integer.valueOf(order_Qty));
        NumberFormat formatter = new DecimalFormat("#.##");

        discount_type="test Discount " + "Amount";
        item_remarks="";


       // transaction_Date=date.toString();
        transaction_Time=time.toString();

        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
        if (itemListC.getCount()==0){
            //insertInvoiceReceipt();

//
//
//
//
//            refreshRecycleview();
//            fillOrderList();
//
//            checkTotalAmount();




        }

        else{

            while(itemListC.moveToNext()){
                oldQty  = itemListC.getInt(3);
                price = Double.parseDouble(itemListC.getString(4));

            }
            if (Integer.valueOf(oldQty)<=1){

                String strsql = "Delete from InvoiceReceiptItem  where TransactionID='" + readRefNumber + "' and OrderID='" + order_ID + "'";
                db2.execSQL(strsql);
                String strsql4 = "Delete from InvoiceReceiptItemFinalWDiscountTemp where TransactionID='" + readRefNumber + "' and OrderID='" + order_ID + "'";
                db2.execSQL(strsql4);
            }
            else {
                int newQty = oldQty - Integer.valueOf(order_Qty);
                String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));

                String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='" + newPrice + "'  where TransactionID='" + readRefNumber + "' and OrderID='" + order_ID + "'";
                db2.execSQL(strsql);
            }


            refreshRecycleview();
            fillOrderList();
            checkTotalAmount();



        }




        itemListC.close();
        db2.close();




    }

    private void deleteFinalReceipt() {

        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());


        for (int i=0;i<recyclerviewAdapter.selectList.size();i++){

            db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
            String strsql3 = "Delete from InvoiceReceiptItem where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'";
            db2.execSQL(strsql3);
            String strsql4 = "Delete from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'";
            db2.execSQL(strsql3);
            db2.execSQL(strsql4);
            refreshRecycleview();
            fillOrderList();
            db2.close();


        }
        selectList.clear();
        checkTotalAmount();

    }



    private void SubtractFromInvoice() { // Subtract Item
        SQLiteDatabase db22 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());




        if (recyclerviewAdapter.selectList.size()!=0) {


                    AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
                    EditText et_quantity = alertLayout.findViewById(R.id.et_quantity);
                    TextView tv_labelKeypad = alertLayout.findViewById(R.id.tv_labelKeypad);


                    String text = et_quantity.getText().toString();
                    tv_labelKeypad.setText("SUBTRACT QUANTITY");



                    Button btn_0=alertLayout.findViewById(R.id.btn_0);

                    btn_0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"0");

                        }
                    });

                    Button btn_1=alertLayout.findViewById(R.id.btn_1);
                    btn_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"1");
                        }
                    });
                    Button btn_2=alertLayout.findViewById(R.id.btn_2);
                    btn_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"2");
                        }
                    });
                    Button btn_3=alertLayout.findViewById(R.id.btn_3);
                    btn_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"3");
                        }
                    });
                    Button btn_4=alertLayout.findViewById(R.id.btn_4);
                    btn_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"4");
                        }
                    });
                    Button btn_5=alertLayout.findViewById(R.id.btn_5);
                    btn_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"5");
                        }
                    });
                    Button btn_6=alertLayout.findViewById(R.id.btn_6);
                    btn_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"6");
                        }
                    });
                    Button btn_7=alertLayout.findViewById(R.id.btn_7);
                    btn_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"7");
                        }
                    });
                    Button btn_8=alertLayout.findViewById(R.id.btn_8);
                    btn_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"8");
                        }
                    });
                    Button btn_9=alertLayout.findViewById(R.id.btn_9);
                    btn_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et_quantity.setText(et_quantity.getText().toString()+"9");
                        }
                    });
                    ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
                    ibtn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int index = et_quantity.getText().length();
                            if (index>0) {
                                et_quantity.getText().delete(index-1,index);

                            }
                            else{

                            }
                        }
                    });

                    ImageButton ibtn_confirm=alertLayout.findViewById(R.id.ibtn_confirm);


            builder.setView(alertLayout);

            et_quantity.setText("1");

            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
          //  alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(true);
            NumberFormat formatter = new DecimalFormat("#.##");

//            ibtn_confirm.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {


                            for (int i = 0; i < recyclerviewAdapter.selectList.size(); i++) {

                                itemListC = db22.rawQuery("select * from InvoiceReceiptItem where OrderID='" + recyclerviewAdapter.selectList.get(i) + "'", null);
                                if (itemListC.getCount() != 0) {
                                    while (itemListC.moveToNext()) {
                                        oldQty  = itemListC.getInt(3);
                                        price = Double.parseDouble(itemListC.getString(4));

                                    }


                                    if (Integer.parseInt(et_quantity.getText().toString())<=oldQty){
                                      //  int newQty = oldQty - Integer.parseInt(et_quantity.getText().toString());
                                       // int newPrice = price * newQty;

                                        int newQty = oldQty - Integer.parseInt(et_quantity.getText().toString());
                                        String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));

                                        String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='" + newPrice + "' where OrderID='" + recyclerviewAdapter.selectList.get(i) + "'";
                                        db22.execSQL(strsql);

                                        if (newQty==0){
                                            db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                                            String strsql3 = "Delete from InvoiceReceiptItem where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'";
                                            db2.execSQL(strsql3);
                                            String strsql4 = "Delete from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'";
                                            db2.execSQL(strsql3);
                                            db2.execSQL(strsql4);
                                            db2.close();


                                        }

                                        //showOrderInvoice();

                                    }
                                    else{
                                        Toast.makeText(getContext(), "INPUT QTY GREATER THAN ITEM QTY", Toast.LENGTH_LONG).show();
                                    }





                                }

                            }
                            refreshRecycleview();
                            fillOrderList();
                            selectList.clear();
                            checkTotalAmount();
                            alertDialog.dismiss();






//
//                        }
//                    });
//







        }
        else{
            Toast.makeText(getContext(), "PLEASE SELECT ITEM", Toast.LENGTH_SHORT).show();
        }

    }
    private void addToInvoice() { // Subtract Item
        SQLiteDatabase db22 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());

        if (recyclerviewAdapter.selectList.size()!=0) {


            AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
            EditText et_quantity = alertLayout.findViewById(R.id.et_quantity);
            TextView tv_labelKeypad = alertLayout.findViewById(R.id.tv_labelKeypad);


            String text = et_quantity.getText().toString();
            tv_labelKeypad.setText("ADD QUANTITY");



            Button btn_0=alertLayout.findViewById(R.id.btn_0);

            btn_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"0");

                }
            });

            Button btn_1=alertLayout.findViewById(R.id.btn_1);
            btn_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"1");
                }
            });
            Button btn_2=alertLayout.findViewById(R.id.btn_2);
            btn_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"2");
                }
            });
            Button btn_3=alertLayout.findViewById(R.id.btn_3);
            btn_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"3");
                }
            });
            Button btn_4=alertLayout.findViewById(R.id.btn_4);
            btn_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"4");
                }
            });
            Button btn_5=alertLayout.findViewById(R.id.btn_5);
            btn_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"5");
                }
            });
            Button btn_6=alertLayout.findViewById(R.id.btn_6);
            btn_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"6");
                }
            });
            Button btn_7=alertLayout.findViewById(R.id.btn_7);
            btn_7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"7");
                }
            });
            Button btn_8=alertLayout.findViewById(R.id.btn_8);
            btn_8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"8");
                }
            });
            Button btn_9=alertLayout.findViewById(R.id.btn_9);
            btn_9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_quantity.setText(et_quantity.getText().toString()+"9");
                }
            });
            ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
            ibtn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = et_quantity.getText().length();
                    if (index>0) {
                        et_quantity.getText().delete(index-1,index);

                    }
                    else{

                    }
                }
            });

            ImageButton ibtn_confirm=alertLayout.findViewById(R.id.ibtn_confirm);


            builder.setView(alertLayout);

             alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
         //   alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(true);
            NumberFormat formatter = new DecimalFormat("#.##");
            et_quantity.setText("1");

//            ibtn_confirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {


                    for (int i = 0; i < recyclerviewAdapter.selectList.size(); i++) {

                        itemListC = db22.rawQuery("select * from InvoiceReceiptItem where OrderID='" + recyclerviewAdapter.selectList.get(i) + "'", null);
                        if (itemListC.getCount() != 0) {
                            while (itemListC.moveToNext()) {
                                oldQty  = itemListC.getInt(3);
                                price = Double.parseDouble(itemListC.getString(4));

                            }


//                            if (Integer.parseInt(et_quantity.getText().toString())<=oldQty){
                                //  int newQty = oldQty - Integer.parseInt(et_quantity.getText().toString());
                                // int newPrice = price * newQty;

                                int newQty = oldQty + Integer.parseInt(et_quantity.getText().toString());
                                String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));

                                String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='" + newPrice + "' where OrderID='" + recyclerviewAdapter.selectList.get(i) + "'";
                                db22.execSQL(strsql);

                                if (newQty==0){
                                    db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                                    String strsql3 = "Delete from InvoiceReceiptItem where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'";
                                    db2.execSQL(strsql3);
                                    String strsql4 = "Delete from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'";
                                    db2.execSQL(strsql3);
                                    db2.execSQL(strsql4);
                                    db2.close();
                                }

                                //showOrderInvoice();

//                            }
//                            else{
//                                Toast.makeText(getContext(), "INPUT QTY GREATER THAN ITEM QTY", Toast.LENGTH_LONG).show();
//                            }





                        }

                    }
                    refreshRecycleview();
                    fillOrderList();
                    selectList.clear();
                    checkTotalAmount();
                    alertDialog.dismiss();







//                }
//            });








        }
        else{
            Toast.makeText(getContext(), "PLEASE SELECT ITEM", Toast.LENGTH_SHORT).show();
        }

    }


    public void refreshRecycleview(){

        getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            recyclerView = view.findViewById(R.id.rv_orderList);
                                            recyclerView.setHasFixedSize(true);
                                            layoutManager = new LinearLayoutManager(getActivity());
                                            layoutManager.removeAllViews();
                                            recyclerView.setLayoutManager(layoutManager);
                                            //  recyclerView.smoothScrollToPosition(rv_.getBottom());
                                            recyclerView.smoothScrollToPosition(recyclerView.getBottom());
                                            mAdapter = new RecyclerviewAdapter(orderItemList, orderItemListDisc, selectList, getActivity());
                                            // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());


                                            recyclerView.setAdapter(mAdapter);

                                        }
                                    });

        //invoice item list



    }

    public void refreshRecycleview2(Context context){
        LayoutInflater inflater = getLayoutInflater();
       final View alertLayout = inflater.inflate(R.layout.fragment_cashier_invoice, null);

        RecyclerView recyclerView3 = alertLayout.findViewById(R.id.rv_orderList);
        recyclerView3.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3=new LinearLayoutManager(context);
        layoutManager3.removeAllViews();
        recyclerView3.setLayoutManager(layoutManager3);
        RecyclerView.Adapter  mAdapter3=new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());

       // recyclerView3.setAdapter(mAdapter3);

        //invoice item list



    }




    private void insertInvoiceReceipt() {

        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());



        myDb = new DatabaseHandler(getActivity());
        boolean isInserted = myDb.insertInvoiceReceipt( transaction_ID,order_ID, order_Name,FinalItemQty,order_Price,order_PriceTotal
               ,transaction_Time,SysDate.getSystemDate(),discount_type,item_remarks );

        if (isInserted = true) {
            //  Toast.makeText(getActivity(), "INSERTED", Toast.LENGTH_SHORT).show();



        } else {
             Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }


    }
    private void insertInvoiceReceiptOpenPrice(String openFinalQty,String openOrderPrice,String openPriceTotal) {

        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());



        myDb = new DatabaseHandler(getActivity());
        boolean isInserted = myDb.insertInvoiceReceipt( transaction_ID,order_ID, order_Name,String.valueOf(openFinalQty),openOrderPrice,openPriceTotal
                ,transaction_Time,SysDate.getSystemDate(),discount_type,item_remarks );

        if (isInserted = true) {
            //  Toast.makeText(getActivity(), "INSERTED", Toast.LENGTH_SHORT).show();



        } else {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }


    }
    private void readReferenceNumber() {

        //primary key 00000001

        // int readPK;

        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptTotal ", null);
        if (itemListC.getCount() == 0) {

           int origRefNumber = 1;
            String formatted = String.format("%010d", origRefNumber);
            readRefNumber = formatted;
        } else {

            itemListC = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%010d", incrementPK);

            readRefNumber = incrementPKString;


            // }
        }
        itemListC.close();
        db2.close();


    }
    private void closeKeyboard(){

        SoftKeyboard keyboardSettings = new SoftKeyboard();
        int Hide=SoftKeyboard.getShowKboard();
        if (Hide!=1){
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }


    }
    @Override
    public void onResume() {
        super.onResume();

        KeyBoardMap();
    }





    public static class RecyclerviewAdapter extends RecyclerView.Adapter <RecyclerviewAdapter.MyViewHolder>{
        List<orderItem> orderItemList;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        public RecyclerviewAdapter(List<orderItem> orderItemList,List<orderItemDiscount> orderItemListDisc,ArrayList<String> selectList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.orderItemList = orderItemList;

            this.selectList = selectList;
            this.context = context;

        }



        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_layout,parent,false);



            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final orderItem model = new orderItem(orderItemList.get(position).getItemId(),orderItemList.get(position).getItemName(),orderItemList.get(position).getItemQty(),orderItemList.get(position).getItemSubtotal(),orderItemList.get(position).getItemRemarks(),orderItemList.get(position).getItemVatIndicator());


            holder.tv_itemName.setText(orderItemList.get(position).getItemName());
            holder.tv_itemQty.setText(String.valueOf(orderItemList.get(position).getItemQty()));
           // BigInteger bigInteger = new BigInteger(orderItemList.get(position).getItemSubtotal());
            //String bigIntegerString=String.valueOf(bigInteger);

//            String itemSubtotalString = orderItemList.get(position).getItemSubtotal().replace(",", "");
//            double itemSubtotal = Double.parseDouble(itemSubtotalString);
//
//            // Now you can use the 'itemSubtotal' variable as needed.
//            holder.tv_itemSubtotal.setText(String.format("%15.2f", itemSubtotal));

            holder.tv_itemSubtotal.setText(String.format("%15.2f",Double.parseDouble(orderItemList.get(position).getItemSubtotal())));
            holder.tv_vatIndicator.setText(orderItemList.get(position).getItemVatIndicator());
            Log.e("ITEM SUBTOTAL",String.valueOf(orderItemList.get(position).getItemSubtotal()));
            Log.e("ITEM SUBTOTAL",orderItemList.get(position).getItemId());
           // holder.tv_itemSubtotal.setText(String.format("%15.2f","999999999.99"));

//            double n = (orderItemList.get(position).getItemSubtotal());
//            NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//            String val = nf.format(n);
//            holder.tv_itemSubtotal.setText(val);


           // holder.tv_itemSubtotal.setText(String.format("%15.2f",NumberFormat.getInstance().format(orderItemList.get(position).getItemSubtotal())));
            holder.tv_remarks.setText("Remarks: "+String.valueOf(orderItemList.get(position).getItemRemarks()));


            String DB_NAME2 = "PosOutputDB.db";
            SQLiteDatabase db2 = context.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
            String id=orderItemList.get(position).getItemId() ;
            Cursor itemListC = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+id+"'", null);
//            Cursor itemListC2 = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+id+"'", null);

                if (itemListC.getCount()!=0){


                    while (itemListC.moveToNext()){


//                        if (itemListC.getString(8).trim().equals("rd")){
//                        holder.linearRegDisc.setVisibility(View.VISIBLE);
//
//
//                          //  Toast.makeText(context,itemListC.getString(12).toString() , Toast.LENGTH_SHORT).show();
//                        if (!itemListC.getString(12).equals("0")) {
//                            holder.tv_regdiscquantity.setText(itemListC.getString(10)+"("+itemListC.getString(12)+"%)");
//
//                        }
//                        else {
//                            holder.tv_regdiscquantity.setText(itemListC.getString(10));
//                        }
//
//
//                        holder.tv_regdiscsubtotal.setText(itemListC.getString(11));
//                        }
//
//
//
//
//    //
//                        if (itemListC.getString(8).equals("SCD")){
//                            holder.linearSCD.setVisibility(View.VISIBLE);
//
//                            double valuePercentdoub = Double.valueOf(itemListC.getString(11));
//                            DecimalFormat format = new DecimalFormat("0.00");
//                            String formatted = format.format(valuePercentdoub);
//
//                            holder.tv_scdquantity.setText(itemListC.getString(10)+"("+itemListC.getString(12)+"%)");
//                            holder.tv_scdsubtotal.setText(formatted);
//                        }


                        if (itemListC.getString(8)!=null){
                            holder.linearSCD.setVisibility(View.VISIBLE);



                            double valuePercentdoub = Double.valueOf(itemListC.getString(11));
                            DecimalFormat format = new DecimalFormat("0.00");
                            String formatted = format.format(valuePercentdoub);

                          //  holder.tv_scdquantity.setText(itemListC.getString(10)+"("+itemListC.getString(12)+"%)");
                            holder.tv_scdquantity.setText("open disc");
                            holder.tv_typeOfDiscount.setText(itemListC.getString(8));
                            holder.tv_scdsubtotal.setText(formatted);



                        }







                    }

            }
    //


            if (model.getItemRemarks().trim().length() == 0){
                holder.tv_remarks.setVisibility(View.GONE);
            }
            if (orderItemList.get(position).getItemQty()<0){
                Cursor checkReference = db2.rawQuery("select * from ReturnExchangeTemp", null);
                holder.ll_referenceLinear.setVisibility(View.VISIBLE);
                if (checkReference.getCount()!=0){
                    if (checkReference.moveToFirst()){
                        holder.tv_SINo.setText(checkReference.getString(1));
                    }
                }
                db2.close();
            }



            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


    //               if (!model.isSelected()){
    //                   holder.btn_cashier_delete.setVisibility(View.VISIBLE);
    //               }
    //               else{
    //                   holder.btn_cashier_delete.setVisibility(View.GONE);
    //
    //               }


                    model.setSelected(!model.isSelected());

                    if (!model.isSelected()){
                        holder.parentLayout.setBackgroundColor(model.isSelected() ? Color.WHITE : context.getResources().getColor(R.color.myColorButtonPressed));
                        holder.iv_check.setVisibility(View.VISIBLE);
                        selectList.add(id);
                    }
                    else{
                        holder.parentLayout.setBackgroundColor(!model.isSelected() ? context.getResources().getColor(R.color.myColorButtonPressed ): Color.WHITE);
                        holder.iv_check.setVisibility(View.INVISIBLE);
                        selectList.remove(id);
                    }



                }
            });

    //
         //   Glide.with(this.context).load(presidentList.get(position).getImageUrl()).into(holder.iv_prespic);
        }

        @Override
        public int getItemCount() {
            return orderItemList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_itemName;
            TextView tv_itemQty;
            TextView tv_remarks,tv_regdiscquantity,tv_regdiscsubtotal,tv_scdquantity,tv_scdsubtotal,tv_typeOfDiscount,tv_SINo;
            ImageView iv_check;
            TextView tv_vatIndicator;
            LinearLayout linearRegDisc,linearSCD,ll_referenceLinear;


            TextView tv_itemSubtotal;
            CardView parentLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_itemName = itemView.findViewById(R.id.tv_itemName);
                tv_itemQty = itemView.findViewById(R.id.tv_quantity);
                tv_itemSubtotal = itemView.findViewById(R.id.tv_subtotal);
                tv_remarks = itemView.findViewById(R.id.tv_remarks);
                iv_check = itemView.findViewById(R.id.iv_check);
                parentLayout = itemView.findViewById(R.id.linear_orderlist_layout);
                linearRegDisc = itemView.findViewById(R.id.linearRegDisc);
                tv_regdiscquantity=itemView.findViewById(R.id.tv_regdiscquantity);
                tv_regdiscsubtotal=itemView.findViewById(R.id.tv_regdiscsubtotal);
                tv_typeOfDiscount=itemView.findViewById(R.id.tv_typeOfDiscount);

                tv_scdquantity=itemView.findViewById(R.id.tv_scdquantity);
                tv_scdsubtotal=itemView.findViewById(R.id.tv_scdsubtotal);
                linearSCD = itemView.findViewById(R.id.linearSCD);
                tv_vatIndicator=itemView.findViewById(R.id.tv_VatIndicator);
                tv_SINo=itemView.findViewById(R.id.tv_SINo);
                ll_referenceLinear=itemView.findViewById(R.id.ll_referenceLinear);




            }
        }
    }


    ImageButton btn_cashier_z_report,btn_cashier_cashinout,btn_cashier_x_report,btn_cashier_logout,btn_cashier_refund,btn_cashier_return_exchange;
    ImageButton btn_cashier_check_sales;
    TextView tv_totalRefundAmount;
    Spinner sp_reason;
    int activateCode=1;

    RecyclerView rv_refund_list;

   // TextView tv_totalRefundAmount;

    List<cashier_shift_model> ArrayDataList = new ArrayList<>();
    List<cashier_invoice_masterlist_model> ArrayDataList2 = new ArrayList<>();
    private void FillDataRV(String TransactionID,String InvoiceNumber){

        SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        ArrayDataList.clear();
        finalItemList.clear();
        finalItemListName.clear();
        finalItemListQty.clear();
        finalItemListPrice.clear();
        amountList.clear();

        Cursor itemListC = db.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='"+TransactionID+"'", null);

        int numberOfItem = itemListC.getCount();
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){



            //  int ItemNo=itemCounter+1;

            if (itemListC.getCount()!=0){
                while (itemListC.moveToNext()){

                    String OrderID =itemListC.getString(1);
                    String OrderName=itemListC.getString(2);
                    String OrderQty=itemListC.getString(3);
                    // String OrderPrice=itemListC.getString(4);
                    String OrderPriceTotal=itemListC.getString(5);
                    String TransactionTime=itemListC.getString(6);
                    String TransactionDate=itemListC.getString(7);
                    String TotalDiscount="0.00";
                    String TotalDiscVat="0.00";

                    Cursor itemListCDiscount = db.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where TransactionID='"+TransactionID+"' and OrderID='"+OrderID+"'", null);
                    if (itemListCDiscount.getCount()!=0){
                        itemListCDiscount.moveToFirst();
                        TotalDiscount=itemListCDiscount.getString(11);
                        TotalDiscVat=itemListCDiscount.getString(13);


                    }
                    String OrderPrice = String.valueOf(Double.parseDouble(itemListC.getString(4)) + Double.parseDouble(TotalDiscount) + Double.parseDouble(TotalDiscVat));






// public cashier_shift_model(String transactionID, String transactionInvoice, String transactionOrderID, String transactionItemName,
//                            String transactionItemQty, String transactionTotalPrice,String transactionTotalDisc,String transactionTotalAmount,
//                            String transactionTime,String transactionDate){


                    cashier_shift_model p0=new cashier_shift_model(TransactionID,OrderID,OrderName,OrderQty,OrderPrice,TotalDiscount,OrderPriceTotal,TransactionTime,TransactionDate);
                    //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

                    ArrayDataList.addAll(Arrays.asList(new cashier_shift_model[]{p0}));

                }
            }



        }
        //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";
        db.close();

    }
    private void RefreshRV(){


        rv_refund_list.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.removeAllViews();
        rv_refund_list.setLayoutManager(layoutManager);
        mAdapter=new RecyclerviewAdapter2(ArrayDataList,getActivity());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_refund_list.setAdapter(mAdapter);
        rv_refund_list.smoothScrollToPosition(rv_refund_list.getBottom());

    }

    ArrayList<String> amountList = new ArrayList<>();
    ArrayList<String> finalItemList = new ArrayList<>();
    ArrayList<String> finalItemListName = new ArrayList<>();
    ArrayList<String> finalItemListQty = new ArrayList<>();
    ArrayList<String> finalItemListPrice = new ArrayList<>();


    String FinalitemName;






    RecyclerView rv_Masterlist;
    int finalMasterListIndicator;
    int dialogSearch=1; //1 for Main search //2 for search item dialog
    public void showDialogMasterlist(int ind){

        finalMasterListIndicator=ind;
        if (finalMasterListIndicator==1 || finalMasterListIndicator==0){
            xCursor=7;
        }
        Rect displayRectangle = new Rect();
        Window window = this.getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);




        AlertDialog.Builder builder  = new AlertDialog.Builder(this.getActivity(),R.style.DialogSlide);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_master_list, null);





        rv_Masterlist = alertLayout.findViewById(R.id.rv_Masterlist);
       EditText et_searchMaster = alertLayout.findViewById(R.id.et_searchMaster);

      //  et_searchMaster.clearFocus();
       // closeKeyboard();
//        et_searchMaster.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                showingItemSearch(et_searchMaster.getText().toString());
//                RefreshMasterlist();
//            }
//        });

        if(ind==1){
            showingItemSearch(" ");
           // et_searchMaster.setVisibility(View.GONE);
        }

        else{

            //search item in Masterlist

            et_searchMaster.requestFocus();
            InitT9MapCode();
            closeKeyboard();

            //use product id for searching

            et_searchMaster.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && ( keyCode == KeyEvent.KEYCODE_ENTER)){
                        Toast.makeText(getActivity(), et_searchMaster.getText().toString(), Toast.LENGTH_SHORT).show();

                        showingItemSearch(et_searchMaster.getText().toString());





                        //  closeKeyboard();
                        et_searchMaster.requestFocus();;
                        // return true;

                    }

                    et_searchMaster.requestFocus();;
                    return false;
                }

            });

            et_searchMaster.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_DONE){
                        showingItemSearch(et_searchMaster.getText().toString());
                        // closeKeyboard();
                        // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        //  return  true;
                    }
                    return false;
                }
            });






        }










        builder.setView(alertLayout);
         alertDialog = builder.create();


        View decorView = alertDialog.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        //alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();





    }
    public static HashMap<Integer,Integer> mapCode2 = new HashMap<>();
    public static HashMap<Integer,String> mapCode = new HashMap<>();
    private final static int EXECUTE_MSG = 750;


    // KeyCodeManager2 kManager2 = new KeyCodeManager2();
    //    public class InputMethod{
    public int nKickKeyIndex=0;
    private int nKickCnt=0;
    private Timer timer = null;
    private TimerTask timeOutTask = null;
    private static final int KICK_TIME_PERIOD_MILLS = 450;

    public void setNewKeyEvent(int nKeyIndex){
        StopTimeOutTask();
        if(nKeyIndex ==0){
            nKickKeyIndex = nKeyIndex;
            nKickCnt =1;
        }
        else{
            if (nKeyIndex == nKickKeyIndex){
                nKickCnt = nKickCnt+1;
            }
            else{
                DoInput(nKickKeyIndex,nKickCnt);
                nKickKeyIndex = nKeyIndex;
                nKickCnt=1;
            }
        }
        InitTimeOutTask();
    }

    private void StopTimeOutTask(){
        if (timeOutTask!=null){
            timeOutTask.cancel();
            timeOutTask = null;
        }
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
    }
    private void InitTimeOutTask(){
        {
            try{
                timer = new Timer();
                timeOutTask = new TimerTask() {
                    @Override
                    public void run() {
                        DoInput(nKickKeyIndex,nKickCnt);
                        nKickKeyIndex =0;
                        nKickCnt = 0;
                    }
                };
                timer.schedule(timeOutTask,KICK_TIME_PERIOD_MILLS);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }



    public void InitT9MapCode() {
        mapCode.clear();
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD0, " 0");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD1, "1PQRS");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD2, "2TUV");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD3, "3WXYZ");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD4, "4GHI");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD5, "5JKL");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD6, "6MNO");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD7, "7@[\\]");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD8, "8ABC");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD9, "9DEF");
        mapCode.put(KeyCodeManager.KEY_INDEX_NUMPAD9, "9DEF");
        mapCode2.put(KeyCodeManager.KEY_INDEX_TOTAL2, KeyEvent.KEYCODE_ENTER);
        mapCode2.put(KeyCodeManager.KEY_INDEX_PAGEDN, KeyEvent.KEYCODE_PAGE_DOWN);
        mapCode2.put(KeyCodeManager.KEY_INDEX_PAGEUP, KeyEvent.KEYCODE_PAGE_UP);
        mapCode2.put(KeyCodeManager.KEY_INDEX_EC, KeyEvent.KEYCODE_DEL);
    }
    public void DoInput(int nKeyIndex,int nCnt){
        int nLayer = nCnt;
        if (nLayer>0){
            nLayer= nLayer-1;
        }

        if (mapCode.containsKey(nKeyIndex)) {
            try {
                String codeT9 = mapCode.get(nKeyIndex);
                if (codeT9 != null) {
                    List<String> lstSel = new ArrayList<>();
                    ///////////////
                    char[] charT9 = codeT9.toCharArray();
                    for (char chInput : charT9) {
                        String strInput = String.valueOf(chInput);
                        lstSel.add(strInput);
                    }
                    /////////////
                    String selIn = "";
                    if (nLayer < lstSel.size()) {
                        selIn = lstSel.get(nLayer);
                    } else {
                        selIn = lstSel.get(lstSel.size() - 1);
                    }
                    ////
                    int nKeyCode = KeyEvent.keyCodeFromString(selIn);
                    //Log.i(TAG, String.format("Do T9Input:%d %d %s=%d", nKeyIndex, nCnt, selIn, nKeyCode));
                    //PostMessage(T9_INPUT_EXECUTE_MSG, selIn);
//                Log.i("NKeyCOde",String.valueOf(nKeyCode));
//                Log.i("ACTION NEXT",String.valueOf(EditorInfo.IME_ACTION_NEXT));
//                Log.i("TAG",String.valueOf(KeyCodeManager.KEY_INDEX_EC));

                    //    if (nKeyCode == EditorInfo.IME_ACTION_NEXT)
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }

                 //   Toast.makeText(getContext(), "INPUTTING STRING", Toast.LENGTH_SHORT).show();
                    PostMessage(EXECUTE_MSG, selIn);


                }
            }
            catch (Exception ex){

            }
        }
        if (mapCode2.containsKey(nKeyIndex)) {
            Log.i("MAPCODE2", "DoInput: "+nKeyIndex);
            int codeT9 = mapCode2.get(nKeyIndex);
            Log.i("MAPCODE2", "DoInput: "+codeT9);
            Log.i("ACTION_SEARCH",  String.valueOf(EditorInfo.IME_ACTION_SEARCH));
            Log.i("ACTION_NEXT",  String.valueOf(EditorInfo.IME_ACTION_NEXT));
            Log.i("ACTION_DONE",  String.valueOf(EditorInfo.IME_ACTION_DONE));
            Log.i("KeyEventActionDown",  String.valueOf(KeyEvent.ACTION_DOWN));
            Log.i("Enter",  String.valueOf(KeyEvent.KEYCODE_ENTER));

            if (Looper.myLooper()==null){
                Looper.prepare();
            }



            if (codeT9 == KeyEvent.KEYCODE_PAGE_DOWN){
                moveEdittextDOWN(EXECUTE_MSG,"down");
            }
            if (codeT9 == KeyEvent.KEYCODE_PAGE_UP){
                moveEdittextUP(EXECUTE_MSG,"up");
            }
            else if (codeT9 == KeyEvent.KEYCODE_ENTER){
                if (InvoiceCursor==5){
                    if (btn_saveCustInfo.hasFocus()) {
                        CustInfo.setCustName(et_custName.getText().toString());
                        CustInfo.setCustIDNo((et_custIDNo.getText().toString()));
                        CustInfo.setCustTIN((et_custTIN.getText().toString()));
                        discountComputation();
                        Toast.makeText(getContext(), discountType + " Applied", Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                        InvoiceCursor = 0;
                    }
                }
                else if (InvoiceCursor==6){
                    if (et_reserveName.getText().length()==0){
                        Toast.makeText(getContext(), "Please input Reserve Name", Toast.LENGTH_SHORT).show();
                    }
                    else if (et_reserveDesc.getText().length()==0){
                        Toast.makeText(getContext(), "Please input Reserve Name", Toast.LENGTH_SHORT).show();
                    }

                    else if (et_reserveAmount.getText().length()==0){
                        Toast.makeText(getContext(), "Please input Reserve Name", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        insertNewReserve(
                                et_reserveID.getText().toString(),
                                et_reserveName.getText().toString(),
                                et_reserveDesc.getText().toString(),
                                et_reserveAmount.getText().toString());
                        InvoiceCursor=0;
                    }

                }
                //noteinv
            }
            else if (codeT9 == KeyEvent.KEYCODE_DEL){

//                int length = et_command.getText().length();
//                if (length > 0) {
//                    et_command.getText().delete(length - 1, length);
//
//                }


                if (InvoiceCursor==6){


                    if (et_reserveName.hasFocus()) {

                        int length = et_reserveName.getText().length();
                        if (length > 0) {
                            et_reserveName.getText().delete(length - 1, length);

                        }

                    } else if (et_reserveDesc.hasFocus()) {
                        int length = et_reserveDesc.getText().length();
                        if (length > 0) {
                            et_reserveDesc.getText().delete(length - 1, length);

                        }

                    } else if (et_reserveAmount.hasFocus()) {
                        int length = et_reserveAmount.getText().length();
                        if (length > 0) {
                            et_reserveAmount.getText().delete(length - 1, length);

                        }

                    }

                }

            }







        }





        Log.i("TAG", "NnKeyIndex: "+nKeyIndex);

//        }

    }


    public void moveEdittextUP(int nMsg,String message){


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (InvoiceCursor==5) {



                    if (et_custName.hasFocus()) {
                        Log.e("Enter key", "proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_custIDNo.requestFocus();
                    } else if (et_custIDNo.hasFocus()) {
                        //Log.e("Enter key","proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_custName.requestFocus();
                    } else if (et_custTIN.hasFocus()) {
                        Log.e("Enter key", "et_custTin");
//                        btn_saveCustInfo.setFocusableInTouchMode(true);
//                        btn_saveCustInfo.requestFocus();
                        et_custIDNo.requestFocus();

                    }
                    else if(btn_saveCustInfo.hasFocus()){

//                        addedCustomerName = et_custName.getText().toString();
//                        addedCustomerAddress = et_custIDNo.getText().toString();
//                        addedCustomerTin = et_custTIN.getText().toString();
//                        alertDialog.dismiss();
//                        mapCode2Activate=0;

                        et_custTIN.requestFocus();

                    }
                    else{
                        alertDialog.dismiss();
                        // mapCode2Activate=0;
                        InvoiceCursor=0;
                    }
                }
                if (InvoiceCursor==6){


                    //            EditText et_reserveName;
//            EditText et_reserveDesc;
//            EditText et_reserveAmount;


                    if (et_reserveName.hasFocus()) {
                        Log.e("Enter key", "proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                       // et_reserveDesc.requestFocus();
                    } else if (et_reserveDesc.hasFocus()) {
                        //Log.e("Enter key","proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_reserveName.requestFocus();
                    } else if (et_reserveAmount.hasFocus()) {
                        Log.e("Enter key", "et_custTin");
//                        btn_saveCustInfo.setFocusableInTouchMode(true);
//                        btn_saveCustInfo.requestFocus();
                        et_reserveDesc.requestFocus();

                    }
                    else if(btn_addNewReserve.hasFocus()){

//                        addedCustomerName = et_custName.getText().toString();
//                        addedCustomerAddress = et_custIDNo.getText().toString();
//                        addedCustomerTin = et_custTIN.getText().toString();
//                        alertDialog.dismiss();
//                        mapCode2Activate=0;

                        et_reserveAmount.requestFocus();

                    }
                    else{
                        alertDialog.dismiss();
                        // mapCode2Activate=0;
                        InvoiceCursor=0;
                    }
                }


            }
        });



        // Log.i("TAG", String.valueOf(nMsg) +" "+message);
        //  Log.i("POSTMESSAGEs",strMessage);


    }
    public void moveEdittextDOWN(int nMsg,String message){


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (InvoiceCursor==5) {



                    if (et_custName.hasFocus()) {
                        Log.e("Enter key", "proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_custIDNo.requestFocus();
                    } else if (et_custIDNo.hasFocus()) {
                        //Log.e("Enter key","proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_custTIN.requestFocus();
                    } else if (et_custTIN.hasFocus()) {
//                        Log.e("Enter key", "et_custTin");
//                        btn_saveCustInfo.setFocusableInTouchMode(true);
//                        btn_saveCustInfo.requestFocus();

                        btn_saveCustInfo.setFocusableInTouchMode(true);
                        btn_saveCustInfo.requestFocus();
                        et_custTIN.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    }
                    else if(btn_saveCustInfo.hasFocus()){

//                        addedCustomerName = et_custName.getText().toString();
//                        addedCustomerAddress = et_custIDNo.getText().toString();
//                        addedCustomerTin = et_custTIN.getText().toString();
//                        alertDialog.dismiss();
//                        mapCode2Activate=0;



                    }
                    else{
                        alertDialog.dismiss();
                        // mapCode2Activate=0;
                        InvoiceCursor=0;
                    }
                }
                if (InvoiceCursor==6){


                    //            EditText et_reserveName;
//            EditText et_reserveDesc;
//            EditText et_reserveAmount;


                    if (et_reserveName.hasFocus()) {
                        Log.e("Enter key", "proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_reserveDesc.requestFocus();
                    } else if (et_reserveDesc.hasFocus()) {
                        //Log.e("Enter key","proceed");
                        //et_custName.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        et_reserveAmount.requestFocus();
                    } else if (et_reserveAmount.hasFocus()) {
                        Log.e("Enter key", "et_custTin");
//                        btn_saveCustInfo.setFocusableInTouchMode(true);
//                        btn_saveCustInfo.requestFocus();
                        et_reserveAmount.onEditorAction(EditorInfo.IME_ACTION_NEXT);
                        btn_addNewReserve.requestFocus();

                    }
                    else if(btn_addNewReserve.hasFocus()){

//                        addedCustomerName = et_custName.getText().toString();
//                        addedCustomerAddress = et_custIDNo.getText().toString();
//                        addedCustomerTin = et_custTIN.getText().toString();
//                        alertDialog.dismiss();
//                        mapCode2Activate=0;

                        et_reserveAmount.requestFocus();

                    }
                    else{
                        alertDialog.dismiss();
                        // mapCode2Activate=0;
                        InvoiceCursor=0;
                    }
                }





            }
        });



        // Log.i("TAG", String.valueOf(nMsg) +" "+message);
        //  Log.i("POSTMESSAGEs",strMessage);


    }


    private void DialogSearchItemAdd(String Itemcode,String ItemName,String ItemPrice){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogSlide);
        // AlertDialog alert = builder.create();
        // List<Integer>al=new ArrayList<>();


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
        EditText et_quantity = alertLayout.findViewById(R.id.et_quantity);
        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

         alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View decorView = alertDialog.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemQty=1;
                String quantityText=et_quantity.getText().toString();
                if(!quantityText.matches("")){
                    FinalItemQty = et_quantity.getText().toString().trim();
                    Log.e("ItemQty","NOT EMPTY"+et_quantity.getText().toString());
                }else{
                    FinalItemQty = String.valueOf(itemQty);
                    Log.e("ItemQty","EMPTY");
                }
             //   FinalItemQty = et_quantity.getText().toString().trim();
                //ItemCursor = child.getId();
                addInvoiceSearch(Itemcode,ItemName,ItemPrice);
                FinalItemQty = "";
                alertDialog.dismiss();

            }
        });


    }

    public void addInvoiceSearch(String Itemcode,String ItemName,String ItemPrice){




        Date currentTime = Calendar.getInstance().getTime();


        // dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("h:mm a");
        // date = dateFormat.format(currentTime.getTime());
        time=dateFormat2.format(currentTime.getTime());

        //  dateTimeDisplay.setText(date);
        readReferenceNumber();
        transaction_ID=readRefNumber;
        order_ID=Itemcode;
        order_Name=ItemName;
        // order_Qty=etEnterQuantity.getText().toString();
        //  order_Qty="1";
        order_Price=ItemPrice;


        NumberFormat formatter = new DecimalFormat("#.##");
        order_PriceTotal=(formatter.format(Double.parseDouble(ItemPrice)*Double.parseDouble(FinalItemQty)));

//                double n = (Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
//                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
//                String val = nf.format(n);
//
        // order_PriceTotal=String.valueOf(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty));
        //order_PriceTotal=val;


        discount_type="";
        item_remarks="";


        //  transaction_Date=date.toString();
        transaction_Time=time.toString();

        // Toast.makeText(getActivity(),String.valueOf(ItemCursor), Toast.LENGTH_SHORT).show();
        //insert into database
        db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
        if (itemListC.getCount()==0){
            insertInvoiceReceipt();
            refreshRecycleview();
            fillOrderList();
            //  showOrderInvoice();
            checkTotalAmount();

            //  ad.dismiss();


        }

        else{
            while(itemListC.moveToNext()){

                oldQty  = itemListC.getInt(3);
                price = Double.parseDouble(itemListC.getString(4));

                //    order_PriceTotal=(formatter.format(Double.parseDouble(itemPrice.get(ItemCursor))*Double.parseDouble(FinalItemQty)));

            }
            int newQty = oldQty+Integer.valueOf(FinalItemQty);
            String newPrice=(formatter.format(Double.parseDouble(String.valueOf(price))*Double.parseDouble(String.valueOf(newQty))));

            // int newPrice = price * newQty;
            String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
            db2.execSQL(strsql);

            //showOrderInvoice();
            refreshRecycleview();
            fillOrderList();
            checkTotalAmount();


//                    while(itemListC.moveToNext()){
//                        oldQty  = itemListC.getInt(3);
//                        price = itemListC.getInt(4);
//
//                    }
//                    int newQty = oldQty+Integer.valueOf(FinalItemQty);
//                    int newPrice = price * newQty;
//                    String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
//                    db2.execSQL(strsql);
//
//                    //showOrderInvoice();
//                    refreshRecycleview();
//                    fillOrderList();
//                    checkTotalAmount();

            // ad.dismiss();

        }





        //  }
//        });



        itemListC.close();
        db2.close();

        //  ad.show();


    }


    private void showingItemSearch(String s){
        int btnCounter=0;

        itemCode = new ArrayList<>();
        itemName = new ArrayList<>();
        recptDesc = new ArrayList<>();
        itemPrice = new ArrayList<>();
        itemQuantity = new ArrayList<>();
        itemVatIndicator=new ArrayList<>();
        priceOverride=new ArrayList<>();
        ArrayDataList2.clear();

        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemVatIndicator.clear();
        priceOverride.clear();


        CheckItemDatabaseMasterlist(s);
        PLUItem(itemCode.size());



       // SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        ArrayDataList.clear();
//        finalItemList.clear();
//        finalItemListName.clear();
//        finalItemListQty.clear();
//        finalItemListPrice.clear();
//        amountList.clear();

     //   Cursor itemListC = db.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='"+TransactionID+"'", null);

      //  int numberOfItem = itemListC.getCount();
       // for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){



            //  int ItemNo=itemCounter+1;
//
//            if (itemListC.getCount()!=0){
//                while (itemListC.moveToNext()){
//
//                    String OrderID =itemListC.getString(1);
//                    String OrderName=itemListC.getString(2);
//                    String OrderQty=itemListC.getString(3);
//                    // String OrderPrice=itemListC.getString(4);
//                    String OrderPriceTotal=itemListC.getString(5);
//                    String TransactionTime=itemListC.getString(6);
//                    String TransactionDate=itemListC.getString(7);
//                    String TotalDiscount="0.00";
//                    String TotalDiscVat="0.00";
//
//                    Cursor itemListCDiscount = db.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where TransactionID='"+TransactionID+"' and OrderID='"+OrderID+"'", null);
//                    if (itemListCDiscount.getCount()!=0){
//                        itemListCDiscount.moveToFirst();
//                        TotalDiscount=itemListCDiscount.getString(11);
//                        TotalDiscVat=itemListCDiscount.getString(13);
//
//
//                    }
//                    String OrderPrice = String.valueOf(Double.parseDouble(itemListC.getString(4)) + Double.parseDouble(TotalDiscount) + Double.parseDouble(TotalDiscVat));
//
//
//



// public cashier_shift_model(String transactionID, String transactionInvoice, String transactionOrderID, String transactionItemName,
//                            String transactionItemQty, String transactionTotalPrice,String transactionTotalDisc,String transactionTotalAmount,
//                            String transactionTime,String transactionDate){


        for(int x=0;x< itemCode.size();x++){
            int pluNumber = x+1;
            cashier_invoice_masterlist_model p0=new cashier_invoice_masterlist_model(
                    "PLU"+String.valueOf(pluNumber),
                    itemCode.get(x),
                    itemName.get(x),
                    recptDesc.get(x),
                    itemPrice.get(x),
                    itemQuantity.get(x),
                    itemVatIndicator.get(x),
                    priceOverride.get(x)
            );
            //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

            ArrayDataList2.addAll(Arrays.asList(new cashier_invoice_masterlist_model[]{p0}));
        }



//
//                }
//            }
//
//
//
//        }
//
//        db.close();


        RefreshMasterlist();




    }

    private void CheckItemDatabaseMasterlist(String item) {





        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemVatIndicator.clear();
        priceOverride.clear();
        db = getActivity().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        itemListC = db.rawQuery("select * from ITEM where ItemName like '%"+item+"%'", null);


        if (itemListC.getCount() == 0) {
            // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }

        while(itemListC.moveToNext()){
            itemCode.add(itemListC.getString(0));
            itemName.add(itemListC.getString(1));
            recptDesc.add(itemListC.getString(2));
            itemPrice.add(itemListC.getString(3));
            itemQuantity.add(itemListC.getString(4));
            itemVatIndicator.add(itemListC.getString(15));
            priceOverride.add(itemListC.getString(16));
            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

        }
        itemListC.close();
        db.close();









    }

    private void RefreshMasterlist(){


        rv_Masterlist.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.removeAllViews();
        rv_Masterlist.setLayoutManager(layoutManager);
        mAdapter=new RecyclerviewAdapter3(ArrayDataList2,getActivity());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_Masterlist.setAdapter(mAdapter);
     //   rv_Masterlist.smoothScrollToPosition(rv_Masterlist.getBottom());

        return;



    }



    public class RecyclerviewAdapter2 extends RecyclerView.Adapter <RecyclerviewAdapter2.MyViewHolder>{
        List<cashier_shift_model> ArrayDataList;
        Context context;
        ArrayList<String> selectList = new ArrayList<>();
        private RecyclerviewAdapter2.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter2(List<cashier_shift_model> ArrayDataList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.ArrayDataList = ArrayDataList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public RecyclerviewAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_alertdialog_invoice_refund_data_layout,parent,false);



            RecyclerviewAdapter2.MyViewHolder holder = new RecyclerviewAdapter2.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull RecyclerviewAdapter2.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;


            holder.cb_itemName.setText(ArrayDataList.get(position).getTransactionItemName());

            // holder.tv_discAmount.setText(ArrayDataList.get(position).getTransactionTotalDisc());
            holder.tv_totalAmount.setText(ArrayDataList.get(position).getTransactionTotalPrice());


            holder.cb_itemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //showPassword(isChecked);


                    if (holder.cb_itemName.isChecked()){
                        // Log.e("TEST CB",ArrayDataList.get(position).getTransactionOrderID());
                        selectList.add(ArrayDataList.get(position).getTransactionOrderID());
                        amountList.add(ArrayDataList.get(position).getTransactionTotalPrice());

                        finalItemList.add(ArrayDataList.get(position).getTransactionOrderID());
                        finalItemListName.add(ArrayDataList.get(position).getTransactionItemName());
                        finalItemListQty.add(ArrayDataList.get(position).getTransactionItemQty());
                        finalItemListPrice.add(ArrayDataList.get(position).getTransactionTotalPrice());

                        Log.e("ARRAY",selectList.toString());
                        Log.e("ARRAY",amountList.toString());
                    }
                    else{
                        //   Log.e("TEST CB",ArrayDataList.get(position).getTransactionOrderID());
                        selectList.remove(ArrayDataList.get(position).getTransactionOrderID());
                        amountList.remove(ArrayDataList.get(position).getTransactionTotalPrice());
                        finalItemList.remove(ArrayDataList.get(position).getTransactionOrderID());
                        finalItemListName.remove(ArrayDataList.get(position).getTransactionItemName());
                        finalItemListQty.remove(ArrayDataList.get(position).getTransactionItemQty());
                        finalItemListPrice.remove(ArrayDataList.get(position).getTransactionTotalPrice());

                        Log.e("ARRAY",selectList.toString());
                        Log.e("ARRAY",amountList.toString());
                    }
                    double totalRef=0.00;
                    for (int x=0;x<amountList.size();x++){
                        totalRef=totalRef+Double.parseDouble(amountList.get(x));
                    }

                    tv_totalRefundAmount.setText(String.valueOf(totalRef));
                }
            });


        }

        @Override
        public int getItemCount() {
            return ArrayDataList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
//            TextView tv_id;
//            TextView tv_paymentName;
//            TextView tv_allowReference;
//            TextView tv_allowDetails;
//            TextView tv_paymentType;
//            TextView tv_changeType;
//            LinearLayout ll_otherPaymentSelect;

            CheckBox cb_itemName;
            //   TextView tv_discAmount;
            TextView tv_totalAmount;


//            private int opID;
//            private String opName;
//            private String opReference;
//            private String opDetails;
//            private int opType;
//            private int opChangetype;





            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//                tv_id = itemView.findViewById(R.id.tv_id);
//                tv_paymentName = itemView.findViewById(R.id.tv_name);
//                tv_allowReference = itemView.findViewById(R.id.tv_reference);
//                tv_allowDetails=itemView.findViewById(R.id.tv_details);
//                tv_paymentType=itemView.findViewById(R.id.tv_paymentType);
//                tv_changeType=itemView.findViewById(R.id.tv_changeType);
//                ll_otherPaymentSelect=itemView.findViewById(R.id.ll_otherPaymentSelect);
                cb_itemName = itemView.findViewById(R.id.cb_itemName);
                //    tv_discAmount = itemView.findViewById(R.id.tv_discAmount);
                tv_totalAmount = itemView.findViewById(R.id.tv_totalAmount);




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



//                ll_otherPaymentSelect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        FinalID=opID;
//                        FinalName=opName;
//                        FinalAllowRef=opReference;
//                        FinalAllowDetails=opDetails;
//                        FinalPaymentType=opType;
//                        FinalChangeType=opChangetype;
//
//                        Log.d("TAG",String.valueOf(FinalID));
//                        Log.d("TAG",String.valueOf(FinalName));
//                        Log.d("TAG",String.valueOf(FinalAllowRef));
//                        Log.d("TAG",String.valueOf(FinalAllowDetails));
//                        Log.d("TAG",String.valueOf(FinalPaymentType));
//                        Log.d("TAG",String.valueOf(FinalChangeType));
//
//
//
//
//
//                    }
//                });



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



    // for showing item masterlist recyclerview
    public class RecyclerviewAdapter3 extends RecyclerView.Adapter <RecyclerviewAdapter3.MyViewHolder>{
        List<cashier_invoice_masterlist_model> ArrayDataList;
        Context context;
        ArrayList<String> selectList = new ArrayList<>();
        private RecyclerviewAdapter3.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter3(List<cashier_invoice_masterlist_model> ArrayDataList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.ArrayDataList = ArrayDataList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public RecyclerviewAdapter3.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_masterlist_layout,parent,false);



            RecyclerviewAdapter3.MyViewHolder holder = new RecyclerviewAdapter3.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull RecyclerviewAdapter3.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

            String pluNum = ArrayDataList.get(position).getPluNumber();
            holder.tv_pluNumber.setText("PLU +" + pluNum.replace("PLU","") + "+ Ent");
            holder.tv_ItemName.setText(ArrayDataList.get(position).getItemName());
            holder.tv_ItemPrice.setText(ArrayDataList.get(position).getItemPrice());
            holder.FinalItemName=(ArrayDataList.get(position).getItemName());
            holder.FinalItemCode=(ArrayDataList.get(position).getItemCode());
            holder.FinalItemPrice=(ArrayDataList.get(position).getItemPrice());






        }



        @Override
        public int getItemCount() {
            return ArrayDataList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView tv_ItemName,tv_ItemPrice,tv_pluNumber;
            LinearLayout ll_linearLayoutMaster,ll_backgroundPosition;
            String FinalItemName;
            String FinalItemCode;
            String FinalItemPrice;







            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//
                ll_backgroundPosition = itemView.findViewById(R.id.ll_backgroundPosition);
                tv_ItemName = itemView.findViewById(R.id.tv_ItemName);
                //    tv_discAmount = itemView.findViewById(R.id.tv_discAmount);
                tv_ItemPrice = itemView.findViewById(R.id.tv_ItemPrice);
                tv_pluNumber = itemView.findViewById(R.id.tv_pluNumber);

                ll_linearLayoutMaster = itemView.findViewById(R.id.ll_linearLayoutMaster);
                ll_linearLayoutMaster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), FinalItemName, Toast.LENGTH_SHORT).show();
                        // String Itemcode,String ItemName,String ItemPrice
                        Log.d("ITEM SEARCH", FinalItemCode);
                        Log.d("ITEM SEARCH", FinalItemName);
                        Log.d("ITEM SEARCH", FinalItemPrice);
                        DialogSearchItemAdd(FinalItemCode,FinalItemName,FinalItemPrice);
                    }
                });






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



//                ll_otherPaymentSelect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        FinalID=opID;
//                        FinalName=opName;
//                        FinalAllowRef=opReference;
//                        FinalAllowDetails=opDetails;
//                        FinalPaymentType=opType;
//                        FinalChangeType=opChangetype;
//
//                        Log.d("TAG",String.valueOf(FinalID));
//                        Log.d("TAG",String.valueOf(FinalName));
//                        Log.d("TAG",String.valueOf(FinalAllowRef));
//                        Log.d("TAG",String.valueOf(FinalAllowDetails));
//                        Log.d("TAG",String.valueOf(FinalPaymentType));
//                        Log.d("TAG",String.valueOf(FinalChangeType));
//
//
//
//
//
//                    }
//                });



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


    int InvoiceCursor = 0; //0 product ID // 1 insert qty
    EditText et_quantity;
    AlertDialog alertDialog;
    AlertDialog alertDialogQty;
    String FinalButtonPrefix;

    private void openQtyDialog(String ButtonPrefix){
        InvoiceCursor=1;
        FinalButtonPrefix=ButtonPrefix;

        Log.d("TAG", "openQtyDialog: "+ButtonPrefix);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogSlide);


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_qty_keypad, null);
        et_quantity = alertLayout.findViewById(R.id.et_quantity);

        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });

        ImageButton imgb_exit = alertLayout.findViewById(R.id.imgb_exit);
        imgb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InvoiceCursor=0;
                alertDialogQty.dismiss();
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

        alertDialogQty  = builder.create();

        alertDialogQty.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialogQty.show();
        alertDialogQty.setCanceledOnTouchOutside(false);
        alertDialogQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogQty.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemQty=1;
                String quantityText=et_quantity.getText().toString();
                if(!quantityText.matches("")){
                    FinalItemQty = et_quantity.getText().toString().trim();
                    Log.e("ItemQty","NOT EMPTY"+et_quantity.getText().toString());
                }else{
                    FinalItemQty = String.valueOf(itemQty);
                    Log.e("ItemQty","EMPTY");
                }
                //FinalItemQty = et_quantity.getText().toString().trim();
                //   ItemCursor = child.getId();

                //   insertPLU();



                if (PLUList.size()!=0) {
                    Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                    String str = removeWords(ButtonPrefix, "PLU");
                    // et_scanItem.setText("");
                    // insertPLU(Integer.parseInt(str));

                    if (PLUList.size()!=0) {
                        ItemCursor = Integer.parseInt(str)-1;
                        // FinalItemQty = "1";
                        addInvoice();
                    }

                }



//                            addInvoice();
                FinalItemQty = "";
                alertDialogQty.dismiss();
                InvoiceCursor=0;

            }
        });




    }
    private void openPrice(String ButtonPrefix){
        InvoiceCursor=101;
        FinalButtonPrefix=ButtonPrefix;

        Log.d("TAG", "openQtyDialog: "+ButtonPrefix);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogSlide);


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_price_keypad, null);
        et_quantity = alertLayout.findViewById(R.id.et_quantity);

        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_00 = alertLayout.findViewById(R.id.btn_00);
        btn_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "00");
            }
        });
        Button btn_dot = alertLayout.findViewById(R.id.btn_dot);
        btn_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + ".");
            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });

        ImageButton imgb_exit = alertLayout.findViewById(R.id.imgb_exit);
        imgb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InvoiceCursor=0;
                alertDialogQty.dismiss();
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

        alertDialogQty  = builder.create();

        alertDialogQty.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialogQty.show();
        alertDialogQty.setCanceledOnTouchOutside(false);
        alertDialogQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogQty.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemQty=1;
                String quantityText=et_quantity.getText().toString();
                if(!quantityText.matches("")){
                    FinalItemQty = et_quantity.getText().toString().trim();
                    Log.e("ItemQty","NOT EMPTY"+et_quantity.getText().toString());

                    if (PLUList.size()!=0) {
                        Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                        String str = removeWords(ButtonPrefix, "PLU");
                        // et_scanItem.setText("");
                        // insertPLU(Integer.parseInt(str));

                        if (PLUList.size()!=0) {
                            ItemCursor = Integer.parseInt(str)-1;
                            // FinalItemQty = "1";
                            //addInvoice();

                        }

                    }



//                            addInvoice();


                    alertDialogQty.dismiss();
                    openPriceQty(ButtonPrefix,et_quantity.getText().toString());
                    FinalItemQty = "";
                    InvoiceCursor=0;


                }else{
                    FinalItemQty = String.valueOf(itemQty);
                    Log.e("ItemQty","EMPTY");
                    Toast.makeText(getContext(), "PLEASE INPUT AMOUNT", Toast.LENGTH_SHORT).show();
                }
                //FinalItemQty = et_quantity.getText().toString().trim();
                //   ItemCursor = child.getId();

                //   insertPLU();





            }
        });




    } //InvoiceCursor = 101;
    String FinalOpenAmount;

    private void discountComputation(){

        readReferenceNumber();

        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());
        // RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        for (int i=0;i<recyclerviewAdapter.selectList.size();i++){

            Toast.makeText(getActivity(), recyclerviewAdapter.selectList.get(i).toString() + " " + readRefNumber, Toast.LENGTH_SHORT).show();


            db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
            itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'and TransactionID='"+readRefNumber+"'", null);
            while (itemListC.moveToNext()){


                String TransactionID=itemListC.getString(0);
                String OrderID=itemListC.getString(1);
                String OrderName=itemListC.getString(2);
                String OrderQty=itemListC.getString(3);
                String OrderPrice=itemListC.getString(4);
                String OrderPriceTotal=itemListC.getString(5);
                String TransactionTime=itemListC.getString(6);
                String TransactionDate=itemListC.getString(7);
                String DiscountType=discountType;
                String ItemRemarks=itemListC.getString(9);
                String DiscQty = OrderQty;


                double valuePercent = Double.valueOf(discountValue)/100;


                DecimalFormat format = new DecimalFormat("0.00");
                String formatted = format.format(valuePercent);

                double v1 = Double.parseDouble(OrderQty) * Double.parseDouble(OrderPrice);

                Log.e("DISCOUNT EXCLUDE",discountExlude);

                if (discountExlude.equals("YES")){
                    Log.e("DISCOUNT","DISCOUNT EXCLUDE VAT");
                    // DiscAmount = String.valueOf((v1 /1.12)*valuePercent );
                    Double grossAmt=v1;
                    Log.e("gross amt",String.valueOf(grossAmt));

                    negVat=grossAmt/1.12-grossAmt;
                    Log.e("neg vat",String.valueOf(negVat));

                    Double subTotal=grossAmt+negVat; // vatable
                    Log.e("subTotal",String.valueOf(subTotal));

                    Double discount=subTotal*valuePercent;
                    Log.e("discount",String.valueOf(discount));

                    Double netDue=subTotal-discount;
                    Log.e("netDue",String.valueOf(netDue));
                    // DiscAmount=String.valueOf(netDue);


                    DecimalFormat discAmountFormat = new DecimalFormat("0.00");

                    DiscAmount=discAmountFormat.format(discount*-1);


                    negVat=negVat*-1;







                }
                else if (discountExlude.equals("NO") && ProRated.equals("NO")){
                    Log.e("DISCOUNT","NO shit");
                    double v = v1;
                    DecimalFormat discAmountFormat = new DecimalFormat("0.00");
                    DiscAmount = discAmountFormat.format(v1 *valuePercent *-1 );
                }
                else if (discountExlude.equals("NO") && ProRated.equals("YES")){
                    Log.e("DISCOUNT","NO PRORATED");
                    double v = v1;
                    DecimalFormat discAmountFormat = new DecimalFormat("0.00");
                    DiscAmount = discAmountFormat.format((v1/1.12) *valuePercent *-1 );
                }

                DiscPercent = String.valueOf(valuePercent*100);
                String DiscBuyerName=" ";
                String DiscIdNumber=" ";
                String DiscOther=" ";


                //check for discount info
                if (discountCategory.equals("SCD") ||  discountCategory.equals("PWD") || discountType.equals("DIPLOMAT") ){




                    DiscBuyerName = CustInfo.getCustName();
                    DiscIdNumber=String.valueOf(CustInfo.getCustIDNo());
                    DiscOther=String.valueOf(CustInfo.getCustTIN());


                }



                Log.e("Discount type",DiscountType);
                Log.e("DISC BUYER",DiscBuyerName);
                Log.e("DISC ID Number",DiscIdNumber);
                Log.e("DISC TIN",DiscOther);




                //  db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                Cursor checkDuplicateDiscount = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'and TransactionID='"+readRefNumber+"'and DiscountType='"+DiscountType+"'" , null);

                if (checkDuplicateDiscount.getCount() !=0){
                    Toast.makeText(getActivity(), "ITEM EXIST", Toast.LENGTH_SHORT).show();
                }else{
                    myDb = new DatabaseHandler(getActivity());

                    DecimalFormat negVatFormat = new DecimalFormat("0.00");
                    String negvats = negVatFormat.format(negVat*-1);

                    boolean isInserted = myDb.insertInvoiceReceiptDiscountTemp(TransactionID,OrderID, OrderName,OrderQty,OrderPrice,OrderPriceTotal
                            ,TransactionTime,TransactionDate,DiscountType,ItemRemarks,DiscQty,DiscAmount,DiscPercent,negvats,DiscBuyerName,DiscIdNumber,DiscOther );

                    if (isInserted = true) {
                        Toast.makeText(getActivity(), "INSERTED", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "DISCOUNT ADDED", Toast.LENGTH_SHORT).show();



                        //edit order qty
//                                  db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
//                                  itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
//                                  while(itemListC.moveToNext()){
//                                      oldQty  = itemListC.getInt(3);
//                                      price = itemListC.getInt(4);
//
//                                  }
//                                  int newQty = oldQty-Integer.valueOf(DiscQty);
//                                  int newPrice = price * newQty;
//                                  String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
//                                  db2.execSQL(strsql);



                        //  selectList.clear();




                    } else {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    }


                    refreshRecycleview();
                    fillOrderList();
                    checkTotalAmount();




                }




            }




        }
        selectList.clear();


        db2.close(); //test












    }

    private void discountComputationOpenPrice(String amount){

        readReferenceNumber();

        RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,orderItemListDisc,selectList,getActivity());
        // RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        for (int i=0;i<recyclerviewAdapter.selectList.size();i++){

            Toast.makeText(getActivity(), recyclerviewAdapter.selectList.get(i).toString() + " " + readRefNumber, Toast.LENGTH_SHORT).show();


            db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
            itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'and TransactionID='"+readRefNumber+"'", null);
            while (itemListC.moveToNext()){


                String TransactionID=itemListC.getString(0);
                String OrderID=itemListC.getString(1);
                String OrderName=itemListC.getString(2);
                String OrderQty=itemListC.getString(3);
                String OrderPrice=itemListC.getString(4);
                String OrderPriceTotal=itemListC.getString(5);
                String TransactionTime=itemListC.getString(6);
                String TransactionDate=itemListC.getString(7);
                String DiscountType=discountType;
                String ItemRemarks=itemListC.getString(9);
                String DiscQty = OrderQty;


               // double valuePercent = Double.valueOf(discountValue)/100;


//                DecimalFormat format = new DecimalFormat("0.00");
//                String formatted = format.format(valuePercent);

           //     double v1 = Double.parseDouble(OrderQty) * Double.parseDouble(OrderPrice);

//                Log.e("DISCOUNT EXCLUDE",discountExlude);


                DiscAmount="-"+amount;
               // DiscPercent = String.valueOf(valuePercent*100);
                String DiscBuyerName=" ";
                String DiscIdNumber=" ";
                String DiscOther=" ";


                //check for discount info
                if (discountCategory.equals("SCD") ||  discountCategory.equals("PWD") || discountType.equals("DIPLOMAT") ){




                    DiscBuyerName = CustInfo.getCustName();
                    DiscIdNumber=String.valueOf(CustInfo.getCustIDNo());
                    DiscOther=String.valueOf(CustInfo.getCustTIN());


                }



                Log.e("Discount type",DiscountType);
                Log.e("DISC BUYER",DiscBuyerName);
                Log.e("DISC ID Number",DiscIdNumber);
                Log.e("DISC TIN",DiscOther);




                //  db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                Cursor checkDuplicateDiscount = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscountTemp where OrderID='"+recyclerviewAdapter.selectList.get(i)+"'and TransactionID='"+readRefNumber+"'and DiscountType='"+DiscountType+"'" , null);

                if (checkDuplicateDiscount.getCount() !=0){
                    Toast.makeText(getActivity(), "ITEM EXIST", Toast.LENGTH_SHORT).show();
                }else{
                    myDb = new DatabaseHandler(getActivity());

                    DecimalFormat negVatFormat = new DecimalFormat("0.00");
                    String negvats = negVatFormat.format(negVat*-1);

                    boolean isInserted = myDb.insertInvoiceReceiptDiscountTemp(TransactionID,OrderID, OrderName,OrderQty,OrderPrice,OrderPriceTotal
                            ,TransactionTime,TransactionDate,DiscountType,ItemRemarks,"1",DiscAmount,DiscPercent,negvats,DiscBuyerName,DiscIdNumber,DiscOther );

                    if (isInserted = true) {
                        Toast.makeText(getActivity(), "INSERTED", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "DISCOUNT ADDED", Toast.LENGTH_SHORT).show();



                        //edit order qty
//                                  db2 = getActivity().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
//                                  itemListC = db2.rawQuery("select * from InvoiceReceiptItem where OrderID='"+order_ID+"'", null);
//                                  while(itemListC.moveToNext()){
//                                      oldQty  = itemListC.getInt(3);
//                                      price = itemListC.getInt(4);
//
//                                  }
//                                  int newQty = oldQty-Integer.valueOf(DiscQty);
//                                  int newPrice = price * newQty;
//                                  String strsql = "UPDATE InvoiceReceiptItem SET OrderQty='" + newQty + "',OrderPriceTotal='"+newPrice+"'  where TransactionID='" + readRefNumber + "' and OrderID='"+order_ID+"'";
//                                  db2.execSQL(strsql);



                        //  selectList.clear();




                    } else {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    }


                    refreshRecycleview();
                    fillOrderList();
                    checkTotalAmount();




                }




            }




        }
        selectList.clear();


        db2.close(); //test












    }




    private void openPriceQty(String ButtonPrefix,String Amount){
        FinalOpenAmount=Amount;
        InvoiceCursor=102;
        FinalButtonPrefix=ButtonPrefix;

        Log.d("TAG", "openQtyDialog: "+ButtonPrefix);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogSlide);


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_openprice_qty_keypad, null);
        et_quantity = alertLayout.findViewById(R.id.et_quantity);

        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });

        ImageButton imgb_exit = alertLayout.findViewById(R.id.imgb_exit);
        imgb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InvoiceCursor=0;
                alertDialogQty.dismiss();
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

        alertDialogQty  = builder.create();

        alertDialogQty.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialogQty.show();
        alertDialogQty.setCanceledOnTouchOutside(false);
        alertDialogQty.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogQty.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemQty=1;
                String quantityText=et_quantity.getText().toString();
                if(!quantityText.matches("")){
                    FinalItemQty = et_quantity.getText().toString().trim();
                    Log.e("ItemQty","NOT EMPTY"+et_quantity.getText().toString());
                }else{
                    FinalItemQty = String.valueOf(itemQty);
                    Log.e("ItemQty","EMPTY");
                }
                //FinalItemQty = et_quantity.getText().toString().trim();
                //   ItemCursor = child.getId();

                //   insertPLU();



                if (PLUList.size()!=0) {
                    Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
                    String str = removeWords(ButtonPrefix, "PLU");
                    // et_scanItem.setText("");
                    // insertPLU(Integer.parseInt(str));

                    if (PLUList.size()!=0) {
                        ItemCursor = Integer.parseInt(str)-1;
                        // FinalItemQty = "1";
                        addInvoiceOpenPrice(Amount,(FinalItemQty));
                      //  showingAllItem();
                    }

                }



//                            addInvoice();
                FinalItemQty = "";
                alertDialogQty.dismiss();
                InvoiceCursor=0;
               // refreshRecycleview();
                //showingAllItem();


            }
        });




    }


        public class RecyclerviewAdapterPLU extends RecyclerView.Adapter <RecyclerviewAdapterPLU.MyViewHolder>{
        private ArrayList<invoice_plu_model> ButtonList;
        private Context Context;
        ArrayList<String>Selectlist = new ArrayList<String>();
        private RecyclerviewAdapterPLU.MyViewHolder holder;
        private int position;



        public RecyclerviewAdapterPLU(Context context, ArrayList<invoice_plu_model> buttonList) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.Context = context;
            this.ButtonList = buttonList;


        }

        @NonNull
        @Override
        public RecyclerviewAdapterPLU.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Context).inflate(R.layout.invoice_fragment_plu_layout,parent,false);
          //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_fragment_button_function_layout,parent,false);
            RecyclerviewAdapterPLU.MyViewHolder holder = new RecyclerviewAdapterPLU.MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerviewAdapterPLU.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;







            final invoice_plu_model button = ButtonList.get(position);

            String ButtonPrefix = ButtonList.get(position).getButtonID();
            String ButtonName=ButtonList.get(position).getItemName();
            String PriceOverrides=ButtonList.get(position).getPriceOverride();
            //= ButtonList.get(position).getButtonPrefix();
            //Toast.makeText(Context, "", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onBindViewHolder: "+PriceOverrides );

            if (PriceOverrides.equalsIgnoreCase("YES")){
                holder.tv_buttonNumber.setText("PLU + "+String.valueOf(ButtonPrefix) + " + Ent");
                holder.tv_buttonDetails.setText(String.valueOf(ButtonName));
                holder.ll_linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        // Toast.makeText(Context, "OPENING QTY", Toast.LENGTH_SHORT).show();
                        //openQtyDialog(ButtonPrefix);
                        if (PriceOverrides.equalsIgnoreCase("YES")){
                            Toast.makeText(Context, "PRICE OVERRIDE" + ButtonPrefix, Toast.LENGTH_SHORT).show();
                            openPrice(ButtonPrefix);
                        }
                        else{
                            Toast.makeText(Context, "OPENING QTY", Toast.LENGTH_SHORT).show();
                            openQtyDialog(ButtonPrefix);
                        }




                    }
                });
            }
            else{
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }












        }

        @Override
        public int getItemCount() {
            return ButtonList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{


            LinearLayout ll_linearLayout;
            TextView tv_buttonNumber,tv_buttonDetails;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//
                ll_linearLayout = itemView.findViewById(R.id.ll_linearLayout);
                tv_buttonNumber = itemView.findViewById(R.id.tv_buttonNumber);
                tv_buttonDetails = itemView.findViewById(R.id.tv_buttonDetails);




            }
        }
    }


    AlertDialog alertDialogDiscount;

    private void openPriceDiscountDialog(){
        // InitT9MapCode();
        //InvoiceCursor=101;

        // mapCode2Activate=5;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogSlide);


        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.add_item_price_keypad, null);
        et_quantity = alertLayout.findViewById(R.id.et_quantity);
        TextView tv_label = alertLayout.findViewById(R.id.tv_label);
        tv_label.setText("OPEN DISCOUNT");

        Button btn_0 = alertLayout.findViewById(R.id.btn_0);
        String text = et_quantity.getText().toString();
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "0");

            }
        });

        Button btn_00 = alertLayout.findViewById(R.id.btn_00);
        btn_00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "00");
            }
        });
        Button btn_dot = alertLayout.findViewById(R.id.btn_dot);
        btn_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + ".");
            }
        });

        Button btn_1 = alertLayout.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "1");
            }
        });
        Button btn_2 = alertLayout.findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "2");
            }
        });
        Button btn_3 = alertLayout.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "3");
            }
        });
        Button btn_4 = alertLayout.findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "4");
            }
        });
        Button btn_5 = alertLayout.findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "5");
            }
        });
        Button btn_6 = alertLayout.findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "6");
            }
        });
        Button btn_7 = alertLayout.findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "7");
            }
        });
        Button btn_8 = alertLayout.findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "8");
            }
        });
        Button btn_9 = alertLayout.findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_quantity.setText(et_quantity.getText().toString() + "9");
            }
        });
        ImageButton ibtn_delete = alertLayout.findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = et_quantity.getText().length();
                if (index > 0) {
                    et_quantity.getText().delete(index - 1, index);

                } else {

                }
            }
        });

        ImageButton imgb_exit = alertLayout.findViewById(R.id.imgb_exit);
        imgb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              //  alertDialog.dismiss();
                alertDialogDiscount.dismiss();

               // DialogCursor=0;
               // checkOut();
            }
        });


        ImageButton ibtn_confirm = alertLayout.findViewById(R.id.ibtn_confirm);

        builder.setView(alertLayout);

        alertDialogDiscount  = builder.create();

        alertDialogDiscount.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialogDiscount.show();
        alertDialogDiscount.setCanceledOnTouchOutside(false);
        alertDialogDiscount.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialogDiscount.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ibtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int itemQty=1;
//                String quantityText=et_quantity.getText().toString();
//                if(!quantityText.matches("")){
//                    FinalItemQty = et_quantity.getText().toString().trim();
//                    Log.e("ItemQty","NOT EMPTY"+et_quantity.getText().toString());
//
//                    if (PLUList.size()!=0) {
//                        Toast.makeText(getActivity(), removeWords(et_scanItem.getText().toString().trim(), "PLU"), Toast.LENGTH_SHORT).show();
//                        String str = removeWords(ButtonPrefix, "PLU");
//                        // et_scanItem.setText("");
//                        // insertPLU(Integer.parseInt(str));
//
//                        if (PLUList.size()!=0) {
//                            ItemCursor = Integer.parseInt(str)-1;
//                            // FinalItemQty = "1";
//                            //addInvoice();
//
//                        }
//
//                    }
//
//
//
////                            addInvoice();
//
//
//                    alertDialogQty.dismiss();
//                    openPriceQty(ButtonPrefix,et_quantity.getText().toString());
//                    FinalItemQty = "";
//                    InvoiceCursor=0;
//
//
//                }else{
//                    FinalItemQty = String.valueOf(itemQty);
//                    Log.e("ItemQty","EMPTY");
//                    Toast.makeText(getContext(), "PLEASE INPUT AMOUNT", Toast.LENGTH_SHORT).show();
//                }
//                //FinalItemQty = et_quantity.getText().toString().trim();
//                //   ItemCursor = child.getId();
//
//                //   insertPLU();
//
//
//
//

                discountComputationOpenPrice(et_quantity.getText().toString());

//                alertDialog.dismiss();
                alertDialogDiscount.dismiss();
                invoice();
             ////   DialogCursor=0;
              //  checkOut();

            }
        });


        Log.e("TAG", "openPrice: "+alertDialogDiscount );




    } //InvoiceCursor = 101;


//    private void openPriceDiscount(String amount){
//
//
//        Log.e("discount type",discountType.substring(0,3));
//        Log.e("discount type",discountValue);
//        Log.e("discount Exclude",discountExlude);
//        Log.e("ProRated",ProRated);
//
//
//        Double discTransTotal=0.00;
//        Double discTransVatableSales=0.00;
//        Double discTransDiscount=0.00;
//        Double discTransVatAmount=0.00;
//        Double discTransVatExemptSales=0.00;
//        Double discTransZeroRatedSales=0.00;
//        Double discTransLessVat=0.00;
//        Double discServiceCharge=0.00;
//        Double discTransDueAmount=0.00;
//
//
//        discTransTotal = (cashierItem.getTotalDueAmount());
//        //discTransVatableSales=0.00;
//        discTransVatableSales=cashierItem.getTotalDueAmount();
//        discTransDiscount = Double.parseDouble(amount);
//        discTransVatAmount=0.00;
//
//
//
//        lbl_total.setText(String.valueOf(Finalformat.format(discTransTotal)));
//        lbl_subtotal.setText(String.valueOf(Finalformat.format(discTransVatableSales)));
//        lbl_discount.setText(String.valueOf(Finalformat.format(discTransDiscount)));
//        Log.e("Discount Amount",String.valueOf(String.valueOf(Finalformat.format(discTransDiscount))));
//        lbl_tax.setText(String.valueOf(Finalformat.format(discTransVatAmount)));
//        tv_vatExemptSale.setText(String.valueOf(Finalformat.format(discTransVatExemptSales)));
//        tv_zeroRatedSales.setText(String.valueOf(String.valueOf(Finalformat.format(discTransZeroRatedSales))));
//        tv_lessVat.setText(String.valueOf(Finalformat.format(discTransLessVat)));
//        lbl_due.setText(String.valueOf(Finalformat.format(discTransDueAmount)));
//        lbl_dueFinal.setText(String.valueOf(DecFormat.format(discTransDueAmount)));
//
//        cashierItem.setTotalAmountToPay((discTransTotal));
//        cashierItem.setVatableAmount(discTransVatableSales);
//        cashierItem.setAmountDiscount(discTransDiscount);
//        cashierItem.setTaxAmount(discTransVatAmount);
//        cashierItem.setVatExemptSale(discTransVatExemptSales);
//        cashierItem.setZeroRatedSales(discTransZeroRatedSales);
//        cashierItem.setLessVat(discTransLessVat);
//        cashierItem.setTotalDueAmount(discTransDueAmount);
//
//
//        if (checkInvoiceReceiptItem.getCount()!=0){
//            DatabaseHandler myDb = new DatabaseHandler(getContext());
//            while(checkInvoiceReceiptItem.moveToNext()){
//
//                String TransactionID=checkInvoiceReceiptItem.getString(0);
//                String OrderID=checkInvoiceReceiptItem.getString(1);
//                String OrderName=checkInvoiceReceiptItem.getString(2);
//                String OrderQty=checkInvoiceReceiptItem.getString(3);
//                String OrderPrice=checkInvoiceReceiptItem.getString(4);
//                String OrderPriceTotal=checkInvoiceReceiptItem.getString(5);
//                String TransactionTime=checkInvoiceReceiptItem.getString(6);
//                String TransactionDate=checkInvoiceReceiptItem.getString(7);
//                String DiscountType=discountType;
//                String ItemRemarks=checkInvoiceReceiptItem.getString(9);
//                String DiscQty=checkInvoiceReceiptItem.getString(3);
//                String DiscAmount=String.valueOf((discTransDiscount)*-1);
//                // String DiscAmount=String.valueOf((300/1.12*(Double.parseDouble(discountValue)/100))*-1);
//                //     String vat=String.valueOf((Double.parseDouble(OrderPriceTotal)/1.12*.12)*-1);
//                String vat = String.valueOf(discTransVatAmount);
//                String DiscPercent=discountValue;
//
//                // String vat=tv_lessVat.getText().toString();
//                String DiscBuyerName=custInfo.getCustName();
//                String DiscIdNumber=custInfo.getCustIDNo();
//                String DiscOther=custInfo.getCustTIN();
//
//
//
//
//
//
//                boolean isInserted = myDb.insertInvoiceReceiptDiscountTemp
//                        (TransactionID,
//                                OrderID,
//                                OrderName,
//                                OrderQty,
//                                OrderPrice,
//                                OrderPriceTotal,
//                                TransactionTime,
//                                TransactionDate,
//                                DiscountType,
//                                ItemRemarks,
//                                DiscQty,
//                                String.format("%7.2f", Double.parseDouble(DiscAmount)),
//                                DiscPercent,
//                                String.format("%7.2f", Double.parseDouble(vat)),
//                                DiscBuyerName,
//                                DiscIdNumber,
//                                DiscOther );
//
//            }
//
//        }
//
//
//
//
//
//
//
//
//
//
//        //region
//
//
//
//        if (discCategory.equals("SCD")){
//            ll_forDiscountType.setVisibility(View.VISIBLE);
//            lbl_discountLabel.setText("DISCOUNT");
//        }
//        else if (discCategory.equals("PWD")){
//            ll_forDiscountType.setVisibility(View.VISIBLE);
//            lbl_discountLabel.setText("DISCOUNT");
//        }
//        else if (discountType.equals("DIPLOMAT")){
//            ll_forDiscountType.setVisibility(View.VISIBLE);
//            lbl_discountLabel.setText("DIPLOMAT");
//        }
//        else if (discCategory.equals("REG")){
//            ll_forDiscountType.setVisibility(View.VISIBLE);
//            lbl_discountLabel.setText("DISCOUNT");
//        }
//
//
//        //endregion
//
//
//
//
//
//        loadLabelData();
//
//        cashierItem.setTotalAmountToPay(discTransDueAmount);
//        Log.e("totalAmountToPay",String.valueOf(discTransDueAmount));
//        alertDialog.dismiss();
//
//
//
//    }

    public class RecyclerviewAdapterButton extends RecyclerView.Adapter <RecyclerviewAdapterButton.MyViewHolder>{

        private ArrayList<invoice_fragment_button_model> FunctionList;
        ArrayList<String>Selectlist = new ArrayList<String>();
        private Context Context;
        private RecyclerviewAdapterButton.MyViewHolder holder;
        private int position;
        private int switchMode;

        public RecyclerviewAdapterButton(Context context, ArrayList<invoice_fragment_button_model> functionList,ArrayList<String>selectlist,int switchModeButton) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.Context = context;
            this.FunctionList = functionList;
            this.Selectlist = selectlist;
            this.switchMode=switchModeButton;

        }

        @NonNull
        @Override
        public RecyclerviewAdapterButton.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_fragment_button_function_layout,parent,false);


            RecyclerviewAdapterButton.MyViewHolder holder = new RecyclerviewAdapterButton.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull RecyclerviewAdapterButton.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

//            holder.tv_ItemName.setText(ArrayDataList.get(position).getItemName());
            int ButtonID = FunctionList.get(position).getButtonID();
            String ButtonName=FunctionList.get(position).getButtonName();

            holder.tv_buttonNumber.setText("Btn + " + String.valueOf(ButtonID) + " + Ent");
            holder.tv_buttonDetails.setText(String.valueOf(ButtonName));
            holder.ll_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "SWITCHMODE : "+String.valueOf(switchMode));
                   // Toast.makeText(Context, "ID : "+String.valueOf(ButtonID) + "\n Name :" + ButtonName , Toast.LENGTH_SHORT).show();
                    if (switchMode==1) {
                        ShowCashierFunctionButton(ButtonName);
                        //showingAllItem();
                    }else{
                        ShowManagerFunctionButton(ButtonName);

                    }



                }
            });

        }

        @Override
        public int getItemCount() {
            return FunctionList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{


            LinearLayout ll_linearLayout;
            TextView tv_buttonNumber,tv_buttonDetails;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//
                ll_linearLayout = itemView.findViewById(R.id.ll_linearLayout);
                tv_buttonNumber = itemView.findViewById(R.id.tv_buttonNumber);
                tv_buttonDetails = itemView.findViewById(R.id.tv_buttonDetails);


            }
        }
    }

    ArrayList<invoice_discount_model> DiscountModelList = new ArrayList<>();
    public class RecyclerviewAdapterDiscount extends RecyclerView.Adapter <RecyclerviewAdapterDiscount.MyViewHolder>{

        private  ArrayList<invoice_discount_model> DiscountModelList;
        ArrayList<String>Selectlist = new ArrayList<String>();
        private Context Context;
        private RecyclerviewAdapterDiscount.MyViewHolder holder;
        private int position;
        private int switchMode;

        public RecyclerviewAdapterDiscount(Context context, ArrayList<invoice_discount_model> discountModelList) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.Context = context;
            this.DiscountModelList = discountModelList;


        }

        @NonNull
        @Override
        public RecyclerviewAdapterDiscount.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_fragment_button_function_layout,parent,false);


            RecyclerviewAdapterDiscount.MyViewHolder holder = new RecyclerviewAdapterDiscount.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull RecyclerviewAdapterDiscount.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

//            holder.tv_ItemName.setText(ArrayDataList.get(position).getItemName());
           // int id = DiscountModelList.get(position).getDiscountID().indexOf();
            String ButtonID = DiscountModelList.get(position).getDiscountID();
            String ButtonName=DiscountModelList.get(position).getDiscountName();

            holder.tv_buttonNumber.setText("Btn + " + String.valueOf(ButtonID) + " + Ent");
            holder.tv_buttonDetails.setText(String.valueOf(ButtonName));
            holder.ll_linearLayout.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View view) {


                            discountType=DiscountIDList.get(position);
                            discountValue=DiscountAmountList.get(position);
                            discountExlude=DiscountExcludeTaxList.get(position);
                            ProRated = ProRatedTaxList.get(position);
                            discountCategory=DiscountCategoryList.get(position);
                            discountOpenDiscount = OpenDiscountList.get(position);

                          //  CustInfo.showDialog(getContext());


                            Log.e("DISCOUNT TYPE SUBS",discountType.substring(0,3));

//                            alertDialog.dismiss();


                            if (discountOpenDiscount.equalsIgnoreCase("YES")){

                                openPriceDiscountDialog();


                            }else {


                                //
                                if (discountCategory.equals("SCD") || discountCategory.equals("PWD")) {
                                    showDialog();
                                } else {
                                    discountComputation();
                                }
                            }





                         //   showingItem();



                            // showSCDiscount();



                            //need authorization









                        }

            });


        }

        @Override
        public int getItemCount() {
            return DiscountModelList.size();
        }
        public List<String> getList() {
            return selectList;
        }

        //note!



        public class MyViewHolder extends RecyclerView.ViewHolder{


            LinearLayout ll_linearLayout;
            TextView tv_buttonNumber,tv_buttonDetails;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//
                ll_linearLayout = itemView.findViewById(R.id.ll_linearLayout);
                tv_buttonNumber = itemView.findViewById(R.id.tv_buttonNumber);
                tv_buttonDetails = itemView.findViewById(R.id.tv_buttonDetails);


            }
        }
    }


    ArrayList<invoice_transaction_model> arraylistTransaction = new ArrayList<>();
    public class RecyclerviewAdapterResumeTrans extends RecyclerView.Adapter <RecyclerviewAdapterResumeTrans.MyViewHolder>{

        private  ArrayList<invoice_transaction_model> arraylistTransaction;
        ArrayList<String>Selectlist = new ArrayList<String>();
        private Context Context;
        private RecyclerviewAdapterResumeTrans.MyViewHolder holder;
        private int position;
        private int switchMode;

        public RecyclerviewAdapterResumeTrans(Context context, ArrayList<invoice_transaction_model> arraylistTransaction) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.Context = context;
            this.arraylistTransaction = arraylistTransaction;


        }

        @NonNull
        @Override
        public RecyclerviewAdapterResumeTrans.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_resume_transaction_layout,parent,false);


            RecyclerviewAdapterResumeTrans.MyViewHolder holder = new RecyclerviewAdapterResumeTrans.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull RecyclerviewAdapterResumeTrans.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

            holder.tv_btnNumber.setText(String.valueOf("Btn + "+arraylistTransaction.get(position).getButtonID() + " Ent"));
            holder.tv_itemName.setText(arraylistTransaction.get(position).getTransaction());
            holder.tv_itemDate.setText(arraylistTransaction.get(position).getDate());
            holder.tv_itemTime.setText(arraylistTransaction.get(position).getTime());

            holder.ll_backgroundPosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransReprint = arraylistTransaction.get(position).getTransaction();
                    Toast.makeText(Context, TransReprint, Toast.LENGTH_SHORT).show();
                }
            });


//            holder.tv_ItemName.setText(ArrayDataList.get(position).getItemName());
            // int id = DiscountModelList.get(position).getDiscountID().indexOf();
//            String ButtonID = DiscountModelList.get(position).getDiscountID();
//            String ButtonName=DiscountModelList.get(position).getDiscountName();
//
//            holder.tv_buttonNumber.setText("Btn + " + String.valueOf(ButtonID) + " + Ent");
//            holder.tv_buttonDetails.setText(String.valueOf(ButtonName));
//            holder.ll_linearLayout.setOnClickListener(new View.OnClickListener() {
//
//
//                @Override
//                public void onClick(View view) {
//
//
//                    discountType=DiscountIDList.get(position);
//                    discountValue=DiscountAmountList.get(position);
//                    discountExlude=DiscountExcludeTaxList.get(position);
//                    ProRated = ProRatedTaxList.get(position);
//                    discountCategory=DiscountCategoryList.get(position);
//
//
//
//                }
//
//            });


        }

        @Override
        public int getItemCount() {
            return arraylistTransaction.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{


            LinearLayout ll_backgroundPosition;
            TextView tv_btnNumber,tv_itemName,tv_itemDate,tv_itemTime;



            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                ll_backgroundPosition = itemView.findViewById(R.id.ll_backgroundPosition);
                tv_btnNumber = itemView.findViewById(R.id.tv_BtnNumber);
                tv_itemName = itemView.findViewById(R.id.tv_ItemName);
                tv_itemDate = itemView.findViewById(R.id.tv_itemDate);
                tv_itemTime = itemView.findViewById(R.id.tv_itemTime);
//



            }
        }
    }











}