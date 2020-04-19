package edu.upenn.cis350.donormatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.net.URL;

public class AvailabilityActivity extends AppCompatActivity {

    TextView mTv;
    Button mBtn;
    Calendar c;
    DatePickerDialog dpd;
    private DrawerLayout d1;
    private ActionBarDrawerToggle t1;
    private NavigationView nv;
    private TextView txtdate;
    Spinner start_spin;
    Spinner end_spin;
    TextView start_spin_text;
    TextView end_spin_text;
    Button confirmbut;
    Button selectBut;
    TextView availability_text;


    int sel_year;
    int sel_month;
    int sel_day;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        mBtn = (Button) findViewById(R.id.datePick);
        txtdate = (TextView) findViewById(R.id.textViewDate);
        start_spin = (Spinner) findViewById(R.id.spinner_start);
        start_spin_text = (TextView) findViewById(R.id.start_spinner_text);
        end_spin = (Spinner) findViewById(R.id.spinner_end);
        end_spin_text = (TextView) findViewById(R.id.end_spinner_text);
        confirmbut = (Button) findViewById(R.id.confirm_button);
        selectBut = (Button) findViewById(R.id.dateCheck);
        availability_text = (TextView) findViewById(R.id.textView);


        // creates the nav drawer
        createSideBar();

        mBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(AvailabilityActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        createSpinners();
                        String date = (month+1) + "/" + dayOfMonth + "/" + year;
                        txtdate.setText("Enter what hours you are available for: " + date);
                        mBtn.setVisibility(View.INVISIBLE);
                        start_spin.setVisibility(View.VISIBLE);
                        end_spin.setVisibility(View.VISIBLE);
                        start_spin_text.setVisibility(View.VISIBLE);
                        end_spin_text.setVisibility(View.VISIBLE);
                        confirmbut.setVisibility(View.VISIBLE);
                        selectBut.setVisibility(View.INVISIBLE);
                        availability_text.setVisibility(View.INVISIBLE);

                        sel_year = year;
                        sel_month = month;
                        sel_day = dayOfMonth;


                    }
                }, year, month, day);
                dpd.show();
            }

        });

    // end of oncreate method
    }

    public void createSideBar(){
        // all code below within onCreate for navigation menu
        d1 = (DrawerLayout)findViewById(R.id.activity_availability);
        t1 = new ActionBarDrawerToggle(this, d1,R.string.Open, R.string.Close);

        d1.addDrawerListener(t1);
        t1.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i = null;
                switch(item.getItemId())
                {
                    case R.id.home:
                        i = new Intent(nv.getContext(), MapsActivity.class);
                        startActivityForResult(i, MapsActivity.RequestCode.MAIN_ACTIVITY_NUM);
                        break;
                    case R.id.account:
                        Toast.makeText(AvailabilityActivity.this, "Not implemented yet",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(AvailabilityActivity.this, "Not implemented yet",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.other:
                        Toast.makeText(AvailabilityActivity.this, "peek a boo",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.user_availability:
                        i = new Intent(nv.getContext(), AvailabilityActivity.class);
                        startActivityForResult(i, MapsActivity.RequestCode.USER_AVAILABILITY_ACTIVITY_NUM);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t1.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void createSpinners() {
        // create the start spinner
        Spinner spinner_start = (Spinner) findViewById(R.id.spinner_start);
        spinner_start.setOnItemSelectedListener(new SpinnerActivity());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_start.setAdapter(adapter);

        // create the end spinner
        Spinner spinner_end = (Spinner) findViewById(R.id.spinner_end);
        spinner_end.setOnItemSelectedListener(new SpinnerActivity());
        spinner_end.setAdapter(adapter);

    }

    public void onConfirmDateClick(View v){

        String start_time =  start_spin.getSelectedItem().toString();
        int time_start = Integer.parseInt(start_time);
        time_start = time_start + 4;
        start_time = Integer.toString(time_start);
        String end_time =  end_spin.getSelectedItem().toString();
        int time_end = Integer.parseInt(end_time);
        time_end = time_end + 4;
        end_time = Integer.toString(time_end);
        String test_string = sel_month +"/" + sel_day + "/" + sel_year + "/" + start_time + "/" + end_time;

        String start_date  = "";
        String end_date = "";
        sel_month = sel_month + 1;
        if (sel_month < 10 && sel_day < 10){
            start_date = sel_year + "-0" + sel_month + "-0" + sel_day + "T" + start_time + ":00:00Z";
            end_date = sel_year + "-0" + sel_month + "-0" + sel_day + "T" + end_time + ":00:00Z";
        } else if (sel_month > 10 && sel_day < 10){
            start_date = sel_year + "-" + sel_month + "-0" + sel_day + "T" + start_time + ":00:00Z";
            end_date = sel_year + "-" + sel_month + "-0" + sel_day + "T" + end_time + ":00:00Z";
        } else if (sel_month < 10 && sel_day > 10){
            start_date = sel_year + "-0" + sel_month + "-" + sel_day + "T" + start_time + ":00:00Z";
            end_date = sel_year + "-0" + sel_month + "-" + sel_day + "T" + end_time + ":00:00Z";
        } else {
            start_date = sel_year + "-" + sel_month + "-" + sel_day + "T" + start_time + ":00:00Z";
            end_date = sel_year + "-" + sel_month + "-" + sel_day + "T" + end_time + ":00:00Z";
        }


        try {
            String address;
            String id = "Phillip";
            String request_method = "POST";
            URL url = new URL("http://10.0.2.2:3000/addDate?name="+id+"&start_dates="+start_date+"&end_dates="+end_date);
            AccessWebTask task = new AccessWebTask(request_method);
            task.execute(url);
            address = task.get();
            System.out.println("date should be printed below");
            System.out.println(address);
        } catch (Exception e){
            e.toString();
        }

        Toast.makeText(AvailabilityActivity.this, "Date has been confirmed",Toast.LENGTH_LONG).show();


    }


    public void onCheckDateClick(View v){

        Intent i = null;
        i = new Intent(nv.getContext(), DateActivity.class);
        startActivityForResult(i, MapsActivity.RequestCode.DATE_ACTIVITY_NUM);

    }


}

