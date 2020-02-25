package com.smile.qrvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText vid,aano,name,dob,mobno, wrd;
    Button submit;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        vid = findViewById(R.id.reg_vid);
        aano = findViewById(R.id.reg_aano);
        name = findViewById(R.id.reg_name);
        dob = findViewById(R.id.reg_dob);
        mobno = findViewById(R.id.reg_mobno);
        wrd = findViewById(R.id.reg_wrd);

        submit = findViewById(R.id.reg_submitbtn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(Registration.this);
                pd.setTitle("Loading");
                pd.setMessage("Please wait....");
                pd.show();

                final String s1 = vid.getText().toString();
                final String s2 = aano.getText().toString();
                final String s3 = name.getText().toString();
                final String s4 =dob.getText().toString();
                final String s5 = mobno.getText().toString();
                final String s6 = wrd.getText().toString();

                StringRequest sr = new StringRequest(Request.Method.POST, "https://sanjaytask.000webhostapp.com/QR%20Voting/addVoters.php", new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Check", response);
                        if (response.equalsIgnoreCase("success")) {

                            pd.dismiss();
                            // If response matched then show the toast.
                            Toast.makeText(Registration.this, "Logged In Successfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Registration.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pd.dismiss();

                        Toast.makeText(Registration.this, "No Internet Connection" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("vid", s1);
                        params.put("aano", s2);
                        params.put("name" , s3);
                        params.put("dob" , s4);
                        params.put("phno" , s5);
                        params.put("wrd_pin" , s6);

                        return params;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(Registration.this);
                requestQueue.add(sr);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext() , Login.class));
        finish();
    }

}
