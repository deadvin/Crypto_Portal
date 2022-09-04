package com.upperhand.cryptoterminal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.Utils;
import com.upperhand.cryptoterminal.objects.tweet;
import org.jetbrains.annotations.NotNull;
import java.util.Date;
import java.util.TimeZone;


public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date date;
    ArrayList<tweet> tweets;

    public TweetsAdapter(Context context, ArrayList<tweet> tweets) {
        this.mContext = context;
        this.tweets = tweets;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_tweet, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        String followers;
        int numberFollowers = tweets.get(position).getfow();

        if (numberFollowers > 1000) {
            followers = Math.round(numberFollowers / 1000) + "M";
        } else {
            followers = numberFollowers + "K";
        }

        holder.text.setText(tweets.get(position).getText());

        if (tweets.get(position).get_eng_score() == 0) {
            holder.likes.setText(" \u2764 " + tweets.get(position).get_likes() + " \uD83D\uDD01 " + tweets.get(position).get_retweets());
        } else {
            holder.likes.setText(" \uD83D\uDCC8 " + tweets.get(position).get_eng_score() + " \u2764 " + tweets.get(position).get_likes() + " \uD83D\uDD01 " + tweets.get(position).get_retweets());
        }
        holder.source.setText(tweets.get(position).getSource() + "  " + " \u2022" + "  " + followers + "  " + "\u2022" + "  " + Utils.getTimeDifference(tweets.get(position).gettime()));
        holder.text.setText(tweets.get(position).getText());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link = tweets.get(position).getLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView source;
        TextView likes;
        ImageView open;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView1);
            source = itemView.findViewById(R.id.textView2);
            likes = itemView.findViewById(R.id.textView3);
            open = itemView.findViewById(R.id.imageView);
        }
    }

}