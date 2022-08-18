//package com.example.cryptoterminal.main;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.cryptoterminal.R;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.TimeZone;
//
//
//public class rec_adapter extends RecyclerView.Adapter<rec_adapter.ViewHolder> {
//
//    ArrayList<post> post_list;
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//    Date d;
//
//
//    public rec_adapter(Context context, ArrayList<post> data) {
//
//        this.post_list = data;
//    }
//
//
//    @NonNull
//    @Override
//
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View view = inflater.inflate(R.layout.tweet_layout,parent,false);
//        ViewHolder viewholder = new ViewHolder(view);
//
//        return viewholder;
//
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//        String text = post_list.get(position).getText();
//        String source =  post_list.get(position).getSource();
//        String time =  post_list.get(position).gettime();
//        String link =  post_list.get(position).getLink();
//        String likes =  post_list.get(position).get_likes();
//        String retweets =  post_list.get(position).get_retweets();
//        int seen =  post_list.get(position).getfow();
//
//
//        // ==============  TIME
//
//        try {
//            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//            d = sdf.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Date today = new Date();
//        long diff =  today.getTime() - d.getTime();
//        int hours = (int) (diff / (1000 * 60 * 60));
//        int minutes = (int) (diff / (1000 * 60)  % 60);
//        String min = ""+ minutes;
//        String fol;
//        int n_fol = (int)post_list.get(position).getfow();
//
//        if(n_fol > 1000){
//            fol =  Math.round(n_fol/1000) + "M";
//        }else {
//            fol = post_list.get(position).getfow() + "K";
//        }
//        if(minutes < 10){
//            min = "0" + minutes;
//        }
//
//        try {
//            holder.source.setText(post_list.get(position).getSource() + "  "+ " \u2022" + "  " + fol + "  " + "\u2022"+ "  " + hours + ":" + min + " ago");
//            holder.text.setText(post_list.get(position).getText());
//            holder.likes.setText(" \u2764 " + post_list.get(position).get_likes() + " \uD83D\uDD01 "+ post_list.get(position).get_retweets());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return post_list.size();
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView text;
//        TextView source;
//        TextView likes;
//        ImageView open;
//
//       public ViewHolder(@NonNull View itemView) {
//           super(itemView);
//           text =  itemView.findViewById(R.id.textView1);
//           source =  itemView.findViewById(R.id.textView2);
//           likes =  itemView.findViewById(R.id.textView3);
//           open =  itemView.findViewById(R.id.imageView);
//       }
//   }
//}
