package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MyKeyboardLogin extends LinearLayout implements View.OnClickListener {

    // constructors
    public MyKeyboardLogin(Context context) {
        this(context, null, 0);
    }

    public MyKeyboardLogin(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyKeyboardLogin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // keyboard keys (buttons)
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton0;
    private Button mButtonClear;
    private Button mButtonDot;
    private Button mButtonDelete;
    private Button mButtonEnter;
    private Button mExactAmount;
    private ImageButton mNext;

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    private void init(Context context, AttributeSet attrs) {

        // initialize buttons
        LayoutInflater.from(context).inflate(R.layout.keyboard_login, this, true);
        mButton1 = (Button) findViewById(R.id.button_1);
        mButton2 = (Button) findViewById(R.id.button_2);
        mButton3 = (Button) findViewById(R.id.button_3);
        mButton4 = (Button) findViewById(R.id.button_4);
        mButton5 = (Button) findViewById(R.id.button_5);
        mButton6 = (Button) findViewById(R.id.button_6);
        mButton7 = (Button) findViewById(R.id.button_7);
        mButton8 = (Button) findViewById(R.id.button_8);
        mButton9 = (Button) findViewById(R.id.button_9);
        mButton0 = (Button) findViewById(R.id.button_0);

        mButtonClear=(Button) findViewById(R.id.button_Clear);
        mNext =  findViewById(R.id.button_next);

        // set button click listeners
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton0.setOnClickListener(this);
        mButtonClear.setOnClickListener(this);
        mNext.setOnClickListener(this);


        // map buttons IDs to input strings
        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_0, "0");
        keyValues.put(R.id.button_0, "0");
      //  keyValues.put(R.id.button_enter, "\n");
        keyValues.put(R.id.button_Clear, "\n");
        keyValues.put(R.id.button_next,"\n");


    }
    int x=0;
    @Override
    public void onClick(View v) {


        Log.d("KEYS",String.valueOf(v.getId()));

        // do nothing if the InputConnection has not been set yet
       // if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() == R.id.button_Clear) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
            Log.d("keys clear",String.valueOf(R.id.button_Clear));

        }
        if ((v.getId()==R.id.button_next)){
            Log.d("TAG", "onClick: button_enter");
          //  inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));


            if (x==0) {
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_NEXT);
                x=1;
            }

            else{
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_PREVIOUS);
                x=0;
            }

            Log.e("TAG", "onClick: "+String.valueOf(x));





        }

        else {
            String value = keyValues.get(v.getId());
          //  Log.e("kbord",value);
            inputConnection.commitText(value, 1);
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        Log.e("TestIC",String.valueOf(ic));
        this.inputConnection = ic;
    }
}