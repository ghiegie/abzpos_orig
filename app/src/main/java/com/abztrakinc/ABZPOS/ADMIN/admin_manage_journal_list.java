package com.abztrakinc.ABZPOS.ADMIN;

public class admin_manage_journal_list {

    public String getDateList() {
        return dateList;
    }

    public void setDateList(String dateList) {
        this.dateList = dateList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    private String dateList,fileName,fileSize;


    public admin_manage_journal_list(String dateList,String fileName,String fileSize){
        this.dateList=dateList;
        this.fileName=fileName;
        this.fileSize=fileSize;
    }


}
