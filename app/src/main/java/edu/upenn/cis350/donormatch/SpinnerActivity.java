package edu.upenn.cis350.donormatch;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;

public class SpinnerActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

