package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.util.List;


public class admin_send_email_async extends AsyncTask<Void, Void, Boolean> {
     String toEmail;
   String subject;
   String body;
    List<Uri> attachment;
    Context cont;


    public void  SendEmailTask(Context context, String toEmail, String subject, String body, List<Uri> attachment) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.attachment = attachment;
        this.cont=context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Call your email sending method here
            admin_send_email_report email = new admin_send_email_report();
            email.admin_send_email_report(cont,toEmail, subject, body, attachment);
            return true; // Return true if email sending succeeds
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if email sending fails
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (success) {
            // Email sent successfully
        } else {
            // Failed to send email
        }
    }
}
