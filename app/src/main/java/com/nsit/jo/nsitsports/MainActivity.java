package com.nsit.jo.nsitsports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

//Structure Done

public class MainActivity extends Activity {
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private static int TIME_OUT = 1000; //Time to launch the another activity
    public static String YEAR;
    public static String BRANCH;
    public static Integer SECTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                YEAR = sharedPreferences.getString("year", "na");
                BRANCH = sharedPreferences.getString("branch", "na");
                SECTION = sharedPreferences.getInt("section", -1);
                Intent i;
                if (SECTION.equals(-1)) {
                    i = new Intent(MainActivity.this, LogInActivity.class);
                } else {
                    i = new Intent(MainActivity.this, FirebaseActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, TIME_OUT);


    }
}