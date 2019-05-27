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
            holder.assignArrow.setImageResource(android.R.drawable.ic_menu_sort_by_size);
            holder.bachelor.setText(row_pos.getBachelor());
            holder.major.setText(row_pos.getMajor());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
