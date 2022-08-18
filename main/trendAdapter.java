package com.upperhand.cryptoterminal.objects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.upperhand.cryptoterminal.R;
import java.util.ArrayList;
import java.util.Random;


public class trendAdapter extends ArrayAdapter<trend> {

    Context mContext;
    private int mResource;

    private class ViewHolder {
        TextView name;
        BarChart chart;
    }

    public trendAdapter(Context context, int resource, ArrayList<trend> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name_str = getItem(position).getName();
        ArrayList<Integer> numbers = getItem(position).getNumbers();
        ArrayList<Integer> numbers_vol = getItem(position).getNumbers_vol();

        trend trend = new trend(name_str,numbers,numbers_vol);
        ArrayList data_list = new ArrayList();

        ArrayList<Integer> num_array = trend.getNumbers();
        int sum = 0;
        for(int i = 0; i < num_array.size(); i++)
            sum = sum + num_array.get(i);
        float av = sum/num_array.size();
        Log.e("asd", String.valueOf(av) + trend.getName());

        for (int i = 0; i < num_array.size(); i++) {
            data_list.add(new BarEntry((float)(i*0.5),(int)(num_array.get(i)/av)));
        }


        BarDataSet bardataset = new BarDataSet(data_list, "");
        BarData data = new BarData(bardataset);
        bardataset.setDrawValues(false);
        bardataset.setColors(ColorTemplate.PASTEL_COLORS);
        data.setBarWidth(0.5f);

        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder.chart = convertView.findViewById(R.id.chart1);
            holder.name = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        int startColor = ContextCompat.getColor(convertView.getContext(), android.R.color.black);
        int endColor = ContextCompat.getColor(convertView.getContext(), android.R.color.holo_blue_bright);
        bardataset.setGradientColor(startColor, endColor);

        holder.name.setText(name_str);

        holder.chart.animateY(500);
        holder.chart.setData(data);

        //=================   VISUAL

        Legend l =  holder.chart.getLegend();
        l.setEnabled(false);
        holder.chart.getAxisLeft().setDrawLabels(false);
        holder.chart.getAxisRight().setDrawLabels(false);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.getXAxis().setDrawAxisLine(false);
        holder.chart.setVisibleXRangeMaximum(15);
        holder.chart.getXAxis().setDrawGridLines(false);

//        holder.chart.setVisibleXRangeMaximum(50);
//        holder.chart.moveViewToX(500);

        holder.chart.getAxisRight().setDrawGridLines(false);
        holder.chart.getAxisLeft().setDrawGridLines(false);
        holder.chart.getXAxis().setDrawGridLines(false);

        holder.chart.setViewPortOffsets(0f, 0f, 0f, -20f);


        return convertView;
    }
}

























