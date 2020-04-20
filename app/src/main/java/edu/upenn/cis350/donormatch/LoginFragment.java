package edu.upenn.cis350.donormatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

import com.mongodb.*;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.connection.ClusterSettings;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static java.util.Arrays.asList;

import org.bson.Document;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final int ButtonClickActivity = 1;
    EditText et_username, et_password;
    Button btn_login;
    String user, pass;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        et_username = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        btn_login = view.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user = et_username.getText().toString();
                pass = et_password.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(getContext(), "Fill all required fields", Toast.LENGTH_SHORT).show();
                }
                validate(user, pass);
            }
        });

        return view;
    }

    private void validate (String user, String pass) {
        try {
            URL url = new URL("http://10.0.2.2:3000/findUser?name="+user);
            AccessUsersTask task = new AccessUsersTask();
            task.execute(url);
            String password = task.get();
            System.out.println(password);
            if (userExists(user)) {
                Toast.makeText(getContext(), "No such username exists", Toast.LENGTH_SHORT).show();
                et_username.setText("");
                et_password.setText("");
            } else {
                if (!pass.equals(password)) {
                //if (!pass.equals(password)) {
                    Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                    et_password.setText("");
                } else {
                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    String[] vals = {user, pass};
                    System.out.println("urgooood");
                    /*i.putExtra("values", vals);
                    startActivityForResult(i, ButtonClickActivity);*/
                    startActivity(i);
                }
            }

        } catch (Exception e) {

        }
    }

    private boolean userExists(String user) {
        return (user == "Paul Zou" || user == "Raph");
    }

}