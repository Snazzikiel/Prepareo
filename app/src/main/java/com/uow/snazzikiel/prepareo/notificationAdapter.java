package com.uow.snazzikiel.prepareo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

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
            holder.noteArrow.setImageResource(R.drawable.baseline_more_vert_black_18dp);
            holder.name.setText(row_pos.getName());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }



    public class AlarmNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Intent myIntent = new Intent(context, Dashboard.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    myIntent,
                    FLAG_ONE_SHOT );

            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Example Notification")
                    .setContentIntent(pendingIntent)
                    .setContentText("Notification")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentInfo("Info");

            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,builder.build());
        }
    }



}
