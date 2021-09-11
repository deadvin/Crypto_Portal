package com.example.cryptoterminal.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.cryptoterminal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class videoAdapter extends ArrayAdapter<video> {

    Context mContext;
    private int mResource;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView name;
        TextView text;
        TextView time;
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
        int  alarm = getItem(position).getAlarm();

        video video = new video(name,text,link,time,alarm);

        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name =  convertView.findViewById(R.id.textView2);
            holder.text =  convertView.findViewById(R.id.textView1);


//
//            holder.open.setOnClickListener( new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    String hhh = "https://twitter.com/twitter/statuses/" +  post.getLink();
//
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hhh));
//                    mContext.startActivity(browserIntent);
//
//                }
//            });

            result = convertView;

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        try {
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



        holder.name.setText(video.getName() + "  "+ " \u2022" + "  " + video.getTime());
        holder.text.setText(video.getText());


        return convertView;
    }
}

























