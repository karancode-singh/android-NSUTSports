package com.nsit.jo.nsitsports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ImageButton btn = (ImageButton) findViewById(R.id.buttonGo);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Spinner spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
                editor.putString("year", String.valueOf(spinnerYear.getSelectedItem()));

                Spinner spinnerBranch = (Spinner) findViewById(R.id.spinnerBranch);
                editor.putString("branch", String.valueOf(spinnerBranch.getSelectedItem()));

                Spinner spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
                editor.putInt("section", Integer.parseInt(String.valueOf(spinnerSection.getSelectedItem())));
                editor.apply();

                MainActivity.YEAR = sharedPreferences.getString("year", "na");
                MainActivity.BRANCH = sharedPreferences.getString("branch", "na");
                MainActivity.SECTION = sharedPreferences.getInt("section", -1);


                startActivity(new Intent(LogInActivity.this, FirebaseActivity.class));
                finish();
            }
        });
    }

//    protected void goFunction(View view) {
//        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        Spinner spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
//        editor.putString("year", String.valueOf(spinnerYear.getSelectedItem()));
//
//        Spinner spinnerBranch = (Spinner) findViewById(R.id.spinnerBranch);
//        editor.putString("branch", String.valueOf(spinnerBranch.getSelectedItem()));
//
//        Spinner spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
//        editor.putInt("section", Integer.parseInt(String.valueOf(spinnerSection.getSelectedItem())));
//        editor.apply();
//
//        MainActivity.YEAR = sharedPreferences.getString("year","na");
//        Log.i("Login",MainActivity.YEAR);
//        MainActivity.BRANCH = sharedPreferences.getString("branch","na");
//        MainActivity.SECTION = sharedPreferences.getInt("section",-1);
//
//        Intent i = new Intent(MainActivity.this, FirebaseActivity.class);
//        MainActivity.this.startActivity(i);
//    }
}
