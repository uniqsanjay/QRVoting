package com.smile.qrvoting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

public class Splase extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splase);

        createDB();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext() , Login.class));
                finish();
            }
        },3000);
    }

    public void createDB(){
        db = openOrCreateDatabase("Logged_Data.db", MODE_PRIVATE, null);
        db.execSQL("Create Table if not exists Voter_Detail(Voter_Id TEXT, Voter_Name TEXT)");
        db.close();
    }
}
