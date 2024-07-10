package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.POSAndroidDB;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.accountSettings;
import com.abztrakinc.ABZPOS.settingsDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class admin_manage_staff extends AppCompatActivity {
    com.abztrakinc.ABZPOS.accountSettings accountSettings;
    Button btn_update;
    EditText et_userNumber,et_pass,et_name;
    Switch sw_switch;
    ImageButton ib_return;
    ImageButton ib_addnew2;

    ArrayList<String> accountID;
    ArrayList<String> accountName;
    ArrayList<String> accountPassword;
    ArrayList<String> accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_staff2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        accountSettings = new accountSettings();

//        Initialize();
//        createStaffList();
//        showStaffAdd();

//        ib_return=findViewById(R.id.ib_return);
//        ib_return.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });f

       // insertDatabase();

        showAccountList();
        refreshRecycleviewAccount();
        KeyBoardMap();
        rv_manageStaff.requestFocus();

        ib_return=findViewById(R.id.ib_return);
        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ib_addnew2=findViewById(R.id.ib_addnew);
        ib_addnew2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAccountDialog();
            }
        });






    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d("TAG", "keycode: "+keyCode + "  Keyevent "+event);


        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // Return false if scrolled to the bounds and allow focus to move off the list
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    Log.e("TAG", "onKeyDown: true" );
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    Log.e("TAG", "onKeyUp: true" );
                    return true ;
                }
            }

            return false;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void Initialize() {
//        btn_update = findViewById(R.id.btn_Update);
//        et_userNumber = findViewById(R.id.et_UserNumber);
//        et_pass = findViewById(R.id.et_Pass);
//        et_name= findViewById(R.id.et_Name);
//        sw_switch = findViewById(R.id.sw_adminSwitch);
//
//        btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                insertDatabase();
//            }
//        });

    }


    int idLayout;
    private void createStaffList() {
        loadAccountDB();

        LinearLayout sv = this.findViewById(R.id.ll_staff_list);
        sv.removeAllViews();




        for (int x = 0; x < count; x++) {
            CardView cardView = new CardView(this);
            sv.addView(cardView);

            View child = getLayoutInflater().inflate(R.layout.activity_admin_manage_staff_recyclerview, null);//child.xml
            child.setId(x);

            TextView id = child.findViewById(R.id.tv_staff_id);
            id.setText(accountID.get(x));
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(admin_manage_staff.this, id.getText().toString(), Toast.LENGTH_SHORT).show();

                    SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
                    cursor = db.rawQuery("select * from Accountsettings where accountNumber='"+ id.getText().toString()+"'", null);
                    if (cursor.getCount() != 0) {
                            cursor.moveToNext();
                                accountSettings.setAccountNumber(cursor.getString(1));
                                accountSettings.setAccountUsername(cursor.getString(2));
                                accountSettings.setAccountPassword(cursor.getString(3));
                                accountSettings.setAccountType(cursor.getString(4));

                        Log.d("TAG", "onClick: "+accountSettings.getAccountNumber());
                        Log.d("TAG", "onClick: "+accountSettings.getAccountUsername());
                        Log.d("TAG", "onClick: "+accountSettings.getAccountPassword());
                        Log.d("TAG", "onClick: "+accountSettings.getAccountType());

                        showStaffAdd();

                    }else{
                        Toast.makeText(admin_manage_staff.this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                    }


                }

            });

            cardView.addView(child);



        }

    }
    int userAccountType;
    private void showStaffAdd(){


        LinearLayout sv = this.findViewById(R.id.ll_staff);
        sv.removeAllViews();



            CardView cardView = new CardView(this);
            sv.addView(cardView);
            View child = getLayoutInflater().inflate(R.layout.activity_admin_manage_staff_add, null);//child.xml
        EditText et_Name =  child.findViewById(R.id.et_Name);
        EditText et_UserNumber =  child.findViewById(R.id.et_UserNumber);
        EditText et_Pass = child.findViewById(R.id.et_Pass);
        Switch  sw_adminSwitch = child.findViewById(R.id.sw_adminSwitch);
        Button btn_save = child.findViewById(R.id.btn_save);
        Button btn_delete = child.findViewById(R.id.btn_delete);

        et_Name.setText(accountSettings.getAccountUsername());

        et_UserNumber.setText(accountSettings.getAccountNumber());
        et_Pass.setText(accountSettings.getAccountPassword());

        if (accountSettings.getAccountType().equals("1")){
            sw_adminSwitch.setChecked(true);
        }

        sw_adminSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_adminSwitch.isChecked()) {


                    accountSettings.setAccountType("2");// 2 admin
                    Log.d("TAG","2 admin");
                    userAccountType=2;
                }
                else{
                    accountSettings.setAccountType("1"); //1 employe
                    userAccountType=1;
                    Log.d("TAG","1 emp");
                }
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                settingsDB settingsDB = new settingsDB(getApplication());
//
                accountSettings accountSettings = new accountSettings();

                accountSettings.setAccountNumber(et_UserNumber.getText().toString());
                accountSettings.setAccountUsername(et_Name.getText().toString());
                accountSettings.setAccountPassword(et_Pass.getText().toString());

                String accountNumber = accountSettings.getAccountNumber().toString();
                String accountUsername = accountSettings.getAccountUsername();
                String accountPassword = accountSettings.getAccountPassword();
                String accountType = accountSettings.getAccountType();




                SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
                Cursor c = db.rawQuery("select * from Accountsettings where accountNumber='"+accountNumber+"'", null);

                if (c.getCount()!=0){
                    Toast.makeText(admin_manage_staff.this, "ACCOUNT NUMBER EXIST", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean isInserted = settingsDB.insertAccount(accountNumber,accountUsername,accountPassword,String.valueOf(userAccountType));

                    if (isInserted = true) {
                        Toast.makeText(getApplication(), "ACCOUNT INSERTED", Toast.LENGTH_SHORT).show();
                        et_UserNumber.setText("");
                        et_Name.setText("");
                        et_Pass.setText("");
                        sw_adminSwitch.setChecked(false);
                        createStaffList();
                    }
                    else {
                        Toast.makeText(getApplication(), "ACCOUNT NOT INSERTED", Toast.LENGTH_SHORT).show();
                    }
                }








            }
        });



        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
               // Cursor c = db.rawQuery("select * from Accountsettings where accountNumber='"+accountNumber+"'", null);
                String accountNumber = et_UserNumber.getText().toString();
                String strsql3 = "Delete from Accountsettings where accountNumber='"+accountNumber+"'";
                db.execSQL(strsql3);
                et_UserNumber.setText("");
                et_Name.setText("");
                et_Pass.setText("");
                sw_adminSwitch.setChecked(false);
                loadAccountDB();
                createStaffList();
            }
        });


           // child.setId(x);
            cardView.addView(child);

    }
    int count;
    Cursor  cursor,cursor2;
    private void loadAccountDB(){

       SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        cursor2 = db.rawQuery("select * from Accountsettings", null);
        if (cursor2.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else{

            accountID = new ArrayList<String>();
            accountID.clear();

            while(cursor2.moveToNext()){


                count=cursor2.getCount();
                accountID.add(cursor2.getString(1));

                // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

            }
        }
    }



    SQLiteDatabase db;
    List<admin_manage_staff_accounts> AccountList = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();
    private void showAccountList(){
       // rv_manageStaff.removeAllViews();
        AccountList.clear();

        db = openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from AccountSettings where accountNumber!='001' ", null);



        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        admin_manage_staff_accounts p0 = null;
        int x=1;
        while(c.moveToNext()){
            p0=new admin_manage_staff_accounts(
                    x,
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getInt(4)
                   );
            x++;

            Log.d("ACcountlist", "showAccountList: "+p0.toString());


            AccountList.addAll(Arrays.asList(new admin_manage_staff_accounts[]{p0}));
        }

        c.close();
        db.close();
    }
    private void updateAccount(String AccountNumber,int accountType){
        db = openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
       // Cursor c = db.rawQuery("", null);
        String sql = "UPDATE AccountSettings set accountType='"+accountType+"' where accountNumber='"+AccountNumber+"'";
        db.execSQL(sql);

    }
    private void addAccountDialog(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.activity_admin_manage_staff_add, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();

        EditText et_Name = alertLayout.findViewById(R.id.et_Name);
        EditText et_UserNumber = alertLayout.findViewById(R.id.et_UserNumber);
        EditText et_Pass = alertLayout.findViewById(R.id.et_Pass);
        Button btn_save = alertLayout.findViewById(R.id.btn_save);
        SwitchCompat sw_accountType = alertLayout.findViewById(R.id.sw_adminSwitch);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int accountType;

                if(sw_accountType.isChecked()){
                    accountType=2;
                }else{
                    accountType=1;
                }

                insertDatabase(et_Name.getText().toString(),et_UserNumber.getText().toString(),et_Pass.getText().toString(),accountType);
                showAccountList();
                refreshRecycleviewAccountBottom();
                alertDialog.cancel();


            }
        });


        alertDialog.show();
    }
    private void insertDatabase(String AccountNumber,String AccountUserName, String AccountPassword, int AccountType) {


        settingsDB settingsDB = new settingsDB(this);

        boolean isInserted = settingsDB.insertAccount(AccountNumber,AccountUserName,AccountPassword,String.valueOf(AccountType));

        if (isInserted = true) {
            Toast.makeText(this, "ACCOUNT INSERTED", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "ACCOUNT NOT INSERTED", Toast.LENGTH_SHORT).show();
        }

    }

    RecyclerView rv_manageStaff;
    LinearLayoutManager layoutManager;
    RecyclerviewAdapter mAdapter;
    public void refreshRecycleviewAccount(){
        rv_manageStaff = findViewById(R.id.rv_manageStaff);

        rv_manageStaff.setHasFixedSize(true);
       // layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        rv_manageStaff.setLayoutManager(layoutManager);
        mAdapter=new admin_manage_staff.RecyclerviewAdapter(AccountList,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_manageStaff.setAdapter(mAdapter);
     //   rv_manageStaff.smoothScrollToPosition(rv_manageStaff.getBottom());

        //invoice item list



    }
    public void refreshRecycleviewAccountBottom(){
        rv_manageStaff = findViewById(R.id.rv_manageStaff);

        rv_manageStaff.setHasFixedSize(true);
        // layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        rv_manageStaff.setLayoutManager(layoutManager);
        mAdapter=new admin_manage_staff.RecyclerviewAdapter(AccountList,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_manageStaff.setAdapter(mAdapter);
          rv_manageStaff.smoothScrollToPosition(rv_manageStaff.getBottom());

        //invoice item list



    }


    KeyboardDevice kboard;
    KeyCodeManager kManager;
    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;

    }int xCursor=0;
    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;


        if (input.equalsIgnoreCase("Exit")){

            //triggerRebirth();
            onBackPressed();
        }
        if(input.equalsIgnoreCase("Page Dn")){
            xCursor += 1;
            rv_manageStaff.post(new Runnable() {
                @Override
                public void run() {
                    rv_manageStaff.scrollToPosition(xCursor);
                    // Here adapter.getItemCount()== child count
                    Log.d("TAG", "SimulateKeyboard:"+xCursor);
                }
            });
            if (input.equalsIgnoreCase("Clear")){
                xCursor=0;
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








    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_manage_staff.RecyclerviewAdapter.MyViewHolder>{
        List<admin_manage_staff_accounts> AccountList;
        Context context;
        int selectItem =0;


        @Override
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            // Handle key up and key down and attempt to move selection
            recyclerView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                    // Return false if scrolled to the bounds and allow focus to move off the list
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            return tryMoveSelection(lm, 1);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            return tryMoveSelection(lm, -1);
                        }
                    }

                    return false;
                }
            });
        }



        private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
            int nextSelectItem = selectItem + direction;

            // If still within valid bounds, move the selection, notify to redraw, and scroll
            Log.d("TAG", "tryMoveSelection: test");
            if (nextSelectItem == 0) {
                notifyItemChanged(selectItem);
                selectItem = nextSelectItem;
                notifyItemChanged(selectItem);
                lm.scrollToPosition(selectItem);
                return true;
            }

            return false;
        }



        ArrayList<String> selectList = new ArrayList<>();
        private admin_manage_staff.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<admin_manage_staff_accounts> AccountList, ArrayList<String> selectList, Context context) {

            this.AccountList = AccountList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_manage_staff.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_manage_staff2_layout,parent,false);



            admin_manage_staff.RecyclerviewAdapter.MyViewHolder holder = new admin_manage_staff.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_manage_staff.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

