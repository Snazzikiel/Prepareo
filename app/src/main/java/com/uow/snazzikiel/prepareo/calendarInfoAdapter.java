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
        TextView activityName;
        ImageView activity_arrow;
        ImageView activity_img;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.activity_item, null);
            holder = new ViewHolder();

            holder.activityDate = (TextView) convertView.findViewById(R.id.activityDate);
            holder.activityName = (TextView) convertView.findViewById(R.id.activityName);
            holder.activity_img = (ImageView) convertView.findViewById(R.id.activity_img);
            holder.activity_arrow = (ImageView) convertView.findViewById(R.id.activity_arrow);

            owlData row_pos = rowItems.get(position);

            holder.activity_img.setImageResource(android.R.drawable.star_big_on);
            holder.activity_arrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);
            holder.activityName.setText(row_pos.getMapKey());
            holder.activityDate.setText(Long.toString(row_pos.getMapTime()));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}