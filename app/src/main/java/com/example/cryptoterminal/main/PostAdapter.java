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
import java.util.Locale;
import java.util.TimeZone;


public class PostAdapter extends ArrayAdapter<post> {


    Context mContext;
    private int mResource;
    private int lastPosition = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView text;
        TextView source;
        TextView likes;
        ImageView open;
    }

    public PostAdapter(Context context, int resource, ArrayList<post> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String text = getItem(position).getText();
        String source = getItem(position).getSource();
        String time = getItem(position).gettime();
        String link = getItem(position).getLink();
        String likes = getItem(position).get_likes();
        String retweets = getItem(position).get_retweets();
        int seen = getItem(position).getseen();


        post post = new post(text,source,seen,time,link,likes,retweets);

        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.text =  convertView.findViewById(R.id.textView1);
            holder.source =  convertView.findViewById(R.id.textView2);
            holder.likes =  convertView.findViewById(R.id.textView3);
            holder.open =  convertView.findViewById(R.id.imageView);



            holder.open.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String hhh = "https://twitter.com/twitter/statuses/" +  post.getLink();

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


//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//        result.startAnimation(animation);
//        lastPosition = position;





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
        String fol;
        int n_fol = (int)post.getseen();

        if(n_fol > 1000){
            fol =  Math.round(n_fol/1000) + "M";
        }else {
            fol = post.getseen() + "K";
        }
        if(minutes < 10){
              min = "0" + minutes;
        }


        holder.source.setText(post.getSource() + "  "+ " \u2022" + "  " + fol + "  " + "\u2022"+ "  " + hours + ":" + min + " ago");
        holder.text.setText(post.getText());
        holder.likes.setText(" \u2764 " + post.get_likes() + " \uD83D\uDD01 "+ post.get_retweets());


        return convertView;
    }
}

























