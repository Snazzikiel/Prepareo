package com.uow.snazzikiel.prepareo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class assignmentsAdapter extends BaseAdapter {

    Context context;
    List<assignmentsData> rowItems;

    assignmentsAdapter(Context context, List<assignmentsData> rowItems) {
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
        TextView name;
        TextView weight;
        ImageView assignArrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.assignments_item, null);
            holder = new ViewHolder();

            holder.weight = (TextView) convertView.findViewById(R.id.assignmentsWeight);
            holder.name = (TextView) convertView.findViewById(R.id.assignmentsName);
            holder.assignLogo = (ImageView) convertView.findViewById(R.id.assignments_img);
            holder.assignArrow = (ImageView) convertView.findViewById(R.id.assignments_arrow);

            assignmentsData row_pos = rowItems.get(position);

            holder.assignLogo.setImageResource(android.R.drawable.star_big_on);
            holder.assignArrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);
            holder.name.setText(row_pos.getAssignmentName());
            holder.weight.setText(row_pos.getAssignmentWeight());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}
