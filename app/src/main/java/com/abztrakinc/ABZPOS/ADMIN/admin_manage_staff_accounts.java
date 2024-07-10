package com.abztrakinc.ABZPOS.ADMIN;

public class admin_manage_staff_accounts {



   private int ID;
   private String AccountNumber;

   public int getID() {
      return ID;
   }

   public void setID(int ID) {
      this.ID = ID;
   }

   public String getAccountNumber() {
      return AccountNumber;
   }

   public void setAccountNumber(String accountNumber) {
      AccountNumber = accountNumber;
   }

   public String getAccountUserName() {
      return AccountUserName;
   }

   public void setAccountUserName(String accountUserName) {
      AccountUserName = accountUserName;
   }

   public String getAccountPassword() {
      return AccountPassword;
   }

   public void setAccountPassword(String accountPassword) {
      AccountPassword = accountPassword;
   }

   public int getAccountType() {
      return AccountType;
   }

   public void setAccountType(int accountType) {
      AccountType = accountType;
   }

   private String AccountUserName;
   private String AccountPassword;
   private int AccountType;


   public admin_manage_staff_accounts(int iD, String accountNumber, String accountuserName, String accountPassword,int accountType){

      this.ID=iD;
      this.AccountNumber=accountNumber;
      this.AccountUserName=accountuserName;
      this.AccountPassword=accountPassword;
      this.AccountType=accountType;




   }



}
