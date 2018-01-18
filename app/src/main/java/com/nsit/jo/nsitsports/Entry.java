package com.nsit.jo.nsitsports;

/**
 * Created by jo on 18/01/18.
 */

public class Entry {
    public String date;
    public String time;
    public Long timeInMiliSec;
    public String team1;
    public String team2;
    public String score1;
    public String score2;
    public String tag;

    Entry() {

    }

    Entry(String date, String time, Long timeInMiliSec, String team1, String team2, String score1, String score2, String tag) {
        this.date = date;
        this.time = time;
        this.timeInMiliSec = timeInMiliSec;
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.tag = tag;
    }
}

