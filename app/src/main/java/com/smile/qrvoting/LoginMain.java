package com.smile.qrvoting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginMain extends AppCompatActivity {

    ImageView scan1;
    TextView scan2;
    SQLiteDatabase db;
    private IntentIntegrator qrScan;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        scan1 = findViewById(R.id.main_scantab2);
        scan2 = findViewById(R.id.main_scantab1);

        checkLocationPermission();

        qrScan = new IntentIntegrator(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String rs = result.getContents();
                    String[] arr = rs.split(";");
                    String id = arr[0];
                    String name = arr[1];
                    String ward = arr[2];
                    new Monitor(this);
                    Monitor.addVoterDetails(id, name);
                    Monitor.addWardNo(ward);
                    db = openOrCreateDatabase("Logged_Data.db", MODE_PRIVATE, null);
                    Cursor c = db.rawQuery("Select Voter_Id, Voter_Name from Voter_Detail where Voter_Id='"+id+"'", null);
                    if(c!=null){
                        if(c.moveToFirst()){
                            do{
                                String vid = c.getString(0);
                                String vname = c.getString(1);
                                if(id.equals(vid) && name.equals(vname)){
                                    startActivity(new Intent(LoginMain.this, CandidateDetails.class));
                                }
                            }while (c.moveToNext());
                        }
                    }
                    //JSONObject obj = new JSONObject(result.getContents());
                    //Toast.makeText(getApplicationContext(), id+" "+name+" "+ward, Toast.LENGTH_LONG).show();
                    scan2.setText(result.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                qrScan.initiateScan();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


}
