package com.abztrakinc.ABZPOS.ADMIN;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class sftpManager {





        public void uploadFile(String localFilePath, String remoteFilePath, String host, int port, String username, String password) {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();
                channelSftp.put(localFilePath, remoteFilePath);
                channelSftp.disconnect();
                session.disconnect();

                System.out.println("File uploaded successfully!");
            } catch (JSchException | SftpException e) {
                e.printStackTrace();
            }
        }

        public void downloadFile(String remoteFilePath, String localFilePath, String host, int port, String username, String password) {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();
                channelSftp.get(remoteFilePath, localFilePath);
                channelSftp.disconnect();
                session.disconnect();

                System.out.println("File downloaded successfully!");
            } catch (JSchException | SftpException e) {
                e.printStackTrace();
            }
        }




}
