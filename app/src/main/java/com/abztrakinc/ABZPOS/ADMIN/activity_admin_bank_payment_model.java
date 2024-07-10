package com.abztrakinc.ABZPOS.ADMIN;

public class activity_admin_bank_payment_model {

    int IDNo;
    String ItemName;
    String BankName;

    public int getIDNo() {
        return IDNo;
    }

    public void setIDNo(int IDNo) {
        this.IDNo = IDNo;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    String CardType;

    public activity_admin_bank_payment_model(int iDNo, String itemName, String bankName, String cardType){
        this.IDNo = iDNo;
        this.ItemName = itemName;
        this.BankName = bankName;
        this.CardType = cardType;
    }
}