//            holder.tv_ItemName.setText(String.valueOf((ProductList.get(position).getItemName())));
            holder.tv_accountID.setText(String.valueOf(AccountList.get(position).getID()));
            holder.tv_accountName.setText(String.valueOf(AccountList.get(position).getAccountUserName()));
            holder.tv_accountNumber.setText(String.valueOf(AccountList.get(position).getAccountNumber()));
            holder.tv_accountPassword.setText(String.valueOf(AccountList.get(position).getAccountPassword()));




            int switchIndicator = AccountList.get(position).getAccountType();
            if (switchIndicator == 1) // normal user
            {
                holder.sw_accountType.setChecked(false);
            }
            else{
                holder.sw_accountType.setChecked(true);
            }

            holder.ib_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
                    // Cursor c = db.rawQuery("select * from Accountsettings where accountNumber='"+accountNumber+"'", null);
                   // String accountNumber = et_UserNumber.getText().toString();
                    String strsql3 = "Delete from Accountsettings where accountNumber='"+AccountList.get(position).getAccountNumber()+"'";
                    db.execSQL(strsql3);
//                    et_UserNumber.setText("");
//                    et_Name.setText("");
//                    et_Pass.setText("");
//                    sw_adminSwitch.setChecked(false);


                    showAccountList();
                    refreshRecycleviewAccount();


                }
            });



