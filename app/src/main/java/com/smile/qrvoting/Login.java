package com.smile.qrvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputLayout vid,vname;
    Button login;
    TextView signup;
    SQLiteDatabase db;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        vid = findViewById(R.id.login_vid);
        vname = findViewById(R.id.login_name);
        login = findViewById(R.id.login_loginbtn);
        signup = findViewById(R.id.login_signup);

    }

    @Override
    protected void onResume() {
        super.onResume();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String id = vid.getEditText().getText().toString();
                final String name = vname.getEditText().getText().toString();
                pd = new ProgressDialog(Login.this);
                pd.setTitle("Loading");
                pd.setMessage("Please wait....");
                pd.show();

                StringRequest sr = new StringRequest(Request.Method.GET, "https://sanjaytask.000webhostapp.com/QR%20Voting/login.php?VoterId="+id+"&Name="+name, new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Success")) {

                            db = openOrCreateDatabase("Logged_Data.db", MODE_PRIVATE, null);
                            db.execSQL("Insert into Voter_Detail(Voter_Id, Voter_Name)values('"+id+"','"+name+"')");
                            db.close();
                            pd.dismiss();
                            Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Login.this, LoginMain.class);
                            startActivity(intent);
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();
                        Toast.makeText(Login.this, "No Internet Connection" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                RequestQueue requestQueue= Volley.newRequestQueue(Login.this);
                requestQueue.add(sr);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Registration.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
