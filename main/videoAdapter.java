package com.upperhand.cryptoterminal.objects;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.upperhand.cryptoterminal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class videoAdapter extends ArrayAdapter<video> {

    Context mContext;
    private int mResource;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView name;
        TextView text;
        TextView time;
        ImageView open;
    }

    public videoAdapter(Context context, int resource, ArrayList<video> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        String text = getItem(position).getText();
        String link = getItem(position).getLink();
        String time = getItem(position).getTime();
        int  seen = getItem(position).getAlarm();

        video video = new video(name,text,link,time,seen);

        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name =  convertView.findViewById(R.id.textView2);
            holder.text =  convertView.findViewById(R.id.textView1);
            holder.open =  convertView.findViewById(R.id.imageView);

            result = convertView;
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.open.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String hhh = video.getLink();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hhh));
                mContext.startActivity(browserIntent);

            }
        });


        try {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date today = new Date();
        long diff =  today.getTime() - d.getTime();


        int hours = (int) (diff / (1000 * 60 * 60));
        int minutes = (int) (diff / (1000 * 60)  % 60);
        String min = ""+ minutes;

        if(minutes < 10){
            min = "0" + minutes;
        }



        holder.name.setText(video.getName() + " \u2022 "  +  hours + ":" + min + " ago");
        holder.text.setText(video.getText());


        return convertView;
    }
}

