//            holder.tv_ItemDesc.setText((String.valueOf(ProductList.get(position).getItemDescription())));
//            holder.tv_ItemID.setText((String.valueOf(ProductList.get(position).getItemID())));
//            holder.tv_ItemPrice.setText((String.valueOf(ProductList.get(position).getItemPrice())));
//            holder.tv_ItemQty.setText((String.valueOf(ProductList.get(position).getItemQty())));
//            holder.tv_FastMoving.setText((String.valueOf(ProductList.get(position).getFastMovingItem())));
//            holder.tv_FastMovingButton.setText((String.valueOf(ProductList.get(position).getFastMovingButton())));
//            holder.tv_ItemCode.setText((String.valueOf(ProductList.get(position).getItemCode())));
//            holder.tv_ItemBarcode.setText((String.valueOf(ProductList.get(position).getBarcode())));
//            holder.tv_VatIndicator.setText((String.valueOf(ProductList.get(position).getVATIndicator())));
//            holder.tv_itemCount.setText(String.valueOf(position+1));






        }

        @Override
        public int getItemCount() {
            return AccountList.size();
        }
        public List<String> getList() {
            return selectList;
        }






        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_accountID;
            TextView tv_accountName;
            TextView tv_accountNumber;
            TextView tv_accountPassword;
            SwitchCompat sw_accountType;
           // TextView tv_ItemPrice;

            LinearLayout ll_linearLayout;
            TextView tv_VatIndicator;
            //CardView parentLayout;

            LinearLayout ll_accountLinear;
            ImageButton ib_edit,ib_save,ib_remove,ib_addnew;





            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_accountID = itemView.findViewById(R.id.tv_accountID);
                tv_accountNumber = itemView.findViewById(R.id.tv_accountNumber);
                tv_accountName = itemView.findViewById(R.id.tv_accountName);
                tv_accountPassword = itemView.findViewById(R.id.tv_accountPassword);
                sw_accountType = itemView.findViewById(R.id.sw_accountType);
                ll_accountLinear = itemView.findViewById(R.id.ll_accountLinear);
                ib_edit = itemView.findViewById(R.id.ib_edit);
                ib_save = itemView.findViewById(R.id.ib_save);
                ib_addnew = findViewById(R.id.ib_addnew);
                ib_remove= itemView.findViewById(R.id.ib_remove);

              //  tv_ItemPrice = itemView.findViewById(R.id.tv_ItemPrice);
                ib_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                ib_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int accountType;
                        String accountNumber;
//                        Log.d("TAG", "onClick: "+ tv_accountNumber.getText().toString());
                        accountNumber = tv_accountNumber.getText().toString();
                        if (sw_accountType.isChecked()){
                            Log.d("TAG", "onClick: "+ "ADMIN");
                            accountType=2;
                        }
                        else{
                            accountType=1;
                            Log.d("TAG", "onClick: "+"NOT ADMIN");
                        }
                        updateAccount(accountNumber,accountType);

                       // Log.d("TAG", "onClick: "+ tv_accountNumber.getText().toString());
                    }
                });

                ib_addnew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //show dialog
                        addAccountDialog();
                    }
                });

                ll_accountLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyItemChanged(selectItem);
                        selectItem = getLayoutPosition();
                        notifyItemChanged(selectItem);
                    }
                });









            }
        }
    }




}