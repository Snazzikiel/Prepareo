package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assited:         Alec
 ***********************************************/

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
    Class:   assignmentsAdapter
    ---------------------------------------
    Adapter class to load list in to the Assignments Page

    context:        Context taken from main activity calling this class
    rowItems:       object list of data stored locally

    TO DO: Join with other adapter functions to cater for different adapters
*/
public class calendarInfoAdapter extends BaseAdapter {

    Context context;
    List<owlData> rowItems;

    calendarInfoAdapter(Context context, List<owlData> rowItems) {
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
        TextView activityDate;
        TextView activityDate2;
        TextView activityDuration;
        TextView activityName;
        ImageView activity_arrow;
        ImageView activity_img;

    }

    /**
        Function:   getView
        ---------------------------------------
        Place item Inflater in to list. Each item is defined by the layout.

        position:       Integer of item pressed
        convertView:    current View of item
        parent:         View of parent field
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.activity_item, null);
            holder = new ViewHolder();

            holder.activityDate = (TextView) convertView.findViewById(R.id.activityDate);
            holder.activityDuration = (TextView) convertView.findViewById(R.id.activityDuration);
            holder.activityDate2 = (TextView) convertView.findViewById(R.id.activityDate2);
            holder.activityName = (TextView) convertView.findViewById(R.id.activityName);
            holder.activity_img = (ImageView) convertView.findViewById(R.id.activity_img);
            holder.activity_arrow = (ImageView) convertView.findViewById(R.id.activity_arrow);

            owlData row_pos = rowItems.get(position);

            holder.activity_img.setImageResource(R.drawable.icon_activity);
            holder.activity_arrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);

            //put space at each capital letter

            holder.activityName.setText(row_pos.getMapKey());
            holder.activityDate.setText(row_pos.getStartTime());
            holder.activityDate2.setText(row_pos.getEndTime());
            holder.activityDuration.setText("(" + Long.toString(row_pos.getMapTime()) + "mins)");


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}
