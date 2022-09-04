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
import com.upperhand.cryptoterminal.objects.event;
import com.upperhand.cryptoterminal.objects.video;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    Context mContext;
    ArrayList<video> videos;

    public VideoAdapter(Context context, ArrayList<video> videos) {
        this.mContext = context;
        this.videos = videos;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView name;
        ImageView open;

        ViewHolder(View itemView) {
            super(itemView);
            text =  itemView.findViewById(R.id.textView1);
            name =  itemView.findViewById(R.id.textView2);
            open =  itemView.findViewById(R.id.imageView);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_video, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        holder.name.setText(videos.get(position).getName() + " \u2022 "  + Utils.getTimeDifference(videos.get(position).getTime()));
        holder.text.setText(videos.get(position).getText());

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link = videos.get(position).getLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                mContext.startActivity(browserIntent);
            }
        });
    }

}

























