package com.upperhand.cryptoterminal.objects;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.upperhand.cryptoterminal.R;
import java.util.ArrayList;



public class SymbolAdapter extends ArrayAdapter<symbol> {

    Context mContext;
    private int mResource;

    private static class ViewHolder {
        TextView name;
        TextView vol;
        TextView m15;
        TextView h1;
        TextView h4;
        TextView h10;

    }

    public SymbolAdapter(Context context, int resource, ArrayList<symbol> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        float a_15m = getItem(position).get15m();
        float a_1h = getItem(position).get1h();
        float a_4h = getItem(position).get4h();
        float a_10h = getItem(position).get10h();
        float vol = getItem(position).getvol();
        float now = getItem(position).getNow();


        symbol symbol = new symbol(name,now,a_15m,a_1h,a_4h,a_10h,vol);

        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name =  convertView.findViewById(R.id.textView);
            holder.h10 =  convertView.findViewById(R.id.textView1);
            holder.h4 =  convertView.findViewById(R.id.textView2);
            holder.h1 =  convertView.findViewById(R.id.textView3);
            holder.m15 =  convertView.findViewById(R.id.textView4);
            holder.vol =  convertView.findViewById(R.id.textView5);
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

//            if (now > a_10h){
//                h10 = (now /a_10h) * 100 - 100;
//                String s = String.format("%.2f", h10);
//                h10 = Float.parseFloat(s);
//            }else{
//                h10 = -(a_10h / now) * 100 + 100;
//                String s = String.format("%.2f", h10);
//                h10 = Float.parseFloat(s);
//            }

            result = convertView;

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.name.setText(symbol.getName());

        if(symbol.get10h()>= 0){
            holder.h10.setTextColor(Color.parseColor("#26a69a"));
        }else{
            holder.h10.setTextColor(Color.parseColor("#ef5350"));
        }

        if(symbol.get4h()>= 0){
            holder.h4.setTextColor(Color.parseColor("#26a69a"));
        }else{
            holder.h4.setTextColor(Color.parseColor("#ef5350"));
        }

        if(symbol.get1h()>= 0){
            holder.h1.setTextColor(Color.parseColor("#26a69a"));
        }else{
            holder.h1.setTextColor(Color.parseColor("#ef5350"));
        }

        if(symbol.getvol()>= 0){
            holder.vol.setTextColor(Color.parseColor("#26a69a"));
        }else{
            holder.vol.setTextColor(Color.parseColor("#ef5350"));
        }

        holder.h10.setText("" + symbol.get10h());
        holder.h4.setText("" +  symbol.get4h());
        holder.h1.setText("" +   symbol.get1h() );
        holder.m15.setText("" +  symbol.getNow());
        holder.vol.setText("" +  symbol.getvol());


        return convertView;
    }
}

























