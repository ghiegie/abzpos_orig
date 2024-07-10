package com.abztrakinc.ABZPOS;

import android.os.Bundle;

import com.abztrakinc.ABZPOS.ORDERSTATION.ordering_station_main;

import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FtpConnect extends ordering_station_main {


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String user;
    private String password;
    FTPClient ftpClient = new FTPClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    public void jetzt(String filename,String Filepath,String OutputName,String ipAddress,String user,String password) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FTPClient client = new FTPClient();
                FileInputStream fis = null;

                try {

                    client.connect(ipAddress);//10.0.0.36
                    client.login(user, password);//user //12345

                    //String filename = "SAMPLETXT.txt";
                    fis = new FileInputStream(Filepath);// file location

                    client.storeFile(filename, fis);
                    // client.logout();
                    // client.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                        client.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();



    }







}
