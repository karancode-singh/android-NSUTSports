package com.nsit.jo.nsitsports;

/**
 * Created by jo on 18/01/18.
 */

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScoreBoard extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private final String DB = GlobalVariables.DB;
    private ArrayList<Entry> entriesPending;
    private ArrayList<Entry> entriesCompleted;
    private ArrayList<String> keysPending;
    private ArrayList<String> keysCompleted;
    private DatabaseReference mDatabase;
    private ListView listView;
    private ListView listView2;
    private TextView textSport;
    static boolean calledAlready = false;
    private ProgressBar progressBar;
    TextView tvPending;
    TextView tvCompleted;
    Snackbar snackbar;
    private NetworkStateReceiver networkStateReceiver;

    @Override
    public void onNetworkAvailable() {
        if (snackbar.isShown()) {
            snackbar.dismiss();
            Log.d("snackbar", "Hiding");
        }
    }

    @Override
    public void onNetworkUnavailable() {
        if (!snackbar.isShown()) {
            snackbar.show();
            Log.d("snackbar", "Showing");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        snackbar = Snackbar.make((CoordinatorLayout) findViewById(R.id.coordinatorLayout), "Unable to connect to the Internet", Snackbar.LENGTH_INDEFINITE);
        networkStateReceiver = new NetworkStateReceiver(this);
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


        tvPending = (TextView) findViewById(R.id.tv_pending);
        tvCompleted = (TextView) findViewById(R.id.tv_completed);
        textSport = (TextView) findViewById(R.id.textSport);
        tvPending.setVisibility(View.INVISIBLE);
        tvCompleted.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) LayoutInflater.from(this).inflate(R.layout.progress_bar, null);
//        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
//        progressBar.setVisibility(View.VISIBLE);

        if (!calledAlready) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }
            catch (Exception e){

            }
            calledAlready = true;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        textSport.setText(FirebaseActivity.selectedSport);

        if (FirebaseActivity.home) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(DB).child(MainActivity.YEAR).child(FirebaseActivity.selectedSport);
            home();
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(DB).child(FirebaseActivity.selectedYear).child(FirebaseActivity.selectedSport);
            generic();
        }
    }


    void home() {
        final String branchSection = MainActivity.BRANCH + "-" + MainActivity.SECTION;

        entriesPending = new ArrayList<>();
        entriesCompleted = new ArrayList<>();

        Query mySortingQuery = mDatabase.orderByChild("timeInMiliSec");

        listView = (ListView) findViewById(R.id.lv);
        listView2 = (ListView) findViewById(R.id.lv2);

        final Padapter myAdapter = new Padapter(this, entriesPending);
        final Cadapter myAdapter2 = new Cadapter(this, entriesCompleted);

        listView.setAdapter(myAdapter);
        listView2.setAdapter(myAdapter2);
        listView2.addFooterView(progressBar);
        mySortingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                listView2.removeFooterView(progressBar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mySortingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                entriesPending.clear();
                entriesCompleted.clear();
                myAdapter.notifyDataSetChanged();
                myAdapter2.notifyDataSetChanged();
                ListUtils.setDynamicHeight(listView);
                ListUtils.setDynamicHeight(listView2);
                Iterable<DataSnapshot> entries = dataSnapshot.getChildren();

                for (DataSnapshot entry : entries) {
                    Entry value = entry.getValue(Entry.class);
                    if (value.team1.equals(branchSection) || value.team2.equals(branchSection)) {
                        if (value.score1.equals("-1") || value.score2.equals("-1")) {
                            entriesPending.add(value);
                            ListUtils.setDynamicHeight(listView);
                        } else {
                            entriesCompleted.add(value);
                            ListUtils.setDynamicHeight(listView2);
                        }
                    }
                }
                if (entriesPending.isEmpty()) {
                    if (entriesCompleted.isEmpty()) {
                        finish();
                        Toast.makeText(ScoreBoard.this, "No upcoming/played matches found", Toast.LENGTH_SHORT).show();
                    } else {
                        tvCompleted.setVisibility(View.VISIBLE);
                        tvPending.setVisibility(View.GONE);
                        Toast.makeText(ScoreBoard.this, "No upcoming matches found", Toast.LENGTH_SHORT).show();
                    }
                } else if (entriesCompleted.isEmpty()) {
                    tvPending.setVisibility(View.VISIBLE);
                    tvCompleted.setVisibility(View.GONE);
                    Toast.makeText(ScoreBoard.this, "No played matches found", Toast.LENGTH_SHORT).show();
                } else {
                    tvPending.setVisibility(View.VISIBLE);
                    tvCompleted.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        mySortingQuery.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Entry value = dataSnapshot.getValue(Entry.class);
//                String key = dataSnapshot.getKey();
//                if (value.team1.equals(branchSection) || value.team1.equals(branchSection)) {
//                    if (value.score1.equals("-1")) {
//                        entriesPending.add(value);
//                        keysPending.add(key);
//                        myAdapter.notifyDataSetChanged();
//                        ListUtils.setDynamicHeight(listView);
//                    } else {
//                        entriesCompleted.add(value);
//                        keysCompleted.add(key);
//                        myAdapter2.notifyDataSetChanged();
//                        ListUtils.setDynamicHeight(listView2);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Entry value = dataSnapshot.getValue(Entry.class);
//                String key = dataSnapshot.getKey();
//
//                if (keysPending.contains(key)) {
//                    int index = keysPending.indexOf(key);
//                    if (value.score1.equals("-1")) {
//                        entriesPending.set(index, value);
//                        myAdapter.notifyDataSetChanged();
//                        ListUtils.setDynamicHeight(listView);
//                    } else {
//                        entriesPending.remove(index);
//                        entriesCompleted.add(value);
//                        Collections.sort(entriesCompleted, new Comparator<Entry>() {
//                            @Override
//                            public int compare(Entry entry1, Entry entry2) {
//                                return entry1.timeInMiliSec.compareTo(entry2.timeInMiliSec);
//                            }
//                        });
//                        myAdapter.notifyDataSetChanged();
//                        myAdapter2.notifyDataSetChanged();
//                        ListUtils.setDynamicHeight(listView);
//                        ListUtils.setDynamicHeight(listView2);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Entry value = dataSnapshot.getValue(Entry.class);
//                String key = dataSnapshot.getKey();
//
//                if (keysPending.contains(key)) {
//                    int index = keysPending.indexOf(key);
//                    entriesPending.remove(index);
//                    myAdapter.notifyDataSetChanged();
//                    ListUtils.setDynamicHeight(listView);
//                }
//                if (keysCompleted.contains(key)) {
//                    int index = keysCompleted.indexOf(key);
//                    entriesCompleted.remove(index);
//                    myAdapter2.notifyDataSetChanged();
//                    ListUtils.setDynamicHeight(listView);
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }


    void generic() {
        entriesPending = new ArrayList<>();
        entriesCompleted = new ArrayList<>();

        Query mySortingQuery = mDatabase.orderByChild("timeInMiliSec");

        listView = (ListView) findViewById(R.id.lv);
        listView2 = (ListView) findViewById(R.id.lv2);

        final Padapter myAdapter = new Padapter(this, entriesPending);
        final Cadapter myAdapter2 = new Cadapter(this, entriesCompleted);

        listView.setAdapter(myAdapter);
        listView2.setAdapter(myAdapter2);
        listView2.addFooterView(progressBar);

        mySortingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                listView2.removeFooterView(progressBar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mySortingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                entriesPending.clear();
                entriesCompleted.clear();
                myAdapter.notifyDataSetChanged();
                myAdapter2.notifyDataSetChanged();
                ListUtils.setDynamicHeight(listView);
                ListUtils.setDynamicHeight(listView2);
                Iterable<DataSnapshot> entries = dataSnapshot.getChildren();

                for (DataSnapshot entry : entries) {
                    Entry value = entry.getValue(Entry.class);
                    if (value.score1.equals("-1")) {
                        entriesPending.add(value);
                        ListUtils.setDynamicHeight(listView);
                    } else {
                        entriesCompleted.add(value);
                        ListUtils.setDynamicHeight(listView2);
                    }
                }
                if (entriesPending.isEmpty()) {
                    if (entriesCompleted.isEmpty()) {
                        finish();
                        Toast.makeText(ScoreBoard.this, "No upcoming/played matches found", Toast.LENGTH_SHORT).show();
                    } else {
                        tvCompleted.setVisibility(View.VISIBLE);
                        tvPending.setVisibility(View.GONE);
                        Toast.makeText(ScoreBoard.this, "No upcoming matches found", Toast.LENGTH_SHORT).show();
                    }
                } else if (entriesCompleted.isEmpty()) {
                    tvPending.setVisibility(View.VISIBLE);
                    tvCompleted.setVisibility(View.GONE);
                    Toast.makeText(ScoreBoard.this, "No played matches found", Toast.LENGTH_SHORT).show();
                } else {
                    tvPending.setVisibility(View.VISIBLE);
                    tvCompleted.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        mySortingQuery.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Entry value = dataSnapshot.getValue(Entry.class);
//                if (value.score1.equals("-1")) {
//                    entriesPending.add(value);
//                    myAdapter.notifyDataSetChanged();
//                    ListUtils.setDynamicHeight(listView);
//                } else {
//                    entriesCompleted.add(value);
//                    myAdapter2.notifyDataSetChanged();
//                    ListUtils.setDynamicHeight(listView2);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Entry value = dataSnapshot.getValue(Entry.class);
//                String key = dataSnapshot.getKey();
//
//                if (keysPending.contains(key)) {
//                    int index = keysPending.indexOf(key);
//                    if (value.score1.equals("-1")) {
//                        entriesPending.set(index, value);
//                        myAdapter.notifyDataSetChanged();
//                        ListUtils.setDynamicHeight(listView);
//                    } else {
//                        entriesPending.remove(index);
//                        entriesCompleted.add(value);
//                        Collections.sort(entriesCompleted, new Comparator<Entry>() {
//                            @Override
//                            public int compare(Entry entry1, Entry entry2) {
//                                return entry1.timeInMiliSec.compareTo(entry2.timeInMiliSec);
//                            }
//                        });
//                        myAdapter.notifyDataSetChanged();
//                        myAdapter2.notifyDataSetChanged();
//                        ListUtils.setDynamicHeight(listView);
//                        ListUtils.setDynamicHeight(listView2);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Entry value = dataSnapshot.getValue(Entry.class);
//                String key = dataSnapshot.getKey();
//
//                if (keysPending.contains(key)) {
//                    int index = keysPending.indexOf(key);
//                    entriesPending.remove(index);
//                    myAdapter.notifyDataSetChanged();
//                    ListUtils.setDynamicHeight(listView);
//                }
//                if (keysCompleted.contains(key)) {
//                    int index = keysCompleted.indexOf(key);
//                    entriesCompleted.remove(index);
//                    myAdapter2.notifyDataSetChanged();
//                    ListUtils.setDynamicHeight(listView);
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

}


