package com.upperhand.cryptoterminal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.word;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class wordsAdapter extends ArrayAdapter<word>  {

    Context mContext;
    private int mResource;
    private boolean isVolume;
    int numberElements;
    int count;
    ArrayList<String> listHours;

    private class ViewHolder {
        TextView name1;
        TextView name2;
        TextView name3;
        TextView name4;
        BarChart chart;
    }

    public wordsAdapter(Context context, int resource, ArrayList<word> objects, boolean vol) {
        super(context, resource, objects);
        this.mContext = context;
        this.isVolume = vol;
        mResource = resource;

        //   ===========================   CREATE HOURS ARRAY

        //region HOURS ARRAY
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        boolean half;
        numberElements = 101;
        count = 50;

        listHours = new ArrayList<String>();
        if (mins > 30){
            listHours.add(hours + ":30");
            half = true;
            if(mins < 45) {
                numberElements = 103;
                count = 51;
            }else {
                numberElements = 102;
                count = 50;
            }
        }else {
            listHours.add(hours + ":00");
            half = false;
            hours -=1;
            if(mins >= 15) {
                numberElements = 102;
            }
        }

        for (int i = 0; i < count; i++) {
            if(half){
                half = !half;
                listHours.add(hours + ":00");
                hours -=1;
            }else{
                half = !half;
                listHours.add(hours + ":30");
            }
            if(hours < 0){
                hours = 23;
            }
        }
        Collections.reverse(listHours);

        //endregion
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


//    ==========================    CREATE DATA FROM TREND OBJECTS

        int avg;
        float scale;
        ArrayList<Integer> numbers;
        if(!isVolume) {
            numbers = getItem(position).getNumbers();
            avg = getItem(position).get_av();
            scale = 0.8f;
        }else {
            numbers = getItem(position).getNumbers_vol();
            avg = getItem(position).get_av_vol();
            scale = 0.2f;
        }
        ArrayList<String> words = getItem(position).getWords();
        numbers = new ArrayList<>(numbers.subList(numbers.size()-Math.min(numbers.size(),numberElements), numbers.size()));

        //   ============   BARCHART DATA

        ArrayList data_list = new ArrayList();
        for (int i = 0; i < numbers.size(); i++) {
            int entry = numbers.get(i) - (int)(avg* scale);
            if (entry < 0){
                entry = 0;
            }

            data_list.add(new BarEntry((float)(i*0.5),entry));
        }

        BarDataSet bardataset = new BarDataSet(data_list, "ooo");
        BarData data = new BarData(  bardataset);
        bardataset.setDrawValues(false);
        bardataset.setColors(ColorTemplate.PASTEL_COLORS);
        data.setBarWidth(0.5f);

        //        ============   CREATE HOLDER VIEW

        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder.chart = convertView.findViewById(R.id.chart1);
            holder.name1 = convertView.findViewById(R.id.name1);
            holder.name2 = convertView.findViewById(R.id.name2);
            holder.name3 = convertView.findViewById(R.id.name3);
            holder.name4 = convertView.findViewById(R.id.name4);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        int startColor = ContextCompat.getColor(convertView.getContext(), R.color.black70);
        int endColor = ContextCompat.getColor(convertView.getContext(), R.color.blue);
        bardataset.setGradientColor(startColor, endColor);

        // ============ SETTING KEYWORDS TEXTVIEWS

        int size = words.size();
        try {
            holder.name1.setText(words.get(0));
            holder.name2.setText(words.get(1));
            if(size > 2){
                holder.name3.setVisibility(View.VISIBLE);
                holder.name3.setText(words.get(2));
            }else {
                holder.name3.setVisibility(View.GONE);
            }
            if(size > 3){
                holder.name4.setVisibility(View.VISIBLE);
                holder.name4.setText(words.get(3));
            }else {
                holder.name4.setVisibility(View.GONE);
            }
        }catch (Exception e){
        }

        //=================   VISUAL

        holder.chart.animateY(500);
        holder.chart.setData(data);

        Legend l =  holder.chart.getLegend();
        l.setEnabled(false);
        holder.chart.getAxisLeft().setDrawLabels(false);
        holder.chart.getAxisRight().setDrawLabels(false);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setVisibleXRangeMaximum(15);
        holder.chart.getAxisRight().setDrawGridLines(false);
        holder.chart.getAxisLeft().setDrawGridLines(false);
        holder.chart.setViewPortOffsets(0f, 0f, 0f, -20f);
        holder.chart.moveViewToX(180);
        holder.chart.setExtraRightOffset(30);
        holder.chart.setScaleEnabled(false);


        //=================   SET HOURS ON TOP

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return listHours.get((int) value);
            }
        });
        xAxis.setAxisMinimum(0.5f);
        xAxis.setYOffset(-15);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.gray_super_light));


        return convertView;
    }


}

























