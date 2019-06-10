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
    Class:   enrolmentAdapter
    ---------------------------------------
    ***** CLASS/FUNCTIONS NOT USED ******
 *
    Adapter class to load list in to the Assignments Page

    context:        Context taken from main activity calling this class
    rowItems:       object list of data stored locally

    TO DO: Join with other adapter functions to cater for different adapters
*/
public class enrolmentAdapter extends BaseAdapter {

    Context context;
    List<enrolmentData> rowItems;

    enrolmentAdapter(Context context, List<enrolmentData> rowItems) {
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
        TextView bachelor;
        TextView major;
        ImageView assignArrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.enrolment_item, null);
            holder = new ViewHolder();

            holder.major = (TextView) convertView.findViewById(R.id.tv_enrolmentMajor);
            holder.bachelor = (TextView) convertView.findViewById(R.id.tv_enrolmentBachelor);
            holder.assignLogo = (ImageView) convertView.findViewById(R.id.iv_enrolment_img);
            holder.assignArrow = (ImageView) convertView.findViewById(R.id.iv_enrolment_arrow);

            enrolmentData row_pos = rowItems.get(position);

            holder.assignLogo.setImageResource(android.R.drawable.star_big_on);
            holder.assignArrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);
            holder.bachelor.setText(row_pos.getBachelor());
            holder.major.setText(row_pos.getMajor());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}

