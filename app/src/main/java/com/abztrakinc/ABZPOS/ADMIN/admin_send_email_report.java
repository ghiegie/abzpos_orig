package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class admin_send_email_report {

    private static final String TAG = "EmailSender";
    public void admin_send_email_report(Context context, String toEmail, String subject, String body, List<Uri> attachments){
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication("abzpos.abztrak@gmail.com", "uuqkmjphztugssrk"); // Set your email and password here
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bobwendelarcon@gmail.com")); // Set sender email

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachments != null) {
                for (Uri attachment : attachments) {
                    messageBodyPart = new MimeBodyPart();
                    InputStream inputStream = context.getContentResolver().openInputStream(attachment);
                    byte[] data = readBytes(inputStream);
                    DataSource source = new InputStreamDataSource(attachment.getLastPathSegment(), "application/octet-stream", data);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(attachment.getLastPathSegment());
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            message.setContent(multipart);

            Transport.send(message);

            Log.i(TAG, "Email sent successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error sending email: " + e.getMessage(), e);
        }
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }




}
