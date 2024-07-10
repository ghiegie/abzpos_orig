package com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview;

public class invoice_fragment_button_model {


    public int getButtonID() {
        return ButtonID;
    }

    public void setButtonID(int buttonID) {
        ButtonID = buttonID;
    }

    public String getButtonName() {
        return ButtonName;
    }

    public void setButtonName(String buttonName) {
        ButtonName = buttonName;
    }

    int ButtonID;
    String ButtonName;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected(){
        return  isSelected;
    }

    public Boolean isSelected=true;


    public invoice_fragment_button_model(int buttonID, String buttonName){
        this.ButtonID = buttonID;
        this.ButtonName=buttonName;


    }
}
