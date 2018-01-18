package com.nsit.jo.nsitsports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jo on 18/01/18.
 */

public class Cadapter extends ArrayAdapter<Entry> {

    public Cadapter(Context context, List<Entry> data) {
        super(context, R.layout.c_custom_row, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater lf = LayoutInflater.from(getContext());
        View customView = lf.inflate(R.layout.c_custom_row, parent, false);
        Entry rData = getItem(position);
        TextView tv_t1 = (TextView) customView.findViewById(R.id.t1f2);
        TextView tv_t2 = (TextView) customView.findViewById(R.id.t2f2);
        TextView tv_date = (TextView) customView.findViewById(R.id.datef2);
        TextView tv_score1 = (TextView) customView.findViewById(R.id.Score1);
        TextView tv_score2 = (TextView) customView.findViewById(R.id.Score2);
        TextView tv_colon = (TextView)customView.findViewById(R.id.colon);
        TextView tv_tag = (TextView) customView.findViewById(R.id.cTag);
        if (FirebaseActivity.selectedSport.equals("Cricket")) {
            tv_score1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv_score2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv_colon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv_t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            tv_t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        }
        tv_t1.setText(rData.team1);
        tv_t2.setText(rData.team2);
        tv_date.setText(rData.date);
        tv_score1.setText(rData.score1);
        tv_score2.setText(rData.score2);
        tv_tag.setText(rData.tag);
        if (rData.tag.equals(""))
            tv_tag.setVisibility(View.GONE);
        return customView;
    }
}
