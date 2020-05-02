package edu.upenn.cis350.donormatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout d1;
    private ActionBarDrawerToggle t1;
    private NavigationView nv;
    private TextView txtdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        createSideBar();


        String temp_address = "";

        List<String> list_name = new ArrayList<String>();
        List<String> list_address = new ArrayList<String>();

        List<Integer> address_index = new ArrayList<Integer>();
        List<Integer> zip_index = new ArrayList<Integer>();
        List<Integer> name_index = new ArrayList<Integer>();
        List<Integer> cap_index = new ArrayList<Integer>();
        // code to see if I can make a marker on the map using Mongo
        try {
            System.out.println("Does it get to here");
            String userzip = "19104";
            URL url = new URL("http://10.0.2.2:3000/findDrives?zip="+userzip);
            AccessWebTask task = new AccessWebTask("GET");
            task.execute(url);
            temp_address = task.get();


        } catch (Exception e){
            e.toString();
        }

        // extracts name and address from json string obejct
        for (int index1 = temp_address.indexOf("name"); index1 >= 0; index1 = temp_address.indexOf("name", index1 + 1)){
            name_index.add(index1);
        }

        for (int index2 = temp_address.indexOf("capacity"); index2 >= 0; index2 = temp_address.indexOf("capacity", index2 + 1)){
            cap_index.add(index2);
        }

        for (int index3 = temp_address.indexOf("address"); index3 >= 0; index3 = temp_address.indexOf("address", index3 + 1)){
            address_index.add(index3);
        }

        for (int index4 = temp_address.indexOf("zip"); index4 >= 0; index4 = temp_address.indexOf("zip", index4 + 1)){
            zip_index.add(index4);
        }


        Map<String,String> name_addr = new HashMap<String,String>();

        for (int a = 0; a < zip_index.size(); a++){
            int indexname = name_index.get(a);
            int indexcap = cap_index.get(a);
            int indexaddr = address_index.get(a);
            int indexzip = zip_index.get(a);
            String real_name = temp_address.substring((indexname + 7), (indexcap - 3));
            String real_address = temp_address.substring((indexaddr + 10), (indexzip - 3));

            list_name.add(real_name);
            list_address.add(real_address);
            name_addr.put(real_name,real_address);
        }

        List<Integer> left_index = new ArrayList<Integer>();
        List<Integer> right_index = new ArrayList<Integer>();

        for (int index5 = temp_address.indexOf("["); index5 >= 0; index5 = temp_address.indexOf("[", index5 + 1)){
            left_index.add(index5);
        }

        for (int index6 = temp_address.indexOf("]"); index6 >= 0; index6 = temp_address.indexOf("]", index6 + 1)){
            right_index.add(index6);
        }

        Map<String, List<Date>> drive_start = new LinkedHashMap<String, List<Date>>();
        Map<String, List<Date>> drive_end = new LinkedHashMap<String, List<Date>>();


        // transfrom dates into date objects
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss.SSS");
        int place = 0;
        for (int loc = 1; loc < left_index.size(); loc++){
            int lbracket = left_index.get(loc);
            int rbracket = right_index.get(loc-1);


            if (loc % 2 == 1 && loc != 1){
                place++;
            }


            List<Date> start_dates = new ArrayList<Date>();
            List<Date> end_dates = new ArrayList<Date>();
            if ((loc % 2 == 1)){

                String str_start_dates1 = temp_address.substring((lbracket + 1), rbracket - 1);
                String str_start_dates = str_start_dates1.replace("\"", "");
                String[] str_start_array = str_start_dates.split(",");

                for(int count = 0; count < str_start_array.length; count++){

                    try {
                      //  System.out.println("--------------------------------------------------------------------");
                       // System.out.println("start date below");
                        str_start_array[count] = str_start_array[count].replace("T","/");
                        str_start_array[count] = str_start_array[count].replace("Z","");


                      //  System.out.println(str_start_array[count]);
                        Date inputstartdate = formatter.parse(str_start_array[count]);
                        start_dates.add(inputstartdate);

                    } catch (ParseException e) {
                        System.out.println("hits the parse exception for date");
                        e.printStackTrace();
                    }

                }

                System.out.println("THE START DATES LIST IS PRINTED BELOW FOR EACH BLOOD DRIVE");
                System.out.println(start_dates);

                drive_start.put(list_name.get(place), start_dates);
            } else {

                String str_end_dates1 = temp_address.substring((lbracket + 1), rbracket - 1);
                String str_end_dates = str_end_dates1.replace("\"", "");
                String[] str_end_array = str_end_dates.split(",");

                for(int count2 = 0; count2 < str_end_array.length; count2++){

                    try {
                    //    System.out.println("--------------------------------------------------------------------");
                       // System.out.println("end date below");
                        str_end_array[count2] = str_end_array[count2].replace("T","/");
                        str_end_array[count2] = str_end_array[count2].replace("Z","");
                     //   System.out.println(str_end_array[count2]);
                        Date inputenddate = formatter.parse(str_end_array[count2]);
                        end_dates.add(inputenddate);

                    } catch (ParseException e) {
                        System.out.println("hits the parse exception for date");
                        e.printStackTrace();
                    }

                }
                System.out.println("THE END DATES LIST IS PRINTED BELOW FOR EACH BLOOD DRIVE");
                System.out.println(end_dates);

                drive_end.put(list_name.get(place), end_dates);
            }

        }

        // NOW WE ARE TRYING TO GET THE PERSONS AVAILABILITY
        String person = "";
        try {
            System.out.println("Does it get to here");
            String name = "Phillip";
            URL url = new URL("http://10.0.2.2:3000/getPerson?name="+name);
            AccessWebTask task = new AccessWebTask("GET");
            task.execute(url);
            person = task.get();

        } catch (Exception e){
            e.toString();
        }

        List<Date> person_start = new ArrayList<Date>();
        List<Date> person_end = new ArrayList<Date>();

        int s_date_index = person.indexOf("start_dates");
        int s1_brack_index = person.indexOf('[', s_date_index);
        int s2_brack_index = person.indexOf(']', s1_brack_index);
        String str_start_dates1 = person.substring((s1_brack_index + 1), s2_brack_index);
        String str_start_dates = str_start_dates1.replace("\"", "");
        System.out.println(str_start_dates);
        String[] str_start_array = str_start_dates.split(",");


        for(int count = 0; count < str_start_array.length; count++){

            try {

                str_start_array[count] = str_start_array[count].replace("T","/");
                str_start_array[count] = str_start_array[count].replace("Z","");

                Date inputstartdate = formatter.parse(str_start_array[count]);
                person_start.add(inputstartdate);

            } catch (ParseException e) {
                System.out.println("hits the parse exception for date");
                e.printStackTrace();
            }

        }


        int e_date_index = person.indexOf("end_dates");
        int e1_brack_index = person.indexOf('[', e_date_index);
        int e2_brack_index = person.indexOf(']', e1_brack_index);
        String str_end_dates1 = person.substring((e1_brack_index + 1), e2_brack_index);
        String str_end_dates = str_end_dates1.replace("\"", "");
        System.out.println(str_end_dates);
        String[] str_end_array = str_end_dates.split(",");

        for(int count2 = 0; count2 < str_end_array.length; count2++){

            try {
                str_end_array[count2] = str_end_array[count2].replace("T","/");
                str_end_array[count2] = str_end_array[count2].replace("Z","");
                Date inputenddate = formatter.parse(str_end_array[count2]);
                person_end.add(inputenddate);

            } catch (ParseException e) {
                System.out.println("hits the parse exception for date");
                e.printStackTrace();
            }

        }

        Calendar cur = Calendar.getInstance();
        Calendar drv = Calendar.getInstance();
        Calendar drv2 = Calendar.getInstance();
        Calendar per = Calendar.getInstance();

        Date current_date = new Date();
        cur.setTime(current_date);

        Map<String, Integer> taken_blood_dr = new HashMap<String, Integer>();
        int amt_blood_drives = drive_start.size();


        int used_count = 1;
        int jump = 0;
        int loc = 0;

        for (int i = 0; i < person_start.size(); i++){
            per.setTime(person_end.get(i));

            loc = 0;
            for(String bank_name : list_name){

                for (int j = 0; j < drive_start.get(bank_name).size(); j++){
                    System.out.printf("LOC IS CURRENTLY: %d", loc);
                    System.out.println();

                    drv.setTime(drive_start.get(bank_name).get(j));
                    drv2.setTime(drive_end.get(bank_name).get(j));
                    if (cur.get(Calendar.YEAR) == drv.get(Calendar.YEAR) && cur.get(Calendar.DAY_OF_YEAR) == drv.get(Calendar.DAY_OF_YEAR) &&
                            cur.get(Calendar.YEAR) == per.get(Calendar.YEAR) && cur.get(Calendar.DAY_OF_YEAR) == per.get(Calendar.DAY_OF_YEAR)){

                        if (cur.get(Calendar.HOUR) > drv2.get(Calendar.HOUR))
                            continue;

                        System.out.println("PLZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZz");

                        RelativeLayout screen = (RelativeLayout) findViewById(R.id.date_relative);

                        Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) drive_start.entrySet().toArray()[loc];
                        String blood_drive_name = entry.getKey();


                        TextView text2 = new TextView(this);
                        text2.setId(used_count);
                        // makes the main block of text
                        if (taken_blood_dr.containsKey(blood_drive_name)){

                            System.out.println("DOES IT EVER HIT THE CONTAINSSSSSSSSSSSSSSSSS");
                            TextView temp = findViewById(taken_blood_dr.get(blood_drive_name));
                            String part1 = "We noticed you're available until ";
                            String num1 = Integer.toString((per.get(Calendar.HOUR_OF_DAY)) - 4);
                            String part2 = ". Come and Donate, we are open today until ";
                            String num2 = Integer.toString(drv2.get(Calendar.HOUR_OF_DAY) - 4);
                            String part3 = ". We are located at ";
                            String addr = name_addr.get(blood_drive_name);
                            String total = part1 + num1 + part2 + num2 + part3 + addr;
                            temp.setText(total);
                            continue;

                        } else {
                            taken_blood_dr.put(blood_drive_name, used_count);
                            used_count++;

                            String part1 = "We noticed you're available until ";
                            String num1 = Integer.toString((per.get(Calendar.HOUR_OF_DAY)) - 4);
                            String part2 = ". Come and Donate, we are open today until ";
                            String num2 = Integer.toString(drv2.get(Calendar.HOUR_OF_DAY) - 4);
                            String part3 = ". We are located at ";
                            String addr = name_addr.get(blood_drive_name);
                            String total = part1 + num1 + part2 + num2 + part3 + addr;
                            text2.setText(total);

                        }


                        // the view made below gets added first
                        TextView text1 = new TextView(this);
                        String name = "From: " + blood_drive_name;
                        text1.setText(name);
                        final RelativeLayout.LayoutParams paramss
                                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        text1.setPadding( 50, 200 + (300*jump), 400, 50);
                        int offset = 300*jump;
                        text1.setTextSize(20);

                        screen.addView(text1, paramss);

                        jump++;

                        final RelativeLayout.LayoutParams params
                                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                        text2.setPadding( 50, 200 + (offset + 75), 100, 50);
                        text2.setTextSize(15);

                        screen.addView(text2, params);


                    }

                }
                loc++;

            }



        }


    }


    public void createSideBar(){
        // all code below within onCreate for navigation menu
        d1 = (DrawerLayout)findViewById(R.id.activity_message);
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
                    case R.id.user_availability:
                        i = new Intent(nv.getContext(), AvailabilityActivity.class);
                        startActivityForResult(i, MapsActivity.RequestCode.USER_AVAILABILITY_ACTIVITY_NUM);
                        break;
                    case R.id.messages:
                        i = new Intent(nv.getContext(), MessageActivity.class);
                        startActivityForResult(i, MapsActivity.RequestCode.MESSAGE_SCREEN);
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



}

