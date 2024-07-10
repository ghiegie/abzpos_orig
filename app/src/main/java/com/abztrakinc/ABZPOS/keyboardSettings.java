package com.abztrakinc.ABZPOS;
import android.view.Window;
import android.view.WindowManager;

public class keyboardSettings {

 Window window;

    public void keyboard(){
       window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


    }


    public Window getWindow() {

        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }


}

