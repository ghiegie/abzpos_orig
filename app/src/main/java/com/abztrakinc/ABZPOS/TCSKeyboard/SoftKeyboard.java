package com.abztrakinc.ABZPOS.TCSKeyboard;

public class SoftKeyboard {
    private static int ShowKboard;

    public static int getShowKboard() {
        return ShowKboard;
    }

    public void setShowKboard(int showKboard) {
        showKboard=0; //0 hide 1 show
        ShowKboard = showKboard;
    }

}
