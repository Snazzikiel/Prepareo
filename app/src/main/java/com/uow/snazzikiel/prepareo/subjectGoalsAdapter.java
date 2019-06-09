package com.uow.snazzikiel.prepareo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/*
    Class:   subjectGoalsAdapter
    ---------------------------------------
    Adapter class to load list in to the subjectGoalsAdapter Page

    context:        Context taken from main activity calling this class
    rowItems:       object list of data stored locally

    TO DO: Join with other adapter functions to cater for different adapters
*/
public class subjectGoalsAdapter extends BaseAdapter {

    Context context;
    List<goalsData> rowItems;

    subjectGoalsAdapter(Context context, List<goalsData> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() { return rowItems.size(); }

    @Override
    public Object getItem(int position) { return rowItems.get(position); }

    @Override
    public long getItemId(int position) { return rowItems.indexOf(getItem(position)); }

    /* private view holder class */
    private class ViewHolder {
        ImageView assignLogo;
        TextView title;
        //TextView date;
        ImageView assignArrow;
        CheckBox goalsChkBox;
    }

    /*
        Function:   getView
        ---------------------------------------
        Get the list of all input fields on the current view

        position:       Integer of item pressed
        convertView:    current View of item
        parent:         View of parent field
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.goals_item, null);
            holder = new ViewHolder();

            //holder.date = (TextView) convertView.findViewById(R.id.goalsDate);
            holder.title = (TextView) convertView.findViewById(R.id.goalsTitle);
            holder.assignLogo = (ImageView) convertView.findViewById(R.id.goals_img);
            holder.assignArrow = (ImageView) convertView.findViewById(R.id.goals_arrow);

            goalsData row_pos = rowItems.get(position);

            holder.assignLogo.setImageResource(android.R.drawable.star_big_on);
            holder.assignArrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);
            holder.title.setText(row_pos.getGoalTitle());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}
