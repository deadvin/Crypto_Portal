package com.example.cryptoterminal.main;

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

import com.example.cryptoterminal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class EventAdapter extends ArrayAdapter<event> {


    Context mContext;
    private int mResource;
    private int lastPosition = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView text;
        TextView time;
        TextView name;
        ImageView open;
    }

    public EventAdapter(Context context, int resource, ArrayList<event> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information

        String sym = getItem(position).getSym();
        String name = getItem(position).getName();
        String date = getItem(position).getDate();
        String link = getItem(position).getLink();
        String time = getItem(position).getTime();
        String text = getItem(position).getText();
        int seen = getItem(position).getseen();

        event event = new event(sym,name,date,text,time,link,seen);

        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.text =  convertView.findViewById(R.id.textView1);
            holder.time =  convertView.findViewById(R.id.textView3);
            holder.name =  convertView.findViewById(R.id.textView2);
            holder.open =  convertView.findViewById(R.id.imageView);


            holder.open.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String hhh = event.getLink();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hhh));
                    mContext.startActivity(browserIntent);

                }
            });


            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        // ==============  TIME

        try {
             sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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



        holder.name.setText(event.getSym().toUpperCase() + " \u2022 " +  event.getName() + " \u2022 " + event.getDate());
        holder.text.setText(event.getText());
        holder.time.setText(hours + ":" + min + " ago");





        return convertView;
    }
}

























