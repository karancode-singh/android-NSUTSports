package com.nsit.jo.nsitsports;

/**
 * Created by jo on 18/01/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FirebaseActivity extends AppCompatActivity {


    private FrameLayout mFrame;
    private ListView list;
    private ArrayList<String> sportsArrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private AdapterView.OnItemClickListener itemClickListener;
    private Spinner spinnerYear;

    static protected String selectedYear;
    static protected String selectedSport;
    static protected boolean home = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    home();
                    return true;
                case R.id.navigation_all:
                    all();
                    return true;
                case R.id.navigation_changeHome:
                    change();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mFrame = (FrameLayout) findViewById(R.id.frame);
        sportsArrayList.add("Football");
        sportsArrayList.add("Kabaddi");
        sportsArrayList.add("Cricket");
        sportsArrayList.add("Basketball");
        sportsArrayList.add("Badminton");
        sportsArrayList.add("TableTennis");
        sportsArrayList.add("Chess");
        list = new ListView(this);
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                sportsArrayList);
        list.setAdapter(arrayAdapter);

        spinnerYear = new Spinner(this);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("1st Year");
        spinnerArray.add("2nd Year");
        spinnerArray.add("3rd Year");
        spinnerArray.add("4th Year");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);

        itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSport = sportsArrayList.get(position);
                selectedYear = String.valueOf(spinnerYear.getSelectedItem());
                Intent intent = new Intent(FirebaseActivity.this, ScoreBoard.class);
                startActivity(intent);
            }
        };

        home();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void home() {
//        mTextMessage.setText(MainActivity.YEAR + " " + MainActivity.BRANCH + " " + MainActivity.SECTION);
        home = true;
        mFrame.removeAllViews();

        if (list.getParent() != null)
            ((ViewGroup) list.getParent()).removeView(list);
        mFrame.addView(list);

        list.setOnItemClickListener(itemClickListener);
    }

    private void all() {
        home = false;
        mFrame.removeAllViews();

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 0);
        ll.setLayoutParams(params);
        mFrame.addView(ll);

        params.setMargins(20, 0, 0, 0);
        spinnerYear.setLayoutParams(params);
        if (spinnerYear.getParent() != null)
            ((ViewGroup) spinnerYear.getParent()).removeView(spinnerYear);
        ((LinearLayout) ll).addView(spinnerYear);

        params.setMargins(0, 40, 0, 0);
        list.setLayoutParams(params);
        if (list.getParent() != null)
            ((ViewGroup) list.getParent()).removeView(list);
        ll.addView(list);

        list.setOnItemClickListener(itemClickListener);
    }

    public void change() {
        Toast.makeText(this, "Ditching your Home Team? Sure?", Toast.LENGTH_LONG).show();
        startActivity(new Intent(FirebaseActivity.this, LogInActivity.class));
        finish();
    }
}
