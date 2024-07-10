package com.abztrakinc.ABZPOS.ADMIN;

import com.rt.printerlibrary.enumerate.ConnectStateEnum;
import com.rt.printerlibrary.printer.RTPrinter;

public class CustomRTPrinter extends RTPrinter {
    @Override
    public void disConnect() {

    }

    @Override
    public void connect(Object o) throws Exception {

    }

    @Override
    public ConnectStateEnum getConnectState() {
        return null;
    }

    @Override
    public void writeMsg(byte[] bytes) {

    }

    @Override
    public void writeMsgAsync(byte[] bytes) {

    }

    @Override
    public byte[] readMsg() {
        return new byte[0];
    }
}
