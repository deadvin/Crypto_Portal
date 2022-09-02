package com.upperhand.cryptoterminal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.tweet;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class BreakingAdapter extends RecyclerView.Adapter<BreakingAdapter.ViewHolder> {

    Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date date;
    ArrayList<tweet> tweets;

    public BreakingAdapter(Context context, ArrayList<tweet> tweets) {

        this.mContext = context;
        this.tweets = tweets;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_breaking, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            date = sdf.parse(tweets.get(position).gettime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date today = new Date();
        long diff = today.getTime() - date.getTime();
        int hours = (int) (diff / (1000 * 60 * 60));
        int mins = (int) (diff / (1000 * 60) % 60);
        String min = "" + mins;

        if (mins < 10) {
            min = "0" + mins;
        }

        holder.time.setText( hours + ":" + min + " ago");
        holder.text.setText(tweets.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView1);
            time = itemView.findViewById(R.id.textView2);
        }
    }

}