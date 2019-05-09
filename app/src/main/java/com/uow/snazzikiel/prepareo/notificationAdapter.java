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

public class notificationAdapter extends BaseAdapter {

    Context context;
    List<notificationData> rowItems;

    notificationAdapter(Context context, List<notificationData> rowItems) {
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
        ImageView noteLogo;
        TextView name;
        ImageView noteArrow;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInFlater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInFlater.inflate(R.layout.notification_item, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.notificationName);
            holder.noteLogo = (ImageView) convertView.findViewById(R.id.notification_img);
            holder.noteArrow = (ImageView) convertView.findViewById(R.id.notification_arrow);

            notificationData row_pos = rowItems.get(position);

            holder.noteLogo.setImageResource(R.drawable.icon_notification);
            holder.noteArrow.setImageResource(R.drawable.icon_arrow);
            holder.name.setText(row_pos.getName());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}
