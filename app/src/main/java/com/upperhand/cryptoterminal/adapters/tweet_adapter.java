package com.upperhand.cryptoterminal.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class tweet_adapter extends ArrayAdapter<tweet> {


    int count = 0;
    Context mContext;
    private int mResource;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView text;
        TextView source;
        TextView likes;
        ImageView open;
    }

    public tweet_adapter(Context context, int resource, ArrayList<tweet> objects) {
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
                holder.source = convertView.findViewById(R.id.textView2);
                holder.likes = convertView.findViewById(R.id.textView3);
                holder.open = convertView.findViewById(R.id.imageView);

                result = convertView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }


            holder.open.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String hhh = tweet.getLink();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hhh));
                    mContext.startActivity(browserIntent);
                }
            });


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
            String fol;
            int n_fol = (int) tweet.getfow();

            if (n_fol > 1000) {
                fol = Math.round(n_fol / 1000) + "M";
            } else {
                fol = tweet.getfow() + "K";
            }
            if (minutes < 10) {
                min = "0" + minutes;
            }
//
//       int  fol = 3;
//       int hours = 5;
//       int min = 10;

            try {

                if(eng_score == 0){
                    holder.likes.setText(" \u2764 " + tweet.get_likes() + " \uD83D\uDD01 " + tweet.get_retweets());
                }else{
                    holder.likes.setText(" \uD83D\uDCC8 " + eng_score + " \u2764 " + tweet.get_likes() + " \uD83D\uDD01 " + tweet.get_retweets());
                }
                holder.source.setText(tweet.getSource() + "  " + " \u2022" + "  " + fol + "  " + "\u2022" + "  " + hours + ":" + min + " ago");
                holder.text.setText(tweet.getText());


            } catch (Exception e) {
                e.printStackTrace();
            }


        return convertView;
    }



}

























