package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private int selectedItemPosition = -1;
    private Spinner spinner;

    public CustomSpinnerAdapter(Context context, int resource, List<String> items, Spinner spinner) {
        super(context, resource, items);
        this.spinner = spinner;
        setupSpinnerListener();
    }

    private void setupSpinnerListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setSelectedItemPosition(position);
                spinner.post(new Runnable() {
                    @Override
                    public void run() {
                        // Dismiss the dropdown after a short delay
                        //spinner.();
                       // spinner.clearFocus();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position == selectedItemPosition) {
            // Customize the appearance of the selected item (e.g., change background color to blue)
           // view.setBackgroundColor(Color.BLUE);
        } else {
            // Reset appearance for other items
          //  view.setBackgroundColor(Color.TRANSPARENT); // Set the background color to transparent
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // This method is called to create the dropdown view
        View dropDownView = super.getDropDownView(position, convertView, parent);
        if (position == selectedItemPosition) {
            // Customize the appearance of the selected item in the dropdown (e.g., change background color to blue)
            dropDownView.setBackgroundColor(Color.BLUE);
        } else {
            // Reset appearance for other items in the dropdown
            dropDownView.setBackgroundColor(Color.TRANSPARENT); // Set the background color to transparent
        }
        return dropDownView;
    }
}