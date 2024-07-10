package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class admin_manage_report_item extends admin_manage_report{


    String BeginningOR;
    String EndingOR;
    String BeginningAccumulateBalance;
    String EndingAccumulateBalance;
    String SalesIssuedManual;
    String GrossSales;
    String VatableSales;
    String VatAmount;
    String VatExempt;

    public String getZeroRated() {
        return ZeroRated;
    }

    public void setZeroRated(String zeroRated) {
        ZeroRated = zeroRated;
    }

    String ZeroRated;
    String SeniorDiscount;
    String PWDDiscount;
    String OtherDiscount;
    String ReturnsDiscount;
    String VoidDiscount;
    String TotalDeduction;


    String SeniorDiscountAdj;
    String PWDDiscountAdj;
    String OtherDiscountAdj;
    String ReturnDiscountAdj;
    String OthersAdj;
    String TotalVatAdj;
    String VatPayable;

    String NetSales;
    String SalesOverrunFlow;

    public String getBeginningOR() {
        return BeginningOR;
    }

    public void setBeginningOR(String beginningOR) {
        BeginningOR = beginningOR;
    }

    public String getEndingOR() {
        return EndingOR;
    }

    public void setEndingOR(String endingOR) {
        EndingOR = endingOR;
    }

    public String getBeginningAccumulateBalance() {
        return BeginningAccumulateBalance;
    }

    public void setBeginningAccumulateBalance(String beginningAccumulateBalance) {
        BeginningAccumulateBalance = beginningAccumulateBalance;
    }

    public String getEndingAccumulateBalance() {
        return EndingAccumulateBalance;
    }

    public void setEndingAccumulateBalance(String endingAccumulateBalance) {
        EndingAccumulateBalance = endingAccumulateBalance;
    }

    public String getSalesIssuedManual() {
        return SalesIssuedManual;
    }

    public void setSalesIssuedManual(String salesIssuedManual) {
        SalesIssuedManual = salesIssuedManual;
    }

    public String getGrossSales() {
        return GrossSales;
    }

    public void setGrossSales(String grossSales) {
        GrossSales = grossSales;
    }

    public String getVatableSales() {
        return VatableSales;
    }

    public void setVatableSales(Double vatableSales) {
        this.vatableSales = vatableSales;
    }

    public void setVatableSales(String vatableSales) {
        VatableSales = vatableSales;
    }

    public String getVatAmount() {
        return VatAmount;
    }

    public void setVatAmount(Double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Double getVatExemptSales() {
        return vatExemptSales;
    }

    public void setVatExemptSales(Double vatExemptSales) {
        this.vatExemptSales = vatExemptSales;
    }

    public Double getZeroRatedSales() {
        return zeroRatedSales;
    }

    public void setZeroRatedSales(Double zeroRatedSales) {
        this.zeroRatedSales = zeroRatedSales;
    }

    public void setVatAmount(String vatAmount) {
        VatAmount = vatAmount;
    }

    public String getVatExempt() {
        return VatExempt;
    }

    public void setVatExempt(String vatExempt) {
        VatExempt = vatExempt;
    }

//    public String getZeroRated() {
//        return ZeroRated;
//    }

//    public void setZeroRated(String zeroRated) {
//        ZeroRated = zeroRated;
//    }

    public String getSeniorDiscount() {
        return SeniorDiscount;
    }

    public void setSeniorDiscount(Double seniorDiscount) {
        this.seniorDiscount = seniorDiscount;
    }

    public Double getPwdDiscount() {
        return pwdDiscount;
    }

    public void setPwdDiscount(Double pwdDiscount) {
        this.pwdDiscount = pwdDiscount;
    }

    public void setSeniorDiscount(String seniorDiscount) {
        SeniorDiscount = seniorDiscount;
    }

    public String getPWDDiscount() {
        return PWDDiscount;
    }

    public void setPWDDiscount(String PWDDiscount) {
        this.PWDDiscount = PWDDiscount;
    }

    public String getOtherDiscount() {
        return OtherDiscount;
    }

    public void setOtherDiscount(Double otherDiscount) {
        this.otherDiscount = otherDiscount;
    }

    public Double getSeniorDiscountVat() {
        return seniorDiscountVat;
    }

    public void setSeniorDiscountVat(Double seniorDiscountVat) {
        this.seniorDiscountVat = seniorDiscountVat;
    }

    public Double getPwdDiscountVAT() {
        return pwdDiscountVAT;
    }

    public void setPwdDiscountVAT(Double pwdDiscountVAT) {
        this.pwdDiscountVAT = pwdDiscountVAT;
    }

    public void setOtherDiscount(String otherDiscount) {
        OtherDiscount = otherDiscount;
    }

    public String getReturnsDiscount() {
        return ReturnsDiscount;
    }

    public void setReturnsDiscount(String returnsDiscount) {
        ReturnsDiscount = returnsDiscount;
    }

    public String getVoidDiscount() {
        return VoidDiscount;
    }

    public void setVoidDiscount(String voidDiscount) {
        VoidDiscount = voidDiscount;
    }

    public String getTotalDeduction() {
        return TotalDeduction;
    }

    public void setTotalDeduction(String totalDeduction) {
        TotalDeduction = totalDeduction;
    }

    public String getSeniorDiscountAdj() {
        return SeniorDiscountAdj;
    }

    public void setSeniorDiscountAdj(String seniorDiscountAdj) {
        SeniorDiscountAdj = seniorDiscountAdj;
    }

    public String getPWDDiscountAdj() {
        return PWDDiscountAdj;
    }

    public void setPWDDiscountAdj(String PWDDiscountAdj) {
        this.PWDDiscountAdj = PWDDiscountAdj;
    }

    public String getOtherDiscountAdj() {
        return OtherDiscountAdj;
    }

    public void setOtherDiscountAdj(String otherDiscountAdj) {
        OtherDiscountAdj = otherDiscountAdj;
    }

    public String getReturnDiscountAdj() {
        return ReturnDiscountAdj;
    }

    public void setReturnDiscountAdj(String returnDiscountAdj) {
        ReturnDiscountAdj = returnDiscountAdj;
    }

    public String getOthersAdj() {
        return OthersAdj;
    }

    public void setOthersAdj(String othersAdj) {
        OthersAdj = othersAdj;
    }

    public String getTotalVatAdj() {
        return TotalVatAdj;
    }

    public void setTotalVatAdj(String totalVatAdj) {
        TotalVatAdj = totalVatAdj;
    }

    public String getVatPayable() {
        return VatPayable;
    }

    public void setVatPayable(String vatPayable) {
        VatPayable = vatPayable;
    }

    public String getNetSales() {
        return NetSales;
    }

    public void setNetSales(Double netSales) {
        this.netSales = netSales;
    }

    public Double getLessVat() {
        return lessVat;
    }

    public void setLessVat(Double lessVat) {
        this.lessVat = lessVat;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public void setNetSales(String netSales) {
        NetSales = netSales;
    }

    public String getSalesOverrunFlow() {
        return SalesOverrunFlow;
    }

    public void setSalesOverrunFlow(String salesOverrunFlow) {
        SalesOverrunFlow = salesOverrunFlow;
    }

    public String getTotalIncomeSales() {
        return TotalIncomeSales;
    }

    public void setTotalIncomeSales(String totalIncomeSales) {
        TotalIncomeSales = totalIncomeSales;
    }

    public String getTotalResetCounter() {
        return TotalResetCounter;
    }

    public void setTotalResetCounter(String totalResetCounter) {
        TotalResetCounter = totalResetCounter;
    }

    public String getZCounter() {
        return ZCounter;
    }

    public void setZCounter(String ZCounter) {
        this.ZCounter = ZCounter;
    }

    String TotalIncomeSales;
    String TotalResetCounter;
    String ZCounter;

    public String getOthersDiscAdj2() {
        return OthersDiscAdj2;
    }

    public void setOthersDiscAdj2(String othersDiscAdj2) {
        OthersDiscAdj2 = othersDiscAdj2;
    }

    String OthersDiscAdj2;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    String Date;

//    public admin_manage_report_item(String BegOR,String EndOr,String BegAccumBal,String EndAccumBal,String GrossSales,String VatableSales,String VatAmount,
//                                    String VatExempt,String ZeroRated,String scdDiscount,String PWDDiscount,String OtherDiscount,String SeniorDiscVat,
//                                    String PWDDiscVat,String OtherDiscVat,String NetSales,String Zcounter){

    public admin_manage_report_item(String Date,
                                    String BegOR,
                                    String EndOr,
                                    String EndAccumBal,
                                    String BegAccumBal,
                                    String SalesIssuedManual,
                                    String GrossSales,
                                    String VatableSales,
                                    String VatAmount,
                                    String VatExempt,
                                    String ZeroRatedSales,
                                    String scdDiscount,
                                    String PWDDiscount,
                                    String OtherDiscount,
                                    String ReturnDiscount,
                                    String VoidDiscount,
                                    String TotalDiscount,
                                    String SeniorDiscAdj,
                                    String PWDDiscAdj,
                                    String OtherDiscAdj,
                                    String VatOnReturnsAdj,
                                    String OthersDiscAdj2,
                                    String TotalVatAdj,
                                    String VatPayable,
                                    String NetSales,
                                    String SalesOverRunFlow,
                                    String TotalIncomeSales,
                                    String ResetCounter,
                                    String Zcounter){

//        ORTrans();
//        AccumulatedBalanceBeginning();
//        AccumulatedBalanceEnding();
//        GrossSales();VatableSales();VatAmount();VatExemptSales();ZeroRatedSales();SeniorDiscount();PWDDiscount();OtherDiscount();
//        SeniorDiscountVat();PWDDiscountVat();OtherDiscountVat();NetSales();ZCounter();



        this.Date=Date;
        this.BeginningOR=BegOR;
        this.EndingOR=EndOr;
        this.BeginningAccumulateBalance=BegAccumBal;
        this.EndingAccumulateBalance=EndAccumBal;
        this.SalesIssuedManual=SalesIssuedManual;
        this.GrossSales=GrossSales;
        this.VatableSales=VatableSales;
        this.VatAmount=VatAmount;
        this.VatExempt=VatExempt;
        this.ZeroRated=ZeroRatedSales;
        this.SeniorDiscount=scdDiscount;
        this.PWDDiscount=PWDDiscount;
        this.OtherDiscount=OtherDiscount;
        this.ReturnsDiscount=ReturnDiscount;
        this.VoidDiscount=VoidDiscount;
        this.TotalDeduction=TotalDiscount;

        this.SeniorDiscountAdj=SeniorDiscAdj;
        this.PWDDiscountAdj=PWDDiscAdj;
        this.OtherDiscountAdj=OtherDiscAdj;
        this.ReturnDiscountAdj=VatOnReturnsAdj;
        this.OthersDiscAdj2=OthersDiscAdj2;
        this.TotalVatAdj=TotalVatAdj;
        this.VatPayable=VatPayable;

        this.NetSales=NetSales;
        this.SalesOverrunFlow=SalesOverRunFlow;
        this.TotalIncomeSales=TotalIncomeSales;
        this.TotalResetCounter=ResetCounter;
        this.ZCounter=Zcounter;

    }








    Double vatExemptSales=0.00;

    Double vatAmount=0.00;
    Double vatableSales=0.00;
    Double zeroRatedSales=0.00;


    Double seniorDiscount=0.00;


    Double pwdDiscount=0.00;


    Double otherDiscount=0.00;


    Double seniorDiscountVat=0.00;


    Double pwdDiscountVAT=0.00;



    Double netSales=0.00;
    // Double grossSales=0.00;
    Double lessVat=0.00;
    Double totalDiscount=0.00;





    private void NetSales(){
        SQLiteDatabase db3 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //  Cursor AccumBegBal = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate between '" + date1Format1 + "'and'"+date2Format1+"'", null);
        Cursor GrossSalesCursor = db3.rawQuery("select sum(TotalLessVat) from FinalTransactionReport where TransDate = '" + date1Format1 +"'", null);
        if (GrossSalesCursor.getCount()!=0){
            GrossSalesCursor.moveToFirst();


            if (GrossSalesCursor.getString(0)==null){
                lessVat=0.00;

            }
            else{
                lessVat=(Double.parseDouble(GrossSalesCursor.getString(0)));
            }
            totalDiscount=Double.parseDouble(getSeniorDiscount())+Double.parseDouble(getPWDDiscount())+Double.parseDouble(getOtherDiscount());
            netSales=Double.parseDouble(getGrossSales())-lessVat-totalDiscount;


            //tv_othersOnVat.setText(String.valueOf(otherDiscountVAT));
            setNetSales(String.valueOf(netSales));
          //  tv_totalIncomeSales.setText(String.valueOf(netSales));
            // tv_totalIncomeSales.setText(String.valueOf(otherDiscountVAT));
        }
        else{
            setNetSales(String.valueOf(0.00));
        }



        db3.close();
    }










}
