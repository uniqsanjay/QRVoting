package com.smile.qrvoting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CandidateDetails extends AppCompatActivity {

    RecyclerView candlist;
    ProgressDialog progressDialog;
    ArrayList<String> namlist = new ArrayList<>();
    ArrayList<String> pinlist = new ArrayList<>();
    ArrayList<String> catlist = new ArrayList<>();
    ArrayList<String> wrdlist = new ArrayList<>();
    ArrayList<String> flagurllist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_details);

        candlist = findViewById(R.id.candidatelist);

        candlist.setHasFixedSize(true);
        candlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewdetails(Monitor.getWard());
    }

    public void viewdetails(String ward) {

        progressDialog = new ProgressDialog(CandidateDetails.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, "https://sanjaytask.000webhostapp.com/QR%20Voting/getCandidatesList.php?Ward="+ward, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    if(response.equals("Fail")){
                        progressDialog.dismiss();
                        Toast.makeText(CandidateDetails.this, response, Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        JSONObject object = new JSONObject(response);
                        JSONArray arr = object.getJSONArray("Candidate_details");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);
                            pinlist.add(obj.getString("Candidate_Id"));
                            namlist.add((obj.getString("Candidate_Name")));
                            catlist.add(obj.getString("Candidate_Type"));
                            wrdlist.add(obj.getString("Ward_no"));
                            flagurllist.add(obj.getString("Flag_no"));
                        }
                        CandidateHolder c = new CandidateHolder(getApplicationContext() , namlist , pinlist, catlist, wrdlist , flagurllist);
                        candlist.setAdapter(c);
                    }
                }catch (Exception e){
                    progressDialog.dismiss();
                    Toast.makeText(CandidateDetails.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CandidateDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(CandidateDetails.this);
        queue.add(request);

    }

}
