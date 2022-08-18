package com.upperhand.cryptoterminal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class breaking_adapter extends ArrayAdapter<tweet> {

    Context mContext;
    private int mResource;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView text;
        TextView time;
    }

    public breaking_adapter(Context context, int resource, ArrayList<tweet> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            String text = getItem(position).getText();
            String source = getItem(position).getSource();
            String time = getItem(position).gettime();
            String link = getItem(position).getLink();
            String likes = getItem(position).get_likes();
            String retweets = getItem(position).get_retweets();
            int seen = getItem(position).getfow();
            float eng_score = getItem(position).get_eng_score();

            tweet tweet = new tweet(text, source, seen, time, link, likes, retweets, eng_score);

            final View result;
            ViewHolder holder;

            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder = new ViewHolder();
                holder.text = convertView.findViewById(R.id.textView1);
                holder.time = convertView.findViewById(R.id.textView2);


                result = convertView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            // ==============  TIME

            try {
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                d = sdf.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date today = new Date();
            long diff = today.getTime() - d.getTime();


            int hours = (int) (diff / (1000 * 60 * 60));
            int minutes = (int) (diff / (1000 * 60) % 60);
            String min = "" + minutes;

            if (minutes < 10) {
                min = "0" + minutes;
            }

            try {
                holder.text.setText(tweet.getText());
                holder.time.setText(hours + ":" + min + " ago");

            } catch (Exception e) {
                e.printStackTrace();
            }



        return convertView;
    }

}

























