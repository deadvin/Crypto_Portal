package com.upperhand.cryptoterminal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.word;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    Context mContext;
    int numberElements;
    int count;
    ArrayList<String> listHours;
    ArrayList<word> wordsList;

    public WordsAdapter(Context context, ArrayList<word> wordsList) {
        this.mContext = context;
        this.wordsList = wordsList;
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }
    
    private void createHoursArray(){

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        numberElements = 48;
        count = 24;

        listHours = new ArrayList<String>();

        hours -= 1;
        if (hours < 0) {
            hours = 23;
        }
        for (int i = 0; i < count; i++) {
            listHours.add(hours + ":00");
            hours -= 2;
            if (hours == -2) {
                hours = 22;
            } else if (hours == -1) {
                hours = 23;
            }
        }
        Collections.reverse(listHours);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_words, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        createHoursArray();

        int average;
        float scale;
        ArrayList<Integer> numbers;

        numbers = wordsList.get(position).getNumbers();
        average =  wordsList.get(position).getAv();
        scale = 0.8f;

        ArrayList<String> words = wordsList.get(position).getWords();
        numbers = new ArrayList<>(numbers.subList(numbers.size()-Math.min(numbers.size(),numberElements), numbers.size()));

        //   ============  CREATE BARDATASET

        ArrayList data_list = new ArrayList();
        for (int i = 0; i < numbers.size(); i++) {
            int entry = numbers.get(i) - (int)(average * scale);
            if (entry < 0){
                entry = 0;
            }

            data_list.add(new BarEntry((float)(i*0.5),entry));
        }

        LineDataSet linedatasetFlat = new LineDataSet(data_list,"");
        linedatasetFlat.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets_flat = new ArrayList<>();
        dataSets_flat.add(linedatasetFlat);
        linedatasetFlat.setDrawValues(false);
        linedatasetFlat.setColor(mContext.getResources().getColor(R.color.blue));
        linedatasetFlat.setLineWidth(1);
        linedatasetFlat.setDrawCircles(true);

        LineData data_flat = new LineData(dataSets_flat);

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

//        =========== SET FILL COLOUR

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_blue);
        linedatasetFlat.setFillDrawable(drawable);
        linedatasetFlat.setDrawFilled(true);

//        ================  SET DATA

        holder.chart.setData(data_flat);
        holder.chart.invalidate();
        holder.chart.animateY(500);
        holder.chart.setData(data_flat);

//        ================  VISUAL

        Legend legendFlat = holder.chart.getLegend();
        legendFlat.setEnabled(false);
        holder.chart.getAxisLeft().setDrawLabels(false);
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setVisibleXRangeMaximum(10);
        holder.chart.moveViewToX(180);
        holder.chart.setScaleEnabled(false);


        holder.chart.getAxisRight().setEnabled(false);
        holder.chart.setViewPortOffsets(10f, 10f, 0f, 10f);
        holder.chart.getAxisRight().setDrawGridLines(false);
        holder.chart.getAxisLeft().setEnabled(true);
        holder.chart.getAxisLeft().setLabelCount(0, true);
        holder.chart.getAxisLeft().setDrawZeroLine(true);
        holder.chart.getDescription().setEnabled(false);


        //=================   SET HOURS ON TOP

        XAxis xAxis = holder.chart.getXAxis();
        try {
                 xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return listHours.get((int) value);
            }
        });
        }catch (Exception e){
        }

        xAxis.setAxisMinimum(0.5f);
        xAxis.setYOffset(-15);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.gray_super_light));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name1;
        TextView name2;
        TextView name3;
        TextView name4;
        LineChart chart;

        ViewHolder(View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.chart1);
            name1 = itemView.findViewById(R.id.name1);
            name2 = itemView.findViewById(R.id.name2);
            name3 = itemView.findViewById(R.id.name3);
            name4 = itemView.findViewById(R.id.name4);
        }
    }
}