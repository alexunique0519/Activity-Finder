package com.example.alex.activityfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.activityfinder.MainActivity;
import com.example.alex.activityfinder.R;
import com.example.alex.activityfinder.model.ActivityData;

import java.util.List;

/**
 * Created by Alex on 2016-07-25.
 */
public class ActivityViewAdapter extends BaseAdapter {

    public static ViewHolder viewHolder;
    public List<ActivityData> activityList;
    public Context context;
    //means android activity
    public Activity activity;

    public ActivityViewAdapter(Activity activity, List<ActivityData> apps) {
        this.activityList = apps;

        this.activity = activity;
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {


            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
            viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
            viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.DataText.setText(activityList.get(position).getDescription() + "");

        //Glide.with(activity.getApplicationContext()).load(activityList.get(position).getImage()).into(viewHolder.cardImage);
        viewHolder.cardImage.setImageBitmap(activityList.get(position).getImage());

        return rowView;
    }

}

