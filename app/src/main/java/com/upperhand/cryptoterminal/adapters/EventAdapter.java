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
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    Context mContext;
    ArrayList<event> events;

    public EventAdapter(Context context, ArrayList<event> events) {
        this.mContext = context;
        this.events = events;
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_event, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        holder.name.setText(events.get(position).getSym().toUpperCase() + " \u2022 " +  events.get(position).getName() + " \u2022 " + events.get(position).getDate());
        holder.text.setText(events.get(position).getText());

        holder.time.setText(Utils.getTimeDifferenceDays(events.get(position).getTime()));

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link = events.get(position).getLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                mContext.startActivity(browserIntent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView time;
        TextView name;
        ImageView open;

        ViewHolder(View itemView) {
            super(itemView);
            text =  itemView.findViewById(R.id.textView1);
            time =  itemView.findViewById(R.id.textView3);
            name =  itemView.findViewById(R.id.textView2);
            open =  itemView.findViewById(R.id.imageView);
        }
    }

}

























