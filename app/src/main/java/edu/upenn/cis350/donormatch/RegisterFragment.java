package edu.upenn.cis350.donormatch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.net.URL;

public class RegisterFragment extends Fragment {

    EditText et_name, et_username, et_password, et_repass;
    Button btn_register;
    String user, pass, name, repass;
    final int ButtonClickActivity = 1;

    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        et_name = view.findViewById(R.id.et_name);
        et_username = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        et_repass = view.findViewById(R.id.et_repassword);
        btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user = et_username.getText().toString();
                pass = et_password.getText().toString();
                name = et_name.getText().toString();
                repass = et_repass.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) ||
                        TextUtils.isEmpty(repass) || TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Fill all required fields", Toast.LENGTH_SHORT).show();
                } else if(!pass.equals(repass)) {
                    Toast.makeText(getContext(), "Passwords must match", Toast.LENGTH_SHORT).show();
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
            if (password.isEmpty()) {
                Toast.makeText(getContext(), "No such username exists", Toast.LENGTH_SHORT).show();
            } else {
                if (!pass.equals(password)) {
                    Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                    //blank password
                } else {
                    Intent i = new Intent(getContext(), MainActivity.class);
                    String[] vals = {user, pass};
                    System.out.println("urgooood");
                    i.putExtra("values", vals);
                    startActivityForResult(i, ButtonClickActivity);
                }
            }

        } catch (Exception e) {

        }




    }

}