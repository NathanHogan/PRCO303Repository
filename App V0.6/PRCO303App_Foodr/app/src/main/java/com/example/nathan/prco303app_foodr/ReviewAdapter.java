package com.example.nathan.prco303app_foodr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nathan on 22/04/2015.
 */
public class ReviewAdapter extends BaseAdapter {
    Context context;

    protected List<reviewClass> reviewList;
    LayoutInflater inflater;

    public ReviewAdapter(Context context, List<reviewClass> reviewList){
        this.reviewList = reviewList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.review_list_layout, parent, false);

            holder.reviewText = (TextView) convertView.findViewById(R.id.revText);
            holder.reviewRate = (RatingBar) convertView.findViewById(R.id.revRate);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        reviewClass review = reviewList.get(position);
        holder.reviewText.setText(review.getReviewText());
        holder.reviewRate.setRating((float)review.getReviewRate()/2);

        return convertView;


    }

    private class ViewHolder{
        TextView reviewText;
        RatingBar reviewRate;
    }
}
