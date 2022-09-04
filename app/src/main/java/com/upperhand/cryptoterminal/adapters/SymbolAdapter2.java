package com.upperhand.cryptoterminal.adapters;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.symbol;

import java.util.ArrayList;



public class SymbolAdapter2 extends ArrayAdapter<symbol> {

    Context mContext;
    private int mResource;

    private static class ViewHolder {
        TextView name;
        TextView vol;
        TextView m15;
        TextView h4;
        TextView h10;
        TextView price;

    }

    public SymbolAdapter2(Context context, int resource, ArrayList<symbol> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String name = getItem(position).getName();
        float a_15m = getItem(position).get15m();
        float a_4h = getItem(position).get4h();
        float a_10h = getItem(position).get10h();
        float vol = getItem(position).getvol();
        float now = getItem(position).getNow();


        symbol symbol = new symbol(name,now,a_15m,a_4h,a_10h,vol);

        final View result;
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name =  convertView.findViewById(R.id.textView1);
            holder.h10 =  convertView.findViewById(R.id.textView2);
            holder.h4 =  convertView.findViewById(R.id.textView3);
            holder.m15 =  convertView.findViewById(R.id.textView4);
            holder.price =  convertView.findViewById(R.id.textView5);
//            holder.vol =  convertView.findViewById(R.id.textView6);

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

        if(symbol.get15m()>= 0){
            holder.m15.setTextColor(Color.parseColor("#26a69a"));
        }else{
            holder.m15.setTextColor(Color.parseColor("#ef5350"));
        }

        if(symbol.getvol()>= 1){
            holder.vol.setTextColor(Color.parseColor("#26a69a"));
        }else{
            holder.vol.setTextColor(Color.parseColor("#ef5350"));
        }



        holder.h10.setText("" + symbol.get10h());
        holder.h4.setText("" +  symbol.get4h());
        holder.m15.setText("" +   symbol.get15m());
        holder.vol.setText("" +  symbol.getvol());

        holder.vol.setBackgroundResource(0);
        holder.m15.setBackgroundResource(0);

        if (symbol.getvol() > 1.3 && symbol.get15m() > 0.3){
           holder.vol.setBackgroundResource(R.color.green_light);
            holder.m15.setBackgroundResource(R.color.green_light);
        }


        if (now > 1000){
            holder.price.setText("" +(int)now);
        }else{
            holder.price.setText("" + now);
        }


        return convertView;
    }
}

























