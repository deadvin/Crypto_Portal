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
import com.upperhand.cryptoterminal.Utils;
import com.upperhand.cryptoterminal.objects.tweet;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class BreakingAdapter extends RecyclerView.Adapter<BreakingAdapter.ViewHolder> {

    Context mContext;
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

        holder.time.setText(Utils.getTimeDifference(tweets.get(position).gettime()));
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