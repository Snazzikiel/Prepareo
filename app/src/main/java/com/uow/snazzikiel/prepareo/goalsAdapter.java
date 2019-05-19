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

import java.util.List;

public class goalsAdapter extends BaseAdapter {

    Context context;
    List<goalsData> rowItems;

    goalsAdapter(Context context, List<goalsData> rowItems) {
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
            holder.goalsChkBox = (CheckBox) convertView.findViewById((R.id.goals_checkbox));

            goalsData row_pos = rowItems.get(position);

            holder.assignLogo.setImageResource(android.R.drawable.star_big_on);
            holder.assignArrow.setImageResource(android.R.drawable.ic_menu_sort_by_size);
            holder.title.setText(row_pos.getGoalTitle());
            //holder.date.setText(row_pos.getAssignmentWeight());
            holder.goalsChkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set click event on item here
                    CheckBox x = (CheckBox) v.findViewById(R.id.goals_checkbox);

                    if (x.isChecked()){
                        x.setChecked(true);
                    } else {
                        x.setChecked(false);
                    }
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



}
