package com.abztrakinc.ABZPOS.ORDERSTATION;
// laval.apacheftpclient;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.DatabaseHelper;
import com.abztrakinc.ABZPOS.R;


public class ordering_station extends AppCompatActivity {
    LinearLayout linearLayout;
    TableRow tr;
    int i=1;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering_station);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        //Toast.makeText(this,String.valueOf(width), Toast.LENGTH_SHORT).show();

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        int numberOfButton=7;
        int numberOfColumn=3;
        int numberOfRow=3;
        int modulo = numberOfButton % 5;


       // copyDatabase();


       // myDb2=new DatabaseHelper(this);
//        db=new DatabaseHelper(this);
//        try{
//            db.createDataBase();
//            Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
//
//        } catch (IOException exception) {
//            Toast.makeText(this, "NO V6BO FOUND", Toast.LENGTH_SHORT).show();
//        }



        for (i = 1; i <= numberOfButton; i++) {

            if (i<=5){
                linearLayout = findViewById(R.id.linearlayoutid1);
                final Button button = new Button(this);
                int buttonWidth = width / 5;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, 150);
                button.setLayoutParams(lp);
                button.setId(i);
              //  final int tableNum = i;
                button.setText("TABLE" + i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(ordering_station.this, "TABLE" + tableNum, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ordering_station.this, ordering_station_main.class);

                        intent.putExtra("tableNumber",String.valueOf(button.getId()));

                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);

            }
            else if (i>5 & i<=10){
                linearLayout = findViewById(R.id.linearlayoutid2);
                final Button button = new Button(this);
                int buttonWidth = width / 5;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, 150);
                button.setLayoutParams(lp);
                button.setId(i);
                final int tableNum = i;
                button.setText("TABLE" + i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ordering_station.this,ordering_station_main.class);
                        intent.putExtra("tableNumber",String.valueOf(button.getId()));
                        startActivity(intent);




                    }
                });
                linearLayout.addView(button);

            }
            else if (i>10 & i<=15){
                linearLayout = findViewById(R.id.linearlayoutid3);
                final Button button = new Button(this);
                int buttonWidth = width / 5;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, 150);
                button.setLayoutParams(lp);
                button.setId(i);
                final int tableNum = i;
                button.setText("TABLE" + i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ordering_station.this,ordering_station_main.class);

                        intent.putExtra("tableNumber",String.valueOf(button.getId()));

                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);
            }
            else if (i>15 & i<=20){
                linearLayout = findViewById(R.id.linearlayoutid4);
                final Button button = new Button(this);
                int buttonWidth = width / 5;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, 150);
                button.setLayoutParams(lp);
                button.setId(i);
                final int tableNum = i;
                button.setText("TABLE" + i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ordering_station.this,ordering_station_main.class);

                        intent.putExtra("tableNumber",String.valueOf(button.getId()));

                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);
            }
            else if (i>20 & i<=25){
                linearLayout = findViewById(R.id.linearlayoutid5);
                final Button button = new Button(this);
                int buttonWidth = width / 5;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, 150);
                button.setLayoutParams(lp);
                button.setId(i);
                final int tableNum = i;
                button.setText("TABLE" + i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ordering_station.this,ordering_station_main.class);

                        intent.putExtra("tableNumber",String.valueOf(button.getId()));

                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);
            }
            else if (i>25 & i<=30){
                linearLayout = findViewById(R.id.linearlayoutid6);
                final Button button = new Button(this);
                int buttonWidth = width / 5;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(buttonWidth, 150);
                button.setLayoutParams(lp);
                button.setId(i);
                final int tableNum = i;
                button.setText("TABLE" + i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ordering_station.this,ordering_station_main.class);

                        intent.putExtra("tableNumber",String.valueOf(button.getId()));

                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);
            }


        } // create TABLE

    }



    public void copyDatabase(){
        db = new DatabaseHelper(this);
        try{
            try{
                db.copyDataBase();
                Toast.makeText(this, "DATABASE UPLOADED", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(this, "NO DATABASE FOUND", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "DATABASE UPLOADED ERROR", Toast.LENGTH_SHORT).show();
        }
    }
}
