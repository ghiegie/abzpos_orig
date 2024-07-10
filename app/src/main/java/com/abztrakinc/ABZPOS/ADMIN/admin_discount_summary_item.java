package com.abztrakinc.ABZPOS.ADMIN;

public class admin_discount_summary_item {


    int No;

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIDNo() {
        return IDNo;
    }

    public void setIDNo(String IDNo) {
        this.IDNo = IDNo;
    }

    public String getSINo() {
        return SINo;
    }

    public void setSINo(String SINo) {
        this.SINo = SINo;
    }

    public String getSalesWithVAT() {
        return SalesWithVAT;
    }

    public void setSalesWithVAT(String salesWithVAT) {
        SalesWithVAT = salesWithVAT;
    }

    public String getVATAmount() {
        return VATAmount;
    }

    public void setVATAmount(String VATAmount) {
        this.VATAmount = VATAmount;
    }

    public String getVatExempt() {
        return VatExempt;
    }

    public void setVatExempt(String vatExempt) {
        VatExempt = vatExempt;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getNetSales() {
        return NetSales;
    }

    public void setNetSales(String netSales) {
        NetSales = netSales;
    }

    String Date;
    String Name;
    String IDNo;
    String SINo;
    String SalesWithVAT;
    String VATAmount;
    String VatExempt;
    String Discount;
    String NetSales;



    public admin_discount_summary_item(int ItemNo, String date, String name, String idNo,String siNO , String vatExempt, String discount){

        this.No=ItemNo;
        this.Date=date;
        this.Name=name;
        this.IDNo=idNo;
        this.SINo=siNO;
//        this.SalesWithVAT=salesWithVAT;
//        this.VATAmount=vATAmount;
        this.VatExempt=vatExempt;
        this.Discount=discount;
//        this.NetSales=netSales;




    }



}
