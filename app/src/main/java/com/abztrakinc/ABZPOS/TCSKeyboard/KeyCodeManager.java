package com.abztrakinc.ABZPOS.TCSKeyboard;
import android.util.Log;
import android.view.KeyEvent;

import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.R;

import java.util.HashMap;

public class KeyCodeManager {
    public static final int ERR_MAP_NOT_CONTAIN = -100;
    public static HashMap<Integer, Integer> keyCodeMap = new HashMap<>();

    public static void InitKeyMap() {
        try {
//                keyCodeMap.clear();
//                keyCodeMap.put(KEY_INDEX_NUMPAD0, KeyEvent.KEYCODE_0);
//                keyCodeMap.put(KEY_INDEX_NUMPAD1, KeyEvent.KEYCODE_1);
//                keyCodeMap.put(KEY_INDEX_NUMPAD2, KeyEvent.KEYCODE_2);
//                keyCodeMap.put(KEY_INDEX_NUMPAD3, KeyEvent.KEYCODE_3);
//                keyCodeMap.put(KEY_INDEX_NUMPAD4, KeyEvent.KEYCODE_4);
//                keyCodeMap.put(KEY_INDEX_NUMPAD5, KeyEvent.KEYCODE_5);
//                keyCodeMap.put(KEY_INDEX_NUMPAD6, KeyEvent.KEYCODE_6);
//                keyCodeMap.put(KEY_INDEX_NUMPAD7, KeyEvent.KEYCODE_7);
                keyCodeMap.put(KEY_INDEX_NUMPAD8, KeyEvent.KEYCODE_8);
//                keyCodeMap.put(KEY_INDEX_NUMPAD9, KeyEvent.KEYCODE_9);
//                keyCodeMap.put(KEY_INDEX_NUMPADDEC, KeyEvent.KEYCODE_PERIOD);
//               // keyCodeMap.put(KEY_INDEX_NUMPAD00,00);
//               // keyCodeMap.put(R.id.button_00, "00");
//                keyCodeMap.put(KEY_INDEX_EC, KeyEvent.KEYCODE_DEL);
//                keyCodeMap.put(KEY_INDEX_CLEAR,KeyEvent.KEYCODE_MOVE_END);
//
//
//            //map for fast moving item
//
//                keyCodeMap.put(KEY_INDEX_DEPT1,KeyEvent.KEYCODE_BUTTON_1);
//                keyCodeMap.put(KEY_INDEX_DEPT2,KeyEvent.KEYCODE_BUTTON_2);
//                keyCodeMap.put(KEY_INDEX_DEPT3,KeyEvent.KEYCODE_BUTTON_3);
//                keyCodeMap.put(KEY_INDEX_DEPT4,KeyEvent.KEYCODE_BUTTON_4);
//                keyCodeMap.put(KEY_INDEX_DEPT5,KeyEvent.KEYCODE_BUTTON_5);
//                keyCodeMap.put(KEY_INDEX_DEPT6,KeyEvent.KEYCODE_BUTTON_6);
//                keyCodeMap.put(KEY_INDEX_DEPT7,KeyEvent.KEYCODE_BUTTON_7);
//                keyCodeMap.put(KEY_INDEX_DEPT8,KeyEvent.KEYCODE_BUTTON_8);
//                keyCodeMap.put(KEY_INDEX_DEPT9,KeyEvent.KEYCODE_BUTTON_9);
//                keyCodeMap.put(KEY_INDEX_DEPT10,KeyEvent.KEYCODE_BUTTON_10);
//                keyCodeMap.put(KEY_INDEX_DEPT11,KeyEvent.KEYCODE_BUTTON_11);
//                keyCodeMap.put(KEY_INDEX_DEPT12,KeyEvent.KEYCODE_BUTTON_12);
//                keyCodeMap.put(KEY_INDEX_DEPT13,KeyEvent.KEYCODE_BUTTON_13);
//
//                keyCodeMap.put(KEY_INDEX_EXACT_AMOUNT,KeyEvent.KEYCODE_BUTTON_14);
//
//               //paper feed
//                keyCodeMap.put(KEY_INDEX_FEED,KeyEvent.KEYCODE_BUTTON_15);
//
//
//                //shifting pages
//                //f1=invoice f2=payment f3=shift f4=void
//                keyCodeMap.put(KEY_INDEX_INVOICE,KeyEvent.KEYCODE_F1);
//                keyCodeMap.put(KEY_INDEX_PAYMENT,KeyEvent.KEYCODE_F2);
//                keyCodeMap.put(KEY_INDEX_SHIFT,KeyEvent.KEYCODE_F3);
//                keyCodeMap.put(KEY_INDEX_VOID,KeyEvent.KEYCODE_F4);
//
//
//                //mode of payment (cashier_payment module)
//
//                keyCodeMap.put(KEY_INDEX_CASH_PAYMENT,KeyEvent.KEYCODE_F5);
//                keyCodeMap.put(KEY_INDEX_CREDIT_PAYMENT,KeyEvent.KEYCODE_F6);
//                keyCodeMap.put(KEY_INDEX_DEBIT_PAYMENT,KeyEvent.KEYCODE_F7);
//                keyCodeMap.put(KEY_INDEX_OTHER_PAYMENT,KeyEvent.KEYCODE_F8);
//                keyCodeMap.put(KEY_INDEX_DISCOUNT,KeyEvent.KEYCODE_F10);
//                keyCodeMap.put(KEY_INDEX_DIPLOMAT,KeyEvent.KEYCODE_F9);
//
//                //map for enter
//                keyCodeMap.put(KEY_INDEX_SUBTOTAL1,KeyEvent.KEYCODE_F11);
//                keyCodeMap.put(KEY_INDEX_SUBTOTAL2,KeyEvent.KEYCODE_F11);
//                keyCodeMap.put(KEY_INDEX_SUBTOTAL3,KeyEvent.KEYCODE_F11);
//
//                keyCodeMap.put(KEY_INDEX_TOTAL1,KeyEvent.KEYCODE_ENTER);
//                keyCodeMap.put(KEY_INDEX_TOTAL2,KeyEvent.KEYCODE_ENTER);
//
//                //map for plu
//
//                keyCodeMap.put(kEY_INDEX_PLU,KeyEvent.KEYCODE_BUTTON_16);
//
//                keyCodeMap.put(KEY_INDEX_XTIME,KeyEvent.KEYCODE_INFO);












        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getVKCode(int nKeyIndex) {
        int nVKCode = ERR_MAP_NOT_CONTAIN;
        if (keyCodeMap.containsKey(nKeyIndex)) {
            Log.d("TAG", "getVKCode1: " + nKeyIndex);
            nVKCode = keyCodeMap.get(nKeyIndex);
        }
        else{
            Log.e("nKeyIndexs",String.valueOf(nKeyIndex));
           // Log.d("TAG", "getVKCode2: " + nKeyIndex);
            nVKCode=nKeyIndex;
           // getDefaultKeyName(nKeyIndex);
        }
        return nVKCode;
    }


    public final static int KEY_INDEX_SUBTOTAL1 = 264;
    public final static int KEY_INDEX_SUBTOTAL2 = 320;
    public final static int KEY_INDEX_SUBTOTAL3 = 328;
    ////////////////////
    public final static int KEY_INDEX_TOTAL1 = 544;
    public final static int KEY_INDEX_TOTAL2 = 288;
    //////////////
    public final static int KEY_INDEX_INVOICE = 1;
    public final static int KEY_INDEX_PAYMENT = 2;
    public final static int KEY_INDEX_SHIFT = 4;
    public final static int KEY_INDEX_VOID= 8;

    public final static int KEY_INDEX_CASH_PAYMENT = 16;
    public final static int KEY_INDEX_DEBIT_PAYMENT = 258;
    public final static int KEY_INDEX_OTHER_PAYMENT = 64;
    public final static int KEY_INDEX_DISCOUNT = 272;
    public final static int KEY_INDEX_CREDIT_PAYMENT = 128;
    public final static int KEY_INDEX_DIPLOMAT = 32;



    public final static int KEY_INDEX_PAGEUP = 257;
    public final static int KEY_INDEX_PAGEDN = 260;

    ///////////////
    public final static int KEY_INDEX_CLEAR = 1600;
    public final static int kEY_INDEX_PLU = 1552;
    public final static int KEY_INDEX_RA = 1544;
    public final static int KEY_INDEX_PO = 1568;
    public final static int KEY_INDEX_FEED = 1538;
    public final static int KEY_INDEX_EC = 1537;
    public final static int KEY_INDEX_REF = 1540;
    public final static int KEY_INDEX_XTIME = 1344;
    //////////////
    public final static int KEY_INDEX_DEPT1 = 1088;
    public final static int KEY_INDEX_DEPT2 = 776;
    public final static int KEY_INDEX_DEPT3 = 772;
    public final static int KEY_INDEX_DEPT4 = 770;
    /////////////////
    public final static int KEY_INDEX_DEPT5 = 800;
    public final static int KEY_INDEX_DEPT6 = 784;
    public final static int KEY_INDEX_DEPT7 = 769;
    public final static int KEY_INDEX_DEPT8 = 513;
    /////////////////
    public final static int KEY_INDEX_DEPT9 = 832;
    public final static int KEY_INDEX_DEPT10 = 528;
    public final static int KEY_INDEX_DEPT11 = 516;
    public final static int KEY_INDEX_DEPT12 = 514;
    public final static int KEY_INDEX_DEPT13 = 576;
    public final static int KEY_INDEX_EXACT_AMOUNT = 520;
    ///////////////////////
    public final static int KEY_INDEX_NUMPAD0 = 1312;
    public final static int KEY_INDEX_NUMPAD1 = 1288;
    public final static int KEY_INDEX_NUMPAD2 = 1284;
    public final static int KEY_INDEX_NUMPAD3 = 1040;
    public final static int KEY_INDEX_NUMPAD4 = 1282;
    public final static int KEY_INDEX_NUMPAD5 = 1028;
    public final static int KEY_INDEX_NUMPAD6 = 1032;
    public final static int KEY_INDEX_NUMPAD7 = 1281;
    public final static int KEY_INDEX_NUMPAD8 = 1026;
    public final static int KEY_INDEX_NUMPAD9 = 1025;
    public final static int KEY_INDEX_NUMPADDEC = 1056;
    public final static int KEY_INDEX_NUMPAD00 = 1296;


    public static String getDefaultKeyName(int nScanCode){ //1,number  //2 letter

        String keyName = "";
        int type = 1;
        if (type ==1 ) {
            switch (nScanCode) {
                case KEY_INDEX_SUBTOTAL1:
                case KEY_INDEX_SUBTOTAL2:

                case KEY_INDEX_SUBTOTAL3:
                    keyName = "Sub Total";
                    break;
                case KEY_INDEX_TOTAL1:
                case KEY_INDEX_TOTAL2:
                    keyName = "Total";
                    break;
                case KEY_INDEX_INVOICE:
                    keyName = "Invoice";
                    break;
                case KEY_INDEX_PAYMENT:
                    keyName = "Payment";
                    break;
                case KEY_INDEX_SHIFT:
                    keyName = "Shift";
                    break;
                case KEY_INDEX_VOID:
                    keyName = "Void";
                    break;
                case KEY_INDEX_CASH_PAYMENT:
                    keyName = "Cash Payment";
                    break;
                case KEY_INDEX_CREDIT_PAYMENT:
                    keyName = "Credit Payment";
                    break;
                case KEY_INDEX_DEBIT_PAYMENT:
                    keyName = "Debit Payment";
                    break;
                case KEY_INDEX_OTHER_PAYMENT:
                    keyName = "Other Payment";
                    break;
                case KEY_INDEX_DISCOUNT:
                    keyName = "Discount";
                    break;
                case KEY_INDEX_PAGEUP:
                    keyName = "Page UP";
                    break;
                case KEY_INDEX_PAGEDN:
                    keyName = "Page DN";
                    break;
                case KEY_INDEX_DIPLOMAT:
                    keyName = "Diplomat";
                    break;
                case KEY_INDEX_CLEAR:
                    keyName = "Clear";
                    break;
                case kEY_INDEX_PLU:
                    keyName = "PLU";
                    break;
                case KEY_INDEX_RA:
                    keyName = "RA"; // receive amount
                    break;
                case KEY_INDEX_PO:
                    keyName = "PO"; // paid out
                    break;
                case KEY_INDEX_FEED:
                    keyName = "Exit";

                    break;
                case KEY_INDEX_EC:
                    keyName = "EC";

                    break;
                case KEY_INDEX_REF:
                  // keyName = "REF/-";
                    keyName = "Btn";
                    break;
                case KEY_INDEX_XTIME:
                    keyName = "X/TIME";
                    break;
                case KEY_INDEX_DEPT1:
                    keyName = "D01";
                    break;
                case KEY_INDEX_DEPT2:
                    keyName = "D02";
                    break;
                case KEY_INDEX_DEPT3:
                    keyName = "D03";
                    break;
                case KEY_INDEX_DEPT4:
                    keyName = "D04";
                    break;
                case KEY_INDEX_DEPT5:
                    keyName = "D05";
                    break;
                case KEY_INDEX_DEPT6:
                    keyName = "D06";
                    break;
                case KEY_INDEX_DEPT7:
                    keyName = "D07";
                    break;
                case KEY_INDEX_DEPT8:
                    keyName = "D08";
                    break;
                case KEY_INDEX_DEPT9:
                    keyName = "D09";
                    break;
                case KEY_INDEX_DEPT10:
                    keyName = "D10";
                    break;
                case KEY_INDEX_DEPT11:
                    keyName = "D11";
                    break;
                case KEY_INDEX_DEPT12:
                    keyName = "D12";
                    break;
                case KEY_INDEX_DEPT13:
                    keyName = "D13";
                    break;
                case KEY_INDEX_EXACT_AMOUNT:
                    keyName = "D14";
                    break;
                case KEY_INDEX_NUMPAD0:
                    keyName = "0";
                    break;
                case KEY_INDEX_NUMPAD1:
                    keyName = "1";
                    break;
                case KEY_INDEX_NUMPAD2:
                    keyName = "2";
                    break;
                case KEY_INDEX_NUMPAD3:
                    keyName = "3";
                    break;
                case KEY_INDEX_NUMPAD4:
                    keyName = "4";
                    break;
                case KEY_INDEX_NUMPAD5:
                    keyName = "5";
                    break;
                case KEY_INDEX_NUMPAD6:
                    keyName = "6";
                    break;
                case KEY_INDEX_NUMPAD7:
                    keyName = "7";
                    break;
                case KEY_INDEX_NUMPAD8:
                    keyName = "8";
                    break;
                case KEY_INDEX_NUMPAD9:
                    keyName = "9";
                    break;
                case KEY_INDEX_NUMPADDEC:
                    keyName = ".";
                    break;
                case KEY_INDEX_NUMPAD00:
                    keyName = "00";
                    break;
                default:
                    break;
            }
        }
        else{
            switch (nScanCode) {

                case KEY_INDEX_NUMPAD0:
                    keyName = "0";
                    break;
                case KEY_INDEX_NUMPAD1:

                    keyName = "1";
                    break;
                case KEY_INDEX_NUMPAD2:
                    keyName = "2";
                    break;
                case KEY_INDEX_NUMPAD3:
                    keyName = "3";
                    break;
                case KEY_INDEX_NUMPAD4:
                    keyName = "4";
                    break;
                case KEY_INDEX_NUMPAD5:
                    keyName = "5";
                    break;
                case KEY_INDEX_NUMPAD6:
                    keyName = "6";
                    break;
                case KEY_INDEX_NUMPAD7:
                    keyName = "7";
                    break;
                case KEY_INDEX_NUMPAD8:
                    keyName = "8";
                    break;
                case KEY_INDEX_NUMPAD9:
                    keyName = "9";
                    break;
                case KEY_INDEX_NUMPADDEC:
                    keyName = ".";
                    break;
                case KEY_INDEX_NUMPAD00:
                    keyName = "00";
                    break;
                default:
                    break;
            }
        }
        return keyName;
    }





    public static void KeyBoardExecuteRoutine() {
        StringBuilder sbDemo = new StringBuilder(512);
        sbDemo.append(" 1.GetScanCode-->KeyIndex=Scan Code");
        sbDemo.append(" 2.RouteKeyIndex-->KeyIndex 自定义转3否则转8");
        sbDemo.append(" 3.RouteKeyDefine->KeyIndex->KeyDefine");
        sbDemo.append(" 4.KeyDefine type Dept     Key(Do Dept Sale)->DeptNo");
        sbDemo.append(" 5.KeyDefine type PLU      Key(Do PLU  Sale)->PLU No");
        sbDemo.append(" 6.KeyDefine type Function Key(Do Key Define Function)->Function No");
        sbDemo.append(" 7.KeyDefine type Number   Key(RouteNumberKeyDefine)-->8");
        sbDemo.append(" 8.Simulate Keyboard(KeyIndex->System VKCode)");
    }
}
