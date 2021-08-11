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


public class PostAdapter extends ArrayAdapter<post> {


    Context mContext;
    private int mResource;
    private int lastPosition = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    Date d;

    private static class ViewHolder {
        TextView text;
        TextView time;
        TextView source;
        TextView followers;
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
        int seen = getItem(position).getseen();
        String followers = getItem(position).getfollow();


        //Create the person object with the information
        post post = new post(text,source,seen,time,link,followers);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.text =  convertView.findViewById(R.id.textView1);
            holder.source =  convertView.findViewById(R.id.textView2);
            holder.followers =  convertView.findViewById(R.id.textView3);
            holder.time =  convertView.findViewById(R.id.textView4);
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


        holder.time.setText(hours + ":" + min + " ago");
        holder.source.setText(post.getSource());
        holder.text.setText(post.getText());
        holder.followers.setText(fol);



        return convertView;
    }
}

























