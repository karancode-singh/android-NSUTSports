package com.nsit.jo.nsitsports;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

//Structure Done

public class MainActivity extends Activity {
    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private static int TIME_OUT = 1000; //Time to launch the another activity
    private static SharedPreferences sharedPreferences;
    public static String YEAR;
    public static String BRANCH;
    public static Integer SECTION;
    private static Boolean ALERT_DISPLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                YEAR = sharedPreferences.getString("year", "na");
                BRANCH = sharedPreferences.getString("branch", "na");
                SECTION = sharedPreferences.getInt("section", -1);
                ALERT_DISPLAY = sharedPreferences.getBoolean("alertDisplay", true);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_app_updates, null);
                CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
                mBuilder.setTitle("Message from developers");
                mBuilder.setMessage("\nDevelopers don't update the match fixtures or their results. Sports Committee is responsible for it.\n\n" +
                        "If match fixture for your Home Team is not available, maybe the fixture hasn't been decided yet. ");
                mBuilder.setView(mView);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent;
                        if (SECTION.equals(-1)) {
                            intent = new Intent(MainActivity.this, LogInActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this, FirebaseActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
                mDialog.setCancelable(false);
                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()){
                            storeDialogStatus(true);
                        }else{
                            storeDialogStatus(false);
                        }
                    }
                });
                if(ALERT_DISPLAY){
                    mDialog.show();
                }else{
                    mDialog.hide();
                    Intent intent;
                    if (SECTION.equals(-1)) {
                        intent = new Intent(MainActivity.this, LogInActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, FirebaseActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        }, TIME_OUT);


    }

    private void storeDialogStatus(boolean isChecked){
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean("alertDisplay", !isChecked);
        mEditor.apply();
    }
}