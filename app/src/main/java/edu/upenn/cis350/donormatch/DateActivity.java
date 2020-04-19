package edu.upenn.cis350.donormatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.net.URL;
import java.util.Date;

public class DateActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout d1;
    private ActionBarDrawerToggle t1;
    private NavigationView nv;
    private TextView txtdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        createSideBar();
        String result = "";
        try {
            String address;
            String id = "Phillip";
            String request_method = "GET";
            URL url = new URL("http://10.0.2.2:3000/getPerson?name="+id);
            AccessWebTask task = new AccessWebTask(request_method);
            task.execute(url);
            result = task.get();
            System.out.println("person should be printed below");
        } catch (Exception e){
            e.toString();
        }

        System.out.println(result);

        int s_date_index = result.indexOf("start_dates");
        int s1_brack_index = result.indexOf('[', s_date_index);
        int s2_brack_index = result.indexOf(']', s1_brack_index);
        String str_start_dates1 = result.substring((s1_brack_index + 1), s2_brack_index);
        String str_start_dates = str_start_dates1.replace("\"", "");
        System.out.println("##########################################################################3");
        System.out.println(str_start_dates);
        String[] str_start_array = str_start_dates.split(",");

        int e_date_index = result.indexOf("end_dates");
        int e1_brack_index = result.indexOf('[', e_date_index);
        int e2_brack_index = result.indexOf(']', e1_brack_index);
        String str_end_dates1 = result.substring((e1_brack_index + 1), e2_brack_index);
        String str_end_dates = str_end_dates1.replace("\"", "");
        System.out.println("##########################################################################3");
        System.out.println(str_end_dates);
        String[] str_end_array = str_end_dates.split(",");

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.date_relative);
        int prevTextViewId = 0;
        for(int i = 0; i < str_start_array.length; i++){
            String cur_date_start = str_start_array[i];
            String cur_date_end = str_end_array[i];
            String cur_year = cur_date_start.substring(0,4);
            String cur_month = cur_date_start.substring(5,7);
            String cur_day = cur_date_start.substring(8,10);

            String cur_start_hour = cur_date_start.substring(11,13);
            int temp_start = Integer.parseInt(cur_start_hour);
            temp_start = temp_start - 4;
            cur_start_hour = Integer.toString(temp_start);
            String cur_end_hour = cur_date_end.substring(11,13);
            int temp_end = Integer.parseInt(cur_end_hour);
            temp_end = temp_end - 4;
            cur_end_hour = Integer.toString(temp_end);

            final TextView textView = new TextView(this);
            String set_date = cur_month + "/" + cur_day + "/" + cur_year;
            set_date += " hours:" + cur_start_hour + "-" + cur_end_hour;
            textView.setText(set_date);

            int curTextViewId = prevTextViewId + 1;
            textView.setId(curTextViewId);

            final RelativeLayout.LayoutParams params
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            //params.height = 100 + (100 * i);

            //params.addRule(RelativeLayout.BELOW, prevTextViewId);
            textView.setPadding( 50, 200 + (100*i) , 400, 200 + (100*i));

            textView.setLayoutParams(params);
            textView.setTextSize(20);

            prevTextViewId = curTextViewId;
            layout.addView(textView, params);

        }


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
                        Toast.makeText(DateActivity.this, "Not implemented yet",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(DateActivity.this, "Not implemented yet",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.other:
                        Toast.makeText(DateActivity.this, "peek a boo",Toast.LENGTH_SHORT).show();
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


}
