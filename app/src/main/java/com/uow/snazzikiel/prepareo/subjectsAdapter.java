package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
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
    Class:   subjectsAdapter
    ---------------------------------------
    Adapter class to load list in to the subjectsAdapter Page

    context:        Context taken from main activity calling this class
    rowItems:       object list of data stored locally

    TO DO: Join with other adapter functions to cater for different adapters
*/
public class subjectsAdapter extends BaseAdapter {

    Context context;
    List<subjectsData> rowItems;

    subjectsAdapter(Context context, List<subjectsData> rowItems) {
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
        ImageView subjLogo;
        TextView name;
        TextView code;
        ImageView subjArrow;
    }

    /**
        Function:   getView
        ---------------------------------------
        Place item Inflater in to list. Each item is defined by the layout.

        @param position:       Integer of item pressed
        @param convertView:    current View of item
        @param parent:         View of parent field
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.subjects_item, null);
            holder = new ViewHolder();

            holder.code = (TextView) convertView.findViewById(R.id.subjectsCode);
            holder.name = (TextView) convertView.findViewById(R.id.subjectsName);
            holder.subjLogo = (ImageView) convertView.findViewById(R.id.subjects_img);
            holder.subjArrow = (ImageView) convertView.findViewById(R.id.subjects_arrow);

            subjectsData row_pos = rowItems.get(position);

            holder.subjLogo.setImageResource(android.R.drawable.star_big_on);
            holder.subjArrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);
            holder.name.setText(row_pos.getCourseName());
            holder.code.setText(row_pos.getCourseCode());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}
