package com.example.alex.activityfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.activityfinder.R;
import com.example.alex.activityfinder.model.ActivityData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2016-08-11.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
    private List<ActivityData> activityDataList;
    private Context mContext;

    //define a layoutInflate object
    private LayoutInflater inflater;

    //define itemCLickCallback object
    private ItemClickCallback itemClickCallback;

    //define an interface for the ItemClickCallback function
    public interface ItemClickCallback{
        void onItemClick(int pos);
    }

    //define a function to set the callback function
    public void setItemClickCallback (final ItemClickCallback itemClickCallback){
        this.itemClickCallback = itemClickCallback;
    }

    public MyRecyclerViewAdapter(Context context, List<ActivityData> activityDataList) {
        this.activityDataList = activityDataList;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    //provide a function that the new data can be passed from adapter object
    public void setListData(List<ActivityData> newList){
        if(this.activityDataList != null){
            this.activityDataList.clear();
            this.activityDataList.addAll(newList);
        }

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ActivityData data = activityDataList.get(position);

        holder.imageView.setImageBitmap(data.getImage());
        holder.title_textView.setText(data.getActivityName());

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String dateText = df.format(data.getDate());
        holder.time_textView.setText(dateText);

        holder.description_textView.setText(data.getDescription());
        holder.location_textView.setText(data.getLocation_line1() + " " + data.getLocation_line2());
    }


    @Override
    public int getItemCount() {
        return (null != activityDataList ? activityDataList.size() : 0);
    }

    //this view holder is for the recyclerView
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView imageView;
        protected TextView title_textView;
        protected TextView time_textView;
        protected TextView description_textView;
        protected TextView location_textView;

        public View container;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.title_textView = (TextView) view.findViewById(R.id.activity_name);
            this.time_textView = (TextView) view.findViewById(R.id.activity_time);
            this.description_textView = (TextView)view.findViewById(R.id.activity_description);
            this.location_textView = (TextView)view.findViewById(R.id.activity_location);

            container = itemView.findViewById(R.id.list_item);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //will be invoked when the item being clicked
            if(v.getId() == R.id.list_item){
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }
    }

}
